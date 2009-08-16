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

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;

import junit.framework.TestCase;

/**
 * @author Jeff Butler
 *
 */
public class JavaBeansUtilTests extends TestCase {
    
    /**
     * 
     */
    public JavaBeansUtilTests() {
        super();
    }

    /**
     * @param arg0
     */
    public JavaBeansUtilTests(String arg0) {
        super(arg0);
    }
    
    public void testGetValidPropertyName() {
        assertEquals("eMail", JavaBeansUtil.getValidPropertyName("eMail"));
        assertEquals("firstName", JavaBeansUtil.getValidPropertyName("firstName"));
        assertEquals("URL", JavaBeansUtil.getValidPropertyName("URL"));
        assertEquals("XAxis", JavaBeansUtil.getValidPropertyName("XAxis"));
        assertEquals("a", JavaBeansUtil.getValidPropertyName("a"));
        assertEquals("b", JavaBeansUtil.getValidPropertyName("B"));
        assertEquals("yaxis", JavaBeansUtil.getValidPropertyName("Yaxis"));
        assertEquals("i_PARAM_INT_1", JavaBeansUtil.getValidPropertyName("I_PARAM_INT_1"));
        assertEquals("_fred", JavaBeansUtil.getValidPropertyName("_fred"));
        assertEquals("accountType", JavaBeansUtil.getValidPropertyName("AccountType"));
    }

    public void testGetGetterMethodName() {
        assertEquals("geteMail", JavaBeansUtil.getGetterMethodName("eMail", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getFirstName", JavaBeansUtil.getGetterMethodName("firstName", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getURL", JavaBeansUtil.getGetterMethodName("URL", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getXAxis", JavaBeansUtil.getGetterMethodName("XAxis", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getA", JavaBeansUtil.getGetterMethodName("a", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("isActive", JavaBeansUtil.getGetterMethodName("active", FullyQualifiedJavaType.getBooleanPrimitiveInstance()));
        assertEquals("getI_PARAM_INT_1", JavaBeansUtil.getGetterMethodName("i_PARAM_INT_1", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("get_fred", JavaBeansUtil.getGetterMethodName("_fred", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getAccountType", JavaBeansUtil.getGetterMethodName("AccountType", FullyQualifiedJavaType.getStringInstance()));
    }

    public void testGetSetterMethodName() {
        assertEquals("seteMail", JavaBeansUtil.getSetterMethodName("eMail"));
        assertEquals("setFirstName", JavaBeansUtil.getSetterMethodName("firstName"));
        assertEquals("setURL", JavaBeansUtil.getSetterMethodName("URL"));
        assertEquals("setXAxis", JavaBeansUtil.getSetterMethodName("XAxis"));
        assertEquals("setA", JavaBeansUtil.getSetterMethodName("a"));
        assertEquals("setI_PARAM_INT_1", JavaBeansUtil.getSetterMethodName("i_PARAM_INT_1"));
        assertEquals("set_fred", JavaBeansUtil.getSetterMethodName("_fred"));
        assertEquals("setAccountType", JavaBeansUtil.getSetterMethodName("AccountType"));
    }
}
