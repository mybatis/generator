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

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringContainsSpace;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * The Class RequiredColumn.
 * To declare columns required;
 *
 * @author Candy Lee
 */
public class RequiredColumn {

    /** The column name. */
    private String columnName;


    /**
     * Instantiates a new ignored column.
     *
     * @param columnName
     *            the column name
     */
    public RequiredColumn(String columnName) {
        super();
        this.columnName = columnName;
    }

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RequiredColumn)) {
            return false;
        }

        return columnName.equals(((RequiredColumn) obj).getColumnName());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return columnName.hashCode();
    }

    /**
     * To xml element.
     *
     * @return the xml element
     */
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("requiredColumn"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("column", columnName)); //$NON-NLS-1$
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
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.21", //$NON-NLS-1$
                    tableName));
        }
    }

    @Override
    public String toString() {
        return "RequiredColumn:" + columnName ;
    }
}
