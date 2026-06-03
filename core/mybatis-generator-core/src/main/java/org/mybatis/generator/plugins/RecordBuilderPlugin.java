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
package org.mybatis.generator.plugins;

import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelRecord;

public class RecordBuilderPlugin extends BaseRecordPlugin {
    private static final FullyQualifiedJavaType BUILDER_TYPE = new FullyQualifiedJavaType("Builder"); //$NON-NLS-1$

    public RecordBuilderPlugin() {
        super("skipRecordBuilderPlugin"); //$NON-NLS-1$
    }

    @Override
    protected void execute(TopLevelRecord topLevelRecord, IntrospectedTable introspectedTable) {
        boolean jspecifyEnabled = JSpecifyPlugin.isEnabled(introspectedTable);
        if (jspecifyEnabled) {
            topLevelRecord.addImportedType(JSpecifyPlugin.NULLABLE_IMPORT);

            if (introspectedTable.getAllColumns().stream().anyMatch(c -> !c.isNullable())) {
                topLevelRecord.addImportedType("java.util.Objects"); //$NON-NLS-1$
            }
        }

        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        Method newBuilder = generateNewBuilderMethod();
        commentGenerator.addGeneralMethodAnnotation(newBuilder, introspectedTable, topLevelRecord.getImportedTypes());
        topLevelRecord.addMethod(newBuilder);

        InnerClass innerClass = generateBuilderClass(introspectedTable, allColumns, jspecifyEnabled);
        commentGenerator.addClassAnnotation(innerClass, introspectedTable, topLevelRecord.getImportedTypes());
        topLevelRecord.addInnerClass(innerClass);
    }

    private Method generateNewBuilderMethod() {
        Method method = new Method("newBuilder"); //$NON-NLS-1$
        method.setStatic(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(BUILDER_TYPE);
        method.addBodyLine("return new Builder();"); //$NON-NLS-1$
        return method;
    }

    private InnerClass generateBuilderClass(IntrospectedTable table, List<IntrospectedColumn> columns,
                                            boolean jspecifyEnabled) {
        InnerClass innerClass = new InnerClass(BUILDER_TYPE);
        innerClass.setStatic(true);
        innerClass.setVisibility(JavaVisibility.PUBLIC);

        for (IntrospectedColumn column : columns) {
            innerClass.addField(generateBuilderField(column, jspecifyEnabled));
            innerClass.addMethod(generateBuilderMethod(column));
        }

        innerClass.addMethod(generateBuildMethod(table, columns, jspecifyEnabled));

        return innerClass;
    }

    private Field generateBuilderField(IntrospectedColumn column, boolean jspecifyEnabled) {
        Field field = new Field(column.getJavaProperty(), column.getFullyQualifiedJavaType());
        if (jspecifyEnabled && !column.getFullyQualifiedJavaType().isPrimitive()) {
            field.addAnnotation(JSpecifyPlugin.NULLABLE_ANNOTATION);
        }
        field.setVisibility(JavaVisibility.PRIVATE);
        return field;
    }

    private Method generateBuilderMethod(IntrospectedColumn column) {
        Method method = new Method(column.getJavaProperty());
        method.setReturnType(BUILDER_TYPE);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(column.getFullyQualifiedJavaType(), column.getJavaProperty()));
        method.addBodyLine("this.%s = %s;".formatted(column.getJavaProperty(), //$NON-NLS-1$
                column.getJavaProperty()));
        method.addBodyLine("return this;"); //$NON-NLS-1$
        return method;
    }

    private Method generateBuildMethod(IntrospectedTable table, List<IntrospectedColumn> columns,
                                       boolean jspecifyEnabled) {
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(table.getBaseRecordType());
        Method method = new Method("build"); //$NON-NLS-1$
        method.setReturnType(returnType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine(calculateReturnNewLine(columns, returnType, jspecifyEnabled));
        return method;
    }

    private String calculateReturnNewLine(List<IntrospectedColumn> columns, FullyQualifiedJavaType returnType,
                                          boolean jspecifyEnabled) {
        String prefix = "return new " + returnType.getShortName() + "("; //$NON-NLS-1$ //$NON-NLS-2$
        return columns.stream()
                .map(c -> calculateParameter(c, jspecifyEnabled))
                .collect(Collectors.joining(", ", prefix, ");")); //$NON-NLS-1$ //$NON-NLS-2$

    }

    private String calculateParameter(IntrospectedColumn column, boolean jspecifyEnabled) {
        if (column.isNullable() || !jspecifyEnabled) {
            return column.getJavaProperty();
        } else {
            return "Objects.requireNonNull(" + column.getJavaProperty() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
