package org.mybatis.generator.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.PluginAggregator;

class CodeGenerationAttributesTest {

    @Test
    void testThatTestObjectBuilds() {
        assertThat(generateTestObject()).isNotNull();
    }

    private CodeGenerationAttributes generateTestObject() {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration.Builder()
                .withTargetPackage("foo.bar")
                .withTargetProject("TestProject")
                .build();

        Context context = new Context.Builder()
                .withId("test")
                .withDefaultModelType(ModelType.CONDITIONAL)
                .withJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration)
                .build();

        FullyQualifiedTable fullyQualifiedTable = new FullyQualifiedTable.Builder()
                // TODO - why does this need context?
                .withContext(context)
                .withIntrospectedTableName("MY_TABLE")
                .build();

        TableConfiguration tableConfiguration = new TableConfiguration.Builder()
                .withTableName("MY_TABLE")
                // TODO - why both here:
                .withModelType(ModelType.CONDITIONAL, "conditional")
                .build();

        return new IntrospectedTable.Builder()
                .withContext(context)
                .withPluginAggregator(new PluginAggregator())
                .withKnownRuntime(KnownRuntime.MYBATIS3)
                .withFullyQualifiedTable(fullyQualifiedTable)
                .withTableConfiguration(tableConfiguration)
                .build();
    }
}
