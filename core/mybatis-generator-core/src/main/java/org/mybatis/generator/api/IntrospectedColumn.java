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

import java.sql.Types;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * This class holds information about an introspected column.
 *
 * @author Jeff Butler
 */
public class IntrospectedColumn {
    /**
     * Only nullable because instances of this class are built through introspection. Not null in practice.
     */
    protected @Nullable String actualColumnName;

    protected int jdbcType;

    /**
     * The platform specific data type name as reported from DatabaseMetadata.getColumns().
     *
     * <p>Only nullable because instances of this class are built through introspection. Not null in practice.
     */
    protected @Nullable String actualTypeName;

    protected @Nullable String jdbcTypeName;

    protected boolean nullable;

    protected int length;

    protected int scale;

    protected boolean identity;

    protected boolean isSequenceColumn;

    /**
     * Only nullable because instances of this class are built through introspection. Not null in practice.
     */
    protected @Nullable String javaProperty;

    /**
     * Only nullable because instances of this class are built through introspection. Not null in practice.
     */
    protected @Nullable FullyQualifiedJavaType fullyQualifiedJavaType;

    protected @Nullable String tableAlias;

    protected @Nullable String typeHandler;

    /**
     * Only nullable because instances of this class are built through introspection. Not null in practice.
     */
    protected @Nullable Context context;

    protected boolean isColumnNameDelimited;

    /**
     * Only nullable because instances of this class are built through introspection. Not null in practice.
     */
    protected @Nullable IntrospectedTable introspectedTable;

    protected final Properties properties;

    // any database comment associated with this column. May be null
    protected @Nullable String remarks;

    protected @Nullable String defaultValue;

    /**
     * true if the JDBC driver reports that this column is auto-increment.
     */
    protected boolean isAutoIncrement;

    /**
     * true if the JDBC driver reports that this column is generated.
     */
    protected boolean isGeneratedColumn;

    /**
     * True if there is a column override that defines this column as GENERATED ALWAYS.
     */
    protected boolean isGeneratedAlways;

    /**
     * Constructs a Column definition. This object holds all the information
     * about a column that is required to generate Java objects and SQL maps;
     */
    public IntrospectedColumn() {
        properties = new Properties();
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    /*
     * This method is primarily used for debugging, so we don't externalize the
     * strings
     */
    @Override
    public String toString() {
        return "Actual Column Name: " //$NON-NLS-1$
                + getActualColumnName()
                + ", JDBC Type: " //$NON-NLS-1$
                + jdbcType
                + ", Nullable: " //$NON-NLS-1$
                + nullable
                + ", Length: " //$NON-NLS-1$
                + length
                + ", Scale: " //$NON-NLS-1$
                + scale
                + ", Identity: " //$NON-NLS-1$
                + identity;
    }

    public void setActualColumnName(String actualColumnName) {
        this.actualColumnName = actualColumnName;
        isColumnNameDelimited = StringUtility.stringContainsSpace(actualColumnName);
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isBLOBColumn() {
        String typeName = getJdbcTypeName();

        return "BINARY".equals(typeName) || "BLOB".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "CLOB".equals(typeName) || "LONGNVARCHAR".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "LONGVARBINARY".equals(typeName) || "LONGVARCHAR".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "NCLOB".equals(typeName) || "VARBINARY".equals(typeName); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public boolean isStringColumn() {
        return getFullyQualifiedJavaType().equals(FullyQualifiedJavaType.getStringInstance());
    }

    public boolean isJdbcCharacterColumn() {
        return jdbcType == Types.CHAR || jdbcType == Types.CLOB
                || jdbcType == Types.LONGVARCHAR || jdbcType == Types.VARCHAR
                || jdbcType == Types.LONGNVARCHAR || jdbcType == Types.NCHAR
                || jdbcType == Types.NCLOB || jdbcType == Types.NVARCHAR;
    }

    public String getJavaProperty() {
        return getJavaProperty(null);
    }

    public String getJavaProperty(@Nullable String prefix) {
        String baseProperty = Objects.requireNonNull(javaProperty);
        if (prefix == null) {
            return baseProperty;
        }

        return prefix + baseProperty;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public boolean isJDBCDateColumn() {
        return getFullyQualifiedJavaType().equals(FullyQualifiedJavaType.getDateInstance())
                && "DATE".equalsIgnoreCase(jdbcTypeName); //$NON-NLS-1$
    }

    public boolean isJDBCTimeColumn() {
        return getFullyQualifiedJavaType().equals(FullyQualifiedJavaType.getDateInstance())
                && "TIME".equalsIgnoreCase(jdbcTypeName); //$NON-NLS-1$
    }

    public Optional<String> getTypeHandler() {
        return Optional.ofNullable(typeHandler);
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public String getActualColumnName() {
        return Objects.requireNonNull(actualColumnName);
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public String getJdbcTypeName() {
        return Objects.requireNonNullElse(jdbcTypeName, "OTHER");//$NON-NLS-1$
    }

    public void setJdbcTypeName(@Nullable String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public FullyQualifiedJavaType getFullyQualifiedJavaType() {
        return Objects.requireNonNull(fullyQualifiedJavaType);
    }

    public void setFullyQualifiedJavaType(FullyQualifiedJavaType fullyQualifiedJavaType) {
        this.fullyQualifiedJavaType = fullyQualifiedJavaType;
    }

    public Optional<String> getTableAlias() {
        return Optional.ofNullable(tableAlias);
    }

    public void setTableAlias(@Nullable String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public Context getContext() {
        return Objects.requireNonNull(context);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public IntrospectedTable getIntrospectedTable() {
        return Objects.requireNonNull(introspectedTable);
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    public Optional<String> getRemarks() {
        return Optional.ofNullable(remarks);
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    public void setDefaultValue(@Nullable String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isSequenceColumn() {
        return isSequenceColumn;
    }

    public void setSequenceColumn(boolean isSequenceColumn) {
        this.isSequenceColumn = isSequenceColumn;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public boolean isGeneratedColumn() {
        return isGeneratedColumn;
    }

    public void setGeneratedColumn(boolean isGeneratedColumn) {
        this.isGeneratedColumn = isGeneratedColumn;
    }

    public boolean isGeneratedAlways() {
        return isGeneratedAlways;
    }

    public void setGeneratedAlways(boolean isGeneratedAlways) {
        this.isGeneratedAlways = isGeneratedAlways;
    }

    /**
     * The platform specific type name as reported by the JDBC driver. This value is determined
     * from the DatabaseMetadata.getColumns() call - specifically ResultSet.getString("TYPE_NAME").
     * This value is platform dependent.
     *
     * @return the platform specific type name as reported by the JDBC driver
     */
    public String getActualTypeName() {
        return Objects.requireNonNull(actualTypeName);
    }

    public void setActualTypeName(String actualTypeName) {
        this.actualTypeName = actualTypeName;
    }
}
