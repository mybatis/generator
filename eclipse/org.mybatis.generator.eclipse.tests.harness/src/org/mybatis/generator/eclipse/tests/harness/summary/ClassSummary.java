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
package org.mybatis.generator.eclipse.tests.harness.summary;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.mybatis.generator.eclipse.tests.harness.summary.support.ClassSummarizer;

public class ClassSummary extends AbstractTypeOrEnumSummary {
    private String superClass;

    private ClassSummary() {
        super();
    }
    
    public String getSuperClass() {
        return superClass;
    }
    
    public static ClassSummary from(TypeDeclaration node) {
        ClassSummarizer summarizer = ClassSummarizer.from(node);
        
        return new Builder()
            .withName(summarizer.getName())
            .withSuperClass(summarizer.getSuperClass())
            .withSuperInterfaces(summarizer.getSuperInterfaces())
            .withFields(summarizer.getFieldSummaries())
            .withMethods(summarizer.getMethodSignatures())
            .withAnnotationSummaries(summarizer.getAnnotationSummaries())
            .withClassSummaries(summarizer.getClassSummaries())
            .withEnumSummaries(summarizer.getEnumSummaries())
            .withInterfaceSummaries(summarizer.getInterfaceSummaries())
            .summary();
    }
    
    private static class Builder extends AbstractTypeOrEnumSummaryBuilder<Builder> {
        private ClassSummary summary = new ClassSummary();
        
        private Builder withSuperClass(String superClass) {
            summary.superClass = superClass;
            return this;
        }

        @Override
        protected ClassSummary summary() {
            return summary;
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
