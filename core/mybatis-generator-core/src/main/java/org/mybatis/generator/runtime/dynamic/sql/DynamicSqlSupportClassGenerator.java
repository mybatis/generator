/*
 *    Copyright 2006-2023 the original author or authors.
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
package org.mybatis.generator.runtime.dynamic.sql;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class DynamicSqlSupportClassGenerator {
    private IntrospectedTable introspectedTable;
    private CommentGenerator commentGenerator;
    private List<String> warnings;

    private DynamicSqlSupportClassGenerator() {
        super();
    }

    public TopLevelClass generate() {
        TopLevelClass topLevelClass = buildBasicClass();
        Field tableField = calculateTableDefinition(topLevelClass);
        topLevelClass.addImportedType(tableField.getType());
        topLevelClass.addField(tableField);

        InnerClass innerClass = buildInnerTableClass(topLevelClass);
        topLevelClass.addInnerClass(innerClass);

        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            handleColumn(topLevelClass, innerClass, column, tableField.getName());
        }

        return topLevelClass;
    }

    private TopLevelClass buildBasicClass() {
        TopLevelClass topLevelClass = new TopLevelClass(introspectedTable.getMyBatisDynamicSqlSupportType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.setFinal(true);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlColumn")); //$NON-NLS-1$
        topLevelClass.addImportedType(
                new FullyQualifiedJavaType("org.mybatis.dynamic.sql.AliasableSqlTable")); //$NON-NLS-1$
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.sql.JDBCType")); //$NON-NLS-1$
        return topLevelClass;
    }

    private InnerClass buildInnerTableClass(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt =
                new FullyQualifiedJavaType(introspectedTable.getMyBatisDynamicSQLTableObjectName());
        InnerClass innerClass = new InnerClass(fqjt.getShortName());
        innerClass.setVisibility(JavaVisibility.PUBLIC);
        innerClass.setStatic(true);
        innerClass.setFinal(true);
        innerClass.setSuperClass(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.AliasableSqlTable<" //$NON-NLS-1$
                        + fqjt.getShortName() + ">")); //$NON-NLS-1$

        Method method = new Method(fqjt.getShortName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.addBodyLine("super(\"" //$NON-NLS-1$
                + escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())
                + "\", " + fqjt.getShortName() + "::new" //$NON-NLS-1$ //$NON-NLS-2$
                + ");"); //$NON-NLS-1$
        innerClass.addMethod(method);

        commentGenerator.addClassAnnotation(innerClass, introspectedTable, topLevelClass.getImportedTypes());

        return innerClass;
    }

    private Field calculateTableDefinition(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt =
                new FullyQualifiedJavaType(introspectedTable.getMyBatisDynamicSQLTableObjectName());
        String fieldName =
                JavaBeansUtil.getValidPropertyName(introspectedTable.getMyBatisDynamicSQLTableObjectName());
        Field field = new Field(fieldName, fqjt);
        commentGenerator.addFieldAnnotation(field, introspectedTable, topLevelClass.getImportedTypes());
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setStatic(true);
        field.setFinal(true);

        String initializationString = String.format("new %s()", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getMyBatisDynamicSQLTableObjectName()));
        field.setInitializationString(initializationString);
        return field;
    }

    private void handleColumn(TopLevelClass topLevelClass, InnerClass innerClass,
            IntrospectedColumn column, String tableFieldName) {
        topLevelClass.addImportedType(column.getFullyQualifiedJavaType());

        FullyQualifiedJavaType javaType;
        if (column.getFullyQualifiedJavaType().isPrimitive()) {
            javaType = column.getFullyQualifiedJavaType().getPrimitiveTypeWrapper();
        } else {
            javaType = column.getFullyQualifiedJavaType();
        }

        FullyQualifiedJavaType fieldType = calculateFieldType(javaType);
        String fieldName = column.getJavaProperty();

        if (fieldName.equals(tableFieldName)) {
            // name collision, skip the shortcut field
            warnings.add(
                    Messages.getString("Warning.29", //$NON-NLS-1$
                            fieldName, topLevelClass.getType().getFullyQualifiedName()));
        } else {
            // shortcut field
            Field field = new Field(fieldName, fieldType);
            field.setVisibility(JavaVisibility.PUBLIC);
            field.setStatic(true);
            field.setFinal(true);
            field.setInitializationString(tableFieldName + "." + fieldName); //$NON-NLS-1$
            commentGenerator.addFieldAnnotation(field, introspectedTable, column, topLevelClass.getImportedTypes());
            topLevelClass.addField(field);
        }

        // inner class field
        Field field = new Field(fieldName, fieldType);
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setFinal(true);
        field.setInitializationString(calculateInnerInitializationString(column, javaType));
        innerClass.addField(field);
    }

    private FullyQualifiedJavaType calculateFieldType(FullyQualifiedJavaType javaType) {
        return new FullyQualifiedJavaType(String.format("SqlColumn<%s>", javaType.getShortName())); //$NON-NLS-1$
    }

    private String calculateInnerInitializationString(IntrospectedColumn column, FullyQualifiedJavaType javaType) {
        StringBuilder initializationString = new StringBuilder();

        initializationString.append(String.format("column(\"%s\", JDBCType.%s", //$NON-NLS-1$ //$NON-NLS-2$
                escapeStringForJava(getEscapedColumnName(column)),
                column.getJdbcTypeName()));

        if (StringUtility.stringHasValue(column.getTypeHandler())) {
            initializationString.append(String.format(", \"%s\")", column.getTypeHandler())); //$NON-NLS-1$
        } else {
            initializationString.append(')'); //$NON-NLS-1$
        }


        if (StringUtility.isTrue(
                column.getProperties().getProperty(PropertyRegistry.COLUMN_OVERRIDE_FORCE_JAVA_TYPE))) {
            initializationString.append(".withJavaType("); //$NON-NLS-1$
            initializationString.append(javaType.getShortName());
            initializationString.append(".class)"); //$NON-NLS-1$
        }

        return initializationString.toString();
    }

    public static DynamicSqlSupportClassGenerator of(IntrospectedTable introspectedTable,
            CommentGenerator commentGenerator, List<String> warnings) {
        DynamicSqlSupportClassGenerator generator = new DynamicSqlSupportClassGenerator();
        generator.introspectedTable = introspectedTable;
        generator.commentGenerator = commentGenerator;
        generator.warnings = warnings;
        return generator;
    }
}
