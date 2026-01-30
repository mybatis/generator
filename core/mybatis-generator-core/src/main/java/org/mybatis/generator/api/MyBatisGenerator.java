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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.codegen.CalculatedContextValues;
import org.mybatis.generator.codegen.GenerationEngine;
import org.mybatis.generator.codegen.GenerationResults;
import org.mybatis.generator.codegen.IntrospectionEngine;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.InternalException;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.merge.xml.XmlFileMergerJaxp;

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
    private final ProgressCallback progressCallback;
    private final Set<String> contextIds;
    private final Set<String> fullyQualifiedTableNames;

    private final List<GenerationResults> generationResultsList = new ArrayList<>();

    private MyBatisGenerator(Builder builder) {
        configuration = Objects.requireNonNull(builder.configuration, getString("RuntimeError.2")); //$NON-NLS-1$
        shellCallback = Objects.requireNonNullElseGet(builder.shellCallback, () -> new DefaultShellCallback(false));
        progressCallback = Objects.requireNonNullElse(builder.progressCallback, NULL_PROGRESS_CALLBACK);
        fullyQualifiedTableNames = builder.fullyQualifiedTableNames;
        contextIds = builder.contextIds;
    }

    /**
     * This is one of the main methods for generating code. This method is long-running, but progress can be provided
     * and the method can be canceled through the ProgressCallback interface. This method will not write results to
     * the disk. The generated objects can be retrieved from the getGeneratedJavaFiles(), getGeneratedKotlinFiles(),
     * getGeneratedXmlFiles(), and getGeneratedGenericFiles() methods.
     *
     * @return any warnings created during the generation process
     * @throws SQLException
     *             the SQL exception
     * @throws InterruptedException
     *             if the method is canceled through the ProgressCallback
     * @throws InvalidConfigurationException
     *             if the specified configuration is invalid
     */
    public List<String> generateOnly() throws SQLException, InterruptedException, InvalidConfigurationException {
        List<String> warnings = new ArrayList<>();
        generateFiles(warnings);
        progressCallback.done();
        return warnings;
    }

    /**
     * This is one of the main methods for generating code. This method is long-running, but progress can be provided
     * and the method can be canceled through the ProgressCallback interface. This method will write results to
     * the disk.
     *
     * @return any warnings created during the generation process
     * @throws SQLException
     *             the SQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *             if the method is canceled through the ProgressCallback
     * @throws InvalidConfigurationException
     *             if the specified configuration is invalid
     */
    public List<String> generateAndWrite() throws SQLException, IOException, InterruptedException,
            InvalidConfigurationException {
        List<String> warnings = new ArrayList<>();
        generateFiles(warnings);
        writeGeneratedFiles(warnings);
        progressCallback.done();
        return warnings;
    }

    private void generateFiles(List<String> warnings) throws SQLException, InterruptedException,
            InvalidConfigurationException {
        configuration.validate();
        generationResultsList.clear();
        ObjectFactory.reset();
        RootClassInfo.reset();

        setupCustomClassloader();
        List<Context> contextsToRun = calculateContextsToRun();
        List<CalculatedContextValues> contextValuesList = calculateContextValues(contextsToRun, warnings);
        List<ContextValuesAndTables> contextValuesAndTablesList = runAllIntrospections(contextValuesList, warnings);
        List<GenerationEngine> generationEngines = createGenerationEngines(contextValuesAndTablesList, warnings);
        runGenerationEngines(generationEngines);
    }

    private void setupCustomClassloader() {
        if (!configuration.getClassPathEntries().isEmpty()) {
            ClassLoader classLoader = getCustomClassloader(configuration.getClassPathEntries());
            ObjectFactory.addExternalClassLoader(classLoader);
        }
    }

    private List<Context> calculateContextsToRun() {
        List<Context> contextsToRun;
        if (fullyQualifiedTableNames.isEmpty()) {
            contextsToRun = configuration.getContexts();
        } else {
            contextsToRun = configuration.getContexts().stream()
                    .filter(c -> contextIds.contains(c.getId()))
                    .toList();
        }

        return contextsToRun;
    }

    private List<CalculatedContextValues> calculateContextValues(List<Context> contextsToRun, List<String> warnings) {
        return contextsToRun.stream()
                .map(c -> createContextValues(c, warnings))
                .toList();
    }

    private CalculatedContextValues createContextValues(Context context, List<String> warnings) {
        return new CalculatedContextValues.Builder()
                .withContext(context)
                .withWarnings(warnings)
                .build();
    }

    private List<ContextValuesAndTables> runAllIntrospections(List<CalculatedContextValues> contextValuesList,
                                                              List<String> warnings)
            throws SQLException, InterruptedException {
        int totalSteps = contextValuesList.stream()
                .map(CalculatedContextValues::context)
                .mapToInt(Context::getIntrospectionSteps)
                .sum();
        progressCallback.introspectionStarted(totalSteps);

        List<ContextValuesAndTables> contextValuesAndTablesList = new ArrayList<>();
        for (CalculatedContextValues contextValues : contextValuesList) {
            contextValuesAndTablesList.add(new ContextValuesAndTables(contextValues,
                    runContextIntrospection(fullyQualifiedTableNames, contextValues, warnings)));
        }

        return contextValuesAndTablesList;
    }

    private List<IntrospectedTable> runContextIntrospection(Set<String> fullyQualifiedTableNames,
                                                            CalculatedContextValues contextValues,
                                                            List<String> warnings)
            throws SQLException, InterruptedException {
        return new IntrospectionEngine.Builder()
                .withContextValues(contextValues)
                .withFullyQualifiedTableNames(fullyQualifiedTableNames)
                .withWarnings(warnings)
                .withProgressCallback(progressCallback)
                .build()
                .introspectTables();
    }

    private List<GenerationEngine> createGenerationEngines(List<ContextValuesAndTables> contextValuesAndTablesListList,
                                                           List<String> warnings) {
        return contextValuesAndTablesListList.stream()
                .map(c -> createGenerationEngine(c, warnings))
                .toList();
    }

    private GenerationEngine createGenerationEngine(ContextValuesAndTables contextValuesAndTables,
                                                    List<String> warnings) {
        return new GenerationEngine.Builder()
                .withContextValues(contextValuesAndTables.contextValues())
                .withProgressCallback(progressCallback)
                .withWarnings(warnings)
                .withIntrospectedTables(contextValuesAndTables.introspectedTables())
                .build();
    }

    private void runGenerationEngines(List<GenerationEngine> generationEngines) throws InterruptedException {
        // calculate the number of steps
        int totalSteps = generationEngines.stream().mapToInt(GenerationEngine::getGenerationSteps).sum();
        progressCallback.generationStarted(totalSteps);

        // now run the generators
        for (GenerationEngine generationEngine: generationEngines) {
            var generationResults = generationEngine.generate();
            generationResultsList.add(generationResults);
        }
    }

    private void writeGeneratedFiles(List<String> warnings) throws IOException, InterruptedException {
        Set<String> projects = new HashSet<>();
        int totalSteps = generationResultsList.stream().mapToInt(GenerationResults::getNumberOfGeneratedFiles).sum();
        progressCallback.saveStarted(totalSteps);

        for (GenerationResults generationResults : generationResultsList) {
            for (GeneratedXmlFile gxf : generationResults.generatedXmlFiles()) {
                projects.add(gxf.getTargetProject());
                writeGeneratedXmlFile(gxf, generationResults.xmlFormatter(), warnings);
            }

            for (GeneratedJavaFile gjf : generationResults.generatedJavaFiles()) {
                projects.add(gjf.getTargetProject());
                writeGeneratedJavaFile(gjf, generationResults.javaFormatter(), generationResults.javaFileEncoding(),
                        warnings);
            }

            for (GeneratedKotlinFile gkf : generationResults.generatedKotlinFiles()) {
                projects.add(gkf.getTargetProject());
                writeGeneratedKotlinFile(gkf, generationResults.kotlinFormatter(),
                        generationResults.kotlinFileEncoding(), warnings);
            }

            for (GenericGeneratedFile gf : generationResults.generatedGenericFiles()) {
                projects.add(gf.getTargetProject());
                writeGenericGeneratedFile(gf, warnings);
            }
        }

        for (String project : projects) {
            shellCallback.refreshProject(project);
        }
    }

    private void writeGeneratedJavaFile(GeneratedJavaFile gjf, JavaFormatter javaFormatter,
                                        @Nullable String javaFileEncoding, List<String> warnings)
            throws InterruptedException, IOException {
        Path targetFile;
        String source = javaFormatter.getFormattedContent(gjf.getCompilationUnit());
        try {
            File directory = shellCallback.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
            targetFile = directory.toPath().resolve(gjf.getFileName());
            if (Files.exists(targetFile)) {
                if (shellCallback.isMergeSupported()) {
                    source = shellCallback.mergeJavaFile(source, targetFile.toFile(),
                            MergeConstants.getOldElementTags(), javaFileEncoding);
                } else if (shellCallback.isOverwriteEnabled()) {
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    targetFile = getUniqueFileName(directory, gjf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            }

            progressCallback.checkCancel();
            progressCallback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
            writeFile(targetFile.toFile(), source, javaFileEncoding);
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    private void writeGeneratedKotlinFile(GeneratedKotlinFile gf, KotlinFormatter kotlinFormatter,
                                          @Nullable String kotlinFileEncoding, List<String> warnings)
            throws InterruptedException, IOException {
        Path targetFile;
        String source = kotlinFormatter.getFormattedContent(gf.getKotlinFile());
        try {
            File directory = shellCallback.getDirectory(gf.getTargetProject(), gf.getTargetPackage());
            targetFile = directory.toPath().resolve(gf.getFileName());
            if (Files.exists(targetFile)) {
                if (shellCallback.isOverwriteEnabled()) {
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    targetFile = getUniqueFileName(directory, gf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            }

            progressCallback.checkCancel();
            progressCallback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
            writeFile(targetFile.toFile(), source, kotlinFileEncoding);
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    private void writeGenericGeneratedFile(GenericGeneratedFile gf, List<String> warnings)
            throws InterruptedException, IOException {
        Path targetFile;
        String source = gf.getFormattedContent();
        try {
            File directory = shellCallback.getDirectory(gf.getTargetProject(), gf.getTargetPackage());
            targetFile = directory.toPath().resolve(gf.getFileName());
            if (Files.exists(targetFile)) {
                if (shellCallback.isOverwriteEnabled()) {
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    targetFile = getUniqueFileName(directory, gf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            }

            progressCallback.checkCancel();
            progressCallback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
            writeFile(targetFile.toFile(), source, gf.getFileEncoding().orElse(null));
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    private void writeGeneratedXmlFile(GeneratedXmlFile gxf, XmlFormatter xmlFormatter, List<String> warnings)
            throws InterruptedException, IOException {
        Path targetFile;
        String source = xmlFormatter.getFormattedContent(gxf.getDocument());
        try {
            File directory = shellCallback.getDirectory(gxf.getTargetProject(), gxf.getTargetPackage());
            targetFile = directory.toPath().resolve(gxf.getFileName());
            if (Files.exists(targetFile)) {
                if (gxf.isMergeable()) {
                    source = XmlFileMergerJaxp.getMergedSource(source, targetFile.toFile());
                } else if (shellCallback.isOverwriteEnabled()) {
                    warnings.add(getString("Warning.11", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                } else {
                    targetFile = getUniqueFileName(directory, gxf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.toFile().getAbsolutePath())); //$NON-NLS-1$
                }
            }

            progressCallback.checkCancel();
            progressCallback.startTask(getString("Progress.15", targetFile.toString())); //$NON-NLS-1$
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
            throw new InternalException(getString("RuntimeError.3", directory.getAbsolutePath())); //$NON-NLS-1$
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
        return generationResultsList.stream()
                .map(GenerationResults::generatedJavaFiles)
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
        return generationResultsList.stream()
                .map(GenerationResults::generatedKotlinFiles)
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
        return generationResultsList.stream()
                .map(GenerationResults::generatedXmlFiles)
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * Returns the list of generated generic files after a call to one of the generate methods.
     * This is useful if you prefer to process the generated files yourself and do not want
     * the generator to write them to disk.
     *
     * <p>The list will be empty unless you have used a plugin that generates generic files
     * or are using a custom runtime.
     *
     * @return the list of generated generic files
     */
    public List<GenericGeneratedFile> getGeneratedGenericFiles() {
        return generationResultsList.stream()
                .map(GenerationResults::generatedGenericFiles)
                .flatMap(Collection::stream)
                .toList();
    }

    private record ContextValuesAndTables(CalculatedContextValues contextValues,
                                          List<IntrospectedTable> introspectedTables) { }

    public static class Builder {
        private @Nullable Configuration configuration;
        private @Nullable ShellCallback shellCallback;
        private @Nullable ProgressCallback progressCallback;
        private final Set<String> contextIds = new HashSet<>();
        private final Set<String> fullyQualifiedTableNames = new HashSet<>();

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withShellCallback(ShellCallback shellCallback) {
            this.shellCallback = shellCallback;
            return this;
        }

        public Builder withProgressCallback(@Nullable ProgressCallback progressCallback) {
            this.progressCallback = progressCallback;
            return this;
        }

        /**
         * Set of context IDs to use in generation. Only the contexts with an id specified in this set will run.
         * If the set is empty, then all contexts are run.
         *
         * @param contextIds
         *            a set of contextIds to use in code generation
         *
         * @return this builder
         */
        public Builder withContextIds(Set<String> contextIds) {
            this.contextIds.addAll(contextIds);
            return this;
        }

        /**
         *  Set of table names to generate. The elements of the set must be Strings that exactly match what's
         *  specified in the configuration. For example, if a table name = "foo" and schema = "bar", then the fully
         *  qualified table name is "foo.bar". If the Set is empty, then all tables in the configuration
         *  will be used for code generation.
         *
         * @param fullyQualifiedTableNames
         *            a set of table names to use in code generation
         *
         * @return this builder
         */
        public Builder withFullyQualifiedTableNames(Set<String> fullyQualifiedTableNames) {
            this.fullyQualifiedTableNames.addAll(fullyQualifiedTableNames);
            return this;
        }

        public MyBatisGenerator build() {
            return new MyBatisGenerator(this);
        }
    }
}
