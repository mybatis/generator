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

import static org.mybatis.generator.internal.util.StringUtility.stringContainsSpace;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * The Class ColumnOverride.
 *
 * @author Jeff Butler
 */
public class ColumnOverride extends PropertyHolder {

    /** The column name. */
    private String columnName;

    /** The java property. */
    private String javaProperty;

    /** The jdbc type. */
    private String jdbcType;

    /** The java type. */
    private String javaType;

    /** The type handler. */
    private String typeHandler;

    /** The is column name delimited. */
    private boolean isColumnNameDelimited;

    /** The configured delimited column name. */
    private String configuredDelimitedColumnName;

    /**
     * Instantiates a new column override.
     *
     * @param columnName
     *            the column name
     */
    public ColumnOverride(String columnName) {
        super();

        this.columnName = columnName;
        isColumnNameDelimited = stringContainsSpace(columnName);
    }

    /**
     * Gets the column name.
     *
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Gets the java property.
     *
     * @return the java property
     */
    public String getJavaProperty() {
        return javaProperty;
    }

    /**
     * Sets the java property.
     *
     * @param javaProperty
     *            the new java property
     */
    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    /**
     * Gets the java type.
     *
     * @return the java type
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * Sets the java type.
     *
     * @param javaType
     *            the new java type
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    /**
     * Gets the jdbc type.
     *
     * @return the jdbc type
     */
    public String getJdbcType() {
        return jdbcType;
    }

    /**
     * Sets the jdbc type.
     *
     * @param jdbcType
     *            the new jdbc type
     */
    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    /**
     * Gets the type handler.
     *
     * @return the type handler
     */
    public String getTypeHandler() {
        return typeHandler;
    }

    /**
     * Sets the type handler.
     *
     * @param typeHandler
     *            the new type handler
     */
    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    /**
     * To xml element.
     *
     * @return the xml element
     */
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("columnOverride"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("column", columnName)); //$NON-NLS-1$

        if (stringHasValue(javaProperty)) {
            xmlElement.addAttribute(new Attribute("property", javaProperty)); //$NON-NLS-1$
        }

        if (stringHasValue(javaType)) {
            xmlElement.addAttribute(new Attribute("javaType", javaType)); //$NON-NLS-1$
        }

        if (stringHasValue(jdbcType)) {
            xmlElement.addAttribute(new Attribute("jdbcType", jdbcType)); //$NON-NLS-1$
        }

        if (stringHasValue(typeHandler)) {
            xmlElement.addAttribute(new Attribute("typeHandler", typeHandler)); //$NON-NLS-1$
        }

        if (stringHasValue(configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute(
                    "delimitedColumnName", configuredDelimitedColumnName)); //$NON-NLS-1$
        }

        addPropertyXmlElements(xmlElement);

        return xmlElement;
    }

    /**
     * Checks if is column name delimited.
     *
     * @return true, if is column name delimited
     */
    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    /**
     * Sets the column name delimited.
     *
     * @param isColumnNameDelimited
     *            the new column name delimited
     */
    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;

        configuredDelimitedColumnName = isColumnNameDelimited ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
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
            errors.add(getString("ValidationError.22", //$NON-NLS-1$
                    tableName));
        }
    }
}
