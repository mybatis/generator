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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * This class encapsulates the idea of an inner enum - it has methods that make
 * it easy to generate inner enum.
 * 
 * @author Jeff Butler
 */
public class InnerEnum extends JavaElement {

    /** The fields. */
    private List<Field> fields;

    /** The inner classes. */
    private List<InnerClass> innerClasses;

    /** The inner enums. */
    private List<InnerEnum> innerEnums;

    /** The type. */
    private FullyQualifiedJavaType type;

    /** The super interface types. */
    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    /** The methods. */
    private List<Method> methods;

    /** The enum constants. */
    private List<String> enumConstants;

    /**
     * Instantiates a new inner enum.
     *
     * @param type
     *            the type
     */
    public InnerEnum(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        fields = new ArrayList<Field>();
        innerClasses = new ArrayList<InnerClass>();
        innerEnums = new ArrayList<InnerEnum>();
        superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
        methods = new ArrayList<Method>();
        enumConstants = new ArrayList<String>();
    }

    /**
     * Gets the fields.
     *
     * @return Returns the fields.
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Adds the field.
     *
     * @param field
     *            the field
     */
    public void addField(Field field) {
        fields.add(field);
    }

    /**
     * Gets the inner classes.
     *
     * @return Returns the innerClasses.
     */
    public List<InnerClass> getInnerClasses() {
        return innerClasses;
    }

    /**
     * Adds the inner class.
     *
     * @param innerClass
     *            the inner class
     */
    public void addInnerClass(InnerClass innerClass) {
        innerClasses.add(innerClass);
    }

    /**
     * Gets the inner enums.
     *
     * @return the inner enums
     */
    public List<InnerEnum> getInnerEnums() {
        return innerEnums;
    }

    /**
     * Adds the inner enum.
     *
     * @param innerEnum
     *            the inner enum
     */
    public void addInnerEnum(InnerEnum innerEnum) {
        innerEnums.add(innerEnum);
    }

    /**
     * Gets the enum constants.
     *
     * @return the enum constants
     */
    public List<String> getEnumConstants() {
        return enumConstants;
    }

    /**
     * Adds the enum constant.
     *
     * @param enumConstant
     *            the enum constant
     */
    public void addEnumConstant(String enumConstant) {
        enumConstants.add(enumConstant);
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

        OutputUtilities.javaIndent(sb, indentLevel);
        if (getVisibility() == JavaVisibility.PUBLIC) {
            sb.append(getVisibility().getValue());
        }

        sb.append("enum "); //$NON-NLS-1$
        sb.append(getType().getShortName());

        if (superInterfaceTypes.size() > 0) {
            sb.append(" implements "); //$NON-NLS-1$

            boolean comma = false;
            for (FullyQualifiedJavaType fqjt : superInterfaceTypes) {
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

        Iterator<String> strIter = enumConstants.iterator();
        while (strIter.hasNext()) {
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            String enumConstant = strIter.next();
            sb.append(enumConstant);

            if (strIter.hasNext()) {
                sb.append(',');
            } else {
                sb.append(';');
            }
        }

        if (fields.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<Field> fldIter = fields.iterator();
        while (fldIter.hasNext()) {
            OutputUtilities.newLine(sb);
            Field field = fldIter.next();
            sb.append(field.getFormattedContent(indentLevel, compilationUnit));
            if (fldIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        if (methods.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<Method> mtdIter = methods.iterator();
        while (mtdIter.hasNext()) {
            OutputUtilities.newLine(sb);
            Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, false, compilationUnit));
            if (mtdIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        if (innerClasses.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<InnerClass> icIter = innerClasses.iterator();
        while (icIter.hasNext()) {
            OutputUtilities.newLine(sb);
            InnerClass innerClass = icIter.next();
            sb.append(innerClass.getFormattedContent(indentLevel, compilationUnit));
            if (icIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        if (innerEnums.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<InnerEnum> ieIter = innerEnums.iterator();
        while (ieIter.hasNext()) {
            OutputUtilities.newLine(sb);
            InnerEnum innerEnum = ieIter.next();
            sb.append(innerEnum.getFormattedContent(indentLevel, compilationUnit));
            if (ieIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        indentLevel--;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }

    /**
     * Gets the super interface types.
     *
     * @return Returns the superInterfaces.
     */
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
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
}
