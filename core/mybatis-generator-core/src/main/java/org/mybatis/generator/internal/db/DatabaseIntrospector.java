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
package org.mybatis.generator.internal.db;

import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringContainsSQLWildcard;
import static org.mybatis.generator.internal.util.StringUtility.stringContainsSpace;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.KnownRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaReservedWords;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.PluginAggregator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

public class DatabaseIntrospector {
    private final DatabaseMetaData databaseMetaData;
    private final JavaTypeResolver javaTypeResolver;
    private final List<String> warnings;
    private final Context context;
    private final Log logger;

    public DatabaseIntrospector(Context context, DatabaseMetaData databaseMetaData,
                                JavaTypeResolver javaTypeResolver, List<String> warnings) {
        this.context = context;
        this.databaseMetaData = databaseMetaData;
        this.javaTypeResolver = javaTypeResolver;
        this.warnings = warnings;
        logger = LogFactory.getLog(getClass());
    }

    private void calculatePrimaryKey(FullyQualifiedTable table, IntrospectedTable introspectedTable) {
        ResultSet rs;

        try {
            rs = databaseMetaData.getPrimaryKeys(
                    table.getIntrospectedCatalog().orElse(null),
                    table.getIntrospectedSchema().orElse(null),
                    table.getIntrospectedTableName());
        } catch (SQLException e) {
            warnings.add(getString("Warning.15")); //$NON-NLS-1$
            return;
        }

        try {
            // keep primary columns in key sequence order
            Map<Short, String> keyColumns = new TreeMap<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
                short keySeq = rs.getShort("KEY_SEQ"); //$NON-NLS-1$
                keyColumns.put(keySeq, columnName);
            }

            for (String columnName : keyColumns.values()) {
                introspectedTable.addPrimaryKeyColumn(columnName);
            }
        } catch (SQLException e) {
            // ignore the primary key if there's any error
        } finally {
            closeResultSet(rs);
        }
    }

    private void closeResultSet(@Nullable ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    private void reportIntrospectionWarnings(IntrospectedTable introspectedTable,
                                             TableConfiguration tableConfiguration, FullyQualifiedTable table) {
        // make sure that every column listed in column overrides
        // actually exists in the table
        for (ColumnOverride columnOverride : tableConfiguration.getColumnOverrides()) {
            if (introspectedTable.getColumn(columnOverride.getColumnName()).isEmpty()) {
                warnings.add(getString("Warning.3", columnOverride.getColumnName(), table.toString()));//$NON-NLS-1$
            }
        }

        // make sure that every column listed in ignored columns
        // actually exists in the table
        for (String string : tableConfiguration.getIgnoredColumnsInError()) {
            warnings.add(getString("Warning.4", string, table.toString())); //$NON-NLS-1$
        }

        tableConfiguration.getGeneratedKey().ifPresent(generatedKey -> {
            if (introspectedTable.getColumn(generatedKey.getColumn()).isEmpty()) {
                if (generatedKey.isIdentity()) {
                    warnings.add(getString("Warning.5", generatedKey.getColumn(), table.toString())); //$NON-NLS-1$
                } else {
                    warnings.add(getString("Warning.6", generatedKey.getColumn(), table.toString())); //$NON-NLS-1$
                }
            }
        });

        for (IntrospectedColumn ic : introspectedTable.getAllColumns()) {
            if (JavaReservedWords.containsWord(ic.getJavaProperty())) {
                warnings.add(getString("Warning.26", ic.getActualColumnName(), table.toString())); //$NON-NLS-1$
            }
        }
    }

    /**
     * Returns a List of IntrospectedTable elements that matches the specified table configuration.
     *
     * @param tc
     *            the table configuration
     * @return a list of introspected tables
     * @throws SQLException
     *             if any errors in introspection
     */
    public List<IntrospectedTable> introspectTables(TableConfiguration tc, KnownRuntime knownRuntime,
                                                    PluginAggregator pluginAggregator) throws SQLException {
        // get the raw columns from the DB
        Map<ActualTableName, List<IntrospectedColumn>> columns = getColumns(tc);

        if (columns.isEmpty()) {
            String formattedCatalog = tc.getCatalog() == null ? "<null>" : "'" + tc.getCatalog() + "'";
            String formattedSchema = tc.getSchema() == null ? "<null>" : "'" + tc.getSchema() + "'";
            String formattedTableName = "'" + tc.getTableName() + "'";
            warnings.add(getString("Warning.19", formattedCatalog, formattedSchema, formattedTableName)); //$NON-NLS-1$
            return Collections.emptyList();
        }

        removeIgnoredColumns(tc, columns);
        calculateExtraColumnInformation(tc, columns);
        applyColumnOverrides(tc, columns);
        calculateIdentityColumns(tc, columns);

        List<IntrospectedTable> introspectedTables =
                calculateIntrospectedTables(tc, columns, knownRuntime, pluginAggregator);

        // now introspectedTables has all the columns from all the
        // tables in the configuration. Do some validation...

        Iterator<IntrospectedTable> iter = introspectedTables.iterator();
        while (iter.hasNext()) {
            IntrospectedTable introspectedTable = iter.next();

            if (!introspectedTable.hasAnyColumns()) {
                // add a warning that the table has no columns, remove from the
                // list
                String warning =
                        getString("Warning.1", introspectedTable.getFullyQualifiedTable().toString()); //$NON-NLS-1$
                warnings.add(warning);
                iter.remove();
            } else if (!introspectedTable.hasPrimaryKeyColumns() && !introspectedTable.hasBaseColumns()) {
                // add a warning that the table has only BLOB columns, remove from
                // the list
                String warning =
                        getString("Warning.18", introspectedTable.getFullyQualifiedTable().toString()); //$NON-NLS-1$
                warnings.add(warning);
                iter.remove();
            } else {
                // now make sure that all columns called out in the
                // configuration
                // actually exist
                reportIntrospectionWarnings(introspectedTable, tc, introspectedTable.getFullyQualifiedTable());
            }
        }

        return introspectedTables;
    }

    private void removeIgnoredColumns(TableConfiguration tc, Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            Iterator<IntrospectedColumn> tableColumns = entry.getValue().iterator();
            while (tableColumns.hasNext()) {
                IntrospectedColumn introspectedColumn = tableColumns.next();
                if (tc.isColumnIgnored(introspectedColumn.getActualColumnName())) {
                    tableColumns.remove();
                    if (logger.isDebugEnabled()) {
                        logger.debug(getString("Tracing.3", //$NON-NLS-1$
                                introspectedColumn.getActualColumnName(), entry.getKey().toString()));
                    }
                }
            }
        }
    }

    private void calculateExtraColumnInformation(TableConfiguration tc, Map<ActualTableName,
            List<IntrospectedColumn>> columns) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                String calculatedColumnName = tc.getColumnRenamingRule().map(rr -> {
                    Matcher matcher = rr.pattern().matcher(introspectedColumn.getActualColumnName());
                    return matcher.replaceAll(rr.replaceString());
                }).orElseGet(introspectedColumn::getActualColumnName);

                if (isTrue(tc.getProperty(PropertyRegistry.TABLE_USE_ACTUAL_COLUMN_NAMES))) {
                    introspectedColumn.setJavaProperty(JavaBeansUtil.getValidPropertyName(calculatedColumnName));
                } else if (isTrue(tc.getProperty(PropertyRegistry.TABLE_USE_COMPOUND_PROPERTY_NAMES))) {
                    sb.setLength(0);
                    sb.append(calculatedColumnName);
                    introspectedColumn.getRemarks().ifPresent(r -> {
                        sb.append('_');
                        sb.append(JavaBeansUtil.getCamelCaseString(r, true));
                    });
                    introspectedColumn.setJavaProperty(JavaBeansUtil.getValidPropertyName(sb.toString()));
                } else {
                    introspectedColumn.setJavaProperty(
                            JavaBeansUtil.getCamelCaseString(calculatedColumnName, false));
                }

                FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver.calculateJavaType(introspectedColumn);

                if (fullyQualifiedJavaType != null) {
                    introspectedColumn.setFullyQualifiedJavaType(fullyQualifiedJavaType);
                    introspectedColumn.setJdbcTypeName(javaTypeResolver.calculateJdbcTypeName(introspectedColumn));
                } else {
                    // type cannot be resolved. Check for ignored or overridden
                    boolean ok = tc.isColumnIgnored(introspectedColumn.getActualColumnName());

                    // if overridden, and a java type is configured, then we can ignore
                    if (!ok) {
                        ok = tc.getColumnOverride(introspectedColumn.getActualColumnName())
                                .map(ColumnOverride::getJavaType)
                                .map(Optional::isPresent)
                                .orElse(false);
                    }

                    // if the type is not supported, then we'll report a warning
                    if (!ok) {
                        introspectedColumn.setFullyQualifiedJavaType(FullyQualifiedJavaType.getObjectInstance());
                        introspectedColumn.setJdbcTypeName("OTHER"); //$NON-NLS-1$

                        String warning = getString("Warning.14", //$NON-NLS-1$
                                Integer.toString(introspectedColumn.getJdbcType()),
                                entry.getKey().toString(),
                                introspectedColumn.getActualColumnName());

                        warnings.add(warning);
                    }
                }

                if (context.autoDelimitKeywords()
                        && SqlReservedWords.containsWord(introspectedColumn.getActualColumnName())) {
                    introspectedColumn.setColumnNameDelimited(true);
                }

                if (tc.isAllColumnDelimitingEnabled()) {
                    introspectedColumn.setColumnNameDelimited(true);
                }
            }
        }
    }

    private void calculateIdentityColumns(TableConfiguration tc,
                                          Map<ActualTableName, List<IntrospectedColumn>> columns) {
        tc.getGeneratedKey().ifPresent(gk -> {
            columns.values().stream()
                    .flatMap(List::stream)
                    .filter(introspectedColumn -> isMatchedColumn(introspectedColumn, gk))
                    .forEach(introspectedColumn -> {
                        if (gk.isIdentity() || gk.isJdbcStandard()) {
                            introspectedColumn.setIdentity(true);
                            introspectedColumn.setSequenceColumn(false);
                        } else {
                            introspectedColumn.setIdentity(false);
                            introspectedColumn.setSequenceColumn(true);
                        }
                    });
        });
    }

    private boolean isMatchedColumn(IntrospectedColumn introspectedColumn, GeneratedKey gk) {
        if (introspectedColumn.isColumnNameDelimited()) {
            return introspectedColumn.getActualColumnName().equals(gk.getColumn());
        } else {
            return introspectedColumn.getActualColumnName().equalsIgnoreCase(gk.getColumn());
        }
    }

    private void applyColumnOverrides(TableConfiguration tc, Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                tc.getColumnOverride(introspectedColumn.getActualColumnName()).ifPresent(columnOverride -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug(getString("Tracing.4", //$NON-NLS-1$
                                introspectedColumn.getActualColumnName(), entry.getKey().toString()));
                    }

                    columnOverride.getJavaProperty().ifPresent(introspectedColumn::setJavaProperty);
                    columnOverride.getJavaType().ifPresent(
                            jt -> introspectedColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType(jt)));
                    columnOverride.getJdbcType().ifPresent(introspectedColumn::setJdbcTypeName);
                    columnOverride.getTypeHandler().ifPresent(introspectedColumn::setTypeHandler);

                    if (columnOverride.isColumnNameDelimited()) {
                        introspectedColumn.setColumnNameDelimited(true);
                    }

                    introspectedColumn.setGeneratedAlways(columnOverride.isGeneratedAlways());

                    introspectedColumn.setProperties(columnOverride.getProperties());
                });
            }
        }
    }

    private Map<ActualTableName, List<IntrospectedColumn>> getColumns(TableConfiguration tc) throws SQLException {
        String localCatalog;
        String localSchema;
        String localTableName;

        boolean delimitIdentifiers = tc.isDelimitIdentifiers()
                || stringContainsSpace(tc.getCatalog())
                || stringContainsSpace(tc.getSchema())
                || stringContainsSpace(tc.getTableName());

        if (delimitIdentifiers) {
            localCatalog = tc.getCatalog();
            localSchema = tc.getSchema();
            localTableName = tc.getTableName();
        } else if (databaseMetaData.storesLowerCaseIdentifiers()) {
            localCatalog = tc.getCatalog() == null ? null : tc.getCatalog().toLowerCase();
            localSchema = tc.getSchema() == null ? null : tc.getSchema().toLowerCase();
            localTableName = tc.getTableName().toLowerCase();
        } else if (databaseMetaData.storesUpperCaseIdentifiers()) {
            localCatalog = tc.getCatalog() == null ? null : tc.getCatalog().toUpperCase();
            localSchema = tc.getSchema() == null ? null : tc.getSchema().toUpperCase();
            localTableName = tc.getTableName().toUpperCase();
        } else {
            localCatalog = tc.getCatalog();
            localSchema = tc.getSchema();
            localTableName = tc.getTableName();
        }

        if (tc.isWildcardEscapingEnabled()) {
            String escapeString = databaseMetaData.getSearchStringEscape();

            if (localSchema != null) {
                localSchema = escapeName(localSchema, escapeString);
            }

            localTableName = escapeName(localTableName, escapeString);
        }

        Map<ActualTableName, List<IntrospectedColumn>> answer = new HashMap<>();

        if (logger.isDebugEnabled()) {
            String fullTableName = composeFullyQualifiedTableName(localCatalog, localSchema,
                    localTableName, '.');
            logger.debug(getString("Tracing.1", fullTableName)); //$NON-NLS-1$
        }

        ResultSet rs = databaseMetaData.getColumns(localCatalog, localSchema, localTableName, "%"); //$NON-NLS-1$

        boolean supportsIsAutoIncrement = false;
        boolean supportsIsGeneratedColumn = false;
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            if ("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) { //$NON-NLS-1$
                supportsIsAutoIncrement = true;
            }
            if ("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) { //$NON-NLS-1$
                supportsIsGeneratedColumn = true;
            }
        }

        while (rs.next()) {
            IntrospectedColumn introspectedColumn = ObjectFactory.createIntrospectedColumn(context);

            introspectedColumn.setTableAlias(tc.getAlias());
            introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE")); //$NON-NLS-1$
            introspectedColumn.setActualTypeName(rs.getString("TYPE_NAME")); //$NON-NLS-1$
            introspectedColumn.setLength(rs.getInt("COLUMN_SIZE")); //$NON-NLS-1$
            introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$
            introspectedColumn
                    .setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable); //$NON-NLS-1$
            introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS")); //$NON-NLS-1$
            introspectedColumn.setRemarks(rs.getString("REMARKS")); //$NON-NLS-1$
            introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF")); //$NON-NLS-1$

            if (supportsIsAutoIncrement) {
                introspectedColumn.setAutoIncrement(
                        "YES".equals(rs.getString("IS_AUTOINCREMENT"))); //$NON-NLS-1$ //$NON-NLS-2$
            }

            if (supportsIsGeneratedColumn) {
                introspectedColumn.setGeneratedColumn(
                        "YES".equals(rs.getString("IS_GENERATEDCOLUMN"))); //$NON-NLS-1$ //$NON-NLS-2$
            }

            ActualTableName atn = new ActualTableName(
                    rs.getString("TABLE_CAT"), //$NON-NLS-1$
                    rs.getString("TABLE_SCHEM"), //$NON-NLS-1$
                    rs.getString("TABLE_NAME")); //$NON-NLS-1$

            List<IntrospectedColumn> columns = answer.computeIfAbsent(atn, k -> new ArrayList<>());

            columns.add(introspectedColumn);

            if (logger.isDebugEnabled()) {
                logger.debug(getString(
                        "Tracing.2", //$NON-NLS-1$
                        introspectedColumn.getActualColumnName(),
                        Integer.toString(introspectedColumn.getJdbcType()),
                        atn.toString()));
            }
        }

        closeResultSet(rs);

        if (answer.size() > 1
                && !stringContainsSQLWildcard(localSchema)
                && !stringContainsSQLWildcard(localTableName)) {
            // issue a warning if there is more than one table and
            // no wildcards were used
            ActualTableName inputAtn = new ActualTableName(tc.getCatalog(), tc.getSchema(), tc.getTableName());

            StringBuilder sb = new StringBuilder();
            boolean comma = false;
            for (ActualTableName atn : answer.keySet()) {
                if (comma) {
                    sb.append(',');
                } else {
                    comma = true;
                }
                sb.append(atn);
            }

            warnings.add(getString("Warning.25", inputAtn.toString(), sb.toString())); //$NON-NLS-1$
        }

        return answer;
    }

    private String escapeName(String localName, String escapeString) {
        StringTokenizer st = new StringTokenizer(localName, "_%", true); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("_") || token.equals("%")) { //$NON-NLS-1$ //$NON-NLS-2$
                sb.append(escapeString);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    private List<IntrospectedTable> calculateIntrospectedTables(TableConfiguration tc, Map<ActualTableName,
            List<IntrospectedColumn>> columns, KnownRuntime knownRuntime, PluginAggregator pluginAggregator) {
        boolean delimitIdentifiers = tc.isDelimitIdentifiers()
                || stringContainsSpace(tc.getCatalog())
                || stringContainsSpace(tc.getSchema())
                || stringContainsSpace(tc.getTableName());

        List<IntrospectedTable> answer = new ArrayList<>();

        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            ActualTableName atn = entry.getKey();

            // we only use the returned catalog and schema if something was
            // actually
            // specified on the table configuration. If something was returned
            // from the DB for these fields, but nothing was specified on the
            // table
            // configuration, then some sort of DB default is being returned,
            // and we don't want that in our SQL
            FullyQualifiedTable table = new FullyQualifiedTable.Builder()
                    .withIntrospectedCatalog(stringHasValue(tc.getCatalog()) ? atn.getCatalog() : null)
                    .withIntrospectedSchema(stringHasValue(tc.getSchema()) ? atn.getSchema() : null)
                    .withIntrospectedTableName(atn.getTableName())
                    .withDomainObjectName(tc.getDomainObjectName())
                    .withAlias(tc.getAlias())
                    .withIgnoreQualifiersAtRuntime(
                            isTrue(tc.getProperty(PropertyRegistry.TABLE_IGNORE_QUALIFIERS_AT_RUNTIME)))
                    .withRuntimeCatalog(tc.getProperty(PropertyRegistry.TABLE_RUNTIME_CATALOG))
                    .withRuntimeSchema(tc.getProperty(PropertyRegistry.TABLE_RUNTIME_SCHEMA))
                    .withRuntimeTableName(tc.getProperty(PropertyRegistry.TABLE_RUNTIME_TABLE_NAME))
                    .withDelimitIdentifiers(delimitIdentifiers)
                    .withDomainObjectRenamingRule(tc.getDomainObjectRenamingRule())
                    .withContext(context)
                    .build();

            IntrospectedTable introspectedTable = new IntrospectedTable.Builder()
                    .withTableConfiguration(tc)
                    .withFullyQualifiedTable(table)
                    .withContext(context)
                    .withKnownRuntime(knownRuntime)
                    .withPluginAggregator(pluginAggregator)
                    .build();

            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                introspectedTable.addColumn(introspectedColumn);
            }

            calculatePrimaryKey(table, introspectedTable);

            enhanceIntrospectedTable(introspectedTable);

            answer.add(introspectedTable);
        }

        return answer;
    }

    /**
     * Calls database metadata to retrieve extra information about the table
     * such as remarks associated with the table and the type.
     *
     * <p>If there is any error, we just add a warning and continue.
     *
     * @param introspectedTable the introspected table to enhance
     */
    private void enhanceIntrospectedTable(IntrospectedTable introspectedTable) {
        try {
            FullyQualifiedTable fqt = introspectedTable.getFullyQualifiedTable();

            ResultSet rs = databaseMetaData.getTables(fqt.getIntrospectedCatalog().orElse(null),
                    fqt.getIntrospectedSchema().orElse(null),
                    fqt.getIntrospectedTableName(), null);
            if (rs.next()) {
                String remarks = rs.getString("REMARKS"); //$NON-NLS-1$
                String tableType = rs.getString("TABLE_TYPE"); //$NON-NLS-1$
                introspectedTable.setRemarks(remarks);
                introspectedTable.setTableType(tableType);
            }
            closeResultSet(rs);
        } catch (SQLException e) {
            warnings.add(getString("Warning.27", e.getMessage())); //$NON-NLS-1$
        }
    }
}
