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
import java.util.Optional;
import java.util.Set;

import org.apache.tools.ant.taskdefs.Java;
import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class GeneralDeleteMethodGenerator extends AbstractJavaInterfaceMethodGenerator {
    private final String tableFieldName;

    private GeneralDeleteMethodGenerator(Builder builder) {
        super(builder);
        tableFieldName = Objects.requireNonNull(builder.tableFieldName);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.delete.DeleteDSLCompleter"); //$NON-NLS-1$
        imports.add(parameterType);
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils")); //$NON-NLS-1$

        Method method = new Method("delete"); //$NON-NLS-1$
        method.setDefault(true);
        method.addParameter(new Parameter(parameterType, "completer")); //$NON-NLS-1$
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addBodyLine(
                "return MyBatis3Utils.deleteFrom(this::delete, " + tableFieldName //$NON-NLS-1$
                        + ", completer);"); //$NON-NLS-1$

        JavaMethodAndImports answer = JavaMethodAndImports.withMethod(method)
                .withImports(imports)
                .build();

        return Optional.of(answer);
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientGeneralDeleteMethodGenerated(method, interfaze, introspectedTable);
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

        public GeneralDeleteMethodGenerator build() {
            return new GeneralDeleteMethodGenerator(this);
        }
    }
}
