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
package org.mybatis.generator.runtime.dynamicsql.kotlin.elements;

import static org.mybatis.generator.api.dom.OutputUtilities.kotlinIndent;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForKotlin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.JavaToKotlinTypeConverter;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.runtime.CodeGenUtils;
import org.mybatis.generator.runtime.dynamicsql.kotlin.KotlinDynamicSqlSupportClassGenerator;
import org.mybatis.generator.runtime.mybatis3.ListUtilities;

public class KotlinFragmentGenerator {

    private final IntrospectedTable introspectedTable;
    private final String resultMapId;
    private final String supportObjectImport;
    private final String tableFieldName;
    private final boolean useSnakeCase;

    private KotlinFragmentGenerator(Builder builder) {
        introspectedTable = Objects.requireNonNull(builder.introspectedTable);
        resultMapId = Objects.requireNonNull(builder.resultMapId);
        supportObjectImport = Objects.requireNonNull(builder.supportObjectImport);
        tableFieldName = Objects.requireNonNull(builder.tableFieldName);
        useSnakeCase = CodeGenUtils.findTableOrClientPropertyAsBoolean(PropertyRegistry.ANY_USE_SNAKE_CASE_IDENTIFIERS,
                introspectedTable);
    }

    public KotlinFunctionParts getPrimaryKeyWhereClauseAndParameters(boolean forUpdate) {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        int columnCount = introspectedTable.getPrimaryKeyColumns().size();
        boolean first = true;
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            String argName;
            if (forUpdate) {
                argName = "row." + column.getJavaProperty() + "!!"; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                String propertyName = column.getJavaProperty();
                if (!useSnakeCase) {
                    propertyName += "_"; //$NON-NLS-1$
                }
                argName = propertyName;
                FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(column.getFullyQualifiedJavaType());
                builder.withImports(kt.getImportList());
                builder.withArgument(KotlinArg.newArg(argName)
                        .withDataType(kt.getShortNameWithTypeArguments())
                        .build());
            }

            FieldNameAndImport fieldNameAndImport = calculateFieldNameAndImport(tableFieldName, supportObjectImport,
                    column);

            builder.withImport(fieldNameAndImport.importString());
            if (columnCount == 1) {
                builder.withCodeLine(singleColumnWhere(fieldNameAndImport.fieldName(), argName));
                first = false;
            } else if (first) {
                builder.withCodeLine(OutputUtilities.kotlinIndent(1) + "where {"); //$NON-NLS-1$
                builder.withCodeLine(multiColumnWhere(fieldNameAndImport.fieldName(), argName));
                first = false;
            } else {
                builder.withCodeLine(multiColumnAnd(fieldNameAndImport.fieldName(), argName));
            }
        }
        if (columnCount > 1) {
            builder.withCodeLine(OutputUtilities.kotlinIndent(1) + "}"); //$NON-NLS-1$
        }

        return builder.build();
    }

    private String singleColumnWhere(String columName, String property) {
        return OutputUtilities.kotlinIndent(1) + "where { " //$NON-NLS-1$
                + composeIsEqualTo(columName, property)  + " }"; //$NON-NLS-1$
    }

    private String multiColumnWhere(String columName, String property) {
        return OutputUtilities.kotlinIndent(2) + composeIsEqualTo(columName, property); //$NON-NLS-1$
    }

    private String multiColumnAnd(String columName, String property) {
        return OutputUtilities.kotlinIndent(2) + "and { " //$NON-NLS-1$
                + composeIsEqualTo(columName, property)  + " }"; //$NON-NLS-1$
    }

    private String composeIsEqualTo(String columName, String property) {
        return columName + " isEqualTo " + property; //$NON-NLS-1$
    }

    public KotlinFunctionParts getAnnotatedResults() {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        builder.withImport("org.apache.ibatis.type.JdbcType"); //$NON-NLS-1$
        builder.withImport("org.apache.ibatis.annotations.Result"); //$NON-NLS-1$
        builder.withImport("org.apache.ibatis.annotations.Results"); //$NON-NLS-1$

        builder.withAnnotation("@Results(id=\"" + resultMapId + "\", value = ["); //$NON-NLS-1$ //$NON-NLS-2$

        StringBuilder sb = new StringBuilder();

        Set<String> imports = new HashSet<>();
        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            kotlinIndent(sb, 1);
            sb.append(getResultAnnotation(imports, introspectedColumn, true));

            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }

            builder.withAnnotation(sb.toString());
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            kotlinIndent(sb, 1);
            sb.append(getResultAnnotation(imports, introspectedColumn, false));

            if (iterNonPk.hasNext()) {
                sb.append(',');
            }

            builder.withAnnotation(sb.toString());
        }

        builder.withAnnotation("])") //$NON-NLS-1$
                .withImports(imports);

        return builder.build();
    }

    private String getResultAnnotation(Set<String> imports, IntrospectedColumn introspectedColumn,
            boolean idColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("Result(column=\""); //$NON-NLS-1$
        sb.append(escapeStringForKotlin(introspectedColumn.getActualColumnName()));
        sb.append("\", property=\""); //$NON-NLS-1$
        sb.append(introspectedColumn.getJavaProperty());
        sb.append('\"');

        introspectedColumn.getTypeHandler().ifPresent(th -> {
            imports.add(th);
            sb.append(", typeHandler="); //$NON-NLS-1$
            sb.append(new FullyQualifiedKotlinType(th).getShortNameWithoutTypeArguments());
            sb.append("::class"); //$NON-NLS-1$
        });

        sb.append(", jdbcType=JdbcType."); //$NON-NLS-1$
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true"); //$NON-NLS-1$
        }
        sb.append(')');

        return sb.toString();
    }

    public KotlinFunctionParts getSetEqualLines(List<IntrospectedColumn> columnList) {

        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(columnList);
        for (IntrospectedColumn column : columns) {
            FieldNameAndImport fieldNameAndImport = calculateFieldNameAndImport(tableFieldName, supportObjectImport,
                    column);
            builder.withImport(fieldNameAndImport.importString());

            builder.withCodeLine(OutputUtilities.kotlinIndent(1) + "set(" + fieldNameAndImport.fieldName() //$NON-NLS-1$
                    + ") equalToOrNull row::" + column.getJavaProperty()); //$NON-NLS-1$
        }

        return builder.build();
    }

    public KotlinFunctionParts getSetEqualWhenPresentLines(List<IntrospectedColumn> columnList) {

        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(columnList);
        for (IntrospectedColumn column : columns) {
            FieldNameAndImport fieldNameAndImport = calculateFieldNameAndImport(tableFieldName, supportObjectImport,
                    column);
            builder.withImport(fieldNameAndImport.importString());

            builder.withCodeLine(OutputUtilities.kotlinIndent(1) + "set(" + fieldNameAndImport.fieldName() //$NON-NLS-1$
                    + ") equalToWhenPresent row::" + column.getJavaProperty()); //$NON-NLS-1$
        }

        return builder.build();
    }

    public FieldNameAndImport calculateFieldNameAndImport(String tableFieldName, String supportObjectImport,
                                                          IntrospectedColumn column) {
        String fieldName = column.getJavaProperty();
        if (useSnakeCase) {
            fieldName = StringUtility.convertCamelCaseToSnakeCase(fieldName);
        }
        String importString;
        if (fieldName.equals(tableFieldName)) {
            // name collision, no shortcut generated
            fieldName = tableFieldName + "." + fieldName; //$NON-NLS-1$
            importString = supportObjectImport + "." + tableFieldName; //$NON-NLS-1$
        } else {
            importString = supportObjectImport + "." + fieldName; //$NON-NLS-1$
        }
        return new FieldNameAndImport(fieldName, importString);
    }

    public record FieldNameAndImport(String fieldName, String importString) { }

    public static class Builder {
        private @Nullable IntrospectedTable introspectedTable;
        private @Nullable String resultMapId;
        private @Nullable String supportObjectImport;
        private @Nullable String tableFieldName;

        public Builder withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return this;
        }

        public Builder withResultMapId(String resultMapId) {
            this.resultMapId = resultMapId;
            return this;
        }

        public Builder withDynamicSqlSupportClassGenerator(KotlinDynamicSqlSupportClassGenerator generator) {
            supportObjectImport = generator.getSupportObjectImport();
            return this;
        }

        public Builder withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }

        public KotlinFragmentGenerator build() {
            return new KotlinFragmentGenerator(this);
        }
    }
}
