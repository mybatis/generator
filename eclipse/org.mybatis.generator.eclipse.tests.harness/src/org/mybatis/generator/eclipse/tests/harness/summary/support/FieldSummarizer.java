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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldSummarizer {

    private VariableDeclarationFragment node;
    
    private FieldSummarizer(VariableDeclarationFragment node) {
        this.node = node;
    }
    
    public static FieldSummarizer from(VariableDeclarationFragment node) {
        return new FieldSummarizer(node);
    }

    public String getName() {
        return node.getName().getFullyQualifiedName();
    }
    
    public String getFieldType() {
        StringBuilder fieldType = new StringBuilder();
        if (node.getParent().getNodeType() == ASTNode.FIELD_DECLARATION) {
            fieldType.append(getFieldType((FieldDeclaration) node.getParent()));
            fieldType.append(getDimensions());
        }
        
        return fieldType.toString();
    }

    private String getFieldType(FieldDeclaration node) {
        return getTypeString(node.getType());
    }

    private String getDimensions() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < node.getExtraDimensions(); i++) {
            sb.append("[]");
        }
        return sb.toString();
    }
}
