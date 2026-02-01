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
package org.mybatis.generator.internal.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StringUtilityTest {

    @ParameterizedTest
    @MethodSource("tableNameVariations")
    void testTableNameVariation(String tableName, String expectedTableName) {
        assertThat(tableName).isEqualTo(expectedTableName);
    }

    static Stream<Arguments> tableNameVariations() {
        return Stream.of(
                Arguments.argumentSet("no catalog",
                        StringUtility.composeFullyQualifiedTableName(null, "schema", "table", '.'),
                        "schema.table"),
                Arguments.argumentSet("no schema",
                        StringUtility.composeFullyQualifiedTableName("catalog", null, "table", '.'),
                        "catalog..table"),
                Arguments.argumentSet("all present",
                        StringUtility.composeFullyQualifiedTableName("catalog", "schema", "table", '.'),
                        "catalog.schema.table"),
                Arguments.argumentSet("table only",
                        StringUtility.composeFullyQualifiedTableName(null, null, "table", '.'),
                        "table")
        );
    }

    @ParameterizedTest
    @MethodSource("snakeCaseVariations")
    void testSnakeCaseVariation(String input, String expected) {
        String answer = StringUtility.convertCamelCaseToSnakeCase(input);
        assertThat(answer).isEqualTo(expected);
    }

    static Stream<Arguments> snakeCaseVariations() {
        return Stream.of(
                Arguments.argumentSet("normal case", "userName", "USER_NAME"),
                Arguments.argumentSet("camel case email",
                        JavaBeansUtil.getValidPropertyName("eMailAddress"), "E_MAIL_ADDRESS"),
                Arguments.argumentSet("URL",
                        JavaBeansUtil.getValidPropertyName("URL"), "URL"),
                Arguments.argumentSet("XAxis",
                        JavaBeansUtil.getValidPropertyName("XAxis"), "X_AXIS"),
                Arguments.argumentSet("Yaxis",
                        JavaBeansUtil.getValidPropertyName("Yaxis"), "YAXIS")
        );
    }
}
