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
package org.mybatis.generator.maven;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.ClassloaderUtility;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;
import org.mybatis.generator.logging.LogFactory;

/**
 * Goal which generates MyBatis/iBATIS artifacts.
 */
@Mojo(name = "generate",defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MyBatisGeneratorMojo extends AbstractMojo {

    /**
     * Maven Project.
     *
     */
    @Parameter(property="project",required=true,readonly=true)
    private MavenProject project;

    /**
     * Output Directory.
     */
    @Parameter(property="mybatis.generator.outputDirectory", defaultValue="${project.build.directory}/generated-sources/mybatis-generator", required=true)
    private File outputDirectory;

    /**
     * Location of the configuration file.
     */
    @Parameter(property="mybatis.generator.configurationFile",defaultValue="${project.basedir}/src/main/resources/generatorConfig.xml", required=true)
    private File configurationFile;

    /**
     * Specifies whether the mojo writes progress messages to the log.
     */
    @Parameter(property="mybatis.generator.verbose", defaultValue="false")
    private boolean verbose;

    /**
     * Specifies whether the mojo overwrites existing files. Default is false.
     */
    @Parameter(property="mybatis.generator.overwrite", defaultValue="false")
    private boolean overwrite;

    /**
     * Location of a SQL script file to run before generating code. If null,
     * then no script will be run. If not null, then jdbcDriver, jdbcURL must be
     * supplied also, and jdbcUserId and jdbcPassword may be supplied.
     */
    @Parameter(property="mybatis.generator.sqlScript")
    private String sqlScript;

    /**
     * JDBC Driver to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcDriver")
    private String jdbcDriver;

    /**
     * JDBC URL to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcURL")
    private String jdbcURL;

    /**
     * JDBC user ID to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcUserId")
    private String jdbcUserId;

    /**
     * JDBC password to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcPassword")
    private String jdbcPassword;

    /**
     * Comma delimited list of table names to generate.
     */
    @Parameter(property="mybatis.generator.tableNames")
    private String tableNames;

    /**
     * Comma delimited list of contexts to generate.
     */
    @Parameter(property="mybatis.generator.contexts")
    private String contexts;

    /**
     * Skip generator.
     */
    @Parameter(property="mybatis.generator.skip", defaultValue="false")
    private boolean skip;

    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info( "MyBatis generator is skipped." );
            return;
        }

    	LogFactory.setLogFactory(new MavenLogFactory(this));

    	// add resource directories to the classpath.  This is required to support
        // use of a properties file in the build.  Typically, the properties file
        // is in the project's source tree, but the plugin classpath does not
        // include the project classpath.
        List<Resource> resources = project.getResources();
        List<String> resourceDirectories = new ArrayList<String>();
        for (Resource resource: resources) {
            resourceDirectories.add(resource.getDirectory());
        }
        ClassLoader cl = ClassloaderUtility.getCustomClassloader(resourceDirectories);
        ObjectFactory.addResourceClassLoader(cl);

        if (configurationFile == null) {
            throw new MojoExecutionException(
                    Messages.getString("RuntimeError.0")); //$NON-NLS-1$
        }

        List<String> warnings = new ArrayList<String>();

        if (!configurationFile.exists()) {
            throw new MojoExecutionException(Messages.getString(
                    "RuntimeError.1", configurationFile.toString())); //$NON-NLS-1$
        }

        runScriptIfNecessary();

        Set<String> fullyqualifiedTables = new HashSet<String>();
        if (StringUtility.stringHasValue(tableNames)) {
            StringTokenizer st = new StringTokenizer(tableNames, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }

        Set<String> contextsToRun = new HashSet<String>();
        if (StringUtility.stringHasValue(contexts)) {
            StringTokenizer st = new StringTokenizer(contexts, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contextsToRun.add(s);
                }
            }
        }

        try {
            ConfigurationParser cp = new ConfigurationParser(
                    project.getProperties(), warnings);
            Configuration config = cp.parseConfiguration(configurationFile);

            ShellCallback callback = new MavenShellCallback(this, overwrite);

            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                    callback, warnings);

            myBatisGenerator.generate(new MavenProgressCallback(getLog(),
                    verbose), contextsToRun, fullyqualifiedTables);

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
        }

        for (String error : warnings) {
            getLog().warn(error);
        }

        if (project != null && outputDirectory != null
                && outputDirectory.exists()) {
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

        SqlScriptRunner scriptRunner = new SqlScriptRunner(sqlScript,
                jdbcDriver, jdbcURL, jdbcUserId, jdbcPassword);
        scriptRunner.setLog(getLog());
        scriptRunner.executeScript();
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
