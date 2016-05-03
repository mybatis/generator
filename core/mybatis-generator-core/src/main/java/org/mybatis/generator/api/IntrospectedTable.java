/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.api;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.rules.ConditionalModelRules;
import org.mybatis.generator.internal.rules.FlatModelRules;
import org.mybatis.generator.internal.rules.HierarchicalModelRules;
import org.mybatis.generator.internal.rules.Rules;

/**
 * Base class for all code generator implementations. This class provides many
 * of the housekeeping methods needed to implement a code generator, with only
 * the actual code generation methods left unimplemented.
 * 
 * @author Jeff Butler
 * 
 */
public abstract class IntrospectedTable {
    
    /**
     * The Enum TargetRuntime.
     */
    public enum TargetRuntime {
        
        /** The IBATI s2. */
        IBATIS2, 
        /** The MYBATI s3. */
        MYBATIS3
    }

    /**
     * The Enum InternalAttribute.
     */
    protected enum InternalAttribute {
        
        /** The attr dao implementation type. */
        ATTR_DAO_IMPLEMENTATION_TYPE,
        
        /** The attr dao interface type. */
        ATTR_DAO_INTERFACE_TYPE,
        
        /** The attr primary key type. */
        ATTR_PRIMARY_KEY_TYPE,
        
        /** The attr base record type. */
        ATTR_BASE_RECORD_TYPE,
        
        /** The attr record with blobs type. */
        ATTR_RECORD_WITH_BLOBS_TYPE,
        
        /** The attr example type. */
        ATTR_EXAMPLE_TYPE,
        
        /** The ATT r_ ibati s2_ sq l_ ma p_ package. */
        ATTR_IBATIS2_SQL_MAP_PACKAGE,
        
        /** The ATT r_ ibati s2_ sq l_ ma p_ fil e_ name. */
        ATTR_IBATIS2_SQL_MAP_FILE_NAME,
        
        /** The ATT r_ ibati s2_ sq l_ ma p_ namespace. */
        ATTR_IBATIS2_SQL_MAP_NAMESPACE,
        
        /** The ATT r_ mybati s3_ xm l_ mappe r_ package. */
        ATTR_MYBATIS3_XML_MAPPER_PACKAGE,
        
        /** The ATT r_ mybati s3_ xm l_ mappe r_ fil e_ name. */
        ATTR_MYBATIS3_XML_MAPPER_FILE_NAME,
        
        /** also used as XML Mapper namespace if a Java mapper is generated. */
        ATTR_MYBATIS3_JAVA_MAPPER_TYPE,
        
        /** used as XML Mapper namespace if no client is generated. */
        ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE,
        
        /** The attr fully qualified table name at runtime. */
        ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
        
        /** The attr aliased fully qualified table name at runtime. */
        ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
        
        /** The attr count by example statement id. */
        ATTR_COUNT_BY_EXAMPLE_STATEMENT_ID,
        
        /** The attr delete by example statement id. */
        ATTR_DELETE_BY_EXAMPLE_STATEMENT_ID,
        
        /** The attr delete by primary key statement id. */
        ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID,
        
        /** The attr insert statement id. */
        ATTR_INSERT_STATEMENT_ID,
        
        /** The attr insert selective statement id. */
        ATTR_INSERT_SELECTIVE_STATEMENT_ID,
        
        /** The attr select all statement id. */
        ATTR_SELECT_ALL_STATEMENT_ID,
        
        /** The attr select by example statement id. */
        ATTR_SELECT_BY_EXAMPLE_STATEMENT_ID,
        
        /** The attr select by example with blobs statement id. */
        ATTR_SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID,
        
        /** The attr select by primary key statement id. */
        ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID,
        
        /** The attr update by example statement id. */
        ATTR_UPDATE_BY_EXAMPLE_STATEMENT_ID,
        
        /** The attr update by example selective statement id. */
        ATTR_UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID,
        
        /** The attr update by example with blobs statement id. */
        ATTR_UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID,
        
        /** The attr update by primary key statement id. */
        ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID,
        
        /** The attr update by primary key selective statement id. */
        ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID,
        
        /** The attr update by primary key with blobs statement id. */
        ATTR_UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID,
        
        /** The attr base result map id. */
        ATTR_BASE_RESULT_MAP_ID,
        
        /** The attr result map with blobs id. */
        ATTR_RESULT_MAP_WITH_BLOBS_ID,
        
        /** The attr example where clause id. */
        ATTR_EXAMPLE_WHERE_CLAUSE_ID,
        
        /** The attr base column list id. */
        ATTR_BASE_COLUMN_LIST_ID,
        
        /** The attr blob column list id. */
        ATTR_BLOB_COLUMN_LIST_ID,
        
        /** The ATT r_ mybati s3_ updat e_ b y_ exampl e_ wher e_ claus e_ id. */
        ATTR_MYBATIS3_UPDATE_BY_EXAMPLE_WHERE_CLAUSE_ID,
        
        /** The ATT r_ mybati s3_ sq l_ provide r_ type. */
        ATTR_MYBATIS3_SQL_PROVIDER_TYPE
    }

    /** The table configuration. */
    protected TableConfiguration tableConfiguration;
    
    /** The fully qualified table. */
    protected FullyQualifiedTable fullyQualifiedTable;
    
    /** The context. */
    protected Context context;
    
    /** The rules. */
    protected Rules rules;
    
    /** The primary key columns. */
    protected List<IntrospectedColumn> primaryKeyColumns;
    
    /** The base columns. */
    protected List<IntrospectedColumn> baseColumns;
    
    /** The blob columns. */
    protected List<IntrospectedColumn> blobColumns;
    
    /** The target runtime. */
    protected TargetRuntime targetRuntime;

    /**
     * Attributes may be used by plugins to capture table related state between
     * the different plugin calls.
     */
    protected Map<String, Object> attributes;

    /** Internal attributes are used to store commonly accessed items by all code generators. */
    protected Map<IntrospectedTable.InternalAttribute, String> internalAttributes;
    
    /**
     * Table remarks retrieved from database metadata
     */
    protected String remarks;
    
    /**
     * Table type retrieved from database metadata
     */
    protected String tableType;

    /**
     * Instantiates a new introspected table.
     *
     * @param targetRuntime
     *            the target runtime
     */
    public IntrospectedTable(TargetRuntime targetRuntime) {
        super();
        this.targetRuntime = targetRuntime;
        primaryKeyColumns = new ArrayList<IntrospectedColumn>();
        baseColumns = new ArrayList<IntrospectedColumn>();
        blobColumns = new ArrayList<IntrospectedColumn>();
        attributes = new HashMap<String, Object>();
        internalAttributes = new HashMap<IntrospectedTable.InternalAttribute, String>();
    }

    /**
     * Gets the fully qualified table.
     *
     * @return the fully qualified table
     */
    public FullyQualifiedTable getFullyQualifiedTable() {
        return fullyQualifiedTable;
    }

    /**
     * Gets the select by example query id.
     *
     * @return the select by example query id
     */
    public String getSelectByExampleQueryId() {
        return tableConfiguration.getSelectByExampleQueryId();
    }

    /**
     * Gets the select by primary key query id.
     *
     * @return the select by primary key query id
     */
    public String getSelectByPrimaryKeyQueryId() {
        return tableConfiguration.getSelectByPrimaryKeyQueryId();
    }

    /**
     * Gets the generated key.
     *
     * @return the generated key
     */
    public GeneratedKey getGeneratedKey() {
        return tableConfiguration.getGeneratedKey();
    }

    /**
     * Gets the column.
     *
     * @param columnName
     *            the column name
     * @return the column
     */
    public IntrospectedColumn getColumn(String columnName) {
        if (columnName == null) {
            return null;
        } else {
            // search primary key columns
            for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            // search base columns
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            // search blob columns
            for (IntrospectedColumn introspectedColumn : blobColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            return null;
        }
    }

    /**
     * Returns true if any of the columns in the table are JDBC Dates (as
     * opposed to timestamps).
     * 
     * @return true if the table contains DATE columns
     */
    public boolean hasJDBCDateColumns() {
        boolean rc = false;

        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCDateColumn()) {
                rc = true;
                break;
            }
        }

        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCDateColumn()) {
                    rc = true;
                    break;
                }
            }
        }

        return rc;
    }

    /**
     * Returns true if any of the columns in the table are JDBC Times (as
     * opposed to timestamps).
     * 
     * @return true if the table contains TIME columns
     */
    public boolean hasJDBCTimeColumns() {
        boolean rc = false;

        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCTimeColumn()) {
                rc = true;
                break;
            }
        }

        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCTimeColumn()) {
                    rc = true;
                    break;
                }
            }
        }

        return rc;
    }

    /**
     * Returns the columns in the primary key. If the generatePrimaryKeyClass()
     * method returns false, then these columns will be iterated as the
     * parameters of the selectByPrimaryKay and deleteByPrimaryKey methods
     * 
     * @return a List of ColumnDefinition objects for columns in the primary key
     */
    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    /**
     * Checks for primary key columns.
     *
     * @return true, if successful
     */
    public boolean hasPrimaryKeyColumns() {
        return primaryKeyColumns.size() > 0;
    }

    /**
     * Gets the base columns.
     *
     * @return the base columns
     */
    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }

    /**
     * Returns all columns in the table (for use by the select by primary key and select by example with BLOBs methods).
     *
     * @return a List of ColumnDefinition objects for all columns in the table
     */
    public List<IntrospectedColumn> getAllColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);

        return answer;
    }

    /**
     * Returns all columns except BLOBs (for use by the select by example without BLOBs method).
     *
     * @return a List of ColumnDefinition objects for columns in the table that are non BLOBs
     */
    public List<IntrospectedColumn> getNonBLOBColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);

        return answer;
    }

    /**
     * Gets the non blob column count.
     *
     * @return the non blob column count
     */
    public int getNonBLOBColumnCount() {
        return primaryKeyColumns.size() + baseColumns.size();
    }

    /**
     * Gets the non primary key columns.
     *
     * @return the non primary key columns
     */
    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);

        return answer;
    }

    /**
     * Gets the BLOB columns.
     *
     * @return the BLOB columns
     */
    public List<IntrospectedColumn> getBLOBColumns() {
        return blobColumns;
    }

    /**
     * Checks for blob columns.
     *
     * @return true, if successful
     */
    public boolean hasBLOBColumns() {
        return blobColumns.size() > 0;
    }

    /**
     * Checks for base columns.
     *
     * @return true, if successful
     */
    public boolean hasBaseColumns() {
        return baseColumns.size() > 0;
    }

    /**
     * Gets the rules.
     *
     * @return the rules
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Gets the table configuration property.
     *
     * @param property
     *            the property
     * @return the table configuration property
     */
    public String getTableConfigurationProperty(String property) {
        return tableConfiguration.getProperty(property);
    }

    /**
     * Gets the primary key type.
     *
     * @return the primary key type
     */
    public String getPrimaryKeyType() {
        return internalAttributes.get(InternalAttribute.ATTR_PRIMARY_KEY_TYPE);
    }

    /**
     * Gets the base record type.
     *
     * @return the type for the record (the class that holds non-primary key and non-BLOB fields). Note that the value
     *         will be calculated regardless of whether the table has these columns or not.
     */
    public String getBaseRecordType() {
        return internalAttributes.get(InternalAttribute.ATTR_BASE_RECORD_TYPE);
    }

    /**
     * Gets the example type.
     *
     * @return the type for the example class.
     */
    public String getExampleType() {
        return internalAttributes.get(InternalAttribute.ATTR_EXAMPLE_TYPE);
    }

    /**
     * Gets the record with blo bs type.
     *
     * @return the type for the record with BLOBs class. Note that the value will be calculated regardless of whether
     *         the table has BLOB columns or not.
     */
    public String getRecordWithBLOBsType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_RECORD_WITH_BLOBS_TYPE);
    }

    /**
     * Calculates an SQL Map file name for the table. Typically the name is
     * "XXXX_SqlMap.xml" where XXXX is the fully qualified table name (delimited
     * with underscores).
     * 
     * @return the name of the SqlMap file
     */
    public String getIbatis2SqlMapFileName() {
        return internalAttributes
                .get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_FILE_NAME);
    }

    /**
     * Gets the ibatis2 sql map namespace.
     *
     * @return the ibatis2 sql map namespace
     */
    public String getIbatis2SqlMapNamespace() {
        return internalAttributes
                .get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_NAMESPACE);
    }

    /**
     * Gets the my batis3 sql map namespace.
     *
     * @return the my batis3 sql map namespace
     */
    public String getMyBatis3SqlMapNamespace() {
        String namespace = getMyBatis3JavaMapperType();
        if (namespace == null) {
            namespace = getMyBatis3FallbackSqlMapNamespace();
        }
        
        return namespace;
    }
    
    /**
     * Gets the my batis3 fallback sql map namespace.
     *
     * @return the my batis3 fallback sql map namespace
     */
    public String getMyBatis3FallbackSqlMapNamespace() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE);
    }
    
    /**
     * Calculates the package for the current table.
     * 
     * @return the package for the SqlMap for the current table
     */
    public String getIbatis2SqlMapPackage() {
        return internalAttributes
                .get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_PACKAGE);
    }

    /**
     * Gets the DAO implementation type.
     *
     * @return the DAO implementation type
     */
    public String getDAOImplementationType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_DAO_IMPLEMENTATION_TYPE);
    }

    /**
     * Gets the DAO interface type.
     *
     * @return the DAO interface type
     */
    public String getDAOInterfaceType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_DAO_INTERFACE_TYPE);
    }

    /**
     * Checks for any columns.
     *
     * @return true, if successful
     */
    public boolean hasAnyColumns() {
        return primaryKeyColumns.size() > 0 || baseColumns.size() > 0
                || blobColumns.size() > 0;
    }

    /**
     * Sets the table configuration.
     *
     * @param tableConfiguration
     *            the new table configuration
     */
    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }

    /**
     * Sets the fully qualified table.
     *
     * @param fullyQualifiedTable
     *            the new fully qualified table
     */
    public void setFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
        this.fullyQualifiedTable = fullyQualifiedTable;
    }

    /**
     * Sets the context.
     *
     * @param context
     *            the new context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Adds the column.
     *
     * @param introspectedColumn
     *            the introspected column
     */
    public void addColumn(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            blobColumns.add(introspectedColumn);
        } else {
            baseColumns.add(introspectedColumn);
        }

        introspectedColumn.setIntrospectedTable(this);
    }

    /**
     * Adds the primary key column.
     *
     * @param columnName
     *            the column name
     */
    public void addPrimaryKeyColumn(String columnName) {
        boolean found = false;
        // first search base columns
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getActualColumnName().equals(columnName)) {
                primaryKeyColumns.add(introspectedColumn);
                iter.remove();
                found = true;
                break;
            }
        }

        // search blob columns in the weird event that a blob is the primary key
        if (!found) {
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                IntrospectedColumn introspectedColumn = iter.next();
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    primaryKeyColumns.add(introspectedColumn);
                    iter.remove();
                    found = true;
                    break;
                }
            }
        }
    }

    /**
     * Gets the attribute.
     *
     * @param name
     *            the name
     * @return the attribute
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * Removes the attribute.
     *
     * @param name
     *            the name
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * Sets the attribute.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Initialize.
     */
    public void initialize() {
        calculateJavaClientAttributes();
        calculateModelAttributes();
        calculateXmlAttributes();

        if (tableConfiguration.getModelType() == ModelType.HIERARCHICAL) {
            rules = new HierarchicalModelRules(this);
        } else if (tableConfiguration.getModelType() == ModelType.FLAT) {
            rules = new FlatModelRules(this);
        } else {
            rules = new ConditionalModelRules(this);
        }

        context.getPlugins().initialized(this);
    }

    /**
     * Calculate xml attributes.
     */
    protected void calculateXmlAttributes() {
        setIbatis2SqlMapPackage(calculateSqlMapPackage());
        setIbatis2SqlMapFileName(calculateIbatis2SqlMapFileName());
        setMyBatis3XmlMapperFileName(calculateMyBatis3XmlMapperFileName());
        setMyBatis3XmlMapperPackage(calculateSqlMapPackage());

        setIbatis2SqlMapNamespace(calculateIbatis2SqlMapNamespace());
        setMyBatis3FallbackSqlMapNamespace(calculateMyBatis3FallbackSqlMapNamespace());
        
        setSqlMapFullyQualifiedRuntimeTableName(calculateSqlMapFullyQualifiedRuntimeTableName());
        setSqlMapAliasedFullyQualifiedRuntimeTableName(calculateSqlMapAliasedFullyQualifiedRuntimeTableName());

        setCountByExampleStatementId("countByExample"); //$NON-NLS-1$
        setDeleteByExampleStatementId("deleteByExample"); //$NON-NLS-1$
        setDeleteByPrimaryKeyStatementId("deleteByPrimaryKey"); //$NON-NLS-1$
        setInsertStatementId("insert"); //$NON-NLS-1$
        setInsertSelectiveStatementId("insertSelective"); //$NON-NLS-1$
        setSelectAllStatementId("selectAll"); //$NON-NLS-1$
        setSelectByExampleStatementId("selectByExample"); //$NON-NLS-1$
        setSelectByExampleWithBLOBsStatementId("selectByExampleWithBLOBs"); //$NON-NLS-1$
        setSelectByPrimaryKeyStatementId("selectByPrimaryKey"); //$NON-NLS-1$
        setUpdateByExampleStatementId("updateByExample"); //$NON-NLS-1$
        setUpdateByExampleSelectiveStatementId("updateByExampleSelective"); //$NON-NLS-1$
        setUpdateByExampleWithBLOBsStatementId("updateByExampleWithBLOBs"); //$NON-NLS-1$
        setUpdateByPrimaryKeyStatementId("updateByPrimaryKey"); //$NON-NLS-1$
        setUpdateByPrimaryKeySelectiveStatementId("updateByPrimaryKeySelective"); //$NON-NLS-1$
        setUpdateByPrimaryKeyWithBLOBsStatementId("updateByPrimaryKeyWithBLOBs"); //$NON-NLS-1$
        setBaseResultMapId("BaseResultMap"); //$NON-NLS-1$
        setResultMapWithBLOBsId("ResultMapWithBLOBs"); //$NON-NLS-1$
        setExampleWhereClauseId("Example_Where_Clause"); //$NON-NLS-1$
        setBaseColumnListId("Base_Column_List"); //$NON-NLS-1$
        setBlobColumnListId("Blob_Column_List"); //$NON-NLS-1$
        setMyBatis3UpdateByExampleWhereClauseId("Update_By_Example_Where_Clause"); //$NON-NLS-1$
    }

    /**
     * Sets the blob column list id.
     *
     * @param s
     *            the new blob column list id
     */
    public void setBlobColumnListId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BLOB_COLUMN_LIST_ID, s);
    }

    /**
     * Sets the base column list id.
     *
     * @param s
     *            the new base column list id
     */
    public void setBaseColumnListId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID, s);
    }

    /**
     * Sets the example where clause id.
     *
     * @param s
     *            the new example where clause id
     */
    public void setExampleWhereClauseId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_EXAMPLE_WHERE_CLAUSE_ID,
                s);
    }

    /**
     * Sets the my batis3 update by example where clause id.
     *
     * @param s
     *            the new my batis3 update by example where clause id
     */
    public void setMyBatis3UpdateByExampleWhereClauseId(String s) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_MYBATIS3_UPDATE_BY_EXAMPLE_WHERE_CLAUSE_ID,
                        s);
    }

    /**
     * Sets the result map with blo bs id.
     *
     * @param s
     *            the new result map with blo bs id
     */
    public void setResultMapWithBLOBsId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_RESULT_MAP_WITH_BLOBS_ID,
                s);
    }

    /**
     * Sets the base result map id.
     *
     * @param s
     *            the new base result map id
     */
    public void setBaseResultMapId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_RESULT_MAP_ID, s);
    }

    /**
     * Sets the update by primary key with blo bs statement id.
     *
     * @param s
     *            the new update by primary key with blo bs statement id
     */
    public void setUpdateByPrimaryKeyWithBLOBsStatementId(String s) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID,
                        s);
    }

    /**
     * Sets the update by primary key selective statement id.
     *
     * @param s
     *            the new update by primary key selective statement id
     */
    public void setUpdateByPrimaryKeySelectiveStatementId(String s) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID,
                        s);
    }

    /**
     * Sets the update by primary key statement id.
     *
     * @param s
     *            the new update by primary key statement id
     */
    public void setUpdateByPrimaryKeyStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }

    /**
     * Sets the update by example with blo bs statement id.
     *
     * @param s
     *            the new update by example with blo bs statement id
     */
    public void setUpdateByExampleWithBLOBsStatementId(String s) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID,
                        s);
    }

    /**
     * Sets the update by example selective statement id.
     *
     * @param s
     *            the new update by example selective statement id
     */
    public void setUpdateByExampleSelectiveStatementId(String s) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID,
                        s);
    }

    /**
     * Sets the update by example statement id.
     *
     * @param s
     *            the new update by example statement id
     */
    public void setUpdateByExampleStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_STATEMENT_ID, s);
    }

    /**
     * Sets the select by primary key statement id.
     *
     * @param s
     *            the new select by primary key statement id
     */
    public void setSelectByPrimaryKeyStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }

    /**
     * Sets the select by example with blo bs statement id.
     *
     * @param s
     *            the new select by example with blo bs statement id
     */
    public void setSelectByExampleWithBLOBsStatementId(String s) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID,
                        s);
    }

    /**
     * Sets the select all statement id.
     *
     * @param s
     *            the new select all statement id
     */
    public void setSelectAllStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_SELECT_ALL_STATEMENT_ID, s);
    }

    /**
     * Sets the select by example statement id.
     *
     * @param s
     *            the new select by example statement id
     */
    public void setSelectByExampleStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_SELECT_BY_EXAMPLE_STATEMENT_ID, s);
    }

    /**
     * Sets the insert selective statement id.
     *
     * @param s
     *            the new insert selective statement id
     */
    public void setInsertSelectiveStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID, s);
    }

    /**
     * Sets the insert statement id.
     *
     * @param s
     *            the new insert statement id
     */
    public void setInsertStatementId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_INSERT_STATEMENT_ID, s);
    }

    /**
     * Sets the delete by primary key statement id.
     *
     * @param s
     *            the new delete by primary key statement id
     */
    public void setDeleteByPrimaryKeyStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }

    /**
     * Sets the delete by example statement id.
     *
     * @param s
     *            the new delete by example statement id
     */
    public void setDeleteByExampleStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_DELETE_BY_EXAMPLE_STATEMENT_ID, s);
    }

    /**
     * Sets the count by example statement id.
     *
     * @param s
     *            the new count by example statement id
     */
    public void setCountByExampleStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_COUNT_BY_EXAMPLE_STATEMENT_ID, s);
    }

    /**
     * Gets the blob column list id.
     *
     * @return the blob column list id
     */
    public String getBlobColumnListId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_BLOB_COLUMN_LIST_ID);
    }

    /**
     * Gets the base column list id.
     *
     * @return the base column list id
     */
    public String getBaseColumnListId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID);
    }

    /**
     * Gets the example where clause id.
     *
     * @return the example where clause id
     */
    public String getExampleWhereClauseId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_EXAMPLE_WHERE_CLAUSE_ID);
    }

    /**
     * Gets the my batis3 update by example where clause id.
     *
     * @return the my batis3 update by example where clause id
     */
    public String getMyBatis3UpdateByExampleWhereClauseId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_UPDATE_BY_EXAMPLE_WHERE_CLAUSE_ID);
    }

    /**
     * Gets the result map with blo bs id.
     *
     * @return the result map with blo bs id
     */
    public String getResultMapWithBLOBsId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_RESULT_MAP_WITH_BLOBS_ID);
    }

    /**
     * Gets the base result map id.
     *
     * @return the base result map id
     */
    public String getBaseResultMapId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_BASE_RESULT_MAP_ID);
    }

    /**
     * Gets the update by primary key with blo bs statement id.
     *
     * @return the update by primary key with blo bs statement id
     */
    public String getUpdateByPrimaryKeyWithBLOBsStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_WITH_BLOBS_STATEMENT_ID);
    }

    /**
     * Gets the update by primary key selective statement id.
     *
     * @return the update by primary key selective statement id
     */
    public String getUpdateByPrimaryKeySelectiveStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID);
    }

    /**
     * Gets the update by primary key statement id.
     *
     * @return the update by primary key statement id
     */
    public String getUpdateByPrimaryKeyStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID);
    }

    /**
     * Gets the update by example with blo bs statement id.
     *
     * @return the update by example with blo bs statement id
     */
    public String getUpdateByExampleWithBLOBsStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID);
    }

    /**
     * Gets the update by example selective statement id.
     *
     * @return the update by example selective statement id
     */
    public String getUpdateByExampleSelectiveStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_SELECTIVE_STATEMENT_ID);
    }

    /**
     * Gets the update by example statement id.
     *
     * @return the update by example statement id
     */
    public String getUpdateByExampleStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_EXAMPLE_STATEMENT_ID);
    }

    /**
     * Gets the select by primary key statement id.
     *
     * @return the select by primary key statement id
     */
    public String getSelectByPrimaryKeyStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID);
    }

    /**
     * Gets the select by example with blo bs statement id.
     *
     * @return the select by example with blo bs statement id
     */
    public String getSelectByExampleWithBLOBsStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID);
    }

    /**
     * Gets the select all statement id.
     *
     * @return the select all statement id
     */
    public String getSelectAllStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_SELECT_ALL_STATEMENT_ID);
    }

    /**
     * Gets the select by example statement id.
     *
     * @return the select by example statement id
     */
    public String getSelectByExampleStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_SELECT_BY_EXAMPLE_STATEMENT_ID);
    }

    /**
     * Gets the insert selective statement id.
     *
     * @return the insert selective statement id
     */
    public String getInsertSelectiveStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID);
    }

    /**
     * Gets the insert statement id.
     *
     * @return the insert statement id
     */
    public String getInsertStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_INSERT_STATEMENT_ID);
    }

    /**
     * Gets the delete by primary key statement id.
     *
     * @return the delete by primary key statement id
     */
    public String getDeleteByPrimaryKeyStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID);
    }

    /**
     * Gets the delete by example statement id.
     *
     * @return the delete by example statement id
     */
    public String getDeleteByExampleStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_DELETE_BY_EXAMPLE_STATEMENT_ID);
    }

    /**
     * Gets the count by example statement id.
     *
     * @return the count by example statement id
     */
    public String getCountByExampleStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_COUNT_BY_EXAMPLE_STATEMENT_ID);
    }

    /**
     * Calculate java client implementation package.
     *
     * @return the string
     */
    protected String calculateJavaClientImplementationPackage() {
        JavaClientGeneratorConfiguration config = context
                .getJavaClientGeneratorConfiguration();
        if (config == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(config.getImplementationPackage())) {
            sb.append(config.getImplementationPackage());
        } else {
            sb.append(config.getTargetPackage());
        }

        sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));

        return sb.toString();
    }
    
    /**
     * Checks if is sub packages enabled.
     *
     * @param propertyHolder
     *            the property holder
     * @return true, if is sub packages enabled
     */
    private boolean isSubPackagesEnabled(PropertyHolder propertyHolder) {
        return isTrue(propertyHolder.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES));
    }

    /**
     * Calculate java client interface package.
     *
     * @return the string
     */
    protected String calculateJavaClientInterfacePackage() {
        JavaClientGeneratorConfiguration config = context
                .getJavaClientGeneratorConfiguration();
        if (config == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());

        sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));

        return sb.toString();
    }

    /**
     * Calculate java client attributes.
     */
    protected void calculateJavaClientAttributes() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(calculateJavaClientImplementationPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAOImpl"); //$NON-NLS-1$
        setDAOImplementationType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("DAO"); //$NON-NLS-1$
        setDAOInterfaceType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Mapper"); //$NON-NLS-1$
        setMyBatis3JavaMapperType(sb.toString());

        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("SqlProvider"); //$NON-NLS-1$
        setMyBatis3SqlProviderType(sb.toString());
    }

    /**
     * Calculate java model package.
     *
     * @return the string
     */
    protected String calculateJavaModelPackage() {
        JavaModelGeneratorConfiguration config = context
                .getJavaModelGeneratorConfiguration();

        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));

        return sb.toString();
    }

    /**
     * Calculate model attributes.
     */
    protected void calculateModelAttributes() {
        String pakkage = calculateJavaModelPackage();

        StringBuilder sb = new StringBuilder();
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Key"); //$NON-NLS-1$
        setPrimaryKeyType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        setBaseRecordType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("WithBLOBs"); //$NON-NLS-1$
        setRecordWithBLOBsType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Example"); //$NON-NLS-1$
        setExampleType(sb.toString());
    }

    /**
     * Calculate sql map package.
     *
     * @return the string
     */
    protected String calculateSqlMapPackage() {
        StringBuilder sb = new StringBuilder();
        SqlMapGeneratorConfiguration config = context
                .getSqlMapGeneratorConfiguration();
        
        // config can be null if the Java client does not require XML
        if (config != null) {
            sb.append(config.getTargetPackage());
            sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));
        }

        return sb.toString();
    }

    /**
     * Calculate ibatis2 sql map file name.
     *
     * @return the string
     */
    protected String calculateIbatis2SqlMapFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getIbatis2SqlMapNamespace());
        sb.append("_SqlMap.xml"); //$NON-NLS-1$
        return sb.toString();
    }

    /**
     * Calculate my batis3 xml mapper file name.
     *
     * @return the string
     */
    protected String calculateMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Mapper.xml"); //$NON-NLS-1$
        return sb.toString();
    }

    /**
     * Calculate ibatis2 sql map namespace.
     *
     * @return the string
     */
    protected String calculateIbatis2SqlMapNamespace() {
        return fullyQualifiedTable.getIbatis2SqlMapNamespace();
    }
    
    /**
     * Calculate my batis3 fallback sql map namespace.
     *
     * @return the string
     */
    protected String calculateMyBatis3FallbackSqlMapNamespace() {
        StringBuilder sb = new StringBuilder();
        sb.append(calculateSqlMapPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Mapper"); //$NON-NLS-1$
        return sb.toString();
    }

    /**
     * Calculate sql map fully qualified runtime table name.
     *
     * @return the string
     */
    protected String calculateSqlMapFullyQualifiedRuntimeTableName() {
        return fullyQualifiedTable.getFullyQualifiedTableNameAtRuntime();
    }

    /**
     * Calculate sql map aliased fully qualified runtime table name.
     *
     * @return the string
     */
    protected String calculateSqlMapAliasedFullyQualifiedRuntimeTableName() {
        return fullyQualifiedTable.getAliasedFullyQualifiedTableNameAtRuntime();
    }

    /**
     * Gets the fully qualified table name at runtime.
     *
     * @return the fully qualified table name at runtime
     */
    public String getFullyQualifiedTableNameAtRuntime() {
        return internalAttributes
                .get(InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME);
    }

    /**
     * Gets the aliased fully qualified table name at runtime.
     *
     * @return the aliased fully qualified table name at runtime
     */
    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        return internalAttributes
                .get(InternalAttribute.ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME);
    }

    /**
     * This method can be used to initialize the generators before they will be called.
     * 
     * This method is called after all the setX methods, but before getNumberOfSubtasks(), getGeneratedJavaFiles, and
     * getGeneratedXmlFiles.
     *
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     */
    public abstract void calculateGenerators(List<String> warnings,
            ProgressCallback progressCallback);

    /**
     * This method should return a list of generated Java files related to this
     * table. This list could include various types of model classes, as well as
     * DAO classes.
     * 
     * @return the list of generated Java files for this table
     */
    public abstract List<GeneratedJavaFile> getGeneratedJavaFiles();

    /**
     * This method should return a list of generated XML files related to this
     * table. Most implementations will only return one file - the generated
     * SqlMap file.
     * 
     * @return the list of generated XML files for this table
     */
    public abstract List<GeneratedXmlFile> getGeneratedXmlFiles();

    /**
     * Denotes whether generated code is targeted for Java version 5.0 or
     * higher.
     * 
     * @return true if the generated code makes use of Java5 features
     */
    public abstract boolean isJava5Targeted();

    /**
     * This method should return the number of progress messages that will be
     * send during the generation phase.
     * 
     * @return the number of progress messages
     */
    public abstract int getGenerationSteps();

    /**
     * This method exists to give plugins the opportunity to replace the calculated rules if necessary.
     *
     * @param rules
     *            the new rules
     */
    public void setRules(Rules rules) {
        this.rules = rules;
    }

    /**
     * Gets the table configuration.
     *
     * @return the table configuration
     */
    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    /**
     * Sets the DAO implementation type.
     *
     * @param DAOImplementationType
     *            the new DAO implementation type
     */
    public void setDAOImplementationType(String DAOImplementationType) {
        internalAttributes.put(InternalAttribute.ATTR_DAO_IMPLEMENTATION_TYPE,
                DAOImplementationType);
    }

    /**
     * Sets the DAO interface type.
     *
     * @param DAOInterfaceType
     *            the new DAO interface type
     */
    public void setDAOInterfaceType(String DAOInterfaceType) {
        internalAttributes.put(InternalAttribute.ATTR_DAO_INTERFACE_TYPE,
                DAOInterfaceType);
    }

    /**
     * Sets the primary key type.
     *
     * @param primaryKeyType
     *            the new primary key type
     */
    public void setPrimaryKeyType(String primaryKeyType) {
        internalAttributes.put(InternalAttribute.ATTR_PRIMARY_KEY_TYPE,
                primaryKeyType);
    }

    /**
     * Sets the base record type.
     *
     * @param baseRecordType
     *            the new base record type
     */
    public void setBaseRecordType(String baseRecordType) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_RECORD_TYPE,
                baseRecordType);
    }

    /**
     * Sets the record with blo bs type.
     *
     * @param recordWithBLOBsType
     *            the new record with blo bs type
     */
    public void setRecordWithBLOBsType(String recordWithBLOBsType) {
        internalAttributes.put(InternalAttribute.ATTR_RECORD_WITH_BLOBS_TYPE,
                recordWithBLOBsType);
    }

    /**
     * Sets the example type.
     *
     * @param exampleType
     *            the new example type
     */
    public void setExampleType(String exampleType) {
        internalAttributes
                .put(InternalAttribute.ATTR_EXAMPLE_TYPE, exampleType);
    }

    /**
     * Sets the ibatis2 sql map package.
     *
     * @param sqlMapPackage
     *            the new ibatis2 sql map package
     */
    public void setIbatis2SqlMapPackage(String sqlMapPackage) {
        internalAttributes.put(InternalAttribute.ATTR_IBATIS2_SQL_MAP_PACKAGE,
                sqlMapPackage);
    }

    /**
     * Sets the ibatis2 sql map file name.
     *
     * @param sqlMapFileName
     *            the new ibatis2 sql map file name
     */
    public void setIbatis2SqlMapFileName(String sqlMapFileName) {
        internalAttributes.put(
                InternalAttribute.ATTR_IBATIS2_SQL_MAP_FILE_NAME,
                sqlMapFileName);
    }

    /**
     * Sets the ibatis2 sql map namespace.
     *
     * @param sqlMapNamespace
     *            the new ibatis2 sql map namespace
     */
    public void setIbatis2SqlMapNamespace(String sqlMapNamespace) {
        internalAttributes.put(
                InternalAttribute.ATTR_IBATIS2_SQL_MAP_NAMESPACE,
                sqlMapNamespace);
    }
    
    /**
     * Sets the my batis3 fallback sql map namespace.
     *
     * @param sqlMapNamespace
     *            the new my batis3 fallback sql map namespace
     */
    public void setMyBatis3FallbackSqlMapNamespace(String sqlMapNamespace) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE,
                sqlMapNamespace);
    }

    /**
     * Sets the sql map fully qualified runtime table name.
     *
     * @param fullyQualifiedRuntimeTableName
     *            the new sql map fully qualified runtime table name
     */
    public void setSqlMapFullyQualifiedRuntimeTableName(
            String fullyQualifiedRuntimeTableName) {
        internalAttributes.put(
                InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
                fullyQualifiedRuntimeTableName);
    }

    /**
     * Sets the sql map aliased fully qualified runtime table name.
     *
     * @param aliasedFullyQualifiedRuntimeTableName
     *            the new sql map aliased fully qualified runtime table name
     */
    public void setSqlMapAliasedFullyQualifiedRuntimeTableName(
            String aliasedFullyQualifiedRuntimeTableName) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
                        aliasedFullyQualifiedRuntimeTableName);
    }

    /**
     * Gets the my batis3 xml mapper package.
     *
     * @return the my batis3 xml mapper package
     */
    public String getMyBatis3XmlMapperPackage() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_PACKAGE);
    }

    /**
     * Sets the my batis3 xml mapper package.
     *
     * @param mybatis3XmlMapperPackage
     *            the new my batis3 xml mapper package
     */
    public void setMyBatis3XmlMapperPackage(String mybatis3XmlMapperPackage) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_PACKAGE,
                mybatis3XmlMapperPackage);
    }

    /**
     * Gets the my batis3 xml mapper file name.
     *
     * @return the my batis3 xml mapper file name
     */
    public String getMyBatis3XmlMapperFileName() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME);
    }

    /**
     * Sets the my batis3 xml mapper file name.
     *
     * @param mybatis3XmlMapperFileName
     *            the new my batis3 xml mapper file name
     */
    public void setMyBatis3XmlMapperFileName(String mybatis3XmlMapperFileName) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME,
                mybatis3XmlMapperFileName);
    }

    /**
     * Gets the my batis3 java mapper type.
     *
     * @return the my batis3 java mapper type
     */
    public String getMyBatis3JavaMapperType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_JAVA_MAPPER_TYPE);
    }

    /**
     * Sets the my batis3 java mapper type.
     *
     * @param mybatis3JavaMapperType
     *            the new my batis3 java mapper type
     */
    public void setMyBatis3JavaMapperType(String mybatis3JavaMapperType) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_JAVA_MAPPER_TYPE,
                mybatis3JavaMapperType);
    }

    /**
     * Gets the my batis3 sql provider type.
     *
     * @return the my batis3 sql provider type
     */
    public String getMyBatis3SqlProviderType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_SQL_PROVIDER_TYPE);
    }

    /**
     * Sets the my batis3 sql provider type.
     *
     * @param mybatis3SqlProviderType
     *            the new my batis3 sql provider type
     */
    public void setMyBatis3SqlProviderType(String mybatis3SqlProviderType) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_SQL_PROVIDER_TYPE,
                mybatis3SqlProviderType);
    }
    
    /**
     * Gets the target runtime.
     *
     * @return the target runtime
     */
    public TargetRuntime getTargetRuntime() {
        return targetRuntime;
    }
    
    /**
     * Checks if is immutable.
     *
     * @return true, if is immutable
     */
    public boolean isImmutable() {
        Properties properties;
        
        if (tableConfiguration.getProperties().containsKey(PropertyRegistry.ANY_IMMUTABLE)) {
            properties = tableConfiguration.getProperties();
        } else {
            properties = context.getJavaModelGeneratorConfiguration().getProperties();
        }
        
        return isTrue(properties.getProperty(PropertyRegistry.ANY_IMMUTABLE));
    }
    
    /**
     * Checks if is constructor based.
     *
     * @return true, if is constructor based
     */
    public boolean isConstructorBased() {
        if (isImmutable()) {
            return true;
        }
        
        Properties properties;
        
        if (tableConfiguration.getProperties().containsKey(PropertyRegistry.ANY_CONSTRUCTOR_BASED)) {
            properties = tableConfiguration.getProperties();
        } else {
            properties = context.getJavaModelGeneratorConfiguration().getProperties();
        }
        
        return isTrue(properties.getProperty(PropertyRegistry.ANY_CONSTRUCTOR_BASED));
    }

    /**
     * Should return true if an XML generator is required for this table. This method will be called during validation
     * of the configuration, so it should not rely on database introspection. This method simply tells the validator if
     * an XML configuration is normally required for this implementation.
     *
     * @return true, if successful
     */
    public abstract boolean requiresXMLGenerator();

    /**
     * Gets the context.
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
}
