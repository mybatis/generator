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

public class UpdateByPrimaryKeyExtensionFunctionGenerator extends AbstractKotlinMapperFunctionGenerator {
    private final FullyQualifiedKotlinType recordType;
    private final KotlinFragmentGenerator fragmentGenerator;
    private final String mapperName;

    private UpdateByPrimaryKeyExtensionFunctionGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
        mapperName = Objects.requireNonNull(builder.mapperName);
    }

    @Override
    public Optional<KotlinFunctionAndImports> generateFunctionAndImports() {
        if (!introspectedTable.getRules().generateUpdateByPrimaryKeyForDSQL()) {
            return Optional.empty();
        }

        Set<String> imports = new HashSet<>();

        KotlinFunction function = KotlinFunction.newOneLineFunction(mapperName + ".updateByPrimaryKey") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("row") //$NON-NLS-1$
                        .withDataType(recordType.getShortNameWithTypeArguments())
                        .build())
                .withCodeLine("update {") //$NON-NLS-1$
                .build();

        commentGenerator.addGeneralFunctionComment(function, introspectedTable, imports);

        return KotlinFunctionAndImports.withFunction(function)
                .withImports(imports)
                .withImports(recordType.getImportList())
                .withExtraFunctionParts(fragmentGenerator.getSetEqualLines(introspectedTable.getNonPrimaryKeyColumns(),
                        false))
                .withExtraFunctionParts(fragmentGenerator.getPrimaryKeyWhereClauseAndParameters(true))
                .buildOptional();
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return pluginAggregator.clientUpdateByPrimaryKeyMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private @Nullable FullyQualifiedKotlinType recordType;
        private @Nullable KotlinFragmentGenerator fragmentGenerator;
        private @Nullable String mapperName;

        public Builder withRecordType(FullyQualifiedKotlinType recordType) {
            this.recordType = recordType;
            return this;
        }

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

        public UpdateByPrimaryKeyExtensionFunctionGenerator build() {
            return new UpdateByPrimaryKeyExtensionFunctionGenerator(this);
        }
    }
}
