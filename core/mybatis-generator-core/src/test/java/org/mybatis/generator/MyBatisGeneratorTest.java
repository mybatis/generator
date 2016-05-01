/**
 *    Copyright 2006-2016 the original author or authors.
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MyBatisGeneratorTest {

    @Test(expected=InvalidConfigurationException.class)
    public void testGenerateMyBatis3() throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("generatorConfigMyBatis3.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
        
        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null, null, null, false);
        } catch (InvalidConfigurationException e) {
            assertEquals(2, e.getErrors().size());
            throw e;
        }
    }

    @Test(expected=InvalidConfigurationException.class)
    public void testGenerateIbatis2() throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("generatorConfigIbatis2.xml"));
            
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
        
        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null, null, null, false);
        } catch (InvalidConfigurationException e) {
            assertEquals(1, e.getErrors().size());
            throw e;
        }
    }
}
