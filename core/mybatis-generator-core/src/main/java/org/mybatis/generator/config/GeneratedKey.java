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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.internal.db.DatabaseDialects;

/**
 * This class specifies that a key is auto-generated, either as an identity
 * column (post insert), or as some other query like a sequence (pre insert).
 *
 * @author Jeff Butler
 */
public class GeneratedKey {

    private final String column;
    private final String runtimeSqlStatement;
    private final boolean isIdentity;

    public GeneratedKey(@Nullable String column, @Nullable String configuredSqlStatement, boolean isIdentity) {
        this.column = Objects.requireNonNull(column);
        this.isIdentity = isIdentity;

        Objects.requireNonNull(configuredSqlStatement);
        this.runtimeSqlStatement = DatabaseDialects.getDatabaseDialect(configuredSqlStatement)
                .map(DatabaseDialects::getIdentityRetrievalStatement)
                .orElse(configuredSqlStatement);
    }

    public String getColumn() {
        return column;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    public String getRuntimeSqlStatement() {
        return runtimeSqlStatement;
    }

    public String getMyBatis3Order() {
        return isIdentity ? "AFTER" : "BEFORE"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void validate(List<String> errors, String tableName, String contextId) {
        if (!stringHasValue(runtimeSqlStatement)) {
            errors.add(getString("ValidationError.7", tableName, contextId)); //$NON-NLS-1$
        }
    }

    public boolean isJdbcStandard() {
        return "JDBC".equals(runtimeSqlStatement); //$NON-NLS-1$
    }
}
