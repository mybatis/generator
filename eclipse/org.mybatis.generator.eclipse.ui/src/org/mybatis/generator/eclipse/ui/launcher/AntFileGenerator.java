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
package org.mybatis.generator.eclipse.ui.launcher;

import static org.mybatis.generator.eclipse.ui.launcher.tabs.LauncherUtils.getTextOrBlank;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.eclipse.ui.launcher.tabs.LauncherUtils;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * This class generates an Ant file that can be used to run the generator from
 * the launcher.
 * 
 * @author Jeff Butler
 */
public class AntFileGenerator implements GeneratorLaunchConstants {

    private ILaunchConfiguration configuration;

    public AntFileGenerator(ILaunchConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getAntFileContent() {

        Document document = new Document();
        document.setRootElement(getProjectElement());

        return new DefaultXmlFormatter().getFormattedContent(document);
    }

    private XmlElement getProjectElement() {
        XmlElement projectElement = new XmlElement("project"); //$NON-NLS-1$

        projectElement.addAttribute(new Attribute("default", "generate")); //$NON-NLS-1$ //$NON-NLS-2$
        projectElement.addElement(getTargetElement());

        return projectElement;
    }

    private XmlElement getTargetElement() {
        XmlElement targetElement = new XmlElement("target"); //$NON-NLS-1$
        targetElement.addAttribute(new Attribute("name", "generate")); //$NON-NLS-1$ //$NON-NLS-2$

        addSqlTaskIfNecessary(targetElement);
        targetElement.addElement(getGenerateTask());

        return targetElement;
    }

    private void addSqlTaskIfNecessary(XmlElement parent) {

        String sqlFileFullPath = getSqlFile();
        if (sqlFileFullPath != null) {
            XmlElement sqlTask = new XmlElement("sql"); //$NON-NLS-1$

            sqlTask.addAttribute(new Attribute("driver", getTextOrBlank(configuration, ATTR_SQL_SCRIPT_DRIVER_CLASS))); //$NON-NLS-1$
            sqlTask.addAttribute(new Attribute("url", getTextOrBlank(configuration, ATTR_SQL_SCRIPT_CONNECTION_URL))); //$NON-NLS-1$
            sqlTask.addAttribute(new Attribute("userid", LauncherUtils.getUserId(configuration))); //$NON-NLS-1$
            sqlTask.addAttribute(new Attribute("password", LauncherUtils.getPassword(configuration))); //$NON-NLS-1$
            sqlTask.addAttribute(new Attribute("src", sqlFileFullPath)); //$NON-NLS-1$

            parent.addElement(sqlTask);
        }
    }

    private XmlElement getGenerateTask() {
        XmlElement generateTask = new XmlElement("mybatis.generate"); //$NON-NLS-1$
        generateTask.addAttribute(new Attribute("configfile", getConfigFile())); //$NON-NLS-1$
        setLoggingImplementation(generateTask);

        return generateTask;
    }

    private void setLoggingImplementation(XmlElement generateTask) {
        String implementation = getTextOrBlank(configuration, ATTR_LOGGING_IMPLEMENTATION);
        if (implementation.length() > 0) {
            generateTask.addAttribute(new Attribute("loggingImplementation", implementation));
        }
    }
    
    private String getConfigFile() {
        String configFileName = getTextOrBlank(configuration, ATTR_CONFIGURATION_FILE_NAME);

        try {
            String fullPath = VariablesPlugin.getDefault().getStringVariableManager()
                    .performStringSubstitution(configFileName);
            return fullPath;
        } catch (CoreException e) {
            return null;
        }
    }

    private String getSqlFile() {
        String sqlScriptFile = getTextOrBlank(configuration, ATTR_SQL_SCRIPT_FILE_NAME);
        if (!StringUtility.stringHasValue(sqlScriptFile)) {
            return null;
        }

        try {
            String fullPath = VariablesPlugin.getDefault().getStringVariableManager()
                    .performStringSubstitution(sqlScriptFile);
            return fullPath;
        } catch (CoreException e) {
            return null;
        }
    }
}
