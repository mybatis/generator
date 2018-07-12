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
package org.mybatis.generator.internal.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getSetterMethodName;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getValidPropertyName;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @author Jeff Butler
 *
 */
public class JavaBeansUtilTest {

    /**
     *
     */
    public JavaBeansUtilTest() {
        super();
    }

    @Test
    public void testGetValidPropertyName() {
        assertEquals("eMail", getValidPropertyName("eMail")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("firstName", getValidPropertyName("firstName")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("URL", getValidPropertyName("URL")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("XAxis", getValidPropertyName("XAxis")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("a", getValidPropertyName("a")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("b", getValidPropertyName("B")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("yaxis", getValidPropertyName("Yaxis")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("i_PARAM_INT_1", getValidPropertyName("I_PARAM_INT_1")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("_fred", getValidPropertyName("_fred")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("accountType", getValidPropertyName("AccountType")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testGetGetterMethodName() {
        assertEquals("geteMail", getGetterMethodName("eMail", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getFirstName", getGetterMethodName("firstName", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getURL", getGetterMethodName("URL", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getXAxis", getGetterMethodName("XAxis", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getA", getGetterMethodName("a", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("isActive", getGetterMethodName("active", FullyQualifiedJavaType.getBooleanPrimitiveInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getI_PARAM_INT_1", getGetterMethodName("i_PARAM_INT_1", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("get_fred", getGetterMethodName("_fred", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getAccountType", getGetterMethodName("AccountType", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testGetSetterMethodName() {
        assertEquals("seteMail", getSetterMethodName("eMail")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setFirstName", getSetterMethodName("firstName")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setURL", getSetterMethodName("URL")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setXAxis", getSetterMethodName("XAxis")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setA", getSetterMethodName("a")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setI_PARAM_INT_1", getSetterMethodName("i_PARAM_INT_1")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("set_fred", getSetterMethodName("_fred")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setAccountType", getSetterMethodName("AccountType")); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
