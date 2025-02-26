/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.runtime.kotlin.elements;

import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;

public class BasicInsertMethodGenerator extends AbstractKotlinFunctionGenerator {

    private final FullyQualifiedKotlinType recordType;
    private final KotlinFragmentGenerator fragmentGenerator;

    private BasicInsertMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
        fragmentGenerator = builder.fragmentGenerator;
    }

    @Override
    public KotlinFunctionAndImports generateMethodAndImports() {
        String parameterType = "InsertStatementProvider<" //$NON-NLS-1$
                + recordType.getShortNameWithTypeArguments()
                + ">"; //$NON-NLS-1$

        KotlinFunction function = KotlinFunction.newOneLineFunction("insert") //$NON-NLS-1$
                .withExplicitReturnType("Int") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("insertStatement") //$NON-NLS-1$
                        .withDataType(parameterType)
                        .build())
                .withAnnotation("@InsertProvider(type=SqlProviderAdapter::class, method=\"insert\")") //$NON-NLS-1$
                .build();

        KotlinFunctionAndImports.Builder functionAndImportsBuilder = KotlinFunctionAndImports.withFunction(function)
                .withImport("org.mybatis.dynamic.sql.util.SqlProviderAdapter") //$NON-NLS-1$
                .withImport("org.apache.ibatis.annotations.InsertProvider") //$NON-NLS-1$
                .withImport("org.mybatis.dynamic.sql.insert.render.InsertStatementProvider") //$NON-NLS-1$
                .withImports(recordType.getImportList());

        introspectedTable.getGeneratedKey().ifPresent(gk -> {
            KotlinFunctionParts functionParts = fragmentGenerator.getGeneratedKeyAnnotation(gk);
            // need to add imports to function and imports
            acceptParts(function, functionParts);
            functionAndImportsBuilder.withImports(functionParts.getImports());
        });

        KotlinFunctionAndImports functionAndImports = functionAndImportsBuilder.build();
        addFunctionComment(functionAndImports);
        return functionAndImports;
    }

    @Override
    public boolean callPlugins(KotlinFunction function, KotlinFile kotlinFile) {
        return context.getPlugins().clientBasicInsertMethodGenerated(function, kotlinFile, introspectedTable);
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

        public BasicInsertMethodGenerator build() {
            return new BasicInsertMethodGenerator(this);
        }
    }
}
