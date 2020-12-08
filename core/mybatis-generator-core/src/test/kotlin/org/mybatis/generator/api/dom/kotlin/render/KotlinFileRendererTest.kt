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
import org.mybatis.generator.api.dom.kotlin.KotlinFile

class KotlinFileRendererTest {

    @Test
    fun testWithCommentAndPackageAndImports() {
        val kf = KotlinFile("TestFile")
        kf.addFileCommentLine("/*")
        kf.addFileCommentLine(" * some comment")
        kf.addFileCommentLine(" */")

        kf.setPackage("com.foo.bar")

        kf.addImport("org.junit.jupiter.api.Test")
        kf.addImport("org.mybatis.generator.api.dom.kotlin.KotlinFile")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
                |/*
                | * some comment
                | */
                |package com.foo.bar
                |
                |import org.junit.jupiter.api.Test
                |import org.mybatis.generator.api.dom.kotlin.KotlinFile
                """.trimMargin())
        assertThat(kf.fileName).isEqualTo("TestFile.kt")
    }

    @Test
    fun testWithCommentAndImports() {
        val kf = KotlinFile("TestFile")
        kf.addFileCommentLine("/*")
        kf.addFileCommentLine(" * some comment")
        kf.addFileCommentLine(" */")

        kf.addImport("org.junit.jupiter.api.Test")
        kf.addImport("org.mybatis.generator.api.dom.kotlin.KotlinFile")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
                |/*
                | * some comment
                | */
                |import org.junit.jupiter.api.Test
                |import org.mybatis.generator.api.dom.kotlin.KotlinFile
                """.trimMargin())
    }

    @Test
    fun testWithCommentAndPackage() {
        val kf = KotlinFile("TestFile")
        kf.addFileCommentLine("/*")
        kf.addFileCommentLine(" * some comment")
        kf.addFileCommentLine(" */")

        kf.setPackage("com.foo.bar")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
                |/*
                | * some comment
                | */
                |package com.foo.bar
                """.trimMargin())
    }

    @Test
    fun testWithPackageAndImports() {
        val kf = KotlinFile("TestFile")
        kf.setPackage("com.foo.bar")

        kf.addImport("org.junit.jupiter.api.Test")
        kf.addImport("org.mybatis.generator.api.dom.kotlin.KotlinFile")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
                |package com.foo.bar
                |
                |import org.junit.jupiter.api.Test
                |import org.mybatis.generator.api.dom.kotlin.KotlinFile
                """.trimMargin())
    }

    @Test
    fun testWithComment() {
        val kf = KotlinFile("TestFile")
        kf.addFileCommentLine("/*")
        kf.addFileCommentLine(" * some comment")
        kf.addFileCommentLine(" */")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
                |/*
                | * some comment
                | */
                """.trimMargin())
    }

    @Test
    fun testWithPackage() {
        val kf = KotlinFile("TestFile")
        kf.setPackage("com.foo.bar")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
                |package com.foo.bar
                """.trimMargin())
    }

    @Test
    fun testWithImports() {
        val kf = KotlinFile("TestFile")

        kf.addImport("org.junit.jupiter.api.Test")
        kf.addImport("org.mybatis.generator.api.dom.kotlin.KotlinFile")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
                |import org.junit.jupiter.api.Test
                |import org.mybatis.generator.api.dom.kotlin.KotlinFile
                """.trimMargin())
    }

    @Test
    fun testEmpty() {
        val kf = KotlinFile("TestFile.kt")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualTo("")
        assertThat(kf.fileName).isEqualTo("TestFile.kt")
    }
}
