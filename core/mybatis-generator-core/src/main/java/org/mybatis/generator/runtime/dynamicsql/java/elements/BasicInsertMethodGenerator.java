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
package org.mybatis.generator.runtime.dynamicsql.java.elements;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;
import org.mybatis.generator.runtime.common.GeneratedKeyAnnotationUtility;

public class BasicInsertMethodGenerator extends AbstractJavaInterfaceMethodGenerator {
    private final FullyQualifiedJavaType recordType;

    private BasicInsertMethodGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        return introspectedTable.getGeneratedKey().map(this::generateMethodWithGeneratedKeys);
    }

    private JavaMethodAndImports generateMethodWithGeneratedKeys(GeneratedKey gk) {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType adapter =
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter"); //$NON-NLS-1$
        FullyQualifiedJavaType annotation =
                new FullyQualifiedJavaType("org.apache.ibatis.annotations.InsertProvider"); //$NON-NLS-1$

        imports.add(new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.insert.render.InsertStatementProvider")); //$NON-NLS-1$
        imports.add(adapter);
        imports.add(annotation);

        FullyQualifiedJavaType parameterType =
                new FullyQualifiedJavaType(
                        "org.mybatis.dynamic.sql.insert.render.InsertStatementProvider"); //$NON-NLS-1$
        imports.add(recordType);
        parameterType.addTypeArgument(recordType);

        Method method = new Method("insert"); //$NON-NLS-1$
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(parameterType, "insertStatement")); //$NON-NLS-1$
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@InsertProvider(type=SqlProviderAdapter.class, method=\"insert\")"); //$NON-NLS-1$

        JavaMethodAndImports.Builder builder = JavaMethodAndImports.withMethod(method)
                .withImports(imports);

        GeneratedKeyAnnotationUtility.getJavaSingleRowGeneratedKeyAnnotation(introspectedTable, gk)
                .ifPresent(builder::withExtraMethodParts);

        return builder.build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientBasicInsertMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FullyQualifiedJavaType recordType;

        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
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
