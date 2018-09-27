/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.api.dom.java.render

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.generator.api.dom.java.InitializationBlock

class InitializationBlockRendererTest {

    @Test
    fun testStaticBlock() {
        val block = InitializationBlock(true)

	block.addJavaDocLine("/**")
	block.addJavaDocLine(" * Some Javadoc")
	block.addJavaDocLine(" */")
	block.addBodyLine("i = 3;")

        assertThat(toString(block)).isEqualTo("""
                |/**
                | * Some Javadoc
                | */
                |static {
                |    i = 3;
                |}
                """.trimMargin())
    }

    @Test
    fun testNonStaticBlock() {
        val block = InitializationBlock()

	block.addJavaDocLine("/**")
	block.addJavaDocLine(" * Some Javadoc")
	block.addJavaDocLine(" */")
	block.addBodyLine("i = 3;")

        assertThat(toString(block)).isEqualTo("""
                |/**
                | * Some Javadoc
                | */
                |{
                |    i = 3;
                |}
                """.trimMargin())
    }

    private fun toString(b: InitializationBlock) = InitializationBlockRenderer().render(b)
                .joinToString(System.getProperty("line.separator"))
}