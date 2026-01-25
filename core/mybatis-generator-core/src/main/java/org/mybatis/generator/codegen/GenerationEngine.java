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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.AbstractRuntime;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.PluginAggregator;

public class GenerationEngine {
    private final CalculatedContextValues contextValues;
    private final ProgressCallback progressCallback;
    private final List<AbstractRuntime> runtimes;

    protected GenerationEngine(Builder builder) {
        contextValues = Objects.requireNonNull(builder.contextValues);
        progressCallback = Objects.requireNonNull(builder.progressCallback);

        Context context = contextValues.context();
        List<String> warnings = Objects.requireNonNull(builder.warnings);
        CommentGenerator commentGenerator = contextValues.commentGenerator();

        // initialize everything first before generating. This allows plugins to know about other
        // items in the configuration.
        runtimes = builder.introspectedTables.stream().map(introspectedTable -> {
            AbstractRuntime.AbstractRuntimeBuilder<?> runtimeBuilder = ObjectFactory.createInternalObject(
                    contextValues.runtimeBuilderClassName(), AbstractRuntime.AbstractRuntimeBuilder.class);
            return runtimeBuilder
                    .withIntrospectedTable(introspectedTable)
                    .withContext(context)
                    .withCommentGenerator(commentGenerator)
                    .withPluginAggregator(contextValues.pluginAggregator())
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .build();
        }).toList();
    }

    public int getGenerationSteps() {
        return runtimes.stream().mapToInt(AbstractRuntime::getGenerationSteps).sum();
    }

    public GenerationResults generate() throws InterruptedException {
        PluginAggregator pluginAggregator = contextValues.pluginAggregator();
        var builder = new GenerationResults.Builder().withContextValues(contextValues);

        for (AbstractRuntime runtime : runtimes) {
            if (!pluginAggregator.shouldGenerate(runtime.getIntrospectedTable())) {
                continue;
            }

            progressCallback.checkCancel();
            builder.withGeneratedJavaFiles(runtime.getGeneratedJavaFiles());
            builder.withGeneratedJavaFiles(
                    pluginAggregator.contextGenerateAdditionalJavaFiles(runtime.getIntrospectedTable()));

            progressCallback.checkCancel();
            builder.withGeneratedXmlFiles(runtime.getGeneratedXmlFiles());
            builder.withGeneratedXmlFiles(
                    pluginAggregator.contextGenerateAdditionalXmlFiles(runtime.getIntrospectedTable()));

            progressCallback.checkCancel();
            builder.withGeneratedKotlinFiles(runtime.getGeneratedKotlinFiles());
            builder.withGeneratedKotlinFiles(
                    pluginAggregator.contextGenerateAdditionalKotlinFiles(runtime.getIntrospectedTable()));

            progressCallback.checkCancel();
            builder.withGeneratedGenericFiles(
                    pluginAggregator.contextGenerateAdditionalFiles(runtime.getIntrospectedTable()));
        }

        progressCallback.checkCancel();
        builder.withGeneratedJavaFiles(pluginAggregator.contextGenerateAdditionalJavaFiles());
        builder.withGeneratedXmlFiles(pluginAggregator.contextGenerateAdditionalXmlFiles());
        builder.withGeneratedKotlinFiles(pluginAggregator.contextGenerateAdditionalKotlinFiles());
        builder.withGeneratedGenericFiles(pluginAggregator.contextGenerateAdditionalFiles());

        return builder.build();
    }

    public static class Builder {
        private @Nullable CalculatedContextValues contextValues;
        private @Nullable ProgressCallback progressCallback;
        private @Nullable List<String> warnings;
        private final List<IntrospectedTable> introspectedTables = new ArrayList<>();

        public Builder withContextValues(CalculatedContextValues contextValues) {
            this.contextValues = contextValues;
            return this;
        }

        public Builder withProgressCallback(ProgressCallback progressCallback) {
            this.progressCallback = progressCallback;
            return this;
        }

        public Builder withWarnings(List<String> warnings) {
            this.warnings = warnings;
            return this;
        }

        public Builder withIntrospectedTables(List<IntrospectedTable> introspectedTables) {
            this.introspectedTables.addAll(introspectedTables);
            return this;
        }

        public GenerationEngine build() {
            return new GenerationEngine(this);
        }
    }
}
