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

import java.util.Objects;

import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;

public class GeneralCountMethodGenerator extends AbstractKotlinFunctionGenerator {

    private final String mapperName;
    private final String tableFieldImport;

    private GeneralCountMethodGenerator(Builder builder) {
        super(builder);
        mapperName = Objects.requireNonNull(builder.mapperName);
        tableFieldImport = Objects.requireNonNull(builder.tableFieldImport);
    }

    @Override
    public KotlinFunctionAndImports generateMethodAndImports() {
        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction(mapperName + ".count") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("completer") //$NON-NLS-1$
                        .withDataType("CountCompleter") //$NON-NLS-1$
                        .build())
                .withCodeLine("countFrom(this::count, " + tableFieldName + ", completer)") //$NON-NLS-1$ //$NON-NLS-2$
                .build())
                .withImport("org.mybatis.dynamic.sql.util.kotlin.CountCompleter") //$NON-NLS-1$
                .withImport("org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom") //$NON-NLS-1$
                .withImport(tableFieldImport)
                .build();

        addFunctionComment(functionAndImports);
        return functionAndImports;
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return context.getPlugins().clientGeneralCountMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private String mapperName;
        private String tableFieldImport;

        public Builder withTableFieldImport(String tableFieldImport) {
            this.tableFieldImport = tableFieldImport;
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

        public GeneralCountMethodGenerator build() {
            return new GeneralCountMethodGenerator(this);
        }
    }
}
