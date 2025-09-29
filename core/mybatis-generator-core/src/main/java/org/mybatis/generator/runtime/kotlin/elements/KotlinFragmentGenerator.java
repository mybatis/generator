/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.runtime.kotlin.elements;

import static org.mybatis.generator.api.dom.OutputUtilities.kotlinIndent;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForKotlin;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.JavaToKotlinTypeConverter;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.runtime.kotlin.KotlinDynamicSqlSupportClassGenerator;

public class KotlinFragmentGenerator {

    private final IntrospectedTable introspectedTable;
    private final String resultMapId;
    private final String supportObjectImport;
    private final String tableFieldName;

    private KotlinFragmentGenerator(Builder builder) {
        introspectedTable = builder.introspectedTable;
        resultMapId = builder.resultMapId;
        supportObjectImport = builder.supportObjectImport;
        tableFieldName = builder.tableFieldName;
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
                argName = column.getJavaProperty() + "_"; //$NON-NLS-1$
                FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(column.getFullyQualifiedJavaType());
                builder.withImports(kt.getImportList());
                builder.withArgument(KotlinArg.newArg(argName)
                        .withDataType(kt.getShortNameWithTypeArguments())
                        .build());
            }

            AbstractKotlinFunctionGenerator.FieldNameAndImport fieldNameAndImport =
                    AbstractKotlinFunctionGenerator.calculateFieldNameAndImport(tableFieldName,
                    supportObjectImport, column);

            builder.withImport(fieldNameAndImport.importString());
            if (columnCount == 1) {
                builder.withCodeLine(singleColumnWhere(fieldNameAndImport.fieldName(), argName));
                first = false;
            } else if (first) {
                builder.withCodeLine("    where {"); //$NON-NLS-1$
                builder.withCodeLine(multiColumnWhere(fieldNameAndImport.fieldName(), argName));
                first = false;
            } else {
                builder.withCodeLine(multiColumnAnd(fieldNameAndImport.fieldName(), argName));
            }
        }
        if (columnCount > 1) {
            builder.withCodeLine("    }"); //$NON-NLS-1$
        }

        return builder.build();
    }

    private String singleColumnWhere(String columName, String property) {
        return "    where { " + composeIsEqualTo(columName, property)  + " }"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private String multiColumnWhere(String columName, String property) {
        return "        " + composeIsEqualTo(columName, property); //$NON-NLS-1$
    }

    private String multiColumnAnd(String columName, String property) {
        return "        and { " + composeIsEqualTo(columName, property)  + " }"; //$NON-NLS-1$ //$NON-NLS-2$
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

        if (stringHasValue(introspectedColumn.getTypeHandler())) {
            FullyQualifiedKotlinType fqjt =
                    new FullyQualifiedKotlinType(introspectedColumn.getTypeHandler());
            imports.add(introspectedColumn.getTypeHandler());
            sb.append(", typeHandler="); //$NON-NLS-1$
            sb.append(fqjt.getShortNameWithoutTypeArguments());
            sb.append("::class"); //$NON-NLS-1$
        }

        sb.append(", jdbcType=JdbcType."); //$NON-NLS-1$
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true"); //$NON-NLS-1$
        }
        sb.append(')');

        return sb.toString();
    }

    public KotlinFunctionParts getGeneratedKeyAnnotation(GeneratedKey gk) {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        StringBuilder sb = new StringBuilder();
        introspectedTable.getColumn(gk.getColumn()).ifPresent(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                builder.withImport("org.apache.ibatis.annotations.Options"); //$NON-NLS-1$
                sb.append("@Options(useGeneratedKeys=true,keyProperty=\"row."); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\""); //$NON-NLS-1$
                sb.append(String.format(", keyColumn=\"%s\"", Optional.ofNullable(gk.getColumn()).map(StringUtility::escapeStringForKotlin).orElse(""))); //$NON-NLS-1$ $NON-NLS-2$
                sb.append(")"); //$NON-NLS-1$
                builder.withAnnotation(sb.toString());
            } else {
                builder.withImport("org.apache.ibatis.annotations.SelectKey"); //$NON-NLS-1$
                FullyQualifiedKotlinType kt =
                        JavaToKotlinTypeConverter.convert(introspectedColumn.getFullyQualifiedJavaType());

                sb.append("@SelectKey(statement=[\""); //$NON-NLS-1$
                sb.append(gk.getRuntimeSqlStatement());
                sb.append("\"], keyProperty=\"row."); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\", before="); //$NON-NLS-1$
                sb.append(gk.isIdentity() ? "false" : "true"); //$NON-NLS-1$ //$NON-NLS-2$
                sb.append(", resultType="); //$NON-NLS-1$
                sb.append(kt.getShortNameWithoutTypeArguments());
                sb.append("::class)"); //$NON-NLS-1$
                builder.withAnnotation(sb.toString());
            }
        });

        return builder.build();
    }

    public KotlinFunctionParts getSetEqualLines(List<IntrospectedColumn> columnList) {

        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(columnList);
        for (IntrospectedColumn column : columns) {
            AbstractKotlinFunctionGenerator.FieldNameAndImport fieldNameAndImport =
                    AbstractKotlinFunctionGenerator.calculateFieldNameAndImport(tableFieldName,
                            supportObjectImport, column);
            builder.withImport(fieldNameAndImport.importString());

            builder.withCodeLine("    set(" + fieldNameAndImport.fieldName() //$NON-NLS-1$
                    + ") equalToOrNull row::" + column.getJavaProperty()); //$NON-NLS-1$
        }

        return builder.build();
    }

    public KotlinFunctionParts getSetEqualWhenPresentLines(List<IntrospectedColumn> columnList) {

        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(columnList);
        for (IntrospectedColumn column : columns) {
            AbstractKotlinFunctionGenerator.FieldNameAndImport fieldNameAndImport =
                    AbstractKotlinFunctionGenerator.calculateFieldNameAndImport(tableFieldName,
                            supportObjectImport, column);
            builder.withImport(fieldNameAndImport.importString());

            builder.withCodeLine("    set(" + fieldNameAndImport.fieldName() //$NON-NLS-1$
                    + ") equalToWhenPresent row::" + column.getJavaProperty()); //$NON-NLS-1$
        }

        return builder.build();
    }

    public static class Builder {
        private IntrospectedTable introspectedTable;
        private String resultMapId;
        private String supportObjectImport;
        private String tableFieldName;

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
