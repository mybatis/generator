/*
 *    Copyright 2006-2023 the original author or authors.
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
 *
 */
package org.mybatis.generator.eclipse.test.tests;

import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mybatis.generator.eclipse.test.Utilities.getCompilationUnitSummaryFromResource;
import static org.mybatis.generator.eclipse.test.matchers.Matchers.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.eclipse.test.Utilities;
import org.mybatis.generator.eclipse.test.Utilities.CantCreateInstanceException;
import org.mybatis.generator.eclipse.test.matchers.HasCount;
import org.mybatis.generator.eclipse.test.matchers.HasElement;
import org.mybatis.generator.eclipse.test.matchers.HasElementWithValue;
import org.mybatis.generator.eclipse.test.summary.CompilationUnitSummary;

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

    @BeforeAll
    public static void beforeClass() throws IOException {
        InputStream resource = MatcherTest.class.getResourceAsStream("/org/mybatis/generator/eclipse/test/tests/resources/OuterClass.java.src");
        cuSummary = getCompilationUnitSummaryFromResource(resource);
    }
    
    @Test
    public void voidTestHasImportSucceeds() {
        assertThat(cuSummary, hasImport("import java.io.Serializable"));
    }
    
    @Test
    public void voidTestHasImportFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasImport("import foo.Bar"));
        });
    }
    
    @Test
    public void voidTestHasImportCountSucceeds() {
        assertThat(cuSummary, hasImportCount(2));
    }
    
    @Test
    public void voidTestHasImportCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasImportCount(3));
        });
    }
    
    @Test
    public void testHasClassSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS));
    }

    @Test
    public void testHasClassFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(FAKE_CLASS));
        });
    }

    @Test
    public void testWithClassSucceeds() {
        assertThat(cuSummary, withClass(OUTER_CLASS));
    }

    @Test
    public void testWithClassFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, withClass(FAKE_CLASS));
        });
    }

    @Test
    public void testHasClassWithClassSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClass(INNER_CLASS)));
    }

    @Test
    public void testHasClassWithClassFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withClass(FAKE_CLASS)));
        });
    }

    @Test
    public void testHasClassWithClassCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClassCount(1)));
    }

    @Test
    public void testHasClassWithClassCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withClassCount(2)));
        });
    }

    @Test
    public void testHasClassWithClassWithFieldCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withClass(INNER_CLASS, withFieldCount(1))));
    }

    @Test
    public void testHasClassWithEnumSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM)));
    }

    @Test
    public void testHasClassWithEnumFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(FAKE_ENUM)));
        });
    }

    @Test
    public void testHasClassWithEnumCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnumCount(1)));
    }

    @Test
    public void testHasClassWithEnumCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withEnumCount(2)));
        });
    }

    @Test
    public void testHasClassWithInterfaceSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterface(INNER_INTERFACE)));
    }

    @Test
    public void testHasClassWithInterfaceFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withInterface(FAKE_INTERFACE)));
        });
    }

    @Test
    public void testHasClassWithInterfaceCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterfaceCount(1)));
    }

    @Test
    public void testHasClassWithInterfaceCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withInterfaceCount(2)));
        });
    }

    @Test
    public void testHasClassWithInterfaceWithMethodSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withInterface(INNER_INTERFACE, withMethod("execute()"))));
    }

    @Test
    public void testHasClassWithAnnotationSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION)));
    }

    @Test
    public void testHasClassWithAnnotationFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(FAKE_ANNOTATION)));
        });
    }

    @Test
    public void testHasClassWithAnnotationCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotationCount(1)));
    }

    @Test
    public void testHasClassWithAnnotationCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotationCount(2)));
        });
    }

    @Test
    public void testHasClassWithFieldSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withField(UNRELIABLE_COUNT)));
    }

    @Test
    public void testHasClassWithFieldFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withField("fakeField")));
        });
    }

    @Test
    public void testHasClassWithFieldOfTypeSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withField(UNRELIABLE_COUNT, ofType("int"))));
    }

    @Test
    public void testHasClassWithFieldOfTypeFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withField(UNRELIABLE_COUNT, ofType("long"))));
        });
    }

    @Test
    public void testHasClassWithFakeFieldOfTypeFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withField("fakeField", ofType("long"))));
        });
    }

    @Test
    public void testHasClassWithFieldCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withFieldCount(2)));
    }

    @Test
    public void testHasClassWithFieldCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withFieldCount(3)));
        });
    }

    @Test
    public void testHasClassWithMethodSucceeds() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withMethod("setId(int)")));
    }

    @Test
    public void testHasClassWithMethodFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withMethod("setId(int)")));
        });
    }

    @Test
    public void testHasClassWithMethodCountSucceeds() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withMethodCount(2)));
    }

    @Test
    public void testHasClassWithMethodCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withMethodCount(3)));
        });
    }

    @Test
    public void testHasClassWithSuperClassSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperClass("ArrayList<String>")));
    }

    @Test
    public void testHasClassWithNullSuperClassFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperClass(nullValue())));
        });
    }

    @Test
    public void testHasClassWithNullSuperClassSucceeds() {
        assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withSuperClass(nullValue())));
    }

    @Test
    public void testHasClassWithSuperClassFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(SECOND_OUTER_CLASS, withSuperClass(FAKE_CLASS)));
        });
    }

    @Test
    public void testHasClassWithSuperInterfaceSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterface("Serializable")));
    }

    @Test
    public void testHasClassWithSuperInterfaceFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterface(FAKE_INTERFACE)));
        });
    }

    @Test
    public void testHasClassWithSuperInterfaceCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterfaceCount(1)));
    }

    @Test
    public void testHasClassWithSuperInterfaceCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withSuperInterfaceCount(2)));
        });
    }

    @Test
    public void testHasClassWithEnumWithEnumConstantSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstant("GEORGE"))));
    }

    @Test
    public void testHasClassWithEnumWithEnumConstantFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstant("FRED"))));
        });
    }

    @Test
    public void testHasClassWithEnumWithEnumConstantCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstantCount(2))));
    }

    @Test
    public void testHasClassWithEnumWithEnumConstantCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withEnum(INNER_ENUM, withEnumConstantCount(3))));
        });
    }

    @Test
    public void testHasClassWithAnnotationWithAnnotationMemberSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMember("amount"))));
    }

    @Test
    public void testHasClassWithAnnotationWithAnnotationMemberFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMember("fakeMember"))));
        });
    }

    @Test
    public void testHasClassWithAnnotationWithAnnotationMemberCountSucceeds() {
        assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMemberCount(1))));
    }

    @Test
    public void testHasClassWithAnnotationWithAnnotationMemberCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(INNER_ANNOTATION, withAnnotationMemberCount(2))));
        });
    }

    @Test
    public void testHasClassWithAnnotationWithFakeAnnotationMemberCountFails() {
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withAnnotation(FAKE_ANNOTATION, withAnnotationMemberCount(2))));
        });
    }
    
    @Test
    public void testHasClassWithEnumConstantCountFails() {
        // the compiler allows us to write tests that don't make any sense,
        // but the tests fail at runtime.
        Assertions.assertThrows(AssertionError.class, () -> {
            assertThat(cuSummary, hasClass(OUTER_CLASS, withEnumConstantCount(0)));
        });
    }

    @Test
    public void testCreateInstanceFails() {
        Assertions.assertThrows(CantCreateInstanceException.class, () -> {
            Utilities.newInstance(Serializable.class);
        });
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
