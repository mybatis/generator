/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.eclipse.tests.harness.tests;

import static org.junit.Assert.assertThat;
import static org.mybatis.generator.eclipse.tests.harness.Utilities.getCompilationUnitSummaryFromResource;
import static org.mybatis.generator.eclipse.tests.harness.matchers.Matchers.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.mybatis.generator.eclipse.tests.harness.summary.CompilationUnitSummary;

public class SummarizerTortureTest {
    
    private static final String CLASS_NAME = "StringifierExercizer";
    
    @Test
    public void testDifficultMethodsAndFields() throws IOException {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/tests/harness/tests/resources/StringifierExercizer.java.src");
        CompilationUnitSummary summary = getCompilationUnitSummaryFromResource(resource);
        
        assertThat(summary, hasImportCount(6));
        assertThat(summary, hasImport("import static org.junit.Assert.*"));
        assertThat(summary, hasImport("import static org.hamcrest.core.IsNot.not"));
        assertThat(summary, hasImport("import java.io.Closeable"));
        assertThat(summary, hasImport("import java.io.IOException"));
        assertThat(summary, hasImport("import java.util.*"));
        assertThat(summary, hasImport("import java.math.BigDecimal"));
        
        assertThat(summary, hasClass(CLASS_NAME, withFieldCount(3)));
        assertThat(summary, hasClass(CLASS_NAME, withField("primitive1", ofType("int"))));
        assertThat(summary, hasClass(CLASS_NAME, withField("decimalArray1", ofType("java.math.BigDecimal[]"))));
        assertThat(summary, hasClass(CLASS_NAME, withField("integerArray2", ofType("java.math.BigInteger[][]"))));
        
        assertThat(summary, hasClass(CLASS_NAME, withMethodCount(10)));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("emptyMethod()")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("<I extends List<String> & Closeable> intersectionType(I)")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("getPrimitive1()")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("setPrimitive1(int)")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("someMethod(List<Map<Integer,java.lang.String>>,Map<Integer,List<String>>)")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("anotherMethod(String[],String[][])")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("main(String[])")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("method22(List<? extends BigDecimal>)")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("method22(Map<Integer,? super java.math.BigDecimal>)")));
        assertThat(summary, hasClass(CLASS_NAME, withMethod("method33(java.lang.String)")));
    }
}
