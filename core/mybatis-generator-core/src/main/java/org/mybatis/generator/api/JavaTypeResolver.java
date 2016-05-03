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
package org.mybatis.generator.api;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;

/**
 * This interface describes methods that are required in any Java type resolver.
 * A Java type resolver is used to make a default translation between a JDBC
 * type as returned from the database introspection process, and a Java type.
 * 
 * @author Jeff Butler
 */
public interface JavaTypeResolver {
    /**
     * Adds properties for this instance from any properties configured in the
     * JavaTypeResolverConfiguration.
     * 
     * This method will be called before any of the get methods.
     * 
     * @param properties
     *            All properties from the configuration
     */
    void addConfigurationProperties(Properties properties);

    /**
     * Sets the instance of the Context object associated with this instance.
     * 
     * This method will be called before any of the get methods.
     * 
     * @param context
     *            The current Context
     */
    void setContext(Context context);

    /**
     * The generator will supply a list to this method. The implementation class may add strings to the list that will
     * be treated as warning messages and displayed to the user. The concept of a warning is that code generation can
     * continue, but that the results may not be what is expected.
     *
     * @param warnings
     *            the new warnings
     */
    void setWarnings(List<String> warnings);

    /**
     * Calculates and returns the Java type that should be associated with this
     * column based on the jdbc type, length, and scale of the column.
     * 
     * @param introspectedColumn
     *            the column whose Java type needs to be calculated
     * @return the calculated type, or null if an unsupported data type. If null
     *         is returned, we will set the type to Object and issue a
     *         warning unless the column is ignored or otherwise overridden
     */
    FullyQualifiedJavaType calculateJavaType(
            IntrospectedColumn introspectedColumn);

    /**
     * Calculates and returns the JDBC type name that should be associated with
     * this column based on the jdbc type, length, and scale of the column.
     * 
     * @param introspectedColumn
     *            the column whose Java type needs to be calculated
     * @return the calculated type name, or null if an unsupported data type. If
     *         null is returned, we will set the type to OTHER and issue a
     *         warning unless the column is ignored or otherwise overridden
     */
    String calculateJdbcTypeName(IntrospectedColumn introspectedColumn);
}
