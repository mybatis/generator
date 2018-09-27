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

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.dom.java.render.InnerClassRenderer;

public class InnerClassTest {

    private static final String LF = System.getProperty("line.separator");

    @Test
    public void testConstructor() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertNotNull(clazz);

        assertFalse(clazz.getSuperClass().isPresent());

        assertNotNull(clazz.getType());
        assertEquals("com.foo.UserClass", clazz.getType().getFullyQualifiedName());

        assertNotNull(clazz.getFields());
        assertEquals(0, clazz.getFields().size());

        assertNotNull(clazz.getInnerClasses());
        assertEquals(0, clazz.getInnerClasses().size());

        assertNotNull(clazz.getInnerEnums());
        assertEquals(0, clazz.getInnerEnums().size());

        assertNotNull(clazz.getTypeParameters());
        assertEquals(0, clazz.getTypeParameters().size());

        assertNotNull(clazz.getSuperInterfaceTypes());
        assertEquals(0, clazz.getSuperInterfaceTypes().size());

        assertNotNull(clazz.getMethods());
        assertEquals(0, clazz.getMethods().size());

        assertNotNull(clazz.getInitializationBlocks());
        assertEquals(0, clazz.getInitializationBlocks().size());

        assertFalse(clazz.isAbstract());
    }

    @Test
    public void testAddFields() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertEquals(0, clazz.getFields().size());
        clazz.addField(new Field("test", FullyQualifiedJavaType.getStringInstance()));
        assertEquals(1, clazz.getFields().size());
        clazz.addField(new Field("test2", FullyQualifiedJavaType.getStringInstance()));
        assertEquals(2, clazz.getFields().size());
    }

    @Test
    public void testSetSuperClass() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertFalse(clazz.getSuperClass().isPresent());
        clazz.setSuperClass("com.hoge.SuperClass");
        assertNotNull(clazz.getSuperClass());
        assertEquals("com.hoge.SuperClass", clazz.getSuperClass().get().getFullyQualifiedName());
    }

    @Test
    public void testAddInnerClasses() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertEquals(0, clazz.getInnerClasses().size());
        clazz.addInnerClass(new InnerClass("InnerUserClass"));
        assertEquals(1, clazz.getInnerClasses().size());
        clazz.addInnerClass(new InnerClass("InnerUserClass2"));
        assertEquals(2, clazz.getInnerClasses().size());
    }

    @Test
    public void testAddInnerEnum() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertEquals(0, clazz.getInnerEnums().size());
        clazz.addInnerEnum(new InnerEnum(new FullyQualifiedJavaType("TestEnum")));
        assertEquals(1, clazz.getInnerEnums().size());
        clazz.addInnerEnum(new InnerEnum(new FullyQualifiedJavaType("TestEnum2")));
        assertEquals(2, clazz.getInnerEnums().size());
    }

    @Test
    public void testAddTypeParameter() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertEquals(0, clazz.getTypeParameters().size());
        clazz.addTypeParameter(new TypeParameter("T"));
        assertEquals(1, clazz.getTypeParameters().size());
        clazz.addTypeParameter(new TypeParameter("U"));
        assertEquals(2, clazz.getTypeParameters().size());
    }

    @Test
    public void testAddInitializationBlock() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertEquals(0, clazz.getInitializationBlocks().size());
        clazz.addInitializationBlock(new InitializationBlock(false));
        assertEquals(1, clazz.getInitializationBlocks().size());
        clazz.addInitializationBlock(new InitializationBlock(true));
        assertEquals(2, clazz.getInitializationBlocks().size());
    }

    @Test
    public void testAddSuperInterface() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertEquals(0, clazz.getSuperInterfaceTypes().size());
        clazz.addSuperInterface(new FullyQualifiedJavaType("com.hoge.UserInterface"));
        assertEquals(1, clazz.getSuperInterfaceTypes().size());
        clazz.addSuperInterface(new FullyQualifiedJavaType("com.hoge.UserInterface2"));
        assertEquals(2, clazz.getSuperInterfaceTypes().size());
    }

    @Test
    public void testAddMethod() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertEquals(0, clazz.getMethods().size());
        clazz.addMethod(new Method("method1"));
        assertEquals(1, clazz.getMethods().size());
        clazz.addMethod(new Method("method2"));
        assertEquals(2, clazz.getMethods().size());
    }

    @Test
    public void testSetAbstract() {
        InnerClass clazz = new InnerClass("com.foo.UserClass");

        assertFalse(clazz.isAbstract());
        clazz.setAbstract(true);
        assertTrue(clazz.isAbstract());
    }

    @Test
    public void testGetFormattedContent() {
        InnerClass innerClass = new InnerClass("com.foo.UserClass");
        innerClass.addField(new Field("test", FullyQualifiedJavaType.getStringInstance()));
        innerClass.setSuperClass("com.hoge.SuperClass");
        innerClass.addInnerClass(new InnerClass("InnerUserClass"));
        innerClass.addInnerEnum(new InnerEnum(new FullyQualifiedJavaType("TestEnum")));
        innerClass.addTypeParameter(new TypeParameter("T"));
        innerClass.addTypeParameter(new TypeParameter("U"));
        innerClass.addInitializationBlock(new InitializationBlock(false));
        innerClass.addSuperInterface(new FullyQualifiedJavaType("com.hoge.UserInterface"));
        
        Method method = new Method("method1");
        method.setAbstract(true);
        innerClass.addMethod(method);
        innerClass.setAbstract(true);

        String excepted = "abstract class UserClass<T, U>  extends SuperClass implements UserInterface {" + LF
                + "    String test;" + LF
                + "" + LF
                + "    {" + LF
                + "    }" + LF
                + "" + LF
                + "    abstract void method1();" + LF
                + "" + LF
                + "    class InnerUserClass {" + LF
                + "    }" + LF
                + "" + LF
                + "    enum TestEnum {" + LF
                + "    }" + LF
                + "}";

        InnerClassRenderer renderer = new InnerClassRenderer();
        String rendered = renderer.render(innerClass, null).stream().collect(Collectors.joining(LF));
        assertEquals(excepted, rendered);
    }
}
