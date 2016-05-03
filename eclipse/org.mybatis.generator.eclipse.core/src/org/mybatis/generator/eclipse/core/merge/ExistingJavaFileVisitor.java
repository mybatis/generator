/*
 *  Copyright 2008 The Apache Software Foundation
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * @author Jeff Butler
 * 
 */
public class ExistingJavaFileVisitor extends ASTVisitor {
    private TypeDeclaration typeDeclaration;
    private String[] javadocTags;
    private List<String> generatedInnerClassesToKeep;
    private Map<String, List<Annotation>> fieldAnnotations;
    private Map<String, List<Annotation>> methodAnnotations;

    /**
     * 
     */
    public ExistingJavaFileVisitor(String[] javadocTags) {
        super();
        this.javadocTags = javadocTags;
        generatedInnerClassesToKeep = new ArrayList<String>();
        fieldAnnotations = new HashMap<String, List<Annotation>>();
        methodAnnotations = new HashMap<String, List<Annotation>>();
    }

    /**
     * Find the generated fields and delete them
     */
    @Override
    public boolean visit(FieldDeclaration node) {
        if (isGenerated(node)) {
            List<Annotation> annotations = retrieveAnnotations(node);
            if (!annotations.isEmpty()) {
                VariableDeclarationFragment variable = (VariableDeclarationFragment) node
                        .fragments().get(0);
                fieldAnnotations.put(variable.getName().getIdentifier(),
                        annotations);
            }
            node.delete();
        }

        return false;
    }

    /**
     * Find the generated methods and delete them
     */
    @Override
    public boolean visit(MethodDeclaration node) {
        if (isGenerated(node)) {
            List<Annotation> annotations = retrieveAnnotations(node);
            if (!annotations.isEmpty()) {
                String methodSignature = EclipseDomUtils
                        .getMethodSignature(node);
                methodAnnotations.put(methodSignature, annotations);
            }
            node.delete();
        }

        return false;
    }

    /**
     * Find any generated inner types and delete them
     */
    @Override
    public boolean visit(TypeDeclaration node) {
        // make sure we only pick up the top level type
        if (node.getParent().getNodeType() == ASTNode.COMPILATION_UNIT) {
            typeDeclaration = node;
            return true;
        } else {
            // is this a generated inner class? If so, then delete
            if (isGenerated(node)) {
                node.delete();
            }

            return false;
        }
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        if (isGenerated(node)) {
            node.delete();
        }

        return false;
    }

    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

    @SuppressWarnings("unchecked")
    private boolean isGenerated(BodyDeclaration node) {
        boolean rc = false;
        Javadoc jd = node.getJavadoc();
        if (jd != null) {
            List<TagElement> tags = jd.tags();
            for (TagElement tag : tags) {
                String tagName = tag.getTagName();
                if (tagName == null) {
                    continue;
                }
                for (String javadocTag : javadocTags) {
                    if (tagName.equals(javadocTag)) {
                        String string = tag.toString();
                        if (string.contains("do_not_delete_during_merge")) {
                            if (node.getNodeType() == ASTNode.TYPE_DECLARATION) {
                                String name = ((TypeDeclaration) node)
                                        .getName().getFullyQualifiedName();
                                generatedInnerClassesToKeep.add(name);
                            }
                        } else {
                            rc = true;
                        }
                        break;
                    }
                }
            }
        }

        return rc;
    }

    public boolean containsInnerClass(String name) {
        return generatedInnerClassesToKeep.contains(name);
    }

    private List<Annotation> retrieveAnnotations(BodyDeclaration node) {
        List<?> modifiers = node.modifiers();
        List<Annotation> annotations = new ArrayList<Annotation>();
        for (Object modifier : modifiers) {
            if (modifier instanceof Annotation) {
                annotations.add((Annotation) modifier);
            }
        }
        return annotations;
    }

    public List<Annotation> getFieldAnnotations(
            FieldDeclaration fieldDeclaration) {
        VariableDeclarationFragment variable = (VariableDeclarationFragment) fieldDeclaration
                .fragments().get(0);
        return fieldAnnotations.get(variable.getName().getIdentifier());
    }

    public List<Annotation> getMethodAnnotations(
            MethodDeclaration methodDeclaration) {
        return methodAnnotations.get(EclipseDomUtils.getMethodSignature(methodDeclaration));
    }
}
