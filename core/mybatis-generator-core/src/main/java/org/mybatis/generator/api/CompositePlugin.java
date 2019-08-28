/**
 *    Copyright 2006-2019 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

/**
 * This class implements a composite plugin. It contains a list of plugins for the
 * current context and is used to aggregate plugins together. This class
 * implements the rule that if any plugin returns "false" from a method, then no
 * subsequent plugin is called.
 * 
 * @author Jeff Butler
 * 
 */
public abstract class CompositePlugin implements Plugin {
    private List<Plugin> plugins = new ArrayList<>();
    
    protected CompositePlugin() {
        super();
    }
    
    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    @Override
    public void setContext(Context context) {
        for (Plugin plugin : plugins) {
            plugin.setContext(context);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        for (Plugin plugin : plugins) {
            plugin.setProperties(properties);
        }
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            plugin.initialized(introspectedTable);
        }
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return plugins.stream()
                .map(Plugin::contextGenerateAdditionalJavaFiles)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        return plugins.stream()
                .map(p -> p.contextGenerateAdditionalJavaFiles(introspectedTable))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        return plugins.stream()
                .map(Plugin::contextGenerateAdditionalXmlFiles)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        return plugins.stream()
                .map(p -> p.contextGenerateAdditionalXmlFiles(introspectedTable))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientGenerated(interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicCountMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicCountMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicDeleteMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicDeleteMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicInsertMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicInsertMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicInsertMultipleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicInsertMultipleHelperMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicInsertMultipleHelperMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicInsertMultipleMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicSelectManyMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicSelectManyMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicSelectOneMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicSelectOneMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientBasicUpdateMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientBasicUpdateMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientCountByExampleMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientDeleteByExampleMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientGeneralCountMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientGeneralCountMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientGeneralDeleteMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientGeneralDeleteMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientGeneralSelectDistinctMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientGeneralSelectDistinctMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientGeneralSelectMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientGeneralSelectMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientGeneralUpdateMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientGeneralUpdateMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientInsertMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientInsertMultipleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientInsertMultipleMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientSelectListFieldGenerated(Field field, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientSelectListFieldGenerated(field, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientSelectOneMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientSelectOneMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateAllColumnsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateAllColumnsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateSelectiveColumnsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateSelectiveColumnsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.clientSelectAllMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
            ModelClassType modelClassType) {
        for (Plugin plugin : plugins) {
            if (!plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable,
                    modelClassType)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
            ModelClassType modelClassType) {
        for (Plugin plugin : plugins) {
            if (!plugin.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable,
                    modelClassType)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
            ModelClassType modelClassType) {
        for (Plugin plugin : plugins) {
            if (!plugin.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable,
                    modelClassType)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.modelExampleClassGenerated(topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapGenerated(sqlMap, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapDocumentGenerated(document, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapResultMapWithoutBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapCountByExampleElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapDeleteByExampleElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapDeleteByPrimaryKeyElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapExampleWhereClauseElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapBaseColumnListElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapBlobColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapBlobColumnListElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapInsertElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapInsertSelectiveElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapResultMapWithBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapSelectAllElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapUpdateByExampleWithBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerGenerated(topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerApplyWhereMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerApplyWhereMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerCountByExampleMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerInsertSelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.providerUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean dynamicSqlSupportGenerated(TopLevelClass supportClass, IntrospectedTable introspectedTable) {
        for (Plugin plugin : plugins) {
            if (!plugin.dynamicSqlSupportGenerated(supportClass, introspectedTable)) {
                return false;
            }
        }
        
        return true;
    }
}
