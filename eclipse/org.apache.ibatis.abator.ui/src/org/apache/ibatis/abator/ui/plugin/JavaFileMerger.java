/*
 *  Copyright 2005, 2006 The Apache Software Foundation
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
package org.apache.ibatis.abator.ui.plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.ibatis.abator.api.GeneratedJavaFile;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.exception.ShellException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

/**
 * This class handles the task of merging changes into an existing Java file.
 * 
 * @author Jeff Butler
 */
public class JavaFileMerger {

    private GeneratedJavaFile generatedJavaFile;

    private IFile existingFile;

    private class GatherNewItemsVisitor extends ASTVisitor {
        private List newNodes;

        /**
         * 
         */
        public GatherNewItemsVisitor() {
            super();
            newNodes = new ArrayList();
        }

        public boolean visit(FieldDeclaration node) {
            newNodes.add(node);

            return false;
        }

        public boolean visit(MethodDeclaration node) {
            newNodes.add(node);

            return false;
        }
        
        public boolean visit(TypeDeclaration node) {
            // make sure we don't pick up the top level class
            if (node.getParent().getNodeType() == ASTNode.COMPILATION_UNIT) {
                return true;
            } else {
                newNodes.add(node);
                return false;
            }
        }
        
        public List getNewNodes() {
            return newNodes;
        }
    };

    private class ExistingJavaFileVisitor extends ASTVisitor {
        private TypeDeclaration typeDeclaration;

        /**
         * 
         */
        public ExistingJavaFileVisitor() {
            super();
        }

        /**
         * Find the Abator generated fields and delete them
         */
        public boolean visit(FieldDeclaration node) {
            if (isAbatorGenerated(node)) {
                node.delete();
            }

            return false;
        }

        /**
         * Find the Abator generated methods and delete them
         */
        public boolean visit(MethodDeclaration node) {
            if (isAbatorGenerated(node)) {
                node.delete();
            }

            return false;
        }

        public boolean visit(TypeDeclaration node) {
            // make sure we only pick up the top level type
            if (node.getParent().getNodeType() == ASTNode.COMPILATION_UNIT) {
                typeDeclaration = node;
                return true;
            } else {
                // is this an Abator generated inner class?  If so, then delete
                if (isAbatorGenerated(node)) {
                    node.delete();
                }

                return false;
            }
        }

        public TypeDeclaration getTypeDeclaration() {
            return typeDeclaration;
        }
        
        private boolean isAbatorGenerated(BodyDeclaration node) {
            boolean rc = false;
            Javadoc jd = node.getJavadoc();
            if (jd != null) {
                List tags = jd.tags();
                Iterator tagIterator = tags.iterator();
                while (tagIterator.hasNext()) {
                    TagElement tag = (TagElement) tagIterator.next();
                    String tagName = tag.getTagName();
                    if ("@abatorgenerated".equals(tagName)) { //$NON-NLS-1$
                        rc = true;
                        break;
                    }
                }
            }
            
            return rc;
        }
    };

    /**
     * 
     */
    public JavaFileMerger(GeneratedJavaFile generatedJavaFile,
            IFile existingFile) {
        super();
        this.generatedJavaFile = generatedJavaFile;
        this.existingFile = existingFile;
    }

    public String getMergedSource() throws ShellException {
        ASTParser astParser = ASTParser.newParser(AST.JLS3);

        ICompilationUnit icu = JavaCore.createCompilationUnitFrom(existingFile);
        IDocument document;
        try {
            document = new Document(icu.getSource());
        } catch (CoreException e) {
            throw new ShellException(e.getStatus().getMessage(), e);
        }

        // delete Abator generated stuff, and collect imports
        astParser.setSource(icu);
        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        AST ast = cu.getAST();

        ExistingJavaFileVisitor visitor = new ExistingJavaFileVisitor();

        cu.recordModifications();
        cu.accept(visitor);

        TypeDeclaration typeDeclaration = visitor.getTypeDeclaration();
        if (typeDeclaration == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("No types defined in the file ");
            sb.append(existingFile.getName());

            throw new ShellException(sb.toString());
        }

        // reconcile the superinterfaces
        List newSuperInterfaces = getNewSuperInterfaces(typeDeclaration
                .superInterfaceTypes());
        Iterator iter = newSuperInterfaces.iterator();
        while (iter.hasNext()) {
            FullyQualifiedJavaType newSuperInterface = (FullyQualifiedJavaType) iter
                    .next();
            typeDeclaration.superInterfaceTypes().add(
                    ast.newSimpleType(ast.newSimpleName(newSuperInterface
                            .getShortName())));
        }

        // set the superclass
        if (generatedJavaFile.getSuperClass() != null) {
            typeDeclaration.setSuperclassType(ast.newSimpleType(ast
                    .newSimpleName(generatedJavaFile.getSuperClass()
                            .getShortName())));
        } else {
            typeDeclaration.setSuperclassType(null);
        }

        // interface or class?
        if (generatedJavaFile.isJavaInterface()) {
            typeDeclaration.setInterface(true);
        } else {
            typeDeclaration.setInterface(false);
        }

        // reconcile the imports
        List newImports = getNewImports(cu.imports());
        iter = newImports.iterator();
        while (iter.hasNext()) {
            String[] newImport = (String[]) iter.next();
            ImportDeclaration newImportDeclaration = ast.newImportDeclaration();
            newImportDeclaration.setName(ast.newName(newImport));
            cu.imports().add(newImportDeclaration);
        }

        TextEdit textEdit = cu.rewrite(document, null);
        try {
            textEdit.apply(document);
        } catch (BadLocationException e) {
            throw new ShellException(
                    "BadLocationException removing prior fields and methods");
        }

        // regenerate the CompilationUnit to reflect all the deletes
        astParser.setSource(document.get().toCharArray());
        CompilationUnit strippedCu = (CompilationUnit) astParser
                .createAST(null);

        // find the top level public type declaration
        TypeDeclaration topLevelType = null;
        iter = strippedCu.types().iterator();
        while (iter.hasNext()) {
            TypeDeclaration td = (TypeDeclaration) iter.next();
            if (td.getParent().equals(strippedCu)
                    && (td.getModifiers() & Modifier.PUBLIC) > 0) {
                topLevelType = td;
                break;
            }
        }

        // Now parse all the new fields and methods, then gather the new
        // methods and fields with a visitor
        astParser.setSource(generatedJavaFile.getFormattedContent()
                .toCharArray());
        CompilationUnit newCu = (CompilationUnit) astParser.createAST(null);
        
        GatherNewItemsVisitor newVisitor = new GatherNewItemsVisitor();

        newCu.accept(newVisitor);

        // now add all the new methods and fields to the existing
        // CompilationUnit with a ListRewrite
        ASTRewrite rewrite = ASTRewrite.create(topLevelType.getRoot().getAST());
        ListRewrite listRewrite = rewrite.getListRewrite(topLevelType,
                TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        iter = newVisitor.getNewNodes().iterator();
        int i = 0;
        while (iter.hasNext()) {
            listRewrite.insertAt((ASTNode) iter.next(), i++, null);
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

    private List getNewSuperInterfaces(List existingInterfaces) {
        List answer = new ArrayList();

        Iterator newInterfaces = generatedJavaFile.getSuperInterfaceTypes()
                .iterator();
        while (newInterfaces.hasNext()) {
            FullyQualifiedJavaType newInterface = (FullyQualifiedJavaType) newInterfaces
                    .next();
            Iterator iter = existingInterfaces.iterator();
            boolean found = false;
            while (iter.hasNext()) {
                Type type = (Type) iter.next();
                if (type.isSimpleType()) {
                    SimpleType st = (SimpleType) type;

                    String s = st.getName().getFullyQualifiedName();

                    if (s.equals(newInterface.getShortName())) {
                        found = true;
                    }
                }
            }

            if (!found) {
                answer.add(newInterface);
            }
        }

        return answer;
    }

    private List getNewImports(List existingImports) {
        List answer = new ArrayList();

        Iterator newImports = generatedJavaFile.getImportedTypes().iterator();
        while (newImports.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) newImports
                    .next();
            if (fqjt.isExplicitlyImported()) {
                Iterator iter = existingImports.iterator();
                boolean found = false;
                while (iter.hasNext()) {
                    ImportDeclaration existingImport = (ImportDeclaration) iter
                            .next();
                    if (existingImport.getName().getFullyQualifiedName()
                            .equals(fqjt.getFullyQualifiedName())) {
                        found = true;
                    }
                }

                if (!found) {
                    answer.add(parseName(fqjt.getFullyQualifiedName()));
                }
            }
        }

        return answer;
    }

    private String[] parseName(String name) {
        StringTokenizer st = new StringTokenizer(name, "."); //$NON-NLS-1$

        String[] answer = new String[st.countTokens()];

        int i = 0;
        while (st.hasMoreTokens()) {
            answer[i++] = st.nextToken();
        }

        return answer;
    }
}
