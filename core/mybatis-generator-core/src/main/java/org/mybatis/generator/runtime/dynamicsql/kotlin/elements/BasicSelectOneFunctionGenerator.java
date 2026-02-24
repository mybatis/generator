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

public class BasicSelectOneFunctionGenerator extends AbstractKotlinMapperFunctionGenerator {

    private final FullyQualifiedKotlinType recordType;
    private final String resultMapId;
    private final KotlinFragmentGenerator fragmentGenerator;
    private final boolean reuseResultMap;

    private BasicSelectOneFunctionGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        resultMapId = Objects.requireNonNull(builder.resultMapId);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
        reuseResultMap = builder.reuseResultMap;
    }

    @Override
    public Optional<KotlinFunctionAndImports> generateFunctionAndImports() {
        Set<String> imports = new HashSet<>();
        imports.add("org.mybatis.dynamic.sql.select.render.SelectStatementProvider"); //$NON-NLS-1$
        imports.add("org.mybatis.dynamic.sql.util.SqlProviderAdapter"); //$NON-NLS-1$
        imports.add("org.apache.ibatis.annotations.SelectProvider"); //$NON-NLS-1$

        KotlinFunction function = KotlinFunction.newOneLineFunction("selectOne") //$NON-NLS-1$
                .withExplicitReturnType(recordType.getShortNameWithTypeArguments() + "?") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("selectStatement") //$NON-NLS-1$
                        .withDataType("SelectStatementProvider") //$NON-NLS-1$
                        .build())
                .withAnnotation("@SelectProvider(type=SqlProviderAdapter::class," //$NON-NLS-1$
                        + " method=\"select\")") //$NON-NLS-1$
                .build();

        commentGenerator.addGeneralFunctionComment(function, introspectedTable, imports);

        KotlinFunctionAndImports.Builder builder = KotlinFunctionAndImports
                .withFunction(function)
                .withImports(imports)
                .withImports(recordType.getImportList());

        if (reuseResultMap) {
            builder.withImport("org.apache.ibatis.annotations.ResultMap"); //$NON-NLS-1$
            function.addAnnotation("@ResultMap(\"" //$NON-NLS-1$
                    + resultMapId
                    + "\")"); //$NON-NLS-1$
        } else {
            builder.withExtraFunctionParts(fragmentGenerator.getAnnotatedResults());
        }

        return builder.buildOptional();
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return pluginAggregator.clientBasicSelectOneMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {

        private @Nullable FullyQualifiedKotlinType recordType;
        private @Nullable String resultMapId;
        private @Nullable KotlinFragmentGenerator fragmentGenerator;
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

        public BasicSelectOneFunctionGenerator build() {
            return new BasicSelectOneFunctionGenerator(this);
        }
    }
}
