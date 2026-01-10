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
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Context;

public abstract class AbstractGenerator {
    protected Context context;
    protected IntrospectedTable introspectedTable;
    protected List<String> warnings;
    protected ProgressCallback progressCallback;

    // TODO - remove once we change the build method for JavaClientGenerators
    protected AbstractGenerator() {
        super();
    }

    protected AbstractGenerator(AbstractGeneratorBuilder<?> builder) {
        this.context = Objects.requireNonNull(builder.context);
        this.introspectedTable = Objects.requireNonNull(builder.introspectedTable);
        this.warnings = Objects.requireNonNull(builder.warnings);
        this.progressCallback = Objects.requireNonNull(builder.progressCallback);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public abstract static class AbstractGeneratorBuilder<T extends AbstractGeneratorBuilder<T>> {
        private @Nullable Context context;
        private @Nullable IntrospectedTable introspectedTable;
        private @Nullable List<String> warnings;
        private @Nullable ProgressCallback progressCallback;

        public T withContext(Context context) {
            this.context = context;
            return getThis();
        }

        public T withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return getThis();
        }

        public T withWarnings(List<String> warnings) {
            this.warnings = warnings;
            return getThis();
        }

        public T withProgressCallback(ProgressCallback progressCallback) {
            this.progressCallback = progressCallback;
            return getThis();
        }

        protected abstract T getThis();
    }
}
