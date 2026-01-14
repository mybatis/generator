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

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.GenericGeneratedFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.KotlinFormatter;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.ObjectFactory;

/**
 * This class holds the intermediate results of the generator.
 *
 */
public class ContextResults {
    private final Context context;
    private final List<IntrospectedTable> introspectedTables = new ArrayList<>();
    private final JavaFormatter javaFormatter;
    private final KotlinFormatter kotlinFormatter;
    private final XmlFormatter xmlFormatter;
    private final CommentGenerator commentGenerator;
    private final @Nullable String javaFileEncoding;
    private final @Nullable String kotlinFileEncoding;
    private final List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();
    private final List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<>();
    private final List<GeneratedKotlinFile> generatedKotlinFiles = new ArrayList<>();
    private final List<GenericGeneratedFile> generatedGenericFiles = new ArrayList<>();

    public ContextResults(Context context) {
        this.context = context;
        javaFormatter = ObjectFactory.createJavaFormatter(context);
        kotlinFormatter = ObjectFactory.createKotlinFormatter(context);
        xmlFormatter = ObjectFactory.createXmlFormatter(context);
        commentGenerator = ObjectFactory.createCommentGenerator(context);
        javaFileEncoding = context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING);
        kotlinFileEncoding = context.getProperty(PropertyRegistry.CONTEXT_KOTLIN_FILE_ENCODING);
    }

    public Context context() {
        return context;
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

    public CommentGenerator commentGenerator() {
        return commentGenerator;
    }

    public @Nullable String javaFileEncoding() {
        return javaFileEncoding;
    }

    public @Nullable String kotlinFileEncoding() {
        return kotlinFileEncoding;
    }

    public void addIntrospectedTables(List<IntrospectedTable> introspectedTables) {
        this.introspectedTables.addAll(introspectedTables);
    }

    public List<IntrospectedTable> introspectedTables() {
        return introspectedTables;
    }

    public int getGenerationSteps() {
        int totalSteps = 0;
        for (IntrospectedTable introspectedTable : introspectedTables) {
            totalSteps += introspectedTable.getGenerationSteps();
        }
        return totalSteps;
    }

    public int getNumberOfGeneratedFiles() {
        return generatedJavaFiles().size()
                + generatedXmlFiles().size()
                + generatedKotlinFiles().size()
                + generatedGenericFiles().size();
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
}
