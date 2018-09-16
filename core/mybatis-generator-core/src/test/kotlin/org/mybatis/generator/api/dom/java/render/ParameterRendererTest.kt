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
import org.mybatis.generator.api.dom.java.Parameter
import org.mybatis.generator.api.dom.java.TopLevelClass

class ParameterRendererTest {
    @Test
    fun testBasicParameter() {
        val renderer = ParameterRenderer();
        val parameter = Parameter(FullyQualifiedJavaType.getStringInstance(), "firstName")

        assertThat(renderer.render(parameter, null)).isEqualTo("String firstName")
    }

    @Test
    fun testBasicParameterWithAnnotation() {
        val renderer = ParameterRenderer();
        val parameter = Parameter(FullyQualifiedJavaType.getStringInstance(), "firstName")
        parameter.addAnnotation("""@Param("firstName")""")

        assertThat(renderer.render(parameter, null)).isEqualTo("""@Param("firstName") String firstName""")
    }
	
    @Test
    fun testBasicVarargsParameter() {
        val renderer = ParameterRenderer();
        val parameter = Parameter(FullyQualifiedJavaType.getStringInstance(), "names", true)

        assertThat(renderer.render(parameter, null)).isEqualTo("String ... names")
    }

    @Test
    fun testParameterAndCuInSamePackage() {
        val renderer = ParameterRenderer();
        val cu = TopLevelClass("com.foo.Baz")
        val parameter = Parameter(FullyQualifiedJavaType("com.foo.Bar"), "parm")

        assertThat(renderer.render(parameter, cu)).isEqualTo("Bar parm")
	
    }

    @Test
    fun testParameterAndCuInSamePackageWithArguments() {
        val renderer = ParameterRenderer();
        val cu = TopLevelClass("com.foo.Baz")
        val parameter = Parameter(FullyQualifiedJavaType("com.foo.Bar<java.lang.String>"), "parm")

        assertThat(renderer.render(parameter, cu)).isEqualTo("Bar<String> parm")
	
    }

    @Test
    fun testParameterAndCuInDifferentPackage() {
        val renderer = ParameterRenderer();
        val cu = TopLevelClass("com.bar.Foo")
        val parameter = Parameter(FullyQualifiedJavaType("com.foo.Bar"), "parm")

        assertThat(renderer.render(parameter, cu)).isEqualTo("com.foo.Bar parm")
	
    }
}