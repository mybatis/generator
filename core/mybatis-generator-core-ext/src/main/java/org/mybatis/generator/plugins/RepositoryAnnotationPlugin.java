/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
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
package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Properties;

/**
 * This plugin adds the annotation @Repository to Client Object
 * <p>
 * This plugin demonstrates adding capabilities to generated Java artifacts, and
 * shows the proper way to add imports to a compilation unit.
 * <p>
 * Important: This is a simplistic implementation of annotation and does not
 * attempt to do any versioning of classes.
 * 
 * @author Jeff Butler
 * 
 */
public class RepositoryAnnotationPlugin extends PluginAdapter {

    private FullyQualifiedJavaType annotation;
    private boolean suppressJavaAnnotation;

    public RepositoryAnnotationPlugin() {
        super();
        annotation = new FullyQualifiedJavaType("org.springframework.stereotype.Repository"); //$NON-NLS-1$
    }

    public boolean validate(List<String> warnings) {
        // this plugin is always valid
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        suppressJavaAnnotation = Boolean.valueOf(properties.getProperty("suppressJavaAnnotation")); //$NON-NLS-1$
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeAnnotation(interfaze, topLevelClass, introspectedTable);
        return true;
    }

    protected void makeAnnotation(Interface interfaze, TopLevelClass topLevelClass,
                                  IntrospectedTable introspectedTable) {
        
        if (!suppressJavaAnnotation) {
            interfaze.addImportedType(annotation);
            interfaze.addAnnotation("@Repository");
        }
    }
}
