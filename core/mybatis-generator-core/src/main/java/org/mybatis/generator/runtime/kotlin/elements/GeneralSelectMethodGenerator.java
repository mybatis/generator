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

import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;

public class GeneralSelectMethodGenerator extends AbstractKotlinFunctionGenerator {
    private final String mapperName;

    private GeneralSelectMethodGenerator(Builder builder) {
        super(builder);
        mapperName = builder.mapperName;
    }

    @Override
    public KotlinFunctionAndImports generateMethodAndImports() {
        KotlinFunctionAndImports functionAndImports = KotlinFunctionAndImports.withFunction(
                KotlinFunction.newOneLineFunction(mapperName + ".select") //$NON-NLS-1$
                .withArgument(KotlinArg.newArg("completer") //$NON-NLS-1$
                        .withDataType("SelectCompleter") //$NON-NLS-1$
                        .build())
                .withCodeLine("selectList(this::selectMany, columnList, " + tableFieldName //$NON-NLS-1$
                        + ", completer)") //$NON-NLS-1$
                .build())
                .withImport("org.mybatis.dynamic.sql.util.kotlin.SelectCompleter") //$NON-NLS-1$
                .withImport("org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList") //$NON-NLS-1$
                .build();

        addFunctionComment(functionAndImports);
        return functionAndImports;
    }

    @Override
    public boolean callPlugins(KotlinFunction kotlinFunction, KotlinFile kotlinFile) {
        return context.getPlugins().clientGeneralSelectMethodGenerated(kotlinFunction, kotlinFile, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private String mapperName;

        public Builder withMapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public GeneralSelectMethodGenerator build() {
            return new GeneralSelectMethodGenerator(this);
        }
    }
}
