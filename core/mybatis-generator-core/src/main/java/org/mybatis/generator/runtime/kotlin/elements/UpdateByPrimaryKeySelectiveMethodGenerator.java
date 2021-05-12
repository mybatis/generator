/*
 *    Copyright 2006-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.runtime.kotlin.elements;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.runtime.dynamic.sql.elements.Utils;

public class UpdateByPrimaryKeySelectiveMethodGenerator extends AbstractKotlinFunctionGenerator {
    private final FullyQualifiedKotlinType recordType;
    private final KotlinFragmentGenerator fragmentGenerator;
    private final String mapperName;

    private UpdateByPrimaryKeySelectiveMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
        fragmentGenerator = builder.fragmentGenerator;
        mapperName = builder.mapperName;
    }

    @Override
    public KotlinFunctionAndImports generateMethodAndImports() {
        if (!Utils.generateUpdateByPrimaryKey(introspectedTable)) {
            return null;
        }

        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction(mapperName + ".updateByPrimaryKeySelective") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("row") //$NON-NLS-1$
                        .withDataType(recordType.getShortNameWithTypeArguments())
                        .build())
                .withCodeLine("update {") //$NON-NLS-1$
                .build())
                .withImports(recordType.getImportList())
                .build();

        addFunctionComment(functionAndImports);

        List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
        KotlinFunctionParts functionParts = fragmentGenerator.getSetEqualWhenPresentLines(columns);
        acceptParts(functionAndImports, functionParts);

        functionParts = fragmentGenerator.getPrimaryKeyWhereClauseForUpdate();
        acceptParts(functionAndImports, functionParts);

        return functionAndImports;
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return context.getPlugins()
                .clientUpdateByPrimaryKeySelectiveMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private FullyQualifiedKotlinType recordType;
        private KotlinFragmentGenerator fragmentGenerator;
        private String mapperName;

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

        public UpdateByPrimaryKeySelectiveMethodGenerator build() {
            return new UpdateByPrimaryKeySelectiveMethodGenerator(this);
        }
    }
}
