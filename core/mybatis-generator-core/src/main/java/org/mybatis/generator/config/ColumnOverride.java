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
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

public class ColumnOverride extends PropertyHolder {
    private final String columnName;
    private final @Nullable String javaProperty;
    private final @Nullable String jdbcType;
    private final @Nullable String javaType;
    private final @Nullable String typeHandler;
    private final boolean isColumnNameDelimited;

    /**
     * If true, the column is a GENERATED ALWAYS column which means
     * that it should not be used in insert or update statements.
     */
    private final boolean isGeneratedAlways;

    protected ColumnOverride(Builder builder) {
        columnName = Objects.requireNonNull(builder.columnName);
        javaProperty = builder.javaProperty;
        jdbcType = builder.jdbcType;
        javaType = builder.javaType;
        typeHandler = builder.typeHandler;
        isGeneratedAlways = builder.isGeneratedAlways;
        isColumnNameDelimited = Objects.requireNonNullElseGet(builder.isColumnNameDelimited,
                () -> stringContainsSpace(columnName));
    }

    public String getColumnName() {
        return columnName;
    }

    public Optional<String> getJavaProperty() {
        return Optional.ofNullable(javaProperty);
    }

    public Optional<String> getJavaType() {
        return Optional.ofNullable(javaType);
    }

    public Optional<String> getJdbcType() {
        return Optional.ofNullable(jdbcType);
    }

    public Optional<String> getTypeHandler() {
        return Optional.ofNullable( typeHandler);
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public boolean isGeneratedAlways() {
        return isGeneratedAlways;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.22", tableName)); //$NON-NLS-1$
        }
    }

    public static class Builder {
        private @Nullable String columnName;
        private @Nullable String javaProperty;
        private @Nullable String jdbcType;
        private @Nullable String javaType;
        private @Nullable String typeHandler;
        private @Nullable Boolean isColumnNameDelimited;
        private boolean isGeneratedAlways;

        public Builder withColumnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public Builder withJavaProperty(String javaProperty) {
            this.javaProperty = javaProperty;
            return this;
        }

        public Builder withJdbcType(String jdbcType) {
            this.jdbcType = jdbcType;
            return this;
        }
        public Builder withJavaType(String javaType) {
            this.javaType = javaType;
            return this;
        }

        public Builder withTypeHandler(String typeHandler) {
            this.typeHandler = typeHandler;
            return this;
        }

        public Builder withColumnNameDelimited(@Nullable Boolean isColumnNameDelimited) {
            this.isColumnNameDelimited = isColumnNameDelimited;
            return this;
        }

        public Builder withGeneratedAlways(boolean isGeneratedAlways) {
            this.isGeneratedAlways = isGeneratedAlways;
            return this;
        }

        public ColumnOverride build() {
            return new ColumnOverride(this);
        }
    }
}
