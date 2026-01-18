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
package org.mybatis.generator.runtime.dynamic.sql;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.runtime.mybatis3.LegacyJavaRuntime;

public class JavaDynamicSqlRuntime extends LegacyJavaRuntime {

    protected JavaDynamicSqlRuntime(Builder builder) {
        super(builder);
    }

    @Override
    protected void calculateXmlMapperGenerator(@Nullable AbstractJavaClientGenerator javaClientGenerator) {
        // no XML with dynamic SQL support
        xmlMapperGenerator = null;
    }

    @Override
    protected Optional<String> calculateJavaClientGeneratorBuilderType() {
        return context.getJavaClientGeneratorConfiguration()
                .map(c -> DynamicSqlMapperGenerator.Builder.class.getName());
    }

    @Override
    protected void calculateJavaModelGenerators() {
        var javaGenerator = initializeSubBuilder(new DynamicSqlModelGenerator.Builder())
                .withProject(getModelProject())
                .build();
        javaGenerators.add(javaGenerator);
    }

    public static class Builder extends LegacyJavaRuntime.Builder {
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
