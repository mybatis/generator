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
package org.mybatis.generator.eclipse.tests.harness.matchers;

import org.hamcrest.Matcher;
import org.mybatis.generator.eclipse.tests.harness.summary.FieldSummary;

/**
 * This class holds static methods that create matchers.  This is in keeping
 * with the normal hamcrest pattern.
 * 
 * Many of the methods are expressed in two ways: hasXXX and withXXX.  These 
 * methods will return equivalent matchers.  They exist to allow for more expressive
 * unit tests when chaining matchers.  For example you can write this:
 * 
 * <code>assertThat(summary, hasClass("OuterClass", withClass("InnerClass")))</code>
 * 
 * This test checks that the summary object contains a class named OuterClass and that
 * the class OuterClass contains an inner class named InnerClass.  The <code>hasClass</code>
 * and <code>withClass</code> methods return equivalent matchers, but the test is more
 * expressive.
 *   
 * @author Jeff Butler
 *
 */
public class Matchers {
    private Matchers () {
        // utility class = no instances
        super();
    }
    
    public static HasMethod hasMethod(String methodSignature) {
        return new HasMethod(methodSignature);
    }

    public static HasMethod withMethod(String methodSignature) {
        return hasMethod(methodSignature);
    }

    public static HasAnnotationMember hasAnnotationMember(String member) {
        return new HasAnnotationMember(member);
    }

    public static HasAnnotationMember withAnnotationMember(String member) {
        return hasAnnotationMember(member);
    }

    public static HasField hasField(String field) {
        return new HasField(field);
    }

    public static HasField withField(String field) {
        return hasField(field);
    }

    public static HasFieldWithValue hasField(String field, Matcher<FieldSummary> matcher) {
        return new HasFieldWithValue(field, matcher);
    }

    public static HasFieldWithValue withField(String field, Matcher<FieldSummary> matcher) {
        return hasField(field, matcher);
    }

    public static HasFieldType hasFieldType(String fieldType) {
        return new HasFieldType(fieldType);
    }
    
    public static HasFieldType withFieldType(String fieldType) {
        return hasFieldType(fieldType);
    }
    
    public static HasFieldType ofType(String fieldType) {
        return hasFieldType(fieldType);
    }
    
    public static HasEnumConstant hasEnumConstant(String enumConstant) {
        return new HasEnumConstant(enumConstant);
    }

    public static HasEnumConstant withEnumConstant(String enumConstant) {
        return hasEnumConstant(enumConstant);
    }

    public static HasImport hasImport(String importDeclaration) {
        return new HasImport(importDeclaration);
    }

    public static HasElement hasAnnotation(String name) {
        return new HasElement(name, HasElement.Type.ANNOTATION);
    }

    public static HasElement withAnnotation(String name) {
        return hasAnnotation(name);
    }

    public static HasElementWithValue hasAnnotation(String name, Matcher<?> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.ANNOTATION, matcher);
    }

    public static HasElementWithValue withAnnotation(String name, Matcher<?> matcher) {
        return hasAnnotation(name, matcher);
    }

    public static HasElement hasClass(String name) {
        return new HasElement(name, HasElement.Type.CLASS);
    }

    public static HasElement withClass(String name) {
        return hasClass(name);
    }

    public static HasElementWithValue hasClass(String name, Matcher<?> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.CLASS, matcher);
    }

    public static HasElementWithValue withClass(String name, Matcher<?> matcher) {
        return hasClass(name, matcher);
    }

    public static HasElement hasInterface(String name) {
        return new HasElement(name, HasElement.Type.INTERFACE);
    }

    public static HasElement withInterface(String name) {
        return hasInterface(name);
    }

    public static HasElementWithValue hasInterface(String name, Matcher<?> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.INTERFACE, matcher);
    }

    public static HasElementWithValue withInterface(String name, Matcher<?> matcher) {
        return hasInterface(name, matcher);
    }

    public static HasElement hasEnum(String name) {
        return new HasElement(name, HasElement.Type.ENUM);
    }

    public static HasElement withEnum(String name) {
        return hasEnum(name);
    }

    public static HasElementWithValue hasEnum(String name, Matcher<?> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.ENUM, matcher);
    }

    public static HasElementWithValue withEnum(String name, Matcher<?> matcher) {
        return hasEnum(name, matcher);
    }

    public static HasSuperClass hasSuperClass(String superClass) {
        return new HasSuperClass(superClass);
    }
    
    public static HasSuperClass withSuperClass(String superClass) {
        return hasSuperClass(superClass);
    }
    
    public static HasSuperInterface hasSuperInterface(String superInterface) {
        return new HasSuperInterface(superInterface);
    }
    
    public static HasSuperInterface withSuperInterface(String superInterface) {
        return hasSuperInterface(superInterface);
    }
    
    public static HasCount hasAnnotationCount(int count) {
        return new HasCount(count, HasCount.Type.ANNOTATION);
    }
    
    public static HasCount withAnnotationCount(int count) {
        return hasAnnotationCount(count);
    }
    
    public static HasAnnotationMemberCount hasAnnotationMemberCount(int count) {
        return new HasAnnotationMemberCount(count);
    }
    
    public static HasAnnotationMemberCount withAnnotationMemberCount(int count) {
        return hasAnnotationMemberCount(count);
    }
    
    public static HasCount hasClassCount(int count) {
        return new HasCount(count, HasCount.Type.CLASS);
    }
    
    public static HasCount withClassCount(int count) {
        return hasClassCount(count);
    }
    
    public static HasCount hasEnumCount(int count) {
        return new HasCount(count, HasCount.Type.ENUM);
    }
    
    public static HasCount withEnumCount(int count) {
        return hasEnumCount(count);
    }
    
    public static HasEnumConstantCount hasEnumConstantCount(int count) {
        return new HasEnumConstantCount(count);
    }
    
    public static HasEnumConstantCount withEnumConstantCount(int count) {
        return hasEnumConstantCount(count);
    }
    
    public static HasFieldCount hasFieldCount(int count) {
        return new HasFieldCount(count);
    }
    
    public static HasFieldCount withFieldCount(int count) {
        return hasFieldCount(count);
    }
    
    public static HasImportCount hasImportCount(int count) {
        return new HasImportCount(count);
    }
    
    public static HasCount hasInterfaceCount(int count) {
        return new HasCount(count, HasCount.Type.INTERFACE);
    }
    
    public static HasCount withInterfaceCount(int count) {
        return hasInterfaceCount(count);
    }
    
    public static HasMethodCount hasMethodCount(int count) {
        return new HasMethodCount(count);
    }
    
    public static HasMethodCount withMethodCount(int count) {
        return hasMethodCount(count);
    }

    public static HasSuperInterfaceCount hasSuperInterfaceCount(int count) {
        return new HasSuperInterfaceCount(count);
    }
    
    public static HasSuperInterfaceCount withSuperInterfaceCount(int count) {
        return hasSuperInterfaceCount(count);
    }
}
