/**
 *    Copyright 2006-2017 the original author or authors.
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
package org.mybatis.generator.codegen.mybatis3;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;

/**
 * Couple of little utility methods to make dealing with generated always
 * columns easier.  If a column is GENERATED ALWAYS, it should not
 * be references on an insert or update method.
 * 
 * <p>If a column is identity, it should not be referenced on an insert method.
 *  
 * <p>TODO - Replace this with Lambdas when we get to Java 8
 * 
 * @author Jeff Butler
 *
 */
public class ListUtilities {

    public static List<IntrospectedColumn> removeGeneratedAlwaysColumns(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!ic.isGeneratedAlways()) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }

    public static List<IntrospectedColumn> removeIdentityAndGeneratedAlwaysColumns(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!ic.isGeneratedAlways() && !ic.isIdentity()) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }
}
