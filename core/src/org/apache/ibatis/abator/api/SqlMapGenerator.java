/*
 * Copyright 2005 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.ibatis.abator.api;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.abator.config.AbatorContext;

/**
 * This interface describes the operations that are required of an 
 * Sql Map Generator.  An Sql Map Generator is a class that generates
 * properly formatted Sql Maps for iBATIS.
 * 
 * All setXXX methods will be called before any getXXX method is called.
 * 
 * @author Jeff Butler
 */
public interface SqlMapGenerator {
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
     * Adds properties for this instance from any properties configured
     * in the SqlMapGeneratorConfiguration.
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

    void setTargetPackage(String targetPackage);

    void setTargetProject(String targetProject);

    void setJavaModelGenerator(JavaModelGenerator javaModelGenerator);

    String getSqlMapNamespace(FullyQualifiedTable table);

    String getInsertStatementId();

    String getUpdateByPrimaryKeyWithBLOBsStatementId();

    String getUpdateByPrimaryKeySelectiveStatementId();
    
    String getUpdateByPrimaryKeyStatementId();

    String getDeleteByPrimaryKeyStatementId();

    String getDeleteByExampleStatementId();

    String getSelectByPrimaryKeyStatementId();

    String getSelectByExampleStatementId();

    String getSelectByExampleWithBLOBsStatementId();
    
    String getCountByExampleStatementId();

    String getUpdateByExampleSelectiveStatementId();
    
    String getUpdateByExampleStatementId();
    
    String getUpdateByExampleWithBLOBsStatementId();
    
    List getGeneratedXMLFiles(IntrospectedTable introspectedTable, ProgressCallback callback);
}
