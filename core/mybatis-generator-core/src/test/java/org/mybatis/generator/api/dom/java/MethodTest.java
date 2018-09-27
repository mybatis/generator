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
package org.mybatis.generator.api.dom.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.dom.java.render.MethodRenderer;

public class MethodTest {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test
    public void testConstructor() {

        Method method = new Method("bar");
        assertNotNull(method);
        assertEquals("bar", method.getName());
    }

    @Test
    public void testConstructorByName() {

        Method method = new Method("foo");
        assertNotNull(method);
        assertEquals("foo", method.getName());
    }

    @Test
    public void testConstructorByMethod() {

        Method original = new Method("foo");
        original.setConstructor(false);
        original.setVisibility(JavaVisibility.PUBLIC);
        original.setStatic(true);
        original.setSynchronized(true);
        original.setFinal(true);

        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        FullyQualifiedJavaType typeT = new FullyQualifiedJavaType("T");
        original.addTypeParameter(new TypeParameter("T", Collections.singletonList(listType)));

        original.addParameter(new Parameter(typeT, "t"));
        original.setReturnType(typeT);

        original.addBodyLine("return t;");

        Method method = new Method(original);
        assertNotNull(method);
        assertEquals("foo", method.getName());
        assertEquals(false, method.isConstructor());
        assertEquals(JavaVisibility.PUBLIC, method.getVisibility());
        assertEquals(true, method.isStatic());
        assertEquals(true, method.isSynchronized());
        assertEquals(true, method.isFinal());
        assertEquals(1, method.getParameters().size());
        assertEquals("t", method.getParameters().get(0).getName());
        assertEquals("T", method.getParameters().get(0).getType().getShortName());
        assertEquals(1, method.getTypeParameters().size());
        assertEquals("T", method.getTypeParameters().get(0).getName());
        assertEquals(1, method.getBodyLines().size());
        assertEquals("return t;", method.getBodyLines().get(0));
    }

    @Test
    public void testAddBodyLines() {

        Method method = new Method("bar");
        assertEquals(0, method.getBodyLines().size());

        method.addBodyLine("// test1");
        assertEquals(1, method.getBodyLines().size());
        assertEquals("// test1", method.getBodyLines().get(0));

        method.addBodyLine(0, "// test0");
        assertEquals(2, method.getBodyLines().size());
        assertEquals("// test0", method.getBodyLines().get(0));

        method.addBodyLines(Arrays.asList("// test2", "// test3"));
        assertEquals(4, method.getBodyLines().size());
        assertEquals("// test2", method.getBodyLines().get(2));
        assertEquals("// test3", method.getBodyLines().get(3));

        method.addBodyLines(3, Arrays.asList("// test2.1", "// test2.2"));
        assertEquals(6, method.getBodyLines().size());
        assertEquals("// test2", method.getBodyLines().get(2));
        assertEquals("// test2.1", method.getBodyLines().get(3));
        assertEquals("// test2.2", method.getBodyLines().get(4));
        assertEquals("// test3", method.getBodyLines().get(5));
    }

    @Test
    public void testSetConstructor() {

        Method method = new Method("Bar");
        assertEquals(false, method.isConstructor());

        method.setConstructor(true);
        assertEquals(true, method.isConstructor());
    }

    @Test
    public void testSetName() {

        Method method = new Method("bar");
        assertEquals("bar", method.getName());

        method.setName("foo");
        assertEquals("foo", method.getName());
    }

    @Test
    public void testAddTypeParamaters() {

        Method method = new Method("bar");
        assertEquals(0, method.getTypeParameters().size());

        method.addTypeParameter(new TypeParameter("T"));
        assertEquals(1, method.getTypeParameters().size());
        assertEquals("T", method.getTypeParameters().get(0).getName());

        method.addTypeParameter(0, new TypeParameter("U"));
        assertEquals(2, method.getTypeParameters().size());
        assertEquals("U", method.getTypeParameters().get(0).getName());
        assertEquals("T", method.getTypeParameters().get(1).getName());
    }

    @Test
    public void testAddParamaters() {

        Method method = new Method("bar");
        assertEquals(0, method.getParameters().size());

        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "test1"));
        assertEquals(1, method.getParameters().size());
        assertEquals("test1", method.getParameters().get(0).getName());

        method.addParameter(0, new Parameter(FullyQualifiedJavaType.getStringInstance(), "test2"));
        assertEquals(2, method.getParameters().size());
        assertEquals("test2", method.getParameters().get(0).getName());
        assertEquals("test1", method.getParameters().get(1).getName());
    }

    @Test
    public void testSetReturnType() {

        Method method = new Method("bar");
        assertFalse(method.getReturnType().isPresent());

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        assertEquals(FullyQualifiedJavaType.getIntInstance(), method.getReturnType().get());
    }

    @Test
    public void testAddExceptions() {

        Method method = new Method("bar");
        assertEquals(0, method.getExceptions().size());

        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        assertEquals(1, method.getExceptions().size());
        assertEquals("Exception", method.getExceptions().get(0).getShortName());
    }

    @Test
    public void testSetSynchronized() {

        Method method = new Method("bar");
        assertEquals(false, method.isSynchronized());
        method.setSynchronized(true);
        assertEquals(true, method.isSynchronized());
    }

    @Test
    public void testSetNative() {

        Method method = new Method("bar");
        assertEquals(false, method.isNative());
        method.setNative(true);
        assertEquals(true, method.isNative());
    }

    @Test
    public void testGetFormattedContent() {

        Method method = new Method("foo");
        method.setConstructor(false);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setStatic(true);
        method.setSynchronized(true);
        method.setFinal(true);

        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        FullyQualifiedJavaType typeT = new FullyQualifiedJavaType("T");

        FullyQualifiedJavaType comparatorType = new FullyQualifiedJavaType("java.util.Comparator");
        comparatorType.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        FullyQualifiedJavaType typeR = new FullyQualifiedJavaType("R");

        method.addTypeParameter(new TypeParameter("T", Collections.singletonList(listType)));
        method.addTypeParameter(new TypeParameter("R", Arrays.asList(listType, comparatorType)));

        FullyQualifiedJavaType functionType = new FullyQualifiedJavaType("java.util.Function");
        functionType.addTypeArgument(typeT);
        functionType.addTypeArgument(typeR);
        method.addParameter(new Parameter(typeT, "t"));
        method.addParameter(new Parameter(functionType, "func"));
        method.setReturnType(typeR);

        method.addBodyLine("return func.apply(t);");

        String excepted = "public static final synchronized <T extends List<String>, R extends List<String> & Comparator<String>> R foo(T t, Function<T, R> func) {" + LINE_SEPARATOR
                        + "    return func.apply(t);" + LINE_SEPARATOR
                        + "}";

        MethodRenderer renderer = new MethodRenderer();
        String rendered = renderer.render(method, false, null).stream().collect(Collectors.joining(LINE_SEPARATOR));
        assertEquals(excepted, rendered);
    }
}
