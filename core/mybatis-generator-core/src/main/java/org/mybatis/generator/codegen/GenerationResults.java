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
    private final JavaFormatter javaFormatter;
    private final KotlinFormatter kotlinFormatter;
    private final XmlFormatter xmlFormatter;
    private final @Nullable String javaFileEncoding;
    private final @Nullable String kotlinFileEncoding;
    private final List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();
    private final List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<>();
    private final List<GeneratedKotlinFile> generatedKotlinFiles = new ArrayList<>();
    private final List<GenericGeneratedFile> generatedGenericFiles = new ArrayList<>();

    protected GenerationResults(Builder builder) {
        javaFormatter = Objects.requireNonNull(builder.javaFormatter);
        kotlinFormatter = Objects.requireNonNull(builder.kotlinFormatter);
        xmlFormatter = Objects.requireNonNull(builder.xmlFormatter);
        javaFileEncoding = builder.javaFileEncoding;
        kotlinFileEncoding = builder.kotlinFileEncoding;
    }

    public JavaFormatter javaFormatter() {
        return javaFormatter;
    }

    public KotlinFormatter kotlinFormatter() {
        return kotlinFormatter;
    }

    public XmlFormatter xmlFormatter() {
        return xmlFormatter;
    }

    public @Nullable String javaFileEncoding() {
        return javaFileEncoding;
    }

    public @Nullable String kotlinFileEncoding() {
        return kotlinFileEncoding;
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

    public void addGeneratedJavaFiles(List<GeneratedJavaFile> generatedJavaFiles) {
        this.generatedJavaFiles.addAll(generatedJavaFiles);
    }

    public void addGeneratedXmlFiles(List<GeneratedXmlFile> generatedXmlFiles) {
        this.generatedXmlFiles.addAll(generatedXmlFiles);
    }

    public void addGeneratedKotlinFiles(List<GeneratedKotlinFile> generatedKotlinFiles) {
        this.generatedKotlinFiles.addAll(generatedKotlinFiles);
    }

    public void addGeneratedGenericFiles(List<GenericGeneratedFile> generatedGenericFiles) {
        this.generatedGenericFiles.addAll(generatedGenericFiles);
    }

    public int getNumberOfGeneratedFiles() {
        return generatedJavaFiles().size()
                + generatedXmlFiles().size()
                + generatedKotlinFiles().size()
                + generatedGenericFiles().size();
    }

    public static class Builder {
        private @Nullable JavaFormatter javaFormatter;
        private @Nullable KotlinFormatter kotlinFormatter;
        private @Nullable XmlFormatter xmlFormatter;
        private @Nullable String javaFileEncoding;
        private @Nullable String kotlinFileEncoding;

        public Builder withJavaFormatter(JavaFormatter javaFormatter) {
            this.javaFormatter = javaFormatter;
            return this;
        }

        public Builder withKotlinFormatter(KotlinFormatter kotlinFormatter) {
            this.kotlinFormatter = kotlinFormatter;
            return this;
        }

        public Builder withXmlFormatter(XmlFormatter xmlFormatter) {
            this.xmlFormatter = xmlFormatter;
            return this;
        }

        public Builder withJavaFileEncoding(@Nullable String javaFileEncoding) {
            this.javaFileEncoding = javaFileEncoding;
            return this;
        }

        public Builder withKotlinFileEncoding(@Nullable String kotlinFileEncoding) {
            this.kotlinFileEncoding = kotlinFileEncoding;
            return this;
        }

        public GenerationResults build() {
            return new GenerationResults(this);
        }
    }
}
