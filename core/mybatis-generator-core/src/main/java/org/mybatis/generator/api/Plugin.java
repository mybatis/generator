/*
 *    Copyright 2006-2022 the original author or authors.
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

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

/**
 * This interface defines methods that will be called at different times during
 * the code generation process. These methods can be used to extend or modify
 * the generated code. Clients may implement this interface in its entirety, or
 * extend the PluginAdapter (highly recommended).
 *
 * <p>Plugins have a lifecycle. In general, the lifecycle is this:
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
 * <p>Plugins are related to contexts - so each context will have its own set of
 * plugins. If the same plugin is specified in multiple contexts, then each
 * context will hold a unique instance of the plugin.
 *
 * <p>Plugins are called, and initialized, in the same order they are specified in
 * the configuration.
 *
 * <p>The clientXXX, modelXXX, and sqlMapXXX methods are called by the code
 * generators. If you replace the default code generators with other
 * implementations, these methods may not be called.
 *
 * @author Jeff Butler
 * @see PluginAdapter
 *
 */
public interface Plugin {

    enum ModelClassType {
        PRIMARY_KEY,
        BASE_RECORD,
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
     *
     * <p>A good example of overriding an attribute would be the case where a user wanted to change the name of one
     * of the generated classes, change the target package, or change the name of the generated SQL map file.
     *
     * <p><b>Warning:</b> Anything that is listed as an attribute should not be changed by one of the other plugin
     * methods. For example, if you want to change the name of a generated example class, you should not simply change
     * the Type in the <code>modelExampleClassGenerated()</code> method. If you do, the change will not be reflected
     * in other generated artifacts.
     *
     * @param introspectedTable
     *            the introspected table
     */
    default void initialized(IntrospectedTable introspectedTable) {}

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
    default List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return Collections.emptyList();
    }

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
    default List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        return Collections.emptyList();
    }

    default List<GeneratedKotlinFile> contextGenerateAdditionalKotlinFiles() {
        return Collections.emptyList();
    }

    default List<GeneratedKotlinFile> contextGenerateAdditionalKotlinFiles(IntrospectedTable introspectedTable) {
        return Collections.emptyList();
    }

    default List<GeneratedFile> contextGenerateAdditionalFiles() {
        return Collections.emptyList();
    }

    default List<GeneratedFile> contextGenerateAdditionalFiles(IntrospectedTable introspectedTable) {
        return Collections.emptyList();
    }

    /**
     * This method can be used to generate any additional XML file needed by
     * your implementation. This method is called once, after all other XML
     * files have been generated.
     *
     * @return a List of GeneratedXmlFiles - these files will be saved
     *         with the other files from this run.
     */
    default List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        return Collections.emptyList();
    }

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
    default List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        return Collections.emptyList();
    }

    /**
     * This method is called when the entire client has been generated.
     * Implement this method to add additional methods or fields to a generated
     * client interface or implementation.
     *
     * @param interfaze
     *            the generated interface if any, may be null
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the interface should be generated, false if the generated
     *         interface should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param method
     *     the generated count method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     * @deprecated this method is no longer called
     */
    @Deprecated
    default boolean clientBasicCountMethodGenerated(Method method, Interface interfaze,
                                                    IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param kotlinFunction
     *     the generated function
     * @param kotlinFile
     *     the partially generated file
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     * @deprecated this method is no longer called
     */
    @Deprecated
    default boolean clientBasicCountMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param method
     *     the generated delete method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     * @deprecated No longer called
     */
    @Deprecated
    default boolean clientBasicDeleteMethodGenerated(Method method, Interface interfaze,
                                                     IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param kotlinFunction
     *     the generated delete function
     * @param kotlinFile
     *     the partially generated file
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     * @deprecated No longer called
     */
    @Deprecated
    default boolean clientBasicDeleteMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the insert method has been generated for the mapper interface.
     * This method is only called in the MyBatis3DynamicSql runtime. This method is only
     * called if the table has generated keys.
     *
     * @param method
     *     the generated insert method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean clientBasicInsertMethodGenerated(Method method, Interface interfaze,
                                                     IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the insert function has been generated for the mapper interface.
     * This method is only called in the MyBatis3Kotlin runtime. This method is only
     * called if the table has generated keys.
     *
     * @param kotlinFunction
     *     the generated insert function
     * @param kotlinFile
     *     the partially generated file
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the function should be generated, false if the generated
     *         function should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean clientBasicInsertMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the insert multiple method has been generated for the mapper interface.
     * This method is only called in the MyBatis3DynamicSql runtime. This method is only
     * called if the table has generated keys.
     *
     * @param method
     *     the generated insert method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean clientBasicInsertMultipleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the insert multiple method has been generated for the mapper interface.
     * This method is only called in the MyBatis3DynamicSql runtime. This method is only
     * called if the table has generated keys.
     *
     * @param kotlinFunction
     *     the generated insert function
     * @param kotlinFile
     *     the partially generated file
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         function should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean clientBasicInsertMultipleMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param method
     *     the generated insert method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     * @deprecated this method is no longer called
     */
    @Deprecated
    default boolean clientBasicInsertMultipleHelperMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Unused legacy method.
     *
     * @param kotlinFunction generated function
     * @param kotlinFile generated file
     * @param introspectedTable introspected table
     * @return true
     * @deprecated this method is no longer called
     */
    @Deprecated
    default boolean clientBasicInsertMultipleHelperMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the selectMany method has been generated for the mapper interface.
     * This method is only called in the MyBatis3DynamicSql runtime.
     *
     * @param method
     *     the generated selectMany method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean clientBasicSelectManyMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientBasicSelectManyMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the selectOne method has been generated for the mapper interface.
     * This method is only called in the MyBatis3DynamicSql runtime.
     *
     * @param method
     *     the generated selectOne method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean clientBasicSelectOneMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientBasicSelectOneMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param method
     *     the generated update method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     * @deprecated no longer called
     */
    @Deprecated
    default boolean clientBasicUpdateMethodGenerated(Method method, Interface interfaze,
                                                     IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param kotlinFunction
     *     the generated update function
     * @param kotlinFile
     *     the partially generated file
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the method should be generated, false if the generated
     *         method should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     * @deprecated no longer called
     */
    @Deprecated
    default boolean clientBasicUpdateMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientCountByExampleMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientDeleteByExampleMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientDeleteByPrimaryKeyMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the general count method has been generated. This is the replacement for countByExample
     * in the MyBatis Dynamic SQL V2 runtime.
     *
     * @param method
     *     the generated general count method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientGeneralCountMethodGenerated(Method method, Interface interfaze,
                                                      IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientGeneralCountMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the general delete method has been generated. This is the replacement for deleteByExample
     * in the MyBatis Dynamic SQL V2 runtime.
     *
     * @param method
     *     the generated general delete method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientGeneralDeleteMethodGenerated(Method method, Interface interfaze,
                                                       IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientGeneralDeleteMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the general select distinct method has been generated. This is the replacement for
     * selectDistinctByExample in the MyBatis Dynamic SQL V2 runtime.
     *
     * @param method
     *     the generated general select distinct method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientGeneralSelectDistinctMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientGeneralSelectDistinctMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the general select method has been generated. This is the replacement for
     * selectByExample in the MyBatis Dynamic SQL V2 runtime.
     *
     * @param method
     *     the generated general select method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientGeneralSelectMethodGenerated(Method method, Interface interfaze,
                                                       IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientGeneralSelectMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the general update method has been generated. This is the replacement for
     * updateByExample in the MyBatis Dynamic SQL V2 runtime.
     *
     * @param method
     *     the generated general update method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientGeneralUpdateMethodGenerated(Method method, Interface interfaze,
                                                       IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientGeneralUpdateMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientInsertMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientInsertMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the insert multiple method has been generated in the
     * client interface.
     * This method is only called in the MyBatis3DynamicSql runtime.
     *
     * @param method
     *            the generated insert multiple method
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
    default boolean clientInsertMultipleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientInsertMultipleMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientInsertSelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientInsertSelectiveMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientSelectByPrimaryKeyMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the selectList field is generated in a MyBatis Dynamic SQL V2 runtime.
     *
     * @param field the generated selectList field
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the field should be generated
     */
    default boolean clientSelectListFieldGenerated(Field field, Interface interfaze,
                                                   IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the selectOne method is generated. This is a new method in the MyBatis Dynamic SQL V2 runtime.
     *
     * @param method
     *     the generated selectOne method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientSelectOneMethodGenerated(Method method, Interface interfaze,
                                                   IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientSelectOneMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientUpdateByExampleSelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the updateAllColumns method is generated. The generated method can be used with the general
     * update method to mimic the function of the old updateByExample method.
     *
     * @param method
     *     the generated updateAllColumns method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientUpdateAllColumnsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientUpdateAllColumnsMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * Called when the updateSelectiveColumns method is generated. The generated method can be used with the general
     * update method to mimic the function of the old updateByExampleSelective method.
     *
     * @param method
     *     the generated updateSelectiveColumns method
     * @param interfaze
     *     the partially generated mapper interfaces
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the method should be generated
     */
    default boolean clientUpdateSelectiveColumnsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientUpdateSelectiveColumnsMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(KotlinFunction kotlinFunction,
            KotlinFile kotlinFile, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean clientSelectAllMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return true;
    }

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
    default boolean modelGetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return true;
    }

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
    default boolean modelSetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return true;
    }

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
    default boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapGenerated(GeneratedXmlFile sqlMap,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
     *         <code>sqlMapGenerated</code> method will not be called.
     */
    default boolean sqlMapDocumentGenerated(Document document,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapCountByExampleElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapDeleteByExampleElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapBaseColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapBlobColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapInsertElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapSelectAllElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerApplyWhereMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerCountByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerDeleteByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerInsertSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerSelectByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerUpdateByExampleSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerUpdateByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

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
    default boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the MyBatis Dynamic SQL support class has
     * been generated in the MyBatis Dynamic SQL runtime.
     *
     * @param supportClass
     *            the generated MyBatis Dynamic SQL support class
     *            You can add additional items to the generated class
     *            if necessary.
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the class should be generated, false if the generated
     *         class should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    default boolean dynamicSqlSupportGenerated(TopLevelClass supportClass, IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is called when the MyBatis Dynamic SQL support object has been generated. The format of the class
     * is an outer object with an inner class. The inner class contains the table and column definitions. The outer
     * (singleton) object contains a reference to an instance of the inner class, and shortcut properties that
     * reference the columns of that instance.
     *
     * @param kotlinFile the generated Kotlin file containing the outer object and inner class
     * @param outerSupportObject a reference to the outer object in the file
     * @param innerSupportClass a reference to the inner class
     * @param introspectedTable the class containing information about the table as
     *                          introspected from the database
     * @return true if the generated file should be kept
     */
    default boolean dynamicSqlSupportGenerated(KotlinFile kotlinFile, KotlinType outerSupportObject,
                                               KotlinType innerSupportClass, IntrospectedTable introspectedTable) {
        return true;
    }

    /**
     * This method is no longer called.
     *
     * @param extensionsFile
     *     the partially generated file
     * @param introspectedTable
     *     The class containing information about the table as introspected from the database
     * @return true if the file should be generated, false if the generated
     *         file should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     *
     * @deprecated this method is no longer called
     */
    @Deprecated
    default boolean mapperExtensionsGenerated(KotlinFile extensionsFile, IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean mapperGenerated(KotlinFile mapperFile, KotlinType mapper, IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean kotlinDataClassGenerated(KotlinFile kotlinFile, KotlinType dataClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientColumnListPropertyGenerated(KotlinProperty kotlinProperty, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientInsertMultipleVarargMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }

    default boolean clientUpdateByPrimaryKeyMethodGenerated(KotlinFunction kotlinFunction, KotlinFile kotlinFile,
            IntrospectedTable introspectedTable) {
        return true;
    }
}
