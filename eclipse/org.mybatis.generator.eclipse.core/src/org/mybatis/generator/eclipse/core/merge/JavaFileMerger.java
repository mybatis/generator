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

import static org.mybatis.generator.eclipse.core.merge.EclipseDomUtils.getCompilationUnitFromSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
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
import org.mybatis.generator.eclipse.core.merge.InvalidExistingFileException.ErrorCode;
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
    private String existingJavaSource;
    private String[] javaDocTags;

    public JavaFileMerger(String newJavaSource, String existingJavaSource,
            String[] javaDocTags) {
        super();
        this.newJavaSource = newJavaSource;
        this.existingJavaSource = existingJavaSource;
        this.javaDocTags = javaDocTags;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getMergedSource() throws ShellException, InvalidExistingFileException {
        NewJavaFileVisitor newJavaFileVisitor = visitNewJavaFile();

        IDocument document = new Document(existingJavaSource);

        // delete generated stuff, and collect imports
        ExistingJavaFileVisitor visitor = new ExistingJavaFileVisitor(
                javaDocTags);

        CompilationUnit cu = getCompilationUnitFromSource(existingJavaSource);
        AST ast = cu.getAST();
        cu.recordModifications();
        cu.accept(visitor);

        TypeDeclaration typeDeclaration = visitor.getTypeDeclaration();
        if (typeDeclaration == null) {
            throw new InvalidExistingFileException(ErrorCode.NO_TYPES_DEFINED_IN_FILE);
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
        CompilationUnit strippedCu = getCompilationUnitFromSource(document.get());

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

        List<Type> answer = new ArrayList<>();

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
        List<ImportDeclaration> answer = new ArrayList<>();

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
     * @return
     */
    private NewJavaFileVisitor visitNewJavaFile() {
        CompilationUnit cu = getCompilationUnitFromSource(newJavaSource);
        NewJavaFileVisitor newVisitor = new NewJavaFileVisitor();
        cu.accept(newVisitor);

        return newVisitor;
    }

    @SuppressWarnings("unchecked")
    private void addExistsAnnotations(BodyDeclaration node,
            List<Annotation> oldAnnotations) {
        Set<String> newAnnotationTypes = new HashSet<>();
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
