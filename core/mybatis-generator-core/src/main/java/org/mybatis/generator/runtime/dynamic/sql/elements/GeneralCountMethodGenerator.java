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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class GeneralCountMethodGenerator extends AbstractJavaInterfaceMethodGenerator {
    private final String tableFieldName;

    private GeneralCountMethodGenerator(Builder builder) {
        super(builder);
        tableFieldName = Objects.requireNonNull(builder.tableFieldName);
    }

    @Override
    public JavaMethodAndImports generateMethodAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.select.CountDSLCompleter"); //$NON-NLS-1$
        imports.add(parameterType);
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils")); //$NON-NLS-1$

        Method method = new Method("count"); //$NON-NLS-1$
        method.setDefault(true);
        method.addParameter(new Parameter(parameterType, "completer")); //$NON-NLS-1$
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);

        method.setReturnType(new FullyQualifiedJavaType("long")); //$NON-NLS-1$

        method.addBodyLine("return MyBatis3Utils.countFrom(this::count, " + tableFieldName //$NON-NLS-1$
                + ", completer);"); //$NON-NLS-1$

        return JavaMethodAndImports.withMethod(method)
                .withImports(imports)
                .build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientGeneralCountMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable String tableFieldName;

        public Builder withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
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
