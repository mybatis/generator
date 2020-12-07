/*
 *    Copyright 2006-2020 the original author or authors.
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

import org.junit.jupiter.api.Test;

class InnerInterfaceTest {

    @Test
    void testConstructor() {
        InnerInterface innerInterface = new InnerInterface("com.foo.InnerUserInterface");
        assertNotNull(innerInterface);
    }

    @Test
    void testAddSuperInterface() {
        InnerInterface innerInterface = new InnerInterface("com.foo.InnerUserInterface");
        FullyQualifiedJavaType superInterType = new FullyQualifiedJavaType("com.foo.SuperUserInterface");
        innerInterface.addSuperInterface(superInterType);
        assertNotNull(innerInterface.getSuperInterfaceTypes());
        assertTrue(innerInterface.getSuperInterfaceTypes().contains(superInterType));
    }

    @Test
    void testAddMethod() {
        InnerInterface interfaze = new InnerInterface("com.foo.UserInterface");
        Method method = new Method("foo");
        interfaze.addMethod(method);

        assertNotNull(interfaze.getMethods());
        assertEquals(1, interfaze.getMethods().size());
        assertSame(interfaze.getMethods().get(0), method);
    }

    @Test
    void testGetType() {
        InnerInterface innerInterface = new InnerInterface("com.foo.InnerUserInterface");
        assertNotNull(innerInterface.getType());
        assertEquals("com.foo.InnerUserInterface", innerInterface.getType().getFullyQualifiedName());
    }

    @Test
    void testAddInnerInterfaces() {
        InnerInterface interfaze = new InnerInterface("com.foo.UserInterface");
        InnerInterface innerInterfaze = new InnerInterface("com.foo.InnerUserInterface");

        interfaze.addInnerInterface(innerInterfaze);
        assertNotNull(interfaze.getInnerInterfaces());
        assertEquals(1, interfaze.getInnerInterfaces().size());
        assertSame(interfaze.getInnerInterfaces().get(0), innerInterfaze);
    }
}
