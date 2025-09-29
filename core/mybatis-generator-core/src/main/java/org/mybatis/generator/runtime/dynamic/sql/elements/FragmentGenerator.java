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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

public class FragmentGenerator {

    private final IntrospectedTable introspectedTable;
    private final String resultMapId;
    private final String tableFieldName;

    private FragmentGenerator(Builder builder) {
        this.introspectedTable = builder.introspectedTable;
        this.resultMapId = builder.resultMapId;
        tableFieldName = builder.tableFieldName;
    }

    public String getSelectList() {
        return introspectedTable.getAllColumns().stream()
                .map(c -> AbstractMethodGenerator.calculateFieldName(tableFieldName, c))
                .collect(Collectors.joining(", ")); //$NON-NLS-1$
    }

    public MethodParts getPrimaryKeyWhereClauseAndParameters() {
        MethodParts.Builder builder = new MethodParts.Builder();

        boolean first = true;
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            String fieldName = AbstractMethodGenerator.calculateFieldName(tableFieldName, column);
            builder.withImport(column.getFullyQualifiedJavaType());
            builder.withParameter(new Parameter(
                    column.getFullyQualifiedJavaType(), column.getJavaProperty() + "_")); //$NON-NLS-1$
            if (first) {
                builder.withBodyLine("    c.where(" + fieldName //$NON-NLS-1$
                        + ", isEqualTo(" + column.getJavaProperty() //$NON-NLS-1$
                        + "_))"); //$NON-NLS-1$
                first = false;
            } else {
                builder.withBodyLine("    .and(" + fieldName //$NON-NLS-1$
                        + ", isEqualTo(" + column.getJavaProperty() //$NON-NLS-1$
                        + "_))"); //$NON-NLS-1$
            }
        }
        builder.withBodyLine(");"); //$NON-NLS-1$

        return builder.build();
    }

    public List<String> getPrimaryKeyWhereClauseForUpdate(String prefix) {
        List<String> lines = new ArrayList<>();

        boolean first = true;
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            String fieldName = AbstractMethodGenerator.calculateFieldName(tableFieldName, column);
            String methodName = JavaBeansUtil.getGetterMethodName(
                    column.getJavaProperty(), column.getFullyQualifiedJavaType());
            if (first) {
                lines.add(prefix + ".where(" + fieldName //$NON-NLS-1$
                        + ", isEqualTo(row::" + methodName //$NON-NLS-1$
                        + "))"); //$NON-NLS-1$
                first = false;
            } else {
                lines.add(prefix + ".and(" + fieldName //$NON-NLS-1$
                        + ", isEqualTo(row::" + methodName //$NON-NLS-1$
                        + "))"); //$NON-NLS-1$
            }
        }

        return lines;
    }

    public MethodParts getAnnotatedConstructorArgs() {
        MethodParts.Builder builder = new MethodParts.Builder();

        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType")); //$NON-NLS-1$
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs")); //$NON-NLS-1$
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg")); //$NON-NLS-1$

        builder.withAnnotation("@ConstructorArgs({"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();

        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getArgAnnotation(imports, introspectedColumn, true));

            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }

            builder.withAnnotation(sb.toString());
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getArgAnnotation(imports, introspectedColumn, false));

            if (iterNonPk.hasNext()) {
                sb.append(',');
            }

            builder.withAnnotation(sb.toString());
        }

        builder.withAnnotation("})") //$NON-NLS-1$
                .withImports(imports);

        return builder.build();
    }

    public MethodParts getAnnotatedResults() {
        MethodParts.Builder builder = new MethodParts.Builder();

        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType")); //$NON-NLS-1$
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result")); //$NON-NLS-1$
        builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results")); //$NON-NLS-1$

        builder.withAnnotation("@Results(id=\"" + resultMapId + "\", value = {"); //$NON-NLS-1$ //$NON-NLS-2$

        StringBuilder sb = new StringBuilder();

        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(imports, introspectedColumn, true));

            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }

            builder.withAnnotation(sb.toString());
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(imports, introspectedColumn, false));

            if (iterNonPk.hasNext()) {
                sb.append(',');
            }

            builder.withAnnotation(sb.toString());
        }

        builder.withAnnotation("})") //$NON-NLS-1$
                .withImports(imports);

        return builder.build();
    }

    private String getArgAnnotation(Set<FullyQualifiedJavaType> imports, IntrospectedColumn introspectedColumn,
                                    boolean idColumn) {
        imports.add(introspectedColumn.getFullyQualifiedJavaType());

        return "@Arg(column=\"" //$NON-NLS-1$
                + introspectedColumn.getActualColumnName()
                + "\", javaType=" //$NON-NLS-1$
                + introspectedColumn.getFullyQualifiedJavaType().getShortName()
                + ".class" //$NON-NLS-1$
                + generateAdditionalItems(imports, introspectedColumn, idColumn)
                + ')';//$NON-NLS-1$
    }

    private String getResultAnnotation(Set<FullyQualifiedJavaType> imports, IntrospectedColumn introspectedColumn,
            boolean idColumn) {
        return "@Result(column=\"" //$NON-NLS-1$
                + introspectedColumn.getActualColumnName()
                + "\", property=\"" //$NON-NLS-1$
                + introspectedColumn.getJavaProperty()
                + '\"'
                + generateAdditionalItems(imports, introspectedColumn, idColumn)
                + ')'; //$NON-NLS-1$
    }

    private String generateAdditionalItems(Set<FullyQualifiedJavaType> imports, IntrospectedColumn introspectedColumn,
                                           boolean idColumn) {
        StringBuilder sb = new StringBuilder();

        if (stringHasValue(introspectedColumn.getTypeHandler())) {
            FullyQualifiedJavaType fqjt =
                    new FullyQualifiedJavaType(introspectedColumn.getTypeHandler());
            imports.add(fqjt);
            sb.append(", typeHandler="); //$NON-NLS-1$
            sb.append(fqjt.getShortName());
            sb.append(".class"); //$NON-NLS-1$
        }

        sb.append(", jdbcType=JdbcType."); //$NON-NLS-1$
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    public MethodParts getGeneratedKeyAnnotation(GeneratedKey gk) {
        MethodParts.Builder builder = new MethodParts.Builder();

        StringBuilder sb = new StringBuilder();
        introspectedTable.getColumn(gk.getColumn()).ifPresent(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options")); //$NON-NLS-1$
                sb.append("@Options(useGeneratedKeys=true,keyProperty=\"row."); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\""); //$NON-NLS-1$
                sb.append(String.format(", keyColumn=\"%s\"", Optional.ofNullable(gk.getColumn()).map(StringUtility::escapeStringForJava).orElse(""))); //$NON-NLS-1$ $NON-NLS-2$
                sb.append(")"); //$NON-NLS-1$
            } else {
                builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectKey")); //$NON-NLS-1$
                FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                sb.append("@SelectKey(statement=\""); //$NON-NLS-1$
                sb.append(gk.getRuntimeSqlStatement());
                sb.append("\", keyProperty=\"row."); //$NON-NLS-1$
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\", before="); //$NON-NLS-1$
                sb.append(gk.isIdentity() ? "false" : "true"); //$NON-NLS-1$ //$NON-NLS-2$
                sb.append(", resultType="); //$NON-NLS-1$
                sb.append(fqjt.getShortName());
                sb.append(".class)"); //$NON-NLS-1$
            }
            builder.withAnnotation(sb.toString());
        });

        return builder.build();
    }

    public List<String> getSetEqualLines(List<IntrospectedColumn> columnList, String firstLinePrefix,
            String subsequentLinePrefix, boolean terminate) {
        return getSetLines(columnList, firstLinePrefix, subsequentLinePrefix, terminate,
                "equalTo"); //$NON-NLS-1$
    }

    public List<String> getSetEqualWhenPresentLines(List<IntrospectedColumn> columnList, String firstLinePrefix,
            String subsequentLinePrefix, boolean terminate) {
        return getSetLines(columnList, firstLinePrefix, subsequentLinePrefix, terminate,
                "equalToWhenPresent"); //$NON-NLS-1$
    }

    private List<String> getSetLines(List<IntrospectedColumn> columnList, String firstLinePrefix,
                                 String subsequentLinePrefix, boolean terminate, String fragment) {
        List<String> lines = new ArrayList<>();
        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(columnList);
        Iterator<IntrospectedColumn> iter = columns.iterator();
        boolean first = true;
        while (iter.hasNext()) {
            IntrospectedColumn column = iter.next();
            String fieldName = AbstractMethodGenerator.calculateFieldName(tableFieldName, column);
            String methodName = JavaBeansUtil.getGetterMethodName(column.getJavaProperty(),
                    column.getFullyQualifiedJavaType());

            String start;
            if (first) {
                start = firstLinePrefix;
                first = false;
            } else {
                start = subsequentLinePrefix;
            }

            String line = start
                    + ".set(" //$NON-NLS-1$
                    + fieldName
                    + ")." //$NON-NLS-1$
                    + fragment
                    + "(row::" //$NON-NLS-1$
                    + methodName
                    + ")"; //$NON-NLS-1$

            if (terminate && !iter.hasNext()) {
                line += ";"; //$NON-NLS-1$
            }

            lines.add(line);
        }

        return lines;
    }

    public static class Builder {
        private IntrospectedTable introspectedTable;
        private String resultMapId;
        private String tableFieldName;

        public Builder withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return this;
        }

        public Builder withResultMapId(String resultMapId) {
            this.resultMapId = resultMapId;
            return this;
        }

        public Builder withTableFieldName(String tableFieldName) {
            this.tableFieldName = tableFieldName;
            return this;
        }

        public FragmentGenerator build() {
            return new FragmentGenerator(this);
        }
    }
}
