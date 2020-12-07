/*
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api.dom.kotlin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

class JavaToKotlinTypeConverterTest {

    @Test
    void testPrimitiveByte() {
        FullyQualifiedJavaType jt = new FullyQualifiedJavaType("byte");
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(jt);

        assertThat(kt.getShortNameWithTypeArguments()).isEqualTo("Byte");
        assertThat(kt.getImportList()).isEmpty();
    }

    @Test
    void testPrimitiveByteArray() {
        FullyQualifiedJavaType jt = new FullyQualifiedJavaType("byte[]");
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(jt);

        assertThat(kt.getShortNameWithTypeArguments()).isEqualTo("ByteArray");
        assertThat(kt.getImportList()).isEmpty();
    }

    @Test
    void testByteWrapper() {
        FullyQualifiedJavaType jt = new FullyQualifiedJavaType("java.lang.Byte");
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(jt);

        assertThat(kt.getShortNameWithTypeArguments()).isEqualTo("Byte");
        assertThat(kt.getImportList()).isEmpty();
    }

    @Test
    void testByteWrapperArray() {
        FullyQualifiedJavaType jt = new FullyQualifiedJavaType("java.lang.Byte[]");
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(jt);

        assertThat(kt.getShortNameWithTypeArguments()).isEqualTo("Array<Byte>");
        assertThat(kt.getImportList()).isEmpty();
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
