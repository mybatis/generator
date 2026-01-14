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
package org.mybatis.generator.engine;

import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.DatabaseIntrospector;

public class IntrospectionEngine {
    private final Context context;
    private final ProgressCallback progressCallback;
    private final List<String> warnings;
    private final Set<String> fullyQualifiedTableNames;
    private final CommentGenerator commentGenerator;

    protected IntrospectionEngine(Builder builder) {
        context = Objects.requireNonNull(builder.context);
        progressCallback = Objects.requireNonNull(builder.progressCallback);
        warnings = Objects.requireNonNull(builder.warnings);
        fullyQualifiedTableNames = Objects.requireNonNull(builder.fullyQualifiedTableNames);
        commentGenerator = Objects.requireNonNull(builder.commentGenerator);
    }

    /**
     * Introspect tables based on the configuration specified in the
     * constructor. This method is long-running.
     *
     * @return a list containing the results of table introspection. The list will be empty
     *     if this method is called before introspectTables(), or if no tables are found that
     *     match the configuration
     *
     * @throws java.sql.SQLException
     *             if some error arises while introspecting the specified
     *             database tables.
     * @throws InterruptedException
     *             if the progress callback reports a cancel
     */
    public List<IntrospectedTable> introspectTables() throws SQLException, InterruptedException {

        List<IntrospectedTable> introspectedTables = new ArrayList<>();
        JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(context, warnings);

        try (Connection connection = getConnection()) {
            progressCallback.startTask(getString("Progress.0")); //$NON-NLS-1$

            DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
                    context, connection.getMetaData(), javaTypeResolver, warnings, commentGenerator);

            // TODO - awkward toList here
            for (TableConfiguration tc : context.tableConfigurations().toList()) {
                String tableName = composeFullyQualifiedTableName(tc.getCatalog(), tc
                                .getSchema(), tc.getTableName(), '.');

                if (isTableExcluded(tableName)) {
                    continue;
                }

                if (!tc.areAnyStatementsEnabled()) {
                    warnings.add(getString("Warning.0", tableName)); //$NON-NLS-1$
                    continue;
                }

                progressCallback.startTask(getString("Progress.1", tableName)); //$NON-NLS-1$
                List<IntrospectedTable> tables = databaseIntrospector.introspectTables(tc);
                introspectedTables.addAll(tables);

                progressCallback.checkCancel();
            }
        }

        return introspectedTables;
    }

    private boolean isTableExcluded(String tableName) {
        return !isTableIncluded(tableName);
    }

    private boolean isTableIncluded(String tableName) {
        if (fullyQualifiedTableNames.isEmpty()) {
            return true;
        }

        return fullyQualifiedTableNames.contains(tableName);
    }

    /**
     * This method creates a new JDBC connection from the values specified in the configuration file.
     *
     * @return a new connection created from the values in the configuration file
     *
     * @throws SQLException if any error occurs while creating the connection
     */
    private Connection getConnection() throws SQLException {
        // if both configs are null, it is an internal error - we should have caught this with validation

        JDBCConnectionConfiguration jdbcConfig = context.getJDBCConnectionConfiguration();
        if (jdbcConfig != null) {
            return new JDBCConnectionFactory(jdbcConfig).getConnection();
        } else {
            ConnectionFactoryConfiguration config = context.getConnectionFactoryConfiguration();
            if (config == null) {
                throw new RuntimeException("Internal Error - no connection configured in context: "
                        + context.getId());
            }

            return ObjectFactory.createConnectionFactory(config).getConnection();
        }
    }

    public static class Builder {
        private @Nullable Context context;
        private @Nullable ProgressCallback progressCallback;
        private @Nullable List<String> warnings;
        private @Nullable Set<String> fullyQualifiedTableNames;
        private @Nullable CommentGenerator commentGenerator;

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withProgressCallback(ProgressCallback progressCallback) {
            this.progressCallback = progressCallback;
            return this;
        }

        public Builder withWarnings(List<String> warnings) {
            this.warnings = warnings;
            return this;
        }

        /**
         * Sets a Set of table names for introspection.
         *
         * @param fullyQualifiedTableNames
         *            a set of table names to introspect. The elements of the set must
         *            be Strings that exactly match what's specified in the
         *            configuration. For example, if a table name is "foo" and the schema is
         *            "bar", then the fully qualified table name is "foo.bar". If
         *            the Set is empty, then all tables in the configuration
         *            will be used for code generation.
         *
         *
         * @return this builder
         */
        public Builder withFullyQualifiedTableNames(Set<String> fullyQualifiedTableNames) {
            this.fullyQualifiedTableNames = fullyQualifiedTableNames;
            return this;
        }

        public Builder withCommentGenerator(CommentGenerator commentGenerator) {
            this.commentGenerator = commentGenerator;
            return this;
        }

        public IntrospectionEngine build() {
            return new IntrospectionEngine(this);
        }
    }
}
