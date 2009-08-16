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

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.abator.config.AbatorContext;
import org.apache.ibatis.abator.exception.UnsupportedDataTypeException;
import org.apache.ibatis.abator.internal.db.ColumnDefinition;

/**
 * This interface describes methods that are required in any Java type
 * resolver.  A Java type resolver is used to make a default translation
 * between a JDBC type as returned from the database introspection process,
 * and a Java type.
 * 
 * @author Jeff Butler
 */
public interface JavaTypeResolver {
    /**
     * Adds properties for this instance from any properties configured
     * in the JavaTypeResolverConfiguration.
     * 
     * This method will be called before any of the get methods.
     * 
     * @param properties
     *            All properties from the configuration
     */
    void addConfigurationProperties(Properties properties);
    
    /**
     * Sets the instance of the AbatorConfiguration object associated with 
     * this instance.
     * 
     * This method will be called before any of the get methods.
     * 
     * @param abatorContext
     *            The current AbatorContext
     */
    void setAbatorContext(AbatorContext abatorContext);
	
	/**
	 * Abator will supply a list to this method.  The implementation class may
	 * add strings to the list that will be treated as warning messages and
	 * displayed to the user.  The concept of a warning is that code generation
	 * can continue, but that the results may not be what is expected.
	 * 
	 * @param warnings
	 */
	void setWarnings(List warnings);

	/**
	 * Initializes the ResolvedJavaType property of the ColumnDescription based
	 * on the jdbc type, length, and scale of the column.
	 * 
	 * @param cd the JDBC type will be used first to resolve the Java type. If
	 *            the type cannot be resolved from this value, then we will try
	 *            from the type name (which may be the qualified UDT from the
	 *            database)
	 */
	void initializeResolvedJavaType(ColumnDefinition cd)
			throws UnsupportedDataTypeException;
}
