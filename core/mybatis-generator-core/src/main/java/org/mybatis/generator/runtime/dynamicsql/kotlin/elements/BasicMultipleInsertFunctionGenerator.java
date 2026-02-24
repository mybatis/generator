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
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.runtime.KotlinFunctionAndImports;
import org.mybatis.generator.runtime.common.GeneratedKeyAnnotationUtility;

public class BasicMultipleInsertFunctionGenerator extends AbstractKotlinMapperFunctionGenerator {
    private final FullyQualifiedKotlinType recordType;

    private BasicMultipleInsertFunctionGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
    }

    @Override
    public Optional<KotlinFunctionAndImports> generateFunctionAndImports() {
        if (!introspectedTable.getRules().generateMultipleRowInsertForDSQL()) {
            return Optional.empty();
        }

        return introspectedTable.getGeneratedKey().map(this::generateMethodWithGeneratedKeys);
    }

    private KotlinFunctionAndImports generateMethodWithGeneratedKeys(GeneratedKey gk) {
        Set<String> imports = new HashSet<>();
        imports.add("org.mybatis.dynamic.sql.util.SqlProviderAdapter"); //$NON-NLS-1$
        imports.add("org.apache.ibatis.annotations.InsertProvider"); //$NON-NLS-1$
        imports.add("org.apache.ibatis.annotations.Param"); //$NON-NLS-1$

        KotlinFunction function = KotlinFunction.newOneLineFunction("insertMultiple") //$NON-NLS-1$
                .withExplicitReturnType("Int") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("insertStatement") //$NON-NLS-1$
                        .withAnnotation("@Param(\"insertStatement\")") //$NON-NLS-1$
                        .withDataType("String") //$NON-NLS-1$
                        .build())
                .withArgument(KotlinArg.newArg("records") //$NON-NLS-1$
                        .withAnnotation("@Param(\"records\")") //$NON-NLS-1$
                        .withDataType("List<" //$NON-NLS-1$
                                + recordType.getShortNameWithTypeArguments()
                                + ">") //$NON-NLS-1$
                        .build())
                .withAnnotation("@InsertProvider(type = SqlProviderAdapter::class," //$NON-NLS-1$
                        + " method = \"insertMultipleWithGeneratedKeys\")") //$NON-NLS-1$
                .build();

        commentGenerator.addGeneralFunctionComment(function, introspectedTable, imports);

        KotlinFunctionAndImports.Builder builder = KotlinFunctionAndImports.withFunction(function)
                .withImports(imports)
                .withImports(recordType.getImportList());

        GeneratedKeyAnnotationUtility.getKotlinMultiRowGeneratedKeyAnnotation(introspectedTable, gk)
                .ifPresent(builder::withExtraFunctionParts);

        return builder.build();
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return pluginAggregator.clientBasicInsertMultipleMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private @Nullable FullyQualifiedKotlinType recordType;

        public Builder withRecordType(FullyQualifiedKotlinType recordType) {
            this.recordType = recordType;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public BasicMultipleInsertFunctionGenerator build() {
            return new BasicMultipleInsertFunctionGenerator(this);
        }
    }
}
