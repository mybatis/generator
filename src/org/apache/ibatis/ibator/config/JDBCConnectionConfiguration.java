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
 * 
 * @author Jeff Butler
 */
public class JDBCConnectionConfiguration extends PropertyHolder {

	private String driverClass;

	private String connectionURL;

	private String userId;

	private String password;

	public JDBCConnectionConfiguration() {
		super();
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getDriverClass() {
		return driverClass;
	}
	
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
    
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("jdbcConnection"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("driverClass", driverClass)); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("connectionURL", connectionURL)); //$NON-NLS-1$
        
        if (StringUtility.stringHasValue(userId)) {
            xmlElement.addAttribute(new Attribute("userId", userId)); //$NON-NLS-1$
        }
        
        if (StringUtility.stringHasValue(password)) {
            xmlElement.addAttribute(new Attribute("password", password)); //$NON-NLS-1$
        }
        
        addPropertyXmlElements(xmlElement);
        
        return xmlElement;
    }

    public void validate(List<String> errors) {
        if (!StringUtility.stringHasValue(driverClass)) {
            errors.add(Messages.getString("ValidationError.4")); //$NON-NLS-1$
        }

        if (!StringUtility.stringHasValue(connectionURL)) {
            errors.add(Messages.getString("ValidationError.5")); //$NON-NLS-1$
        }
    }
}
