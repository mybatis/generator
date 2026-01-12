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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.internal.util.messages.Messages;

public class TableConfiguration extends PropertyHolder {
    private final boolean insertStatementEnabled;
    private final boolean selectByPrimaryKeyStatementEnabled;
    private final boolean selectByExampleStatementEnabled;
    private final boolean updateByPrimaryKeyStatementEnabled;
    private final boolean deleteByPrimaryKeyStatementEnabled;
    private final boolean deleteByExampleStatementEnabled;
    private final boolean countByExampleStatementEnabled;
    private final boolean updateByExampleStatementEnabled;
    private final List<ColumnOverride> columnOverrides;
    // this is a Map for validation purposes. Initially, all items will be FALSE. When accessed, an item will
    // be made TRUE. This allows us to generate warning for columns configured to be ignored but not found.
    private final Map<IgnoredColumn, Boolean> ignoredColumns;
    private final @Nullable GeneratedKey generatedKey;
    private final @Nullable String selectByPrimaryKeyQueryId;
    private final @Nullable String selectByExampleQueryId;
    private final @Nullable String catalog;
    private final @Nullable String schema;
    private final String tableName;
    private final @Nullable String domainObjectName;
    private final @Nullable String alias;
    private final ModelType modelType;
    private final boolean wildcardEscapingEnabled;
    private final boolean delimitIdentifiers;
    private final @Nullable DomainObjectRenamingRule domainObjectRenamingRule;
    private final @Nullable ColumnRenamingRule columnRenamingRule;
    private final boolean isAllColumnDelimitingEnabled;
    private final @Nullable String mapperName;
    private final @Nullable String sqlProviderName;
    private final List<IgnoredColumnPattern> ignoredColumnPatterns;

    protected TableConfiguration(Builder builder) {
        super(builder);

        catalog = builder.catalog;
        schema = builder.schema;
        tableName = Objects.requireNonNull(builder.tableName);
        domainObjectName = builder.domainObjectName;
        alias = builder.alias;
        modelType = Objects.requireNonNull(builder.modelType);
        insertStatementEnabled = builder.insertStatementEnabled;
        selectByPrimaryKeyStatementEnabled = builder.selectByPrimaryKeyStatementEnabled;
        selectByExampleStatementEnabled = builder.selectByExampleStatementEnabled;
        updateByPrimaryKeyStatementEnabled = builder.updateByPrimaryKeyStatementEnabled;
        deleteByPrimaryKeyStatementEnabled = builder.deleteByPrimaryKeyStatementEnabled;
        deleteByExampleStatementEnabled = builder.deleteByExampleStatementEnabled;
        countByExampleStatementEnabled = builder.countByExampleStatementEnabled;
        updateByExampleStatementEnabled = builder.updateByExampleStatementEnabled;
        wildcardEscapingEnabled = builder.wildcardEscapingEnabled;
        delimitIdentifiers = builder.delimitIdentifiers;
        isAllColumnDelimitingEnabled = builder.isAllColumnDelimitingEnabled;
        selectByPrimaryKeyQueryId = builder.selectByPrimaryKeyQueryId;
        selectByExampleQueryId = builder.selectByExampleQueryId;
        mapperName = builder.mapperName;
        sqlProviderName = builder.sqlProviderName;
        columnOverrides = Collections.unmodifiableList(builder.columnOverrides);
        ignoredColumns = builder.ignoredColumns;
        generatedKey = builder.generatedKey;
        domainObjectRenamingRule = builder.domainObjectRenamingRule;
        columnRenamingRule = builder.columnRenamingRule;
        ignoredColumnPatterns = Collections.unmodifiableList(builder.ignoredColumnPatterns);
    }

    public boolean isDeleteByPrimaryKeyStatementEnabled() {
        return deleteByPrimaryKeyStatementEnabled;
    }

    public boolean isInsertStatementEnabled() {
        return insertStatementEnabled;
    }

    public boolean isSelectByPrimaryKeyStatementEnabled() {
        return selectByPrimaryKeyStatementEnabled;
    }

    public boolean isUpdateByPrimaryKeyStatementEnabled() {
        return updateByPrimaryKeyStatementEnabled;
    }

    public boolean isColumnIgnored(String columnName) {
        for (Map.Entry<IgnoredColumn, Boolean> entry : ignoredColumns.entrySet()) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof TableConfiguration other)) {
            return false;
        }

        return Objects.equals(this.catalog, other.catalog)
                && Objects.equals(this.schema, other.schema)
                && Objects.equals(this.tableName, other.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalog, schema, tableName);
    }

    public boolean isSelectByExampleStatementEnabled() {
        return selectByExampleStatementEnabled;
    }

    /**
     * May return null if the column has not been overridden.
     *
     * @param columnName
     *            the column name
     * @return the column override (if any) related to this column
     */
    public Optional<ColumnOverride> getColumnOverride(String columnName) {
        for (ColumnOverride co : columnOverrides) {
            if (co.isColumnNameDelimited()) {
                if (columnName.equals(co.getColumnName())) {
                    return Optional.of(co);
                }
            } else {
                if (columnName.equalsIgnoreCase(co.getColumnName())) {
                    return Optional.of(co);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<GeneratedKey> getGeneratedKey() {
        return Optional.ofNullable(generatedKey);
    }

    public Optional<String> getSelectByExampleQueryId() {
        return Optional.ofNullable(selectByExampleQueryId);
    }

    public Optional<String> getSelectByPrimaryKeyQueryId() {
        return Optional.ofNullable(selectByPrimaryKeyQueryId);
    }

    public boolean isDeleteByExampleStatementEnabled() {
        return deleteByExampleStatementEnabled;
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

    public @Nullable String getAlias() {
        return alias;
    }

    public @Nullable String getCatalog() {
        return catalog;
    }

    public @Nullable String getDomainObjectName() {
        return domainObjectName;
    }

    public @Nullable String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
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

        for (Map.Entry<IgnoredColumn, Boolean> entry : ignoredColumns.entrySet()) {
            if (!entry.getValue()) {
                answer.add(entry.getKey().getColumnName());
            }
        }

        return answer;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public boolean isWildcardEscapingEnabled() {
        return wildcardEscapingEnabled;
    }

    @Override
    public String toString() {
        return composeFullyQualifiedTableName(catalog, schema, tableName, '.');
    }

    public boolean isDelimitIdentifiers() {
        return delimitIdentifiers;
    }

    public boolean isCountByExampleStatementEnabled() {
        return countByExampleStatementEnabled;
    }

    public boolean isUpdateByExampleStatementEnabled() {
        return updateByExampleStatementEnabled;
    }

    public void validate(List<String> errors, int listPosition) {
        if (!stringHasValue(tableName)) {
            errors.add(Messages.getString(
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
                errors.add(Messages.getString("ValidationError.13", //$NON-NLS-1$
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

    public @Nullable DomainObjectRenamingRule getDomainObjectRenamingRule() {
        return domainObjectRenamingRule;
    }

    public Optional<ColumnRenamingRule> getColumnRenamingRule() {
        return Optional.ofNullable(columnRenamingRule);
    }

    public boolean isAllColumnDelimitingEnabled() {
        return isAllColumnDelimitingEnabled;
    }

    public @Nullable String getMapperName() {
        return mapperName;
    }

    public @Nullable String getSqlProviderName() {
        return sqlProviderName;
    }

    public @Nullable String getDynamicSqlSupportClassName() {
        return getProperty(PropertyRegistry.TABLE_DYNAMIC_SQL_SUPPORT_CLASS_NAME);
    }

    public @Nullable String getDynamicSqlTableObjectName() {
        return getProperty(PropertyRegistry.TABLE_DYNAMIC_SQL_TABLE_OBJECT_NAME);
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private @Nullable ModelType modelType;
        private @Nullable String catalog;
        private @Nullable String schema;
        private @Nullable String tableName;
        private @Nullable String domainObjectName;
        private @Nullable String alias;
        private boolean insertStatementEnabled = true;
        private boolean selectByPrimaryKeyStatementEnabled = true;
        private boolean selectByExampleStatementEnabled = true;
        private boolean updateByPrimaryKeyStatementEnabled = true;
        private boolean deleteByPrimaryKeyStatementEnabled = true;
        private boolean deleteByExampleStatementEnabled = true;
        private boolean countByExampleStatementEnabled = true;
        private boolean updateByExampleStatementEnabled = true;
        private boolean wildcardEscapingEnabled;
        private boolean delimitIdentifiers;
        private boolean isAllColumnDelimitingEnabled;
        private @Nullable String selectByPrimaryKeyQueryId;
        private @Nullable String selectByExampleQueryId;
        private @Nullable String mapperName;
        private @Nullable String sqlProviderName;
        private @Nullable GeneratedKey generatedKey;
        private @Nullable DomainObjectRenamingRule domainObjectRenamingRule;
        private @Nullable ColumnRenamingRule columnRenamingRule;
        private final List<IgnoredColumnPattern> ignoredColumnPatterns = new ArrayList<>();
        private final List<ColumnOverride> columnOverrides = new ArrayList<>();
        private final Map<IgnoredColumn, Boolean> ignoredColumns = new HashMap<>();

        public TableConfiguration build() {
            return new TableConfiguration(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder withModelType(ModelType defaultModelType, @Nullable String tableModelType) {
            this.modelType = tableModelType == null ? defaultModelType : ModelType.getModelType(tableModelType);
            return getThis();
        }

        public Builder withCatalog(@Nullable String catalog) {
            this.catalog = catalog;
            return this;
        }

        public Builder withSchema(@Nullable String schema) {
            this.schema = schema;
            return this;
        }

        public Builder withTableName(@Nullable String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder withDomainObjectName(@Nullable String domainObjectName) {
            this.domainObjectName = domainObjectName;
            return this;
        }

        public Builder withAlias(@Nullable String alias) {
            this.alias = alias;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withInsertStatementEnabled(boolean insertStatementEnabled) {
            this.insertStatementEnabled = insertStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withSelectByPrimaryKeyStatementEnabled(boolean selectByPrimaryKeyStatementEnabled) {
            this.selectByPrimaryKeyStatementEnabled = selectByPrimaryKeyStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withSelectByExampleStatementEnabled(boolean selectByExampleStatementEnabled) {
            this.selectByExampleStatementEnabled = selectByExampleStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withUpdateByPrimaryKeyStatementEnabled(boolean updateByPrimaryKeyStatementEnabled) {
            this.updateByPrimaryKeyStatementEnabled = updateByPrimaryKeyStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withDeleteByPrimaryKeyStatementEnabled(boolean deleteByPrimaryKeyStatementEnabled) {
            this.deleteByPrimaryKeyStatementEnabled = deleteByPrimaryKeyStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withDeleteByExampleStatementEnabled(boolean deleteByExampleStatementEnabled) {
            this.deleteByExampleStatementEnabled = deleteByExampleStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withCountByExampleStatementEnabled(boolean countByExampleStatementEnabled) {
            this.countByExampleStatementEnabled = countByExampleStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withUpdateByExampleStatementEnabled(boolean updateByExampleStatementEnabled) {
            this.updateByExampleStatementEnabled = updateByExampleStatementEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withWildcardEscapingEnabled(boolean wildcardEscapingEnabled) {
            this.wildcardEscapingEnabled = wildcardEscapingEnabled;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withDelimitIdentifiers(boolean delimitIdentifiers) {
            this.delimitIdentifiers = delimitIdentifiers;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withAllColumnDelimitingEnabled(boolean isAllColumnDelimitingEnabled) {
            this.isAllColumnDelimitingEnabled = isAllColumnDelimitingEnabled;
            return this;
        }

        public Builder withSelectByPrimaryKeyQueryId(@Nullable String selectByPrimaryKeyQueryId) {
            this.selectByPrimaryKeyQueryId = selectByPrimaryKeyQueryId;
            return this;
        }

        public Builder withSelectByExampleQueryId(@Nullable String selectByExampleQueryId) {
            this.selectByExampleQueryId = selectByExampleQueryId;
            return this;
        }

        public Builder withMapperName(@Nullable String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        public Builder withSqlProviderName(@Nullable String sqlProviderName) {
            this.sqlProviderName = sqlProviderName;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withGeneratedKey(@Nullable GeneratedKey generatedKey) {
            this.generatedKey = generatedKey;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withDomainObjectRenamingRule(@Nullable DomainObjectRenamingRule domainObjectRenamingRule) {
            this.domainObjectRenamingRule = domainObjectRenamingRule;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withColumnRenamingRule(@Nullable ColumnRenamingRule columnRenamingRule) {
            this.columnRenamingRule = columnRenamingRule;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withIgnoredColumnPattern(IgnoredColumnPattern ignoredColumnPattern) {
            this.ignoredColumnPatterns.add(ignoredColumnPattern);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withIgnoredColumn(IgnoredColumn ignoredColumn) {
            this.ignoredColumns.put(ignoredColumn, Boolean.FALSE);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withColumnOverride(ColumnOverride columnOverride) {
            this.columnOverrides.add(columnOverride);
            return this;
        }
    }
}
