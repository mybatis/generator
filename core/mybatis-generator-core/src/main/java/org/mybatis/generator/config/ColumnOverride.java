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

import static org.mybatis.generator.internal.util.StringUtility.stringContainsSpace;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

public class ColumnOverride extends PropertyHolder {

    private final String columnName;

    private @Nullable String javaProperty;

    private @Nullable String jdbcType;

    private @Nullable String javaType;

    private @Nullable String typeHandler;

    private boolean isColumnNameDelimited;

    /**
     * If true, the column is a GENERATED ALWAYS column which means
     * that it should not be used in insert or update statements.
     */
    private boolean isGeneratedAlways;

    public ColumnOverride(String columnName) {
        this.columnName = columnName;
        isColumnNameDelimited = stringContainsSpace(columnName);
    }

    public String getColumnName() {
        return columnName;
    }

    public Optional<String> getJavaProperty() {
        return Optional.ofNullable(javaProperty);
    }

    public void setJavaProperty(@Nullable String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public Optional<String> getJavaType() {
        return Optional.ofNullable(javaType);
    }

    public void setJavaType(@Nullable String javaType) {
        this.javaType = javaType;
    }

    public Optional<String> getJdbcType() {
        return Optional.ofNullable(jdbcType);
    }

    public void setJdbcType(@Nullable String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public Optional<String> getTypeHandler() {
        return Optional.ofNullable( typeHandler);
    }

    public void setTypeHandler(@Nullable String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.22", //$NON-NLS-1$
                    tableName));
        }
    }

    public boolean isGeneratedAlways() {
        return isGeneratedAlways;
    }

    public void setGeneratedAlways(boolean isGeneratedAlways) {
        this.isGeneratedAlways = isGeneratedAlways;
    }
}
