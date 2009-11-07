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
import org.apache.ibatis.ibator.internal.util.StringUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * @author Jeff Butler
 */
public class ColumnOverride extends PropertyHolder {

	private String columnName;

	private String javaProperty;

	private String jdbcType;

	private String javaType;
    
    private String typeHandler;
    
    private boolean isColumnNameDelimited;

    private String configuredDelimitedColumnName;
    
	/**
	 *  
	 */
	public ColumnOverride(String columnName) {
		super();
        
        this.columnName = columnName;
        isColumnNameDelimited = StringUtility.stringContainsSpace(columnName);
}

	public String getColumnName() {
		return columnName;
	}

	public String getJavaProperty() {
		return javaProperty;
	}

	public void setJavaProperty(String javaProperty) {
		this.javaProperty = javaProperty;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }
    
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("columnOverride"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("column", columnName)); //$NON-NLS-1$
        
        if (StringUtility.stringHasValue(javaProperty)) {
            xmlElement.addAttribute(new Attribute("property", javaProperty)); //$NON-NLS-1$
        }
        
        if (StringUtility.stringHasValue(javaType)) {
            xmlElement.addAttribute(new Attribute("javaType", javaType)); //$NON-NLS-1$
        }
        
        if (StringUtility.stringHasValue(jdbcType)) {
            xmlElement.addAttribute(new Attribute("jdbcType", jdbcType)); //$NON-NLS-1$
        }
        
        if (StringUtility.stringHasValue(typeHandler)) {
            xmlElement.addAttribute(new Attribute("typeHandler", typeHandler)); //$NON-NLS-1$
        }
        
        if (StringUtility.stringHasValue(configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", configuredDelimitedColumnName)); //$NON-NLS-1$
        }
        
        addPropertyXmlElements(xmlElement);
        
        return xmlElement;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
        
        configuredDelimitedColumnName = isColumnNameDelimited ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    public void validate(List<String> errors, String tableName) {
        if (!StringUtility.stringHasValue(columnName)) {
            errors.add(Messages.getString("ValidationError.22",  //$NON-NLS-1$
                tableName));
        }
    }
}
