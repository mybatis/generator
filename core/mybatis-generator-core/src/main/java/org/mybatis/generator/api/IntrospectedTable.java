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
package org.mybatis.generator.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.internal.PluginAggregator;
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
 */
public class IntrospectedTable extends CodeGenerationAttributes{
    protected Rules rules;
    protected final List<IntrospectedColumn> primaryKeyColumns = new ArrayList<>();
    protected final List<IntrospectedColumn> baseColumns = new ArrayList<>();
    protected final List<IntrospectedColumn> blobColumns = new ArrayList<>();

    /**
     * Table remarks retrieved from database metadata.
     */
    protected @Nullable String remarks;

    /**
     * Table type retrieved from database metadata.
     *
     * <p>Non-null in practice
     */
    protected @Nullable String tableType;

    protected IntrospectedTable(Builder builder) {
        super(builder);

        switch (getTableConfiguration().getModelType()) {
        case HIERARCHICAL:
            rules = new HierarchicalModelRules(this);
            break;
        case FLAT:
            rules = new FlatModelRules(this);
            break;
        case CONDITIONAL:
            rules = new ConditionalModelRules(this);
            break;
        default:
            throw new IllegalArgumentException("Unknown model type: " + getTableConfiguration().getModelType());
        }

        Objects.requireNonNull(builder.pluginAggregator);
        builder.pluginAggregator.initialized(this);
    }

    public Optional<IntrospectedColumn> getColumn(String columnName) {
        return Stream.of(primaryKeyColumns.stream(), baseColumns.stream(), blobColumns.stream())
                .flatMap(Function.identity())
                .filter(ic -> columnMatches(ic, columnName))
                .findFirst();
    }

    private boolean columnMatches(IntrospectedColumn introspectedColumn, String columnName) {
        if (introspectedColumn.isColumnNameDelimited()) {
            return introspectedColumn.getActualColumnName().equals(columnName);
        } else {
            return introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName);
        }
    }

    /**
     * Returns true if any of the columns in the table are JDBC Dates (as
     * opposed to timestamps).
     *
     * @return true if the table contains DATE columns
     */
    public boolean hasJDBCDateColumns() {
        return Stream.of(primaryKeyColumns.stream(), baseColumns.stream())
                .flatMap(Function.identity())
                .anyMatch(IntrospectedColumn::isJDBCDateColumn);
    }

    /**
     * Returns true if any of the columns in the table are JDBC Times (as
     * opposed to timestamps).
     *
     * @return true if the table contains TIME columns
     */
    public boolean hasJDBCTimeColumns() {
        return Stream.of(primaryKeyColumns.stream(), baseColumns.stream())
                .flatMap(Function.identity())
                .anyMatch(IntrospectedColumn::isJDBCTimeColumn);
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

    public boolean hasPrimaryKeyColumns() {
        return !primaryKeyColumns.isEmpty();
    }

    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }

    /**
     * Returns all columns in the table (for use by the select by primary key and
     * select by example with BLOBs methods).
     *
     * @return a List of ColumnDefinition objects for all columns in the table
     */
    public List<IntrospectedColumn> getAllColumns() {
        return Stream.of(primaryKeyColumns.stream(), baseColumns.stream(), blobColumns.stream())
                .flatMap(Function.identity())
                .toList();
    }

    /**
     * Returns all columns except BLOBs (for use by the select by example without BLOBs method).
     *
     * @return a List of ColumnDefinition objects for columns in the table that are non BLOBs
     */
    public List<IntrospectedColumn> getNonBLOBColumns() {
        return Stream.of(primaryKeyColumns.stream(), baseColumns.stream())
                .flatMap(Function.identity())
                .toList();
    }

    public int getNonBLOBColumnCount() {
        return primaryKeyColumns.size() + baseColumns.size();
    }

    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        return Stream.of(baseColumns.stream(), blobColumns.stream())
                .flatMap(Function.identity())
                .toList();
    }

    public List<IntrospectedColumn> getBLOBColumns() {
        return blobColumns;
    }

    public boolean hasBLOBColumns() {
        return !blobColumns.isEmpty();
    }

    public boolean hasBaseColumns() {
        return !baseColumns.isEmpty();
    }

    public Rules getRules() {
        return Objects.requireNonNull(rules);
    }

    public boolean hasAnyColumns() {
        return hasPrimaryKeyColumns() || hasBaseColumns() || hasBLOBColumns();
    }

    public void addColumn(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            blobColumns.add(introspectedColumn);
        } else {
            baseColumns.add(introspectedColumn);
        }

        introspectedColumn.setIntrospectedTable(this);
    }

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
                    break;
                }
            }
        }
    }

    /**
     * This method exists to give plugins the opportunity to replace the calculated rules if necessary.
     *
     * @param rules
     *            the new rules
     */
    public void setRules(Rules rules) {
        this.rules = rules;
    }


    public Optional<String> getRemarks() {
        return Optional.ofNullable(remarks);
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTableType() {
        return Objects.requireNonNull(tableType);
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private @Nullable PluginAggregator pluginAggregator;

        public Builder withPluginAggregator(PluginAggregator pluginAggregator) {
            this.pluginAggregator = pluginAggregator;
            return this;
        }

        public IntrospectedTable build() {
            return new IntrospectedTable(this);
        }

        protected Builder getThis() {
            return this;
        }
    }
}
