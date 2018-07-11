/**
 *    Copyright 2006-2017 the original author or authors.
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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mybatis.generator.api.GeneratedModelFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

@RunWith(Parameterized.class)
public class JavaCodeGenerationTest {

    private GeneratedModelFile generatedModelFile;

    public JavaCodeGenerationTest(GeneratedModelFile generatedModelFile) {
        this.generatedModelFile = generatedModelFile;
    }

    @Test
    public void testJavaParse() {
        ByteArrayInputStream is = new ByteArrayInputStream(
                generatedModelFile.getCompilationUnit().getFormattedContent().getBytes());
        try {
            JavaParser.parse(is);
        } catch (ParseException e) {
            fail("Generated Java File " + generatedModelFile.getFileName() + " will not compile");
        }
    }

    @Parameters
    public static List<GeneratedModelFile> generateJavaFiles() throws Exception {
        List<GeneratedModelFile> generatedFiles = new ArrayList<GeneratedModelFile>();
        generatedFiles.addAll(generateJavaFilesMybatis());
        generatedFiles.addAll(generateJavaFilesMybatisDsql());
        generatedFiles.addAll(generateJavaFilesIbatis());
        return generatedFiles;
    }

    private static List<GeneratedModelFile> generateJavaFilesMybatis() throws Exception {
        createDatabase();
        return generateJavaFiles("/scripts/generatorConfig.xml");
    }

    private static List<GeneratedModelFile> generateJavaFilesMybatisDsql() throws Exception {
        createDatabase();
        return generateJavaFiles("/scripts/generatorConfig_Dsql.xml");
    }

    private static List<GeneratedModelFile> generateJavaFilesIbatis() throws Exception {
        createDatabase();
        return generateJavaFiles("/scripts/ibatorConfig.xml");
    }

    private static List<GeneratedModelFile> generateJavaFiles(String configFile) throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(JavaCodeGenerationTest.class.getResourceAsStream(configFile));

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
        myBatisGenerator.generate(null, null, null, false);
        return myBatisGenerator.getGeneratedModelFiles();
    }

    public static void createDatabase() throws Exception {
        SqlScriptRunner scriptRunner = new SqlScriptRunner(JavaCodeGenerationTest.class.getResourceAsStream("/scripts/CreateDB.sql"), "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:aname", "sa", "");
        scriptRunner.executeScript();
    }
}
