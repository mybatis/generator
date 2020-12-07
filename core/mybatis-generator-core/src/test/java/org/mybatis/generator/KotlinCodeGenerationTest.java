/*
 *    Copyright 2006-2020 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.api.GeneratedKotlinFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * This test executes the same generator configuration that is in the mybatis-generator-systests-kotlin
 * project. The purpose of this test is to exercise the Kotlin code generators during the initial build
 * as a kind of smoke test - currently we do not try to parse the generated code. This test also shows
 * test coverage as it mimics the code paths executed by the integration test.
 *
 * @author Jeff Butler
 *
 */
class KotlinCodeGenerationTest {

    @ParameterizedTest
    @MethodSource("generateKotlinFiles")
    void testKotlinParse(GeneratedKotlinFile generatedKotlinFile) {
        // for now, just let the test pass. if we find a good Kotlin parser, then mimic the
        // function of the Java test by trying to parse generated code
        assertTrue(true);
    }

    static List<GeneratedKotlinFile> generateKotlinFiles() throws Exception {
        JavaCodeGenerationTest.createDatabase();
        return generateKotlinFiles("/scripts/generatorConfig-kotlin.xml");
    }

    static List<GeneratedKotlinFile> generateKotlinFiles(String configFile) throws Exception {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(KotlinCodeGenerationTest.class.getResourceAsStream(configFile));

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
        myBatisGenerator.generate(null, null, null, false);
        return myBatisGenerator.getGeneratedKotlinFiles();
    }
}
