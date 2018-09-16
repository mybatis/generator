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
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.InnerClass
import org.mybatis.generator.api.dom.java.InnerEnum
import org.mybatis.generator.api.dom.java.InnerInterface
import org.mybatis.generator.api.dom.java.JavaVisibility
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.Parameter
import org.mybatis.generator.api.dom.java.TopLevelEnumeration

class InnerEnumRendererTest {
    @Test
    fun testSimpleEnum() {
        val outerEnum = InnerEnum("com.foo.Bar")
	outerEnum.setVisibility(JavaVisibility.PUBLIC)
        outerEnum.addEnumConstant("ONE")
        outerEnum.addEnumConstant("TWO")
        outerEnum.addEnumConstant("THREE")

        assertThat(toString(outerEnum)).isEqualTo("""
                |public enum Bar {
                |    ONE,
                |    TWO,
                |    THREE;
                |}
                """.trimMargin())
    }

    @Test
    fun testComplexEnum() {
        val outerEnum = TopLevelEnumeration("com.foo.Bar")
	outerEnum.setVisibility(JavaVisibility.PUBLIC)
        outerEnum.addEnumConstant("""ONE("One")""")
        outerEnum.addEnumConstant("""TWO("Two")""")
        outerEnum.addEnumConstant("""THREE("Three")""")
	val constructor = Method("Bar")
	constructor.setConstructor(true)
	constructor.setVisibility(JavaVisibility.PRIVATE)
	constructor.addParameter(Parameter(FullyQualifiedJavaType.getStringInstance(), "value"))
	constructor.addBodyLine("this.value = value;")
	outerEnum.addMethod(constructor)
	val outerField = Field("value", FullyQualifiedJavaType.getStringInstance())
	outerField.setVisibility(JavaVisibility.PRIVATE)
	outerEnum.addField(outerField)
	val outerMethod = Method("getValue")
	outerMethod.setReturnType(FullyQualifiedJavaType.getStringInstance())
	outerMethod.setVisibility(JavaVisibility.PUBLIC)
	outerMethod.addBodyLine("return value;")
	outerEnum.addMethod(outerMethod)

        val innerEnum = InnerEnum("InnerEnum")
        innerEnum.setVisibility(JavaVisibility.PUBLIC)
        innerEnum.setStatic(true)
        innerEnum.addEnumConstant("FOUR")
        innerEnum.addEnumConstant("FIVE")
        innerEnum.addEnumConstant("SIX")
        outerEnum.addInnerEnum(innerEnum)

        val innerClass = InnerClass("InnerClass")
	innerClass.setVisibility(JavaVisibility.PUBLIC)
	innerClass.setStatic(true)
	val field = Field("fred", FullyQualifiedJavaType.getStringInstance())
	field.setVisibility(JavaVisibility.PRIVATE)
	innerClass.addField(field)
	outerEnum.addInnerClass(innerClass)

        val innerInterface = InnerInterface("InnerInterface")
	innerInterface.setVisibility(JavaVisibility.PUBLIC)
	innerInterface.setStatic(true)
	val method = Method("getName")
	method.setVisibility(JavaVisibility.PUBLIC)
	method.setDefault(true)
	method.setReturnType(FullyQualifiedJavaType.getStringInstance())
	method.addBodyLine("""return "Fred";""")
	innerInterface.addMethod(method)
	outerEnum.addInnerInterface(innerInterface)

        assertThat(TopLevelEnumerationRenderer().render(outerEnum)).isEqualTo("""
                |package com.foo;
                |
                |public enum Bar {
                |    ONE("One"),
                |    TWO("Two"),
                |    THREE("Three");
                |
                |    private String value;
                |
                |    private Bar(String value) {
                |        this.value = value;
                |    }
                |
                |    public String getValue() {
                |        return value;
                |    }
                |
                |    public static class InnerClass {
                |        private String fred;
                |    }
                |
                |    public static interface InnerInterface {
                |        default String getName() {
                |            return "Fred";
                |        }
                |    }
                |
                |    public static enum InnerEnum {
                |        FOUR,
                |        FIVE,
                |        SIX;
                |    }
                |}
                """.trimMargin())
    }

    private fun toString(en: InnerEnum) = InnerEnumRenderer().render(en, null)
                .joinToString(System.getProperty("line.separator"))
}