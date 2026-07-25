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
package org.mybatis.generator.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelGeneratorConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;

class MyBatisGeneratorTest {

    @Test
    void testCalculateContextsToRun() {
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
        assertThat(generatorWithContextId.calculateContextsToRun())
                .extracting(Context::getId)
                .containsExactly("context1");

        MyBatisGenerator generatorWithoutContextIds = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .build();
        assertThat(generatorWithoutContextIds.calculateContextsToRun())
                .extracting(Context::getId)
                .containsExactly("context1", "context2");

        MyBatisGenerator generatorWithContextIdAndTableName = new MyBatisGenerator.Builder()
                .withConfiguration(config)
                .withShellCallback(new DefaultShellCallback())
                .withContextIds(Set.of("context1"))
                .withFullyQualifiedTableNames(Set.of("some_table"))
                .build();
        assertThat(generatorWithContextIdAndTableName.calculateContextsToRun())
                .extracting(Context::getId)
                .containsExactly("context1");
    }
}
