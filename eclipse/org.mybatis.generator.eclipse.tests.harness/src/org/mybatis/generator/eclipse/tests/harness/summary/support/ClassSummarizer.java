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

import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassSummarizer extends AbstractTypeOrEnumSummarizer {

    private TypeDeclaration node;
    
    private ClassSummarizer(TypeDeclaration node) {
        super(node);
        this.node = node;
    }
    
    public static ClassSummarizer from(TypeDeclaration node) {
        return new ClassSummarizer(node);
    }

    public String getSuperClass() {
        Type type = node.getSuperclassType();
        return type == null ? null : getTypeString(type);
    }
}
