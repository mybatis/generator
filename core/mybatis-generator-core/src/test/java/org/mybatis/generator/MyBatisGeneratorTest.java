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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.ModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.Property;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

class MyBatisGeneratorTest {

    @Test
    void testCalculateContextsToRun() throws Exception {
        ModelGeneratorConfiguration modelGeneratorConfiguration = new ModelGeneratorConfiguration.Builder()
                .withTargetPackage("test")
                .withTargetProject("test")
                .build();
        Context context1 = new Context.Builder()
                .withId("context1")
                .withModelGeneratorConfiguration(modelGeneratorConfiguration)
                .build();
        Context context2 = new Context.Builder()
                .withId("context2")
                .withModelGeneratorConfiguration(modelGeneratorConfiguration)
                .build();
        Configuration config = new Configuration.Builder()
                .withContext(context1)
                .withContext(context2)
                .build();

        MyBatisGenerator generatorWithContextId = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .withContextIds(Set.of("context1"))
                .build();
        assertThat(calculateContextsToRun(generatorWithContextId))
                .extracting(Context::getId)
                .containsExactly("context1");

        MyBatisGenerator generatorWithoutContextIds = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .build();
        assertThat(calculateContextsToRun(generatorWithoutContextIds))
                .extracting(Context::getId)
                .containsExactly("context1", "context2");

        MyBatisGenerator generatorWithContextIdAndTableName = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .withContextIds(Set.of("context1"))
                .withFullyQualifiedTableNames(Set.of("some_table"))
                .build();
        assertThat(calculateContextsToRun(generatorWithContextIdAndTableName))
                .extracting(Context::getId)
                .containsExactly("context1");
    }

    @SuppressWarnings("unchecked")
    private List<Context> calculateContextsToRun(MyBatisGenerator generator) throws Exception {
        Method method = MyBatisGenerator.class.getDeclaredMethod("calculateContextsToRun");
        method.setAccessible(true);
        return (List<Context>) method.invoke(generator);
    }

    @Test
    void testGenerateMyBatis3WithInvalidConfig() throws Exception {
        ConfigurationParser cp = new ConfigurationParser();
        InputStream is = getClass().getClassLoader().getResourceAsStream("generatorConfigMyBatis3_badConfig.xml");
        assert is != null;
        Configuration config = cp.parseConfiguration(is);

        assertThat(cp.getWarnings()).hasSize(23);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .withOverwriteEnabled(true)
                .build();

        InvalidConfigurationException e =
                assertThrows(InvalidConfigurationException.class, myBatisGenerator::generateOnly);

        assertThat(e.getExtraMessages()).hasSize(6);
    }

    @Test
    void testGenerateInvalidConfigWithNoConnectionSources() {
        Context context = new Context.Builder()
                .withId("MyContext")
                .withDefaultModelType(ModelType.FLAT)
                .withTargetRuntime("MyBatis3Simple")
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("foo.bar")
                        .withTargetProject("MyProject")
                        .build())
                .build();
        Configuration config = new Configuration.Builder().withContext(context).build();

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .withOverwriteEnabled(true)
                .build();

        InvalidConfigurationException e =
                assertThrows(InvalidConfigurationException.class, myBatisGenerator::generateOnly);
        assertEquals(2, e.getExtraMessages().size());
    }

    @Test
    void testGenerateInvalidConfigWithTwoConnectionSources() {
        Context context = new Context.Builder()
                .withId("MyContext")
                .withDefaultModelType(ModelType.FLAT)
                .withTargetRuntime("MyBatis3Simple")
                .withConnectionFactoryConfiguration(new ConnectionFactoryConfiguration.Builder().build())
                .withJdbcConnectionConfiguration(new JDBCConnectionConfiguration.Builder().withDriverClass("a").withConnectionURL("b").build())
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("foo.bar")
                        .withTargetProject("MyProject")
                        .build())
                .build();
        Configuration config = new Configuration.Builder().withContext(context).build();

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .withOverwriteEnabled(true)
                .build();

        InvalidConfigurationException e =
                assertThrows(InvalidConfigurationException.class, myBatisGenerator::generateOnly);
        assertEquals(2, e.getExtraMessages().size());
    }

    @Test
    void testInvalidConfigWithGeneratedKeysAndRecords() {
        Context context = new Context.Builder()
                .withId("MyContext")
                .withDefaultModelType(ModelType.RECORD)
                .withJdbcConnectionConfiguration(new JDBCConnectionConfiguration.Builder()
                        .withDriverClass("a")
                        .withConnectionURL("b")
                        .build())
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("foo.bar")
                        .withTargetProject("MyProject")
                        .build())
                .withTableConfiguration(new TableConfiguration.Builder()
                        .withTableName("test")
                        .withGeneratedKey(new GeneratedKey("id", "JDBC", true))
                        .build())
                .build();

        List<String> errors = new ArrayList<>();
        context.validate(errors);
        assertThat(errors).hasSize(1);
    }

    @Test
    void testInvalidConfigWithGeneratedKeysAndRecords2() {
        Context context = new Context.Builder()
                .withId("MyContext")
                .withDefaultModelType(ModelType.FLAT)
                .withJdbcConnectionConfiguration(new JDBCConnectionConfiguration.Builder()
                        .withDriverClass("a")
                        .withConnectionURL("b")
                        .build())
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("foo.bar")
                        .withTargetProject("MyProject")
                        .build())
                .withTableConfiguration(new TableConfiguration.Builder()
                        .withTableName("test")
                        .withModelType("record")
                        .withGeneratedKey(new GeneratedKey("id", "JDBC", true))
                        .build())
                .build();

        List<String> errors = new ArrayList<>();
        context.validate(errors);
        assertThat(errors).hasSize(1);
    }

    @Test
    void testValidConfigWithGeneratedKeysAndRecords() {
        Context context = new Context.Builder()
                .withId("MyContext")
                .withDefaultModelType(ModelType.RECORD)
                .withJdbcConnectionConfiguration(new JDBCConnectionConfiguration.Builder()
                        .withDriverClass("a")
                        .withConnectionURL("b")
                        .build())
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("foo.bar")
                        .withTargetProject("MyProject")
                        .build())
                .withTableConfiguration(new TableConfiguration.Builder()
                        .withTableName("test")
                        .withModelType("flat")
                        .withGeneratedKey(new GeneratedKey("id", "JDBC", true))
                        .build())
                .build();

        List<String> errors = new ArrayList<>();
        context.validate(errors);
        assertThat(errors).isEmpty();
    }

    @Test
    void testInvalidConfigWithGeneratedKeysAndImmutable() {
        Context context = new Context.Builder()
                .withId("MyContext")
                .withDefaultModelType(ModelType.FLAT)
                .withJdbcConnectionConfiguration(new JDBCConnectionConfiguration.Builder()
                        .withDriverClass("a")
                        .withConnectionURL("b")
                        .build())
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("foo.bar")
                        .withTargetProject("MyProject")
                        .build())
                .withTableConfiguration(new TableConfiguration.Builder()
                        .withTableName("test")
                        .withProperty(new Property("immutable", "true"))
                        .withGeneratedKey(new GeneratedKey("id", "JDBC", true))
                        .build())
                .build();

        List<String> errors = new ArrayList<>();
        context.validate(errors);
        assertThat(errors).hasSize(1);
    }
}
