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
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedFile;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.KotlinFormatter;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.PluginAggregator;
import org.mybatis.generator.internal.db.DatabaseIntrospector;

public class Context extends PropertyHolder {
    private final String id;
    private final @Nullable JDBCConnectionConfiguration jdbcConnectionConfiguration;
    private final @Nullable ConnectionFactoryConfiguration connectionFactoryConfiguration;
    private final @Nullable SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;
    private final @Nullable JavaTypeResolverConfiguration javaTypeResolverConfiguration;
    private final JavaModelGeneratorConfiguration javaModelGeneratorConfiguration;
    private final @Nullable JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;
    private final List<TableConfiguration> tableConfigurations;
    private final ModelType defaultModelType;
    private final String beginningDelimiter;
    private final String endingDelimiter;
    private final @Nullable Boolean autoDelimitKeywords;
    private final @Nullable CommentGeneratorConfiguration commentGeneratorConfiguration;
    private final List<PluginConfiguration> pluginConfigurations;
    private final @Nullable String targetRuntime;
    private final @Nullable String introspectedColumnImpl;

    // the following are not really configurations, they are used in the code generation phase.
    // Ultimately, they should be moved out of this class and into the classes executing the generation
    private final JavaFormatter javaFormatter;
    private final KotlinFormatter kotlinFormatter;
    private final XmlFormatter xmlFormatter;
    private final CommentGenerator commentGenerator;
    // This is timing-dependent. The aggregator should be built before anyone calls it.
    private @Nullable PluginAggregator pluginAggregator;


    protected Context(Builder builder) {
        super(builder);
        id = Objects.requireNonNull(builder.id, getString("ValidationError.16")); //$NON-NLS-1$);
        defaultModelType = Objects.requireNonNull(builder.defaultModelType);
        tableConfigurations = Collections.unmodifiableList(builder.tableConfigurations);
        pluginConfigurations = Collections.unmodifiableList(builder.pluginConfigurations);
        commentGeneratorConfiguration = builder.commentGeneratorConfiguration;
        jdbcConnectionConfiguration = builder.jdbcConnectionConfiguration;
        connectionFactoryConfiguration = builder.connectionFactoryConfiguration;
        sqlMapGeneratorConfiguration = builder.sqlMapGeneratorConfiguration;
        javaTypeResolverConfiguration = builder.javaTypeResolverConfiguration;
        introspectedColumnImpl = builder.introspectedColumnImpl;
        javaModelGeneratorConfiguration = Objects.requireNonNull(builder.javaModelGeneratorConfiguration,
                getString("ValidationError.8", id)); //$NON-NLS-1$
        javaClientGeneratorConfiguration = builder.javaClientGeneratorConfiguration;
        targetRuntime = builder.targetRuntime;

        String property = getProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER);
        beginningDelimiter = property == null ? Defaults.DEFAULT_BEGINNING_DELIMITER : property;

        property = getProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER);
        endingDelimiter = property == null ? Defaults.DEFAULT_ENDING_DELIMITER : property;

        property = getProperty(PropertyRegistry.CONTEXT_AUTO_DELIMIT_KEYWORDS);
        autoDelimitKeywords = isTrue(property);

        javaFormatter = ObjectFactory.createJavaFormatter(this);
        kotlinFormatter = ObjectFactory.createKotlinFormatter(this);
        xmlFormatter = ObjectFactory.createXmlFormatter(this);
        commentGenerator = ObjectFactory.createCommentGenerator(this);
    }

    public Optional<JavaClientGeneratorConfiguration> getJavaClientGeneratorConfiguration() {
        return Optional.ofNullable(javaClientGeneratorConfiguration);
    }

    public JavaModelGeneratorConfiguration getJavaModelGeneratorConfiguration() {
        return Objects.requireNonNull(javaModelGeneratorConfiguration);
    }

    public Optional<JavaTypeResolverConfiguration> getJavaTypeResolverConfiguration() {
        return Optional.ofNullable(javaTypeResolverConfiguration);
    }

    public Optional<SqlMapGeneratorConfiguration> getSqlMapGeneratorConfiguration() {
        return Optional.ofNullable(sqlMapGeneratorConfiguration);
    }

    /**
     * This method does a simple validate, it makes sure that all required fields have been filled in. It does not do
     * any more complex operations such as validating that database tables exist or validating that named columns exist
     *
     * @param errors
     *            the errors
     */
    public void validate(List<String> errors) {
        if (!stringHasValue(id)) {
            errors.add(getString("ValidationError.16")); //$NON-NLS-1$
        }

        if (jdbcConnectionConfiguration == null && connectionFactoryConfiguration == null) {
            // must specify one
            errors.add(getString("ValidationError.10", id)); //$NON-NLS-1$
        } else if (jdbcConnectionConfiguration != null && connectionFactoryConfiguration != null) {
            // must not specify both
            errors.add(getString("ValidationError.10", id)); //$NON-NLS-1$
        } else if (jdbcConnectionConfiguration != null) {
            jdbcConnectionConfiguration.validate(errors);
        } else {
            connectionFactoryConfiguration.validate(errors);
        }

        javaModelGeneratorConfiguration.validate(errors, id);

        if (javaClientGeneratorConfiguration != null) {
            javaClientGeneratorConfiguration.validate(errors, id);
        }

        IntrospectedTable it = null;
        try {
            it = ObjectFactory.createIntrospectedTableForValidation(this);
        } catch (Exception e) {
            errors.add(getString("ValidationError.25", id)); //$NON-NLS-1$
        }

        if (it != null && it.requiresXMLGenerator()) {
            if (sqlMapGeneratorConfiguration == null) {
                errors.add(getString("ValidationError.9", id)); //$NON-NLS-1$
            } else {
                sqlMapGeneratorConfiguration.validate(errors, id);
            }
        }

        if (tableConfigurations.isEmpty()) {
            errors.add(getString("ValidationError.3", id)); //$NON-NLS-1$
        } else {
            for (int i = 0; i < tableConfigurations.size(); i++) {
                TableConfiguration tc = tableConfigurations.get(i);

                tc.validate(errors, i);
            }
        }

        for (PluginConfiguration pluginConfiguration : pluginConfigurations) {
            pluginConfiguration.validate(errors, id);
        }
    }

    public String getId() {
        return id;
    }

    public ModelType getDefaultModelType() {
        return defaultModelType;
    }

    public String getBeginningDelimiter() {
        return beginningDelimiter;
    }

    public String getEndingDelimiter() {
        return endingDelimiter;
    }

    public CommentGenerator getCommentGenerator() {
        return commentGenerator;
    }

    public JavaFormatter getJavaFormatter() {
        return javaFormatter;
    }

    public KotlinFormatter getKotlinFormatter() {
        return kotlinFormatter;
    }

    public XmlFormatter getXmlFormatter() {
        return xmlFormatter;
    }

    public Optional<CommentGeneratorConfiguration> getCommentGeneratorConfiguration() {
        return Optional.ofNullable(commentGeneratorConfiguration);
    }

    public Plugin getPlugins() {
        return Objects.requireNonNull(pluginAggregator);
    }

    public Optional<String> getTargetRuntime() {
        return Optional.ofNullable(targetRuntime);
    }

    public Optional<String> getIntrospectedColumnImpl() {
        return Optional.ofNullable(introspectedColumnImpl);
    }

    // methods related to code generation.
    //
    // Methods should be called in this order:
    //
    // 1. getIntrospectionSteps()
    // 2. introspectTables()
    // 3. getGenerationSteps()
    // 4. generateFiles()
    //

    private final List<IntrospectedTable> introspectedTables = new ArrayList<>();

    /**
     * This method could be useful for users that use the library for introspection only
     * and not for code generation.
     *
     * @return a list containing the results of table introspection. The list will be empty
     *     if this method is called before introspectTables(), or if no tables are found that
     *     match the configuration
     */
    public List<IntrospectedTable> getIntrospectedTables() {
        return introspectedTables;
    }

    public int getIntrospectionSteps() {
        int steps = 0;

        steps++; // connect to database

        // for each table:
        //
        // 1. Create introspected table implementation

        steps += tableConfigurations.size();

        return steps;
    }

    /**
     * Introspect tables based on the configuration specified in the
     * constructor. This method is long-running.
     *
     * @param callback
     *            a progress callback if progress information is desired, or
     *            <code>null</code>
     * @param warnings
     *            any warning generated from this method will be added to the
     *            List. Warnings are always Strings.
     * @param fullyQualifiedTableNames
     *            a set of table names to generate. The elements of the set must
     *            be Strings that exactly match what's specified in the
     *            configuration. For example, if table name = "foo" and schema =
     *            "bar", then the fully qualified table name is "foo.bar". If
     *            the Set is null or empty, then all tables in the configuration
     *            will be used for code generation.
     *
     * @throws SQLException
     *             if some error arises while introspecting the specified
     *             database tables.
     * @throws InterruptedException
     *             if the progress callback reports a cancel
     */
    public void introspectTables(ProgressCallback callback,
            List<String> warnings, @Nullable Set<String> fullyQualifiedTableNames)
            throws SQLException, InterruptedException {

        introspectedTables.clear();
        JavaTypeResolver javaTypeResolver = ObjectFactory
                .createJavaTypeResolver(this, warnings);

        Connection connection = null;

        try {
            callback.startTask(getString("Progress.0")); //$NON-NLS-1$
            connection = getConnection();

            DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
                    this, connection.getMetaData(), javaTypeResolver, warnings);

            for (TableConfiguration tc : tableConfigurations) {
                String tableName = composeFullyQualifiedTableName(tc.getCatalog(), tc
                                .getSchema(), tc.getTableName(), '.');

                if (fullyQualifiedTableNames != null
                        && !fullyQualifiedTableNames.isEmpty()
                        && !fullyQualifiedTableNames.contains(tableName)) {
                    continue;
                }

                if (!tc.areAnyStatementsEnabled()) {
                    warnings.add(getString("Warning.0", tableName)); //$NON-NLS-1$
                    continue;
                }

                callback.startTask(getString("Progress.1", tableName)); //$NON-NLS-1$
                List<IntrospectedTable> tables = databaseIntrospector.introspectTables(tc);
                introspectedTables.addAll(tables);

                callback.checkCancel();
            }
        } finally {
            closeConnection(connection);
        }
    }

    public int getGenerationSteps() {
        int steps = 0;

        for (IntrospectedTable introspectedTable : introspectedTables) {
            steps += introspectedTable.getGenerationSteps();
        }

        return steps;
    }

    public void generateFiles(ProgressCallback callback, List<GeneratedJavaFile> generatedJavaFiles,
                              List<GeneratedXmlFile> generatedXmlFiles, List<GeneratedKotlinFile> generatedKotlinFiles,
                              List<GeneratedFile> otherGeneratedFiles, List<String> warnings)
            throws InterruptedException {

        pluginAggregator = new PluginAggregator();
        for (PluginConfiguration pluginConfiguration : pluginConfigurations) {
            Plugin plugin = ObjectFactory.createPlugin(this, pluginConfiguration);
            if (plugin.validate(warnings)) {
                pluginAggregator.addPlugin(plugin);
            } else {
                warnings.add(getString("Warning.24", //$NON-NLS-1$
                        pluginConfiguration.getConfigurationType()
                                .orElse("Unknown Plugin Type"), id)); //$NON-NLS-1$
            }
        }

        // initialize everything first before generating. This allows plugins to know about other
        // items in the configuration.
        for (IntrospectedTable introspectedTable : introspectedTables) {
            callback.checkCancel();
            introspectedTable.initialize();
            introspectedTable.calculateGenerators(warnings, callback);
        }

        for (IntrospectedTable introspectedTable : introspectedTables) {
            callback.checkCancel();

            if (!pluginAggregator.shouldGenerate(introspectedTable)) {
                continue;
            }

            generatedJavaFiles.addAll(introspectedTable.getGeneratedJavaFiles());
            generatedXmlFiles.addAll(introspectedTable.getGeneratedXmlFiles());
            generatedKotlinFiles.addAll(introspectedTable.getGeneratedKotlinFiles());

            generatedJavaFiles.addAll(pluginAggregator.contextGenerateAdditionalJavaFiles(introspectedTable));
            generatedXmlFiles.addAll(pluginAggregator.contextGenerateAdditionalXmlFiles(introspectedTable));
            generatedKotlinFiles.addAll(pluginAggregator.contextGenerateAdditionalKotlinFiles(introspectedTable));
            otherGeneratedFiles.addAll(pluginAggregator.contextGenerateAdditionalFiles(introspectedTable));
        }

        generatedJavaFiles.addAll(pluginAggregator.contextGenerateAdditionalJavaFiles());
        generatedXmlFiles.addAll(pluginAggregator.contextGenerateAdditionalXmlFiles());
        generatedKotlinFiles.addAll(pluginAggregator.contextGenerateAdditionalKotlinFiles());
        otherGeneratedFiles.addAll(pluginAggregator.contextGenerateAdditionalFiles());
    }

    /**
     * This method creates a new JDBC connection from the values specified in the configuration file.
     * If you call this method, then you are responsible
     * for closing the connection (See {@link Context#closeConnection(Connection)}). If you do not
     * close the connection, then there could be connection leaks.
     *
     * @return a new connection created from the values in the configuration file
     * @throws SQLException if any error occurs while creating the connection
     */
    public Connection getConnection() throws SQLException {
        if (jdbcConnectionConfiguration != null) {
            return new JDBCConnectionFactory(jdbcConnectionConfiguration).getConnection();
        } else {
            return ObjectFactory.createConnectionFactory(Objects.requireNonNull(connectionFactoryConfiguration))
                    .getConnection();
        }
    }

    /**
     * This method closes a JDBC connection and ignores any errors. If the passed connection is null,
     * then the method does nothing.
     *
     * @param connection a JDBC connection to close, may be null
     */
    public void closeConnection(@Nullable Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    public boolean autoDelimitKeywords() {
        return autoDelimitKeywords != null && autoDelimitKeywords;
    }

    public Optional<ConnectionFactoryConfiguration> getConnectionFactoryConfiguration() {
        return Optional.ofNullable(connectionFactoryConfiguration);
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private @Nullable String id;
        private @Nullable ModelType defaultModelType;
        private @Nullable String targetRuntime;
        private @Nullable String introspectedColumnImpl;
        private final List<PluginConfiguration> pluginConfigurations = new ArrayList<>();
        private final ArrayList<TableConfiguration> tableConfigurations = new ArrayList<>();
        private @Nullable CommentGeneratorConfiguration commentGeneratorConfiguration;
        private @Nullable JDBCConnectionConfiguration jdbcConnectionConfiguration;
        private @Nullable ConnectionFactoryConfiguration connectionFactoryConfiguration;
        private @Nullable SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration;
        private @Nullable JavaTypeResolverConfiguration javaTypeResolverConfiguration;
        private @Nullable JavaModelGeneratorConfiguration javaModelGeneratorConfiguration;
        private @Nullable JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;

        @Override
        protected Builder getThis() {
            return this;
        }

        public Context build() {
            return new Context(this);
        }

        public Builder withId(@Nullable String id) {
            this.id = id;
            return this;
        }

        public Builder withDefaultModelType(ModelType defaultModelType) {
            this.defaultModelType = defaultModelType;
            return this;
        }

        public Builder withTargetRuntime(@Nullable String targetRuntime) {
            this.targetRuntime = targetRuntime;
            return this;
        }

        public Builder withIntrospectedColumnImpl(@Nullable String introspectedColumnImpl) {
            this.introspectedColumnImpl = introspectedColumnImpl;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withPluginConfiguration(PluginConfiguration pluginConfiguration) {
            pluginConfigurations.add(pluginConfiguration);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withTableConfiguration(TableConfiguration tableConfiguration) {
            tableConfigurations.add(tableConfiguration);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withCommentGeneratorConfiguration(CommentGeneratorConfiguration commentGeneratorConfiguration) {
            this.commentGeneratorConfiguration = commentGeneratorConfiguration;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withJdbcConnectionConfiguration(JDBCConnectionConfiguration jdbcConnectionConfiguration) {
            this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withSqlMapGeneratorConfiguration(SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration) {
            this.sqlMapGeneratorConfiguration = sqlMapGeneratorConfiguration;
            return this;
        }

        public Builder withConnectionFactoryConfiguration(ConnectionFactoryConfiguration connectionFactoryConfiguration) {
            this.connectionFactoryConfiguration = connectionFactoryConfiguration;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withJavaTypeResolverConfiguration(JavaTypeResolverConfiguration javaTypeResolverConfiguration) {
            this.javaTypeResolverConfiguration = javaTypeResolverConfiguration;
            return this;
        }

        public Builder withJavaModelGeneratorConfiguration(JavaModelGeneratorConfiguration javaModelGeneratorConfiguration) {
            this.javaModelGeneratorConfiguration = javaModelGeneratorConfiguration;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withJavaClientGeneratorConfiguration(JavaClientGeneratorConfiguration javaClientGeneratorConfiguration) {
            this.javaClientGeneratorConfiguration = javaClientGeneratorConfiguration;
            return this;
        }
    }
}
