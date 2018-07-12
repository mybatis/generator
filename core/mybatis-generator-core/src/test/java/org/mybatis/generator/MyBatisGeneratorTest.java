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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MyBatisGeneratorTest {

    @Test
    public void testGenerateMyBatis3WithInvalidConfig() throws Exception {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("generatorConfigMyBatis3_badConfig.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        InvalidConfigurationException e = 
                assertThrows(InvalidConfigurationException.class, () -> {
                    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
                    myBatisGenerator.generate(null, null, null, false);
                });

        assertEquals(2, e.getErrors().size());
    }

    @Test
    public void testGenerateInvalidConfigWithNoConnectionSources() throws Exception {
        List<String> warnings = new ArrayList<>();
        Configuration config = new Configuration();
        Context context = new Context(ModelType.HIERARCHICAL);
        context.setId("MyContext");
        config.addContext(context);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        InvalidConfigurationException e =
                assertThrows(InvalidConfigurationException.class, () -> {
                    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
                    myBatisGenerator.generate(null, null, null, false);
                });
        assertEquals(3, e.getErrors().size());
    }

    @Test
    public void testGenerateInvalidConfigWithTwoConnectionSources() throws Exception {
        List<String> warnings = new ArrayList<>();
        Configuration config = new Configuration();
        Context context = new Context(ModelType.HIERARCHICAL);
        context.setId("MyContext");
        context.setConnectionFactoryConfiguration(new ConnectionFactoryConfiguration());
        context.setJdbcConnectionConfiguration(new JDBCConnectionConfiguration());
        config.addContext(context);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        InvalidConfigurationException e =
                assertThrows(InvalidConfigurationException.class, () -> {
                    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
                    myBatisGenerator.generate(null, null, null, false);
                });
        assertEquals(3, e.getErrors().size());
    }
}
