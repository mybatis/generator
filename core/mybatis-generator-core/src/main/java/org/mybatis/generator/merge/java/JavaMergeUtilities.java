/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.merge.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class JavaMergeUtilities {
    private JavaMergeUtilities() {
        // utility class, no instances
    }

    /**
     * Compare two compilation units and find imports that are in the existing file but not in the new file.
     * We assume this means they are required for some custom method, so we will add them to the new
     * file if there are other items to merge. This may create unused imports in the new file if the
     * initial assumption is incorrect, but better safe than sorry.
     *
     * @param existingCompilationUnit compilation unit representing the existing file
     * @param newCompilationUnit compilation unit representing the new file
     */
    public static List<ImportDeclaration> findCustomImports(CompilationUnit existingCompilationUnit,
                                                            CompilationUnit newCompilationUnit) {
        List<ImportDeclaration> customImports = new ArrayList<>();

        List<String> newFileImports = newCompilationUnit.getImports().stream()
                .map(JavaMergeUtilities::stringify).toList();

        for (ImportDeclaration id : existingCompilationUnit.getImports()) {
            if (!newFileImports.contains(stringify(id))) {
                customImports.add(id);
            }
        }

        return customImports;
    }

    /**
     * Compare two members to see if they are "functionally equivalent". This is defined as:
     *
     * <ul>
     *     <li>Members are the same type</li>
     *     <li>Members have the same signature or basic declaration</li>
     * </ul>
     *
     * @param member1 the first member
     * @param member2 the second member
     * @return true if the members are functionally equivalent
     */
    public static boolean membersMatch(BodyDeclaration<?> member1, BodyDeclaration<?> member2) {
        if (member1.isTypeDeclaration() && member2.isTypeDeclaration()) {
            return member1.asTypeDeclaration().getNameAsString()
                    .equals(member2.asTypeDeclaration().getNameAsString());
        } else if (member1.isCallableDeclaration() && member2.isCallableDeclaration()) {
            return member1.asCallableDeclaration().getSignature().asString()
                    .equals(member2.asCallableDeclaration().getSignature().asString());
        } else if (member1.isFieldDeclaration() && member2.isFieldDeclaration()) {
            return stringify(member1.asFieldDeclaration()).equals(stringify(member2.asFieldDeclaration()));
        }

        return false;
    }

    public static List<ClassOrInterfaceType> findCustomSuperInterfaces(BodyDeclaration<?> existingType,
                                                                       BodyDeclaration<?> newType) {
        List<ClassOrInterfaceType> customSuperInterfaces = new ArrayList<>();

        List<String> newFileSuperInterfaces = findSuperInterfaces(newType).stream()
                .map(NodeWithSimpleName::getNameAsString).toList();

        for (ClassOrInterfaceType id : findSuperInterfaces(existingType)) {
            if (!newFileSuperInterfaces.contains(id.getNameAsString())) {
                customSuperInterfaces.add(id);
            }
        }

        return customSuperInterfaces;
    }

    public static List<ClassOrInterfaceType> findSuperInterfaces(BodyDeclaration<?> bodyDeclaration) {
        if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            return bodyDeclaration.asClassOrInterfaceDeclaration().getImplementedTypes();
        } else if (bodyDeclaration.isEnumDeclaration()) {
            return bodyDeclaration.asEnumDeclaration().getImplementedTypes();
        } else if(bodyDeclaration.isRecordDeclaration()) {
            return bodyDeclaration.asRecordDeclaration().getImplementedTypes();
        }

        return Collections.emptyList();
    }

    public static void addSuperInterface(BodyDeclaration<?> bodyDeclaration, ClassOrInterfaceType superInterface) {
        if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            bodyDeclaration.asClassOrInterfaceDeclaration().addImplementedType(superInterface);
        } else if (bodyDeclaration.isEnumDeclaration()) {
            bodyDeclaration.asEnumDeclaration().addImplementedType(superInterface);
        } else if(bodyDeclaration.isRecordDeclaration()) {
            bodyDeclaration.asRecordDeclaration().addImplementedType(superInterface);
        }
    }

    /**
     * Create a string representation of an import that we can use to find matches.
     *
     * @param importDeclaration the import declaration to stringify
     * @return string representation of the import (not a full import statement)
     */
    public static String stringify(ImportDeclaration importDeclaration) {
        StringBuilder sb = new StringBuilder();
        if (importDeclaration.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }
        if (importDeclaration.isModule()) {
            sb.append("module "); //$NON-NLS-1$
        }
        sb.append(importDeclaration.getNameAsString());
        if (importDeclaration.isAsterisk()) {
            sb.append(".*"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    public static String stringify(FieldDeclaration fieldDeclaration) {
        return fieldDeclaration.getVariables().stream()
                .map(JavaMergeUtilities::stringify)
                .collect(Collectors.joining(",")); //$NON-NLS-1$
    }

    public static String stringify(VariableDeclarator variableDeclarator) {
        return variableDeclarator.getType().toString()
                + " " //$NON-NLS-1$
                + variableDeclarator.getName().toString();
    }
}
