/**
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.ShellException;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author wang.jiyong
 */
public class JavaFileMerger {

    private static Map<Class<? extends BodyDeclaration<?>>, Integer> membersWeight = new HashMap<>();

    static {
        membersWeight.put(AnnotationDeclaration.class, 200);
        membersWeight.put(AnnotationMemberDeclaration.class, 210);

        membersWeight.put(EnumDeclaration.class, 300);
        membersWeight.put(EnumConstantDeclaration.class, 310);

        membersWeight.put(FieldDeclaration.class, 10);
        membersWeight.put(InitializerDeclaration.class, 20);
        membersWeight.put(ConstructorDeclaration.class, 30);
        membersWeight.put(MethodDeclaration.class, 40);
        // inner class
        membersWeight.put(ClassOrInterfaceDeclaration.class, 100);
    }




    public String mergeJavaFile(String newFileSource,
                                InputStream existingFileInputStream, final String publicClassName, String fileEncoding)
            throws ShellException {

        ParserConfiguration pc = new ParserConfiguration();
        StaticJavaParser.setConfiguration(pc);
        CompilationUnit newCompilationUnit;
        CompilationUnit oldCompilationUnit;
        if (fileEncoding == null || fileEncoding.isEmpty()) {
            fileEncoding = StandardCharsets.UTF_8.name();
        }
        pc.setCharacterEncoding(Charset.forName(fileEncoding));

        oldCompilationUnit = StaticJavaParser.parse(existingFileInputStream);
        newCompilationUnit = StaticJavaParser.parse(newFileSource);

        Optional<ClassOrInterfaceDeclaration> newPublicClass = newCompilationUnit.getClassByName(publicClassName);
        Optional<ClassOrInterfaceDeclaration> oldPublicClass = oldCompilationUnit.getClassByName(publicClassName);
        if (!newPublicClass.isPresent()){
            newPublicClass = newCompilationUnit.getInterfaceByName(publicClassName);
        }
        if (!oldPublicClass.isPresent()){
            oldPublicClass = oldCompilationUnit.getInterfaceByName(publicClassName);
        }
        if (!newPublicClass.isPresent() || !oldPublicClass.isPresent()) {
            throw new ShellException(getString("ValidationError.29", publicClassName));
        }
        ClassOrInterfaceDeclaration newPublicClassDeclaration = newPublicClass.get();
        ClassOrInterfaceDeclaration oldPublicClassDeclaration = oldPublicClass.get();

        ClassMembersVisitor visitor = new ClassMembersVisitor(publicClassName);

        //step 1. delete all auto generator members in the existing file.
        List<BodyDeclaration<?>> oldMembers = new ArrayList<>();
        //get all public class members
        visitor.visit(oldCompilationUnit, oldMembers);
        for (BodyDeclaration<?> oldMember : oldMembers) {
            if (isGenerated(oldMember)) {
                oldMember.remove();
            }
        }

        //step 2. get all members in the new java source file.
        List<BodyDeclaration<?>> newMembers = new ArrayList<>();
        visitor.visit(newCompilationUnit, newMembers);

        //step 3. add the members to existing file.
        newMembers.addAll(oldPublicClassDeclaration.getMembers());
        sortMembers(newMembers);
        oldPublicClassDeclaration.setMembers(new NodeList<>(newMembers));

        //step 4. resolve import.
        //just add import statement,not delete import statement in existingFile. because It's not worth it.
        Set<String> oldImportSet = new HashSet<>();
        for (ImportDeclaration oldImport : oldCompilationUnit.getImports()) {
            oldImportSet.add(oldImport.getNameAsString());
        }
        for (ImportDeclaration newImport : newCompilationUnit.getImports()) {
            if (!oldImportSet.contains(newImport.getNameAsString())) {
                oldCompilationUnit.addImport(newImport);
            }
        }

        //step 5. resolve class annotation.
        Set<String> oldAnnotationSet = new HashSet<>();
        for (AnnotationExpr annotation : oldPublicClassDeclaration.getAnnotations()) {
            oldAnnotationSet.add(annotation.getNameAsString());
        }
        for (AnnotationExpr newAnnotation : newPublicClassDeclaration.getAnnotations()) {
            if (!oldAnnotationSet.contains(newAnnotation.getNameAsString())) {
                oldPublicClassDeclaration.addAnnotation(newAnnotation);
            }
        }
        // pretty print the result
        return prettyPrint(oldCompilationUnit);
    }

    private void sortMembers(List<BodyDeclaration<?>> members) {
        int length = members.size();
        int[] arrays = new int[length];
        for (int i = 0; i < length; i++) {
            arrays[i] = calculateWeight(members.get(i));
        }
        //Insert sort
        int i, j;
        for (i = 1; i < length; i++) {
            int key = arrays[i];
            BodyDeclaration<?> rKey = members.get(i);
            for (j = i - 1; j >= 0 && arrays[j] > key; j--) {
                arrays[j + 1] = arrays[j];
                members.set(j + 1, members.get(j));
            }
            arrays[j + 1] = key;
            members.set(j + 1, rKey);
        }
    }



    public String prettyPrint(CompilationUnit compilationUnit) {
        PrettyPrinterConfiguration conf = new PrettyPrinterConfiguration();
        PrettyPrinter prettyPrinter = new PrettyPrinter(conf);
        return prettyPrinter.print(compilationUnit);
    }

    //find all members of the public class.
    public static class ClassMembersVisitor extends VoidVisitorAdapter<List<BodyDeclaration<?>>> {
        private String publicClassName;

        public ClassMembersVisitor(String publicClassName) {
            this.publicClassName = publicClassName;
        }

        @Override
        public void visit(MethodDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(ConstructorDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(EnumConstantDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(EnumDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(AnnotationDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(AnnotationMemberDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(FieldDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        @Override
        public void visit(InitializerDeclaration n, List<BodyDeclaration<?>> nodes) {
            super.visit(n, nodes);
            if (isBodyDeclaration(n)) {
                nodes.add(n);
            }
        }

        private boolean isBodyDeclaration(BodyDeclaration<?> bodyDeclaration) {
            Optional<Node> parentNode = bodyDeclaration.getParentNode();
            if (parentNode.isPresent()) {
                Node node = parentNode.get();
                return node instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) node).getName().asString().equals(publicClassName);
            }
            return false;
        }
    }

    private boolean isGenerated(BodyDeclaration<?> bodyDeclaration) {
        Optional<Comment> comment = bodyDeclaration.getComment();
        if (comment.isPresent()) {
            String commentData = comment.get().getContent();
            return MergeConstants.commentContainsTag(commentData);
        }
        return false;
    }
    private int calculateWeight(BodyDeclaration<?> o1) {
        int i = isGenerated(o1) ? 0 : 1;
        return membersWeight.getOrDefault(o1.getClass(), 1000) + i;
    }

}
