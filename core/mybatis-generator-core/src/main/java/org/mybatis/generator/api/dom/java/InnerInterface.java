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
package org.mybatis.generator.api.dom.java;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.api.dom.OutputUtilities.newLine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.dom.OutputUtilities;

public class InnerInterface extends JavaElement {

    private List<Field> fields;

    private FullyQualifiedJavaType type;

    private List<InnerInterface> innerInterfaces;

    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    private List<Method> methods;

    public InnerInterface(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        innerInterfaces = new ArrayList<>();
        superInterfaceTypes = new LinkedHashSet<>();
        methods = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public InnerInterface(String type) {
        this(new FullyQualifiedJavaType(type));
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        javaIndent(sb, indentLevel);
        sb.append(getVisibility().getValue());

        if (isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        sb.append("interface "); //$NON-NLS-1$
        sb.append(getType().getShortName());

        if (getSuperInterfaceTypes().size() > 0) {
            sb.append(" extends "); //$NON-NLS-1$

            boolean comma = false;
            for (FullyQualifiedJavaType fqjt : getSuperInterfaceTypes()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }

                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt));
            }
        }

        sb.append(" {"); //$NON-NLS-1$
        indentLevel++;

        Iterator<Field> fldIter = fields.iterator();
        while (fldIter.hasNext()) {
            OutputUtilities.newLine(sb);
            Field field = fldIter.next();
            sb.append(field.getFormattedContent(indentLevel, compilationUnit));
        }

        if (fields.size() > 0 && methods.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<Method> mtdIter = getMethods().iterator();
        while (mtdIter.hasNext()) {
            newLine(sb);
            Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, true, compilationUnit));
            if (mtdIter.hasNext()) {
                newLine(sb);
            }
        }

        if (innerInterfaces.size() > 0) {
            newLine(sb);
        }
        Iterator<InnerInterface> iiIter = innerInterfaces.iterator();
        while (iiIter.hasNext()) {
            newLine(sb);
            InnerInterface innerInterface = iiIter.next();
            sb.append(innerInterface.getFormattedContent(indentLevel, compilationUnit));
            if (iiIter.hasNext()) {
                newLine(sb);
            }
        }

        indentLevel--;
        newLine(sb);
        javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }

    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public FullyQualifiedJavaType getSuperClass() {
        // interfaces do not have superclasses
        return null;
    }

    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }

    public List<InnerInterface> getInnerInterfaces() {
        return innerInterfaces;
    }

    public void addInnerInterfaces(InnerInterface innerInterface) {
        innerInterfaces.add(innerInterface);
    }

    public boolean isJavaInterface() {
        return true;
    }

    public boolean isJavaEnumeration() {
        return false;
    }
}
