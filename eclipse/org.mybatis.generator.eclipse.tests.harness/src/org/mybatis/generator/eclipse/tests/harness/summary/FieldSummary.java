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

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.mybatis.generator.eclipse.tests.harness.summary.support.FieldSummarizer;

public class FieldSummary {
    private String name;
    private String type;

    private FieldSummary() {
        super();
    }
    
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    
    public static FieldSummary from(VariableDeclarationFragment node) {
        FieldSummarizer summarizer = FieldSummarizer.from(node);
        
        return new Builder()
            .withName(summarizer.getName())
            .withType(summarizer.getFieldType())
            .summary();
    }

    private static class Builder {
        private FieldSummary fieldSummary = new FieldSummary();
        
        public Builder withName(String name) {
            fieldSummary.name = name;
            return this;
        }
        
        public Builder withType(String type) {
            fieldSummary.type = type;
            return this;
        }
        
        public FieldSummary summary() {
            return fieldSummary;
        }
    }
}
