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

import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.mybatis.generator.eclipse.tests.harness.summary.AnnotationSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.ClassSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.EnumSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.FieldSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.InterfaceSummary;

public abstract class AbstractSummarizer {
    
    private SummaryGatheringVisitor visitor = new SummaryGatheringVisitor();
    
    @SuppressWarnings("unchecked")
    public AbstractSummarizer(CompilationUnit node) {
        visitBodyDeclarations(node.types());
    }
    
    @SuppressWarnings("unchecked")
    public AbstractSummarizer(AbstractTypeDeclaration node) {
        visitBodyDeclarations(node.bodyDeclarations());
    }
    
    private void visitBodyDeclarations(List<BodyDeclaration> bodyDeclarations) {
        for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
            bodyDeclaration.accept(visitor);
        }
    }

    public List<AnnotationSummary> getAnnotationSummaries() {
        return visitor.getAnnotationSummaries();
    }

    public List<ClassSummary> getClassSummaries() {
        return visitor.getClassSummaries();
    }

    public List<EnumSummary> getEnumSummaries() {
        return visitor.getEnumSummaries();
    }

    public List<InterfaceSummary> getInterfaceSummaries() {
        return visitor.getInterfaceSummaries();
    }

    public List<String> getMethodSignatures() {
        return visitor.getMethodSignatures();
    }

    public List<FieldSummary> getFieldSummaries() {
        return visitor.getFieldSummaries();
    }

    public List<String> getAnnotationMembers() {
        return visitor.getAnnotationMembers();
    }
}
