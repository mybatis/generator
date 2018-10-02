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
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.eclipse.core.callback.EclipseProgressCallback;
import org.mybatis.generator.eclipse.core.callback.EclipseShellCallback;
import org.mybatis.generator.eclipse.ui.ant.logging.AntLogFactory;
import org.mybatis.generator.eclipse.ui.ant.logging.LogException;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.logging.LogFactory;

/**
 * @author Jeff Butler
 *
 */
public class GeneratorAntTask extends Task {
    private PropertySet propertyset;
    private String configfile;
    private String contextIds;
    private String fullyQualifiedTableNames;
    private String loggingImplementation;

    /**
     *  
     */
    public GeneratorAntTask() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        setLoggingImplementation();
        
        if (!StringUtility.stringHasValue(configfile)) {
            throw new BuildException("configfile is a required parameter");
        }

        List<String> warnings = new ArrayList<>();

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
        
        try {
            SubMonitor subMonitor = SubMonitor.convert(monitor, 1000);
            subMonitor.beginTask("Generating MyBatis Artifacts:", 1000);
            subMonitor.subTask("Parsing Configuration");
            
            Properties p = propertyset == null ? new Properties() : propertyset.getProperties();
            p.putAll(getProject().getUserProperties());
            
            ConfigurationParser cp = new ConfigurationParser(p,
                    warnings);
            Configuration config = cp
                    .parseConfiguration(configurationFile);

            subMonitor.worked(50);
            monitor.subTask("Generating Files from Database Tables");
            
            MyBatisGenerator generator = new MyBatisGenerator(config, new EclipseShellCallback(),
                    warnings);

            EclipseProgressCallback progressCallback = new EclipseProgressCallback(subMonitor.newChild(950));

            generator.generate(progressCallback, contexts, fullyqualifiedTables);

        } catch (XMLParserException e) {
            for (String error : e.getErrors()) {
                log(error, Project.MSG_ERR);
            }

            throw new BuildException(e.getMessage());
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        } catch (IOException e) {
            throw new BuildException(e.getMessage());
        } catch (InvalidConfigurationException e) {
            throw new BuildException(e.getMessage());
        } catch (InterruptedException e) {
            throw new BuildException("Cancelled by user");
        } finally {
            monitor.done();
        }

        for (String warning : warnings) {
            log("WARNING: " + warning, Project.MSG_WARN);
        }
    }

    private void setLoggingImplementation() {
        try {
            LogFactory.setLogFactory(new AntLogFactory(loggingImplementation));
        } catch (LogException e) {
            // this exception will only be thrown when a specific logger is selected
            LogFactory.forceNoLogging();
            log("WARNING: Logging Disabled.  Do you need to add a logging implementation to the launch classpath?", Project.MSG_WARN);
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

    public void setLoggingImplementation(String loggingImplementation) {
        this.loggingImplementation = loggingImplementation;
    }
}
