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

import org.junit.jupiter.api.Test;

public class JavaDomUtilsTest {

    @Test
    public void testGenericTypeNothingImported() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<com.beeant.dto.User>>");
        assertEquals("java.util.Map<java.math.BigDecimal, java.util.List<com.beeant.dto.User>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }

    @Test
    public void testGenericTypeBaseTypeImportedImported() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));

        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Map"));
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<com.beeant.dto.User>>");
        assertEquals("Map<java.math.BigDecimal, java.util.List<com.beeant.dto.User>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }

    @Test
    public void testGenericTypeWithAllTypeParametersImported() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));

        interfaze.addImportedType(new FullyQualifiedJavaType("com.beeant.dto.User"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.math.BigDecimal"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<com.beeant.dto.User>>");
        assertEquals("java.util.Map<BigDecimal, List<User>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }

    @Test
    public void testGenericTypeWithSomeParametersImported() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));

        interfaze.addImportedType(new FullyQualifiedJavaType("com.beeant.dto.User"));
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<com.beeant.dto.User>>");
        assertEquals("java.util.Map<java.math.BigDecimal, java.util.List<User>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }

    @Test
    public void testGenericTypeWithAllImported() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));

        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Map"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        interfaze.addImportedType(new FullyQualifiedJavaType("com.beeant.dto.User"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.math.BigDecimal"));
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<com.beeant.dto.User>>");
        assertEquals("Map<BigDecimal, List<User>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }

    @Test
    public void testGenericTypeWithWildCardAllImported() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));

        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Map"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        interfaze.addImportedType(new FullyQualifiedJavaType("com.beeant.dto.User"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.math.BigDecimal"));
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<? extends com.beeant.dto.User>>");
        assertEquals("Map<BigDecimal, List<? extends User>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }

    @Test
    public void testGenericTypeWithWildCardSomeImported() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));

        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Map"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.math.BigDecimal"));
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<? super com.beeant.dto.User>>");
        assertEquals("Map<BigDecimal, List<? super com.beeant.dto.User>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }

    @Test
    public void testGenericTypeWithWildCard() {
        Interface interfaze = new Interface(new FullyQualifiedJavaType("com.foo.UserMapper"));

        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Map"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.math.BigDecimal"));

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.Map<java.math.BigDecimal, java.util.List<?>>");
        assertEquals("Map<BigDecimal, List<?>>",
                JavaDomUtils.calculateTypeName(interfaze, fqjt));
    }
}
