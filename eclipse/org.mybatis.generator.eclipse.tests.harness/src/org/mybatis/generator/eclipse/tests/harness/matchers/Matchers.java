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
import org.mybatis.generator.eclipse.tests.harness.summary.MatcherSupport;

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
    
    public static HasElement hasMethod(String methodSignature) {
        return new HasElement(methodSignature, HasElement.Type.METHOD);
    }

    public static HasElement withMethod(String methodSignature) {
        return hasMethod(methodSignature);
    }

    public static HasElement hasAnnotationMember(String member) {
        return new HasElement(member, HasElement.Type.ANNOTATION_MEMBER);
    }

    public static HasElement withAnnotationMember(String member) {
        return hasAnnotationMember(member);
    }

    public static HasElement hasField(String field) {
        return new HasElement(field, HasElement.Type.FIELD);
    }

    public static HasElement withField(String field) {
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
    
    public static HasElement hasEnumConstant(String enumConstant) {
        return new HasElement(enumConstant, HasElement.Type.ENUM_CONSTANT);
    }

    public static HasElement withEnumConstant(String enumConstant) {
        return hasEnumConstant(enumConstant);
    }

    public static HasElement hasImport(String importDeclaration) {
        return new HasElement(importDeclaration, HasElement.Type.IMPORT);
    }

    public static HasElement hasAnnotation(String name) {
        return new HasElement(name, HasElement.Type.ANNOTATION);
    }

    public static HasElement withAnnotation(String name) {
        return hasAnnotation(name);
    }

    public static HasElementWithValue hasAnnotation(String name, Matcher<MatcherSupport> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.ANNOTATION, matcher);
    }

    public static HasElementWithValue withAnnotation(String name, Matcher<MatcherSupport> matcher) {
        return hasAnnotation(name, matcher);
    }

    public static HasElement hasClass(String name) {
        return new HasElement(name, HasElement.Type.CLASS);
    }

    public static HasElement withClass(String name) {
        return hasClass(name);
    }

    public static HasElementWithValue hasClass(String name, Matcher<MatcherSupport> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.CLASS, matcher);
    }

    public static HasElementWithValue withClass(String name, Matcher<MatcherSupport> matcher) {
        return hasClass(name, matcher);
    }

    public static HasElement hasInterface(String name) {
        return new HasElement(name, HasElement.Type.INTERFACE);
    }

    public static HasElement withInterface(String name) {
        return hasInterface(name);
    }

    public static HasElementWithValue hasInterface(String name, Matcher<MatcherSupport> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.INTERFACE, matcher);
    }

    public static HasElementWithValue withInterface(String name, Matcher<MatcherSupport> matcher) {
        return hasInterface(name, matcher);
    }

    public static HasElement hasEnum(String name) {
        return new HasElement(name, HasElement.Type.ENUM);
    }

    public static HasElement withEnum(String name) {
        return hasEnum(name);
    }

    public static HasElementWithValue hasEnum(String name, Matcher<MatcherSupport> matcher) {
        return new HasElementWithValue(name, HasElementWithValue.Type.ENUM, matcher);
    }

    public static HasElementWithValue withEnum(String name, Matcher<MatcherSupport> matcher) {
        return hasEnum(name, matcher);
    }

    public static HasElement hasSuperClass(String superClass) {
        return new HasElement(superClass, HasElement.Type.SUPER_CLASS);
    }
    
    public static HasElement withSuperClass(String superClass) {
        return hasSuperClass(superClass);
    }
    
    public static HasElement hasSuperInterface(String superInterface) {
        return new HasElement(superInterface, HasElement.Type.SUPER_INTERFACE);
    }
    
    public static HasElement withSuperInterface(String superInterface) {
        return hasSuperInterface(superInterface);
    }
    
    public static HasCount hasAnnotationCount(int count) {
        return new HasCount(count, HasCount.Type.ANNOTATION);
    }
    
    public static HasCount withAnnotationCount(int count) {
        return hasAnnotationCount(count);
    }
    
    public static HasCount hasAnnotationMemberCount(int count) {
        return new HasCount(count, HasCount.Type.ANNOTATION_MEMBER);
    }
    
    public static HasCount withAnnotationMemberCount(int count) {
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
    
    public static HasCount hasEnumConstantCount(int count) {
        return new HasCount(count, HasCount.Type.ENUM_CONSTANT);
    }
    
    public static HasCount withEnumConstantCount(int count) {
        return hasEnumConstantCount(count);
    }
    
    public static HasCount hasFieldCount(int count) {
        return new HasCount(count, HasCount.Type.FIELD);
    }
    
    public static HasCount withFieldCount(int count) {
        return hasFieldCount(count);
    }
    
    public static HasCount hasImportCount(int count) {
        return new HasCount(count, HasCount.Type.IMPORT);
    }
    
    public static HasCount hasInterfaceCount(int count) {
        return new HasCount(count, HasCount.Type.INTERFACE);
    }
    
    public static HasCount withInterfaceCount(int count) {
        return hasInterfaceCount(count);
    }
    
    public static HasCount hasMethodCount(int count) {
        return new HasCount(count, HasCount.Type.METHOD);
    }
    
    public static HasCount withMethodCount(int count) {
        return hasMethodCount(count);
    }

    public static HasCount hasSuperInterfaceCount(int count) {
        return new HasCount(count, HasCount.Type.SUPER_INTERFACE);
    }
    
    public static HasCount withSuperInterfaceCount(int count) {
        return hasSuperInterfaceCount(count);
    }

    public static HasCount hasSuperClassCount(int count) {
        return new HasCount(count, HasCount.Type.SUPER_CLASS);
    }

    public static HasCount withSuperClassCount(int count) {
        return hasSuperClassCount(count);
    }
}
