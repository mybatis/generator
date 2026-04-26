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

import static org.mybatis.generator.internal.util.StringUtility.escapeStringForKotlin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.Indenter;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.JavaToKotlinTypeConverter;
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.runtime.KotlinFunctionParts;
import org.mybatis.generator.runtime.dynamicsql.kotlin.KotlinDynamicSqlSupportClassGenerator;
import org.mybatis.generator.runtime.mybatis3.ListUtilities;

public class KotlinFragmentGenerator {

    private final IntrospectedTable introspectedTable;
    private final String resultMapId;
    private final String supportObjectImport;
    private final String tableFieldName;
    private final boolean useSnakeCase;
    private final Indenter indenter;

    private KotlinFragmentGenerator(Builder builder) {
        introspectedTable = Objects.requireNonNull(builder.introspectedTable);
        resultMapId = Objects.requireNonNull(builder.resultMapId);
        supportObjectImport = Objects.requireNonNull(builder.supportObjectImport);
        tableFieldName = Objects.requireNonNull(builder.tableFieldName);
        useSnakeCase = introspectedTable
                .findTableOrClientGeneratorPropertyAsBoolean(PropertyRegistry.ANY_USE_SNAKE_CASE_IDENTIFIERS);
        indenter = Objects.requireNonNull(builder.indenter);
    }

    public KotlinFunctionParts getPrimaryKeyWhereClauseAndParameters(boolean forUpdate) {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        int columnCount = introspectedTable.getPrimaryKeyColumns().size();
        boolean first = true;
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            String argName;
            if (forUpdate) {
                if (introspectedTable.generateKotlinV1Model() || column.isNullable() || column.isIdentity()) {
                    argName = "row." + column.getJavaProperty() + "!!"; //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    argName = "row." + column.getJavaProperty(); //$NON-NLS-1$
                }
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
                builder.withCodeLine(indenter.kotlinIndent(1) + "where {"); //$NON-NLS-1$
                builder.withCodeLine(multiColumnWhere(fieldNameAndImport.fieldName(), argName));
                first = false;
            } else {
                builder.withCodeLine(multiColumnAnd(fieldNameAndImport.fieldName(), argName));
            }
        }
        if (columnCount > 1) {
            builder.withCodeLine(indenter.kotlinIndent(1) + "}"); //$NON-NLS-1$
        }

        builder.withCodeLine("}"); //$NON-NLS-1$

        return builder.build();
    }

    private String singleColumnWhere(String columName, String property) {
        return indenter.kotlinIndent(1) + "where { " //$NON-NLS-1$
                + composeIsEqualTo(columName, property)  + " }"; //$NON-NLS-1$
    }

    private String multiColumnWhere(String columName, String property) {
        return indenter.kotlinIndent(2) + composeIsEqualTo(columName, property); //$NON-NLS-1$
    }

    private String multiColumnAnd(String columName, String property) {
        return indenter.kotlinIndent(2) + "and { " //$NON-NLS-1$
                + composeIsEqualTo(columName, property)  + " }"; //$NON-NLS-1$
    }

    private String composeIsEqualTo(String columName, String property) {
        return columName + " isEqualTo " + property; //$NON-NLS-1$
    }

    public KotlinFunctionParts getAnnotatedArgs() {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        builder.withImport("org.apache.ibatis.type.JdbcType"); //$NON-NLS-1$
        builder.withImport("org.apache.ibatis.annotations.Arg"); //$NON-NLS-1$
        builder.withImport("org.apache.ibatis.annotations.Results"); //$NON-NLS-1$

        builder.withAnnotation("@Results(id=\"" + resultMapId + "\")"); //$NON-NLS-1$ //$NON-NLS-2$

        Set<String> imports = new HashSet<>();
        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            builder.withAnnotation(getArgAnnotation(imports, introspectedColumn, true));
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            builder.withAnnotation(getArgAnnotation(imports, introspectedColumn, false));
        }

        return builder.withImports(imports).build();
    }

    private String getArgAnnotation(Set<String> imports, IntrospectedColumn introspectedColumn,
                                    boolean idColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append("@Arg(column=\""); //$NON-NLS-1$
        sb.append(escapeStringForKotlin(introspectedColumn.getActualColumnName()));
        sb.append("\""); //$NON-NLS-1$

        introspectedColumn.getTypeHandler().ifPresent(th -> {
            imports.add(th);
            sb.append(", typeHandler="); //$NON-NLS-1$
            sb.append(new FullyQualifiedKotlinType(th).getShortNameWithoutTypeArguments());
            sb.append("::class"); //$NON-NLS-1$
        });

        sb.append(", jdbcType=JdbcType."); //$NON-NLS-1$
        sb.append(introspectedColumn.getJdbcTypeName());

        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(introspectedColumn.getFullyQualifiedJavaType());
        imports.addAll(kt.getImportList());

        if (introspectedTable.generateKotlinV1Model() || introspectedColumn.isNullable()
                || introspectedColumn.isIdentity()) {
            sb.append(", javaType="); //$NON-NLS-1$
            sb.append(calculateNullableTypeForArgAnnotation(kt));
            sb.append("::class"); //$NON-NLS-1$
        } else {
            sb.append(", javaType="); //$NON-NLS-1$
            sb.append(kt.getShortNameWithoutTypeArguments());
            sb.append("::class"); //$NON-NLS-1$
        }

        if (idColumn) {
            sb.append(", id=true"); //$NON-NLS-1$
        }
        sb.append(')');

        return sb.toString();
    }

    /**
     * Calculates the javaType for a constructor arg annotation. Kotlin nullable primitive types
     * map to the Java wrapper types, but all except <code>Int</code> have the same name as their
     * Java counterparts. MyBatis will get confused if we use the Kotlin type because, for example
     * <code>kotlin.Long</code> will map to Java's primitive <code>long</code>. In a constructor
     * mapping, the types must match exactly. So, for example, a <code>kotlin.Long?</code> needs
     * to map to a <code>java.lang.Long</code>.
     *
     * @param kt the Kotlin type
     * @return the type string for a constructor arg annotation
     */
    private String calculateNullableTypeForArgAnnotation(FullyQualifiedKotlinType kt) {
        return switch (kt.getShortNameWithoutTypeArguments()) {
        case "Int" -> "Integer"; //$NON-NLS-1$ //$NON-NLS-2$
        case "Long" -> "java.lang.Long"; //$NON-NLS-1$ //$NON-NLS-2$
        case "Short" -> "java.lang.Short"; //$NON-NLS-1$ //$NON-NLS-2$
        case "Byte" -> "java.lang.Byte"; //$NON-NLS-1$ //$NON-NLS-2$
        case "Float" -> "java.lang.Float"; //$NON-NLS-1$ //$NON-NLS-2$
        case "Double" -> "java.lang.Double"; //$NON-NLS-1$ //$NON-NLS-2$
        case "Boolean" -> "java.lang.Boolean"; //$NON-NLS-1$ //$NON-NLS-2$
        default -> kt.getShortNameWithoutTypeArguments();
        };
    }

    public KotlinFunctionParts getSetEqualLinesForUpdateStatement(List<IntrospectedColumn> columnList,
                                                                  boolean terminate) {

        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        List<IntrospectedColumn> columns = ListUtilities.filterColumnsForUpdate(columnList);
        for (IntrospectedColumn column : columns) {
            FieldNameAndImport fieldNameAndImport = calculateFieldNameAndImport(tableFieldName, supportObjectImport,
                    column);
            builder.withImport(fieldNameAndImport.importString());

            if (column.isIdentity()) {
                // identity columns are always generated as nullable
                builder.withCodeLine(indenter.kotlinIndent(1) + "set(" //$NON-NLS-1$
                        + fieldNameAndImport.fieldName()
                        + ") equalTo row." + column.getJavaProperty() + "!!"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (introspectedTable.generateKotlinV1Model() || column.isNullable()) {
                builder.withCodeLine(indenter.kotlinIndent(1) + "set(" //$NON-NLS-1$
                        + fieldNameAndImport.fieldName()
                        + ") equalToOrNull row::" + column.getJavaProperty()); //$NON-NLS-1$
            } else {
                builder.withCodeLine(indenter.kotlinIndent(1) + "set(" //$NON-NLS-1$
                        + fieldNameAndImport.fieldName()
                        + ") equalTo row::" + column.getJavaProperty()); //$NON-NLS-1$

            }
        }

        if (terminate) {
            builder.withCodeLine("}"); //$NON-NLS-1$
        }

        return builder.build();
    }

    public KotlinFunctionParts getSetEqualWhenPresentLinesForUpdateStatement(List<IntrospectedColumn> columnList,
                                                                             boolean terminate) {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        ListUtilities.filterColumnsForUpdate(columnList).stream()
                .filter(ic -> introspectedTable.generateKotlinV1Model() || ic.isNullable())
                .forEach(column -> {
                    FieldNameAndImport fieldNameAndImport = calculateFieldNameAndImport(tableFieldName,
                            supportObjectImport,
                            column);
                    builder.withImport(fieldNameAndImport.importString());

                    builder.withCodeLine(indenter.kotlinIndent(1) + "set(" //$NON-NLS-1$
                            + fieldNameAndImport.fieldName()
                            + ") equalToWhenPresent row::" + column.getJavaProperty()); //$NON-NLS-1$
                });

        if (terminate) {
            builder.withCodeLine("}"); //$NON-NLS-1$
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

    public KotlinFunctionParts generateInsertMultipleBody(String functionShortName) {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        builder.withCodeLine(functionShortName + "(this::insertMultiple" //$NON-NLS-1$
                + ", records, " + tableFieldName //$NON-NLS-1$
                + ") {"); //$NON-NLS-1$

        return completeInsertBody(builder);
    }

    public KotlinFunctionParts generateInsertBody() {
        KotlinFunctionParts.Builder builder = new KotlinFunctionParts.Builder();

        builder.withCodeLine("insert(this::insert, row, " + tableFieldName //$NON-NLS-1$
                + ") {"); //$NON-NLS-1$

        return completeInsertBody(builder);
    }

    private KotlinFunctionParts completeInsertBody(KotlinFunctionParts.Builder builder) {
        List<IntrospectedColumn> columns =
                ListUtilities.filterColumnsForInsert(introspectedTable.getAllColumns());
        for (IntrospectedColumn column : columns) {
            FieldNameAndImport fieldNameAndImport =
                    calculateFieldNameAndImport(tableFieldName, supportObjectImport, column);
            builder.withImport(fieldNameAndImport.importString());

            builder.withCodeLine(indenter.kotlinIndent(1) + "withMappedColumn("  //$NON-NLS-1$
                    + fieldNameAndImport.fieldName()
                    + ")"); //$NON-NLS-1$
        }

        builder.withCodeLine("}"); //$NON-NLS-1$
        return builder.build();
    }

    public static class Builder {
        private @Nullable IntrospectedTable introspectedTable;
        private @Nullable String resultMapId;
        private @Nullable String supportObjectImport;
        private @Nullable String tableFieldName;
        private @Nullable Indenter indenter;

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

        public Builder withIndenter(Indenter indenter) {
            this.indenter = indenter;
            return this;
        }

        public KotlinFragmentGenerator build() {
            return new KotlinFragmentGenerator(this);
        }
    }
}
