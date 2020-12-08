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
import org.mybatis.generator.api.dom.kotlin.KotlinArg
import org.mybatis.generator.api.dom.kotlin.KotlinFunction
import java.util.stream.Collectors

class KotlinFunctionTest {
    @Test
    fun testMultilineFunction() {
        val kf = KotlinFunction.newMultiLineFunction("add")
                .withArgument(KotlinArg.newArg("a").withDataType("Int").build())
                .withArgument(KotlinArg.newArg("b").withDataType("Int").build())
                .withExplicitReturnType("Int")
                .withCodeLine("val answer = a + b")
                .withCodeLine("return answer")
                .build()

        val renderedFunction = KotlinFunctionRenderer().render(kf).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedFunction).isEqualToNormalizingNewlines("""
            |fun add(a: Int, b: Int): Int {
            |    val answer = a + b
            |    return answer
            |}
            """.trimMargin())
    }

    @Test
    fun testMultilineFunctionWithDefaultValue() {
        val kf = KotlinFunction.newMultiLineFunction("add")
                .withArgument(KotlinArg.newArg("a").withDataType("Int").withInitializationString("1").build())
                .withArgument(KotlinArg.newArg("b").withDataType("Int").withInitializationString(("2")).build())
                .withExplicitReturnType("Int")
                .withCodeLine("val answer = a + b")
                .withCodeLine("return answer")
                .build()

        val renderedFunction = KotlinFunctionRenderer().render(kf).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedFunction).isEqualToNormalizingNewlines("""
            |fun add(a: Int = 1, b: Int = 2): Int {
            |    val answer = a + b
            |    return answer
            |}
            """.trimMargin())
    }

    @Test
    fun testMultilineFunctionWithAnnotation() {
        val kf = KotlinFunction.newMultiLineFunction("add")
                .withArgument(KotlinArg.newArg("a").withDataType("Int").withInitializationString("1")
                        .withAnnotation("@Param(\"a\")")
                        .build())
                .withArgument(KotlinArg.newArg("b").withDataType("Int").withInitializationString(("2")).build())
                .withExplicitReturnType("Int")
                .withCodeLine("val answer = a + b")
                .withCodeLine("return answer")
                .build()

        val renderedFunction = KotlinFunctionRenderer().render(kf).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedFunction).isEqualToNormalizingNewlines("""
            |fun add(@Param("a") a: Int = 1, b: Int = 2): Int {
            |    val answer = a + b
            |    return answer
            |}
            """.trimMargin())
    }
}
