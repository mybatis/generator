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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;
import org.mybatis.generator.codegen.AbstractRuntime;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

public class IntrospectedTableKotlinImpl extends AbstractRuntime {
    protected final List<AbstractKotlinGenerator> kotlinGenerators = new ArrayList<>();

    public IntrospectedTableKotlinImpl(Builder builder) {
        super(builder);
        calculateKotlinDataClassGenerator(warnings, progressCallback);
        if (contextHasClientConfiguration() && introspectedTable.getRules().generateJavaClient()) {
            calculateKotlinMapperAndExtensionsGenerator(warnings, progressCallback);
        }
    }

    private boolean contextHasClientConfiguration() {
        return context.getJavaClientGeneratorConfiguration().isPresent();
    }

    protected void calculateKotlinMapperAndExtensionsGenerator(List<String> warnings,
            ProgressCallback progressCallback) {
        AbstractKotlinGenerator kotlinGenerator = new KotlinMapperAndExtensionsGenerator.Builder()
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

    protected String getModelProject() {
        return context.getJavaModelGeneratorConfiguration().getTargetProject();
    }

    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        return Collections.emptyList();
    }

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        return Collections.emptyList();
    }

    @Override
    public List<GeneratedKotlinFile> getGeneratedKotlinFiles() {
        List<GeneratedKotlinFile> answer = new ArrayList<>();

        for (AbstractKotlinGenerator kotlinGenerator : kotlinGenerators) {
            List<KotlinFile> kotlinFiles = kotlinGenerator.getKotlinFiles();
            for (KotlinFile kotlinFile : kotlinFiles) {
                GeneratedKotlinFile gjf = new GeneratedKotlinFile(kotlinFile, kotlinGenerator.getProject());
                answer.add(gjf);
            }
        }

        return answer;
    }

    @Override
    public int getGenerationSteps() {
        return kotlinGenerators.size();
    }

    public static class Builder extends AbstractRuntimeBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public IntrospectedTableKotlinImpl build() {
            return new IntrospectedTableKotlinImpl(this);
        }
    }
}
