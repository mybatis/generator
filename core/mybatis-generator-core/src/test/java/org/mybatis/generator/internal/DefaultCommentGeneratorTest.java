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
package org.mybatis.generator.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.KnownRuntime;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelGeneratorConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;

class DefaultCommentGeneratorTest {
    @Test
    void testDefaultsOnField() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();

        Field field = new Field("testField", FullyQualifiedJavaType.getStringInstance());
        commentGenerator.addFieldAnnotation(field, getTestTable(), imports);
        String annotation = field.getAnnotations().get(0);
        assertThat(annotation)
                .startsWith("@Generated(value=\"org.mybatis.generator.api.MyBatisGenerator\", date=")
                .endsWith(", comments=\"Source Table: test\")");
    }

    @Test
    void testSuppressDateOnField() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Properties properties = new Properties();
        properties.setProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE, "true");

        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();
        commentGenerator.addConfigurationProperties(properties);

        Field field = new Field("testField", FullyQualifiedJavaType.getStringInstance());
        commentGenerator.addFieldAnnotation(field, getTestTable(), imports);
        assertThat(field.getAnnotations())
                .containsOnly("@Generated(value=\"org.mybatis.generator.api.MyBatisGenerator\", comments=\"Source Table: test\")");
    }

    @Test
    void testSuppressAllOnField() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Properties properties = new Properties();
        properties.setProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS, "true");

        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();
        commentGenerator.addConfigurationProperties(properties);

        Field field = new Field("testField", FullyQualifiedJavaType.getStringInstance());
        commentGenerator.addFieldAnnotation(field, getTestTable(), imports);
        assertThat(field.getAnnotations()).isEmpty();
    }

    @Test
    void testSuppressMinimizeOnField() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Properties properties = new Properties();
        properties.setProperty(PropertyRegistry.COMMENT_GENERATOR_MINIMIZE_COMMENTS, "true");

        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();
        commentGenerator.addConfigurationProperties(properties);

        Field field = new Field("testField", FullyQualifiedJavaType.getStringInstance());
        commentGenerator.addFieldAnnotation(field, getTestTable(), imports);
        assertThat(field.getAnnotations())
                .containsOnly("@Generated(\"org.mybatis.generator.api.MyBatisGenerator\")");
    }

    @Test
    void testDefaultsOnKeptClass() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();

        InnerClass innerClass = new InnerClass("Bar");
        commentGenerator.addClassAnnotationAndMarkAsDoNotDelete(innerClass, getTestTable(), imports);
        String annotation = innerClass.getAnnotations().get(0);
        assertThat(annotation)
                .startsWith("@Generated(value=\"org.mybatis.generator.api.MyBatisGenerator\", date=")
                .endsWith(", comments=\"do_not_delete_during_merge\")");
    }

    @Test
    void testSuppressDateOnKeptClass() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Properties properties = new Properties();
        properties.setProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE, "true");

        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();
        commentGenerator.addConfigurationProperties(properties);

        InnerClass innerClass = new InnerClass("Bar");
        commentGenerator.addClassAnnotationAndMarkAsDoNotDelete(innerClass, getTestTable(), imports);
        assertThat(innerClass.getAnnotations())
                .containsOnly("@Generated(value=\"org.mybatis.generator.api.MyBatisGenerator\", comments=\"do_not_delete_during_merge\")");
    }

    @Test
    void testSuppressAllOnKeptClass() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Properties properties = new Properties();
        properties.setProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS, "true");

        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();
        commentGenerator.addConfigurationProperties(properties);

        InnerClass innerClass = new InnerClass("Bar");
        commentGenerator.addClassAnnotationAndMarkAsDoNotDelete(innerClass, getTestTable(), imports);
        assertThat(innerClass.getAnnotations()).isEmpty();
    }

    @Test
    void testSuppressMinimizeOnKeptClass() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        Properties properties = new Properties();
        properties.setProperty(PropertyRegistry.COMMENT_GENERATOR_MINIMIZE_COMMENTS, "true");

        DefaultCommentGenerator commentGenerator = new DefaultCommentGenerator();
        commentGenerator.addConfigurationProperties(properties);

        InnerClass innerClass = new InnerClass("Bar");
        commentGenerator.addClassAnnotationAndMarkAsDoNotDelete(innerClass, getTestTable(), imports);
        assertThat(innerClass.getAnnotations())
                .containsOnly("@Generated(value=\"org.mybatis.generator.api.MyBatisGenerator\", comments=\"do_not_delete_during_merge\")");
    }

    private IntrospectedTable getTestTable() {
        var answer = new IntrospectedTable.Builder()
                .withKnownRuntime(KnownRuntime.MYBATIS3_DYNAMIC_SQL)
                .withTableConfiguration(new TableConfiguration.Builder()
                        .withTableName("test")
                        .build())
                .withFullyQualifiedTable(new FullyQualifiedTable.Builder()
                        .withIntrospectedTableName("test")
                        .build())
                .withContext(new Context.Builder().withId("test-context")
                        .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                                .withTargetPackage("foo")
                                .withTargetProject("test-project")
                                .build())
                        .build())
                .build();
        answer.setRemarks("Database table remarks");

        return answer;
    }
}
