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
package org.mybatis.generator.eclipse.core.merge.visitors;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

public class MethodSignatureStringifier extends TypeStringifier {
    @Override
    public boolean visit(MethodDeclaration node) {
        visitTypeParameters(node);
        visitMethodName(node);
        buffer.append('(');
        visitParameters(node);
        buffer.append(')');

        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void visitTypeParameters(MethodDeclaration node) {
        if (!node.typeParameters().isEmpty()) {
            buffer.append('<');
            for (Iterator<TypeParameter> it = node.typeParameters().iterator(); it.hasNext();) {
                TypeParameter t = it.next();
                t.accept(this);
                if (it.hasNext()) {
                    buffer.append(',');
                }
            }
            buffer.append("> "); //$NON-NLS-1$
        }
    }
    
    private void visitMethodName(MethodDeclaration node) {
        node.getName().accept(this);
    }
    
    @SuppressWarnings("unchecked")
    private void visitParameters(MethodDeclaration node) {
        for (Iterator<SingleVariableDeclaration> it = node.parameters().iterator(); it.hasNext();) {
            SingleVariableDeclaration v = it.next();
            v.accept(this);
            if (it.hasNext()) {
                buffer.append(',');
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(TypeParameter node) {
        node.getName().accept(this);
        if (!node.typeBounds().isEmpty()) {
            buffer.append(" extends ");//$NON-NLS-1$
            for (Iterator<Type> it = node.typeBounds().iterator(); it.hasNext();) {
                Type t = it.next();
                t.accept(this);
                if (it.hasNext()) {
                    buffer.append(" & ");//$NON-NLS-1$
                }
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean visit(SingleVariableDeclaration node) {
        node.getType().accept(this);
        for (Dimension dimension : (List<Dimension>) node.extraDimensions()) {
            dimension.accept(this);
        }
        return false;
    }
}
