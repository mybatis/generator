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
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.TopLevelClass
import org.mybatis.generator.api.dom.java.TypeParameter

class TypeParameterRendererTest {
    @Test
    fun testSimpleTypeParameterRender() {
        val renderer = TypeParameterRenderer()
        val tp = TypeParameter("someName")

        assertThat(renderer.render(tp, null)).isEqualTo("someName")
    }

    @Test
    fun testComplexTypeParameterRenderNoCU() {
        val renderer = TypeParameterRenderer()
	val extendsTypes =listOf(FullyQualifiedJavaType("com.foo.Bar"))
        val tp = TypeParameter("someName", extendsTypes)
	
        assertThat(renderer.render(tp, null)).isEqualTo("someName extends Bar")
    }


    @Test
    fun testComplexTypeParameterRenderWithCU() {
        val renderer = TypeParameterRenderer()
	val tlc = TopLevelClass(FullyQualifiedJavaType("com.foo.Baz"))
	tlc.addImportedType("com.bar.Foo2")
	val extendsTypes =listOf(FullyQualifiedJavaType("java.lang.String"),
	        FullyQualifiedJavaType("com.foo.Bar"), FullyQualifiedJavaType("com.bar.Foo"),
	        FullyQualifiedJavaType("com.bar.Foo2"))
        val tp = TypeParameter("someName", extendsTypes)
	
        assertThat(renderer.render(tp, tlc)).isEqualTo("someName extends String & Bar & com.bar.Foo & Foo2")
    }
}