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

import org.jspecify.annotations.Nullable;

public class IgnoredColumn {
    protected final String columnName;
    private final boolean isColumnNameDelimited;

    public IgnoredColumn(@Nullable String columnName, boolean isColumnNameDelimited) {
        this.columnName = Objects.requireNonNull(columnName);
        this.isColumnNameDelimited = isColumnNameDelimited || stringContainsSpace(columnName);
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IgnoredColumn)) {
            return false;
        }

        return columnName.equals(((IgnoredColumn) obj).getColumnName());
    }

    @Override
    public int hashCode() {
        return columnName.hashCode();
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.21", tableName)); //$NON-NLS-1$
        }
    }

    public boolean matches(String columnName) {
        if (isColumnNameDelimited) {
            return this.columnName.equals(columnName);
        } else {
            return this.columnName.equalsIgnoreCase(columnName);
        }
    }
}
