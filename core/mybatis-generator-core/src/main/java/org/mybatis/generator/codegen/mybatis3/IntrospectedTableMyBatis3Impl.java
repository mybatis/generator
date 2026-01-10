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
package org.mybatis.generator.codegen.mybatis3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.model.BaseRecordGenerator;
import org.mybatis.generator.codegen.mybatis3.model.ExampleGenerator;
import org.mybatis.generator.codegen.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.codegen.mybatis3.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TypedPropertyHolder;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * Introspected table implementation for generating MyBatis3 artifacts.
 *
 * @author Jeff Butler
 */
public class IntrospectedTableMyBatis3Impl extends IntrospectedTable {

    protected final List<AbstractJavaGenerator> javaGenerators = new ArrayList<>();

    protected final List<AbstractKotlinGenerator> kotlinGenerators = new ArrayList<>();

    protected @Nullable AbstractXmlGenerator xmlMapperGenerator;

    public IntrospectedTableMyBatis3Impl() {
        super(TargetRuntime.MYBATIS3);
    }

    @Override
    public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
        calculateJavaModelGenerators(warnings, progressCallback);

        AbstractJavaClientGenerator javaClientGenerator =
                calculateClientGenerators(warnings, progressCallback).orElse(null);

        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }

    protected void calculateXmlMapperGenerator(@Nullable AbstractJavaClientGenerator javaClientGenerator,
            List<String> warnings,
            ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (getContext().getSqlMapGeneratorConfiguration().isPresent()) {
                xmlMapperGenerator = new XMLMapperGenerator.Builder()
                        .withContext(getContext())
                        .withIntrospectedTable(this)
                        .withWarnings(warnings)
                        .withProgressCallback(progressCallback)
                        .build();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator().orElse(null);
        }
    }

    protected Optional<AbstractJavaClientGenerator> calculateClientGenerators(List<String> warnings,
            ProgressCallback progressCallback) {
        if (!getRules().generateJavaClient()) {
            return Optional.empty();
        }

        return createJavaClientGenerator().map(g -> {
            initializeAbstractGenerator(g, warnings, progressCallback);
            javaGenerators.add(g);
            return g;
        });
    }

    protected Optional<AbstractJavaClientGenerator> createJavaClientGenerator() {
        return getContext().getJavaClientGeneratorConfiguration()
                .flatMap(TypedPropertyHolder::getConfigurationType)
                .map(t -> {
                    if ("XMLMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return new JavaMapperGenerator(getClientProject());
                    } else if ("MIXEDMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return new MixedClientGenerator(getClientProject());
                    } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return new AnnotatedClientGenerator(getClientProject());
                    } else if ("MAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return new JavaMapperGenerator(getClientProject());
                    } else {
                        return ObjectFactory.createInternalObject(t, AbstractJavaClientGenerator.class);
                    }
                });
    }

    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generateExampleClass()) {
            AbstractJavaGenerator javaGenerator = new ExampleGenerator.Builder()
                    .withProject(getModelProject())
                    .withContext(getContext())
                    .withIntrospectedTable(this)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (getRules().generatePrimaryKeyClass()) {
            AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator.Builder()
                    .withProject(getModelProject())
                    .withContext(getContext())
                    .withIntrospectedTable(this)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new BaseRecordGenerator.Builder()
                    .withProject(getModelProject())
                    .withContext(getContext())
                    .withIntrospectedTable(this)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .build();
            javaGenerators.add(javaGenerator);
        }

        if (getRules().generateRecordWithBLOBsClass()) {
            AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator.Builder()
                    .withProject(getModelProject())
                    .withContext(getContext())
                    .withIntrospectedTable(this)
                    .withProgressCallback(progressCallback)
                    .withWarnings(warnings)
                    .build();
            javaGenerators.add(javaGenerator);
        }
    }

    protected void initializeAbstractGenerator(@Nullable AbstractGenerator abstractGenerator, List<String> warnings,
                                               ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(getContext());
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<>();

        for (AbstractJavaGenerator javaGenerator : javaGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit, javaGenerator.getProject(),
                        getContext().getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        getContext().getJavaFormatter());
                answer.add(gjf);
            }
        }

        return answer;
    }

    @Override
    public List<GeneratedKotlinFile> getGeneratedKotlinFiles() {
        List<GeneratedKotlinFile> answer = new ArrayList<>();

        for (AbstractKotlinGenerator kotlinGenerator : kotlinGenerators) {
            List<KotlinFile> kotlinFiles = kotlinGenerator.getKotlinFiles();
            for (KotlinFile kotlinFile : kotlinFiles) {
                GeneratedKotlinFile gjf = new GeneratedKotlinFile(kotlinFile,
                                kotlinGenerator.getProject(),
                        getContext().getProperty(PropertyRegistry.CONTEXT_KOTLIN_FILE_ENCODING),
                        getContext().getKotlinFormatter());
                answer.add(gjf);
            }
        }

        return answer;
    }

    protected String getClientProject() {
        return getContext().getJavaClientGeneratorConfiguration().orElseThrow().getTargetProject();
    }

    protected String getModelProject() {
        return getContext().getJavaModelGeneratorConfiguration().getTargetProject();
    }

    protected String getExampleProject() {
        String project = getContext().getJavaModelGeneratorConfiguration().getProperty(
                PropertyRegistry.MODEL_GENERATOR_EXAMPLE_PROJECT);

        if (StringUtility.stringHasValue(project)) {
            return project;
        } else {
            return getModelProject();
        }
    }

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            if (document != null) {
                GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                        getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                        getContext().getSqlMapGeneratorConfiguration().orElseThrow().getTargetProject(),
                        true, getContext().getXmlFormatter());
                if (getContext().getPlugins().sqlMapGenerated(gxf, this)) {
                    answer.add(gxf);
                }
            }
        }

        return answer;
    }

    @Override
    public int getGenerationSteps() {
        return javaGenerators.size() + (xmlMapperGenerator == null ? 0 : 1);
    }

    @Override
    public boolean requiresXMLGenerator() {
        return createJavaClientGenerator()
                .map(AbstractJavaClientGenerator::requiresXMLGenerator)
                .orElse(false);
    }
}
