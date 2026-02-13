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

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.Optional;

import org.mybatis.generator.api.AbstractRuntime;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TypedPropertyHolder;
import org.mybatis.generator.exception.InternalException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.runtime.mybatis3.javamapper.AnnotatedMapperGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.MixedMapperGenerator;
import org.mybatis.generator.runtime.mybatis3.model.BaseRecordGenerator;
import org.mybatis.generator.runtime.mybatis3.model.ExampleGenerator;
import org.mybatis.generator.runtime.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.runtime.mybatis3.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.MixedXmlMapperGenerator;
import org.mybatis.generator.runtime.mybatis3.xmlmapper.XMLMapperGenerator;

/**
 * Runtime for generating MyBatis3 artifacts.
 *
 * @author Jeff Butler
 */
public class LegacyJavaRuntime extends AbstractRuntime {
    protected LegacyJavaRuntime(Builder builder) {
        super(builder);
        calculateGenerators();
    }

    private void calculateGenerators() {
        calculateJavaModelGenerators();
        calculateClientGenerator();
        calculateXmlMapperGenerator();
    }

    protected void calculateXmlMapperGenerator() {
        if (context.getSqlMapGeneratorConfiguration().isPresent()) {
            xmlMapperGenerator = context.getJavaClientGeneratorConfiguration()
                    .flatMap(TypedPropertyHolder::getConfigurationType)
                    .filter(JavaClientGeneratorConfiguration.MIXED_MAPPER::equalsIgnoreCase)
                    .map(ct -> initializeSubBuilder(new MixedXmlMapperGenerator.Builder()).build())
                    .orElseGet(() -> initializeSubBuilder(new XMLMapperGenerator.Builder()).build());
        }
    }

    protected void calculateClientGenerator() {
        if (!introspectedTable.getRules().generateJavaClient()) {
            return;
        }

        calculateJavaClientGeneratorBuilderType().ifPresent(t -> {
            var generator = buildClientGenerator(t);
            javaGenerators.add(generator);
        });
    }

    private <T extends AbstractJavaGenerator.AbstractJavaGeneratorBuilder<T>>
            AbstractJavaGenerator buildClientGenerator(String builderType) {
        T builder = ObjectFactory.createInternalObject(builderType);

        return initializeSubBuilder(builder)
                .withProject(getClientProject().orElseThrow(() ->
                        new InternalException(getString("RuntimeError.25", context.getId()))) //$NON-NLS-1$
                )
                .build();
    }

    protected Optional<String> calculateJavaClientGeneratorBuilderType() {
        return context.getJavaClientGeneratorConfiguration()
                .flatMap(TypedPropertyHolder::getConfigurationType)
                .map(t -> {
                    if (JavaClientGeneratorConfiguration.XML_MAPPER.equalsIgnoreCase(t)) {
                        return JavaMapperGenerator.Builder.class.getName();
                    } else if (JavaClientGeneratorConfiguration.MIXED_MAPPER.equalsIgnoreCase(t)) {
                        return MixedMapperGenerator.Builder.class.getName();
                    } else if (JavaClientGeneratorConfiguration.ANNOTATED_MAPPER.equalsIgnoreCase(t)) {
                        return AnnotatedMapperGenerator.Builder.class.getName();
                    } else if (JavaClientGeneratorConfiguration.MAPPER.equalsIgnoreCase(t)) {
                        return JavaMapperGenerator.Builder.class.getName();
                    } else {
                        return t;
                    }
                });
    }

    protected void calculateJavaModelGenerators() {
        if (introspectedTable.getRules().generateExampleClass()) {
            var javaGenerator = initializeSubBuilder(new ExampleGenerator.Builder())
                    .withProject(getExampleProject())
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            var javaGenerator = initializeSubBuilder(new PrimaryKeyGenerator.Builder())
                    .withProject(getModelProject())
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (introspectedTable.getRules().generateBaseRecordClass()) {
            var javaGenerator = initializeSubBuilder(new BaseRecordGenerator.Builder())
                    .withProject(getModelProject())
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            var javaGenerator = initializeSubBuilder(new RecordWithBLOBsGenerator.Builder())
                    .withProject(getModelProject())
                    .build();
            javaGenerators.add(javaGenerator);
        }
    }

    protected String getExampleProject() {
        String project = context.getJavaModelGeneratorConfiguration()
                .getProperty(PropertyRegistry.MODEL_GENERATOR_EXAMPLE_PROJECT);

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
