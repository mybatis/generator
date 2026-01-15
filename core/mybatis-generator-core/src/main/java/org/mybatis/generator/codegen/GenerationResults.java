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
package org.mybatis.generator.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.GenericGeneratedFile;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.KotlinFormatter;
import org.mybatis.generator.api.XmlFormatter;

/**
 * Container for generated files from a single context.
 * Also includes information needed for writing those files to disk.
 */
public class GenerationResults {
    private final CalculatedContextValues contextValues;
    private final List<GeneratedJavaFile> generatedJavaFiles;
    private final List<GeneratedXmlFile> generatedXmlFiles;
    private final List<GeneratedKotlinFile> generatedKotlinFiles;
    private final List<GenericGeneratedFile> generatedGenericFiles;

    protected GenerationResults(Builder builder) {
        contextValues = Objects.requireNonNull(builder.contextValues);
        generatedJavaFiles = builder.generatedJavaFiles;
        generatedXmlFiles = builder.generatedXmlFiles;
        generatedKotlinFiles = builder.generatedKotlinFiles;
        generatedGenericFiles = builder.generatedGenericFiles;
    }

    public JavaFormatter javaFormatter() {
        return contextValues.javaFormatter();
    }

    public KotlinFormatter kotlinFormatter() {
        return contextValues.kotlinFormatter();
    }

    public XmlFormatter xmlFormatter() {
        return contextValues.xmlFormatter();
    }

    public @Nullable String javaFileEncoding() {
        return contextValues.javaFileEncoding();
    }

    public @Nullable String kotlinFileEncoding() {
        return contextValues.kotlinFileEncoding();
    }

    public List<GeneratedJavaFile> generatedJavaFiles() {
        return generatedJavaFiles;
    }

    public List<GeneratedXmlFile> generatedXmlFiles() {
        return generatedXmlFiles;
    }

    public List<GeneratedKotlinFile> generatedKotlinFiles() {
        return generatedKotlinFiles;
    }

    public List<GenericGeneratedFile> generatedGenericFiles() {
        return generatedGenericFiles;
    }

    public int getNumberOfGeneratedFiles() {
        return generatedJavaFiles().size()
                + generatedXmlFiles().size()
                + generatedKotlinFiles().size()
                + generatedGenericFiles().size();
    }

    public static class Builder {
        private @Nullable CalculatedContextValues contextValues;
        private final List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();
        private final List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<>();
        private final List<GeneratedKotlinFile> generatedKotlinFiles = new ArrayList<>();
        private final List<GenericGeneratedFile> generatedGenericFiles = new ArrayList<>();

        public Builder withContextValues(CalculatedContextValues contextValues) {
            this.contextValues = contextValues;
            return this;
        }

        public Builder withGeneratedJavaFiles(List<GeneratedJavaFile> generatedJavaFiles) {
            this.generatedJavaFiles.addAll(generatedJavaFiles);
            return this;
        }

        public Builder withGeneratedKotlinFiles(List<GeneratedKotlinFile> generatedKotlinFiles) {
            this.generatedKotlinFiles.addAll(generatedKotlinFiles);
            return this;
        }

        public Builder withGeneratedXmlFiles(List<GeneratedXmlFile> generatedXmlFiles) {
            this.generatedXmlFiles.addAll(generatedXmlFiles);
            return this;
        }

        public Builder withGeneratedGenericFiles(List<GenericGeneratedFile> generatedGenericFiles) {
            this.generatedGenericFiles.addAll(generatedGenericFiles);
            return this;
        }

        public GenerationResults build() {
            return new GenerationResults(this);
        }
    }
}
