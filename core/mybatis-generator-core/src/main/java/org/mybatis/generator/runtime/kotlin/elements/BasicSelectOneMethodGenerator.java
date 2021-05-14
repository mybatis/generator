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

import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;

public class BasicSelectOneMethodGenerator extends AbstractKotlinFunctionGenerator {

    private final FullyQualifiedKotlinType recordType;
    private final String resultMapId;
    private final KotlinFragmentGenerator fragmentGenerator;
    private final boolean reuseResultMap;

    private BasicSelectOneMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
        resultMapId = builder.resultMapId;
        fragmentGenerator = builder.fragmentGenerator;
        reuseResultMap = builder.reuseResultMap;
    }

    @Override
    public KotlinFunctionAndImports generateMethodAndImports() {
        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports
                .withFunction(KotlinFunction.newOneLineFunction("selectOne") //$NON-NLS-1$
                        .withExplicitReturnType(recordType.getShortNameWithTypeArguments() + "?") //$NON-NLS-1$
                        .withArgument(KotlinArg.newArg("selectStatement") //$NON-NLS-1$
                                .withDataType("SelectStatementProvider") //$NON-NLS-1$
                                .build())
                        .withAnnotation("@SelectProvider(type=SqlProviderAdapter::class," //$NON-NLS-1$
                                + " method=\"select\")") //$NON-NLS-1$
                        .build())
                .withImport("org.mybatis.dynamic.sql.select.render.SelectStatementProvider") //$NON-NLS-1$
                .withImport("org.mybatis.dynamic.sql.util.SqlProviderAdapter") //$NON-NLS-1$
                .withImport("org.apache.ibatis.annotations.SelectProvider") //$NON-NLS-1$
                .withImports(recordType.getImportList())
                .build();

        addFunctionComment(functionAndImports);

        if (reuseResultMap) {
            functionAndImports.getImports().add("org.apache.ibatis.annotations.ResultMap"); //$NON-NLS-1$
            functionAndImports.getFunction().addAnnotation("@ResultMap(\"" //$NON-NLS-1$
                    + resultMapId
                    + "\")"); //$NON-NLS-1$
        } else {
            KotlinFunctionParts functionParts = fragmentGenerator.getAnnotatedResults();
            acceptParts(functionAndImports, functionParts);
        }

        return functionAndImports;
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return context.getPlugins().clientBasicSelectOneMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {

        private FullyQualifiedKotlinType recordType;
        private String resultMapId;
        private KotlinFragmentGenerator fragmentGenerator;
        private boolean reuseResultMap;

        public Builder withRecordType(FullyQualifiedKotlinType recordType) {
            this.recordType = recordType;
            return this;
        }

        public Builder withResultMapId(String resultMapId) {
            this.resultMapId = resultMapId;
            return this;
        }

        public Builder withFragmentGenerator(KotlinFragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        public Builder withReuseResultMap(boolean reuseResultMap) {
            this.reuseResultMap = reuseResultMap;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public BasicSelectOneMethodGenerator build() {
            return new BasicSelectOneMethodGenerator(this);
        }
    }
}
