/**
 *    Copyright 2006-2015 the original author or authors.
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
package org.mybatis.generator.internal.db;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getCamelCaseString;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getValidPropertyName;
import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringContainsSQLWildcard;
import static org.mybatis.generator.internal.util.StringUtility.stringContainsSpace;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaReservedWords;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

/**
 * The Class DatabaseIntrospector.
 *
 * @author Jeff Butler
 */
public class DatabaseIntrospector {

    /** The database meta data. */
    private DatabaseMetaData databaseMetaData;
    
    /** The java type resolver. */
    private JavaTypeResolver javaTypeResolver;
    
    /** The warnings. */
    private List<String> warnings;
    
    /** The context. */
    private Context context;
    
    /** The logger. */
    private Log logger;

    /**
     * Instantiates a new database introspector.
     *
     * @param context
     *            the context
     * @param databaseMetaData
     *            the database meta data
     * @param javaTypeResolver
     *            the java type resolver
     * @param warnings
     *            the warnings
     */
    public DatabaseIntrospector(Context context,
            DatabaseMetaData databaseMetaData,
            JavaTypeResolver javaTypeResolver, List<String> warnings) {
        super();
        this.context = context;
        this.databaseMetaData = databaseMetaData;
        this.javaTypeResolver = javaTypeResolver;
        this.warnings = warnings;
        logger = LogFactory.getLog(getClass());
    }

    /**
     * Calculate primary key.
     *
     * @param table
     *            the table
     * @param introspectedTable
     *            the introspected table
     */
    private void calculatePrimaryKey(FullyQualifiedTable table,
            IntrospectedTable introspectedTable) {
        ResultSet rs = null;

        try {
            rs = databaseMetaData.getPrimaryKeys(
                    table.getIntrospectedCatalog(), table
                            .getIntrospectedSchema(), table
                            .getIntrospectedTableName());
        } catch (SQLException e) {
            closeResultSet(rs);
            warnings.add(getString("Warning.15")); //$NON-NLS-1$
            return;
        }

        try {
            // keep primary columns in key sequence order
            Map<Short, String> keyColumns = new TreeMap<Short, String>();
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

    /**
     * Close result set.
     *
     * @param rs
     *            the rs
     */
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
                ;
            }
        }
    }

    /**
     * Report introspection warnings.
     *
     * @param introspectedTable
     *            the introspected table
     * @param tableConfiguration
     *            the table configuration
     * @param table
     *            the table
     */
    private void reportIntrospectionWarnings(
            IntrospectedTable introspectedTable,
            TableConfiguration tableConfiguration, FullyQualifiedTable table) {
        // make sure that every column listed in column overrides
        // actually exists in the table
        for (ColumnOverride columnOverride : tableConfiguration
                .getColumnOverrides()) {
            if (introspectedTable.getColumn(columnOverride.getColumnName()) == null) {
                warnings.add(getString("Warning.3", //$NON-NLS-1$
                        columnOverride.getColumnName(), table.toString()));
            }
        }

        // make sure that every column listed in ignored columns
        // actually exists in the table
        for (String string : tableConfiguration.getIgnoredColumnsInError()) {
            warnings.add(getString("Warning.4", //$NON-NLS-1$
                    string, table.toString()));
        }

        GeneratedKey generatedKey = tableConfiguration.getGeneratedKey();
        if (generatedKey != null
                && introspectedTable.getColumn(generatedKey.getColumn()) == null) {
            if (generatedKey.isIdentity()) {
                warnings.add(getString("Warning.5", //$NON-NLS-1$
                        generatedKey.getColumn(), table.toString()));
            } else {
                warnings.add(getString("Warning.6", //$NON-NLS-1$
                        generatedKey.getColumn(), table.toString()));
            }
        }
        
        for (IntrospectedColumn ic : introspectedTable.getAllColumns()) {
            if (JavaReservedWords.containsWord(ic.getJavaProperty())) {
                warnings.add(getString("Warning.26", //$NON-NLS-1$
                        ic.getActualColumnName(), table.toString()));
            }
        }
    }

    /**
     * Returns a List of IntrospectedTable elements that matches the specified table configuration.
     *
     * @param tc
     *            the tc
     * @return a list of introspected tables
     * @throws SQLException
     *             the SQL exception
     */
    public List<IntrospectedTable> introspectTables(TableConfiguration tc)
            throws SQLException {

        // get the raw columns from the DB
        Map<ActualTableName, List<IntrospectedColumn>> columns = getColumns(tc);

        if (columns.isEmpty()) {
            warnings.add(getString("Warning.19", tc.getCatalog(), //$NON-NLS-1$
                    tc.getSchema(), tc.getTableName()));
            return null;
        }

        removeIgnoredColumns(tc, columns);
        calculateExtraColumnInformation(tc, columns);
        applyColumnOverrides(tc, columns);
        calculateIdentityColumns(tc, columns);

        List<IntrospectedTable> introspectedTables = calculateIntrospectedTables(
                tc, columns);

        // now introspectedTables has all the columns from all the
        // tables in the configuration. Do some validation...

        Iterator<IntrospectedTable> iter = introspectedTables.iterator();
        while (iter.hasNext()) {
            IntrospectedTable introspectedTable = iter.next();

            if (!introspectedTable.hasAnyColumns()) {
                // add warning that the table has no columns, remove from the
                // list
                String warning = getString(
                                "Warning.1", introspectedTable.getFullyQualifiedTable().toString()); //$NON-NLS-1$
                warnings.add(warning);
                iter.remove();
            } else if (!introspectedTable.hasPrimaryKeyColumns()
                    && !introspectedTable.hasBaseColumns()) {
                // add warning that the table has only BLOB columns, remove from
                // the list
                String warning = getString(
                                "Warning.18", introspectedTable.getFullyQualifiedTable().toString()); //$NON-NLS-1$ 
                warnings.add(warning);
                iter.remove();
            } else {
                // now make sure that all columns called out in the
                // configuration
                // actually exist
                reportIntrospectionWarnings(introspectedTable, tc,
                        introspectedTable.getFullyQualifiedTable());
            }
        }

        return introspectedTables;
    }

    /**
     * Removes the ignored columns.
     *
     * @param tc
     *            the tc
     * @param columns
     *            the columns
     */
    private void removeIgnoredColumns(TableConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns
                .entrySet()) {
            Iterator<IntrospectedColumn> tableColumns = (entry.getValue())
                    .iterator();
            while (tableColumns.hasNext()) {
                IntrospectedColumn introspectedColumn = tableColumns.next();
                if (tc
                        .isColumnIgnored(introspectedColumn
                                .getActualColumnName())) {
                    tableColumns.remove();
                    if (logger.isDebugEnabled()) {
                        logger.debug(getString("Tracing.3", //$NON-NLS-1$
                                introspectedColumn.getActualColumnName(), entry
                                        .getKey().toString()));
                    }
                }
            }
        }
    }

    /**
     * Calculate extra column information.
     *
     * @param tc
     *            the tc
     * @param columns
     *            the columns
     */
    private void calculateExtraColumnInformation(TableConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = null;
        String replaceString = null;
        if (tc.getColumnRenamingRule() != null) {
            pattern = Pattern.compile(tc.getColumnRenamingRule()
                    .getSearchString());
            replaceString = tc.getColumnRenamingRule().getReplaceString();
            replaceString = replaceString == null ? "" : replaceString; //$NON-NLS-1$
        }

        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns
                .entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                String calculatedColumnName;
                if (pattern == null) {
                    calculatedColumnName = introspectedColumn
                            .getActualColumnName();
                } else {
                    Matcher matcher = pattern.matcher(introspectedColumn
                            .getActualColumnName());
                    calculatedColumnName = matcher.replaceAll(replaceString);
                }

                if (isTrue(tc
                        .getProperty(PropertyRegistry.TABLE_USE_ACTUAL_COLUMN_NAMES))) {
                    introspectedColumn.setJavaProperty(
                            getValidPropertyName(calculatedColumnName));
                } else if (isTrue(tc
                                .getProperty(PropertyRegistry.TABLE_USE_COMPOUND_PROPERTY_NAMES))) {
                    sb.setLength(0);
                    sb.append(calculatedColumnName);
                    sb.append('_');
                    sb.append(getCamelCaseString(
                            introspectedColumn.getRemarks(), true));
                    introspectedColumn.setJavaProperty(
                            getValidPropertyName(sb.toString()));
                } else {
                    introspectedColumn.setJavaProperty(
                            getCamelCaseString(calculatedColumnName, false));
                }

                FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver
                        .calculateJavaType(introspectedColumn);

                if (fullyQualifiedJavaType != null) {
                    introspectedColumn
                            .setFullyQualifiedJavaType(fullyQualifiedJavaType);
                    introspectedColumn.setJdbcTypeName(javaTypeResolver
                            .calculateJdbcTypeName(introspectedColumn));
                } else {
                    // type cannot be resolved. Check for ignored or overridden
                    boolean warn = true;
                    if (tc.isColumnIgnored(introspectedColumn
                            .getActualColumnName())) {
                        warn = false;
                    }

                    ColumnOverride co = tc.getColumnOverride(introspectedColumn
                            .getActualColumnName());
                    if (co != null) {
                        if (stringHasValue(co.getJavaType())
                                && stringHasValue(co.getJavaType())) {
                            warn = false;
                        }
                    }

                    // if the type is not supported, then we'll report a warning
                    if (warn) {
                        introspectedColumn
                                .setFullyQualifiedJavaType(FullyQualifiedJavaType
                                        .getObjectInstance());
                        introspectedColumn.setJdbcTypeName("OTHER"); //$NON-NLS-1$

                        String warning = getString("Warning.14", //$NON-NLS-1$
                                Integer.toString(introspectedColumn.getJdbcType()),
                                entry.getKey().toString(),
                                introspectedColumn.getActualColumnName());

                        warnings.add(warning);
                    }
                }

                if (context.autoDelimitKeywords()) {
                    if (SqlReservedWords.containsWord(introspectedColumn
                            .getActualColumnName())) {
                        introspectedColumn.setColumnNameDelimited(true);
                    }
                }

                if (tc.isAllColumnDelimitingEnabled()) {
                    introspectedColumn.setColumnNameDelimited(true);
                }
            }
        }
    }

    /**
     * Calculate identity columns.
     *
     * @param tc
     *            the tc
     * @param columns
     *            the columns
     */
    private void calculateIdentityColumns(TableConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {
        GeneratedKey gk = tc.getGeneratedKey();
        if (gk == null) {
            // no generated key, then no identity or sequence columns
            return;
        }
        
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns
                .entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                if (isMatchedColumn(introspectedColumn, gk)) {
                    if (gk.isIdentity() || gk.isJdbcStandard()) {
                        introspectedColumn.setIdentity(true);
                        introspectedColumn.setSequenceColumn(false);
                    } else {
                        introspectedColumn.setIdentity(false);
                        introspectedColumn.setSequenceColumn(true);
                    }
                }
            }
        }
    }
    
    /**
     * Checks if is matched column.
     *
     * @param introspectedColumn
     *            the introspected column
     * @param gk
     *            the gk
     * @return true, if is matched column
     */
    private boolean isMatchedColumn(IntrospectedColumn introspectedColumn, GeneratedKey gk) {
        if (introspectedColumn.isColumnNameDelimited()) {
            return introspectedColumn.getActualColumnName().equals(gk.getColumn());
        } else {
            return introspectedColumn.getActualColumnName().equalsIgnoreCase(gk.getColumn());
        }
    }

    /**
     * Apply column overrides.
     *
     * @param tc
     *            the tc
     * @param columns
     *            the columns
     */
    private void applyColumnOverrides(TableConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns
                .entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                ColumnOverride columnOverride = tc
                        .getColumnOverride(introspectedColumn
                                .getActualColumnName());

                if (columnOverride != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(getString("Tracing.4", //$NON-NLS-1$
                                introspectedColumn.getActualColumnName(), entry
                                        .getKey().toString()));
                    }

                    if (stringHasValue(columnOverride
                            .getJavaProperty())) {
                        introspectedColumn.setJavaProperty(columnOverride
                                .getJavaProperty());
                    }

                    if (stringHasValue(columnOverride
                            .getJavaType())) {
                        introspectedColumn
                                .setFullyQualifiedJavaType(new FullyQualifiedJavaType(
                                        columnOverride.getJavaType()));
                    }

                    if (stringHasValue(columnOverride
                            .getJdbcType())) {
                        introspectedColumn.setJdbcTypeName(columnOverride
                                .getJdbcType());
                    }

                    if (stringHasValue(columnOverride
                            .getTypeHandler())) {
                        introspectedColumn.setTypeHandler(columnOverride
                                .getTypeHandler());
                    }

                    if (columnOverride.isColumnNameDelimited()) {
                        introspectedColumn.setColumnNameDelimited(true);
                    }

                    introspectedColumn.setProperties(columnOverride
                            .getProperties());
                }
            }
        }
    }

    /**
     * This method returns a Map<ActualTableName, List<ColumnDefinitions>> of columns returned from the database
     * introspection.
     *
     * @param tc
     *            the tc
     * @return introspected columns
     * @throws SQLException
     *             the SQL exception
     */
    private Map<ActualTableName, List<IntrospectedColumn>> getColumns(
            TableConfiguration tc) throws SQLException {
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
            localCatalog = tc.getCatalog() == null ? null : tc.getCatalog()
                    .toLowerCase();
            localSchema = tc.getSchema() == null ? null : tc.getSchema()
                    .toLowerCase();
            localTableName = tc.getTableName() == null ? null : tc
                    .getTableName().toLowerCase();
        } else if (databaseMetaData.storesUpperCaseIdentifiers()) {
            localCatalog = tc.getCatalog() == null ? null : tc.getCatalog()
                    .toUpperCase();
            localSchema = tc.getSchema() == null ? null : tc.getSchema()
                    .toUpperCase();
            localTableName = tc.getTableName() == null ? null : tc
                    .getTableName().toUpperCase();
        } else {
            localCatalog = tc.getCatalog();
            localSchema = tc.getSchema();
            localTableName = tc.getTableName();
        }

        if (tc.isWildcardEscapingEnabled()) {
            String escapeString = databaseMetaData.getSearchStringEscape();

            StringBuilder sb = new StringBuilder();
            StringTokenizer st;
            if (localSchema != null) {
                st = new StringTokenizer(localSchema, "_%", true); //$NON-NLS-1$
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token.equals("_") //$NON-NLS-1$
                            || token.equals("%")) { //$NON-NLS-1$
                        sb.append(escapeString);
                    }
                    sb.append(token);
                }
                localSchema = sb.toString();
            }

            sb.setLength(0);
            st = new StringTokenizer(localTableName, "_%", true); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.equals("_") //$NON-NLS-1$
                        || token.equals("%")) { //$NON-NLS-1$
                    sb.append(escapeString);
                }
                sb.append(token);
            }
            localTableName = sb.toString();
        }

        Map<ActualTableName, List<IntrospectedColumn>> answer = new HashMap<ActualTableName, List<IntrospectedColumn>>();

        if (logger.isDebugEnabled()) {
            String fullTableName = composeFullyQualifiedTableName(localCatalog, localSchema,
                            localTableName, '.');
            logger.debug(getString("Tracing.1", fullTableName)); //$NON-NLS-1$
        }

        ResultSet rs = databaseMetaData.getColumns(localCatalog, localSchema,
                localTableName, null);

        while (rs.next()) {
            IntrospectedColumn introspectedColumn = ObjectFactory
                    .createIntrospectedColumn(context);

            introspectedColumn.setTableAlias(tc.getAlias());
            introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE")); //$NON-NLS-1$
            introspectedColumn.setLength(rs.getInt("COLUMN_SIZE")); //$NON-NLS-1$
            introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME")); //$NON-NLS-1$
            introspectedColumn
                    .setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable); //$NON-NLS-1$
            introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS")); //$NON-NLS-1$
            introspectedColumn.setRemarks(rs.getString("REMARKS")); //$NON-NLS-1$
            introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF")); //$NON-NLS-1$

            ActualTableName atn = new ActualTableName(
                    rs.getString("TABLE_CAT"), //$NON-NLS-1$
                    rs.getString("TABLE_SCHEM"), //$NON-NLS-1$
                    rs.getString("TABLE_NAME")); //$NON-NLS-1$

            List<IntrospectedColumn> columns = answer.get(atn);
            if (columns == null) {
                columns = new ArrayList<IntrospectedColumn>();
                answer.put(atn, columns);
            }

            columns.add(introspectedColumn);

            if (logger.isDebugEnabled()) {
                logger.debug(getString(
                        "Tracing.2", //$NON-NLS-1$
                        introspectedColumn.getActualColumnName(), Integer
                                .toString(introspectedColumn.getJdbcType()),
                        atn.toString()));
            }
        }

        closeResultSet(rs);

        if (answer.size() > 1
                && !stringContainsSQLWildcard(localSchema)
                && !stringContainsSQLWildcard(localTableName)) {
            // issue a warning if there is more than one table and
            // no wildcards were used
            ActualTableName inputAtn = new ActualTableName(tc.getCatalog(), tc
                    .getSchema(), tc.getTableName());

            StringBuilder sb = new StringBuilder();
            boolean comma = false;
            for (ActualTableName atn : answer.keySet()) {
                if (comma) {
                    sb.append(',');
                } else {
                    comma = true;
                }
                sb.append(atn.toString());
            }

            warnings.add(getString("Warning.25", //$NON-NLS-1$
                    inputAtn.toString(), sb.toString()));
        }

        return answer;
    }

    /**
     * Calculate introspected tables.
     *
     * @param tc
     *            the tc
     * @param columns
     *            the columns
     * @return the list
     */
    private List<IntrospectedTable> calculateIntrospectedTables(
            TableConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {
        boolean delimitIdentifiers = tc.isDelimitIdentifiers()
                || stringContainsSpace(tc.getCatalog())
                || stringContainsSpace(tc.getSchema())
                || stringContainsSpace(tc.getTableName());

        List<IntrospectedTable> answer = new ArrayList<IntrospectedTable>();

        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns
                .entrySet()) {
            ActualTableName atn = entry.getKey();

            // we only use the returned catalog and schema if something was
            // actually
            // specified on the table configuration. If something was returned
            // from the DB for these fields, but nothing was specified on the
            // table
            // configuration, then some sort of DB default is being returned
            // and we don't want that in our SQL
            FullyQualifiedTable table = new FullyQualifiedTable(
                    stringHasValue(tc.getCatalog()) ? atn
                            .getCatalog() : null,
                    stringHasValue(tc.getSchema()) ? atn
                            .getSchema() : null,
                    atn.getTableName(),
                    tc.getDomainObjectName(),
                    tc.getAlias(),
                    isTrue(tc.getProperty(PropertyRegistry.TABLE_IGNORE_QUALIFIERS_AT_RUNTIME)),
                    tc.getProperty(PropertyRegistry.TABLE_RUNTIME_CATALOG),
                    tc.getProperty(PropertyRegistry.TABLE_RUNTIME_SCHEMA),
                    tc.getProperty(PropertyRegistry.TABLE_RUNTIME_TABLE_NAME),
                    delimitIdentifiers, context);

            IntrospectedTable introspectedTable = ObjectFactory
                    .createIntrospectedTable(tc, table, context);

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
     * This method calls database metadata to retrieve some extra information about the table
     * such as remarks associated with the table and the type.
     * 
     * If there is any error, we just add a warning and continue.
     * 
     * @param introspectedTable
     */
    private void enhanceIntrospectedTable(IntrospectedTable introspectedTable) {
        try {
            FullyQualifiedTable fqt = introspectedTable.getFullyQualifiedTable();

            ResultSet rs = databaseMetaData.getTables(fqt.getIntrospectedCatalog(), fqt.getIntrospectedSchema(),
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
