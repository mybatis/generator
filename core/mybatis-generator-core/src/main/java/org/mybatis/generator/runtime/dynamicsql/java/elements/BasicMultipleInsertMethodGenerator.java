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

public class BasicMultipleInsertMethodGenerator extends AbstractJavaInterfaceMethodGenerator {

    private final FullyQualifiedJavaType recordType;

    private BasicMultipleInsertMethodGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        if (!introspectedTable.getRules().generateMultipleRowInsertForDSQL()) {
            return Optional.empty();
        }

        return introspectedTable.getGeneratedKey().map(this::generateMethodWithGeneratedKeys);
    }

    private JavaMethodAndImports generateMethodWithGeneratedKeys(GeneratedKey gk) {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter")); //$NON-NLS-1$)
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.InsertProvider")); //$NON-NLS-1$
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$

        Parameter parm1 = new Parameter(FullyQualifiedJavaType.getStringInstance(), "insertStatement"); //$NON-NLS-1$
        parm1.addAnnotation("@Param(\"insertStatement\")"); //$NON-NLS-1$

        FullyQualifiedJavaType recordListType = FullyQualifiedJavaType.getNewListInstance();
        recordListType.addTypeArgument(recordType);
        imports.add(recordListType);

        Parameter parm2 = new Parameter(recordListType, "records"); //$NON-NLS-1$
        parm2.addAnnotation("@Param(\"records\")"); //$NON-NLS-1$

        Method method = new Method("insertMultiple"); //$NON-NLS-1$
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(parm1);
        method.addParameter(parm2);
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@InsertProvider(type=SqlProviderAdapter.class, " //$NON-NLS-1$
                + "method=\"insertMultipleWithGeneratedKeys\")"); //$NON-NLS-1$

        JavaMethodAndImports.Builder builder = JavaMethodAndImports.withMethod(method)
                .withImports(imports);

        GeneratedKeyAnnotationUtility.getJavaMultiRowGeneratedKeyAnnotation(introspectedTable, gk)
                .ifPresent(builder::withExtraMethodParts);

        return builder.build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientBasicInsertMultipleMethodGenerated(method, interfaze, introspectedTable);
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

        public BasicMultipleInsertMethodGenerator build() {
            return new BasicMultipleInsertMethodGenerator(this);
        }
    }
}
