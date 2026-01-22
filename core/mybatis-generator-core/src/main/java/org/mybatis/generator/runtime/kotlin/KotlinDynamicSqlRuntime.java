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

import org.mybatis.generator.api.AbstractRuntime;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;

public class KotlinDynamicSqlRuntime extends AbstractRuntime {
    public KotlinDynamicSqlRuntime(Builder builder) {
        super(builder);
    }

    @Override
    protected void calculateGenerators() {
        kotlinGenerators.add(calculateKotlinDataClassGenerator());
        if (introspectedTable.getRules().generateJavaClient()) {
            getClientProject().map(this::calculateKotlinMapperAndExtensionsGenerator).ifPresent(kotlinGenerators::add);
        }
    }

    protected AbstractKotlinGenerator calculateKotlinMapperAndExtensionsGenerator(String clientProject) {
        return initializeSubBuilder(new KotlinMapperAndExtensionsGenerator.Builder())
                .withProject(clientProject)
                .build();
    }

    protected AbstractKotlinGenerator calculateKotlinDataClassGenerator() {
        return initializeSubBuilder(new KotlinDataClassGenerator.Builder())
                .withProject(getModelProject())
                .build();
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
