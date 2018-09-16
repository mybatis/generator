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
import org.mybatis.generator.api.dom.java.JavaVisibility
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.Parameter
import org.mybatis.generator.api.dom.java.TypeParameter

class MethodRendererTest {

    @Test
    fun testSimpleGetterMethod() {
        val method = Method("getName");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.setVisibility(JavaVisibility.PUBLIC)
        method.addBodyLine("return name;")

        assertThat(toString(method)).isEqualTo("""
                |public String getName() {
                |    return name;
                |}
                """.trimMargin())
    }

    @Test
    fun testSimpleSetterMethod() {
        val method = Method("setName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.addParameter(Parameter(FullyQualifiedJavaType.getStringInstance(), "name"))
        method.addBodyLine("this.name = name;")

        assertThat(toString(method)).isEqualTo("""
                |public void setName(String name) {
                |    this.name = name;
                |}
                """.trimMargin())
    }

    @Test
    fun testMethodWithIf() {
        val method = Method("setName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.addParameter(Parameter(FullyQualifiedJavaType.getStringInstance(), "name"))
        method.addBodyLine("if(name == null) {")
        method.addBodyLine("this.name = null;")
        method.addBodyLine("} else {")
        method.addBodyLine("this.name = name;")
        method.addBodyLine("}")

        assertThat(toString(method)).isEqualTo("""
                |public void setName(String name) {
                |    if(name == null) {
                |        this.name = null;
                |    } else {
                |        this.name = name;
                |    }
                |}
                """.trimMargin())
    }

    @Test
    fun testMethodWithSwitch() {
        val method = Method("setType");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.addParameter(Parameter(FullyQualifiedJavaType.getIntInstance(), "type"))
        method.addBodyLine("switch(type) {")
        method.addBodyLine("case 1:")
        method.addBodyLine("""this.name = "Fred";""")
        method.addBodyLine("break;")
        method.addBodyLine("case 2:")
        method.addBodyLine("""this.name = "Barney";""")
        method.addBodyLine("break;")
        method.addBodyLine("default:")
        method.addBodyLine("""this.name = "Wilma";""")
        method.addBodyLine("break;")
        method.addBodyLine("}")

        assertThat(toString(method)).isEqualTo("""
                |public void setType(int type) {
                |    switch(type) {
                |    case 1:
                |        this.name = "Fred";
                |        break;
                |    case 2:
                |        this.name = "Barney";
                |        break;
                |    default:
                |        this.name = "Wilma";
                |        break;
                |    }
                |}
                """.trimMargin())
    }

    @Test
    fun testTypeParameters() {
        val method = Method("getName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addBodyLine("""return "Fred";""")

        assertThat(toString(method)).isEqualTo("""
                |public <R> String getName(R name) {
                |    return "Fred";
                |}
                """.trimMargin())
    }

    @Test
    fun testMultipleTypeParameters() {
        val method = Method("getName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addTypeParameter(TypeParameter("T"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addBodyLine("""return "Fred";""")

        assertThat(toString(method)).isEqualTo("""
                |public <R, T> String getName(R name) {
                |    return "Fred";
                |}
                """.trimMargin())
    }

    @Test
    fun testMultipleParameters() {
        val method = Method("getName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addTypeParameter(TypeParameter("T"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addParameter(Parameter(FullyQualifiedJavaType("T"), "type"))
        method.addBodyLine("""return "Fred";""")

        assertThat(toString(method)).isEqualTo("""
                |public <R, T> String getName(R name, T type) {
                |    return "Fred";
                |}
                """.trimMargin())
    }

    @Test
    fun testExceptions() {
        val method = Method("getName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addException(FullyQualifiedJavaType("Exception"))
        method.addBodyLine("""return "Fred";""")

        assertThat(toString(method)).isEqualTo("""
                |public <R> String getName(R name) throws Exception {
                |    return "Fred";
                |}
                """.trimMargin())
    }

    @Test
    fun testAbstract() {
        val method = Method("getName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addException(FullyQualifiedJavaType("Exception"))
        method.setAbstract(true)
        method.addBodyLine("""return "Fred";""") // should be ignored

        assertThat(toString(method)).isEqualTo("""
                |public abstract <R> String getName(R name) throws Exception;
                """.trimMargin())
    }

    @Test
    fun testAbstractInInterface() {
        val method = Method("getName");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addException(FullyQualifiedJavaType("Exception"))
        method.setAbstract(true)
        method.setVisibility(JavaVisibility.PUBLIC)  // should be ignored
        method.addBodyLine("""return "Fred";""") // should be ignored

        assertThat(toString(method, true)).isEqualTo("""
                |<R> String getName(R name) throws Exception;
                """.trimMargin())
    }

    @Test
    fun testPrivateInInterface() {
        val method = Method("getName");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addException(FullyQualifiedJavaType("Exception"))
        method.setVisibility(JavaVisibility.PRIVATE)
        method.addBodyLine("""return "Fred";""") // should be ignored

        assertThat(toString(method, true)).isEqualTo("""
                |private <R> String getName(R name) throws Exception {
                |    return "Fred";
                |}
                """.trimMargin())
    }

    @Test
    fun testNative() {
        val method = Method("getName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setReturnType(FullyQualifiedJavaType.getStringInstance())
        method.addTypeParameter(TypeParameter("R"))
        method.addParameter(Parameter(FullyQualifiedJavaType("R"), "name"))
        method.addException(FullyQualifiedJavaType("Exception"))
        method.setNative(true)
        method.addBodyLine("""return "Fred";""") // should be ignored

        assertThat(toString(method)).isEqualTo("""
                |public native <R> String getName(R name) throws Exception;
                """.trimMargin())
    }

    @Test
    fun testEmptyMethod() {
        val method = Method("setName");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.addParameter(Parameter(FullyQualifiedJavaType.getStringInstance(), "name"))

        assertThat(toString(method)).isEqualTo("""
                |public void setName(String name) {
                |}
                """.trimMargin())
    }

    @Test
    fun testConstructor() {
        val method = Method("MyClass");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setConstructor(true)
        method.addParameter(Parameter(FullyQualifiedJavaType.getStringInstance(), "name"))
        method.addBodyLine("super(name);")
        method.setReturnType(FullyQualifiedJavaType.getStringInstance()) // should be ignored

        assertThat(toString(method)).isEqualTo("""
                |public MyClass(String name) {
                |    super(name);
                |}
                """.trimMargin())
    }

    @Test
    fun testJavadocAndAnnotations() {
        val method = Method("MyClass");
        method.setVisibility(JavaVisibility.PUBLIC)
        method.setConstructor(true)
        method.addParameter(Parameter(FullyQualifiedJavaType.getStringInstance(), "name"))
        method.addBodyLine("super(name);")

        method.addAnnotation("@Generated")
	method.addJavaDocLine("/**")
	method.addJavaDocLine(" * Some Javadoc")
	method.addJavaDocLine(" */")


        assertThat(toString(method)).isEqualTo("""
                |/**
                | * Some Javadoc
                | */
                |@Generated
                |public MyClass(String name) {
                |    super(name);
                |}
                """.trimMargin())
    }

    private fun toString(m: Method, inInterface: Boolean = false) = MethodRenderer().render(m, inInterface, null)
                .joinToString(System.getProperty("line.separator"))
}
