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
 * This class includes no-operation methods for almost every method in the
 * Plugin interface. Clients may extend this class to implement some or all of
 * the methods in a plugin.
 * <p>
 * This adapter does not implement the <tt>validate</tt> method - all plugins
 * must perform validation.
 * 
 * @author Jeff Butler
 * 
 */
public abstract class PluginAdapter implements Plugin {
    protected Context context;
    protected Properties properties;

    public PluginAdapter() {
        properties = new Properties();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return null;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {
        return null;
    }

    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        return null;
    }

    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(
            IntrospectedTable introspectedTable) {
        return null;
    }

    public boolean clientCountByExampleMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientCountByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientDeleteByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientInsertMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientGenerated(Interface interfaze,
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(
            Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(
            Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean modelFieldGenerated(Field field,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
        return true;
    }

    public boolean modelGetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
        return true;
    }

    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean modelSetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
        return true;
    }

    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapCountByExampleElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapDocumentGenerated(Document document,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapInsertElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientInsertSelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientInsertSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public void initialized(IntrospectedTable introspectedTable) {
    }

    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapBlobColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerApplyWhereMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerCountByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerDeleteByExampleMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerInsertSelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerSelectByExampleWithBLOBsMethodGenerated(
            Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(
            Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerUpdateByExampleSelectiveMethodGenerated(
            Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerUpdateByExampleWithBLOBsMethodGenerated(
            Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(
            Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(
            Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectAllMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean clientSelectAllMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    public boolean sqlMapSelectAllElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        return true;
    }
}
