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
 */
package org.mybatis.generator.api;

import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.codegen.ContextResults;
import org.mybatis.generator.codegen.GenerationEngine;
import org.mybatis.generator.codegen.IntrospectionEngine;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.XmlFileMergerJaxp;

/**
 * This class is the main interface to MyBatis generator. A typical execution of the tool involves these steps:
 * <ol>
 * <li>Create a Configuration object. The Configuration can be the result of a parsing the XML configuration file, or it
 * can be created solely in Java.</li>
 * <li>Create a MyBatisGenerator object</li>
 * <li>Call one of the generate() methods</li>
 * </ol>
 *
 * @author Jeff Butler
 *
 * @see org.mybatis.generator.config.xml.ConfigurationParser
 */
public class MyBatisGenerator {
    private static final ProgressCallback NULL_PROGRESS_CALLBACK = new ProgressCallback() { };
    private final Configuration configuration;
    private final ShellCallback shellCallback;
    private final List<String> warnings;
    private final Set<String> projects = new HashSet<>();
    private final List<ContextResults> contextResultList = new ArrayList<>();

    /**
     * Constructs a MyBatisGenerator object.
     *
     * @param configuration
     *            The configuration for this invocation
     * @param shellCallback
     *            an instance of a ShellCallback interface. You may specify
     *            <code>null</code> in which case the DefaultShellCallback will
     *            be used.
     * @param warnings
     *            Any warnings generated during execution will be added to this
     *            list. Warnings do not affect the running of the tool, but they
     *            may affect the results. A typical warning is an unsupported
     *            data type. In that case, the column will be ignored and
     *            generation will continue. You may specify <code>null</code> if
     *            you do not want warnings returned.
     * @throws InvalidConfigurationException
     *             if the specified configuration is invalid
     */
    public MyBatisGenerator(Configuration configuration, @Nullable ShellCallback shellCallback,
            @Nullable List<String> warnings) throws InvalidConfigurationException {
        if (configuration == null) {
            throw new IllegalArgumentException(getString("RuntimeError.2")); //$NON-NLS-1$
        } else {
            this.configuration = configuration;
        }

        this.shellCallback = Objects.requireNonNullElseGet(shellCallback, () -> new DefaultShellCallback(false));

        this.warnings = Objects.requireNonNullElseGet(warnings, ArrayList::new);

        this.configuration.validate();
    }

    /**
     * This is the main method for generating code. This method is long-running, but progress can be provided and the
     * method can be canceled through the ProgressCallback interface. This version of the method runs all configured
     * contexts.
     *
     * @param callback
     *            an instance of the ProgressCallback interface, or <code>null</code> if you do not require progress
     *            information
     * @throws SQLException
     *             the SQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *             if the method is canceled through the ProgressCallback
     */
    public void generate(ProgressCallback callback) throws SQLException,
            IOException, InterruptedException {
        generate(callback, null, null, true);
    }

    /**
     * This is the main method for generating code. This method is long-running, but progress can be provided and the
     * method can be canceled through the ProgressCallback interface.
     *
     * @param callback
     *            an instance of the ProgressCallback interface, or <code>null</code> if you do not require progress
     *            information
     * @param contextIds
     *            a set of Strings containing context ids to run. Only the contexts with an id specified in this list
     *            will be run. If the list is null or empty, then all contexts are run.
     * @throws SQLException
     *             the SQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *             if the method is canceled through the ProgressCallback
     */
    public void generate(ProgressCallback callback, Set<String> contextIds)
            throws SQLException, IOException, InterruptedException {
        generate(callback, contextIds, null, true);
    }

    /**
     * This is the main method for generating code. This method is long-running, but progress can be provided and the
     * method can be cancelled through the ProgressCallback interface.
     *
     * @param callback
     *            an instance of the ProgressCallback interface, or <code>null</code> if you do not require progress
     *            information
     * @param contextIds
     *            a set of Strings containing context ids to run. Only the contexts with an id specified in this list
     *            will be run. If the list is null or empty, then all contexts are run.
     * @param fullyQualifiedTableNames
     *            a set of table names to generate. The elements of the set must be Strings that exactly match what's
     *            specified in the configuration. For example, if table name = "foo" and schema = "bar", then the fully
     *            qualified table name is "foo.bar". If the Set is null or empty, then all tables in the configuration
     *            will be used for code generation.
     * @throws SQLException
     *             the SQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *             if the method is canceled through the ProgressCallback
     */
    public void generate(@Nullable ProgressCallback callback, @Nullable Set<String> contextIds,
            @Nullable Set<String> fullyQualifiedTableNames) throws SQLException,
            IOException, InterruptedException {
        generate(callback, contextIds, fullyQualifiedTableNames, true);
    }

    /**
     * This is the main method for generating code. This method is long-running, but progress can be provided and the
     * method can be cancelled through the ProgressCallback interface.
     *
     * @param callback
     *            an instance of the ProgressCallback interface, or <code>null</code> if you do not require progress
     *            information
     * @param contextIds
     *            a set of Strings containing context ids to run. Only the contexts with an id specified in this list
     *            will be run. If the list is null or empty, then all contexts are run.
     * @param fullyQualifiedTableNames
     *            a set of table names to generate. The elements of the set must be Strings that exactly match what's
     *            specified in the configuration. For example, if table name = "foo" and schema = "bar", then the fully
     *            qualified table name is "foo.bar". If the Set is null or empty, then all tables in the configuration
     *            will be used for code generation.
     * @param writeFiles
     *            if true, then the generated files will be written to disk.  If false,
     *            then the generator runs but nothing is written
     * @throws SQLException
     *             the SQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *             if the method is canceled through the ProgressCallback
     */
    public void generate(@Nullable ProgressCallback callback, @Nullable Set<String> contextIds,
                         @Nullable Set<String> fullyQualifiedTableNames, boolean writeFiles) throws SQLException,
            IOException, InterruptedException {

        if (callback == null) {
            callback = NULL_PROGRESS_CALLBACK;
        }

        contextResultList.clear();
        ObjectFactory.reset();
        RootClassInfo.reset();

        // calculate the contexts to run
        List<Context> contextsToRun;
        if (contextIds == null || contextIds.isEmpty()) {
            contextsToRun = configuration.getContexts();
        } else {
            contextsToRun = new ArrayList<>();
            for (Context context : configuration.getContexts()) {
                if (contextIds.contains(context.getId())) {
                    contextsToRun.add(context);
                }
            }
        }

        // setup custom classloader if required
        if (!configuration.getClassPathEntries().isEmpty()) {
            ClassLoader classLoader = getCustomClassloader(configuration.getClassPathEntries());
            ObjectFactory.addExternalClassLoader(classLoader);
        }

        // now run the introspections...
        int totalSteps = 0;
        for (Context context : contextsToRun) {
            totalSteps += context.getIntrospectionSteps();
        }
        callback.introspectionStarted(totalSteps);

        for (Context context : contextsToRun) {
            ContextResults contextResults = new ContextResults.Builder()
                    .withContext(context)
                    .withWarnings(warnings)
                    .build();
            this.contextResultList.add(contextResults);

            List<IntrospectedTable> introspectedTables = new IntrospectionEngine.Builder()
                    .withContext(context)
                    .withFullyQualifiedTableNames(
                            fullyQualifiedTableNames == null ? Collections.emptySet() : fullyQualifiedTableNames)
                    .withWarnings(warnings)
                    .withProgressCallback(callback)
                    .build()
                    .introspectTables(contextResults.knownRuntime(), contextResults.pluginAggregator());

            contextResults.addIntrospectedTables(introspectedTables);
        }

        // now run the generates
        totalSteps = 0;
        for (ContextResults contextResults : this.contextResultList) {
            totalSteps += contextResults.getGenerationSteps();
        }
        callback.generationStarted(totalSteps);

        for (ContextResults contextResults : this.contextResultList) {
            var generationEngine = new GenerationEngine.Builder()
                    .withContext(contextResults.context())
                    .withProgressCallback(callback)
                    .withWarnings(warnings)
                    .withIntrospectedTables(contextResults.introspectedTables())
                    .withCommentGenerator(contextResults.commentGenerator())
                    .withPluginAggregator(contextResults.pluginAggregator())
                    .withRuntimeBuilderClassName(contextResults.runtimeBuilderClassName())
                    .build();

            contextResults.addGeneratedJavaFiles(generationEngine.generateJavaFiles());
            contextResults.addGeneratedXmlFiles(generationEngine.generateXmlFiles());
            contextResults.addGeneratedKotlinFiles(generationEngine.generateKotlinFiles());
            contextResults.addGeneratedGenericFiles(generationEngine.generateGenericFiles());
        }

        // now save the files
        if (writeFiles) {
            totalSteps = 0;
            for (ContextResults contextResults : this.contextResultList) {
                totalSteps += contextResults.getNumberOfGeneratedFiles();
            }
            callback.saveStarted(totalSteps);

            for (ContextResults contextResults : this.contextResultList) {
                for (GeneratedXmlFile gxf : contextResults.generatedXmlFiles()) {
                    projects.add(gxf.getTargetProject());
                    writeGeneratedXmlFile(gxf, contextResults.xmlFormatter(), callback);
                }

                for (GeneratedJavaFile gjf : contextResults.generatedJavaFiles()) {
                    projects.add(gjf.getTargetProject());
                    writeGeneratedJavaFile(gjf, contextResults.javaFormatter(), contextResults.javaFileEncoding(),
                            callback);
                }

                for (GeneratedKotlinFile gkf : contextResults.generatedKotlinFiles()) {
                    projects.add(gkf.getTargetProject());
                    writeGeneratedKotlinFile(gkf, contextResults.kotlinFormatter(), contextResults.kotlinFileEncoding(),
                            callback);
                }

                for (GenericGeneratedFile gf : contextResults.generatedGenericFiles()) {
                    projects.add(gf.getTargetProject());
                    writeGenericGeneratedFile(gf, callback);
                }
            }

            for (String project : projects) {
                shellCallback.refreshProject(project);
            }
        }

        callback.done();
    }

    private void writeGeneratedJavaFile(GeneratedJavaFile gjf, JavaFormatter javaFormatter,
                                        @Nullable String javaFileEncoding, ProgressCallback callback)
            throws InterruptedException, IOException {
        Path targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
            targetFile = directory.toPath().resolve(gjf.getFileName());
            if (Files.exists(targetFile)) {
                if (shellCallback.isMergeSupported()) {
                    source = shellCallback.mergeJavaFile(
                            javaFormatter.getFormattedContent(gjf.getCompilationUnit()), targetFile.toFile(),
                            MergeConstants.getOldElementTags(), javaFileEncoding);
                } else if (shellCallback.isOverwriteEnabled()) {
                    source = javaFormatter.getFormattedContent(gjf.getCompilationUnit());
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    source = javaFormatter.getFormattedContent(gjf.getCompilationUnit());
                    targetFile = getUniqueFileName(directory, gjf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            } else {
                source = javaFormatter.getFormattedContent(gjf.getCompilationUnit());
            }

            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
            writeFile(targetFile.toFile(), source, javaFileEncoding);
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    private void writeGeneratedKotlinFile(GeneratedKotlinFile gf, KotlinFormatter kotlinFormatter,
                                          @Nullable String kotlinFileEncoding, ProgressCallback callback)
            throws InterruptedException, IOException {
        Path targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gf.getTargetProject(), gf.getTargetPackage());
            targetFile = directory.toPath().resolve(gf.getFileName());
            if (Files.exists(targetFile)) {
                if (shellCallback.isOverwriteEnabled()) {
                    source = kotlinFormatter.getFormattedContent(gf.getKotlinFile());
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    source = kotlinFormatter.getFormattedContent(gf.getKotlinFile());
                    targetFile = getUniqueFileName(directory, gf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            } else {
                source = kotlinFormatter.getFormattedContent(gf.getKotlinFile());
            }

            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
            writeFile(targetFile.toFile(), source, kotlinFileEncoding);
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    private void writeGenericGeneratedFile(GenericGeneratedFile gf, ProgressCallback callback)
            throws InterruptedException, IOException {
        Path targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gf.getTargetProject(), gf.getTargetPackage());
            targetFile = directory.toPath().resolve(gf.getFileName());
            if (Files.exists(targetFile)) {
                if (shellCallback.isOverwriteEnabled()) {
                    source = gf.getFormattedContent();
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    source = gf.getFormattedContent();
                    targetFile = getUniqueFileName(directory, gf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            } else {
                source = gf.getFormattedContent();
            }

            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
            writeFile(targetFile.toFile(), source, gf.getFileEncoding().orElse(null));
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    private void writeGeneratedXmlFile(GeneratedXmlFile gxf, XmlFormatter xmlFormatter, ProgressCallback callback)
            throws InterruptedException, IOException {
        Path targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gxf.getTargetProject(), gxf.getTargetPackage());
            targetFile = directory.toPath().resolve(gxf.getFileName());
            if (Files.exists(targetFile)) {
                if (gxf.isMergeable()) {
                    source = XmlFileMergerJaxp.getMergedSource(xmlFormatter.getFormattedContent(gxf.getDocument()),
                            targetFile.toFile());
                } else if (shellCallback.isOverwriteEnabled()) {
                    source = xmlFormatter.getFormattedContent(gxf.getDocument());
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    source = xmlFormatter.getFormattedContent(gxf.getDocument());
                    targetFile = getUniqueFileName(directory, gxf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            } else {
                source = xmlFormatter.getFormattedContent(gxf.getDocument());
            }

            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
            writeFile(targetFile.toFile(), source, "UTF-8"); //$NON-NLS-1$
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    /**
     * Writes, or overwrites, the contents of the specified file.
     *
     * @param file
     *            the file
     * @param content
     *            the content
     * @param fileEncoding
     *            the file encoding
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void writeFile(File file, String content, @Nullable String fileEncoding) throws IOException {
        try (OutputStream fos = Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            OutputStreamWriter osw;
            if (fileEncoding == null) {
                osw = new OutputStreamWriter(fos);
            } else {
                osw = new OutputStreamWriter(fos, Charset.forName(fileEncoding));
            }

            try (BufferedWriter bw = new BufferedWriter(osw)) {
                bw.write(content);
            }
        }
    }

    /**
     * Gets the unique file name.
     *
     * @param directory
     *            the directory
     * @param fileName
     *            the file name
     * @return the unique file name
     */
    private Path getUniqueFileName(File directory, String fileName) {
        Path answer = null;

        // try up to 1000 times to generate a unique file name
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 1000; i++) {
            sb.setLength(0);
            sb.append(fileName);
            sb.append('.');
            sb.append(i);

            Path testFile = directory.toPath().resolve(sb.toString());
            if (Files.notExists(testFile)) {
                answer = testFile;
                break;
            }
        }

        if (answer == null) {
            throw new RuntimeException(getString(
                    "RuntimeError.3", directory.getAbsolutePath())); //$NON-NLS-1$
        }

        return answer;
    }

    /**
     * Returns the list of generated Java files after a call to one of the generate methods.
     * This is useful if you prefer to process the generated files yourself and do not want
     * the generator to write them to disk.
     *
     * @return the list of generated Java files
     */
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        return contextResultList.stream()
                .map(ContextResults::generatedJavaFiles)
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * Returns the list of generated Kotlin files after a call to one of the generate methods.
     * This is useful if you prefer to process the generated files yourself and do not want
     * the generator to write them to disk.
     *
     * @return the list of generated Kotlin files
     */
    public List<GeneratedKotlinFile> getGeneratedKotlinFiles() {
        return contextResultList.stream()
                .map(ContextResults::generatedKotlinFiles)
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * Returns the list of generated XML files after a call to one of the generate methods.
     * This is useful if you prefer to process the generated files yourself and do not want
     * the generator to write them to disk.
     *
     * @return the list of generated XML files
     */
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        return contextResultList.stream()
                .map(ContextResults::generatedXmlFiles)
                .flatMap(Collection::stream)
                .toList();
    }
}
