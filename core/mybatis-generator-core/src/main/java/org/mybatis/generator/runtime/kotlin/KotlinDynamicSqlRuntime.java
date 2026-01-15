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
package org.mybatis.generator.runtime.kotlin;

import java.util.List;

import org.mybatis.generator.api.AbstractRuntime;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;

public class KotlinDynamicSqlRuntime extends AbstractRuntime {
    public KotlinDynamicSqlRuntime(Builder builder) {
        super(builder);
    }

    @Override
    protected void calculateGenerators() {
        calculateKotlinDataClassGenerator(warnings, progressCallback);
        context.getJavaClientGeneratorConfiguration().ifPresent(config -> {
            if (introspectedTable.getRules().generateJavaClient()) {
                calculateKotlinMapperAndExtensionsGenerator(config.getTargetProject(), warnings, progressCallback);
            }
        });
    }

    protected void calculateKotlinMapperAndExtensionsGenerator(String clientProject, List<String> warnings,
            ProgressCallback progressCallback) {
        AbstractKotlinGenerator kotlinGenerator = new KotlinMapperAndExtensionsGenerator.Builder()
                .withProject(clientProject)
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withProgressCallback(progressCallback)
                .withWarnings(warnings)
                .withCommentGenerator(commentGenerator)
                .withPluginAggregator(pluginAggregator)
                .build();
        kotlinGenerators.add(kotlinGenerator);
    }

    protected void calculateKotlinDataClassGenerator(List<String> warnings,
            ProgressCallback progressCallback) {
        AbstractKotlinGenerator kotlinGenerator = new KotlinDataClassGenerator.Builder()
                .withProject(getModelProject())
                .withContext(context)
                .withIntrospectedTable(introspectedTable)
                .withProgressCallback(progressCallback)
                .withWarnings(warnings)
                .withCommentGenerator(commentGenerator)
                .withPluginAggregator(pluginAggregator)
                .build();
        kotlinGenerators.add(kotlinGenerator);
    }

    public static class Builder extends AbstractRuntimeBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public KotlinDynamicSqlRuntime build() {
            return new KotlinDynamicSqlRuntime(this);
        }
    }
}
