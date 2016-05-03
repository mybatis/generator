/*
 *  Copyright 2005, 2006, 2008 The Apache Software Foundation
 *  Copyright 2012 The MyBatis.org Team
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.eclipse.core.merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.mybatis.generator.exception.ShellException;

/**
 * This class handles the task of merging changes into an existing Java file.
 * 
 * This class makes several assumptions about the structure of the new and
 * existing files, including:
 * 
 * <ul>
 * <li>The imports of both files are fully qualified (no wildcard imports)</li>
 * <li>The super interfaces of both files are NOT fully qualified</li>
 * <li>The super classes of both files are NOT fully qualified</li>
 * </ul>
 * 
 * @author Jeff Butler
 * @author Tomas Neuberg
 */
public class JavaFileMerger {

    private String newJavaSource;
    private String existingFilePath;
    private String[] javaDocTags;
    private String fileEncoding;

    public JavaFileMerger(String newJavaSource, String existingFilePath,
            String[] javaDocTags, String fileEncoding) {
        super();
        this.newJavaSource = newJavaSource;
        this.existingFilePath = existingFilePath;
        this.javaDocTags = javaDocTags;
        this.fileEncoding = fileEncoding;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getMergedSource() throws ShellException {
        ASTParser astParser = ASTParser.newParser(AST.JLS3);
        NewJavaFileVisitor newJavaFileVisitor = visitNewJavaFile(astParser);

        String existingFile = getExistingFileContents();
        IDocument document = new Document(existingFile);

        // delete generated stuff, and collect imports
        ExistingJavaFileVisitor visitor = new ExistingJavaFileVisitor(
                javaDocTags);

        astParser.setSource(existingFile.toCharArray());
        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        AST ast = cu.getAST();
        cu.recordModifications();
        cu.accept(visitor);

        TypeDeclaration typeDeclaration = visitor.getTypeDeclaration();
        if (typeDeclaration == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("No types defined in the file ");
            sb.append(existingFilePath);

            throw new ShellException(sb.toString());
        }

        // reconcile the superinterfaces
        List<Type> newSuperInterfaces = getNewSuperInterfaces(
                typeDeclaration.superInterfaceTypes(), newJavaFileVisitor);
        for (Type newSuperInterface : newSuperInterfaces) {
            typeDeclaration.superInterfaceTypes().add(
                    ASTNode.copySubtree(ast, newSuperInterface));
        }

        // set the superclass
        if (newJavaFileVisitor.getSuperclass() != null) {
            typeDeclaration.setSuperclassType((Type) ASTNode.copySubtree(ast,
                    newJavaFileVisitor.getSuperclass()));
        } else {
            typeDeclaration.setSuperclassType(null);
        }

        // interface or class?
        if (newJavaFileVisitor.isInterface()) {
            typeDeclaration.setInterface(true);
        } else {
            typeDeclaration.setInterface(false);
        }

        // reconcile the imports
        List<ImportDeclaration> newImports = getNewImports(cu.imports(),
                newJavaFileVisitor);
        for (ImportDeclaration newImport : newImports) {
            Name name = ast
                    .newName(newImport.getName().getFullyQualifiedName());
            ImportDeclaration newId = ast.newImportDeclaration();
            newId.setName(name);
            cu.imports().add(newId);
        }

        TextEdit textEdit = cu.rewrite(document, null);
        try {
            textEdit.apply(document);
        } catch (BadLocationException e) {
            throw new ShellException(
                    "BadLocationException removing prior fields and methods");
        }

        // regenerate the CompilationUnit to reflect all the deletes and changes
        astParser.setSource(document.get().toCharArray());
        CompilationUnit strippedCu = (CompilationUnit) astParser
                .createAST(null);

        // find the top level public type declaration
        TypeDeclaration topLevelType = null;
        Iterator iter = strippedCu.types().iterator();
        while (iter.hasNext()) {
            TypeDeclaration td = (TypeDeclaration) iter.next();
            if (td.getParent().equals(strippedCu)
                    && (td.getModifiers() & Modifier.PUBLIC) > 0) {
                topLevelType = td;
                break;
            }
        }

        // now add all the new methods and fields to the existing
        // CompilationUnit with a ListRewrite
        ASTRewrite rewrite = ASTRewrite.create(topLevelType.getRoot().getAST());
        ListRewrite listRewrite = rewrite.getListRewrite(topLevelType,
                TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        Iterator<ASTNode> astIter = newJavaFileVisitor.getNewNodes().iterator();
        int i = 0;
        while (astIter.hasNext()) {
            ASTNode node = astIter.next();

            if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
                String name = ((TypeDeclaration) node).getName()
                        .getFullyQualifiedName();
                if (visitor.containsInnerClass(name)) {
                    continue;
                }
            } else if (node instanceof FieldDeclaration) {
                addExistsAnnotations((BodyDeclaration) node,
                        visitor.getFieldAnnotations((FieldDeclaration) node));
            } else if (node instanceof MethodDeclaration) {
                addExistsAnnotations((BodyDeclaration) node,
                        visitor.getMethodAnnotations((MethodDeclaration) node));
            }

            listRewrite.insertAt(node, i++, null);
        }

        textEdit = rewrite.rewriteAST(document, JavaCore.getOptions());
        try {
            textEdit.apply(document);
        } catch (BadLocationException e) {
            throw new ShellException(
                    "BadLocationException adding new fields and methods");
        }

        String newSource = document.get();
        return newSource;
    }

    private List<Type> getNewSuperInterfaces(
            List<Type> existingSuperInterfaces,
            NewJavaFileVisitor newJavaFileVisitor) {

        List<Type> answer = new ArrayList<Type>();

        for (Type newSuperInterface : newJavaFileVisitor
                .getSuperInterfaceTypes()) {
            boolean found = false;
            for (Type existingSuperInterface : existingSuperInterfaces) {
                found = EclipseDomUtils.typesMatch(newSuperInterface,
                        existingSuperInterface);
                if (found) {
                    break;
                }
            }

            if (!found) {
                answer.add(newSuperInterface);
            }
        }

        return answer;
    }

    private List<ImportDeclaration> getNewImports(
            List<ImportDeclaration> existingImports,
            NewJavaFileVisitor newJavaFileVisitor) {
        List<ImportDeclaration> answer = new ArrayList<ImportDeclaration>();

        for (ImportDeclaration newImport : newJavaFileVisitor.getImports()) {
            boolean found = false;
            for (ImportDeclaration existingImport : existingImports) {
                found = EclipseDomUtils.importDeclarationsMatch(newImport,
                        existingImport);
                if (found) {
                    break;
                }
            }

            if (!found) {
                answer.add(newImport);
            }
        }

        return answer;
    }

    /**
     * This method parses the new Java file and returns a filled out
     * NewJavaFileVisitor. The returned visitor can be used to determine
     * characteristics of the new file, and a lost of new nodes that need to be
     * incorporated into the existing file.
     * 
     * @param astParser
     * @return
     */
    private NewJavaFileVisitor visitNewJavaFile(ASTParser astParser) {
        astParser.setSource(newJavaSource.toCharArray());
        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        NewJavaFileVisitor newVisitor = new NewJavaFileVisitor();
        cu.accept(newVisitor);

        return newVisitor;
    }

    private String getExistingFileContents() throws ShellException {
        File file = new File(existingFilePath);

        if (!file.exists()) {
            // this should not happen because MyBatis Generator only returns the
            // path
            // calculated by the eclipse callback
            StringBuilder sb = new StringBuilder();
            sb.append("The file ");
            sb.append(existingFilePath);
            sb.append(" does not exist");
            throw new ShellException(sb.toString());
        }

        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr;
            if (fileEncoding == null) {
                isr = new InputStreamReader(fis);
            } else {
                isr = new InputStreamReader(fis, fileEncoding);
            }
            BufferedReader br = new BufferedReader(isr);
            char[] buffer = new char[1024];
            int returnedBytes = br.read(buffer);
            while (returnedBytes != -1) {
                sb.append(buffer, 0, returnedBytes);
                returnedBytes = br.read(buffer);
            }

            br.close();
            return sb.toString();
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("IOException reading the file ");
            sb.append(existingFilePath);
            throw new ShellException(sb.toString(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private void addExistsAnnotations(BodyDeclaration node,
            List<Annotation> oldAnnotations) {
        Set<String> newAnnotationTypes = new HashSet<String>();
        int lastAnnotationIndex = 0;
        int idx = 0;
        for (Object modifier : node.modifiers()) {
            if (modifier instanceof Annotation) {
                Annotation newAnnotation = (Annotation) modifier;
                newAnnotationTypes.add(newAnnotation.getTypeName()
                        .getFullyQualifiedName());
                lastAnnotationIndex = idx;
            }
            idx++;
        }
        
        if (oldAnnotations != null) {
            for (Annotation oldAnnotation : oldAnnotations) {
                if (newAnnotationTypes.contains(oldAnnotation.getTypeName()
                        .getFullyQualifiedName()))
                    continue;

                AST nodeAst = node.getAST();
                node.modifiers().add(lastAnnotationIndex++,
                        ASTNode.copySubtree(nodeAst, oldAnnotation));
            }
        }
    }
}
