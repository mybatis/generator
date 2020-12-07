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
package org.mybatis.generator.api.dom.kotlin.render

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.generator.api.dom.kotlin.KotlinModifier
import org.mybatis.generator.api.dom.kotlin.KotlinProperty
import java.util.stream.Collectors

class KotlinPropertyTest {
    @Test
    fun testVal() {
        val obj = KotlinProperty.newVal("id")
                .withDataType("Int?")
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualTo("val id: Int?")
    }

    @Test
    fun testValWithInitializer() {
        val obj = KotlinProperty.newVal("id")
                .withDataType("Int?")
                .withInitializationString("null")
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualTo("val id: Int? = null")
    }

    @Test
    fun testPrivateVal() {
        val obj = KotlinProperty.newVal("id")
                .withDataType("Int?")
                .withModifier(KotlinModifier.PRIVATE)
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualTo("private val id: Int?")
    }

    @Test
    fun testVar() {
        val obj = KotlinProperty.newVar("id")
                .withDataType("Int?")
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualTo("var id: Int?")
    }

    @Test
    fun testVarWithInitializer() {
        val obj = KotlinProperty.newVar("id")
                .withDataType("Int?")
                .withInitializationString("null")
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualTo("var id: Int? = null")
    }

    @Test
    fun testPrivateVar() {
        val obj = KotlinProperty.newVar("id")
                .withDataType("Int?")
                .withModifier(KotlinModifier.PRIVATE)
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualTo("private var id: Int?")
    }

    @Test
    fun testNoDataType() {
        val obj = KotlinProperty.newVal("id")
                .withInitializationString("33")
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualTo("val id = 33")
    }

    @Test
    fun testAnnotation() {
        val obj = KotlinProperty.newVal("id")
                .withInitializationString("33")
                .withAnnotation("@SomeAnnotation")
                .build()

        val renderedObj = KotlinPropertyRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedObj).isEqualToNormalizingNewlines("""
            |@SomeAnnotation
            |val id = 33
        """.trimMargin())
    }
}
