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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.EqualsUtil.areEqual;
import static org.mybatis.generator.internal.util.HashCodeUtil.hash;
import static org.mybatis.generator.internal.util.HashCodeUtil.SEED;
import static org.mybatis.generator.internal.util.messages.Messages.getString;
import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * The Class TableConfiguration.
 *
 * @author Jeff Butler
 */
public class TableConfiguration extends PropertyHolder {
    
    /** The insert statement enabled. */
    private boolean insertStatementEnabled;

    /** The select by primary key statement enabled. */
    private boolean selectByPrimaryKeyStatementEnabled;

    /** The select by example statement enabled. */
    private boolean selectByExampleStatementEnabled;

    /** The update by primary key statement enabled. */
    private boolean updateByPrimaryKeyStatementEnabled;

    /** The delete by primary key statement enabled. */
    private boolean deleteByPrimaryKeyStatementEnabled;

    /** The delete by example statement enabled. */
    private boolean deleteByExampleStatementEnabled;

    /** The count by example statement enabled. */
    private boolean countByExampleStatementEnabled;

    /** The update by example statement enabled. */
    private boolean updateByExampleStatementEnabled;

    /** The column overrides. */
    private List<ColumnOverride> columnOverrides;

    /** The ignored columns. */
    private Map<IgnoredColumn, Boolean> ignoredColumns;

    /** The generated key. */
    private GeneratedKey generatedKey;

    /** The select by primary key query id. */
    private String selectByPrimaryKeyQueryId;

    /** The select by example query id. */
    private String selectByExampleQueryId;

    /** The catalog. */
    private String catalog;
    
    /** The schema. */
    private String schema;
    
    /** The table name. */
    private String tableName;
    
    /** The domain object name. */
    private String domainObjectName;
    
    /** The alias. */
    private String alias;
    
    /** The model type. */
    private ModelType modelType;
    
    /** The wildcard escaping enabled. */
    private boolean wildcardEscapingEnabled;
    
    /** The configured model type. */
    private String configuredModelType;
    
    /** The delimit identifiers. */
    private boolean delimitIdentifiers;

    /** The column renaming rule. */
    private ColumnRenamingRule columnRenamingRule;
    
    /** The is all column delimiting enabled. */
    private boolean isAllColumnDelimitingEnabled;

    /**
     * Instantiates a new table configuration.
     *
     * @param context
     *            the context
     */
    public TableConfiguration(Context context) {
        super();

        this.modelType = context.getDefaultModelType();

        columnOverrides = new ArrayList<ColumnOverride>();
        ignoredColumns = new HashMap<IgnoredColumn, Boolean>();

        insertStatementEnabled = true;
        selectByPrimaryKeyStatementEnabled = true;
        selectByExampleStatementEnabled = true;
        updateByPrimaryKeyStatementEnabled = true;
        deleteByPrimaryKeyStatementEnabled = true;
        deleteByExampleStatementEnabled = true;
        countByExampleStatementEnabled = true;
        updateByExampleStatementEnabled = true;
    }

    /**
     * Checks if is delete by primary key statement enabled.
     *
     * @return true, if is delete by primary key statement enabled
     */
    public boolean isDeleteByPrimaryKeyStatementEnabled() {
        return deleteByPrimaryKeyStatementEnabled;
    }

    /**
     * Sets the delete by primary key statement enabled.
     *
     * @param deleteByPrimaryKeyStatementEnabled
     *            the new delete by primary key statement enabled
     */
    public void setDeleteByPrimaryKeyStatementEnabled(
            boolean deleteByPrimaryKeyStatementEnabled) {
        this.deleteByPrimaryKeyStatementEnabled = deleteByPrimaryKeyStatementEnabled;
    }

    /**
     * Checks if is insert statement enabled.
     *
     * @return true, if is insert statement enabled
     */
    public boolean isInsertStatementEnabled() {
        return insertStatementEnabled;
    }

    /**
     * Sets the insert statement enabled.
     *
     * @param insertStatementEnabled
     *            the new insert statement enabled
     */
    public void setInsertStatementEnabled(boolean insertStatementEnabled) {
        this.insertStatementEnabled = insertStatementEnabled;
    }

    /**
     * Checks if is select by primary key statement enabled.
     *
     * @return true, if is select by primary key statement enabled
     */
    public boolean isSelectByPrimaryKeyStatementEnabled() {
        return selectByPrimaryKeyStatementEnabled;
    }

    /**
     * Sets the select by primary key statement enabled.
     *
     * @param selectByPrimaryKeyStatementEnabled
     *            the new select by primary key statement enabled
     */
    public void setSelectByPrimaryKeyStatementEnabled(
            boolean selectByPrimaryKeyStatementEnabled) {
        this.selectByPrimaryKeyStatementEnabled = selectByPrimaryKeyStatementEnabled;
    }

    /**
     * Checks if is update by primary key statement enabled.
     *
     * @return true, if is update by primary key statement enabled
     */
    public boolean isUpdateByPrimaryKeyStatementEnabled() {
        return updateByPrimaryKeyStatementEnabled;
    }

    /**
     * Sets the update by primary key statement enabled.
     *
     * @param updateByPrimaryKeyStatementEnabled
     *            the new update by primary key statement enabled
     */
    public void setUpdateByPrimaryKeyStatementEnabled(
            boolean updateByPrimaryKeyStatementEnabled) {
        this.updateByPrimaryKeyStatementEnabled = updateByPrimaryKeyStatementEnabled;
    }

    /**
     * Checks if is column ignored.
     *
     * @param columnName
     *            the column name
     * @return true, if is column ignored
     */
    public boolean isColumnIgnored(String columnName) {
        for (Map.Entry<IgnoredColumn, Boolean> entry : ignoredColumns
                .entrySet()) {
            IgnoredColumn ic = entry.getKey();
            if (ic.isColumnNameDelimited()) {
                if (columnName.equals(ic.getColumnName())) {
                    entry.setValue(Boolean.TRUE);
                    return true;
                }
            } else {
                if (columnName.equalsIgnoreCase(ic.getColumnName())) {
                    entry.setValue(Boolean.TRUE);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Adds the ignored column.
     *
     * @param ignoredColumn
     *            the ignored column
     */
    public void addIgnoredColumn(IgnoredColumn ignoredColumn) {
        ignoredColumns.put(ignoredColumn, Boolean.FALSE);
    }

    /**
     * Adds the column override.
     *
     * @param columnOverride
     *            the column override
     */
    public void addColumnOverride(ColumnOverride columnOverride) {
        columnOverrides.add(columnOverride);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof TableConfiguration)) {
            return false;
        }

        TableConfiguration other = (TableConfiguration) obj;

        return areEqual(this.catalog, other.catalog)
                && areEqual(this.schema, other.schema)
                && areEqual(this.tableName, other.tableName);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = SEED;
        result = hash(result, catalog);
        result = hash(result, schema);
        result = hash(result, tableName);

        return result;
    }

    /**
     * Checks if is select by example statement enabled.
     *
     * @return true, if is select by example statement enabled
     */
    public boolean isSelectByExampleStatementEnabled() {
        return selectByExampleStatementEnabled;
    }

    /**
     * Sets the select by example statement enabled.
     *
     * @param selectByExampleStatementEnabled
     *            the new select by example statement enabled
     */
    public void setSelectByExampleStatementEnabled(
            boolean selectByExampleStatementEnabled) {
        this.selectByExampleStatementEnabled = selectByExampleStatementEnabled;
    }

    /**
     * May return null if the column has not been overridden.
     *
     * @param columnName
     *            the column name
     * @return the column override (if any) related to this column
     */
    public ColumnOverride getColumnOverride(String columnName) {
        for (ColumnOverride co : columnOverrides) {
            if (co.isColumnNameDelimited()) {
                if (columnName.equals(co.getColumnName())) {
                    return co;
                }
            } else {
                if (columnName.equalsIgnoreCase(co.getColumnName())) {
                    return co;
                }
            }
        }

        return null;
    }

    /**
     * Gets the generated key.
     *
     * @return the generated key
     */
    public GeneratedKey getGeneratedKey() {
        return generatedKey;
    }

    /**
     * Gets the select by example query id.
     *
     * @return the select by example query id
     */
    public String getSelectByExampleQueryId() {
        return selectByExampleQueryId;
    }

    /**
     * Sets the select by example query id.
     *
     * @param selectByExampleQueryId
     *            the new select by example query id
     */
    public void setSelectByExampleQueryId(String selectByExampleQueryId) {
        this.selectByExampleQueryId = selectByExampleQueryId;
    }

    /**
     * Gets the select by primary key query id.
     *
     * @return the select by primary key query id
     */
    public String getSelectByPrimaryKeyQueryId() {
        return selectByPrimaryKeyQueryId;
    }

    /**
     * Sets the select by primary key query id.
     *
     * @param selectByPrimaryKeyQueryId
     *            the new select by primary key query id
     */
    public void setSelectByPrimaryKeyQueryId(String selectByPrimaryKeyQueryId) {
        this.selectByPrimaryKeyQueryId = selectByPrimaryKeyQueryId;
    }

    /**
     * Checks if is delete by example statement enabled.
     *
     * @return true, if is delete by example statement enabled
     */
    public boolean isDeleteByExampleStatementEnabled() {
        return deleteByExampleStatementEnabled;
    }

    /**
     * Sets the delete by example statement enabled.
     *
     * @param deleteByExampleStatementEnabled
     *            the new delete by example statement enabled
     */
    public void setDeleteByExampleStatementEnabled(
            boolean deleteByExampleStatementEnabled) {
        this.deleteByExampleStatementEnabled = deleteByExampleStatementEnabled;
    }

    /**
     * Are any statements enabled.
     *
     * @return true, if successful
     */
    public boolean areAnyStatementsEnabled() {
        return selectByExampleStatementEnabled
                || selectByPrimaryKeyStatementEnabled || insertStatementEnabled
                || updateByPrimaryKeyStatementEnabled
                || deleteByExampleStatementEnabled
                || deleteByPrimaryKeyStatementEnabled
                || countByExampleStatementEnabled
                || updateByExampleStatementEnabled;
    }

    /**
     * Sets the generated key.
     *
     * @param generatedKey
     *            the new generated key
     */
    public void setGeneratedKey(GeneratedKey generatedKey) {
        this.generatedKey = generatedKey;
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias.
     *
     * @param alias
     *            the new alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Gets the catalog.
     *
     * @return the catalog
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * Sets the catalog.
     *
     * @param catalog
     *            the new catalog
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * Gets the domain object name.
     *
     * @return the domain object name
     */
    public String getDomainObjectName() {
        return domainObjectName;
    }

    /**
     * Sets the domain object name.
     *
     * @param domainObjectName
     *            the new domain object name
     */
    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }

    /**
     * Gets the schema.
     *
     * @return the schema
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the schema.
     *
     * @param schema
     *            the new schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the table name.
     *
     * @param tableName
     *            the new table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets the column overrides.
     *
     * @return the column overrides
     */
    public List<ColumnOverride> getColumnOverrides() {
        return columnOverrides;
    }

    /**
     * This method returns a List of Strings. The values are the columns
     * that were specified to be ignored in the table, but do not exist in the
     * table.
     * 
     * @return a List of Strings - the columns that were improperly configured
     *         as ignored columns
     */
    public List<String> getIgnoredColumnsInError() {
        List<String> answer = new ArrayList<String>();

        for (Map.Entry<IgnoredColumn, Boolean> entry : ignoredColumns
                .entrySet()) {
            if (Boolean.FALSE.equals(entry.getValue())) {
                answer.add(entry.getKey().getColumnName());
            }
        }

        return answer;
    }

    /**
     * Gets the model type.
     *
     * @return the model type
     */
    public ModelType getModelType() {
        return modelType;
    }

    /**
     * Sets the configured model type.
     *
     * @param configuredModelType
     *            the new configured model type
     */
    public void setConfiguredModelType(String configuredModelType) {
        this.configuredModelType = configuredModelType;
        this.modelType = ModelType.getModelType(configuredModelType);
    }

    /**
     * Checks if is wildcard escaping enabled.
     *
     * @return true, if is wildcard escaping enabled
     */
    public boolean isWildcardEscapingEnabled() {
        return wildcardEscapingEnabled;
    }

    /**
     * Sets the wildcard escaping enabled.
     *
     * @param wildcardEscapingEnabled
     *            the new wildcard escaping enabled
     */
    public void setWildcardEscapingEnabled(boolean wildcardEscapingEnabled) {
        this.wildcardEscapingEnabled = wildcardEscapingEnabled;
    }

    /**
     * To xml element.
     *
     * @return the xml element
     */
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("table"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("tableName", tableName)); //$NON-NLS-1$

        if (stringHasValue(catalog)) {
            xmlElement.addAttribute(new Attribute("catalog", catalog)); //$NON-NLS-1$
        }

        if (stringHasValue(schema)) {
            xmlElement.addAttribute(new Attribute("schema", schema)); //$NON-NLS-1$
        }

        if (stringHasValue(alias)) {
            xmlElement.addAttribute(new Attribute("alias", alias)); //$NON-NLS-1$
        }

        if (stringHasValue(domainObjectName)) {
            xmlElement.addAttribute(new Attribute(
                    "domainObjectName", domainObjectName)); //$NON-NLS-1$
        }

        if (!insertStatementEnabled) {
            xmlElement.addAttribute(new Attribute("enableInsert", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (!selectByPrimaryKeyStatementEnabled) {
            xmlElement.addAttribute(new Attribute(
                    "enableSelectByPrimaryKey", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (!selectByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute(
                    "enableSelectByExample", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (!updateByPrimaryKeyStatementEnabled) {
            xmlElement.addAttribute(new Attribute(
                    "enableUpdateByPrimaryKey", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (!deleteByPrimaryKeyStatementEnabled) {
            xmlElement.addAttribute(new Attribute(
                    "enableDeleteByPrimaryKey", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (!deleteByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute(
                    "enableDeleteByExample", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (!countByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute(
                    "enableCountByExample", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (!updateByExampleStatementEnabled) {
            xmlElement.addAttribute(new Attribute(
                    "enableUpdateByExample", "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (stringHasValue(selectByPrimaryKeyQueryId)) {
            xmlElement.addAttribute(new Attribute(
                    "selectByPrimaryKeyQueryId", selectByPrimaryKeyQueryId)); //$NON-NLS-1$
        }

        if (stringHasValue(selectByExampleQueryId)) {
            xmlElement.addAttribute(new Attribute(
                    "selectByExampleQueryId", selectByExampleQueryId)); //$NON-NLS-1$
        }

        if (configuredModelType != null) {
            xmlElement.addAttribute(new Attribute(
                    "modelType", configuredModelType)); //$NON-NLS-1$
        }

        if (wildcardEscapingEnabled) {
            xmlElement.addAttribute(new Attribute("escapeWildcards", "true")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (isAllColumnDelimitingEnabled) {
            xmlElement.addAttribute(new Attribute("delimitAllColumns", "true")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (delimitIdentifiers) {
            xmlElement
                    .addAttribute(new Attribute("delimitIdentifiers", "true")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        addPropertyXmlElements(xmlElement);

        if (generatedKey != null) {
            xmlElement.addElement(generatedKey.toXmlElement());
        }

        if (columnRenamingRule != null) {
            xmlElement.addElement(columnRenamingRule.toXmlElement());
        }

        if (ignoredColumns.size() > 0) {
            for (IgnoredColumn ignoredColumn : ignoredColumns.keySet()) {
                xmlElement.addElement(ignoredColumn.toXmlElement());
            }
        }

        if (columnOverrides.size() > 0) {
            for (ColumnOverride columnOverride : columnOverrides) {
                xmlElement.addElement(columnOverride.toXmlElement());
            }
        }

        return xmlElement;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return composeFullyQualifiedTableName(catalog, schema,
                tableName, '.');
    }

    /**
     * Checks if is delimit identifiers.
     *
     * @return true, if is delimit identifiers
     */
    public boolean isDelimitIdentifiers() {
        return delimitIdentifiers;
    }

    /**
     * Sets the delimit identifiers.
     *
     * @param delimitIdentifiers
     *            the new delimit identifiers
     */
    public void setDelimitIdentifiers(boolean delimitIdentifiers) {
        this.delimitIdentifiers = delimitIdentifiers;
    }

    /**
     * Checks if is count by example statement enabled.
     *
     * @return true, if is count by example statement enabled
     */
    public boolean isCountByExampleStatementEnabled() {
        return countByExampleStatementEnabled;
    }

    /**
     * Sets the count by example statement enabled.
     *
     * @param countByExampleStatementEnabled
     *            the new count by example statement enabled
     */
    public void setCountByExampleStatementEnabled(
            boolean countByExampleStatementEnabled) {
        this.countByExampleStatementEnabled = countByExampleStatementEnabled;
    }

    /**
     * Checks if is update by example statement enabled.
     *
     * @return true, if is update by example statement enabled
     */
    public boolean isUpdateByExampleStatementEnabled() {
        return updateByExampleStatementEnabled;
    }

    /**
     * Sets the update by example statement enabled.
     *
     * @param updateByExampleStatementEnabled
     *            the new update by example statement enabled
     */
    public void setUpdateByExampleStatementEnabled(
            boolean updateByExampleStatementEnabled) {
        this.updateByExampleStatementEnabled = updateByExampleStatementEnabled;
    }

    /**
     * Validate.
     *
     * @param errors
     *            the errors
     * @param listPosition
     *            the list position
     */
    public void validate(List<String> errors, int listPosition) {
        if (!stringHasValue(tableName)) {
            errors.add(getString(
                    "ValidationError.6", Integer.toString(listPosition))); //$NON-NLS-1$
        }

        String fqTableName = composeFullyQualifiedTableName(
                catalog, schema, tableName, '.');

        if (generatedKey != null) {
            generatedKey.validate(errors, fqTableName);
        }

        // when using column indexes, either both or neither query ids
        // should be set
        if (isTrue(getProperty(PropertyRegistry.TABLE_USE_COLUMN_INDEXES))
                && selectByExampleStatementEnabled
                && selectByPrimaryKeyStatementEnabled) {
            boolean queryId1Set = stringHasValue(selectByExampleQueryId);
            boolean queryId2Set = stringHasValue(selectByPrimaryKeyQueryId);

            if (queryId1Set != queryId2Set) {
                errors.add(getString("ValidationError.13", //$NON-NLS-1$
                        fqTableName));
            }
        }

        if (columnRenamingRule != null) {
            columnRenamingRule.validate(errors, fqTableName);
        }

        for (ColumnOverride columnOverride : columnOverrides) {
            columnOverride.validate(errors, fqTableName);
        }

        for (IgnoredColumn ignoredColumn : ignoredColumns.keySet()) {
            ignoredColumn.validate(errors, fqTableName);
        }
    }

    /**
     * Gets the column renaming rule.
     *
     * @return the column renaming rule
     */
    public ColumnRenamingRule getColumnRenamingRule() {
        return columnRenamingRule;
    }

    /**
     * Sets the column renaming rule.
     *
     * @param columnRenamingRule
     *            the new column renaming rule
     */
    public void setColumnRenamingRule(ColumnRenamingRule columnRenamingRule) {
        this.columnRenamingRule = columnRenamingRule;
    }

    /**
     * Checks if is all column delimiting enabled.
     *
     * @return true, if is all column delimiting enabled
     */
    public boolean isAllColumnDelimitingEnabled() {
        return isAllColumnDelimitingEnabled;
    }

    /**
     * Sets the all column delimiting enabled.
     *
     * @param isAllColumnDelimitingEnabled
     *            the new all column delimiting enabled
     */
    public void setAllColumnDelimitingEnabled(
            boolean isAllColumnDelimitingEnabled) {
        this.isAllColumnDelimitingEnabled = isAllColumnDelimitingEnabled;
    }
}
