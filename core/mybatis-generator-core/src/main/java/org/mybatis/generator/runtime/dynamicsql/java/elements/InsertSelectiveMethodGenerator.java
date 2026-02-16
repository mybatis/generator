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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.runtime.AbstractJavaInterfaceMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;
import org.mybatis.generator.runtime.dynamicsql.DynamicSqlUtils;
import org.mybatis.generator.runtime.mybatis3.ListUtilities;

public class InsertSelectiveMethodGenerator extends AbstractJavaInterfaceMethodGenerator {
    private final FullyQualifiedJavaType recordType;
    private final String tableFieldName;

    private InsertSelectiveMethodGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        tableFieldName = Objects.requireNonNull(builder.tableFieldName);
    }

    @Override
    public Optional<JavaMethodAndImports> generateMethodAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils")); //$NON-NLS-1$
        imports.add(recordType);

        Method method = new Method("insertSelective"); //$NON-NLS-1$
        method.setDefault(true);
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(recordType, "row")); //$NON-NLS-1$

        method.addBodyLine("return MyBatis3Utils.insert(this::insert, row, " + tableFieldName //$NON-NLS-1$
                + ", c ->"); //$NON-NLS-1$

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(
                introspectedTable.getAllColumns());
        boolean first = true;
        for (IntrospectedColumn column : columns) {
            String fieldName = DynamicSqlUtils.calculateFieldName(tableFieldName, column);
            if (column.isSequenceColumn()) {
                if (first) {
                    method.addBodyLine("    c.map(" + fieldName //$NON-NLS-1$
                            + ").toProperty(\"" + column.getJavaProperty() //$NON-NLS-1$
                            + "\")"); //$NON-NLS-1$
                    first = false;
                } else {
                    method.addBodyLine("    .map(" + fieldName //$NON-NLS-1$
                            + ").toProperty(\"" + column.getJavaProperty() //$NON-NLS-1$
                            + "\")"); //$NON-NLS-1$
                }
            } else {
                String methodName = JavaBeansUtil.getCallingGetterMethodName(column);
                if (first) {
                    method.addBodyLine("    c.map(" + fieldName //$NON-NLS-1$
                            + ").toPropertyWhenPresent(\"" + column.getJavaProperty() //$NON-NLS-1$
                            + "\", row::" + methodName //$NON-NLS-1$
                            + ")"); //$NON-NLS-1$
                    first = false;
                } else {
                    method.addBodyLine("    .map(" + fieldName //$NON-NLS-1$
                            + ").toPropertyWhenPresent(\"" + column.getJavaProperty() //$NON-NLS-1$
                            + "\", row::" + methodName //$NON-NLS-1$
                            + ")"); //$NON-NLS-1$
                }
            }
        }

        method.addBodyLine(");"); //$NON-NLS-1$

        JavaMethodAndImports answer = JavaMethodAndImports.withMethod(method)
                .withImports(imports)
                .build();

        return Optional.of(answer);
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FullyQualifiedJavaType recordType;
        private @Nullable String tableFieldName;

        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }

        public Builder withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public InsertSelectiveMethodGenerator build() {
            return new InsertSelectiveMethodGenerator(this);
        }
    }
}
