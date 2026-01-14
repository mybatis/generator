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
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.PluginAggregator;

public abstract class AbstractGenerator {
    protected final Context context;
    protected final IntrospectedTable introspectedTable;
    protected final List<String> warnings;
    protected final ProgressCallback progressCallback;
    protected final CommentGenerator commentGenerator;
    protected final PluginAggregator pluginAggregator;

    protected AbstractGenerator(AbstractGeneratorBuilder<?> builder) {
        this.context = Objects.requireNonNull(builder.context);
        this.introspectedTable = Objects.requireNonNull(builder.introspectedTable);
        this.warnings = Objects.requireNonNull(builder.warnings);
        this.progressCallback = Objects.requireNonNull(builder.progressCallback);
        this.commentGenerator = Objects.requireNonNull(builder.commentGenerator);
        this.pluginAggregator = Objects.requireNonNull(builder.pluginAggregator);
    }

    protected <T extends AbstractGeneratorBuilder<T>> T initializeSubBuilder(T builder) {
        return builder.withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withWarnings(warnings)
                .withProgressCallback(progressCallback)
                .withCommentGenerator(commentGenerator)
                .withPluginAggregator(pluginAggregator);
    }

    public abstract static class AbstractGeneratorBuilder<T extends AbstractGeneratorBuilder<T>> {
        private @Nullable Context context;
        private @Nullable IntrospectedTable introspectedTable;
        private @Nullable List<String> warnings;
        private @Nullable ProgressCallback progressCallback;
        private @Nullable CommentGenerator commentGenerator;
        private @Nullable PluginAggregator pluginAggregator;

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

        public T withCommentGenerator(CommentGenerator commentGenerator) {
            this.commentGenerator = commentGenerator;
            return getThis();
        }

        public T withPluginAggregator(PluginAggregator pluginAggregator) {
            this.pluginAggregator = pluginAggregator;
            return getThis();
        }

        protected abstract T getThis();
    }
}
