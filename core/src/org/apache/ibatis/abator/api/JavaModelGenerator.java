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

import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.abator.config.AbatorContext;

/**
 * This interface describes methods needed in any Java model generator.
 * A Java model generator is used to generate POJOs that match the
 * structure of the table.  Typically this includes:
 * 
 * <ul>
 *   <li>A class to match the primary key (if there is a primary key)</li>
 *   <li>A class to match the row without BLOBs</li>
 *   <li>A class to match the row with BLOBs</li>
 *   <li>A class to hold indicators for the "by example" methods</li>
 * </ul>
 * 
 * @author Jeff Butler
 */
public interface JavaModelGenerator {
    
    /**
     * Adds properties for this instance from any properties configured
     * in the JavaModelGeneratorConfiguration.
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
	 * Sets the target package of the generator taken from the 
	 * JavaModelGeneratorConfiguration element.  This method is
	 * called before any getXXX method.
	 * 
	 * @param targetPackage the configuration element's target package
	 */
	void setTargetPackage(String targetPackage);

	/**
	 * Sets the target project of the generator taken from the 
	 * JavaModelGeneratorConfiguration element.  This method is
	 * called before any getXXX method.
	 * 
	 * @param targetProject the configuration element's target project
	 */
	void setTargetProject(String targetProject);
	
	FullyQualifiedJavaType getPrimaryKeyType(FullyQualifiedTable table);

	/**
	 * 
	 * @param table the table for which the name should be generated
	 * @return the type for the record (the class that holds non-primary
	 *  key and non-BLOB fields).  Note that
	 *  the value will be calculated regardless of whether the table has these columns or not.
	 */
	FullyQualifiedJavaType getBaseRecordType(FullyQualifiedTable table);

	/**
	 * 
	 * @param table the table for which the name should be generated
	 * @return the type for the example class.
	 */
	FullyQualifiedJavaType getExampleType(FullyQualifiedTable table);

	/**
	 * 
	 * @param table the table for which the name should be generated
	 * @return the type for the record with BLOBs class.  Note that
	 *  the value will be calculated regardless of whether the table has BLOB columns or not.
	 */
	FullyQualifiedJavaType getRecordWithBLOBsType(FullyQualifiedTable table);

	/**
	 * This method returns a list of GenerateJavaFile objects.  The list may
	 * include any, or all, of the following types of generated java classes:
	 * 
	 * <ul>
	 *   <li>A Primary Key Class</li>
	 *   <li>A "record" class containing non-primary key and non-BLOB fields</li>
	 *   <li>A "record" class containing BLOB fields</li>
	 *   <li>An example class to be used on the "by example" queries</li>
	 * </ul>
	 * 
	 * @param introspectedTable
	 * @param callback
	 * @return a list of GeneratedJavaFile objects
	 */
	List getGeneratedJavaFiles(IntrospectedTable introspectedTable, ProgressCallback callback);
}
