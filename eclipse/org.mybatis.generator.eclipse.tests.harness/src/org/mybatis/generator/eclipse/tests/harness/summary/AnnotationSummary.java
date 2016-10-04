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

import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.mybatis.generator.eclipse.tests.harness.summary.support.AnnotationSummarizer;

public class AnnotationSummary extends AbstractBodyElementSummary {

    private List<String> annotationMembers = new ArrayList<String>();

    private AnnotationSummary() {
        super();
    }
    
    public boolean hasAnnotationMember(String annotationMember) {
        return annotationMembers.contains(annotationMember);
    }
    
    public int getAnnotationMemberCount() {
        return annotationMembers.size();
    }
    
    public static AnnotationSummary from(AnnotationTypeDeclaration node) {
        AnnotationSummarizer summarizer = AnnotationSummarizer.from(node);
        
        return new Builder()
            .withName(summarizer.getName())
            .withAnnotationMembers(summarizer.getAnnotationMembers())
            .withFields(summarizer.getFieldSummaries())
            .withAnnotationSummaries(summarizer.getAnnotationSummaries())
            .withClassSummaries(summarizer.getClassSummaries())
            .withEnumSummaries(summarizer.getEnumSummaries())
            .withInterfaceSummaries(summarizer.getInterfaceSummaries())
            .summary();
    }
    
    private static class Builder extends AbstractBodyElementSummaryBuilder<Builder> {
        private AnnotationSummary summary = new AnnotationSummary();

        private Builder withAnnotationMembers(List<String> annotationMembers) {
            summary.annotationMembers = annotationMembers;
            return this;
        }
        
        @Override
        protected AnnotationSummary summary() {
            return summary;
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
