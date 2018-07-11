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
package org.mybatis.generator.eclipse.tests.harness.summary;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a base class for Classes, Enums, and Interfaces.
 * They all have methods and super interfaces, as well as a name, fields and
 * inner Annotations, Classes, Enums, and Interfaces.
 * 
 * @author Jeff Butler
 *
 */
public abstract class AbstractTypeOrEnumSummary extends AbstractBodyElementSummary {

    private List<String> methods = new ArrayList<>();
    private List<String> superInterfaces = new ArrayList<>();

    public boolean hasMethod(String method) {
        return methods.contains(method);
    }

    public int getMethodCount() {
        return methods.size();
    }

    public boolean hasSuperInterface(String superInterface) {
        return superInterfaces.contains(superInterface);
    }

    public int getSuperInterfaceCount() {
        return superInterfaces.size();
    }
    
    protected abstract static class AbstractTypeOrEnumSummaryBuilder<T extends AbstractTypeOrEnumSummaryBuilder<T>> extends AbstractBodyElementSummaryBuilder<T> {

        protected T withMethods(List<String> methods) {
            summary().methods = methods;
            return getThis();
        }

        protected T withSuperInterfaces(List<String> superInterfaces) {
            summary().superInterfaces = superInterfaces;
            return getThis();
        }
        
        @Override
        protected abstract AbstractTypeOrEnumSummary summary();
    }
}
