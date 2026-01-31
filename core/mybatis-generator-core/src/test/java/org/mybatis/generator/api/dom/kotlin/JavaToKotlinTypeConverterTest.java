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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

class JavaToKotlinTypeConverterTest {

    @ParameterizedTest
    @MethodSource("primitiveVariations")
    void testPrimitiveVariation(FullyQualifiedJavaType javaType, String expected) {
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(javaType);

        assertThat(kt.getShortNameWithTypeArguments()).isEqualTo(expected);
        assertThat(kt.getImportList()).isEmpty();
    }

    static Stream<Arguments> primitiveVariations() {
        return Stream.of(
                Arguments.argumentSet("primitive byte", new FullyQualifiedJavaType("byte"), "Byte"),
                Arguments.argumentSet("primitive byte array", new FullyQualifiedJavaType("byte[]"), "ByteArray"),
                Arguments.argumentSet("byte wrapper", new FullyQualifiedJavaType("java.lang.Byte"), "Byte"),
                Arguments.argumentSet("byte wrapper array", new FullyQualifiedJavaType("java.lang.Byte[]"),
                        "Array<Byte>")
        );
    }

    @Test
    void testUnmappedType() {
        FullyQualifiedJavaType jt = new FullyQualifiedJavaType("java.math.BigDecimal");
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(jt);

        assertThat(kt.getShortNameWithTypeArguments()).isEqualTo("BigDecimal");
        assertThat(kt.getImportList()).hasSize(1);
        assertThat(kt.getImportList()).contains("java.math.BigDecimal");
    }

    @Test
    void testGenericType() {
        FullyQualifiedJavaType jt = new FullyQualifiedJavaType("java.util.List<java.math.BigDecimal>");
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(jt);

        assertThat(kt.getShortNameWithTypeArguments()).isEqualTo("List<BigDecimal>");
        assertThat(kt.getImportList()).hasSize(2);
        assertThat(kt.getImportList()).contains("java.math.BigDecimal", "java.util.List");
    }
}
