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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.mybatis.generator.eclipse.tests.harness.summary.support.EnumSummarizer;

public class EnumSummary extends AbstractTypeOrEnumSummary {
    private List<String> enumConstants = new ArrayList<>();

    private EnumSummary() {
        super();
    }
    
    public boolean hasEnumConstant(String enumConstant) {
        return enumConstants.contains(enumConstant);
    }
    
    public int getEnumConstantCount() {
        return enumConstants.size();
    }
    
    public static EnumSummary from(EnumDeclaration node) {
        EnumSummarizer summarizer = EnumSummarizer.from(node);

        return new Builder()
            .withName(summarizer.getName())
            .withSuperInterfaces(summarizer.getSuperInterfaces())
            .withEnumConstants(summarizer.getEnumConstants())
            .withFields(summarizer.getFieldSummaries())
            .withMethods(summarizer.getMethodSignatures())
            .withAnnotationSummaries(summarizer.getAnnotationSummaries())
            .withClassSummaries(summarizer.getClassSummaries())
            .withEnumSummaries(summarizer.getEnumSummaries())
            .withInterfaceSummaries(summarizer.getInterfaceSummaries())
            .summary();
    }
    
    private static class Builder extends AbstractTypeOrEnumSummaryBuilder<Builder> {
        private EnumSummary summary = new EnumSummary();
        
        private Builder withEnumConstants(List<String> enumConstants) {
            summary.enumConstants = enumConstants;
            return this;
        }
        
        @Override
        protected EnumSummary summary() {
            return summary;
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
