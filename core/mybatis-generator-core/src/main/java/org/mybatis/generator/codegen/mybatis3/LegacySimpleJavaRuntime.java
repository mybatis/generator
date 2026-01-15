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
package org.mybatis.generator.codegen.mybatis3;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.SimpleAnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.SimpleJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.model.SimpleModelGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.SimpleXMLMapperGenerator;
import org.mybatis.generator.config.TypedPropertyHolder;

/**
 * Introspected table implementation for generating simple MyBatis3 artifacts.
 *
 * <p>Simple means no "by example" methods, flat model, etc.
 *
 * @author Jeff Butler
 */
public class LegacySimpleJavaRuntime extends LegacyJavaRuntime {
    protected LegacySimpleJavaRuntime(Builder builder) {
        super(builder);
    }

    @Override
    protected void calculateXmlMapperGenerator(@Nullable AbstractJavaClientGenerator javaClientGenerator,
                                               List<String> warnings,
                                               ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration().isPresent()) {
                xmlMapperGenerator = new SimpleXMLMapperGenerator.Builder()
                        .withContext(context)
                        .withIntrospectedTable(introspectedTable)
                        .withProgressCallback(progressCallback)
                        .withWarnings(warnings)
                        .build();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator().orElse(null);
        }
    }

    @Override
    protected Optional<String> calculateJavaClientGeneratorBuilderType() {
        return context.getJavaClientGeneratorConfiguration().flatMap(TypedPropertyHolder::getConfigurationType)
                .map(t -> {
                    if ("XMLMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return SimpleJavaClientGenerator.Builder.class.getName();
                    } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return SimpleAnnotatedClientGenerator.Builder.class.getName();
                    } else if ("MAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return SimpleJavaClientGenerator.Builder.class.getName();
                    } else {
                        return t;
                    }
                });
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        var javaGenerator = new SimpleModelGenerator.Builder()
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

    public static class Builder extends LegacyJavaRuntime.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public LegacySimpleJavaRuntime build() {
            return new LegacySimpleJavaRuntime(this);
        }
    }
}
