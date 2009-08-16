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
package org.apache.ibatis.ibator.config;

import java.util.List;

import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;
import org.apache.ibatis.ibator.internal.db.DatabaseDialects;
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * This class specifies that a key is auto-generated, either as an identity
 * column (post insert), or as some other query like a sequences (pre insert).
 * 
 * @author Jeff Butler
 */
public class GeneratedKey {
    private String column;

    private String configuredSqlStatement;

    private String runtimeSqlStatement;
    
    private boolean isIdentity;

    private String type;

    /**
     * 
     */
    public GeneratedKey(String column, String configuredSqlStatement, boolean isIdentity, String type) {
        super();
        this.column = column;
        this.type = type;
        this.isIdentity = isIdentity;
        this.configuredSqlStatement = configuredSqlStatement;

        DatabaseDialects dialect = DatabaseDialects.getDatabaseDialect(configuredSqlStatement);
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
    
    public boolean isBeforeInsert() {
        boolean rc;
        if (StringUtility.stringHasValue(type)) {
            rc = true;
        } else {
            if (isIdentity) {
                rc = false;
            } else {
                rc = true;
            }
        }
        
        return rc;
    }
    
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("generatedKey"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("column", column)); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("sqlStatement", configuredSqlStatement)); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("type", type)); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("identity", //$NON-NLS-1$
                isIdentity ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$
        
        return xmlElement;
    }
    
    public void validate(List<String> errors, String tableName) {
        if (!StringUtility.stringHasValue(runtimeSqlStatement)) {
            errors.add(Messages.getString("ValidationError.7",  //$NON-NLS-1$
                        tableName));
        }
            
        if (StringUtility.stringHasValue(type)) {
            if (!"pre".equals(type) && !"post".equals(type)) { //$NON-NLS-1$ //$NON-NLS-2$
                errors.add(Messages.getString("ValidationError.15",  //$NON-NLS-1$
                    tableName));
            }
        }
    }
}
