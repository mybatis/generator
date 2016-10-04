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
import org.mybatis.generator.eclipse.tests.harness.summary.AnnotationSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.AbstractSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.ClassSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.CompilationUnitSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.EnumSummary;
import org.mybatis.generator.eclipse.tests.harness.summary.InterfaceSummary;

public class SummarizerTest {

    private static final String IMPORT_JAVA_IO_SERIALIZABLE = "import java.io.Serializable";
    private static final String SERIALIZABLE = "Serializable";

    @Test
    public void testAnnotationSummarizer() throws IOException {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/tests/harness/tests/resources/OuterAnnotation.src");
        CompilationUnitSummary cuSummary = getCompilationUnitSummaryFromResource(resource);

        assertThat(cuSummary, hasImportCount(4));
        assertThat(cuSummary, hasImport("import java.lang.annotation.ElementType"));
        assertThat(cuSummary, hasImport("import java.lang.annotation.Retention"));
        assertThat(cuSummary, hasImport("import java.lang.annotation.RetentionPolicy"));
        assertThat(cuSummary, hasImport("import java.lang.annotation.Target"));

        assertThat(cuSummary, hasAnnotationCount(2));
        assertThat(cuSummary, hasAnnotation("OuterAnnotation"));
        assertThat(cuSummary, hasAnnotation("SecondOuterAnnotation"));
        
        assertThat(cuSummary, hasClassCount(0));
        assertThat(cuSummary, hasEnumCount(0));
        assertThat(cuSummary, hasInterfaceCount(0));

        AnnotationSummary annotationSummary = cuSummary.getAnnotationSummary("OuterAnnotation");
        
        assertThat(annotationSummary, hasAnnotationMember("name"));
        assertThat(annotationSummary, hasAnnotationMemberCount(1));
        
        assertThat(annotationSummary, hasField("id", ofType("int")));
        assertThat(annotationSummary, hasField("id2", ofType("int")));
        assertThat(annotationSummary, hasField("name", ofType("int")));
        assertThat(annotationSummary, hasFieldCount(3));
        
        verifyInners(annotationSummary);
    }

    @Test
    public void testClassSummarizer() throws IOException {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/tests/harness/tests/resources/OuterClass.src");
        CompilationUnitSummary cuSummary = getCompilationUnitSummaryFromResource(resource);

        assertThat(cuSummary, hasImportCount(2));
        assertThat(cuSummary, hasImport(IMPORT_JAVA_IO_SERIALIZABLE));
        assertThat(cuSummary, hasImport("import java.util.ArrayList"));

        assertThat(cuSummary, hasClassCount(2));
        assertThat(cuSummary, hasClass("SecondOuterClass"));
        assertThat(cuSummary, hasClass("OuterClass"));

        assertThat(cuSummary, hasEnumCount(0));
        assertThat(cuSummary, hasInterfaceCount(0));
        
        ClassSummary classSummary = cuSummary.getClassSummary("OuterClass");
        
        assertThat(classSummary, hasSuperClass("ArrayList<String>"));
        assertThat(classSummary, hasSuperInterface(SERIALIZABLE));
        assertThat(classSummary, hasSuperInterfaceCount(1));
        
        assertThat(classSummary, hasField("serialVersionUID", withFieldType("long")));
        assertThat(classSummary, hasField("unreliableCount"));
        assertThat(classSummary, hasFieldCount(2));
        
        assertThat(classSummary, hasMethod("add(String)"));
        assertThat(classSummary, hasMethod("getUnreliableCount()"));
        assertThat(classSummary, hasMethodCount(2));

        verifyInners(classSummary);
    }

    @Test
    public void testEnumSummarizer() throws IOException {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/tests/harness/tests/resources/OuterEnum.src");
        CompilationUnitSummary cuSummary = getCompilationUnitSummaryFromResource(resource);

        assertThat(cuSummary, hasImportCount(1));
        assertThat(cuSummary, hasImport(IMPORT_JAVA_IO_SERIALIZABLE));

        assertThat(cuSummary, hasClassCount(0));

        assertThat(cuSummary, hasEnumCount(2));
        assertThat(cuSummary, hasEnum("SecondOuterEnum"));
        assertThat(cuSummary, hasEnum("OuterEnum"));

        assertThat(cuSummary, hasInterfaceCount(0));
        
        EnumSummary enumSummary = cuSummary.getEnumSummary("OuterEnum");
        
        assertThat(enumSummary, hasEnumConstant("FRED"));
        assertThat(enumSummary, hasEnumConstant("WILMA"));
        assertThat(enumSummary, hasEnumConstant("BARNEY"));
        assertThat(enumSummary, hasEnumConstant("BETTY"));
        assertThat(enumSummary, hasEnumConstantCount(4));
        
        assertThat(enumSummary, hasSuperInterface(SERIALIZABLE));
        assertThat(enumSummary, hasSuperInterfaceCount(1));
        
        assertThat(enumSummary, hasField("name"));
        assertThat(enumSummary, hasFieldCount(1)        );
        
        assertThat(enumSummary, hasMethod("OuterEnum(String)"));
        assertThat(enumSummary, hasMethod("getName()"));
        assertThat(enumSummary, hasMethodCount(2));
        
        verifyInners(enumSummary);
    }
    
    @Test
    public void testInterfaceSummarizer() throws IOException {
        InputStream resource = getClass().getResourceAsStream("/org/mybatis/generator/eclipse/tests/harness/tests/resources/OuterInterface.src");
        CompilationUnitSummary cuSummary = getCompilationUnitSummaryFromResource(resource);

        assertThat(cuSummary, hasImportCount(1));
        assertThat(cuSummary, hasImport(IMPORT_JAVA_IO_SERIALIZABLE));

        assertThat(cuSummary, hasClassCount(0));
        assertThat(cuSummary, hasEnumCount(0));

        assertThat(cuSummary, hasInterfaceCount(2));
        assertThat(cuSummary, hasInterface("SecondOuterInterface"));
        assertThat(cuSummary, hasInterface("OuterInterface"));
        
        InterfaceSummary interfaceSummary = cuSummary.getInterfaceSummary("OuterInterface");
        
        assertThat(interfaceSummary, hasSuperInterface(SERIALIZABLE));
        assertThat(interfaceSummary, hasSuperInterfaceCount(1));
        
        assertThat(interfaceSummary, hasField("MY_NAME"));
        assertThat(interfaceSummary, hasFieldCount(1));
        
        assertThat(interfaceSummary, hasMethod("doSomething(int)"));
        assertThat(interfaceSummary, hasMethod("doSomethingElse()"));
        assertThat(interfaceSummary, hasMethodCount(2));
        
        verifyInners(interfaceSummary);
    }

    private void verifyInners(AbstractSummary summary) {
        assertThat(summary, hasClass("InnerClass"));
        verifyInnerClass(summary.getClassSummary("InnerClass"));
        assertThat(summary, hasClassCount(1));

        assertThat(summary, hasInterface("InnerInterface"));
        verifyInnerInterface(summary.getInterfaceSummary("InnerInterface"));
        assertThat(summary, hasInterfaceCount(1));

        assertThat(summary, hasEnum("InnerEnum"));
        verifyInnerEnum(summary.getEnumSummary("InnerEnum"));
        assertThat(summary, hasEnumCount(1));
        
        assertThat(summary, hasAnnotation("InnerAnnotation"));
        verifyInnerAnnotation(summary.getAnnotationSummary("InnerAnnotation"));
        assertThat(summary, hasAnnotationCount(1));
    }

    private void verifyInnerAnnotation(AnnotationSummary annotationSummary) {
        assertThat(annotationSummary, hasAnnotationMember("amount"));
        assertThat(annotationSummary, hasAnnotationMemberCount(1));
        
        assertThat(annotationSummary, hasFieldCount(0));
        assertThat(annotationSummary, hasClassCount(0));
        assertThat(annotationSummary, hasEnumCount(0));
        assertThat(annotationSummary, hasAnnotationCount(0));
        assertThat(annotationSummary, hasInterfaceCount(0));
    }

    private void verifyInnerEnum(EnumSummary enumSummary) {
        assertThat(enumSummary, hasEnumConstant("GEORGE"));
        assertThat(enumSummary, hasEnumConstant("JANE"));
        assertThat(enumSummary, hasEnumConstantCount(2));

        assertThat(enumSummary, hasField("index"));
        assertThat(enumSummary, hasFieldCount(1));
        
        assertThat(enumSummary, hasMethod("InnerEnum(int)"));
        assertThat(enumSummary, hasMethod("getIndex()"));
        assertThat(enumSummary, hasMethodCount(2));

        assertThat(enumSummary, hasClassCount(0));
        assertThat(enumSummary, hasEnumCount(0));
        assertThat(enumSummary, hasAnnotationCount(0));
        assertThat(enumSummary, hasInterfaceCount(0));
        assertThat(enumSummary, hasSuperInterfaceCount(0));
    }

    private void verifyInnerInterface(InterfaceSummary interfaceSummary) {
        assertThat(interfaceSummary, hasMethod("execute()"));
        assertThat(interfaceSummary, hasMethodCount(1));
        assertThat(interfaceSummary, hasFieldCount(0));
        assertThat(interfaceSummary, hasClassCount(0));
        assertThat(interfaceSummary, hasEnumCount(0));
        assertThat(interfaceSummary, hasAnnotationCount(0));
        assertThat(interfaceSummary, hasInterfaceCount(0));
        assertThat(interfaceSummary, hasSuperInterfaceCount(0));
    }

    private void verifyInnerClass(ClassSummary classSummary) {
        assertThat(classSummary, hasMethod("getDescription()"));
        assertThat(classSummary, hasMethod("setDescription(String)"));
        assertThat(classSummary, hasMethodCount(2));
        
        assertThat(classSummary, hasField("description"));
        assertThat(classSummary, hasFieldCount(1));
        
        assertThat(classSummary, hasClassCount(0));
        assertThat(classSummary, hasEnumCount(0));
        assertThat(classSummary, hasAnnotationCount(0));
        assertThat(classSummary, hasInterfaceCount(0));
        assertThat(classSummary, hasSuperInterfaceCount(0));
        assertThat(classSummary, hasSuperClass(null));
    }
}
