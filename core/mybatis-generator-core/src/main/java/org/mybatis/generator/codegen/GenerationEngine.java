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

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.GenericGeneratedFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.PluginAggregator;

public class GenerationEngine {
    private final Context context;
    private final List<String> warnings;
    private final ProgressCallback progressCallback;
    private final List<IntrospectedTable> introspectedTables;
    private final CommentGenerator commentGenerator;
    private final PluginAggregator pluginAggregator;

    protected GenerationEngine(Builder builder) {
        context = Objects.requireNonNull(builder.context);
        warnings = Objects.requireNonNull(builder.warnings);
        progressCallback = Objects.requireNonNull(builder.progressCallback);
        introspectedTables = Objects.requireNonNull(builder.introspectedTables);
        commentGenerator = Objects.requireNonNull(builder.commentGenerator);

        pluginAggregator = new PluginAggregator();
        context.pluginConfigurations().forEach(pluginConfiguration -> {
            Plugin plugin = ObjectFactory.createPlugin(context, pluginConfiguration, commentGenerator);
            if (plugin.validate(warnings)) {
                pluginAggregator.addPlugin(plugin);
            } else {
                warnings.add(getString("Warning.24", //$NON-NLS-1$
                        pluginConfiguration.getConfigurationType()
                                .orElse("Unknown Plugin Type"), context.getId())); //$NON-NLS-1$
            }
        });

        // initialize everything first before generating. This allows plugins to know about other
        // items in the configuration.
        for (IntrospectedTable introspectedTable : introspectedTables) {
            introspectedTable.initialize(pluginAggregator, commentGenerator);
            introspectedTable.calculateGenerators(warnings, progressCallback);
        }
    }

    public List<GeneratedJavaFile> generateJavaFiles() throws InterruptedException {
        List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();

        for (IntrospectedTable introspectedTable : introspectedTables) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(introspectedTable)) {
                continue;
            }

            generatedJavaFiles.addAll(introspectedTable.getGeneratedJavaFiles());
            generatedJavaFiles.addAll(pluginAggregator.contextGenerateAdditionalJavaFiles(introspectedTable));
        }

        generatedJavaFiles.addAll(pluginAggregator.contextGenerateAdditionalJavaFiles());

        return generatedJavaFiles;
    }

    public List<GeneratedXmlFile> generateXmlFiles() throws InterruptedException {
        List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<>();

        for (IntrospectedTable introspectedTable : introspectedTables) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(introspectedTable)) {
                continue;
            }

            generatedXmlFiles.addAll(introspectedTable.getGeneratedXmlFiles());
            generatedXmlFiles.addAll(pluginAggregator.contextGenerateAdditionalXmlFiles(introspectedTable));
        }

        generatedXmlFiles.addAll(pluginAggregator.contextGenerateAdditionalXmlFiles());

        return generatedXmlFiles;
    }

    public List<GeneratedKotlinFile> generateKotlinFiles() throws InterruptedException {
        List<GeneratedKotlinFile> generatedKotlinFiles = new ArrayList<>();

        for (IntrospectedTable introspectedTable : introspectedTables) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(introspectedTable)) {
                continue;
            }

            generatedKotlinFiles.addAll(introspectedTable.getGeneratedKotlinFiles());
            generatedKotlinFiles.addAll(pluginAggregator.contextGenerateAdditionalKotlinFiles(introspectedTable));
        }

        generatedKotlinFiles.addAll(pluginAggregator.contextGenerateAdditionalKotlinFiles());
        return generatedKotlinFiles;
    }

    public List<GenericGeneratedFile> generateGenericFiles() throws InterruptedException {
        List<GenericGeneratedFile> genericGeneratedFiles = new ArrayList<>();

        for (IntrospectedTable introspectedTable : introspectedTables) {
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
        private @Nullable Context context;
        private @Nullable ProgressCallback progressCallback;
        private @Nullable List<IntrospectedTable> introspectedTables;
        private @Nullable List<String> warnings;
        private @Nullable CommentGenerator commentGenerator;

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withIntrospectedTables(List<IntrospectedTable> introspectedTables) {
            this.introspectedTables = introspectedTables;
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

        public Builder withCommentGenerator(CommentGenerator commentGenerator) {
            this.commentGenerator = commentGenerator;
            return this;
        }

        public GenerationEngine build() {
            return new GenerationEngine(this);
        }
    }
}
