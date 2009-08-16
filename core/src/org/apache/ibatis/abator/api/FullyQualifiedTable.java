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
package org.apache.ibatis.abator.api;

import org.apache.ibatis.abator.config.AbatorContext;
import org.apache.ibatis.abator.internal.util.EqualsUtil;
import org.apache.ibatis.abator.internal.util.HashCodeUtil;
import org.apache.ibatis.abator.internal.util.JavaBeansUtil;
import org.apache.ibatis.abator.internal.util.StringUtility;

/**
 * @author Jeff Butler
 */
public class FullyQualifiedTable {

	private String catalog;

	private String schema;

	private String tableName;

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
     * @param catalog the actual catalog of the table as returned from
     *  DatabaseMetaData.  This value should only be set if the user
     *  configured a catalog.  Otherwise the DatabaseMetaData is reporting
     *  some database default that we don't want in the generated code.
     *  
     * @param schema the actual schema of the table as returned from
     *  DatabaseMetaData.  This value should only be set if the user
     *  configured a schema.  Otherwise the DatabaseMetaData is reporting
     *  some database default that we don't want in the generated code.
     *  
     * @param tableName the actual table name as returned from DatabaseMetaData
     * 
     * @param domainObjectName the configred domain object name for this table.
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
     * @param runtimeTableName this is used to "rename" the table in the 
     *  generated SQL.  This is usefule, for example, when generating code
     *  to run with an Oracle synonym.  The user would have to specify
     *  the actual table name and schema for generation, but would want to 
     *  use the synonym name in the generated SQL
     *  
     *  @param delimitIdentifiers if true, then the table identifiers will be
     *   delimited at runtime.  The delimiter characters are obtained
     *   from the AbatorContext.
     */
	public FullyQualifiedTable(String catalog, String schema, String tableName,
            String domainObjectName, String alias, boolean ignoreQualifiersAtRuntime,
            String runtimeTableName, boolean delimitIdentifiers, AbatorContext abatorContext) {
		super();
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
        this.domainObjectName = domainObjectName;
        this.ignoreQualifiersAtRuntime = ignoreQualifiersAtRuntime;
        this.runtimeTableName = runtimeTableName;
        
        if (alias == null) {
            this.alias = null;
        } else {
            this.alias = alias.trim();
        }

        beginningDelimiter = delimitIdentifiers ? abatorContext.getBeginningDelimiter() : ""; //$NON-NLS-1$
        endingDelimiter = delimitIdentifiers ? abatorContext.getEndingDelimiter() : ""; //$NON-NLS-1$
    }

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}

	public String getTableName() {
		return tableName;
	}

    public String getFullyQualifiedTableNameAtRuntime() {
        String localCatalog;
        String localSchema;
        String localTableName;
        
        if (StringUtility.stringHasValue(catalog)  && !ignoreQualifiersAtRuntime) {
            localCatalog = beginningDelimiter + catalog + endingDelimiter;
        } else {
            localCatalog = null;
        }
        
        if (StringUtility.stringHasValue(schema)  && !ignoreQualifiersAtRuntime) {
            localSchema = beginningDelimiter + schema + endingDelimiter;
        } else {
            localSchema = null;
        }
        
        if (StringUtility.stringHasValue(runtimeTableName)) {
            localTableName = beginningDelimiter + runtimeTableName + endingDelimiter;
        } else {
            localTableName = beginningDelimiter + tableName + endingDelimiter;
        }
        
        return StringUtility.composeFullyQualifiedTableName(
                localCatalog,
                localSchema,
                localTableName,
                '.');
    }

    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        StringBuffer sb = new StringBuffer();

        sb.append(getFullyQualifiedTableNameAtRuntime());
        
        if(StringUtility.stringHasValue(alias)) {
            sb.append(' ');
            sb.append(alias);
        }

        return sb.toString();
    }

    /**
     * This method returns a string that is the fully qualified table name, with
     * underscores as the seperator.  This String should be 
     * @return
     */
    public String getSqlMapNamespace() {
        return StringUtility.composeFullyQualifiedTableName(
                ignoreQualifiersAtRuntime ? null : catalog,
                ignoreQualifiersAtRuntime ? null : schema,
                StringUtility.stringHasValue(runtimeTableName) ? runtimeTableName : tableName,
                '_');
	}
	
	public String getDomainObjectName() {
		if (StringUtility.stringHasValue(domainObjectName)) {
			return domainObjectName;
        } else if (StringUtility.stringHasValue(runtimeTableName)) {
            return JavaBeansUtil.getCamelCaseString(runtimeTableName, true);
		} else {
			return JavaBeansUtil.getCamelCaseString(tableName, true);
		}
	}

    public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FullyQualifiedTable)) {
			return false;
		}

		FullyQualifiedTable other = (FullyQualifiedTable) obj;
		
		return EqualsUtil.areEqual(this.tableName, other.tableName)
		        && EqualsUtil.areEqual(this.catalog, other.catalog)
				&& EqualsUtil.areEqual(this.schema, other.schema);
    }
    
    public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, tableName);
		result = HashCodeUtil.hash(result, catalog);
		result = HashCodeUtil.hash(result, schema);

		return result;
    }
    
    public String toString() {
        return StringUtility.composeFullyQualifiedTableName(
                catalog, schema, tableName, '.');
    }

    public String getAlias() {
        return alias;
    }

    /**
     * Calculates a Java package fragment based on the 
     * table catalog and schema.  If qualifiers are ignored,
     * then this method will return an empty string 
     * 
     * @return
     */
    public String getSubPackage() {
        StringBuffer sb = new StringBuffer();
        if (!ignoreQualifiersAtRuntime) {
            if (StringUtility.stringHasValue(catalog)) {
                sb.append('.');
                sb.append(catalog.toLowerCase());
            }

            if (StringUtility.stringHasValue(schema)) {
                sb.append('.');
                sb.append(schema.toLowerCase());
            }
        }

        return sb.toString();
    }
}
