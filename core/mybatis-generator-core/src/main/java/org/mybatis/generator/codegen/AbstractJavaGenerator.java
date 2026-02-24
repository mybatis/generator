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
package org.mybatis.generator.codegen;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.CompilationUnit;

public abstract class AbstractJavaGenerator extends AbstractGenerator {
    public abstract List<CompilationUnit> getCompilationUnits();

    private final String project;

    protected AbstractJavaGenerator(AbstractJavaGeneratorBuilder<?> builder) {
        super(builder);
        this.project = Objects.requireNonNull(builder.project);
    }

    public String getProject() {
        return project;
    }

    public abstract static class AbstractJavaGeneratorBuilder<T extends AbstractJavaGeneratorBuilder<T>>
            extends AbstractGeneratorBuilder<T> {
        protected @Nullable String project;

        public T withProject(String project) {
            this.project = project;
            return getThis();
        }

        public abstract AbstractJavaGenerator build();
    }
}
