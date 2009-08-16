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

/**
 * The DAOGenerator interface describes the methods needed to generate DAO
 * objects for a table.
 * 
 * @author Jeff Butler
 */
public interface DAOGenerator {
    /**
     * Adds properties for this instance from any properties configured
     * in the DAOGeneratorConfiguration.
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
     * Sets the target package for this instance. This value should be used to
     * calculate the package for the DAO interface and implementation classes.
     * 
     * This method will be called before any of the get methods.
     * 
     * @param targetPackage
     *            The target package from the configuration
     */
    void setTargetPackage(String targetPackage);

    /**
     * 
     * @param targetProject
     */
    void setTargetProject(String targetProject);

    /**
     * Sets the instance of JavaModelGenerator associated with this instance.
     * 
     * This method will be called before any of the get methods.
     * 
     * @param javaModelGenerator
     *            The JavaModelGenerator associated with this instance
     */
    void setJavaModelGenerator(JavaModelGenerator javaModelGenerator);

    /**
     * Sets the instance of SqlMapGenerator associated with this instance.
     * 
     * This method will be called before any of the get methods.
     * 
     * @param sqlMapGenerator
     *            The SqlMapGenerator associated with this instance
     */
    void setSqlMapGenerator(SqlMapGenerator sqlMapGenerator);

    /**
     * This method returns a list of GenerateJavaFile objects. The list may
     * include any, or all, of the following types of generated java classes:
     * 
     * <ul>
     * <li>A DAO Interface Class</li>
     * <li>A DAO Implementation Class</li>
     * </ul>
     * 
     * @param introspectedTable
     * @param callback
     * @return a list of GeneratedJavaFile objects
     */
    List getGeneratedJavaFiles(IntrospectedTable introspectedTable, ProgressCallback callback);
}
