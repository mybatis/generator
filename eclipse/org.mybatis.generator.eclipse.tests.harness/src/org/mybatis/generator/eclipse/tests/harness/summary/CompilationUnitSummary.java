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

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.mybatis.generator.eclipse.tests.harness.summary.support.CompilationUnitSummerizer;

public class CompilationUnitSummary extends AbstractSummary {
    private List<String> importDeclarations = new ArrayList<>();

    private CompilationUnitSummary() {
        super();
    }
    
    public boolean hasImportDeclaration(String importDeclaration) {
        return importDeclarations.contains(importDeclaration);
    }
    
    public int getImportCount() {
        return importDeclarations.size();
    }
    
    public static CompilationUnitSummary from(CompilationUnit node) {
        CompilationUnitSummerizer summerizer = CompilationUnitSummerizer.from(node);

        return new Builder()
            .withImportDeclarations(summerizer.getImports())
            .withAnnotationSummaries(summerizer.getAnnotationSummaries())
            .withClassSummaries(summerizer.getClassSummaries())
            .withEnumSummaries(summerizer.getEnumSummaries())
            .withInterfaceSummaries(summerizer.getInterfaceSummaries())
            .summary();
    }
    
    private static class Builder extends AbstractSummaryBuilder<Builder> {
        private CompilationUnitSummary summary = new CompilationUnitSummary();

        private Builder withImportDeclarations(List<String> importDeclarations) {
            summary.importDeclarations = importDeclarations;
            return this;
        }

        @Override
        protected CompilationUnitSummary summary() {
            return summary;
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
