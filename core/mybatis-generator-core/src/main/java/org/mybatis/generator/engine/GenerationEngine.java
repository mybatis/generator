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
package org.mybatis.generator.engine;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.ContextResults;
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
    }

    public void generateFiles(ContextResults.GeneratedFiles generatedFiles) throws InterruptedException {
        // initialize everything first before generating. This allows plugins to know about other
        // items in the configuration.
        for (IntrospectedTable introspectedTable : introspectedTables) {
            progressCallback.checkCancel();
            introspectedTable.initialize(pluginAggregator, commentGenerator);
            introspectedTable.calculateGenerators(warnings, progressCallback);
        }

        for (IntrospectedTable introspectedTable : introspectedTables) {
            progressCallback.checkCancel();

            if (!pluginAggregator.shouldGenerate(introspectedTable)) {
                continue;
            }

            generatedFiles.addGeneratedJavaFiles(introspectedTable.getGeneratedJavaFiles());
            generatedFiles.addGeneratedXmlFiles(introspectedTable.getGeneratedXmlFiles());
            generatedFiles.addGeneratedKotlinFiles(introspectedTable.getGeneratedKotlinFiles());

            generatedFiles.addGeneratedJavaFiles(pluginAggregator.contextGenerateAdditionalJavaFiles(introspectedTable));
            generatedFiles.addGeneratedXmlFiles(pluginAggregator.contextGenerateAdditionalXmlFiles(introspectedTable));
            generatedFiles.addGeneratedKotlinFiles(pluginAggregator.contextGenerateAdditionalKotlinFiles(introspectedTable));
            generatedFiles.addOtherGeneratedFiles(pluginAggregator.contextGenerateAdditionalFiles(introspectedTable));
        }

        generatedFiles.addGeneratedJavaFiles(pluginAggregator.contextGenerateAdditionalJavaFiles());
        generatedFiles.addGeneratedXmlFiles(pluginAggregator.contextGenerateAdditionalXmlFiles());
        generatedFiles.addGeneratedKotlinFiles(pluginAggregator.contextGenerateAdditionalKotlinFiles());
        generatedFiles.addOtherGeneratedFiles(pluginAggregator.contextGenerateAdditionalFiles());
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
