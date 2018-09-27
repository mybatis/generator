/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.eclipse.tests.harness.summary.support;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.mybatis.generator.eclipse.core.merge.visitors.MethodSignatureStringifier;
import org.mybatis.generator.eclipse.tests.harness.summary.AnnotationSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.ClassSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.EnumSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.FieldSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.InterfaceSummary;

public class SummaryGatheringVisitor extends ASTVisitor {
    private List<String> methodSignatures = new ArrayList<>();
    private List<FieldSummary> fieldSummaries = new ArrayList<>();
    private List<AnnotationSummary> annotationSummaries = new ArrayList<>();
    private List<ClassSummary> classSummaries = new ArrayList<>();
    private List<EnumSummary> enumSummaries = new ArrayList<>();
    private List<InterfaceSummary> interfaceSummaries = new ArrayList<>();
    private List<String> annotationMembers = new ArrayList<>();

    @Override
    public boolean visit(AnnotationTypeMemberDeclaration node) {
        annotationMembers.add(node.getName().getFullyQualifiedName());
        return false;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        MethodSignatureStringifier visitor = new MethodSignatureStringifier();
        node.accept(visitor);
        methodSignatures.add(visitor.toString());
        return false;
    }

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        fieldSummaries.add(FieldSummary.from(node));
        return false;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        if (node.isInterface()) {
            interfaceSummaries.add(InterfaceSummary.from(node));
        } else {
            classSummaries.add(ClassSummary.from(node));
        }
        return false;
    }

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {
        annotationSummaries.add(AnnotationSummary.from(node));
        return false;
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        enumSummaries.add(EnumSummary.from(node));
        return false;
    }

    public List<String> getAnnotationMembers() {
        return annotationMembers;
    }
    
    public List<String> getMethodSignatures() {
        return methodSignatures;
    }

    public List<FieldSummary> getFieldSummaries() {
        return fieldSummaries;
    }

    public List<AnnotationSummary> getAnnotationSummaries() {
        return annotationSummaries;
    }

    public List<ClassSummary> getClassSummaries() {
        return classSummaries;
    }

    public List<EnumSummary> getEnumSummaries() {
        return enumSummaries;
    }

    public List<InterfaceSummary> getInterfaceSummaries() {
        return interfaceSummaries;
    }
}
