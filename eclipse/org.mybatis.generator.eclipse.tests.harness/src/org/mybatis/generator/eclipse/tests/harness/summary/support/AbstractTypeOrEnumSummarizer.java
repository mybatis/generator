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
package org.mybatis.generator.eclipse.tests.harness.summary.support;

import static org.mybatis.generator.eclipse.tests.harness.Utilities.getTypeString;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public abstract class AbstractTypeOrEnumSummarizer extends AbstractBodyElementSummarizer {

    private List<Type> superInterfaceTypes;
    
    @SuppressWarnings("unchecked")
    public AbstractTypeOrEnumSummarizer(TypeDeclaration node) {
        super(node);
        superInterfaceTypes = node.superInterfaceTypes();
    }
    
    @SuppressWarnings("unchecked")
    public AbstractTypeOrEnumSummarizer(EnumDeclaration node) {
        super(node);
        superInterfaceTypes = node.superInterfaceTypes();
    }

    public List<String> getSuperInterfaces() {
        List<String> superInterfaces = new ArrayList<>();
        for (Type superInterfaceType : superInterfaceTypes) {
            superInterfaces.add(getTypeString(superInterfaceType));
        }
        return superInterfaces;
    }
}
