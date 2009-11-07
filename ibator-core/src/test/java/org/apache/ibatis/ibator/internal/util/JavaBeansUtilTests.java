/*
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.ibatis.ibator.internal.util;

import static org.junit.Assert.assertEquals;

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.junit.Test;

/**
 * @author Jeff Butler
 *
 */
public class JavaBeansUtilTests {
    
    /**
     * 
     */
    public JavaBeansUtilTests() {
        super();
    }

    @Test
    public void testGetValidPropertyName() {
        assertEquals("eMail", JavaBeansUtil.getValidPropertyName("eMail")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("firstName", JavaBeansUtil.getValidPropertyName("firstName")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("URL", JavaBeansUtil.getValidPropertyName("URL")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("XAxis", JavaBeansUtil.getValidPropertyName("XAxis")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("a", JavaBeansUtil.getValidPropertyName("a")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("b", JavaBeansUtil.getValidPropertyName("B")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("yaxis", JavaBeansUtil.getValidPropertyName("Yaxis")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("i_PARAM_INT_1", JavaBeansUtil.getValidPropertyName("I_PARAM_INT_1")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("_fred", JavaBeansUtil.getValidPropertyName("_fred")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("accountType", JavaBeansUtil.getValidPropertyName("AccountType")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testGetGetterMethodName() {
        assertEquals("geteMail", JavaBeansUtil.getGetterMethodName("eMail", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getFirstName", JavaBeansUtil.getGetterMethodName("firstName", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getURL", JavaBeansUtil.getGetterMethodName("URL", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getXAxis", JavaBeansUtil.getGetterMethodName("XAxis", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getA", JavaBeansUtil.getGetterMethodName("a", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("isActive", JavaBeansUtil.getGetterMethodName("active", FullyQualifiedJavaType.getBooleanPrimitiveInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getI_PARAM_INT_1", JavaBeansUtil.getGetterMethodName("i_PARAM_INT_1", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("get_fred", JavaBeansUtil.getGetterMethodName("_fred", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("getAccountType", JavaBeansUtil.getGetterMethodName("AccountType", FullyQualifiedJavaType.getStringInstance())); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testGetSetterMethodName() {
        assertEquals("seteMail", JavaBeansUtil.getSetterMethodName("eMail")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setFirstName", JavaBeansUtil.getSetterMethodName("firstName")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setURL", JavaBeansUtil.getSetterMethodName("URL")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setXAxis", JavaBeansUtil.getSetterMethodName("XAxis")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setA", JavaBeansUtil.getSetterMethodName("a")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setI_PARAM_INT_1", JavaBeansUtil.getSetterMethodName("i_PARAM_INT_1")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("set_fred", JavaBeansUtil.getSetterMethodName("_fred")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("setAccountType", JavaBeansUtil.getSetterMethodName("AccountType")); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
