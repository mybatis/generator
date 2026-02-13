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

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.KnownRuntime;

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

    protected Context(Builder builder) {
        super(builder);
        id = Objects.requireNonNull(builder.id, getString("ValidationError.16")); //$NON-NLS-1$
        defaultModelType = Objects.requireNonNullElseGet(builder.defaultModelType,
                () -> calculateDefaultModelType(builder.targetRuntime));
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
    }

    private ModelType calculateDefaultModelType(@Nullable String targetRuntime) {
        if (targetRuntime == null) {
            return ModelType.FLAT;
        } else {
            KnownRuntime knownRuntime = KnownRuntime.getByAlias(targetRuntime);
            if (knownRuntime.isDynamicSqlBased()) {
                return ModelType.FLAT;
            } else {
                return ModelType.CONDITIONAL;
            }
        }
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

        KnownRuntime knownRuntime = KnownRuntime.getByAlias(targetRuntime);
        if (knownRuntime.isLegacyMyBatis3Based()
                && javaClientGeneratorConfiguration != null
                && javaClientGeneratorConfiguration.requiresXmlMapper()
                && sqlMapGeneratorConfiguration == null) {
            errors.add(getString("ValidationError.9", id)); //$NON-NLS-1$
        }

        if (knownRuntime.isDynamicSqlBased()) {
            if (defaultModelType != ModelType.FLAT && defaultModelType != ModelType.RECORD) {
                // TODO - Externalize
                errors.add("Dynamic SQL runtimes support flat or record based models only");
            }
        }

        if (knownRuntime.isLegacyMyBatis3Based() && defaultModelType == ModelType.RECORD) {
            // TODO - Externalize
            errors.add("Legacy runtimes do not support record as a model type");
        }

        if (sqlMapGeneratorConfiguration != null) {
            sqlMapGeneratorConfiguration.validate(errors, id);
        }

        if (tableConfigurations.isEmpty()) {
            errors.add(getString("ValidationError.3", id)); //$NON-NLS-1$
        } else {
            for (int i = 0; i < tableConfigurations.size(); i++) {
                TableConfiguration tc = tableConfigurations.get(i);

                tc.validate(errors, i, this);
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

    public Optional<CommentGeneratorConfiguration> getCommentGeneratorConfiguration() {
        return Optional.ofNullable(commentGeneratorConfiguration);
    }

    public Optional<String> getTargetRuntime() {
        return Optional.ofNullable(targetRuntime);
    }

    public Optional<String> getIntrospectedColumnImpl() {
        return Optional.ofNullable(introspectedColumnImpl);
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

    public Stream<PluginConfiguration> pluginConfigurations() {
        return pluginConfigurations.stream();
    }

    public List<TableConfiguration> tableConfigurations() {
        return tableConfigurations;
    }

    public boolean autoDelimitKeywords() {
        return autoDelimitKeywords != null && autoDelimitKeywords;
    }

    public @Nullable ConnectionFactoryConfiguration getConnectionFactoryConfiguration() {
        return connectionFactoryConfiguration;
    }

    public @Nullable JDBCConnectionConfiguration getJDBCConnectionConfiguration() {
        return jdbcConnectionConfiguration;
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

        public Builder withDefaultModelType(@Nullable ModelType defaultModelType) {
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

        public Builder withConnectionFactoryConfiguration(
                ConnectionFactoryConfiguration connectionFactoryConfiguration) {
            this.connectionFactoryConfiguration = connectionFactoryConfiguration;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withJavaTypeResolverConfiguration(JavaTypeResolverConfiguration javaTypeResolverConfiguration) {
            this.javaTypeResolverConfiguration = javaTypeResolverConfiguration;
            return this;
        }

        public Builder withJavaModelGeneratorConfiguration(
                JavaModelGeneratorConfiguration javaModelGeneratorConfiguration) {
            this.javaModelGeneratorConfiguration = javaModelGeneratorConfiguration;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder withJavaClientGeneratorConfiguration(
                JavaClientGeneratorConfiguration javaClientGeneratorConfiguration) {
            this.javaClientGeneratorConfiguration = javaClientGeneratorConfiguration;
            return this;
        }
    }
}
