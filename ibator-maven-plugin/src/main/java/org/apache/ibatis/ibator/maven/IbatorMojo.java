/*
 * Copyright 2009 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.ibator.maven;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.ibator.api.Ibator;
import org.apache.ibatis.ibator.api.ShellCallback;
import org.apache.ibatis.ibator.config.IbatorConfiguration;
import org.apache.ibatis.ibator.config.xml.IbatorConfigurationParser;
import org.apache.ibatis.ibator.exception.InvalidConfigurationException;
import org.apache.ibatis.ibator.exception.XMLParserException;
import org.apache.ibatis.ibator.internal.util.messages.Messages;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which generates iBATIS artifacts.
 * 
 * @goal generate
 * @phase generate-sources
 */
public class IbatorMojo extends AbstractMojo {
    
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     * 
     */
    private MavenProject project;
    
    /**
     * @parameter expression="${ibator.outputDirectory}" 
     *             default-value="${project.build.directory}/generated-sources/ibator"
     * @required
     */
    private File outputDirectory;
    
    /**
     * Location of the configuration file.
     * 
     * @parameter expression="${ibator.configurationFile}"
     *             default-value="${basedir}/src/main/resources/ibatorConfig.xml"
     * @required
     */
    private File configurationFile;
    
    /**
     * Specifies whether the mojo writes progress messages to the log
     * 
     * @parameter expression="${ibator.verbose}" default-value=false
     */
    private boolean verbose;

    /**
     * Specifies whether the mojo overwrites existing files.  Default is false.
     * 
     * @parameter expression="${ibator.overwrite}" default-value=false
     */
    private boolean overwrite;
    
    /**
     * Location of a SQL script file to run before generating code.
     * If null, then no script will be run.  If not null,
     * then jdbc.driver, jdbc.url must be supplies also,
     * and jdbc.user.id and jdbc.password may be supplied.
     * 
     * @parameter expression="${ibator.sqlScript}"
     */
    private File sqlScript;
    
    /**
     * JDBC Driver to use if a sql.script.file is specified
     * 
     * @parameter expression="${ibator.jdbcDriver}"
     */
    private String jdbcDriver;
    
    /**
     * JDBC URL to use if a sql.script.file is specified
     * 
     * @parameter expression="${ibator.jdbcURL}"
     */
    private String jdbcURL;
    
    /**
     * JDBC user ID to use if a sql.script.file is specified
     * 
     * @parameter expression="${ibator.jdbcUserId}"
     */
    private String jdbcUserId;
    
    /**
     * JDBC password to use if a sql.script.file is specified
     * 
     * @parameter expression="${ibator.jdbcPassword}"
     */
    private String jdbcPassword;
    
    public void execute() throws MojoExecutionException {
        if (configurationFile == null) {
            throw new MojoExecutionException(Messages.getString("RuntimeError.0")); //$NON-NLS-1$
        }

        List<String> warnings = new ArrayList<String>();

        if (!configurationFile.exists()) {
            throw new MojoExecutionException(Messages.getString(
                    "RuntimeError.1", configurationFile.toString())); //$NON-NLS-1$
        }
        
        runScriptIfNecessary();

        Set<String> fullyqualifiedTables = new HashSet<String>();
        // TODO...
//        if (StringUtility.stringHasValue(fullyQualifiedTableNames)) {
//            StringTokenizer st = new StringTokenizer(fullyQualifiedTableNames,
//                    ","); //$NON-NLS-1$
//            while (st.hasMoreTokens()) {
//                String s = st.nextToken().trim();
//                if (s.length() > 0) {
//                    fullyqualifiedTables.add(s);
//                }
//            }
//        }

        Set<String> contexts = new HashSet<String>();
        // TODO...
//        if (StringUtility.stringHasValue(contextIds)) {
//            StringTokenizer st = new StringTokenizer(contextIds, ","); //$NON-NLS-1$
//            while (st.hasMoreTokens()) {
//                String s = st.nextToken().trim();
//                if (s.length() > 0) {
//                    contexts.add(s);
//                }
//            }
//        }

        try {
            IbatorConfigurationParser cp = new IbatorConfigurationParser(project.getProperties(), warnings);
            IbatorConfiguration config = cp
                    .parseIbatorConfiguration(configurationFile);

            ShellCallback callback = new MavenShellCallback(this, overwrite);

            Ibator ibator = new Ibator(config, callback, warnings);

            ibator.generate(new MavenProgressCallback(getLog(), verbose), contexts,
                    fullyqualifiedTables);

        } catch (XMLParserException e) {
            for (String error : e.getErrors()) {
                getLog().error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (SQLException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (InvalidConfigurationException e) {
            for (String error : e.getErrors()) {
                getLog().error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (InterruptedException e) {
            // ignore (will never happen with the DefaultShellCallback)
            ;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }

        for (String error : warnings) {
            getLog().warn(error);
        }
        
        if (project != null && outputDirectory != null && outputDirectory.exists()) {
            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
            
            Resource resource = new Resource();
            resource.setDirectory(outputDirectory.getAbsolutePath());
            resource.addInclude("**/*.xml");
            project.addResource(resource);
        }
    }
    
    private void runScriptIfNecessary() throws MojoExecutionException {
        if (sqlScript == null) {
            return;
        }
        
        SqlScriptRunner scriptRunner = new SqlScriptRunner(sqlScript, jdbcDriver, jdbcURL, jdbcUserId, jdbcPassword);
        scriptRunner.setLog(getLog());
        scriptRunner.executeScript();
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
