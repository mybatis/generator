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
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.runtime.KotlinFunctionAndImports;
import org.mybatis.generator.runtime.KotlinFunctionParts;

public class InsertExtensionFunctionGenerator extends AbstractKotlinMapperFunctionGenerator {
    private final FullyQualifiedKotlinType recordType;
    private final String mapperName;
    private final KotlinFragmentGenerator fragmentGenerator;

    private InsertExtensionFunctionGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        mapperName = Objects.requireNonNull(builder.mapperName);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
    }

    @Override
    public Optional<KotlinFunctionAndImports> generateFunctionAndImports() {
        Set<String> imports = new HashSet<>();
        imports.add("org.mybatis.dynamic.sql.util.kotlin.mybatis3.insert");

        KotlinFunctionParts functionBody = fragmentGenerator.generateInsertBody();

        KotlinFunction function = KotlinFunction.newOneLineFunction(mapperName + ".insert") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("row") //$NON-NLS-1$
                        .withDataType(recordType.getShortNameWithTypeArguments())
                        .build())
                .withCodeLines(functionBody.getCodeLines())
                .build();

        commentGenerator.addGeneralFunctionComment(function, introspectedTable, imports);

        return KotlinFunctionAndImports.withFunction(function)
                .withImports(imports)
                .withImports(functionBody.getImports())
                .withImports(recordType.getImportList())
                .buildOptional();
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return pluginAggregator.clientInsertMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private @Nullable FullyQualifiedKotlinType recordType;
        private @Nullable String mapperName;
        private @Nullable KotlinFragmentGenerator fragmentGenerator;

        public Builder withRecordType(FullyQualifiedKotlinType recordType) {
            this.recordType = recordType;
            return this;
        }

        public Builder withMapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        public Builder withFragmentGenerator(KotlinFragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public InsertExtensionFunctionGenerator build() {
            return new InsertExtensionFunctionGenerator(this);
        }
    }
}
