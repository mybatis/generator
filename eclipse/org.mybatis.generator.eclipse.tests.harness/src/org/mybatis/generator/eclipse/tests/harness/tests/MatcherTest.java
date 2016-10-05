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
import java.io.Serializable;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.generator.eclipse.tests.harness.Utilities;
import org.mybatis.generator.eclipse.tests.harness.Utilities.CantCreateInstanceException;
import org.mybatis.generator.eclipse.tests.harness.matchers.HasCount;
import org.mybatis.generator.eclipse.tests.harness.matchers.HasElement;
import org.mybatis.generator.eclipse.tests.harness.matchers.HasElementWithValue;
import org.mybatis.generator.eclipse.tests.harness.summary.CompilationUnitSummary;

public class MatcherTest {

    private static final String FAKE_ENUM = "FakeEnum";
    private static final String INNER_ENUM = "InnerEnum";
    private static final String UNRELIABLE_COUNT = "unreliableCount";
    private static final String SECOND_OUTER_CLASS = "SecondOuterClass";
    private static final String INNER_ANNOTATION = "InnerAnnotation";
    private static final String INNER_INTERFACE = "InnerInterface";
    private static final String INNER_CLASS = "InnerClass";
    private static final String FAKE_ANNOTATION = "FakeAnnotation";
    private static final String FAKE_INTERFACE = "FakeInterface";
    private static final String FAKE_CLASS = "FakeClass";
    private static final String OUTER_CLASS = "OuterClass";
    
    private static CompilationUnitSummary cuSummary;

    @BeforeClass
    public static void beforeClass() throws IOException {
        InputStream resource = MatcherTest.class.getResourceAsStream("/org/mybatis/generator/eclipse/tests/harness/tests/resources/OuterClass.src");
        cuSummary = getCompilationUnitSummaryFromResource(resource);
    }
    
    @Test
    public void voidTestHasImportSucceeds() {
        assertThat(cuSummary, hasImport("import java.io.Serializable"));
    }
    
    @Test(expected=AssertionError.class)
    public void voidTestHasImportFails() {
        assertThat(cuSummary, hasImport("import foo.Bar"));
    }
    
    @Test
    public void voidTestHasImportCountSucceeds() {
        assertThat(cuSummary, hasImportCount(2));
    }
    
    @Test(expected=AssertionError.class)
    public void voidTestHasImportCountFails() {
        assertThat(cuSummary, hasImportCount(3));
    }
    
    @Test
    public void testHasClassSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassFails() {
        assertThat(cuSummary, hasClass(FAKE_CLASS));
    }

    @Test
    public void testWithClassSucceeds() {
        assertThat(cuSummary, withClass(OUTER_CLASS));
    }

    @Test(expected=AssertionError.class)
    public void testWithClassFails() {
        assertThat(cuSummary, withClass(FAKE_CLASS));
    }

    @Test
    public void testHasClassWithClassSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClass(INNER_CLASS)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithClassFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClass(FAKE_CLASS)));
    }

    @Test
    public void testHasClassWithClassCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClassCount(1)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithClassCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClassCount(2)));
    }

    @Test
    public void testHasClassWithClassWithFieldCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClass(INNER_CLASS, withFieldCount(1))));
    }

    @Test
    public void testHasClassWithEnumSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithEnumFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(FAKE_ENUM)));
    }

    @Test
    public void testHasClassWithEnumCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnumCount(1)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithEnumCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnumCount(2)));
    }

    @Test
    public void testHasClassWithInterfaceSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterface(INNER_INTERFACE)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithInterfaceFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterface(FAKE_INTERFACE)));
    }

    @Test
    public void testHasClassWithInterfaceCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterfaceCount(1)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithInterfaceCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterfaceCount(2)));
    }

    @Test
    public void testHasClassWithInterfaceWithMethodSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterface(INNER_INTERFACE, withMethod("execute()"))));
    }

    @Test
    public void testHasClassWithAnnotationSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithAnnotationFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(FAKE_ANNOTATION)));
    }

    @Test
    public void testHasClassWithAnnotationCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotationCount(1)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithAnnotationCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotationCount(2)));
    }

    @Test
    public void testHasClassWithFieldSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withField(UNRELIABLE_COUNT)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithFieldFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withField("fakeField")));
    }

    @Test
    public void testHasClassWithFieldOfTypeSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withField(UNRELIABLE_COUNT, ofType("int"))));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithFieldOfTypeFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withField(UNRELIABLE_COUNT, ofType("long"))));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithFakeFieldOfTypeFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withField("fakeField", ofType("long"))));
    }

    @Test
    public void testHasClassWithFieldCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withFieldCount(2)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithFieldCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withFieldCount(3)));
    }

    @Test
    public void testHasClassWithMethodSucceeds() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withMethod("setId(int)")));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithMethodFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withMethod("setId(int)")));
    }

    @Test
    public void testHasClassWithMethodCountSucceeds() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withMethodCount(2)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithMethodCountFails() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withMethodCount(3)));
    }

    @Test
    public void testHasClassWithSuperClassSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperClass("ArrayList<String>")));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithNullSuperClassFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperClass(null)));
    }

    @Test
    public void testHasClassWithNullSuperClassSucceeds() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withSuperClass(null)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithSuperClassFails() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withSuperClass(FAKE_CLASS)));
    }

    @Test
    public void testHasClassWithSuperInterfaceSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterface("Serializable")));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithSuperInterfaceFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterface(FAKE_INTERFACE)));
    }

    @Test
    public void testHasClassWithSuperInterfaceCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterfaceCount(1)));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithSuperInterfaceCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterfaceCount(2)));
    }

    @Test
    public void testHasClassWithEnumWithEnumConstantSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstant("GEORGE"))));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithEnumWithEnumConstantFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstant("FRED"))));
    }

    @Test
    public void testHasClassWithEnumWithEnumConstantCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstantCount(2))));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithEnumWithEnumConstantCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstantCount(3))));
    }

    @Test
    public void testHasClassWithAnnotationWithAnnotationMemberSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMember("amount"))));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithAnnotationWithAnnotationMemberFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMember("fakeMember"))));
    }

    @Test
    public void testHasClassWithAnnotationWithAnnotationMemberCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMemberCount(1))));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithAnnotationWithAnnotationMemberCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMemberCount(2))));
    }

    @Test(expected=AssertionError.class)
    public void testHasClassWithAnnotationWithFakeAnnotationMemberCountFails() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(FAKE_ANNOTATION, withAnnotationMemberCount(2))));
    }
    
    @Test(expected=AssertionError.class)
    public void testHasClassWithEnumConstantCountFails() {
        // the compiler allows us to write tests that don't make any sense,
        // but the tests fail at runtime.
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnumConstantCount(0)));
    }

    @Test(expected=CantCreateInstanceException.class)
    public void testCreateInstanceFails() {
        Utilities.newInstance(Serializable.class);
    }
    
    @Test
    public void notATest() {
        // this is not a test - it exercises some methods in classes with enums
        // to clear up some code coverage noise
        HasCount.Type.values();
        HasElement.Type.values();
        HasElementWithValue.Type.values();
    }
}
