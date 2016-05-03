/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.api;

import static org.mybatis.generator.internal.util.EqualsUtil.areEqual;
import static org.mybatis.generator.internal.util.HashCodeUtil.SEED;
import static org.mybatis.generator.internal.util.HashCodeUtil.hash;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getCamelCaseString;
import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.config.Context;

/**
 * The Class FullyQualifiedTable.
 *
 * @author Jeff Butler
 */
public class FullyQualifiedTable {

    /** The introspected catalog. */
    private String introspectedCatalog;

    /** The introspected schema. */
    private String introspectedSchema;

    /** The introspected table name. */
    private String introspectedTableName;

    /** The runtime catalog. */
    private String runtimeCatalog;

    /** The runtime schema. */
    private String runtimeSchema;

    /** The runtime table name. */
    private String runtimeTableName;

    /** The domain object name. */
    private String domainObjectName;
    
    /** The domain object sub package. */
    private String domainObjectSubPackage;

    /** The alias. */
    private String alias;

    /** The ignore qualifiers at runtime. */
    private boolean ignoreQualifiersAtRuntime;

    /** The beginning delimiter. */
    private String beginningDelimiter;

    /** The ending delimiter. */
    private String endingDelimiter;

    /**
     * This object is used to hold information related to the table itself, not the columns in the table.
     *
     * @param introspectedCatalog
     *            the actual catalog of the table as returned from DatabaseMetaData. This value should only be set if
     *            the user configured a catalog. Otherwise the DatabaseMetaData is reporting some database default that
     *            we don't want in the generated code.
     * @param introspectedSchema
     *            the actual schema of the table as returned from DatabaseMetaData. This value should only be set if the
     *            user configured a schema. Otherwise the DatabaseMetaData is reporting some database default that we
     *            don't want in the generated code.
     * @param introspectedTableName
     *            the actual table name as returned from DatabaseMetaData
     * @param domainObjectName
     *            the configured domain object name for this table. If nothing is configured, we'll build the domain
     *            object named based on the tableName or runtimeTableName.
     * @param alias
     *            a configured alias for the table. This alias will be added to the table name in the SQL
     * @param ignoreQualifiersAtRuntime
     *            if true, then the catalog and schema qualifiers will be ignored when composing fully qualified names
     *            in the generated SQL. This is used, for example, when the user needs to specify a specific schema for
     *            generating code but does not want the schema in the generated SQL
     * @param runtimeCatalog
     *            this is used to "rename" the catalog in the generated SQL. This is useful, for example, when
     *            generating code against one catalog that should run with a different catalog.
     * @param runtimeSchema
     *            this is used to "rename" the schema in the generated SQL. This is useful, for example, when generating
     *            code against one schema that should run with a different schema.
     * @param runtimeTableName
     *            this is used to "rename" the table in the generated SQL. This is useful, for example, when generating
     *            code to run with an Oracle synonym. The user would have to specify the actual table name and schema
     *            for generation, but would want to use the synonym name in the generated SQL
     * @param delimitIdentifiers
     *            if true, then the table identifiers will be delimited at runtime. The delimiter characters are
     *            obtained from the Context.
     * @param context
     *            the context
     */
    public FullyQualifiedTable(String introspectedCatalog,
            String introspectedSchema, String introspectedTableName,
            String domainObjectName, String alias,
            boolean ignoreQualifiersAtRuntime, String runtimeCatalog,
            String runtimeSchema, String runtimeTableName,
            boolean delimitIdentifiers, Context context) {
        super();
        this.introspectedCatalog = introspectedCatalog;
        this.introspectedSchema = introspectedSchema;
        this.introspectedTableName = introspectedTableName;
        this.ignoreQualifiersAtRuntime = ignoreQualifiersAtRuntime;
        this.runtimeCatalog = runtimeCatalog;
        this.runtimeSchema = runtimeSchema;
        this.runtimeTableName = runtimeTableName;
        
        if (stringHasValue(domainObjectName)) {
            int index = domainObjectName.lastIndexOf('.');
            if (index == -1) {
                this.domainObjectName = domainObjectName;
            } else {
                this.domainObjectName = domainObjectName.substring(index + 1);
                this.domainObjectSubPackage = domainObjectName.substring(0, index);
            }
        }

        if (alias == null) {
            this.alias = null;
        } else {
            this.alias = alias.trim();
        }

        beginningDelimiter = delimitIdentifiers ? context
                .getBeginningDelimiter() : ""; //$NON-NLS-1$
        endingDelimiter = delimitIdentifiers ? context.getEndingDelimiter()
                : ""; //$NON-NLS-1$
    }

    /**
     * Gets the introspected catalog.
     *
     * @return the introspected catalog
     */
    public String getIntrospectedCatalog() {
        return introspectedCatalog;
    }

    /**
     * Gets the introspected schema.
     *
     * @return the introspected schema
     */
    public String getIntrospectedSchema() {
        return introspectedSchema;
    }

    /**
     * Gets the introspected table name.
     *
     * @return the introspected table name
     */
    public String getIntrospectedTableName() {
        return introspectedTableName;
    }

    /**
     * Gets the fully qualified table name at runtime.
     *
     * @return the fully qualified table name at runtime
     */
    public String getFullyQualifiedTableNameAtRuntime() {
        StringBuilder localCatalog = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (stringHasValue(runtimeCatalog)) {
                localCatalog.append(runtimeCatalog);
            } else if (stringHasValue(introspectedCatalog)) {
                localCatalog.append(introspectedCatalog);
            }
        }
        if (localCatalog.length() > 0) {
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
        if (localSchema.length() > 0) {
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

    /**
     * Gets the aliased fully qualified table name at runtime.
     *
     * @return the aliased fully qualified table name at runtime
     */
    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        StringBuilder sb = new StringBuilder();

        sb.append(getFullyQualifiedTableNameAtRuntime());

        if (stringHasValue(alias)) {
            sb.append(' ');
            sb.append(alias);
        }

        return sb.toString();
    }

    /**
     * This method returns a string that is the fully qualified table name, with
     * underscores as the separator.
     * 
     * @return the namespace
     */
    public String getIbatis2SqlMapNamespace() {
        String localCatalog = stringHasValue(runtimeCatalog) ? runtimeCatalog
                : introspectedCatalog;
        String localSchema = stringHasValue(runtimeSchema) ? runtimeSchema
                : introspectedSchema;
        String localTable = stringHasValue(runtimeTableName) ? runtimeTableName
                : introspectedTableName;

        return composeFullyQualifiedTableName(
                        ignoreQualifiersAtRuntime ? null : localCatalog,
                        ignoreQualifiersAtRuntime ? null : localSchema,
                        localTable, '_');
    }

    /**
     * Gets the domain object name.
     *
     * @return the domain object name
     */
    public String getDomainObjectName() {
        if (stringHasValue(domainObjectName)) {
            return domainObjectName;
        } else if (stringHasValue(runtimeTableName)) {
            return getCamelCaseString(runtimeTableName, true);
        } else {
            return getCamelCaseString(introspectedTableName, true);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FullyQualifiedTable)) {
            return false;
        }

        FullyQualifiedTable other = (FullyQualifiedTable) obj;

        return areEqual(this.introspectedTableName,
                other.introspectedTableName)
                && areEqual(this.introspectedCatalog,
                        other.introspectedCatalog)
                && areEqual(this.introspectedSchema,
                        other.introspectedSchema);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = SEED;
        result = hash(result, introspectedTableName);
        result = hash(result, introspectedCatalog);
        result = hash(result, introspectedSchema);

        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return composeFullyQualifiedTableName(
                introspectedCatalog, introspectedSchema, introspectedTableName,
                '.');
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Calculates a Java package fragment based on the table catalog and schema. If qualifiers are ignored, then this
     * method will return an empty string
     *
     * @param isSubPackagesEnabled
     *            the is sub packages enabled
     * @return the subpackage for this table
     */
    public String getSubPackage(boolean isSubPackagesEnabled) {
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
        
        if (stringHasValue(domainObjectSubPackage)) {
            sb.append('.');
            sb.append(domainObjectSubPackage);
        }

        // TODO - strip characters that are not valid in package names
        return sb.toString();
    }

    /**
     * Adds the delimiters.
     *
     * @param sb
     *            the sb
     */
    private void addDelimiters(StringBuilder sb) {
        if (stringHasValue(beginningDelimiter)) {
            sb.insert(0, beginningDelimiter);
        }

        if (stringHasValue(endingDelimiter)) {
            sb.append(endingDelimiter);
        }
    }
}
