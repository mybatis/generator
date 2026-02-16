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
package org.mybatis.generator;

import static org.junit.jupiter.api.Assertions.fail;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParserConfiguration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

class JavaCodeGenerationTest {

    @ParameterizedTest
    @MethodSource("javaFileGenerator")
    void testJavaParse(GeneratedJavaFile generatedJavaFile) {
        DefaultJavaFormatter formatter = new DefaultJavaFormatter();

        String content = formatter.getFormattedContent(generatedJavaFile.getCompilationUnit());
        try {
            ParserConfiguration parserConfiguration = new ParserConfiguration();
            parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_25);
            JavaParser javaParser = new JavaParser(parserConfiguration);
            javaParser.parse(content);
        } catch (ParseProblemException e) {
            fail("Generated Java File " + generatedJavaFile.getFileName() + " will not compile");
        }
    }

    static List<GeneratedJavaFile> javaFileGenerator() throws Exception {
        List<GeneratedJavaFile> generatedFiles = new ArrayList<>();
        generatedFiles.addAll(generateJavaFilesMybatis());
        generatedFiles.addAll(generateJavaFilesMybatisDsql());
        return generatedFiles;
    }

    static List<GeneratedJavaFile> generateJavaFilesMybatis() throws Exception {
        createDatabase();
        return generateJavaFiles("/scripts/generatorConfig.xml");
    }

    static List<GeneratedJavaFile> generateJavaFilesMybatisDsql() throws Exception {
        createDatabase();
        return generateJavaFiles("/scripts/generatorConfig_Dsql.xml");
    }

    static List<GeneratedJavaFile> generateJavaFiles(String configFile) throws Exception {
        ConfigurationParser cp = new ConfigurationParser();
        InputStream is = JavaCodeGenerationTest.class.getResourceAsStream(configFile);
        if (is == null) {
            throw new RuntimeException("Cannot load configFile: " + configFile);
        }
        Configuration config = cp.parseConfiguration(is);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .withOverwriteEnabled(true)
                .build();
        myBatisGenerator.generateOnly();
        return myBatisGenerator.getGeneratedJavaFiles();
    }

    static void createDatabase() throws Exception {
        SqlScriptRunner scriptRunner =
                new SqlScriptRunner(JavaCodeGenerationTest.class.getResourceAsStream("/scripts/CreateDB.sql"),
                        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:aname", "sa", "");
        scriptRunner.executeScript();
    }
}
