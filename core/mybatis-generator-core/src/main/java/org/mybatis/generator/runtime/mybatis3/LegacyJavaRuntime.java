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
package org.mybatis.generator.runtime.mybatis3;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.AbstractRuntime;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TypedPropertyHolder;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.runtime.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.runtime.mybatis3.model.BaseRecordGenerator;
import org.mybatis.generator.runtime.mybatis3.model.ExampleGenerator;
import org.mybatis.generator.runtime.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.runtime.mybatis3.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.XMLMapperGenerator;

/**
 * Introspected table implementation for generating MyBatis3 artifacts.
 *
 * @author Jeff Butler
 */
public class LegacyJavaRuntime extends AbstractRuntime {
    protected LegacyJavaRuntime(Builder builder) {
        super(builder);
    }

    @Override
    protected void calculateGenerators() {
        calculateJavaModelGenerators(warnings, progressCallback);

        AbstractJavaClientGenerator javaClientGenerator =
                calculateClientGenerator(warnings, progressCallback).orElse(null);

        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }

    protected void calculateXmlMapperGenerator(@Nullable AbstractJavaClientGenerator javaClientGenerator,
            List<String> warnings,
            ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration().isPresent()) {
                xmlMapperGenerator = new XMLMapperGenerator.Builder()
                        .withContext(context)
                        .withIntrospectedTable(introspectedTable)
                        .withWarnings(warnings)
                        .withProgressCallback(progressCallback)
                        .withCommentGenerator(commentGenerator)
                        .withPluginAggregator(pluginAggregator)
                        .build();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator().orElse(null);
        }
    }

    protected Optional<AbstractJavaClientGenerator> calculateClientGenerator(List<String> warnings,
            ProgressCallback progressCallback) {
        if (!introspectedTable.getRules().generateJavaClient()) {
            return Optional.empty();
        }

        return calculateJavaClientGeneratorBuilderType().map(t -> {
            AbstractJavaClientGenerator.AbstractJavaClientGeneratorBuilder<?> builder =
                    ObjectFactory.createInternalObject(t,
                    AbstractJavaClientGenerator.AbstractJavaClientGeneratorBuilder.class);
            var generator = builder
                    .withProject(getClientProject().orElseThrow(() -> new IllegalArgumentException(
                            "Internal Error: No client project exists when a client generator configuration exists.")
                    ))
                    .withContext(context)
                    .withIntrospectedTable(introspectedTable)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .withCommentGenerator(commentGenerator)
                    .withPluginAggregator(pluginAggregator)
                    .build();

            javaGenerators.add(generator);
            return generator;
        });
    }

    protected Optional<String> calculateJavaClientGeneratorBuilderType() {
        return context.getJavaClientGeneratorConfiguration()
                .flatMap(TypedPropertyHolder::getConfigurationType)
                .map(t -> {
                    if ("XMLMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return JavaMapperGenerator.Builder.class.getName();
                    } else if ("MIXEDMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return MixedClientGenerator.Builder.class.getName();
                    } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return AnnotatedClientGenerator.Builder.class.getName();
                    } else if ("MAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return JavaMapperGenerator.Builder.class.getName();
                    } else {
                        return t;
                    }
                });
    }

    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (introspectedTable.getRules().generateExampleClass()) {
            var javaGenerator = new ExampleGenerator.Builder()
                    .withProject(getExampleProject())
                    .withContext(context)
                    .withIntrospectedTable(introspectedTable)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .withCommentGenerator(commentGenerator)
                    .withPluginAggregator(pluginAggregator)
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            var javaGenerator = new PrimaryKeyGenerator.Builder()
                    .withProject(getModelProject())
                    .withContext(context)
                    .withIntrospectedTable(introspectedTable)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .withCommentGenerator(commentGenerator)
                    .withPluginAggregator(pluginAggregator)
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (introspectedTable.getRules().generateBaseRecordClass()) {
            var javaGenerator = new BaseRecordGenerator.Builder()
                    .withProject(getModelProject())
                    .withContext(context)
                    .withIntrospectedTable(introspectedTable)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .withCommentGenerator(commentGenerator)
                    .withPluginAggregator(pluginAggregator)
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            var javaGenerator = new RecordWithBLOBsGenerator.Builder()
                    .withProject(getModelProject())
                    .withContext(context)
                    .withIntrospectedTable(introspectedTable)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .withCommentGenerator(commentGenerator)
                    .withPluginAggregator(pluginAggregator)
                    .build();
            javaGenerators.add(javaGenerator);
        }
    }

    protected String getExampleProject() {
        String project = context.getJavaModelGeneratorConfiguration().getProperty(
                PropertyRegistry.MODEL_GENERATOR_EXAMPLE_PROJECT);

        if (StringUtility.stringHasValue(project)) {
            return project;
        } else {
            return getModelProject();
        }
    }

    public static class Builder extends AbstractRuntimeBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public LegacyJavaRuntime build() {
            return new LegacyJavaRuntime(this);
        }
    }
}
