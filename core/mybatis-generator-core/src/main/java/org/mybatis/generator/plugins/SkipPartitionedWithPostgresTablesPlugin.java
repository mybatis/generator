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

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * MBG plugin to skip generating artifacts for partitioned tables/views by using
 * an allowlist file that contains non-partitioned table (and view) names.
 * <p>
 * Behavior:
 * - Property "allowlistFile": path to a file to store allowed table names.
 * The plugin will connect to the configured database and create (or overwrite) this file
 * by listing non-partitioned tables/views (excluding 'pg_catalog' and 'information_schema' schemas).
 * - If the allowlist could be loaded, generation occurs only for tables in the list.
 * - If loading fails for any reason, the plugin will throw a RuntimeException (fail-fast).
 */
public class SkipPartitionedWithPostgresTablesPlugin extends PluginAdapter {

    private static final Logger log = LoggerFactory.getLogger(SkipPartitionedWithPostgresTablesPlugin.class);

    private Set<String> allowlist = null; // null until discovered
    private boolean caseInsensitive = false;
    private Set<String> allowlistNormalized = null; // used only when caseInsensitive

    private boolean hasConnectionConfiguration() {
        if (context == null) {
            return false;
        }

        if (context.getConnectionFactoryConfiguration() != null) {
            return true;
        }

        try {
            Field f = Context.class.getDeclaredField("jdbcConnectionConfiguration");
            f.setAccessible(true);
            Object jdbcCfg = f.get(context);
            return jdbcCfg != null;
        } catch (Exception e) {
            log.debug("Failed to inspect JDBC configuration reflectively; proceeding as if present", e);
            return true;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        String ciProp = properties.getProperty("caseInsensitive");
        if (ciProp != null && ciProp.equalsIgnoreCase("true")) {
            caseInsensitive = true;
        }

        if (!hasConnectionConfiguration()) {
            String msg = "No JDBC or ConnectionFactory configuration present - cannot discover non-partitioned tables for SkipPartitionedWithPostgresTablesPlugin";
            log.error(msg);
            throw new RuntimeException(msg);
        }

        try {
            this.allowlist = discoverNonPartitionedTables();
            if (caseInsensitive && allowlist != null) {
                buildNormalizedAllowlist();
            }
            log.info("Discovered non-partitioned tables/views: {}", allowlist.size());
        } catch (Exception e) { // fail-fast
            log.error("Failed to discover non-partitioned tables/views", e);
            throw new RuntimeException("Failed to discover non-partitioned tables/views", e);
        }

        String allowlistFilePath = properties.getProperty("allowlistFile", "");
        if (allowlistFilePath != null && !allowlistFilePath.isEmpty() && allowlist != null) {
            allowlistFilePath = allowlistFilePath.replace("/", File.separator);
            Path output = Path.of(allowlistFilePath);
            try {
                Path parent = output.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.write(output, new TreeSet<>(this.allowlist));
                log.info("Wrote allowlist file: {} (caseInsensitive={})", output.toAbsolutePath(), caseInsensitive);
            } catch (IOException e) {
                log.error("Failed to write allowlist file: {}", output.toAbsolutePath(), e);
                throw new RuntimeException("Failed to write allowlist file: " + output.toAbsolutePath(), e);
            }
        }
    }

    private void buildNormalizedAllowlist() {
        if (allowlist != null) {
            Set<String> normalized = new HashSet<>(allowlist.size());
            for (String s : allowlist) {
                if (s != null) {
                    normalized.add(s.toLowerCase(Locale.ROOT));
                }
            }
            this.allowlistNormalized = normalized;
        }
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true; // always valid
    }

    private Set<String> discoverNonPartitionedTables() throws SQLException {
        final String query = "WITH partition_children AS (\n" +
                "    SELECT\n" +
                "        relname\n" +
                "    FROM\n" +
                "        pg_class\n" +
                "            JOIN pg_inherits ON pg_class.oid = pg_inherits.inhrelid\n" +
                ")\n" +
                "\n" +
                "SELECT\n" +
                "    tablename\n" +
                "FROM\n" +
                "    pg_tables\n" +
                "WHERE\n" +
                "    schemaname NOT IN ('pg_catalog', 'information_schema')\n" +
                "    AND tablename NOT IN (SELECT relname FROM partition_children)\n" +
                "UNION ALL\n" +
                "SELECT\n" +
                "    viewname AS tablename\n" +
                "FROM\n" +
                "    pg_views\n" +
                "WHERE\n" +
                "    schemaname NOT IN ('pg_catalog', 'information_schema')\n" +
                "ORDER BY\n" +
                "    tablename\n";

        Set<String> tables = new HashSet<>();
        try (Connection conn = context.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tables.add(rs.getString("tablename"));
            }
        }
        return tables;
    }

    private boolean shouldSkip(IntrospectedTable introspectedTable) {
        if (allowlist == null) {
            return false; // If discovery failed we would have thrown; here null only before setProperties run in tests
        }
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        boolean skip;
        if (caseInsensitive) {
            if (allowlistNormalized == null) { // lazy build if injected via reflection in tests
                buildNormalizedAllowlist();
            }
            String key = tableName == null ? null : tableName.toLowerCase(Locale.ROOT);
            skip = key == null || !allowlistNormalized.contains(key);
        } else {
            skip = !allowlist.contains(tableName);
        }
        if (skip) {
            log.debug("Skipping partitioned table: {} (caseInsensitive={})", tableName, caseInsensitive);
        }
        return skip;
    }

    // Model generation hooks
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !shouldSkip(introspectedTable);
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !shouldSkip(introspectedTable);
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !shouldSkip(introspectedTable);
    }

    // Dynamic SQL support generation hook
    @Override
    public boolean dynamicSqlSupportGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !shouldSkip(introspectedTable);
    }

    // Client (mapper) generation hooks
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        return !shouldSkip(introspectedTable);
    }
}
