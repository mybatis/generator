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
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.GenericGeneratedFile;
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
        runtimes = new ArrayList<>();

        Context context = contextValues.context();
        List<String> warnings = Objects.requireNonNull(builder.warnings);
        CommentGenerator commentGenerator = contextValues.commentGenerator();

        // initialize everything first before generating. This allows plugins to know about other
        // items in the configuration.
        for (IntrospectedTable introspectedTable : contextValues.introspectedTables()) {
            AbstractRuntime.AbstractRuntimeBuilder<?> runtimeBuilder = ObjectFactory.createInternalObject(
                    contextValues.runtimeBuilderClassName(), AbstractRuntime.AbstractRuntimeBuilder.class);
            runtimes.add(runtimeBuilder
                    .withIntrospectedTable(introspectedTable)
                    .withContext(context)
                    .withCommentGenerator(commentGenerator)
                    .withPluginAggregator(contextValues.pluginAggregator())
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .build());
        }
    }

    public int getGenerationSteps() {
        int totalSteps = 0;
        for (AbstractRuntime runtime : runtimes) {
            totalSteps += runtime.getGenerationSteps();
        }
        return totalSteps;
    }

    public GenerationResults generate() throws InterruptedException {
        GenerationResults generationResults = new GenerationResults.Builder()
                .withJavaFormatter(contextValues.javaFormatter())
                .withKotlinFormatter(contextValues.kotlinFormatter())
                .withXmlFormatter(contextValues.xmlFormatter())
                .withJavaFileEncoding(contextValues.javaFileEncoding())
                .withKotlinFileEncoding(contextValues.kotlinFileEncoding())
                .build();

        generationResults.addGeneratedJavaFiles(generateJavaFiles());
        generationResults.addGeneratedXmlFiles(generateXmlFiles());
        generationResults.addGeneratedKotlinFiles(generateKotlinFiles());
        generationResults.addGeneratedGenericFiles(generateGenericFiles());

        return generationResults;
    }

    private List<GeneratedJavaFile> generateJavaFiles() throws InterruptedException {
        PluginAggregator pluginAggregator = contextValues.pluginAggregator();
        List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();

        for (AbstractRuntime runtime : runtimes) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(runtime.getIntrospectedTable())) {
                continue;
            }

            generatedJavaFiles.addAll(runtime.getGeneratedJavaFiles());
            generatedJavaFiles.addAll(
                    pluginAggregator.contextGenerateAdditionalJavaFiles(runtime.getIntrospectedTable()));
        }

        generatedJavaFiles.addAll(pluginAggregator.contextGenerateAdditionalJavaFiles());

        return generatedJavaFiles;
    }

    private List<GeneratedXmlFile> generateXmlFiles() throws InterruptedException {
        PluginAggregator pluginAggregator = contextValues.pluginAggregator();
        List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<>();

        for (AbstractRuntime runtime : runtimes) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(runtime.getIntrospectedTable())) {
                continue;
            }

            generatedXmlFiles.addAll(runtime.getGeneratedXmlFiles());
            generatedXmlFiles.addAll(
                    pluginAggregator.contextGenerateAdditionalXmlFiles(runtime.getIntrospectedTable()));
        }

        generatedXmlFiles.addAll(pluginAggregator.contextGenerateAdditionalXmlFiles());

        return generatedXmlFiles;
    }

    private List<GeneratedKotlinFile> generateKotlinFiles() throws InterruptedException {
        PluginAggregator pluginAggregator = contextValues.pluginAggregator();
        List<GeneratedKotlinFile> generatedKotlinFiles = new ArrayList<>();

        for (AbstractRuntime runtime : runtimes) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(runtime.getIntrospectedTable())) {
                continue;
            }

            generatedKotlinFiles.addAll(runtime.getGeneratedKotlinFiles());
            generatedKotlinFiles.addAll(
                    pluginAggregator.contextGenerateAdditionalKotlinFiles(runtime.getIntrospectedTable()));
        }

        generatedKotlinFiles.addAll(pluginAggregator.contextGenerateAdditionalKotlinFiles());
        return generatedKotlinFiles;
    }

    private List<GenericGeneratedFile> generateGenericFiles() throws InterruptedException {
        PluginAggregator pluginAggregator = contextValues.pluginAggregator();
        List<GenericGeneratedFile> genericGeneratedFiles = new ArrayList<>();

        for (IntrospectedTable introspectedTable : contextValues.introspectedTables()) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(introspectedTable)) {
                continue;
            }

            genericGeneratedFiles.addAll(pluginAggregator.contextGenerateAdditionalFiles(introspectedTable));
        }

        genericGeneratedFiles.addAll(pluginAggregator.contextGenerateAdditionalFiles());
        return genericGeneratedFiles;
    }

    public static class Builder {
        private @Nullable CalculatedContextValues contextValues;
        private @Nullable ProgressCallback progressCallback;
        private @Nullable List<String> warnings;

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

        public GenerationEngine build() {
            return new GenerationEngine(this);
        }
    }
}
