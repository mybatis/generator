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
import org.mybatis.generator.api.dom.java.JavaVisibility

class FieldRendererTest {
    @Test
    fun testBasicField() {
        val f = Field("name", FullyQualifiedJavaType.getStringInstance())
        assertThat(toString(f)).isEqualTo("String name;")
    }

    @Test
    fun testBasicFieldWithInitializationString() {
        val f = Field("name", FullyQualifiedJavaType.getStringInstance())
        f.setInitializationString(""""Fred"""")
        assertThat(toString(f)).isEqualTo("""String name = "Fred";""")
    }

    @Test
    fun testPrivateFieldWithInitializationString() {
        val f = Field("name", FullyQualifiedJavaType.getStringInstance())
        f.setVisibility(JavaVisibility.PRIVATE)
        f.setInitializationString(""""Fred"""")
        assertThat(toString(f)).isEqualTo("""private String name = "Fred";""")
    }

    @Test
    fun testPrivateStaticFieldWithInitializationString() {
        val f = Field("name", FullyQualifiedJavaType.getStringInstance())
        f.setVisibility(JavaVisibility.PRIVATE)
	f.setStatic(true)
	f.setFinal(true)
        f.setInitializationString(""""Fred"""")
        assertThat(toString(f)).isEqualTo("""private static final String name = "Fred";""")
    }

    @Test
    fun testPrivateTransientFieldWithInitializationString() {
        val f = Field("name", FullyQualifiedJavaType.getStringInstance())
        f.setVisibility(JavaVisibility.PRIVATE)
	f.setTransient(true)
	f.setVolatile(true)
        f.setInitializationString(""""Fred"""")
        assertThat(toString(f)).isEqualTo("""private transient volatile String name = "Fred";""")
    }

    @Test
    fun testPrivateFieldWithInitializationStringJavadocAndAnnotations() {
        val f = Field("name", FullyQualifiedJavaType.getStringInstance())
        f.setVisibility(JavaVisibility.PRIVATE)
        f.setInitializationString(""""Fred"""")
	
	f.addAnnotation("@Generated")
	f.addJavaDocLine("/**")
	f.addJavaDocLine(" * Some Javadoc")
	f.addJavaDocLine(" */")
		
        assertThat(toString(f)).isEqualTo("""
                |/**
                | * Some Javadoc
                | */
                |@Generated
                |private String name = "Fred";
                """.trimMargin())
    }

    private fun toString(f: Field) = FieldRenderer().render(f, null)
                .joinToString(System.getProperty("line.separator"))
}
