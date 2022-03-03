/*
 *    Copyright 2006-2022 the original author or authors.
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
package org.mybatis.generator.runtime.kotlin;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForKotlin;

import java.util.List;
import java.util.Objects;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.JavaToKotlinTypeConverter;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class KotlinDynamicSqlSupportClassGenerator {
    private final IntrospectedTable introspectedTable;
    private final Context context;
    private final List<String> warnings;
    private KotlinFile kotlinFile;
    private KotlinType innerClass;
    private KotlinType outerObject;
    private KotlinProperty tableProperty;

    public KotlinDynamicSqlSupportClassGenerator(Context context, IntrospectedTable introspectedTable,
                                                 List<String> warnings) {
        this.introspectedTable = Objects.requireNonNull(introspectedTable);
        this.context = Objects.requireNonNull(context);
        this.warnings = Objects.requireNonNull(warnings);
        generate();
    }

    private void generate() {
        FullyQualifiedJavaType type =
                new FullyQualifiedJavaType(introspectedTable.getMyBatisDynamicSqlSupportType());

        kotlinFile = buildBasicFile(type);

        outerObject = buildOuterObject(kotlinFile, type);

        tableProperty = calculateTableProperty();
        outerObject.addNamedItem(tableProperty);

        innerClass = buildInnerClass();

        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            handleColumn(kotlinFile, outerObject, innerClass, getTablePropertyName(), column);
        }

        outerObject.addNamedItem(innerClass);
    }

    public KotlinFile getKotlinFile() {
        return kotlinFile;
    }

    public String getTablePropertyName() {
        return tableProperty.getName();
    }

    public KotlinType getInnerClass() {
        return innerClass;
    }

    public KotlinType getOuterObject() {
        return outerObject;
    }

    public String getTablePropertyImport() {
        return getSupportObjectImport()
                + "." //$NON-NLS-1$
                + tableProperty.getName();
    }

    public String getSupportObjectImport() {
        return kotlinFile.getPackage().map(s -> s + ".").orElse("") //$NON-NLS-1$ //$NON-NLS-2$
                + outerObject.getName();
    }

    private KotlinFile buildBasicFile(FullyQualifiedJavaType type) {
        KotlinFile kf = new KotlinFile(type.getShortNameWithoutTypeArguments());
        kf.setPackage(type.getPackageName());
        context.getCommentGenerator().addFileComment(kf);

        return kf;
    }

    private KotlinType buildOuterObject(KotlinFile kotlinFile, FullyQualifiedJavaType type) {
        KotlinType outerObject = KotlinType.newObject(type.getShortNameWithoutTypeArguments())
                .build();

        kotlinFile.addImport("org.mybatis.dynamic.sql.AliasableSqlTable"); //$NON-NLS-1$
        kotlinFile.addImport("org.mybatis.dynamic.sql.util.kotlin.elements.column"); //$NON-NLS-1$
        kotlinFile.addImport("java.sql.JDBCType"); //$NON-NLS-1$
        kotlinFile.addNamedItem(outerObject);
        return outerObject;
    }


    private KotlinType buildInnerClass() {
        String domainObjectName = introspectedTable.getMyBatisDynamicSQLTableObjectName();

        return KotlinType.newClass(domainObjectName)
                .withSuperType("AliasableSqlTable<" + domainObjectName + ">(\"" //$NON-NLS-1$ //$NON-NLS-2$
                        + escapeStringForKotlin(introspectedTable.getFullyQualifiedTableNameAtRuntime())
                        + "\", ::" + domainObjectName //$NON-NLS-1$
                        + ")") //$NON-NLS-1$
                .build();
    }

    private KotlinProperty calculateTableProperty() {
        String tableType = introspectedTable.getMyBatisDynamicSQLTableObjectName();
        String fieldName =
                JavaBeansUtil.getValidPropertyName(introspectedTable.getMyBatisDynamicSQLTableObjectName());

        return KotlinProperty.newVal(fieldName)
                .withInitializationString(tableType + "()") //$NON-NLS-1$
                .build();
    }

    private void handleColumn(KotlinFile kotlinFile, KotlinType outerObject, KotlinType innerClass,
            String tableFieldName, IntrospectedColumn column) {

        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(column.getFullyQualifiedJavaType());

        kotlinFile.addImports(kt.getImportList());

        String fieldName = column.getJavaProperty();

        // outer object
        if (fieldName.equals(tableFieldName)) {
            // name collision, skip the shortcut field
            warnings.add(
                    Messages.getString("Warning.29", //$NON-NLS-1$
                            fieldName, getSupportObjectImport()));
        } else {
            KotlinProperty prop = KotlinProperty.newVal(fieldName)
                    .withInitializationString(tableFieldName + "." + fieldName)
                    .build();
            outerObject.addNamedItem(prop);
        }


        // inner class
        KotlinProperty property = KotlinProperty.newVal(fieldName)
                .withInitializationString(calculateInnerInitializationString(column, kt))
                .build();

        innerClass.addNamedItem(property);
    }

    private String calculateInnerInitializationString(IntrospectedColumn column, FullyQualifiedKotlinType kt) {
        StringBuilder initializationString = new StringBuilder();

        initializationString.append(String.format("column<%s>(name = \"%s\", jdbcType = JDBCType.%s", //$NON-NLS-1$
                kt.getShortNameWithTypeArguments(),
                escapeStringForKotlin(getEscapedColumnName(column)),
                column.getJdbcTypeName()));

        if (StringUtility.stringHasValue(column.getTypeHandler())) {
            initializationString.append(
                    String.format(", typeHandler = \"%s\"", column.getTypeHandler())); //$NON-NLS-1$
        }

        if (StringUtility.isTrue(
                column.getProperties().getProperty(PropertyRegistry.COLUMN_OVERRIDE_FORCE_JAVA_TYPE))) {
            initializationString.append(
                    String.format(", javaType = %s::class", kt.getShortNameWithoutTypeArguments())); //$NON-NLS-1$
        }

        initializationString.append(')'); //$NON-NLS-1$

        return initializationString.toString();
    }
}
