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
package org.mybatis.generator.config;

import java.util.Enumeration;
import java.util.Properties;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * @author Jeff Butler
 */
public abstract class PropertyHolder {
    private Properties properties;

    /**
	 *  
	 */
    public PropertyHolder() {
        super();
        properties = new Properties();
    }

    public void addProperty(String name, String value) {
        properties.setProperty(name, value);
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public Properties getProperties() {
        return properties;
    }

    protected void addPropertyXmlElements(XmlElement xmlElement) {
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String propertyName = (String) enumeration.nextElement();

            XmlElement propertyElement = new XmlElement("property"); //$NON-NLS-1$
            propertyElement.addAttribute(new Attribute("name", propertyName)); //$NON-NLS-1$
            propertyElement.addAttribute(new Attribute(
                    "value", properties.getProperty(propertyName))); //$NON-NLS-1$
            xmlElement.addElement(propertyElement);
        }
    }
}
