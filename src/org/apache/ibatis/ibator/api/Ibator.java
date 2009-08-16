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
package org.apache.ibatis.ibator.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.ibator.config.IbatorConfiguration;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.config.MergeConstants;
import org.apache.ibatis.ibator.exception.InvalidConfigurationException;
import org.apache.ibatis.ibator.exception.ShellException;
import org.apache.ibatis.ibator.internal.DefaultShellCallback;
import org.apache.ibatis.ibator.internal.IbatorObjectFactory;
import org.apache.ibatis.ibator.internal.NullProgressCallback;
import org.apache.ibatis.ibator.internal.XmlFileMergerJaxp;
import org.apache.ibatis.ibator.internal.util.ClassloaderUtility;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * This class is the main interface to the Ibator for iBATIS code generator. A
 * typical execution of the tool involves these steps:
 * 
 * <ol>
 * <li>Create an IbatorConfiguration object. The IbatorConfiguration can
 * be the result of a parsing the XML configuration file, or it can be created
 * solely in Java.</li>
 * <li>Create an Ibator object</li>
 * <li>Call one of the generate() methods</li>
 * </ol>
 * 
 * @see org.apache.ibatis.ibator.config.xml.IbatorConfigurationParser
 * 
 * @author Jeff Butler
 */
public class Ibator {

    private IbatorConfiguration ibatorConfiguration;

    private ShellCallback shellCallback;

    private List<GeneratedJavaFile> generatedJavaFiles;

    private List<GeneratedXmlFile> generatedXmlFiles;

    private List<String> warnings;

    private Set<String> projects;

    /**
     * Constructs an Ibator object.
     * 
     * @param ibatorConfiguration The configuration for this run of Ibator
     * @param shellCallback an instance of a ShellCallback interface.  You may specify
     *    <code>null</code> in which case Ibator will use the DefaultShellCallback.
     * @param warnings Any warnings generated during execution will be added to this
     *            list. Warnings do not affect the running of the tool, but they
     *            may affect the results. A typical warning is an unsupported
     *            data type. In that case, the column will be ignored and
     *            generation will continue. Ibator will only add Strings to the
     *            list.  You may specify <code>null</code> if you do not
     *            want warnings returned.
     * @throws InvalidConfigurationException if the specified configuration
     *   is invalid
     */
    public Ibator(IbatorConfiguration ibatorConfiguration,
            ShellCallback shellCallback, List<String> warnings) throws InvalidConfigurationException {
        super();
        if (ibatorConfiguration == null) {
            throw new IllegalArgumentException(Messages.getString("RuntimeError.2")); //$NON-NLS-1$
        } else {
            this.ibatorConfiguration = ibatorConfiguration;
        }
        
        if (shellCallback == null) {
            this.shellCallback = new DefaultShellCallback(false);
        } else {
            this.shellCallback = shellCallback;
        }
        
        if (warnings == null) {
            this.warnings = new ArrayList<String>();
        } else {
            this.warnings = warnings;
        }
        generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
        generatedXmlFiles = new ArrayList<GeneratedXmlFile>();
        projects = new HashSet<String>();
        
        this.ibatorConfiguration.validate();
    }

    /**
     * This is the main method for generating code.  This method is long running, but
     * progress can be provided and the method can be canceled through the ProgressCallback
     * interface.  This version of the method runs all configured contexts.
     * 
     * @param callback an instance of the ProgressCallback interface, or <code>null</code>
     *   if you do not require progress information
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException if the method is canceled through the ProgressCallback
     */
    public void generate(ProgressCallback callback)
            throws SQLException, IOException, InterruptedException {
        generate(callback, null, null);
    }
    
    /**
     * This is the main method for generating code.  This method is long running, but
     * progress can be provided and the method can be canceled through the ProgressCallback
     * interface.
     * 
     * @param callback an instance of the ProgressCallback interface, or <code>null</code>
     *   if you do not require progress information
     * @param contextIds a set of Strings containing context ids to run.  Only the
     *   contexts with an id specified in this list will be run.  If the list is
     *   null or empty, than all contexts are run.
     * @throws InvalidConfigurationException
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException if the method is canceled through the ProgressCallback
     */
    public void generate(ProgressCallback callback, Set<String> contextIds)
            throws SQLException, IOException, InterruptedException {
        generate(callback, contextIds, null);
    }
    
    /**
     * This is the main method for generating code.  This method is long running, but
     * progress can be provided and the method can be cancelled through the ProgressCallback
     * interface.
     * 
     * @param callback an instance of the ProgressCallback interface, or <code>null</code>
     *   if you do not require progress information
     * @param contextIds a set of Strings containing context ids to run.  Only the
     *   contexts with an id specified in this list will be run.  If the list is
     *   null or empty, than all contexts are run.
     * @param fullyQualifiedTableNames a set of table names to generate.  The elements
     *   of the set must be Strings that exactly match what's specified in the configuration.
     *   For example, if table name = "foo" and schema = "bar", then the fully qualified
     *   table name is "foo.bar".
     *   If the Set is null or empty, then all tables in the configuration will be
     *   used for code generation.
     * @throws InvalidConfigurationException
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException if the method is canceled through the ProgressCallback
     */
    public void generate(ProgressCallback callback, Set<String> contextIds, Set<String> fullyQualifiedTableNames)
            throws SQLException, IOException, InterruptedException {

        if (callback == null) {
            callback = new NullProgressCallback();
        }

        generatedJavaFiles.clear();
        generatedXmlFiles.clear();
        
        // calculate the contexts to run
        List<IbatorContext> contextsToRun;
        if (contextIds == null || contextIds.size() == 0) {
            contextsToRun = ibatorConfiguration.getIbatorContexts();
        } else {
            contextsToRun = new ArrayList<IbatorContext>();
            for (IbatorContext ibatorContext : ibatorConfiguration.getIbatorContexts()) {
                if (contextIds.contains(ibatorContext.getId())) {
                    contextsToRun.add(ibatorContext);
                }
            }
        }

        // setup custom classloader if required
        if (ibatorConfiguration.getClassPathEntries().size() > 0) {
            ClassLoader classLoader = 
                ClassloaderUtility.getCustomClassloader(ibatorConfiguration.getClassPathEntries());
            IbatorObjectFactory.setExternalClassLoader(classLoader);
        }

        // now run the introspections...
        int totalSteps = 0;
        for (IbatorContext ibatorContext : contextsToRun) {
            totalSteps += ibatorContext.getIntrospectionSteps();
        }
        callback.introspectionStarted(totalSteps);
        
        for (IbatorContext ibatorContext : contextsToRun) {
            ibatorContext.introspectTables(callback, warnings, fullyQualifiedTableNames);
        }
        
        // now run the generates
        totalSteps = 0;
        for (IbatorContext ibatorContext : contextsToRun) {
            totalSteps += ibatorContext.getGenerationSteps();
        }
        callback.generationStarted(totalSteps);
        
        for (IbatorContext ibatorContext : contextsToRun) {
            ibatorContext.generateFiles(callback, generatedJavaFiles, generatedXmlFiles, warnings);
        }
        
        // now save the files
        callback.saveStarted(generatedXmlFiles.size() + generatedJavaFiles.size());
        
        for (GeneratedXmlFile gxf : generatedXmlFiles) {
            projects.add(gxf.getTargetProject());

            File targetFile;
            String source;
            try {
                File directory = shellCallback.getDirectory(gxf
                        .getTargetProject(), gxf.getTargetPackage());
                targetFile = new File(directory, gxf.getFileName());
                if (targetFile.exists()) {
                    if (gxf.isMergeable()) {
                        source = XmlFileMergerJaxp.getMergedSource(gxf, targetFile);
                    } else {
                        source = gxf.getFormattedContent();
                        targetFile = getUniqueFileName(directory, gxf.getFileName());
                        warnings.add(Messages.getString("Warning.2", targetFile.getAbsolutePath())); //$NON-NLS-1$
                    }
                } else {
                    source = gxf.getFormattedContent();
                }
            } catch (ShellException e) {
                warnings.add(e.getMessage());
                continue;
            }

            callback.checkCancel();
            callback.startTask(Messages.getString("Progress.15", targetFile.getName())); //$NON-NLS-1$
            writeFile(targetFile, source);
        }

        for (GeneratedJavaFile gjf : generatedJavaFiles) {
            projects.add(gjf.getTargetProject());

            File targetFile;
            String source;
            try {
                File directory = shellCallback.getDirectory(gjf
                        .getTargetProject(), gjf.getTargetPackage());
                targetFile = new File(directory, gjf.getFileName());
                if (targetFile.exists()) {
                    if (shellCallback.isMergeSupported()) {
                        source = shellCallback.mergeJavaFile(gjf.getFormattedContent(),
                                targetFile.getAbsolutePath(),
                                MergeConstants.OLD_JAVA_ELEMENT_TAGS);
                    } else if (shellCallback.isOverwriteEnabled()) { 
                        source = gjf.getFormattedContent();
                        warnings.add(Messages.getString("Warning.11", //$NON-NLS-1$
                                targetFile.getAbsolutePath()));
                    } else {
                        source = gjf.getFormattedContent();
                        targetFile = getUniqueFileName(directory, gjf.getFileName());
                        warnings.add(Messages.getString("Warning.2", targetFile.getAbsolutePath())); //$NON-NLS-1$
                    }
                } else {
                    source = gjf.getFormattedContent();
                }

                callback.checkCancel();
                callback.startTask(Messages.getString("Progress.15", targetFile.getName())); //$NON-NLS-1$
                writeFile(targetFile, source);
            } catch (ShellException e) {
                warnings.add(e.getMessage());
            }
        }
        
        for (String project : projects) {
            shellCallback.refreshProject(project);
        }

        callback.done();
    }

    /**
     * Writes, or overwrites, the contents of the specified file
     * 
     * @param file
     * @param content
     */
    private void writeFile(File file, String content) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
        bw.write(content);
        bw.close();
    }
    
    private File getUniqueFileName(File directory, String fileName) {
        File answer = null;
        
        // try up to 1000 times to generate a unique file name
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 1000; i++) {
            sb.setLength(0);
            sb.append(fileName);
            sb.append('.');
            sb.append(i);
            
            File testFile = new File(directory, sb.toString());
            if (!testFile.exists()) {
                answer = testFile;
                break;
            }
        }
        
        if (answer == null) {
            throw new RuntimeException(Messages.getString("RuntimeError.3", directory.getAbsolutePath())); //$NON-NLS-1$
        }
        
        return answer;
    }
}
