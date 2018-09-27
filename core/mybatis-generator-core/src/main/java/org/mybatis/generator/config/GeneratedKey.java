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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import org.mybatis.generator.internal.db.DatabaseDialects;

/**
 * This class specifies that a key is auto-generated, either as an identity
 * column (post insert), or as some other query like a sequences (pre insert).
 * 
 * @author Jeff Butler
 */
public class GeneratedKey {

    private String column;

    private String runtimeSqlStatement;

    private boolean isIdentity;

    private String type;

    public GeneratedKey(String column, String configuredSqlStatement,
            boolean isIdentity, String type) {
        super();
        this.column = column;
        this.type = type;
        this.isIdentity = isIdentity;

        DatabaseDialects dialect = DatabaseDialects
                .getDatabaseDialect(configuredSqlStatement);
        if (dialect == null) {
            this.runtimeSqlStatement = configuredSqlStatement;
        } else {
            this.runtimeSqlStatement = dialect.getIdentityRetrievalStatement();
        }
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

    public String getType() {
        return type;
    }

    public String getMyBatis3Order() {
        return isIdentity ? "AFTER" : "BEFORE"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(runtimeSqlStatement)) {
            errors.add(getString("ValidationError.7", //$NON-NLS-1$
                    tableName));
        }

        if (stringHasValue(type)
                && !"pre".equals(type) //$NON-NLS-1$
                && !"post".equals(type)) { //$NON-NLS-1$ //$NON-NLS-2$
            errors.add(getString("ValidationError.15", tableName)); //$NON-NLS-1$
        }

        if ("pre".equals(type) && isIdentity) { //$NON-NLS-1$
            errors.add(getString("ValidationError.23", //$NON-NLS-1$
                    tableName));
        }

        if ("post".equals(type) && !isIdentity) { //$NON-NLS-1$
            errors.add(getString("ValidationError.24", //$NON-NLS-1$
                    tableName));
        }
    }

    public boolean isJdbcStandard() {
        return "JDBC".equals(runtimeSqlStatement); //$NON-NLS-1$
    }
}
