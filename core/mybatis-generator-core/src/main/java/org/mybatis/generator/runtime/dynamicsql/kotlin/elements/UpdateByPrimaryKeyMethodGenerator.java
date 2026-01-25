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

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.runtime.dynamicsql.DynamicSqlUtils;

public class UpdateByPrimaryKeyMethodGenerator extends AbstractKotlinFunctionGenerator {
    private final FullyQualifiedKotlinType recordType;
    private final KotlinFragmentGenerator fragmentGenerator;
    private final String mapperName;

    private UpdateByPrimaryKeyMethodGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
        mapperName = Objects.requireNonNull(builder.mapperName);
    }

    @Override
    public @Nullable KotlinFunctionAndImports generateMethodAndImports() {
        if (!DynamicSqlUtils.generateUpdateByPrimaryKey(introspectedTable)) {
            return null;
        }

        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction(mapperName + ".updateByPrimaryKey") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("row") //$NON-NLS-1$
                        .withDataType(recordType.getShortNameWithTypeArguments())
                        .build())
                .withCodeLine("update {") //$NON-NLS-1$
                .build())
                .withImports(recordType.getImportList())
                .build();

        addFunctionComment(functionAndImports);

        List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
        KotlinFunctionParts functionParts = fragmentGenerator.getSetEqualLines(columns);
        acceptParts(functionAndImports, functionParts);

        functionParts = fragmentGenerator.getPrimaryKeyWhereClauseAndParameters(true);
        acceptParts(functionAndImports, functionParts);
        functionAndImports.getFunction().getCodeLines().add("}"); //$NON-NLS-1$

        return functionAndImports;
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

        public UpdateByPrimaryKeyMethodGenerator build() {
            return new UpdateByPrimaryKeyMethodGenerator(this);
        }
    }
}
