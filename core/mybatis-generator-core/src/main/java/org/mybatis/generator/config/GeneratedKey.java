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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.db.DatabaseDialects;

/**
 * This class specifies that a key is auto-generated, either as an identity
 * column (post insert), or as some other query like a sequences (pre insert).
 * 
 * @author Jeff Butler
 */
public class GeneratedKey {
    
    /** The column. */
    private String column;

    /** The configured sql statement. */
    private String configuredSqlStatement;

    /** The runtime sql statement. */
    private String runtimeSqlStatement;

    /** The is identity. */
    private boolean isIdentity;

    /** The type. */
    private String type;

    /**
     * Instantiates a new generated key.
     *
     * @param column
     *            the column
     * @param configuredSqlStatement
     *            the configured sql statement
     * @param isIdentity
     *            the is identity
     * @param type
     *            the type
     */
    public GeneratedKey(String column, String configuredSqlStatement,
            boolean isIdentity, String type) {
        super();
        this.column = column;
        this.type = type;
        this.isIdentity = isIdentity;
        this.configuredSqlStatement = configuredSqlStatement;

        DatabaseDialects dialect = DatabaseDialects
                .getDatabaseDialect(configuredSqlStatement);
        if (dialect == null) {
            this.runtimeSqlStatement = configuredSqlStatement;
        } else {
            this.runtimeSqlStatement = dialect.getIdentityRetrievalStatement();
        }
    }

    /**
     * Gets the column.
     *
     * @return the column
     */
    public String getColumn() {
        return column;
    }

    /**
     * Checks if is identity.
     *
     * @return true, if is identity
     */
    public boolean isIdentity() {
        return isIdentity;
    }

    /**
     * Gets the runtime sql statement.
     *
     * @return the runtime sql statement
     */
    public String getRuntimeSqlStatement() {
        return runtimeSqlStatement;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * This method is used by the iBATIS2 generators to know if the XML &lt;selectKey&gt; element should be placed before the
     * insert SQL statement.
     *
     * @return true, if is placed before insert in ibatis2
     */
    public boolean isPlacedBeforeInsertInIbatis2() {
        boolean rc;
        
        if (stringHasValue(type)) {
            rc = true;
        } else {
            rc = !isIdentity;
        }
        
        return rc;
    }
    
    /**
     * Gets the my batis3 order.
     *
     * @return the my batis3 order
     */
    public String getMyBatis3Order() {
        return isIdentity ? "AFTER" : "BEFORE"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * To xml element.
     *
     * @return the xml element
     */
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("generatedKey"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("column", column)); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute(
                "sqlStatement", configuredSqlStatement)); //$NON-NLS-1$
        if (stringHasValue(type)) {
            xmlElement.addAttribute(new Attribute("type", type)); //$NON-NLS-1$
        }
        xmlElement.addAttribute(new Attribute("identity", //$NON-NLS-1$
                isIdentity ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$

        return xmlElement;
    }

    /**
     * Validate.
     *
     * @param errors
     *            the errors
     * @param tableName
     *            the table name
     */
    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(runtimeSqlStatement)) {
            errors.add(getString("ValidationError.7", //$NON-NLS-1$
                    tableName));
        }

        if (stringHasValue(type) &&
                !"pre".equals(type) && !"post".equals(type)) { //$NON-NLS-1$ //$NON-NLS-2$
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
    
    /**
     * Checks if is jdbc standard.
     *
     * @return true, if is jdbc standard
     */
    public boolean isJdbcStandard() {
        return "JDBC".equals(runtimeSqlStatement); //$NON-NLS-1$
    }
}
