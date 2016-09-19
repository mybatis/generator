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

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * This class is an AST visitor that creates a string representation of a
 * Type.
 * 
 * @author Jeff Butler
 *
 */
public class TypeStringifier extends ASTVisitor {
    protected StringBuilder buffer = new StringBuilder();

    public String toString() {
        return buffer.toString();
    }

    @Override
    public boolean visit(QualifiedName node) {
        node.getQualifier().accept(this);
        buffer.append('.');
        node.getName().accept(this);
        return false;
    }

    @Override
    public boolean visit(SimpleName node) {
        buffer.append(node.getIdentifier());
        return false;
    }

    @Override
    public boolean visit(Dimension node) {
        buffer.append("[]"); //$NON-NLS-1$
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(ParameterizedType node) {
        node.getType().accept(this);
        buffer.append('<');
        for (Iterator<Type> it = node.typeArguments().iterator(); it.hasNext();) {
            Type t = it.next();
            t.accept(this);
            if (it.hasNext()) {
                buffer.append(',');
            }
        }
        buffer.append('>');
        return false;
    }

    @Override
    public boolean visit(PrimitiveType node) {
        buffer.append(node.getPrimitiveTypeCode().toString());
        return false;
    }

    @Override
    public boolean visit(WildcardType node) {
        buffer.append('?');
        Type bound = node.getBound();
        if (bound != null) {
            if (node.isUpperBound()) {
                buffer.append(" extends ");//$NON-NLS-1$
            } else {
                buffer.append(" super ");//$NON-NLS-1$
            }
            bound.accept(this);
        }
        return false;
    }

    @Override
    public boolean visit(QualifiedType node) {
        node.getQualifier().accept(this);
        buffer.append('.');
        node.getName().accept(this);
        return false;
    }

    @Override
    public boolean visit(SimpleType node) {
        node.getName().accept(this);
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean visit(ArrayType node) {
        node.getElementType().accept(this);
        List<Dimension> dimensions = node.dimensions();
        for (Dimension dimension : dimensions) {
            dimension.accept(this);
        }
        return false;
    }

    @Override
    public boolean visit(NameQualifiedType node) {
        node.getQualifier().accept(this);
        buffer.append('.');
        node.getName().accept(this);
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(UnionType node) {
        for (Iterator<Type> it = node.types().iterator(); it.hasNext();) {
            Type t = it.next();
            t.accept(this);
            if (it.hasNext()) {
                buffer.append('|');
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean visit(IntersectionType node) {
        for (Iterator<Type> it = node.types().iterator(); it.hasNext();) {
            Type t = it.next();
            t.accept(this);
            if (it.hasNext()) {
                buffer.append(" & "); //$NON-NLS-1$
            }
        }
        return false;
    }
}
