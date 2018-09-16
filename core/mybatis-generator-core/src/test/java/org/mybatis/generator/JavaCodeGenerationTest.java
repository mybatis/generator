/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;

public class JavaCodeGenerationTest {

    @ParameterizedTest
    @MethodSource("generateJavaFiles")
    public void testJavaParse(GeneratedJavaFile generatedJavaFile) {
        DefaultJavaFormatter formatter = new DefaultJavaFormatter();

        ByteArrayInputStream is = new ByteArrayInputStream(
                formatter.getFormattedContent(generatedJavaFile.getCompilationUnit()).getBytes());
        try {
            JavaParser.parse(is);
        } catch (ParseProblemException e) {
            fail("Generated Java File " + generatedJavaFile.getFileName() + " will not compile");
        }
    }

    public static List<GeneratedJavaFile> generateJavaFiles() throws Exception {
        List<GeneratedJavaFile> generatedFiles = new ArrayList<>();
        generatedFiles.addAll(generateJavaFilesMybatis());
        generatedFiles.addAll(generateJavaFilesMybatisDsql());
        return generatedFiles;
    }

    private static List<GeneratedJavaFile> generateJavaFilesMybatis() throws Exception {
        createDatabase();
        return generateJavaFiles("/scripts/generatorConfig.xml");
    }

    private static List<GeneratedJavaFile> generateJavaFilesMybatisDsql() throws Exception {
        createDatabase();
        return generateJavaFiles("/scripts/generatorConfig_Dsql.xml");
    }

    private static List<GeneratedJavaFile> generateJavaFiles(String configFile) throws Exception {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(JavaCodeGenerationTest.class.getResourceAsStream(configFile));

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
        myBatisGenerator.generate(null, null, null, false);
        return myBatisGenerator.getGeneratedJavaFiles();
    }

    public static void createDatabase() throws Exception {
        SqlScriptRunner scriptRunner = new SqlScriptRunner(JavaCodeGenerationTest.class.getResourceAsStream("/scripts/CreateDB.sql"), "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:aname", "sa", "");
        scriptRunner.executeScript();
    }
}
