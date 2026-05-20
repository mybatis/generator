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
package org.mybatis.generator.merge.java;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.SqlScriptRunner;
import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.api.KnownRuntime;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.ClientGeneratorConfiguration;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.ModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.Property;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;

class GenerateAndMergeTest {
    private static final String DRIVER_CLASS = "org.hsqldb.jdbcDriver";
    public static final String JDBC_URL = "jdbc:hsqldb:mem:aname";

    @Test
    void shouldMergeJavaFiles() throws Exception {
        Path modelDirectory = Files.createTempDirectory("model");

        createDatabase();

        writeExistingJavaFile(modelDirectory);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(configuration(modelDirectory))
                .withOverwriteEnabled(false)
                .withJavaFileMergeEnabled(true)
                .withContextIds(Set.of("test-context"))
                .withFullyQualifiedTableNames(Set.of("PKONLY"))
                .build();
        List<String> warnings = myBatisGenerator.generateAndWrite();
        assertThat(warnings).isEmpty();
        String content = Files.readString(modelDirectory.resolve("test/model/Pkonly.java"));
        assertThat(content).isEqualToNormalizingNewlines("""
            package test.model;

            import java.util.stream.IntStream;

            import jakarta.annotation.Generated;

            public class Pkonly {

                private int existingId;

                public int getSum() {
                    return IntStream.range(0, 100)
                                    .filter(i -> i % 2 == 0)
                                    .map(i -> i * 3)
                                    .sum();
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                private Integer id;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                private Integer seqNum;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public Integer getId() {
                    return id;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public void setId(Integer id) {
                    this.id = id;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public Integer getSeqNum() {
                    return seqNum;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public void setSeqNum(Integer seqNum) {
                    this.seqNum = seqNum;
                }
            }
            """);
    }

    @Test
    void shouldOverwriteJavaFiles() throws Exception {
        Path modelDirectory = Files.createTempDirectory("model");

        createDatabase();

        writeExistingJavaFile(modelDirectory);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(configuration(modelDirectory))
                .withOverwriteEnabled(true)
                .withJavaFileMergeEnabled(false)
                .build();
        List<String> warnings = myBatisGenerator.generateAndWrite();
        assertThat(warnings).hasSize(1);
        String content = Files.readString(modelDirectory.resolve("test/model/Pkonly.java"));
        assertThat(content).isEqualToNormalizingNewlines("""
            package test.model;

            import jakarta.annotation.Generated;

            public class Pkonly {
                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                private Integer id;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                private Integer seqNum;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public Integer getId() {
                    return id;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public void setId(Integer id) {
                    this.id = id;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public Integer getSeqNum() {
                    return seqNum;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public void setSeqNum(Integer seqNum) {
                    this.seqNum = seqNum;
                }
            }""");

    }

    @Test
    void shouldWriteNewVersionOfJavaFiles() throws Exception {
        Path modelDirectory = Files.createTempDirectory("model");

        createDatabase();

        writeExistingJavaFile(modelDirectory);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(configuration(modelDirectory))
                .withOverwriteEnabled(false)
                .withJavaFileMergeEnabled(false)
                .build();
        List<String> warnings = myBatisGenerator.generateAndWrite();
        assertThat(warnings).hasSize(1);

        String content = Files.readString(modelDirectory.resolve("test/model/Pkonly.java"));
        assertThat(content).isEqualToNormalizingNewlines("""
            package test.model;

            import java.util.stream.IntStream;

            public class Pkonly {
                private int existingId;

                public int getSum() {
                    return IntStream.range(0, 100).filter(i -> i % 2 == 0).map(i -> i * 3).sum();
                }
            }""");

        content = Files.readString(modelDirectory.resolve("test/model/Pkonly.java.1"));
        assertThat(content).isEqualToNormalizingNewlines("""
            package test.model;

            import jakarta.annotation.Generated;

            public class Pkonly {
                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                private Integer id;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                private Integer seqNum;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public Integer getId() {
                    return id;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public void setId(Integer id) {
                    this.id = id;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public Integer getSeqNum() {
                    return seqNum;
                }

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public void setSeqNum(Integer seqNum) {
                    this.seqNum = seqNum;
                }
            }""");
    }

    @Test
    void shouldOverwriteKotlinFiles() throws Exception {
        Path modelDirectory = Files.createTempDirectory("model");

        createDatabase();

        writeExistingKotlinFile(modelDirectory);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(kotlinConfiguration(modelDirectory))
                .withOverwriteEnabled(true)
                .build();
        List<String> warnings = myBatisGenerator.generateAndWrite();
        assertThat(warnings).hasSize(1);
        String content = Files.readString(modelDirectory.resolve("test/model/Pkonly.kt"));
        assertThat(content).isEqualToNormalizingNewlines("""
            /*
             * Auto-generated file. Created by MyBatis Generator
             */
            package test.model

            data class Pkonly(
                val id: Int,
                val seqNum: Int
            )""");
    }

    @Test
    void shouldWriteNewVersionOfKotlinFiles() throws Exception {
        Path modelDirectory = Files.createTempDirectory("model");

        createDatabase();

        writeExistingKotlinFile(modelDirectory);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator.Builder()
                .withConfiguration(kotlinConfiguration(modelDirectory))
                .withOverwriteEnabled(false)
                .build();
        List<String> warnings = myBatisGenerator.generateAndWrite();
        assertThat(warnings).hasSize(1);

        String content = Files.readString(modelDirectory.resolve("test/model/Pkonly.kt"));
        assertThat(content).isEqualToNormalizingNewlines("""
            package test.model

            import java.util.stream.IntStream

            class Pkonly {
                private val existingId: Int = 0

                fun getSum(): Int {
                    return IntStream.range(0, 100).filter { i -> i % 2 == 0 }.map { i -> i * 3 }.sum()
                }
            }
            """);

        content = Files.readString(modelDirectory.resolve("test/model/Pkonly.kt.1"));
        assertThat(content).isEqualToNormalizingNewlines("""
            /*
             * Auto-generated file. Created by MyBatis Generator
             */
            package test.model

            data class Pkonly(
                val id: Int,
                val seqNum: Int
            )""");
    }

    private Configuration configuration(Path modelDirectory) throws IOException {
        return new Configuration.Builder()
                .withContext(context(modelDirectory))
                .build();
    }

    private Context context(Path modelDirectory) throws IOException {
        Path mapperDirectory = Files.createTempDirectory("mapper");
        Path xmlDirectory = Files.createTempDirectory("xml");

        return new Context.Builder()
                .withId("test-context")
                .withDefaultModelType(ModelType.FLAT)
                .withTargetRuntime(KnownRuntime.MYBATIS3.getAlias())
                .withJdbcConnectionConfiguration(new JDBCConnectionConfiguration.Builder()
                        .withDriverClass(DRIVER_CLASS)
                        .withConnectionURL(JDBC_URL)
                        .withUserId("sa")
                        .build())
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("test.model")
                        .withTargetProject(modelDirectory.toAbsolutePath().toString())
                        .build())
                .withClientGeneratorConfiguration(new ClientGeneratorConfiguration.Builder()
                        .withLegacyClientType(ClientGeneratorConfiguration.LegacyClientType.XML_MAPPER)
                        .withTargetPackage("test.mapper")
                        .withTargetProject(mapperDirectory.toAbsolutePath().toString())
                        .build())
                .withSqlMapGeneratorConfiguration(new SqlMapGeneratorConfiguration.Builder()
                        .withTargetPackage("test.xml")
                        .withTargetProject(xmlDirectory.toAbsolutePath().toString())
                        .build())
                .withCommentGeneratorConfiguration(new CommentGeneratorConfiguration.Builder()
                        .withProperty(new Property("minimizeComments", "true"))
                        .build())
                .withTableConfiguration(new TableConfiguration.Builder()
                        .withTableName("PKONLY")
                        .build())
                .build();
    }

    private Configuration kotlinConfiguration(Path modelDirectory) throws IOException {
        return new Configuration.Builder()
                .withContext(kotlinContext(modelDirectory))
                .build();
    }

    private Context kotlinContext(Path modelDirectory) throws IOException {
        Path mapperDirectory = Files.createTempDirectory("mapper");

        return new Context.Builder()
                .withId("kotlin-context")
                .withDefaultModelType(ModelType.FLAT)
                .withTargetRuntime(KnownRuntime.MYBATIS3_KOTLIN.getAlias())
                .withJdbcConnectionConfiguration(new JDBCConnectionConfiguration.Builder()
                        .withDriverClass(DRIVER_CLASS)
                        .withConnectionURL(JDBC_URL)
                        .withUserId("sa")
                        .build())
                .withModelGeneratorConfiguration(new ModelGeneratorConfiguration.Builder()
                        .withTargetPackage("test.model")
                        .withTargetProject(modelDirectory.toAbsolutePath().toString())
                        .build())
                .withClientGeneratorConfiguration(new ClientGeneratorConfiguration.Builder()
                        .withLegacyClientType(ClientGeneratorConfiguration.LegacyClientType.XML_MAPPER)
                        .withTargetPackage("test.mapper")
                        .withTargetProject(mapperDirectory.toAbsolutePath().toString())
                        .build())
                .withCommentGeneratorConfiguration(new CommentGeneratorConfiguration.Builder()
                        .withProperty(new Property("minimizeComments", "true"))
                        .build())
                .withTableConfiguration(new TableConfiguration.Builder()
                        .withTableName("PKONLY")
                        .build())
                .build();
    }

    private void writeExistingJavaFile(Path modelDirectory) throws IOException {
        TopLevelClass topLevelClass = new TopLevelClass("test.model.Pkonly");
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        Field field = new Field("existingId", FullyQualifiedJavaType.getIntInstance());
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);

        Method method = new Method("getSum");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addBodyLine("return IntStream.range(0, 100).filter(i -> i % 2 == 0).map(i -> i * 3).sum();");
        topLevelClass.addMethod(method);

        topLevelClass.addImportedType("java.util.stream.IntStream");

        DefaultJavaFormatter defaultJavaFormatter = new DefaultJavaFormatter();
        defaultJavaFormatter.setIndenter(Indenter.defaultIndenter());
        String content = defaultJavaFormatter.getFormattedContent(topLevelClass);
        Files.createDirectories(modelDirectory.resolve("test/model"));
        Path javaFile = modelDirectory.resolve("test/model/Pkonly.java");
        Files.writeString(javaFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void writeExistingKotlinFile(Path modelDirectory) throws IOException {
        String content = """
                package test.model

                import java.util.stream.IntStream

                class Pkonly {
                    private val existingId: Int = 0

                    fun getSum(): Int {
                        return IntStream.range(0, 100).filter { i -> i % 2 == 0 }.map { i -> i * 3 }.sum()
                    }
                }
                """;
        Files.createDirectories(modelDirectory.resolve("test/model"));
        Path javaFile = modelDirectory.resolve("test/model/Pkonly.kt");
        Files.writeString(javaFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    static void createDatabase() throws Exception {
        SqlScriptRunner scriptRunner =
                new SqlScriptRunner(GenerateAndMergeTest.class.getResourceAsStream("/scripts/CreateDB.sql"),
                        DRIVER_CLASS, JDBC_URL, "sa", "");
        scriptRunner.executeScript();
    }
}
