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
package org.mybatis.generator.api.dom.java;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.api.dom.OutputUtilities.newLine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * The Class Interface.
 *
 * @author Jeff Butler
 */
public class InnerInterface extends JavaElement {

    private List<Field> fields;

    /** The type. */
    private FullyQualifiedJavaType type;

    /** The inner interfaces. */
    private List<InnerInterface> innerInterfaces;

    /** The super interface types. */
    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    /** The methods. */
    private List<Method> methods;

    /**
     * Instantiates a new interface.
     *
     * @param type
     *            the type
     */
    public InnerInterface(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        innerInterfaces = new ArrayList<InnerInterface>();
        superInterfaceTypes = new LinkedHashSet<FullyQualifiedJavaType>();
        methods = new ArrayList<Method>();
        fields = new ArrayList<Field>();
    }

    /**
     * Instantiates a new interface.
     *
     * @param type
     *            the type
     */
    public InnerInterface(String type) {
        this(new FullyQualifiedJavaType(type));
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    /**
     * Gets the formatted content.
     *
     * @param indentLevel
     *            the indent level
     * @param compilationUnit the compilation unit
     * @return the formatted content
     */
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

    /**
     * Adds the super interface.
     *
     * @param superInterface
     *            the super interface
     */
    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }

    /**
     * Gets the methods.
     *
     * @return Returns the methods.
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * Adds the method.
     *
     * @param method
     *            the method
     */
    public void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * Gets the type.
     *
     * @return Returns the type.
     */
    public FullyQualifiedJavaType getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#getSuperClass()
     */
    public FullyQualifiedJavaType getSuperClass() {
        // interfaces do not have superclasses
        return null;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#getSuperInterfaceTypes()
     */
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }

    /**
     * Gets the inner interface.
     *
     * @return Returns the innerInterfaces.
     */
    public List<InnerInterface> getInnerInterfaces() {
        return innerInterfaces;
    }

    /**
     * Adds the inner interface.
     *
     * @param innerInterface
     *            the inner interface
     */
    public void addInnerInterfaces(InnerInterface innerInterface) {
        innerInterfaces.add(innerInterface);
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#isJavaInterface()
     */
    public boolean isJavaInterface() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#isJavaEnumeration()
     */
    public boolean isJavaEnumeration() {
        return false;
    }
}
