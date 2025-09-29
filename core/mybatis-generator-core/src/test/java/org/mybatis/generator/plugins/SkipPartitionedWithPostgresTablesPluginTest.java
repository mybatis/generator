/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link SkipPartitionedWithPostgresTablesPlugin}.
 *
 * These tests focus on the decision logic that determines whether generation hooks
 * should proceed based on the plugin's internal allowlist. We purposefully avoid
 * calling {@link SkipPartitionedWithPostgresTablesPlugin#setProperties(java.util.Properties)}
 * because that method attempts to connect to a PostgreSQL database using JDBC URLs
 * and Postgres-specific catalog queries. Instead, we inject the allowlist directly
 * via reflection so the tests remain fast and deterministic without external DB dependencies.
 */
class SkipPartitionedWithPostgresTablesPluginTest {

    /**
     * Simple IntrospectedTable stub that only supplies a table name.
     */
    private static class StubIntrospectedTable extends IntrospectedTable {
        StubIntrospectedTable(String tableName) {
            super(TargetRuntime.MYBATIS3);
            // Create a minimal Context for FullyQualifiedTable (delimiters not used here)
            Context ctx = new Context(null);
            FullyQualifiedTable fqt = new FullyQualifiedTable(null, null, tableName, null, null,
                    false, null, null, null, false, null, ctx);
            setFullyQualifiedTable(fqt);
        }
        @Override public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) { }
        @Override public List<GeneratedJavaFile> getGeneratedJavaFiles() { return Collections.emptyList(); }
        @Override public List<GeneratedXmlFile> getGeneratedXmlFiles() { return Collections.emptyList(); }
        @Override public List<GeneratedKotlinFile> getGeneratedKotlinFiles() { return Collections.emptyList(); }
        @Override public int getGenerationSteps() { return 0; }
        @Override public boolean requiresXMLGenerator() { return false; }
    }

    private SkipPartitionedWithPostgresTablesPlugin newPluginWithAllowlist(Set<String> allowlist) throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = new SkipPartitionedWithPostgresTablesPlugin();
        Field f = SkipPartitionedWithPostgresTablesPlugin.class.getDeclaredField("allowlist");
        f.setAccessible(true);
        f.set(plugin, allowlist);
        return plugin;
    }

    private SkipPartitionedWithPostgresTablesPlugin newPluginWithAllowlistCaseInsensitive(Set<String> allowlist) throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlist(allowlist);
        Field ci = SkipPartitionedWithPostgresTablesPlugin.class.getDeclaredField("caseInsensitive");
        ci.setAccessible(true);
        ci.set(plugin, true);
        // allowlistNormalized will be lazily built; ensure null now
        Field norm = SkipPartitionedWithPostgresTablesPlugin.class.getDeclaredField("allowlistNormalized");
        norm.setAccessible(true);
        norm.set(plugin, null);
        return plugin;
    }

    @Test
    @DisplayName("When allowlist is null, plugin should NOT skip any table (fail-open)")
    void testNoAllowlistDoesNotSkip() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlist(null);
        boolean result = plugin.modelBaseRecordClassGenerated(new TopLevelClass("Dummy"), new StubIntrospectedTable("some_table"));
        assertThat(result).as("Expect generation to proceed when allowlist is null").isTrue();
    }

    @Test
    @DisplayName("Table present in allowlist should be generated (base record)")
    void testAllowlistAllowsMatching() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlist(Set.of("customer"));
        boolean result = plugin.modelBaseRecordClassGenerated(new TopLevelClass("Customer"), new StubIntrospectedTable("customer"));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Table NOT present in allowlist should be skipped (base record)")
    void testAllowlistSkipsNonMatching() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlist(Set.of("customer"));
        boolean result = plugin.modelBaseRecordClassGenerated(new TopLevelClass("Order"), new StubIntrospectedTable("orders"));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Case sensitivity: exact match required (mismatch should skip)")
    void testCaseSensitivity() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlist(Set.of("Customer"));
        // underlying table name is lowercase 'customer' which does not equal 'Customer'
        boolean result = plugin.modelBaseRecordClassGenerated(new TopLevelClass("Customer"), new StubIntrospectedTable("customer"));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Case insensitive: allowlist 'Customer' matches table 'customer' when caseInsensitive=true")
    void testCaseInsensitiveAllowsLowerCase() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlistCaseInsensitive(Set.of("Customer"));
        boolean result = plugin.modelBaseRecordClassGenerated(new TopLevelClass("Customer"), new StubIntrospectedTable("customer"));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Case insensitive: non-matching table still skipped when caseInsensitive=true")
    void testCaseInsensitiveSkipsMissing() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlistCaseInsensitive(Set.of("Alpha", "Beta"));
        boolean result = plugin.modelBaseRecordClassGenerated(new TopLevelClass("Gamma"), new StubIntrospectedTable("gAmMa"));
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("All generation hook methods behave consistently with allowlist")
    void testOtherHooksConsistency() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = newPluginWithAllowlist(Set.of("alpha", "beta"));
        StubIntrospectedTable allowed = new StubIntrospectedTable("alpha");
        StubIntrospectedTable disallowed = new StubIntrospectedTable("gamma");

        TopLevelClass tlc = new TopLevelClass("Alpha");
        Interface iface = new Interface("AlphaMapper");

        // Allowed table
        assertThat(plugin.modelPrimaryKeyClassGenerated(tlc, allowed)).isTrue();
        assertThat(plugin.modelRecordWithBLOBsClassGenerated(tlc, allowed)).isTrue();
        assertThat(plugin.dynamicSqlSupportGenerated(tlc, allowed)).isTrue();
        assertThat(plugin.clientGenerated(iface, allowed)).isTrue();

        // Disallowed table
        assertThat(plugin.modelPrimaryKeyClassGenerated(tlc, disallowed)).isFalse();
        assertThat(plugin.modelRecordWithBLOBsClassGenerated(tlc, disallowed)).isFalse();
        assertThat(plugin.dynamicSqlSupportGenerated(tlc, disallowed)).isFalse();
        assertThat(plugin.clientGenerated(iface, disallowed)).isFalse();
    }

    @Test
    @DisplayName("Fail-fast: JDBC 未設定 + allowlistFile 指定時は 事前チェックメッセージで RuntimeException")
    void testSetPropertiesWithoutJdbcConfig() throws Exception {
        SkipPartitionedWithPostgresTablesPlugin plugin = new SkipPartitionedWithPostgresTablesPlugin();
        Context ctx = new Context(null); // JDBC 設定なし
        plugin.setContext(ctx);
        Path tempDir = Files.createTempDirectory("mbg-test");
        Path allowlistFile = tempDir.resolve("allowlist-generated.txt");
        Properties props = new Properties();
        props.setProperty("allowlistFile", allowlistFile.toString());

        assertThatThrownBy(() -> plugin.setProperties(props))
                .as("事前チェックで JDBC/ConnectionFactory 未設定エラーが出る")
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No JDBC or ConnectionFactory configuration present");

        assertThat(Files.exists(allowlistFile)).as("例外発生時は allowlist ファイルは生成されない").isFalse();
    }
}
