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
package org.mybatis.generator.api.dom.kotlin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FullyQualifiedKotlinTypeTest {

    @ParameterizedTest
    @MethodSource("simpleVariants")
    void testSimpleVariant(String fullTypeSpec, String expectedShortName) {
        FullyQualifiedKotlinType fqjt = new FullyQualifiedKotlinType(fullTypeSpec);
        assertThat(fqjt.getShortNameWithTypeArguments()).isEqualTo(expectedShortName);
        assertThat(fqjt.getImportList()).isEmpty();
    }

    static Stream<Arguments> simpleVariants() {
        return Stream.of(
                Arguments.argumentSet("kotlin primitive", "kotlin.String", "String"),
                Arguments.argumentSet("kotlin primitive2", "String", "String"),
                Arguments.argumentSet("kotlin generic type 1",
                        "kotlin.collections.List<kotlin.String>", "List<String>"),
                Arguments.argumentSet("kotlin generic type 2", "List<kotlin.String>", "List<String>"),
                Arguments.argumentSet("kotlin generic type 3",
                        "kotlin.collections.Map<kotlin.String, kotlin.collections.List<kotlin.String>>",
                        "Map<String, List<String>>"),
                Arguments.argumentSet("kotlin generic type 4", "List<Map<String, String>>",
                        "List<Map<String, String>>")
        );
    }

    @ParameterizedTest
    @MethodSource("complexVariants")
    void testComplexVariant(String fullTypeSpec, String expectedShortName, List<String> expectedImportList) {
        FullyQualifiedKotlinType fqjt =
                new FullyQualifiedKotlinType(fullTypeSpec);
        assertThat(fqjt.getShortNameWithTypeArguments()).isEqualTo(expectedShortName);
        assertThat(fqjt.getImportList()).hasSize(expectedImportList.size());
        assertThat(fqjt.getImportList()).containsExactlyInAnyOrderElementsOf(expectedImportList);
    }

    static Stream<Arguments> complexVariants() {
        return Stream.of(
                Arguments.argumentSet("simple type 1", "com.foo.Bar", "Bar",
                        List.of("com.foo.Bar")),
                Arguments.argumentSet("simple type 2", "com.foo.bar", "bar",
                        List.of("com.foo.bar")),
                Arguments.argumentSet("uppercase package 1", "org.foo.Bar.Inner", "Inner",
                        List.of("org.foo.Bar.Inner")),
                Arguments.argumentSet("uppercase package 2", "org.foo.Bar.Inner.Inner", "Inner",
                        List.of("org.foo.Bar.Inner.Inner")),
                Arguments.argumentSet("uppercase package 3", "java.util.List<org.foo.Bar.Inner>",
                        "List<Inner>",
                        List.of("java.util.List", "org.foo.Bar.Inner"))
        );
    }

    @Test
    void testLateInitialization1() {
        FullyQualifiedKotlinType fqjt = new FullyQualifiedKotlinType("List");
        fqjt.addTypeArgument(new FullyQualifiedKotlinType("java.math.BigDecimal"));
        assertThat(fqjt.getShortNameWithTypeArguments()).isEqualTo("List<BigDecimal>"); //$NON-NLS-1$
        assertThat(fqjt.getImportList()).hasSize(1);
        assertThat(fqjt.getImportList()).contains("java.math.BigDecimal");
    }

    @Test
    void testLateInitialization2() {
        FullyQualifiedKotlinType inner = new FullyQualifiedKotlinType("some.generic.Thing");
        inner.addTypeArgument(new FullyQualifiedKotlinType("java.math.BigDecimal"));
        FullyQualifiedKotlinType fqjt = new FullyQualifiedKotlinType("kotlin.List");
        fqjt.addTypeArgument(inner);
        assertThat(fqjt.getShortNameWithTypeArguments()).isEqualTo("List<Thing<BigDecimal>>"); //$NON-NLS-1$
        assertThat(fqjt.getImportList()).hasSize(2);
        assertThat(fqjt.getImportList()).contains("java.math.BigDecimal", "some.generic.Thing");
    }
}
