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
package org.mybatis.generator.runtime.dynamicsql.java;

import org.mybatis.generator.api.AbstractRuntime;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.runtime.common.RecordModelGenerator;
import org.mybatis.generator.runtime.common.SimpleModelGenerator;

public class JavaDynamicSqlRuntime extends AbstractRuntime {
    protected JavaDynamicSqlRuntime(Builder builder) {
        super(builder);
        calculateGenerators();
    }

    private void calculateGenerators() {
        javaGenerators.add(calculateJavaModelGenerator());
        if (introspectedTable.getRules().generateJavaClient()) {
            getClientProject().map(this::calculateJavaClientGenerator).ifPresent(javaGenerators::add);
        }
    }

    protected AbstractJavaGenerator calculateJavaClientGenerator(String clientProject) {
        return initializeSubBuilder(new DynamicSqlMapperGenerator.Builder())
                .withProject(clientProject)
                .build();
    }

    protected AbstractJavaGenerator calculateJavaModelGenerator() {
        if (introspectedTable.isRecordBased()) {
            return initializeSubBuilder(new RecordModelGenerator.Builder())
                    .withProject(getModelProject())
                    .build();
        } else {
            return initializeSubBuilder(new SimpleModelGenerator.Builder())
                    .withProject(getModelProject())
                    .build();
        }
    }

    public static class Builder extends AbstractRuntimeBuilder<Builder> {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public JavaDynamicSqlRuntime build() {
            return new JavaDynamicSqlRuntime(this);
        }
    }
}
