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

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.runtime.KotlinFunctionAndImports;

public class GeneralUpdateExtensionFunctionGenerator extends AbstractKotlinMapperFunctionGenerator {
    private final String mapperName;

    private GeneralUpdateExtensionFunctionGenerator(Builder builder) {
        super(builder);
        this.mapperName = Objects.requireNonNull(builder.mapperName);
    }

    @Override
    public Optional<KotlinFunctionAndImports> generateFunctionAndImports() {
        Set<String> imports = new HashSet<>();
        imports.add("org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter"); //$NON-NLS-1$
        imports.add("org.mybatis.dynamic.sql.util.kotlin.mybatis3.update"); //$NON-NLS-1$

        KotlinFunction function = KotlinFunction.newOneLineFunction(mapperName + ".update") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("completer") //$NON-NLS-1$
                        .withDataType("UpdateCompleter") //$NON-NLS-1$
                        .build())
                .withCodeLine("update(this::update, " + tableFieldName + ", completer)") //$NON-NLS-1$ //$NON-NLS-2$
                .build();

        commentGenerator.addGeneralFunctionComment(function, introspectedTable, imports);

        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(function)
                .withImports(imports)
                .build();

        return Optional.of(functionAndImports);
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return pluginAggregator.clientGeneralUpdateMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private @Nullable String mapperName;

        public Builder withMapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public GeneralUpdateExtensionFunctionGenerator build() {
            return new GeneralUpdateExtensionFunctionGenerator(this);
        }
    }
}
