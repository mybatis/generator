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
package org.mybatis.generator.runtime.dynamicsql.java.elements;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class GeneralSelectDistinctMethodGenerator extends AbstractJavaInterfaceMethodGenerator {
    private final FragmentGenerator fragmentGenerator;

    private GeneralSelectDistinctMethodGenerator(Builder builder) {
        super(builder);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        return Optional.of(fragmentGenerator.generateGeneralSelectMethod(true));
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientGeneralSelectDistinctMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FragmentGenerator fragmentGenerator;

        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public GeneralSelectDistinctMethodGenerator build() {
            return new GeneralSelectDistinctMethodGenerator(this);
        }
    }
}
