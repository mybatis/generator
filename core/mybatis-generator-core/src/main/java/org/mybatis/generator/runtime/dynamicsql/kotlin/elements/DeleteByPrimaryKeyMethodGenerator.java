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
package org.mybatis.generator.runtime.dynamicsql.kotlin.elements;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.runtime.KotlinFunctionAndImports;
import org.mybatis.generator.runtime.dynamicsql.DynamicSqlUtils;

public class DeleteByPrimaryKeyMethodGenerator extends AbstractKotlinMapperFunctionGenerator {

    private final KotlinFragmentGenerator fragmentGenerator;
    private final String mapperName;

    private DeleteByPrimaryKeyMethodGenerator(Builder builder) {
        super(builder);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
        mapperName = Objects.requireNonNull(builder.mapperName);
    }

    @Override
    public Optional<KotlinFunctionAndImports> generateFunctionAndImports() {
        if (!DynamicSqlUtils.generateDeleteByPrimaryKey(introspectedTable)) {
            return Optional.empty();
        }

        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction(mapperName + ".deleteByPrimaryKey") //$NON-NLS-1$
                .withCodeLine("delete {") //$NON-NLS-1$
                .build())
                .build();

        addFunctionComment(functionAndImports);

        KotlinFunctionParts functionParts = fragmentGenerator.getPrimaryKeyWhereClauseAndParameters(false);
        acceptParts(functionAndImports, functionParts);
        functionAndImports.getFunction().getCodeLines().add("}"); //$NON-NLS-1$

        return Optional.of(functionAndImports);
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return pluginAggregator.clientDeleteByPrimaryKeyMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private @Nullable KotlinFragmentGenerator fragmentGenerator;
        private @Nullable String mapperName;

        public Builder withFragmentGenerator(KotlinFragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        public Builder withMapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public DeleteByPrimaryKeyMethodGenerator build() {
            return new DeleteByPrimaryKeyMethodGenerator(this);
        }
    }
}
