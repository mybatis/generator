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

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.runtime.KotlinFunctionAndImports;
import org.mybatis.generator.runtime.dynamicsql.DynamicSqlUtils;

public class InsertMultipleVarargExtensionFunctionGenerator extends AbstractKotlinMapperFunctionGenerator {
    private final FullyQualifiedKotlinType recordType;
    private final String mapperName;

    private InsertMultipleVarargExtensionFunctionGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        mapperName = Objects.requireNonNull(builder.mapperName);
    }

    @Override
    public Optional<KotlinFunctionAndImports> generateFunctionAndImports() {
        if (!DynamicSqlUtils.generateMultipleRowInsert(introspectedTable)) {
            return Optional.empty();
        }

        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction(mapperName + ".insertMultiple") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("vararg records") //$NON-NLS-1$
                        .withDataType(recordType.getShortNameWithTypeArguments()) //$NON-NLS-1$ //$NON-NLS-2$
                        .build())
                .withCodeLine("insertMultiple(records.toList())") //$NON-NLS-1$
                .build())
                .withImports(recordType.getImportList())
                .build();

        addFunctionComment(functionAndImports);
        return Optional.of(functionAndImports);
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return pluginAggregator.clientInsertMultipleVarargMethodGenerated(kotlinFunction, kotlinFile,
                introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private @Nullable FullyQualifiedKotlinType recordType;
        private @Nullable String mapperName;

        public Builder withRecordType(FullyQualifiedKotlinType recordType) {
            this.recordType = recordType;
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

        public InsertMultipleVarargExtensionFunctionGenerator build() {
            return new InsertMultipleVarargExtensionFunctionGenerator(this);
        }
    }
}
