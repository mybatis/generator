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
package org.apache.ibatis.abator.ant;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.ibatis.abator.api.Abator;
import org.apache.ibatis.abator.config.AbatorConfiguration;
import org.apache.ibatis.abator.config.xml.AbatorConfigurationParser;
import org.apache.ibatis.abator.exception.InvalidConfigurationException;
import org.apache.ibatis.abator.exception.XMLParserException;
import org.apache.ibatis.abator.internal.DefaultShellCallback;
import org.apache.ibatis.abator.internal.util.StringUtility;
import org.apache.ibatis.abator.internal.util.messages.Messages;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.PropertySet;

/**
 * This is an Ant task that will run Abator.  The following is a sample Ant
 * script that shows how to run Abator from Ant:
 * 
 * <pre>
 *  &lt;project default="genfiles" basedir="."&gt;
 *    &lt;property name="generated.source.dir" value="${basedir}" /&gt;
 *    &lt;target name="genfiles" description="Generate the files"&gt;
 *      &lt;taskdef name="abator"
 *               classname="org.apache.ibatis.abator.ant.AbatorAntTask"
 *               classpath="abator.jar" /&gt;
 *      &lt;abator overwrite="true" configfile="abatorConfig.xml" verbose="false" &gt;
 *        &lt;propertyset&gt;
 *          &lt;propertyref name="generated.source.dir"/&gt;
 *        &lt;/propertyset&gt;
 *      &lt;/abator&gt;
 *    &lt;/target&gt;
 *  &lt;/project&gt;
 * </pre>
 * 
 * The task also supports these optional attributes:
 * <ul>
 *   <li>"contextIds" - a comma delimited list of contaxtIds to use
 *      for this run</li>
 *   <li>"fullyQualifiedTableNames" - a comma delimited list of 
 *     fully qualified table names to use for this run</li>
 * </ul>
 * 
 * 
 * @author Jeff Butler
 */
public class AbatorAntTask extends Task {
    
    private String configfile;
    private boolean overwrite;
    private PropertySet propertyset;
    private boolean verbose;
    private String contextIds;
    private String fullyQualifiedTableNames;

    /**
     * 
     */
    public AbatorAntTask() {
        super();
    }

    /* (non-Javadoc)
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() throws BuildException {
        if (!StringUtility.stringHasValue(configfile)) {
            throw new BuildException(Messages.getString("RuntimeError.0")); //$NON-NLS-1$
        }

        List warnings = new ArrayList();
        
        File configurationFile = new File(configfile);
        if (!configurationFile.exists()) {
            throw new BuildException(Messages.getString("RuntimeError.1", configfile)); //$NON-NLS-1$
        }
        
        Set fullyqualifiedTables = new HashSet();
        if (StringUtility.stringHasValue(fullyQualifiedTableNames)) {
            StringTokenizer st = new StringTokenizer(fullyQualifiedTableNames, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }
        
        Set contexts = new HashSet();
        if (StringUtility.stringHasValue(contextIds)) {
            StringTokenizer st = new StringTokenizer(contextIds, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contexts.add(s);
                }
            }
        }
        
        try {
            Properties p = propertyset == null ? null : propertyset.getProperties();
            
            AbatorConfigurationParser cp = new AbatorConfigurationParser(p,
                warnings);
            AbatorConfiguration config = cp.parseAbatorConfiguration(configurationFile);
            
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            
            Abator abator = new Abator(config, callback, warnings);
            
            abator.generate(new AntProgressCallback(this, verbose), contexts, fullyqualifiedTables);
            
        } catch (XMLParserException e) {
            Iterator errors = e.getErrors().iterator();
            while (errors.hasNext()) {
                log((String) errors.next(), Project.MSG_ERR);
            }
            
            throw new BuildException(e.getMessage());
        } catch (SQLException e) {
            throw new BuildException(e.getMessage());
        } catch (IOException e) {
            throw new BuildException(e.getMessage());
        } catch (InvalidConfigurationException e) {
            Iterator errors = e.getErrors().iterator();
            while (errors.hasNext()) {
                log((String) errors.next(), Project.MSG_ERR);
            }
            throw new BuildException(e.getMessage());
        } catch (InterruptedException e) {
            // ignore (will never happen with the DefaultShellCallback)
            ;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e.getMessage());
        }
        
        Iterator iter = warnings.iterator();
        while (iter.hasNext()) {
            log((String) iter.next(), Project.MSG_WARN);
        }
    }
    
    /**
     * @return Returns the configfile.
     */
    public String getConfigfile() {
        return configfile;
    }
    
    /**
     * @param configfile The configfile to set.
     */
    public void setConfigfile(String configfile) {
        this.configfile = configfile;
    }
    
    /**
     * @return Returns the overwrite.
     */
    public boolean isOverwrite() {
        return overwrite;
    }
    
    /**
     * @param overwrite The overwrite to set.
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public PropertySet createPropertyset() {
        if (propertyset == null) {
            propertyset = new PropertySet();
        }
        
        return propertyset;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
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
}
