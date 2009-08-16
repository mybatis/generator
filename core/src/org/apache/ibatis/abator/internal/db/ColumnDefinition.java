/*
 * Copyright 2005 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.ibatis.abator.internal.db;

import java.sql.Types;
import java.util.StringTokenizer;

import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.config.AbatorContext;
import org.apache.ibatis.abator.internal.types.ResolvedJavaType;
import org.apache.ibatis.abator.internal.util.StringUtility;

/**
 * This class holds information about an introspected column.  The
 * class has utility methods useful for generating iBATIS objects.
 * 
 * @author Jeff Butler
 */
public class ColumnDefinition {
    private String actualColumnName;
    
    private int jdbcType;

    private boolean nullable;

    private int length;

    private int scale;

    private String typeName;

    private boolean identity;

    private String javaProperty;
    
    private ResolvedJavaType resolvedJavaType;
    
    private String tableAlias;
    
    private String typeHandler;
    
    private AbatorContext abatorContext;
    
    private boolean isColumnNameDelimited;

    /**
     * Constructs a Column definition.  This object holds all the 
     * information about a column that is required to generate
     * Java objects and SQL maps;
     * 
     * @param tableAlias The specified table alias, or null.  This
     *   value is used to rename and alias column names for select statements
     */
    public ColumnDefinition(String tableAlias, AbatorContext abatorContext) {
        super();
        this.tableAlias = tableAlias;
        this.abatorContext = abatorContext;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	/*
	 * This method is primarily used for debugging, so we don't externalize the strings
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Actual Column Name: "); //$NON-NLS-1$
        sb.append(actualColumnName);
        sb.append(", JDBC Type: "); //$NON-NLS-1$
        sb.append(jdbcType);
        sb.append(", Type Name: "); //$NON-NLS-1$
        sb.append(typeName);
        sb.append(", Nullable: "); //$NON-NLS-1$
        sb.append(nullable);
        sb.append(", Length: "); //$NON-NLS-1$
        sb.append(length);
        sb.append(", Scale: "); //$NON-NLS-1$
        sb.append(scale);
        sb.append(", Identity: "); //$NON-NLS-1$
        sb.append(identity);

        return sb.toString();
    }

    public void setActualColumnName(String actualColumnName) {
        this.actualColumnName = actualColumnName;
        isColumnNameDelimited = StringUtility.stringContainsSpace(actualColumnName);
    }

    /**
     * @return Returns the identity.
     */
    public boolean isIdentity() {
        return identity;
    }

    /**
     * @param identity
     *            The identity to set.
     */
    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isBLOBColumn() {
        String typeName = resolvedJavaType.getJdbcTypeName();

        return "BLOB".equals(typeName) || "LONGVARBINARY".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
                || "LONGVARCHAR".equals(typeName) || "CLOB".equals(typeName); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public boolean isStringColumn() {
        return resolvedJavaType.getFullyQualifiedJavaType().equals( 
            FullyQualifiedJavaType.getStringInstance());
    }
    
    public boolean isJdbcCharacterColumn() {
        return jdbcType == Types.CHAR
            || jdbcType == Types.CLOB
            || jdbcType == Types.LONGVARCHAR
            || jdbcType == Types.VARCHAR;
    }

    public String getJavaProperty() {
        return getJavaProperty(null);
    }

    public String getJavaProperty(String prefix) {
        if (prefix == null) {
            return javaProperty;
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append(javaProperty);
        
        return sb.toString();
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public ResolvedJavaType getResolvedJavaType() {
        return resolvedJavaType;
    }

    public void setResolvedJavaType(ResolvedJavaType resolvedJavaType) {
        this.resolvedJavaType = resolvedJavaType;
    }
    
    public String getByExampleIndicatorProperty() {
        return javaProperty + "_Indicator"; //$NON-NLS-1$
    }
    
    /**
     * The renamed column name for a select statement.  If there
     * is a table alias, the value will be alias_columnName.  This is
     * appropriate for use in a result map.
     * 
     * @return
     */
    public String getRenamedColumnNameForResultMap() {
        if (StringUtility.stringHasValue(tableAlias)) {
            StringBuffer sb = new StringBuffer();
            
            sb.append(tableAlias);
            sb.append('_');
            sb.append(actualColumnName);
            return sb.toString();
        } else {
            return actualColumnName;
        }
    }

    /**
     * The phrase to use in a select list.  If there
     * is a table alias, the value will be 
     * "alias.columnName as alias_columnName"
     * 
     * @return the proper phrase
     */
    public String getSelectListPhrase() {
        if (StringUtility.stringHasValue(tableAlias)) {
            StringBuffer sb = new StringBuffer();
            
            sb.append(getAliasedEscapedColumnName());
            sb.append(" as "); //$NON-NLS-1$
            if (isColumnNameDelimited) {
                sb.append(abatorContext.getBeginningDelimiter());
            }
            sb.append(tableAlias);
            sb.append('_');
            sb.append(escapeStringForIbatis(actualColumnName));
            if (isColumnNameDelimited) {
                sb.append(abatorContext.getEndingDelimiter());
            }
            return sb.toString();
        } else {
            return getEscapedColumnName();
        }
    }
    
    public boolean isJDBCDateColumn() {
        return resolvedJavaType.isJDBCDate()
            && !StringUtility.stringHasValue(typeHandler);
    }
    
    public boolean isJDBCTimeColumn() {
        return resolvedJavaType.isJDBCTime()
            && !StringUtility.stringHasValue(typeHandler);
    }
    
    public String getIbatisFormattedParameterClause() {
        return getIbatisFormattedParameterClause(null);
    }
    
    public String getIbatisFormattedParameterClause(String prefix) {
        StringBuffer sb = new StringBuffer();
        
        sb.append('#');
        sb.append(getJavaProperty(prefix));
        
        if (StringUtility.stringHasValue(typeHandler)) {
            sb.append(",jdbcType="); //$NON-NLS-1$
            sb.append(getResolvedJavaType().getJdbcTypeName());
            sb.append(",handler="); //$NON-NLS-1$
            sb.append(typeHandler);
        } else {
            sb.append(':');
            sb.append(getResolvedJavaType().getJdbcTypeName());
        }
        
        sb.append('#');
        
        return sb.toString();
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }
    
    private String escapeStringForIbatis(String s) {
        StringTokenizer st = new StringTokenizer(s, "$#", true); //$NON-NLS-1$
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("$".equals(token)) { //$NON-NLS-1$
                sb.append("$$"); //$NON-NLS-1$
            } else if ("#".equals(token)) { //$NON-NLS-1$
                sb.append("##"); //$NON-NLS-1$
            } else {
                sb.append(token);
            }
        }
        
        return sb.toString();
    }

    public String getActualColumnName() {
        return actualColumnName;
    }

    public String getEscapedColumnName() {
        StringBuffer sb = new StringBuffer();
        sb.append(escapeStringForIbatis(actualColumnName));
        
        if (isColumnNameDelimited) {
            sb.insert(0, abatorContext.getBeginningDelimiter());
            sb.append(abatorContext.getEndingDelimiter());
        }
        
        return sb.toString();
    }

    /**
     * The aliased column name for a select statement generated by the example clauses.
     * This is not appropriate for selects in SqlMaps because the column is
     * not escaped for iBATIS.  If there
     * is a table alias, the value will be alias.columnName.
     * 
     * This method is used in the Example classes and the returned value will be
     * in a Java string.  So we need to escape double quotes if they are
     * the delimiters.
     * 
     * @return
     */
    public String getAliasedActualColumnName() {
        StringBuffer sb = new StringBuffer();
        if (StringUtility.stringHasValue(tableAlias)) {
            sb.append(tableAlias);
            sb.append('.');
        }

        if (isColumnNameDelimited) {
            sb.append(StringUtility.escapeStringForJava(abatorContext.getBeginningDelimiter()));
        }
        
        sb.append(actualColumnName);
            
        if (isColumnNameDelimited) {
            sb.append(StringUtility.escapeStringForJava(abatorContext.getEndingDelimiter()));
        }
        
        return sb.toString();
    }

    /**
     * Calculates the string to use in select phrases in SqlMaps.
     * 
     * @return
     */
    public String getAliasedEscapedColumnName() {
        if (StringUtility.stringHasValue(tableAlias)) {
            StringBuffer sb = new StringBuffer();
            
            sb.append(tableAlias);
            sb.append('.');
            sb.append(getEscapedColumnName());
            return sb.toString();
        } else {
            return getEscapedColumnName();
        }
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }
}
