/**
 *    Copyright 2006-2018 the original author or authors.
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
import static org.mybatis.generator.internal.util.HashCodeUtil.SEED;
import static org.mybatis.generator.internal.util.HashCodeUtil.hash;
import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableConfiguration extends PropertyHolder {

    private boolean insertStatementEnabled;

    private boolean selectByPrimaryKeyStatementEnabled;

    private boolean selectByExampleStatementEnabled;

    private boolean updateByPrimaryKeyStatementEnabled;

    private boolean deleteByPrimaryKeyStatementEnabled;

    private boolean deleteByExampleStatementEnabled;

    private boolean countByExampleStatementEnabled;

    private boolean updateByExampleStatementEnabled;

    private List<ColumnOverride> columnOverrides;

    private Map<IgnoredColumn, Boolean> ignoredColumns;

    private GeneratedKey generatedKey;

    private String selectByPrimaryKeyQueryId;

    private String selectByExampleQueryId;

    private String catalog;

    private String schema;

    private String tableName;

    private String domainObjectName;

    private String alias;

    private ModelType modelType;

    private boolean wildcardEscapingEnabled;

    private boolean delimitIdentifiers;

    private DomainObjectRenamingRule domainObjectRenamingRule;

    private ColumnRenamingRule columnRenamingRule;

    private boolean isAllColumnDelimitingEnabled;

    private String mapperName;
    private String sqlProviderName;

    private List<IgnoredColumnPattern> ignoredColumnPatterns = new ArrayList<>();

    public TableConfiguration(Context context) {
        super();

        this.modelType = context.getDefaultModelType();

        columnOverrides = new ArrayList<>();
        ignoredColumns = new HashMap<>();

        insertStatementEnabled = true;
        selectByPrimaryKeyStatementEnabled = true;
        selectByExampleStatementEnabled = true;
        updateByPrimaryKeyStatementEnabled = true;
        deleteByPrimaryKeyStatementEnabled = true;
        deleteByExampleStatementEnabled = true;
        countByExampleStatementEnabled = true;
        updateByExampleStatementEnabled = true;
    }

    public boolean isDeleteByPrimaryKeyStatementEnabled() {
        return deleteByPrimaryKeyStatementEnabled;
    }

    public void setDeleteByPrimaryKeyStatementEnabled(
            boolean deleteByPrimaryKeyStatementEnabled) {
        this.deleteByPrimaryKeyStatementEnabled = deleteByPrimaryKeyStatementEnabled;
    }

    public boolean isInsertStatementEnabled() {
        return insertStatementEnabled;
    }

    public void setInsertStatementEnabled(boolean insertStatementEnabled) {
        this.insertStatementEnabled = insertStatementEnabled;
    }

    public boolean isSelectByPrimaryKeyStatementEnabled() {
        return selectByPrimaryKeyStatementEnabled;
    }

    public void setSelectByPrimaryKeyStatementEnabled(
            boolean selectByPrimaryKeyStatementEnabled) {
        this.selectByPrimaryKeyStatementEnabled = selectByPrimaryKeyStatementEnabled;
    }

    public boolean isUpdateByPrimaryKeyStatementEnabled() {
        return updateByPrimaryKeyStatementEnabled;
    }

    public void setUpdateByPrimaryKeyStatementEnabled(
            boolean updateByPrimaryKeyStatementEnabled) {
        this.updateByPrimaryKeyStatementEnabled = updateByPrimaryKeyStatementEnabled;
    }

    public boolean isColumnIgnored(String columnName) {
        for (Map.Entry<IgnoredColumn, Boolean> entry : ignoredColumns
                .entrySet()) {
            if (entry.getKey().matches(columnName)) {
                entry.setValue(Boolean.TRUE);
                return true;
            }
        }

        for (IgnoredColumnPattern ignoredColumnPattern : ignoredColumnPatterns) {
            if (ignoredColumnPattern.matches(columnName)) {
                return true;
            }
        }

        return false;
    }

    public void addIgnoredColumn(IgnoredColumn ignoredColumn) {
        ignoredColumns.put(ignoredColumn, Boolean.FALSE);
    }

    public void addIgnoredColumnPattern(IgnoredColumnPattern ignoredColumnPattern) {
        ignoredColumnPatterns.add(ignoredColumnPattern);
    }

    public void addColumnOverride(ColumnOverride columnOverride) {
        columnOverrides.add(columnOverride);
    }

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

    @Override
    public int hashCode() {
        int result = SEED;
        result = hash(result, catalog);
        result = hash(result, schema);
        result = hash(result, tableName);

        return result;
    }

    public boolean isSelectByExampleStatementEnabled() {
        return selectByExampleStatementEnabled;
    }

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

    public GeneratedKey getGeneratedKey() {
        return generatedKey;
    }

    public String getSelectByExampleQueryId() {
        return selectByExampleQueryId;
    }

    public void setSelectByExampleQueryId(String selectByExampleQueryId) {
        this.selectByExampleQueryId = selectByExampleQueryId;
    }

    public String getSelectByPrimaryKeyQueryId() {
        return selectByPrimaryKeyQueryId;
    }

    public void setSelectByPrimaryKeyQueryId(String selectByPrimaryKeyQueryId) {
        this.selectByPrimaryKeyQueryId = selectByPrimaryKeyQueryId;
    }

    public boolean isDeleteByExampleStatementEnabled() {
        return deleteByExampleStatementEnabled;
    }

    public void setDeleteByExampleStatementEnabled(
            boolean deleteByExampleStatementEnabled) {
        this.deleteByExampleStatementEnabled = deleteByExampleStatementEnabled;
    }

    public boolean areAnyStatementsEnabled() {
        return selectByExampleStatementEnabled
                || selectByPrimaryKeyStatementEnabled || insertStatementEnabled
                || updateByPrimaryKeyStatementEnabled
                || deleteByExampleStatementEnabled
                || deleteByPrimaryKeyStatementEnabled
                || countByExampleStatementEnabled
                || updateByExampleStatementEnabled;
    }

    public void setGeneratedKey(GeneratedKey generatedKey) {
        this.generatedKey = generatedKey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDomainObjectName() {
        return domainObjectName;
    }

    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnOverride> getColumnOverrides() {
        return columnOverrides;
    }

    /**
     * Returns a List of Strings. The values are the columns
     * that were specified to be ignored in the table, but do not exist in the
     * table.
     * 
     * @return a List of Strings - the columns that were improperly configured
     *         as ignored columns
     */
    public List<String> getIgnoredColumnsInError() {
        List<String> answer = new ArrayList<>();

        for (Map.Entry<IgnoredColumn, Boolean> entry : ignoredColumns
                .entrySet()) {
            if (Boolean.FALSE.equals(entry.getValue())) {
                answer.add(entry.getKey().getColumnName());
            }
        }

        return answer;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setConfiguredModelType(String configuredModelType) {
        this.modelType = ModelType.getModelType(configuredModelType);
    }

    public boolean isWildcardEscapingEnabled() {
        return wildcardEscapingEnabled;
    }

    public void setWildcardEscapingEnabled(boolean wildcardEscapingEnabled) {
        this.wildcardEscapingEnabled = wildcardEscapingEnabled;
    }

    @Override
    public String toString() {
        return composeFullyQualifiedTableName(catalog, schema,
                tableName, '.');
    }

    public boolean isDelimitIdentifiers() {
        return delimitIdentifiers;
    }

    public void setDelimitIdentifiers(boolean delimitIdentifiers) {
        this.delimitIdentifiers = delimitIdentifiers;
    }

    public boolean isCountByExampleStatementEnabled() {
        return countByExampleStatementEnabled;
    }

    public void setCountByExampleStatementEnabled(
            boolean countByExampleStatementEnabled) {
        this.countByExampleStatementEnabled = countByExampleStatementEnabled;
    }

    public boolean isUpdateByExampleStatementEnabled() {
        return updateByExampleStatementEnabled;
    }

    public void setUpdateByExampleStatementEnabled(
            boolean updateByExampleStatementEnabled) {
        this.updateByExampleStatementEnabled = updateByExampleStatementEnabled;
    }

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

        if (domainObjectRenamingRule != null) {
            domainObjectRenamingRule.validate(errors, fqTableName);
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

        for (IgnoredColumnPattern ignoredColumnPattern : ignoredColumnPatterns) {
            ignoredColumnPattern.validate(errors, fqTableName);
        }
    }

    public DomainObjectRenamingRule getDomainObjectRenamingRule() {
        return domainObjectRenamingRule;
    }

    public void setDomainObjectRenamingRule(DomainObjectRenamingRule domainObjectRenamingRule) {
        this.domainObjectRenamingRule = domainObjectRenamingRule;
    }

    public ColumnRenamingRule getColumnRenamingRule() {
        return columnRenamingRule;
    }

    public void setColumnRenamingRule(ColumnRenamingRule columnRenamingRule) {
        this.columnRenamingRule = columnRenamingRule;
    }

    public boolean isAllColumnDelimitingEnabled() {
        return isAllColumnDelimitingEnabled;
    }

    public void setAllColumnDelimitingEnabled(
            boolean isAllColumnDelimitingEnabled) {
        this.isAllColumnDelimitingEnabled = isAllColumnDelimitingEnabled;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getSqlProviderName() {
        return sqlProviderName;
    }

    public void setSqlProviderName(String sqlProviderName) {
        this.sqlProviderName = sqlProviderName;
    }
}
