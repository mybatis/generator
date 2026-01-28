/*
 *    Copyright 2006-2025 the original author or authors.
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
import org.mybatis.generator.api.dom.java.RecordGenerator

class TopLevelRecordRendererTest {
    @Test
    fun testComplexRecord() {
        val cu = RecordGenerator.complexRecord()
        val renderedRecord = DefaultJavaFormatter().getFormattedContent(cu)
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
}