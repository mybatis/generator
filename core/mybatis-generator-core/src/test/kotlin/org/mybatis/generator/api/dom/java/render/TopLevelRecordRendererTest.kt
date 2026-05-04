/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
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
import org.mybatis.generator.api.dom.DefaultJavaFormatter
import org.mybatis.generator.api.Indenter
import org.mybatis.generator.api.dom.java.RecordGenerator
import org.mybatis.generator.api.IndentType

class TopLevelRecordRendererTest {
    @Test
    fun testComplexRecord() {
        val cu = RecordGenerator.complexRecord()
        val formatter = DefaultJavaFormatter()
        formatter.setIndenter(Indenter.defaultIndenter())
        val renderedRecord = formatter.getFormattedContent(cu)
        assertThat(renderedRecord).isEqualToNormalizingNewlines("""
            |/**
            | * A complex record
            | */
            |package mbg.domtest.generators.records;
            |
            |import static java.lang.Math.PI;
            |
            |import java.io.Serializable;
            |
            |public record ComplexRecord<T> (int id, String description, T theT) implements Serializable {
            |    static final int f;
            |
            |    static {
            |        f = 22;
            |    }
            |
            |    ComplexRecord(int id, String description) {
            |        this(id, description, null);
            |    }
            |
            |    static int ff() {
            |        return f;
            |    }
            |
            |    int add(int a, int b) {
            |        return a + b + f;
            |    }
            |
            |    String sayIt() {
            |        return id + " " + description;
            |    }
            |
            |    public static class Fred implements Flintstone {
            |    }
            |
            |    interface Flintstone {
            |    }
            |
            |    enum MyEnum {
            |        A,
            |        B,
            |        C;
            |    }
            |
            |    record InnerRecord(int id, String description) {
            |    }
            |}
            """.trimMargin())
    }

    @Test
    fun testComplexRecordTwoSpaces() {
        val cu = RecordGenerator.complexRecord()
        val formatter = DefaultJavaFormatter()
        formatter.setIndenter(Indenter.Builder().withJavaIndentAmount(2).build())
        val renderedRecord = formatter.getFormattedContent(cu)
        assertThat(renderedRecord).isEqualToNormalizingNewlines("""
            |/**
            | * A complex record
            | */
            |package mbg.domtest.generators.records;
            |
            |import static java.lang.Math.PI;
            |
            |import java.io.Serializable;
            |
            |public record ComplexRecord<T> (int id, String description, T theT) implements Serializable {
            |  static final int f;
            |
            |  static {
            |    f = 22;
            |  }
            |
            |  ComplexRecord(int id, String description) {
            |    this(id, description, null);
            |  }
            |
            |  static int ff() {
            |    return f;
            |  }
            |
            |  int add(int a, int b) {
            |    return a + b + f;
            |  }
            |
            |  String sayIt() {
            |    return id + " " + description;
            |  }
            |
            |  public static class Fred implements Flintstone {
            |  }
            |
            |  interface Flintstone {
            |  }
            |
            |  enum MyEnum {
            |    A,
            |    B,
            |    C;
            |  }
            |
            |  record InnerRecord(int id, String description) {
            |  }
            |}
            """.trimMargin())
    }

    @Test
    fun testComplexRecordWithTabs() {
        val cu = RecordGenerator.complexRecord()
        val formatter = DefaultJavaFormatter()
        formatter.setIndenter(Indenter.Builder().withJavaIndentType(IndentType.TABS).build())
        val renderedRecord = formatter.getFormattedContent(cu)
        assertThat(renderedRecord).isEqualToNormalizingNewlines("""
            |/**
            | * A complex record
            | */
            |package mbg.domtest.generators.records;
            |
            |import static java.lang.Math.PI;
            |
            |import java.io.Serializable;
            |
            |public record ComplexRecord<T> (int id, String description, T theT) implements Serializable {
            |${"\t"}static final int f;
            |
            |${"\t"}static {
            |${"\t\t"}f = 22;
            |${"\t"}}
            |
            |${"\t"}ComplexRecord(int id, String description) {
            |${"\t\t"}this(id, description, null);
            |${"\t"}}
            |
            |${"\t"}static int ff() {
            |${"\t\t"}return f;
            |${"\t"}}
            |
            |${"\t"}int add(int a, int b) {
            |${"\t\t"}return a + b + f;
            |${"\t"}}
            |
            |${"\t"}String sayIt() {
            |${"\t\t"}return id + " " + description;
            |${"\t"}}
            |
            |${"\t"}public static class Fred implements Flintstone {
            |${"\t"}}
            |
            |${"\t"}interface Flintstone {
            |${"\t"}}
            |
            |${"\t"}enum MyEnum {
            |${"\t\t"}A,
            |${"\t\t"}B,
            |${"\t\t"}C;
            |${"\t"}}
            |
            |${"\t"}record InnerRecord(int id, String description) {
            |${"\t"}}
            |}
            """.trimMargin())
    }
}
