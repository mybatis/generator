/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package org.mybatis.generator.eclipse.ui.ant;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.PropertySet;
import org.eclipse.ant.core.AntCorePlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.mybatis.generator.api.IndentType;
import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.eclipse.ui.callbacks.EclipseProgressCallback;
import org.mybatis.generator.eclipse.ui.callbacks.EclipseShellCallback;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author Jeff Butler
 *
 */
public class GeneratorAntTask extends Task {
    private PropertySet propertyset;
    private String configfile;
    private String contextIds;
    private String fullyQualifiedTableNames;

    /**
     *  
     */
    public GeneratorAntTask() {
        super();
    }

    @Override
    public void execute() throws BuildException {
        
        if (!StringUtility.stringHasValue(configfile)) {
            throw new BuildException("configfile is a required parameter");
        }

        File configurationFile = new File(configfile);
        if (!configurationFile.exists()) {
            throw new BuildException("configfile " + configfile
                    + " does not exist");
        }

        Set<String> fullyqualifiedTables = new HashSet<>();
        if (StringUtility.stringHasValue(fullyQualifiedTableNames)) {
            StringTokenizer st = new StringTokenizer(fullyQualifiedTableNames, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }
        
        Set<String> contexts = new HashSet<>();
        if (StringUtility.stringHasValue(contextIds)) {
            StringTokenizer st = new StringTokenizer(contextIds, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contexts.add(s);
                }
            }
        }
        
        IProgressMonitor monitor = (IProgressMonitor) getProject()
            .getReferences()
            .get(AntCorePlugin.ECLIPSE_PROGRESS_MONITOR);
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        
        List<String> warnings = new ArrayList<>();

        try {
            SubMonitor subMonitor = SubMonitor.convert(monitor, 1000);
            subMonitor.beginTask("Generating MyBatis Artifacts:", 1000);
            subMonitor.subTask("Parsing Configuration");
            
            Properties p = propertyset == null ? new Properties() : propertyset.getProperties();
            p.putAll(getProject().getUserProperties());
            
            ConfigurationParser cp = new ConfigurationParser(p);
            Configuration config = cp.parseConfiguration(configurationFile);
            warnings.addAll(cp.getWarnings());

            subMonitor.worked(50);
            monitor.subTask("Generating Files from Database Tables");
            
            EclipseProgressCallback progressCallback = new EclipseProgressCallback(subMonitor.newChild(950));

            MyBatisGenerator generator = new MyBatisGenerator.Builder()
            		.withConfiguration(config)
            		.withShellCallback(new EclipseShellCallback())
            		.withProgressCallback(progressCallback)
            		.withContextIds(contexts)
            		.withFullyQualifiedTableNames(fullyqualifiedTables)
            		.withJavaFileMergeEnabled(true)
            		.withOverwriteEnabled(false)
            		.withIndenter(calculateIndenter())
            		.build();

            warnings.addAll(generator.generateAndWrite());

        } catch (XMLParserException e) {
            for (String error : e.getExtraMessages()) {
                log(error, Project.MSG_ERR);
            }

            throw new BuildException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new BuildException(e.getMessage(), e);
        } catch (IOException e) {
            throw new BuildException(e.getMessage(), e);
        } catch (InvalidConfigurationException e) {
            for (String error : e.getExtraMessages()) {
                log(error, Project.MSG_ERR);
            }

            throw new BuildException(e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new BuildException("Cancelled by user");
        } finally {
            monitor.done();
        }

        for (String warning : warnings) {
            log("WARNING: " + warning, Project.MSG_WARN);
        }
    }

    /**
     * @return Returns the configfile.
     */
    public String getConfigfile() {
        return configfile;
    }

    /**
     * @param configfile
     *            The configfile to set.
     */
    public void setConfigfile(String configfile) {
        this.configfile = configfile;
    }

    public PropertySet createPropertyset() {
        if (propertyset == null) {
            propertyset = new PropertySet();
        }
        
        return propertyset;
    }

    public String getContextIds() {
        return contextIds;
    }

    public void setContextIds(String contextIds) {
        this.contextIds = contextIds;
    }

    public String getFullyQualifiedTableNames() {
        return fullyQualifiedTableNames;
    }

    public void setFullyQualifiedTableNames(String fullyQualifiedTableNames) {
        this.fullyQualifiedTableNames = fullyQualifiedTableNames;
    }
    
    private Indenter calculateIndenter() {
        Indenter.Builder builder = new Indenter.Builder();
        
        // TAB, SPACE, MIXED
        String javaTabCharacter = JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR);
        if ("tab".equalsIgnoreCase(javaTabCharacter)) { //$NON-NLS-1$
            builder.withJavaIndentType(IndentType.TABS).build();
        } else {
            // String tabSize =  JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE);

            // only used in "mixed" or "spaces" - number of characters that represent a level.
            // "mixed" means uses spaces if the indent level is less than the indentation size.
            // so if tabSize is 8 and indentation size is 4, then spaces are used for first level, tabs for second level, etc.
            // we don't have a match for this, so if it is mixed, we will use spaces
            String indentationSize = JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_INDENTATION_SIZE);
            builder.withJavaIndentAmount(Integer.valueOf(indentationSize)).build();
        }
        
        IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode("org.eclipse.wst.xml.core"); //$NON-NLS-1$
        // eclipse defaults are 1 tab character. If nothing is set, that's what is in use
        String xmlIndentationCharacter = preferences.get("indentationChar", "tab"); //$NON-NLS-1$ //$NON-NLS-2$
        if ("tab".equalsIgnoreCase(xmlIndentationCharacter)) {
            builder.withXmlIndentType(IndentType.TABS);
        }
        
        String xmlIndentationSize = preferences.get("indentationSize", "1"); //$NON-NLS-1$ //$NON-NLS-2$
        builder.withXmlIndentAmount(Integer.valueOf(xmlIndentationSize));
        
        return builder.build();
        
    }
}
