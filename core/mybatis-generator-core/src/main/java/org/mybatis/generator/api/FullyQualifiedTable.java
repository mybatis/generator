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

import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.DomainObjectRenamingRule;
import org.mybatis.generator.internal.util.JavaBeansUtil;

public class FullyQualifiedTable {

    private final @Nullable String introspectedCatalog;
    private final @Nullable String introspectedSchema;
    private final String introspectedTableName;
    private final @Nullable String runtimeCatalog;
    private final @Nullable String runtimeSchema;
    private final @Nullable String runtimeTableName;
    private @Nullable String configuredDomainObjectName;
    private @Nullable String domainObjectSubPackage;
    private final @Nullable String alias;
    private final boolean ignoreQualifiersAtRuntime;
    private final String beginningDelimiter;
    private final String endingDelimiter;
    private final @Nullable DomainObjectRenamingRule domainObjectRenamingRule;

    public FullyQualifiedTable(Builder builder) {
        super();
        this.introspectedCatalog = builder.introspectedCatalog;
        this.introspectedSchema = builder.introspectedSchema;
        this.introspectedTableName = Objects.requireNonNull(builder.introspectedTableName);
        this.ignoreQualifiersAtRuntime = builder.ignoreQualifiersAtRuntime;
        this.runtimeCatalog = builder.runtimeCatalog;
        this.runtimeSchema = builder.runtimeSchema;
        this.runtimeTableName = builder.runtimeTableName;
        this.domainObjectRenamingRule = builder.domainObjectRenamingRule;

        if (stringHasValue(builder.domainObjectName)) {
            int index = builder.domainObjectName.lastIndexOf('.');
            if (index == -1) {
                this.configuredDomainObjectName = builder.domainObjectName;
            } else {
                this.configuredDomainObjectName = builder.domainObjectName.substring(index + 1);
                this.domainObjectSubPackage = builder.domainObjectName.substring(0, index);
            }
        }

        if (builder.alias == null) {
            this.alias = null;
        } else {
            this.alias = builder.alias.trim();
        }

        beginningDelimiter = builder.delimitIdentifiers ? builder.context.getBeginningDelimiter() : ""; //$NON-NLS-1$
        endingDelimiter = builder.delimitIdentifiers ? builder.context.getEndingDelimiter() : ""; //$NON-NLS-1$
    }

    public Optional<String> getIntrospectedCatalog() {
        return Optional.ofNullable(introspectedCatalog);
    }

    public Optional<String> getIntrospectedSchema() {
        return Optional.ofNullable(introspectedSchema);
    }

    public String getIntrospectedTableName() {
        return introspectedTableName;
    }

    public String getFullyQualifiedTableNameAtRuntime() {
        StringBuilder localCatalog = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (stringHasValue(runtimeCatalog)) {
                localCatalog.append(runtimeCatalog);
            } else if (stringHasValue(introspectedCatalog)) {
                localCatalog.append(introspectedCatalog);
            }
        }
        if (!localCatalog.isEmpty()) {
            addDelimiters(localCatalog);
        }

        StringBuilder localSchema = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (stringHasValue(runtimeSchema)) {
                localSchema.append(runtimeSchema);
            } else if (stringHasValue(introspectedSchema)) {
                localSchema.append(introspectedSchema);
            }
        }
        if (!localSchema.isEmpty()) {
            addDelimiters(localSchema);
        }

        StringBuilder localTableName = new StringBuilder();
        if (stringHasValue(runtimeTableName)) {
            localTableName.append(runtimeTableName);
        } else {
            localTableName.append(introspectedTableName);
        }
        addDelimiters(localTableName);

        return composeFullyQualifiedTableName(localCatalog
                .toString(), localSchema.toString(), localTableName.toString(),
                '.');
    }

    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        StringBuilder sb = new StringBuilder();

        sb.append(getFullyQualifiedTableNameAtRuntime());

        if (stringHasValue(alias)) {
            sb.append(' ');
            sb.append(alias);
        }

        return sb.toString();
    }

    public String getDomainObjectName() {
        if (stringHasValue(configuredDomainObjectName)) {
            return configuredDomainObjectName;
        }

        String finalDomainObjectName;
        if (stringHasValue(runtimeTableName)) {
            finalDomainObjectName = JavaBeansUtil.getCamelCaseString(runtimeTableName, true);
        } else {
            finalDomainObjectName = JavaBeansUtil.getCamelCaseString(introspectedTableName, true);
        }

        if (domainObjectRenamingRule != null) {
            Pattern pattern = domainObjectRenamingRule.pattern();
            String replaceString = domainObjectRenamingRule.replaceString();
            Matcher matcher = pattern.matcher(finalDomainObjectName);
            finalDomainObjectName = JavaBeansUtil.getFirstCharacterUppercase(matcher.replaceAll(replaceString));
        }
        return finalDomainObjectName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FullyQualifiedTable other)) {
            return false;
        }

        return Objects.equals(this.introspectedTableName, other.introspectedTableName)
                && Objects.equals(this.introspectedCatalog, other.introspectedCatalog)
                && Objects.equals(this.introspectedSchema, other.introspectedSchema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(introspectedTableName, introspectedCatalog, introspectedCatalog);
    }

    @Override
    public String toString() {
        return composeFullyQualifiedTableName(
                introspectedCatalog, introspectedSchema, introspectedTableName,
                '.');
    }

    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    /**
     * Calculates a Java package fragment based on the table catalog and schema.
     * If qualifiers are ignored, then this method will return an empty string.
     *
     * <p>This method is used for determining the sub package for Java client and
     * SQL map (XML) objects.  It ignores any sub-package added to the
     * domain object name in the table configuration.
     *
     * @param isSubPackagesEnabled
     *            the is sub packages enabled
     * @return the subpackage for this table
     */
    public String getSubPackageForClientOrSqlMap(boolean isSubPackagesEnabled) {
        StringBuilder sb = new StringBuilder();
        if (!ignoreQualifiersAtRuntime && isSubPackagesEnabled) {
            if (stringHasValue(runtimeCatalog)) {
                sb.append('.');
                sb.append(runtimeCatalog.toLowerCase());
            } else if (stringHasValue(introspectedCatalog)) {
                sb.append('.');
                sb.append(introspectedCatalog.toLowerCase());
            }

            if (stringHasValue(runtimeSchema)) {
                sb.append('.');
                sb.append(runtimeSchema.toLowerCase());
            } else if (stringHasValue(introspectedSchema)) {
                sb.append('.');
                sb.append(introspectedSchema.toLowerCase());
            }
        }

        // TODO - strip characters that are not valid in package names
        return sb.toString();
    }

    /**
     * Calculates a Java package fragment based on the table catalog and schema.
     * If qualifiers are ignored, then this method will return an empty string.
     *
     * <p>This method is used for determining the sub package for Java model objects only.
     * It takes into account the possibility that a sub-package was added to the
     * domain object name in the table configuration.
     *
     * @param isSubPackagesEnabled
     *            the is sub packages enabled
     * @return the subpackage for this table
     */
    public String getSubPackageForModel(boolean isSubPackagesEnabled) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSubPackageForClientOrSqlMap(isSubPackagesEnabled));

        if (stringHasValue(domainObjectSubPackage)) {
            sb.append('.');
            sb.append(domainObjectSubPackage);
        }

        return sb.toString();
    }

    private void addDelimiters(StringBuilder sb) {
        if (stringHasValue(beginningDelimiter)) {
            sb.insert(0, beginningDelimiter);
        }

        if (stringHasValue(endingDelimiter)) {
            sb.append(endingDelimiter);
        }
    }

    public Optional<String> getDomainObjectSubPackage() {
        return Optional.ofNullable(domainObjectSubPackage);
    }

    public static class Builder {
        private @Nullable String introspectedCatalog;
        private @Nullable String introspectedSchema;
        private @Nullable String introspectedTableName;
        private @Nullable String runtimeCatalog;
        private @Nullable String runtimeSchema;
        private @Nullable String runtimeTableName;
        private @Nullable String domainObjectName;
        private @Nullable String alias;
        private boolean ignoreQualifiersAtRuntime;
        private boolean delimitIdentifiers;
        private @Nullable DomainObjectRenamingRule domainObjectRenamingRule;
        private @Nullable Context context;

        /**
         * Sets the actual catalog of the table as returned from DatabaseMetaData.
         *
         * <p>This value should only be set if the user configured a catalog. Otherwise, the
         * DatabaseMetaData is reporting some database default that we don't want in the generated code.
         *
         * @param introspectedCatalog the introspected catalog
         * @return this builder
         */
        public Builder withIntrospectedCatalog(@Nullable String introspectedCatalog) {
            this.introspectedCatalog = introspectedCatalog;
            return this;
        }

        /**
         * Sets the actual schema of the table as returned from DatabaseMetaData.
         *
         * <p>This value should only be set if the user configured a schema. Otherwise, the
         * DatabaseMetaData is reporting some database default that we don't want in the generated code.
         *
         * @param introspectedSchema the introspected schema
         * @return this builder
         */
        public Builder withIntrospectedSchema(@Nullable String introspectedSchema) {
            this.introspectedSchema = introspectedSchema;
            return this;
        }

        /**
         * Sets the actual table name as returned from DatabaseMetaData.
         *
         * @param introspectedTableName the introspected table name
         * @return this builder
         */
        public Builder withIntrospectedTableName(String introspectedTableName) {
            this.introspectedTableName = introspectedTableName;
            return this;
        }

        /**
         * Sets the runtime catalog.
         *
         * <p>This is used to "rename" the catalog in the generated SQL. This is useful, for example, when
         *    generating code against one catalog that should run with a different catalog.
         *
         * @param runtimeCatalog the runtime catalog
         * @return this builder
         */
        public Builder withRuntimeCatalog(String runtimeCatalog) {
            this.runtimeCatalog = runtimeCatalog;
            return this;
        }

        /**
         * Sets the runtime schema.
         *
         * <p>This is used to "rename" the schema in the generated SQL. This is useful, for example, when
         *    generating code against one schema that should run with a different schema.
         *
         * @param runtimeSchema the runtime schema
         * @return this builder
         */
        public Builder withRuntimeSchema(String runtimeSchema) {
            this.runtimeSchema = runtimeSchema;
            return this;
        }

        /**
         * Sets the runtime table name.
         *
         * <p>This is used to "rename" the table in the generated SQL. This is useful, for example, when generating
         *    code to run with an Oracle synonym. The user would have to specify the actual table name and schema
         *    for generation, but would want to use the synonym name in the generated SQL
         *
         * @param runtimeTableName the runtime table name
         * @return this builder
         */
        public Builder withRuntimeTableName(String runtimeTableName) {
            this.runtimeTableName = runtimeTableName;
            return this;
        }

        /**
         * Set the configured domain object name for this table.
         *
         * <p>If nothing is configured, we'll build the domain object named based on the tableName or runtimeTableName.
         *
         * @param domainObjectName the domain object name
         * @return this builder
         */
        public Builder withDomainObjectName(String domainObjectName) {
            this.domainObjectName = domainObjectName;
            return this;
        }

        /**
         * Sets a configured alias for the table. This alias will be added to the table name in the SQL.
         *
         * @param alias the alias
         * @return this builder
         */
        public Builder withAlias(@Nullable String alias) {
            this.alias = alias;
            return this;
        }

        /**
         * If true, then the catalog and schema qualifiers will be ignored when composing fully qualified names
         * in the generated SQL.
         *
         * <p>This is used, for example, when the user needs to specify a specific schema for
         * generating code but does not want the schema in the generated SQL
         *
         * @param ignoreQualifiersAtRuntime whether to ignore qualifiers at runtime
         * @return this builder
         */
        public Builder withIgnoreQualifiersAtRuntime(boolean ignoreQualifiersAtRuntime) {
            this.ignoreQualifiersAtRuntime = ignoreQualifiersAtRuntime;
            return this;
        }

        /**
         * If true, then the table identifiers will be delimited at runtime.
         *
         * <p>The delimiter characters are obtained from the Context.
         *
         * @param delimitIdentifiers whether to delimit identifiers at runtime
         * @return this builder
         */
        public Builder withDelimitIdentifiers(boolean delimitIdentifiers) {
            this.delimitIdentifiers = delimitIdentifiers;
            return this;
        }

        /**
         * Sets a domain object renaming rule.
         *
         * <p>This is ignored is a domain object name is configured.
         *
         * <p>If a domain object name is not configured, we'll build the domain object named based on the tableName
         * or runtimeTableName, and then we use the domain object renaming rule to generate the final domain object name.
         *
         * @param domainObjectRenamingRule the domain object renaming rule
         * @return this builder
         */
        public Builder withDomainObjectRenamingRule(DomainObjectRenamingRule domainObjectRenamingRule) {
            this.domainObjectRenamingRule = domainObjectRenamingRule;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public FullyQualifiedTable build() {
            return new FullyQualifiedTable(this);
        }
    }
}
