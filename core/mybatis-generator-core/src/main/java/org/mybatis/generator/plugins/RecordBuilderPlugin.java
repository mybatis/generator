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
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelRecord;

public class RecordBuilderPlugin extends PluginAdapter {
    private static final FullyQualifiedJavaType BUILDER_TYPE = new FullyQualifiedJavaType("Builder"); //$NON-NLS-1$

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelRecordGenerated(TopLevelRecord topLevelRecord, IntrospectedTable introspectedTable) {
        if (Boolean.parseBoolean(introspectedTable.getTableConfigurationProperty("skipRecordBuilder"))) { //$NON-NLS-1$
            // TODO - document this property - good name?
            return true;
        }

        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        if (allColumns.size() < 4) {
            // TODO - make this configurable
            return true;
        }

        Method newBuilder = generateNewBuilderMethod();
        commentGenerator.addGeneralMethodAnnotation(newBuilder, introspectedTable, topLevelRecord.getImportedTypes());
        topLevelRecord.addMethod(newBuilder);

        InnerClass innerClass = generateBuilderClass(introspectedTable, allColumns);
        commentGenerator.addClassAnnotation(innerClass, introspectedTable, topLevelRecord.getImportedTypes());
        topLevelRecord.addInnerClass(innerClass);
        return true;
    }

    private Method generateNewBuilderMethod() {
        Method method = new Method("newBuilder"); //$NON-NLS-1$
        method.setStatic(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(BUILDER_TYPE);
        method.addBodyLine("return new Builder();"); //$NON-NLS-1$
        return method;
    }

    private InnerClass generateBuilderClass(IntrospectedTable table, List<IntrospectedColumn> columns) {
        InnerClass innerClass = new InnerClass(BUILDER_TYPE);
        innerClass.setStatic(true);
        innerClass.setVisibility(JavaVisibility.PUBLIC);

        for (IntrospectedColumn column : columns) {
            innerClass.addField(generateBuilderField(column));
            innerClass.addMethod(generateBuilderMethod(column));
        }

        innerClass.addMethod(generateBuildMethod(table, columns));

        return innerClass;
    }

    private Field generateBuilderField(IntrospectedColumn column) {
        Field field = new Field(column.getJavaProperty(), column.getFullyQualifiedJavaType());
        field.setVisibility(JavaVisibility.PRIVATE);
        return field;
    }

    private Method generateBuilderMethod(IntrospectedColumn column) {
        Method method = new Method(column.getJavaProperty());
        method.setReturnType(BUILDER_TYPE);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(column.getFullyQualifiedJavaType(), column.getJavaProperty()));
        method.addBodyLine(String.format("this.%s = %s;", column.getJavaProperty(), column.getJavaProperty())); //$NON-NLS-1$
        method.addBodyLine("return this;"); //$NON-NLS-1$
        return method;
    }

    private Method generateBuildMethod(IntrospectedTable table, List<IntrospectedColumn> columns) {
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(table.getBaseRecordType());
        Method method = new Method("build"); //$NON-NLS-1$
        method.setReturnType(returnType);
        method.setVisibility(JavaVisibility.PUBLIC);

        String line = columns.stream()
                .map(IntrospectedColumn::getJavaProperty)
                .collect(Collectors.joining(", ", "return new " + returnType.getShortName() + "(", ");"));
        method.addBodyLine(line);
        return method;
    }
}
