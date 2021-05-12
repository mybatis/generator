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

public class UpdateSelectiveColumnsMethodGenerator extends AbstractKotlinFunctionGenerator {
    private final FullyQualifiedKotlinType recordType;
    private final KotlinFragmentGenerator fragmentGenerator;

    private UpdateSelectiveColumnsMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
        fragmentGenerator = builder.fragmentGenerator;
    }

    @Override
    public KotlinFunctionAndImports generateMethodAndImports() {

        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction("KotlinUpdateBuilder.updateSelectiveColumns") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("row") //$NON-NLS-1$
                        .withDataType(recordType.getShortNameWithTypeArguments())
                        .build())
                .build())
                .withImport("org.mybatis.dynamic.sql.util.kotlin.KotlinUpdateBuilder") //$NON-NLS-1$
                .withImports(recordType.getImportList())
                .build();

        addFunctionComment(functionAndImports);

        KotlinFunction function = functionAndImports.getFunction();

        function.addCodeLine("apply {"); //$NON-NLS-1$

        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        KotlinFunctionParts functionParts = fragmentGenerator.getSetEqualWhenPresentLines(columns);

        acceptParts(functionAndImports, functionParts);

        function.addCodeLine("}"); //$NON-NLS-1$

        return functionAndImports;
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return context.getPlugins().clientUpdateSelectiveColumnsMethodGenerated(kotlinFunction, kotlinFile,
                introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private FullyQualifiedKotlinType recordType;
        private KotlinFragmentGenerator fragmentGenerator;

        public Builder withRecordType(FullyQualifiedKotlinType recordType) {
            this.recordType = recordType;
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

        public UpdateSelectiveColumnsMethodGenerator build() {
            return new UpdateSelectiveColumnsMethodGenerator(this);
        }
    }
}
