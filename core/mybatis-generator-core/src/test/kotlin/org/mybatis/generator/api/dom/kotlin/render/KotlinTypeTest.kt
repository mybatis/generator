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
import org.mybatis.generator.api.dom.kotlin.KotlinFile
import org.mybatis.generator.api.dom.kotlin.KotlinFunction
import org.mybatis.generator.api.dom.kotlin.KotlinModifier
import org.mybatis.generator.api.dom.kotlin.KotlinProperty
import org.mybatis.generator.api.dom.kotlin.KotlinType
import java.util.stream.Collectors

class KotlinTypeTest {
    @Test
    fun testDataClass() {
        val obj = KotlinType.newClass("AddressRecord")
                .withModifier(KotlinModifier.DATA)
                .withConstructorProperty(KotlinProperty.newVar("id")
                    .withDataType("Int?")
                    .withInitializationString("null")
                    .build())
                .withConstructorProperty(KotlinProperty.newVar("streetAddress")
                    .withDataType("String?")
                    .withInitializationString("null")
                    .build())
                .build()

        val kf = KotlinFile("AddressRecord")
        kf.setPackage("com.foo.bar")
        kf.addNamedItem(obj)

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
            |package com.foo.bar
            |
            |data class AddressRecord(
            |    var id: Int? = null,
            |    var streetAddress: String? = null
            |)
            """.trimMargin())
    }

    @Test
    fun testSerializableDataClass() {
        val obj = KotlinType.newClass("AddressRecord")
            .withModifier(KotlinModifier.DATA)
            .withConstructorProperty(KotlinProperty.newVar("id")
                .withDataType("Int?")
                .withInitializationString("null")
                .build())
            .withConstructorProperty(KotlinProperty.newVar("streetAddress")
                .withDataType("String?")
                .withInitializationString("null")
                .build())
            .withSuperType("Serializable")
            .build()

        val kf = KotlinFile("AddressRecord")
        kf.setPackage("com.foo.bar")
        kf.addNamedItem(obj)
        kf.addImport("java.io.Serializable")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
            |package com.foo.bar
            |
            |import java.io.Serializable
            |
            |data class AddressRecord(
            |    var id: Int? = null,
            |    var streetAddress: String? = null
            |) : Serializable
            """.trimMargin())
    }

    @Test
    fun testDSQLSupportObject() {
        val outerObj = KotlinType.newObject("AddressDynamicSqlSupport")
                .withNamedItem(KotlinType.newObject("Address")
                        .withSuperType("SqlTable(\"Address\")")
                        .withNamedItem(KotlinProperty.newVal("id")
                                .withInitializationString("column<Int>(\"address_id\", JDBCType.INTEGER)")
                                .build())
                        .withNamedItem(KotlinProperty.newVal("streetAddress")
                                .withInitializationString("column<String>(\"street_address\", JDBCType.VARCHAR)")
                                .build())
                        .withNamedItem(KotlinProperty.newVal("city")
                                .withInitializationString("column<String>(\"city\", JDBCType.VARCHAR)")
                                .build())
                        .withNamedItem(KotlinProperty.newVal("state")
                                .withInitializationString("column<String>(\"state\", JDBCType.VARCHAR)")
                                .build())
                        .build())
                .build()

        val kf = KotlinFile("AddressDynamicSqlSupport.kt")
        kf.setPackage("examples.kotlin.mybatis3.canonical")
        kf.addNamedItem(outerObj)

        kf.addImport("org.mybatis.dynamic.sql.SqlTable")
        kf.addImport("java.sql.JDBCType")

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
            |package examples.kotlin.mybatis3.canonical
            |
            |import java.sql.JDBCType
            |import org.mybatis.dynamic.sql.SqlTable
            |
            |object AddressDynamicSqlSupport {
            |    object Address : SqlTable("Address") {
            |        val id = column<Int>("address_id", JDBCType.INTEGER)
            |
            |        val streetAddress = column<String>("street_address", JDBCType.VARCHAR)
            |
            |        val city = column<String>("city", JDBCType.VARCHAR)
            |
            |        val state = column<String>("state", JDBCType.VARCHAR)
            |    }
            |}
            """.trimMargin())
    }

    @Test
    fun testMapper() {
        val kf = KotlinFile("PersonMapper")

        kf.setPackage("examples.kotlin.mybatis3.canonical")

        kf.addImport("org.apache.ibatis.annotations.*")
        kf.addImport("org.apache.ibatis.type.JdbcType")
        kf.addImport("org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider")
        kf.addImport("org.mybatis.dynamic.sql.select.render.SelectStatementProvider")
        kf.addImport("org.mybatis.dynamic.sql.util.SqlProviderAdapter")

        val interfaze = KotlinType.newInterface("PersonMapper")
                .withAnnotation(("@Mapper"))
                .withNamedItem(KotlinFunction.newOneLineFunction("count")
                        .withExplicitReturnType("Long")
                        .withArgument(KotlinArg.newArg("selectStatement")
                                .withDataType("SelectStatementProvider")
                                .build())
                        .withAnnotation("@SelectProvider(type = SqlProviderAdapter::class, method = \"select\")")
                        .build())
                .withNamedItem(KotlinFunction.newOneLineFunction("delete")
                        .withExplicitReturnType("Int")
                        .withArgument(KotlinArg.newArg("deleteStatement")
                                .withDataType("DeleteStatementProvider")
                                .build())
                        .withAnnotation("@DeleteProvider(type = SqlProviderAdapter::class, method = \"delete\")")
                        .build())
                .build()
        kf.addNamedItem(interfaze)

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
            |package examples.kotlin.mybatis3.canonical
            |
            |import org.apache.ibatis.annotations.*
            |import org.apache.ibatis.type.JdbcType
            |import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider
            |import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
            |import org.mybatis.dynamic.sql.util.SqlProviderAdapter
            |
            |@Mapper
            |interface PersonMapper {
            |    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
            |    fun count(selectStatement: SelectStatementProvider): Long
            |
            |    @DeleteProvider(type = SqlProviderAdapter::class, method = "delete")
            |    fun delete(deleteStatement: DeleteStatementProvider): Int
            |}
            """.trimMargin())
    }

    @Test
    fun testExtensionsFile() {
        val kf = KotlinFile("PersonMapperExtensions")

        kf.setPackage("examples.kotlin.mybatis3.canonical")

        kf.addImport("org.apache.ibatis.annotations.*")
        kf.addImport("org.apache.ibatis.type.JdbcType")
        kf.addImport("org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider")
        kf.addImport("org.mybatis.dynamic.sql.select.render.SelectStatementProvider")
        kf.addImport("org.mybatis.dynamic.sql.util.SqlProviderAdapter")

        kf.addNamedItem(KotlinFunction.newOneLineFunction("PersonMapper.count")
                .withArgument(KotlinArg.newArg("completer")
                        .withDataType("CountCompleter")
                        .build())
                .withCodeLine("countFrom(this::count, Person, completer)")
                .build())

        kf.addNamedItem(KotlinFunction.newOneLineFunction("PersonMapper.insert")
                .withArgument(KotlinArg.newArg("record")
                        .withDataType("PersonRecord")
                        .build())
                .withCodeLine("insert(this::insert, record, Person) {")
                .withCodeLine("    map(id).toProperty(\"id\")")
                .withCodeLine("    map(firstName).toProperty(\"firstName\")")
                .withCodeLine("}")
                .build())

        val renderedKf = KotlinFileRenderer().render(kf)

        assertThat(renderedKf).isEqualToNormalizingNewlines("""
            |package examples.kotlin.mybatis3.canonical
            |
            |import org.apache.ibatis.annotations.*
            |import org.apache.ibatis.type.JdbcType
            |import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider
            |import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
            |import org.mybatis.dynamic.sql.util.SqlProviderAdapter
            |
            |fun PersonMapper.count(completer: CountCompleter) =
            |    countFrom(this::count, Person, completer)
            |
            |fun PersonMapper.insert(record: PersonRecord) =
            |    insert(this::insert, record, Person) {
            |        map(id).toProperty("id")
            |        map(firstName).toProperty("firstName")
            |    }
            """.trimMargin())
    }

    @Test
    fun testRegularClass() {
        val obj = KotlinType.newClass("Adder")
                .withConstructorProperty(KotlinProperty.newVal("a")
                        .withDataType("Int")
                        .build())
                .withConstructorProperty(KotlinProperty.newVal("b")
                        .withDataType("Int")
                        .build())
                .withNamedItem(KotlinFunction.newOneLineFunction("sum")
                        .withCodeLine("a + b")
                        .build())
                .build()

        val renderedType = KotlinTypeRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedType).isEqualToNormalizingNewlines("""
            |class Adder(
            |    val a: Int,
            |    val b: Int
            |) {
            |    fun sum() =
            |        a + b
            |}
            """.trimMargin())
    }

    @Test
    fun testRegularClassSerializable() {
        val obj = KotlinType.newClass("Adder")
            .withConstructorProperty(KotlinProperty.newVal("a")
                .withDataType("Int")
                .build())
            .withConstructorProperty(KotlinProperty.newVal("b")
                .withDataType("Int")
                .build())
            .withNamedItem(KotlinFunction.newOneLineFunction("sum")
                .withCodeLine("a + b")
                .build())
            .withSuperType("Serializable")
            .build()

        val renderedType = KotlinTypeRenderer().render(obj).stream()
            .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedType).isEqualToNormalizingNewlines("""
            |class Adder(
            |    val a: Int,
            |    val b: Int
            |) : Serializable {
            |    fun sum() =
            |        a + b
            |}
            """.trimMargin())
    }

    @Test
    fun testEmptyClass() {
        val obj = KotlinType.newClass("Adder")
                .build()

        val renderedType = KotlinTypeRenderer().render(obj).stream()
                .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedType).isEqualToNormalizingNewlines("""
            |class Adder
            """.trimMargin())
    }

    @Test
    fun testEmptyClassSerializable() {
        val obj = KotlinType.newClass("Adder")
            .withSuperType("Serializable")
            .build()

        val renderedType = KotlinTypeRenderer().render(obj).stream()
            .collect(Collectors.joining(System.getProperty("line.separator")))

        assertThat(renderedType).isEqualToNormalizingNewlines("""
            |class Adder : Serializable
            """.trimMargin())
    }
}
