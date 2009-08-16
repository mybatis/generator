/*
 *  Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.ibatis.ibator.api;

import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.internal.util.EqualsUtil;
import org.apache.ibatis.ibator.internal.util.HashCodeUtil;
import org.apache.ibatis.ibator.internal.util.JavaBeansUtil;
import org.apache.ibatis.ibator.internal.util.StringUtility;

/**
 * @author Jeff Butler
 */
public class FullyQualifiedTable {

	private String introspectedCatalog;

	private String introspectedSchema;

	private String introspectedTableName;

    private String runtimeCatalog;
    
    private String runtimeSchema;
    
    private String runtimeTableName;
    
	private String domainObjectName;
    
    private String alias;
    
    private boolean ignoreQualifiersAtRuntime;
    
    private String beginningDelimiter;
    
    private String endingDelimiter;
    
    
    /**
     * This object is used to hold information related to the table itself,
     * not the columns in the table.
     * 
     * @param introspectedCatalog the actual catalog of the table as returned from
     *  DatabaseMetaData.  This value should only be set if the user
     *  configured a catalog.  Otherwise the DatabaseMetaData is reporting
     *  some database default that we don't want in the generated code.
     *  
     * @param introspectedSchema the actual schema of the table as returned from
     *  DatabaseMetaData.  This value should only be set if the user
     *  configured a schema.  Otherwise the DatabaseMetaData is reporting
     *  some database default that we don't want in the generated code.
     *  
     * @param introspectedTableName the actual table name as returned from DatabaseMetaData
     * 
     * @param domainObjectName the configured domain object name for this table.
     *  If nothing is configured, we'll build the domain object named based
     *  on the tableName or runtimeTableName.
     *  
     * @param alias a configured alias for the table. This alias will be
     *  added to the table name in the SQL
     * 
     * @param ignoreQualifiersAtRuntime if true, then the catalog and schema
     *  qualifiers will be ignored when composing fully qualified names in 
     *  the generated SQL.  This is used, for example, when the user needs
     *  to specify a specific schema for generating code but does not want
     *  the schema in the generated SQL 
     * 
     * @param runtimeCatalog this is used to "rename" the catalog in the 
     *  generated SQL.  This is useful, for example, when generating code
     *  against one catalog that should run with a different catalog.
     *  
     * @param runtimeSchema this is used to "rename" the schema in the 
     *  generated SQL.  This is useful, for example, when generating code
     *  against one schema that should run with a different schema.
     *  
     * @param runtimeTableName this is used to "rename" the table in the 
     *  generated SQL.  This is useful, for example, when generating code
     *  to run with an Oracle synonym.  The user would have to specify
     *  the actual table name and schema for generation, but would want to 
     *  use the synonym name in the generated SQL
     *  
     *  @param delimitIdentifiers if true, then the table identifiers will be
     *   delimited at runtime.  The delimiter characters are obtained
     *   from the IbatorContext.
     */
	public FullyQualifiedTable(String introspectedCatalog,
            String introspectedSchema,
            String introspectedTableName,
            String domainObjectName,
            String alias,
            boolean ignoreQualifiersAtRuntime,
            String runtimeCatalog,
            String runtimeSchema,
            String runtimeTableName,
            boolean delimitIdentifiers,
            IbatorContext ibatorContext) {
		super();
        this.introspectedCatalog = introspectedCatalog;
        this.introspectedSchema = introspectedSchema;
        this.introspectedTableName = introspectedTableName;
        this.domainObjectName = domainObjectName;
        this.ignoreQualifiersAtRuntime = ignoreQualifiersAtRuntime;
        this.runtimeCatalog = runtimeCatalog;
        this.runtimeSchema = runtimeSchema;
        this.runtimeTableName = runtimeTableName;
        
        if (alias == null) {
            this.alias = null;
        } else {
            this.alias = alias.trim();
        }

        beginningDelimiter = delimitIdentifiers ? ibatorContext.getBeginningDelimiter() : ""; //$NON-NLS-1$
        endingDelimiter = delimitIdentifiers ? ibatorContext.getEndingDelimiter() : ""; //$NON-NLS-1$
    }

	public String getIntrospectedCatalog() {
		return introspectedCatalog;
	}

	public String getIntrospectedSchema() {
		return introspectedSchema;
	}

	public String getIntrospectedTableName() {
		return introspectedTableName;
	}

	/**
	 * @return
	 */
    public String getFullyQualifiedTableNameAtRuntime() {
        StringBuilder localCatalog = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (StringUtility.stringHasValue(runtimeCatalog)) {
                localCatalog.append(runtimeCatalog);
            } else if (StringUtility.stringHasValue(introspectedCatalog)) {
                localCatalog.append(introspectedCatalog);
            }
        }
        if (localCatalog.length() > 0) {
            addDelimiters(localCatalog);
        }
        
        StringBuilder localSchema = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (StringUtility.stringHasValue(runtimeSchema)) {
                localSchema.append(runtimeSchema);
            } else if (StringUtility.stringHasValue(introspectedSchema)) {
                localSchema.append(introspectedSchema);
            }
        }
        if (localSchema.length() > 0) {
            addDelimiters(localSchema);
        }
        
        StringBuilder localTableName = new StringBuilder();
        if (StringUtility.stringHasValue(runtimeTableName)) {
            localTableName.append(runtimeTableName);
        } else {
            localTableName.append(introspectedTableName);
        }
        addDelimiters(localTableName);
        
        return StringUtility.composeFullyQualifiedTableName(
                localCatalog.toString(),
                localSchema.toString(),
                localTableName.toString(),
                '.');
    }

    /**
     * @return
     */
    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        StringBuilder sb = new StringBuilder();

        sb.append(getFullyQualifiedTableNameAtRuntime());
        
        if(StringUtility.stringHasValue(alias)) {
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
        String localCatalog = 
            StringUtility.stringHasValue(runtimeCatalog) ? runtimeCatalog : introspectedCatalog;
        String localSchema =
            StringUtility.stringHasValue(runtimeSchema) ? runtimeSchema : introspectedSchema;
        String localTable =
            StringUtility.stringHasValue(runtimeTableName) ? runtimeTableName : introspectedTableName; 
        
        return StringUtility.composeFullyQualifiedTableName(
                ignoreQualifiersAtRuntime ? null : localCatalog,
                ignoreQualifiersAtRuntime ? null : localSchema,
                localTable,
                '_');
	}
	
	public String getDomainObjectName() {
		if (StringUtility.stringHasValue(domainObjectName)) {
			return domainObjectName;
        } else if (StringUtility.stringHasValue(runtimeTableName)) {
            return JavaBeansUtil.getCamelCaseString(runtimeTableName, true);
		} else {
			return JavaBeansUtil.getCamelCaseString(introspectedTableName, true);
		}
	}

	@Override
    public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FullyQualifiedTable)) {
			return false;
		}

		FullyQualifiedTable other = (FullyQualifiedTable) obj;
		
		return EqualsUtil.areEqual(this.introspectedTableName, other.introspectedTableName)
		        && EqualsUtil.areEqual(this.introspectedCatalog, other.introspectedCatalog)
				&& EqualsUtil.areEqual(this.introspectedSchema, other.introspectedSchema);
    }
    
	@Override
    public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, introspectedTableName);
		result = HashCodeUtil.hash(result, introspectedCatalog);
		result = HashCodeUtil.hash(result, introspectedSchema);

		return result;
    }
    
	@Override
    public String toString() {
        return StringUtility.composeFullyQualifiedTableName(
                introspectedCatalog, introspectedSchema, introspectedTableName, '.');
    }

    public String getAlias() {
        return alias;
    }

    /**
     * Calculates a Java package fragment based on the 
     * table catalog and schema.  If qualifiers are ignored,
     * then this method will return an empty string 
     * 
     * @return the subpackage for this table
     */
    public String getSubPackage() {
        StringBuilder sb = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (StringUtility.stringHasValue(runtimeCatalog)) {
                sb.append('.');
                sb.append(runtimeCatalog.toLowerCase());
            } else if (StringUtility.stringHasValue(introspectedCatalog)) {
                sb.append('.');
                sb.append(introspectedCatalog.toLowerCase());
            }

            if (StringUtility.stringHasValue(runtimeSchema)) {
                sb.append('.');
                sb.append(runtimeSchema.toLowerCase());
            } else if (StringUtility.stringHasValue(introspectedSchema)) {
                sb.append('.');
                sb.append(introspectedSchema.toLowerCase());
            }
        }
        
        // TODO - strip characters that are not valid in package names
        return sb.toString();
    }
    
    private void addDelimiters(StringBuilder sb) {
        if (StringUtility.stringHasValue(beginningDelimiter)) {
            sb.insert(0, beginningDelimiter);
        }
        
        if (StringUtility.stringHasValue(endingDelimiter)) {
            sb.append(endingDelimiter);
        }
    }
}
