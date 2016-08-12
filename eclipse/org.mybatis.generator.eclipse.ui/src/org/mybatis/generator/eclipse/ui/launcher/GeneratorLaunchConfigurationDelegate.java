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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Project;
import org.eclipse.ant.core.AntCorePlugin;
import org.eclipse.ant.core.AntCorePreferences;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.ant.core.IAntClasspathEntry;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.mybatis.generator.eclipse.ui.Activator;
import org.mybatis.generator.eclipse.ui.Messages;
import org.mybatis.generator.eclipse.ui.launcher.tabs.LauncherUtils;

/**
 * This launcher works by invoking the AntRunner on a generated ant script for the generator.
 * It turns out that calculating the classpath for running a plain Java launch configuration
 * is an incredible pain when we need to include Eclipse dependencies, and then we still
 * have the problem that the Java launch would need to be able run in the same JRE as the
 * workspace so that it could resolve Eclipse projects and directories.  In the end, it is
 * far simpler to reuse the built in Ant support which handles most of those issues
 * automatically.
 * 
 * @author Jeff Butler
 *
 */
public class GeneratorLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate {

    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
            throws CoreException {
        AntRunner antRunner = new AntRunner();
        
        String buildFile;
        try {
            buildFile = generateAntScript(configuration);
        } catch (IOException e) {
            Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, Messages.LAUNCH_ERROR_ERROR_GENERATING_ANT_FILE, e);
            throw new CoreException(status);
        }

        antRunner.setBuildFileLocation(buildFile);
        modifyAntClasspathIfNecessary(configuration, antRunner);
        if (ILaunchManager.DEBUG_MODE.equals(mode)) {
            antRunner.setMessageOutputLevel(Project.MSG_DEBUG);
            antRunner.setArguments("-debug"); //$NON-NLS-1$
            antRunner.addBuildListener("org.mybatis.generator.eclipse.ui.ant.DebugBuildListener"); //$NON-NLS-1$
        }
        
        antRunner.run(monitor);
        
        if (LauncherUtils.getBooleanOrFalse(configuration, GeneratorLaunchConstants.ATTR_SQL_SCRIPT_SECURE_CREDENTIALS)) {
            File file = new File(buildFile);
            file.delete();
        }
    }
    
    private void modifyAntClasspathIfNecessary(ILaunchConfiguration configuration, AntRunner antRunner) throws CoreException {
        String[] classpathEntries = getClasspath(configuration);
        if (classpathEntries == null || classpathEntries.length == 0) {
            return;
        }
        
        List<URL> classpathURLs = new ArrayList<URL>();

        // this is a hack determined by looking at the source for 
        // org.eclipse.ant.internal.core.ant.InternalAntRunner
        //
        // we may need to add JARs to the classpath for this runner.  If we don't
        // specify a custom classpath, then the classpath is 
        //   getAntHomeClasspathEntries() +
        //   getAdditionalClasspathEntries() +
        //   getExtraClasspathURLs()
        //
        // if we do specify a custom classpath, it is only our specified
        // classpath + getExtraClasspathURLs()
        //
        // So we will build the first two ourselves, then add our extra JARs
        //
        AntCorePreferences antCorePreferences = AntCorePlugin.getPlugin().getPreferences();
        
        for (IAntClasspathEntry entry : antCorePreferences.getAntHomeClasspathEntries()) {
            classpathURLs.add(entry.getEntryURL());
        }
        
        for (IAntClasspathEntry entry : antCorePreferences.getAdditionalClasspathEntries()) {
            classpathURLs.add(entry.getEntryURL());
        }
        
        for (String classPathEntry : classpathEntries) {
            File f = new File (classPathEntry);
            try {
                classpathURLs.add(f.toURI().toURL());
            } catch (MalformedURLException e) {
            }
        }

        antRunner.setCustomClasspath(classpathURLs.toArray(new URL[classpathURLs.size()]));
    }

    /**
     * Generates and writes an Ant file for the build.
     * 
     * @param configuration
     * @return the full path of the generated file
     * @throws IOException
     */
    private String generateAntScript(ILaunchConfiguration configuration) throws IOException {
        IPath ipath = Activator.getDefault().getStateLocation().append(".generatedAntScripts"); //$NON-NLS-1$
        File file = ipath.toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        file = ipath.append(configuration.getName() + ".xml").toFile(); //$NON-NLS-1$

        AntFileGenerator antFileGenerator = new AntFileGenerator(configuration);
        
        writeFile(file, antFileGenerator.getAntFileContent());
        
        return file.getAbsolutePath();
    }

    private void writeFile(File file, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, false);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); //$NON-NLS-1$
        
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(content);
        bw.close();
    }
}
