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
package org.mybatis.generator.api.dom.java.render

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.JavaVisibility
import org.mybatis.generator.api.dom.java.TopLevelClass

class TopLevelClassRendererTest {

    @Test
    fun testGH467() {
        val topLevelClass = TopLevelClass("com.test.GH467")
        topLevelClass.visibility = JavaVisibility.PUBLIC
        topLevelClass.addImportedType(FullyQualifiedJavaType("java.util.List<java.math.BigDecimal>"))

        val field = Field("listVal", FullyQualifiedJavaType("java.util.List<java.math.BigDecimal>"))
        field.visibility = JavaVisibility.PRIVATE
        topLevelClass.addField(field)

        val renderedTlc = TopLevelClassRenderer().render(topLevelClass)
        assertThat(renderedTlc).isEqualToNormalizingNewlines("""
                |package com.test;
                |
                |import java.math.BigDecimal;
                |import java.util.List;
                |
                |public class GH467 {
                |    private List<BigDecimal> listVal;
                |}
                """.trimMargin())
    }
}
