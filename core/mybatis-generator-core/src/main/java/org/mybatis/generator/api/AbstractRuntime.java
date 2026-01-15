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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;

public abstract class AbstractRuntime extends AbstractGenerator {
    protected final List<AbstractKotlinGenerator> kotlinGenerators = new ArrayList<>();
    protected final List<AbstractJavaGenerator> javaGenerators = new ArrayList<>();
    protected @Nullable AbstractXmlGenerator xmlMapperGenerator;

    protected AbstractRuntime(AbstractRuntimeBuilder<?> builder) {
        super(builder);
        calculateGenerators();
    }

    protected Optional<String> getClientProject() {
        return context.getJavaClientGeneratorConfiguration().map(JavaClientGeneratorConfiguration::getTargetProject);
    }

    protected String getModelProject() {
        return context.getJavaModelGeneratorConfiguration().getTargetProject();
    }

    /**
     * This method should return the number of progress messages that will be
     * sent during the generation phase.
     *
     * @return the number of progress messages
     */
    public int getGenerationSteps() {
        return javaGenerators.size() + kotlinGenerators.size() + (xmlMapperGenerator == null ? 0 : 1);
    }

    /**
     * This method should return a list of generated Java files related to this
     * table. This list could include various types of model classes, as well as
     * DAO classes.
     *
     * @return the list of generated Java files for this table
     */
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<>();

        for (AbstractJavaGenerator javaGenerator : javaGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit, javaGenerator.getProject());
                answer.add(gjf);
            }
        }

        return answer;
    }

    /**
     * This method should return a list of generated XML files related to this
     * table. Most implementations will only return one file - the generated
     * SqlMap file.
     *
     * @return the list of generated XML files for this table
     */
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            if (document != null) {
                GeneratedXmlFile gxf = new GeneratedXmlFile(document, introspectedTable.getMyBatis3XmlMapperFileName(),
                        introspectedTable.getMyBatis3XmlMapperPackage(),
                        context.getSqlMapGeneratorConfiguration()
                                .map(SqlMapGeneratorConfiguration::getTargetProject).orElse(""),
                        true);
                if (pluginAggregator.sqlMapGenerated(gxf, introspectedTable)) {
                    answer.add(gxf);
                }
            }
        }

        return answer;
    }

    /**
     * This method should return a list of generated Kotlin files related to this
     * table. This list could include a data classes, a mapper interface, extension methods, etc.
     *
     * @return the list of generated Kotlin files for this table
     */
    public List<GeneratedKotlinFile> getGeneratedKotlinFiles() {
        List<GeneratedKotlinFile> answer = new ArrayList<>();

        for (AbstractKotlinGenerator kotlinGenerator : kotlinGenerators) {
            List<KotlinFile> kotlinFiles = kotlinGenerator.getKotlinFiles();
            for (KotlinFile kotlinFile : kotlinFiles) {
                GeneratedKotlinFile gjf = new GeneratedKotlinFile(kotlinFile, kotlinGenerator.getProject());
                answer.add(gjf);
            }
        }

        return answer;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    /**
     * This method will be called in constructor. Subclasses should calculate generators for their runtimes and add
     * them to the various lists contained in this class.
     */
    protected abstract void calculateGenerators();

    public abstract static class AbstractRuntimeBuilder<T extends AbstractRuntimeBuilder<T>>
            extends AbstractGeneratorBuilder<T> {
        public abstract AbstractRuntime build();
    }
}
