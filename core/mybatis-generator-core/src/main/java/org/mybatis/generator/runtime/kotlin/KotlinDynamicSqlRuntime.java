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

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import org.mybatis.generator.api.AbstractRuntime;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;
import org.mybatis.generator.exception.InternalException;

public class KotlinDynamicSqlRuntime extends AbstractRuntime {
    public KotlinDynamicSqlRuntime(Builder builder) {
        super(builder);
    }

    @Override
    protected void calculateGenerators() {
        calculateKotlinDataClassGenerator();
        context.getJavaClientGeneratorConfiguration().ifPresent(config -> {
            if (introspectedTable.getRules().generateJavaClient()) {
                calculateKotlinMapperAndExtensionsGenerator(config.getTargetProject());
            }
        });
    }

    protected void calculateKotlinMapperAndExtensionsGenerator(String clientProject) {
        AbstractKotlinGenerator kotlinGenerator =
                initializeSubBuilder(new KotlinMapperAndExtensionsGenerator.Builder())
                .withProject(clientProject)
                .withKotlinMapperType(
                        introspectedTable.getMyBatis3JavaMapperType().orElseThrow(() ->
                                new InternalException(getString("RuntimeError.26", context.getId()))) //$NON-NLS-1$
                )
                .build();
        kotlinGenerators.add(kotlinGenerator);
    }

    protected void calculateKotlinDataClassGenerator() {
        AbstractKotlinGenerator kotlinGenerator = initializeSubBuilder(new KotlinDataClassGenerator.Builder())
                .withProject(getModelProject())
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
