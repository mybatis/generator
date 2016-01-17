package org.mybatis.generator.internal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilityTest {

    @Test
    public void testNoCatalog() {
        String answer = StringUtility.composeFullyQualifiedTableName(null, "schema", "table", '.');
        assertEquals("schema.table", answer);
    }

    @Test
    public void testNoSchema() {
        String answer = StringUtility.composeFullyQualifiedTableName("catalog", null, "table", '.');
        assertEquals("catalog..table", answer);
    }

    @Test
    public void testAllPresent() {
        String answer = StringUtility.composeFullyQualifiedTableName("catalog", "schema", "table", '.');
        assertEquals("catalog.schema.table", answer);
    }

    @Test
    public void testTableOnly() {
        String answer = StringUtility.composeFullyQualifiedTableName(null, null, "table", '.');
        assertEquals("table", answer);
    }
}
