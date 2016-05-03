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

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

/**
 * This interface defines methods that will be called at different times during
 * the code generation process. These methods can be used to extend or modify
 * the generated code. Clients may implement this interface in its entirety, or
 * extend the PluginAdapter (highly recommended).
 * <p>
 * Plugins have a lifecycle. In general, the lifecycle is this:
 * 
 * <ol>
 * <li>The setXXX methods are called one time</li>
 * <li>The validate method is called one time</li>
 * <li>The initialized method is called for each introspected table</li>
 * <li>The clientXXX methods are called for each introspected table</li>
 * <li>The providerXXX methods are called for each introspected table</li>
 * <li>The modelXXX methods are called for each introspected table</li>
 * <li>The sqlMapXXX methods are called for each introspected table</li>
 * <li>The contextGenerateAdditionalJavaFiles(IntrospectedTable) method is
 * called for each introspected table</li>
 * <li>The contextGenerateAdditionalXmlFiles(IntrospectedTable) method is called
 * for each introspected table</li>
 * <li>The contextGenerateAdditionalJavaFiles() method is called one time</li>
 * <li>The contextGenerateAdditionalXmlFiles() method is called one time</li>
 * </ol>
 * 
 * Plugins are related to contexts - so each context will have its own set of
 * plugins. If the same plugin is specified in multiple contexts, then each
 * context will hold a unique instance of the plugin.
 * <p>
 * Plugins are called, and initialized, in the same order they are specified in
 * the configuration.
 * <p>
 * The clientXXX, modelXXX, and sqlMapXXX methods are called by the code
 * generators. If you replace the default code generators with other
 * implementations, these methods may not be called.
 * 
 * @author Jeff Butler
 * @see PluginAdapter
 * 
 */
public interface Plugin {
    
    /**
     * The Enum ModelClassType.
     */
    public enum ModelClassType {
        
        /** The primary key. */
        PRIMARY_KEY, 
 /** The base record. */
 BASE_RECORD, 
 /** The record with blobs. */
 RECORD_WITH_BLOBS
    }

    /**
     * Set the context under which this plugin is running.
     *
     * @param context
     *            the new context
     */
    void setContext(Context context);

    /**
     * Set properties from the plugin configuration.
     *
     * @param properties
     *            the new properties
     */
    void setProperties(Properties properties);

    /**
     * This method is called just before the getGeneratedXXXFiles methods are called on the introspected table. Plugins
     * can implement this method to override any of the default attributes, or change the results of database
     * introspection, before any code generation activities occur. Attributes are listed as static Strings with the
     * prefix ATTR_ in IntrospectedTable.
     * <p>
     * A good example of overriding an attribute would be the case where a user wanted to change the name of one of the
     * generated classes, change the target package, or change the name of the generated SQL map file.
     * <p>
     * <b>Warning:</b> Anything that is listed as an attribute should not be changed by one of the other plugin methods.
     * For example, if you want to change the name of a generated example class, you should not simply change the Type
     * in the <code>modelExampleClassGenerated()</code> method. If you do, the change will not be reflected in other
     * generated artifacts.
     *
     * @param introspectedTable
     *            the introspected table
     */
    void initialized(IntrospectedTable introspectedTable);

    /**
     * This method is called after all the setXXX methods are called, but before
     * any other method is called. This allows the plugin to determine whether
     * it can run or not. For example, if the plugin requires certain properties
     * to be set, and the properties are not set, then the plugin is invalid and
     * will not run.
     * 
     * @param warnings
     *            add strings to this list to specify warnings. For example, if
     *            the plugin is invalid, you should specify why. Warnings are
     *            reported to users after the completion of the run.
     * @return true if the plugin is in a valid state. Invalid plugins will not
     *         be called
     */
    boolean validate(List<String> warnings);

    /**
     * This method can be used to generate any additional Java file needed by
     * your implementation. This method is called once, after all other Java
     * files have been generated.
     * 
     * @return a List of GeneratedJavaFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles();

    /**
     * This method can be used to generate additional Java files needed by your
     * implementation that might be related to a specific table. This method is
     * called once for every table in the configuration.
     * 
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return a List of GeneratedJavaFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable);

    /**
     * This method can be used to generate any additional XML file needed by
     * your implementation. This method is called once, after all other XML
     * files have been generated.
     * 
     * @return a List of GeneratedXmlFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles();

    /**
     * This method can be used to generate additional XML files needed by your
     * implementation that might be related to a specific table. This method is
     * called once for every table in the configuration.
     * 
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return a List of GeneratedXmlFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the entire client has been generated.
     * Implement this method to add additional methods or fields to a generated
     * client interface or implementation.
     * 
     * @param interfaze
     *            the generated interface if any, may be null
     * @param topLevelClass
     *            the generated implementation class if any, may be null
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the interface should be generated, false if the generated
     *         interface should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the countByExample method has been generated
     * in the client implementation class.
     * 
     * @param method
     *            the generated countByExample method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientCountByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the deleteByExample method has been generated
     * in the client implementation class.
     * 
     * @param method
     *            the generated deleteByExample method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientDeleteByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the deleteByPrimaryKey method has been
     * generated in the client implementation class.
     * 
     * @param method
     *            the generated deleteByPrimaryKey method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the insert method has been generated in the
     * client implementation class.
     * 
     * @param method
     *            the generated insert method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientInsertMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the insert selective method has been generated
     * in the client implementation class.
     * 
     * @param method
     *            the generated insert method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientInsertSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByExampleWithBLOBs method has been
     * generated in the client implementation class.
     * 
     * @param method
     *            the generated selectByExampleWithBLOBs method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByExampleWithoutBLOBs method has
     * been generated in the client implementation class.
     * 
     * @param method
     *            the generated selectByExampleWithoutBLOBs method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByPrimaryKey method has been
     * generated in the client implementation class.
     * 
     * @param method
     *            the generated selectByPrimaryKey method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleSelective method has been
     * generated in the client implementation class.
     * 
     * @param method
     *            the generated updateByExampleSelective method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByExampleSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithBLOBs method has been
     * generated in the client implementation class.
     * 
     * @param method
     *            the generated updateByExampleWithBLOBs method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithoutBLOBs method has
     * been generated in the client implementation class.
     * 
     * @param method
     *            the generated updateByExampleWithoutBLOBs method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeySelective method has
     * been generated in the client implementation class.
     * 
     * @param method
     *            the generated updateByPrimaryKeySelective method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeyWithBLOBs method has
     * been generated in the client implementation class.
     * 
     * @param method
     *            the generated updateByPrimaryKeyWithBLOBs method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeyWithoutBLOBs method has
     * been generated in the client implementation class.
     * 
     * @param method
     *            the generated updateByPrimaryKeyWithBLOBs method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the countByExample method has been generated
     * in the client interface.
     * 
     * @param method
     *            the generated countByExample method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientCountByExampleMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the deleteByExample method has been generated
     * in the client interface.
     * 
     * @param method
     *            the generated deleteByExample method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientDeleteByExampleMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the deleteByPrimaryKey method has been
     * generated in the client interface.
     * 
     * @param method
     *            the generated deleteByPrimaryKey method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the insert method has been generated in the
     * client interface.
     * 
     * @param method
     *            the generated insert method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientInsertMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the insert selective method has been generated
     * in the client interface.
     * 
     * @param method
     *            the generated insert method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientInsertSelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectAll method has been
     * generated in the client interface.  This method is only generated by
     * the simple runtime.
     * 
     * @param method
     *            the generated selectAll method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectAllMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectAll method has been
     * generated in the client implementation class.
     * 
     * @param method
     *            the generated selectAll method
     * @param topLevelClass
     *            the partially implemented client implementation class. You can
     *            add additional imported classes to the implementation class if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectAllMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    
    /**
     * This method is called when the selectByExampleWithBLOBs method has been
     * generated in the client interface.
     * 
     * @param method
     *            the generated selectByExampleWithBLOBs method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByExampleWithoutBLOBs method has
     * been generated in the client interface.
     * 
     * @param method
     *            the generated selectByExampleWithoutBLOBs method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByPrimaryKey method has been
     * generated in the client interface.
     * 
     * @param method
     *            the generated selectByPrimaryKey method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleSelective method has been
     * generated in the client interface.
     * 
     * @param method
     *            the generated updateByExampleSelective method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByExampleSelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithBLOBs method has been
     * generated in the client interface.
     * 
     * @param method
     *            the generated updateByExampleWithBLOBs method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithoutBLOBs method has
     * been generated in the client interface.
     * 
     * @param method
     *            the generated updateByExampleWithoutBLOBs method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeySelective method has
     * been generated in the client interface.
     * 
     * @param method
     *            the generated updateByPrimaryKeySelective method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeyWithBLOBs method has
     * been generated in the client interface.
     * 
     * @param method
     *            the generated updateByPrimaryKeyWithBLOBs method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeyWithoutBLOBs method has
     * been generated in the client interface.
     * 
     * @param method
     *            the generated updateByPrimaryKeyWithoutBLOBs method
     * @param interfaze
     *            the partially implemented client interface. You can add
     *            additional imported classes to the interface if
     *            necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * This method is called after the field is generated for a specific column
     * in a table.
     * 
     * @param field
     *            the field generated for the specified column
     * @param topLevelClass
     *            the partially implemented model class. You can add additional
     *            imported classes to the implementation class if necessary.
     * @param introspectedColumn
     *            The class containing information about the column related
     *            to this field as introspected from the database
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @param modelClassType
     *            the type of class that the field is generated for
     * @return true if the field should be generated, false if the generated
     *         field should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType);

    /**
     * This method is called after the getter, or accessor, method is generated
     * for a specific column in a table.
     * 
     * @param method
     *            the getter, or accessor, method generated for the specified
     *            column
     * @param topLevelClass
     *            the partially implemented model class. You can add additional
     *            imported classes to the implementation class if necessary.
     * @param introspectedColumn
     *            The class containing information about the column related
     *            to this field as introspected from the database
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @param modelClassType
     *            the type of class that the field is generated for
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean modelGetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType);

    /**
     * This method is called after the setter, or mutator, method is generated
     * for a specific column in a table.
     * 
     * @param method
     *            the setter, or mutator, method generated for the specified
     *            column
     * @param topLevelClass
     *            the partially implemented model class. You can add additional
     *            imported classes to the implementation class if necessary.
     * @param introspectedColumn
     *            The class containing information about the column related
     *            to this field as introspected from the database
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @param modelClassType
     *            the type of class that the field is generated for
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean modelSetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType);

    /**
     * This method is called after the primary key class is generated by the
     * JavaModelGenerator. This method will only be called if
     * the table rules call for generation of a primary key class.
     * <br><br>
     * This method is only guaranteed to be called by the Java
     * model generators. Other user supplied generators may, or may not, call
     * this method.
     * 
     * @param topLevelClass
     *            the generated primary key class
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the class should be generated, false if the generated
     *         class should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    /**
     * This method is called after the base record class is generated by the
     * JavaModelGenerator. This method will only be called if
     * the table rules call for generation of a base record class.
     * <br><br>
     * This method is only guaranteed to be called by the default Java
     * model generators. Other user supplied generators may, or may not, call
     * this method.
     * 
     * @param topLevelClass
     *            the generated base record class
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the class should be generated, false if the generated
     *         class should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    /**
     * This method is called after the record with BLOBs class is generated by
     * the JavaModelGenerator. This method will only be called
     * if the table rules call for generation of a record with BLOBs class.
     * <br><br>
     * This method is only guaranteed to be called by the default Java
     * model generators. Other user supplied generators may, or may not, call
     * this method.
     * 
     * @param topLevelClass
     *            the generated record with BLOBs class
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the class should be generated, false if the generated
     *         class should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    /**
     * This method is called after the example class is generated by the 
     * JavaModelGenerator. This method will only be called if the table
     * rules call for generation of an example class.
     * <br><br>
     * This method is only guaranteed to be called by the default Java
     * model generators. Other user supplied generators may, or may not, call
     * this method.
     * 
     * @param topLevelClass
     *            the generated example class
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the class should be generated, false if the generated
     *         class should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the SqlMap file has been generated.
     * 
     * @param sqlMap
     *            the generated file (containing the file name, package name,
     *            and project name)
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the sqlMap should be generated, false if the generated
     *         sqlMap should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapGenerated(GeneratedXmlFile sqlMap,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the SqlMap document has been generated. This
     * method can be used to add additional XML elements the the generated
     * document.
     * 
     * @param document
     *            the generated document (note that this is the MyBatis generator's internal
     *            Document class - not the w3c XML Document class)
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the document should be generated, false if the generated
     *         document should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins. Also, if any plugin returns false, then the
     *         <tt>sqlMapGenerated</tt> method will not be called.
     */
    boolean sqlMapDocumentGenerated(Document document,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the base resultMap is generated.
     * 
     * @param element
     *            the generated &lt;resultMap&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the countByExample element is generated.
     * 
     * @param element
     *            the generated &lt;select&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapCountByExampleElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the deleteByExample element is generated.
     * 
     * @param element
     *            the generated &lt;delete&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapDeleteByExampleElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the deleteByPrimaryKey element is generated.
     * 
     * @param element
     *            the generated &lt;delete&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the exampleWhereClause element is generated.
     * 
     * @param element
     *            the generated &lt;sql&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the baseColumnList element is generated.
     * 
     * @param element
     *            the generated &lt;sql&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapBaseColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the blobColumnList element is generated.
     * 
     * @param element
     *            the generated &lt;sql&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapBlobColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the insert element is generated.
     * 
     * @param element
     *            the generated &lt;insert&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapInsertElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the insert selective element is generated.
     * 
     * @param element
     *            the generated &lt;insert&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the resultMap with BLOBs element is generated
     * - this resultMap will extend the base resultMap.
     * 
     * @param element
     *            the generated &lt;resultMap&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectAll element is generated.
     * 
     * @param element
     *            the generated &lt;select&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapSelectAllElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByPrimaryKey element is generated.
     * 
     * @param element
     *            the generated &lt;select&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByExample element is generated.
     * 
     * @param element
     *            the generated &lt;select&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByExampleWithBLOBs element is
     * generated.
     * 
     * @param element
     *            the generated &lt;select&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleSelective element is
     * generated.
     * 
     * @param element
     *            the generated &lt;update&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithBLOBs element is
     * generated.
     * 
     * @param element
     *            the generated &lt;update&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithourBLOBs element is
     * generated.
     * 
     * @param element
     *            the generated &lt;update&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeySelective element is
     * generated.
     * 
     * @param element
     *            the generated &lt;update&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeyWithBLOBs element is
     * generated.
     * 
     * @param element
     *            the generated &lt;update&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeyWithoutBLOBs element is
     * generated.
     * 
     * @param element
     *            the generated &lt;update&gt; element
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the element should be generated, false if the generated
     *         element should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    /**
     * This method is called when the SQL provider has been generated.
     * Implement this method to add additional methods or fields to a generated
     * SQL provider.
     * 
     * @param topLevelClass
     *            the generated provider
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the provider should be generated, false if the generated
     *         provider should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the applyWhere method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated applyWhere method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerApplyWhereMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the countByExample method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated countByExample method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerCountByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the deleteByExample method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated deleteByExample method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerDeleteByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the insertSelective method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated insertSelective method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerInsertSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByExampleWithBLOBs method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated selectByExampleWithBLOBs method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerSelectByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the selectByExampleWithoutBLOBs method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated selectByExampleWithoutBLOBs method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleSelective method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated updateByExampleSelective method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerUpdateByExampleSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithBLOBs method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated updateByExampleWithBLOBs method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerUpdateByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByExampleWithoutBLOBs method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated updateByExampleWithoutBLOBs method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * This method is called when the updateByPrimaryKeySelective method has
     * been generated in the SQL provider.
     * 
     * @param method
     *            the generated updateByPrimaryKeySelective method
     * @param topLevelClass
     *            the partially generated provider class
     *            You can add additional imported classes to the class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
}
