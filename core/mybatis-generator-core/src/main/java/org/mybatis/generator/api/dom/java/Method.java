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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * The Class Method.
 *
 * @author Jeff Butler
 */
public class Method extends JavaElement {

    /** The body lines. */
    private List<String> bodyLines;

    /** The constructor. */
    private boolean constructor;

    /** The return type. */
    private FullyQualifiedJavaType returnType;

    /** The name. */
    private String name;

    /** The type parameters. */
    private List<TypeParameter> typeParameters;

    /** The parameters. */
    private List<Parameter> parameters;

    /** The exceptions. */
    private List<FullyQualifiedJavaType> exceptions;

    /** The is synchronized. */
    private boolean isSynchronized;

    /** The is native. */
    private boolean isNative;

    private boolean isDefault;

    /**
     * Instantiates a new method.
     */
    public Method() {
        // use a default name to avoid malformed code
        this("bar"); //$NON-NLS-1$
    }

    /**
     * Instantiates a new method.
     *
     * @param name
     *            the name
     */
    public Method(String name) {
        super();
        bodyLines = new ArrayList<String>();
        typeParameters = new ArrayList<TypeParameter>();
        parameters = new ArrayList<Parameter>();
        exceptions = new ArrayList<FullyQualifiedJavaType>();
        this.name = name;
    }

    /**
     * Copy constructor. Not a truly deep copy, but close enough for most purposes.
     *
     * @param original
     *            the original
     */
    public Method(Method original) {
        super(original);
        bodyLines = new ArrayList<String>();
        typeParameters = new ArrayList<TypeParameter>();
        parameters = new ArrayList<Parameter>();
        exceptions = new ArrayList<FullyQualifiedJavaType>();
        this.bodyLines.addAll(original.bodyLines);
        this.constructor = original.constructor;
        this.exceptions.addAll(original.exceptions);
        this.name = original.name;
        this.typeParameters.addAll(original.typeParameters);
        this.parameters.addAll(original.parameters);
        this.returnType = original.returnType;
        this.isNative = original.isNative;
        this.isSynchronized = original.isSynchronized;
        this.isDefault = original.isDefault;
    }

    /**
     * Gets the body lines.
     *
     * @return Returns the bodyLines.
     */
    public List<String> getBodyLines() {
        return bodyLines;
    }

    /**
     * Adds the body line.
     *
     * @param line
     *            the line
     */
    public void addBodyLine(String line) {
        bodyLines.add(line);
    }

    /**
     * Adds the body line.
     *
     * @param index
     *            the index
     * @param line
     *            the line
     */
    public void addBodyLine(int index, String line) {
        bodyLines.add(index, line);
    }

    /**
     * Adds the body lines.
     *
     * @param lines
     *            the lines
     */
    public void addBodyLines(Collection<String> lines) {
        bodyLines.addAll(lines);
    }

    /**
     * Adds the body lines.
     *
     * @param index
     *            the index
     * @param lines
     *            the lines
     */
    public void addBodyLines(int index, Collection<String> lines) {
        bodyLines.addAll(index, lines);
    }

    /**
     * Gets the formatted content.
     *
     * @param indentLevel
     *            the indent level
     * @param interfaceMethod
     *            the interface method
     * @param compilationUnit the compilation unit
     * @return the formatted content
     */
    public String getFormattedContent(int indentLevel, boolean interfaceMethod, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        OutputUtilities.javaIndent(sb, indentLevel);

        if (interfaceMethod) {
            if (isStatic()) {
                sb.append("static "); //$NON-NLS-1$
            } else if (isDefault()) {
                sb.append("default "); //$NON-NLS-1$
            }
        } else {
            sb.append(getVisibility().getValue());

            if (isStatic()) {
                sb.append("static "); //$NON-NLS-1$
            }

            if (isFinal()) {
                sb.append("final "); //$NON-NLS-1$
            }

            if (isSynchronized()) {
                sb.append("synchronized "); //$NON-NLS-1$
            }

            if (isNative()) {
                sb.append("native "); //$NON-NLS-1$
            } else if (bodyLines.size() == 0) {
                sb.append("abstract "); //$NON-NLS-1$
            }
        }

        if (!getTypeParameters().isEmpty()) {
            sb.append("<"); //$NON-NLS-1$
            boolean comma = false;
            for (TypeParameter typeParameter : getTypeParameters()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }

                sb.append(typeParameter.getFormattedContent(compilationUnit));
            }
            sb.append("> "); //$NON-NLS-1$
        }

        if (!constructor) {
            if (getReturnType() == null) {
                sb.append("void"); //$NON-NLS-1$
            } else {
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, getReturnType()));
            }
            sb.append(' ');
        }

        sb.append(getName());
        sb.append('(');

        boolean comma = false;
        for (Parameter parameter : getParameters()) {
            if (comma) {
                sb.append(", "); //$NON-NLS-1$
            } else {
                comma = true;
            }

            sb.append(parameter.getFormattedContent(compilationUnit));
        }

        sb.append(')');

        if (getExceptions().size() > 0) {
            sb.append(" throws "); //$NON-NLS-1$
            comma = false;
            for (FullyQualifiedJavaType fqjt : getExceptions()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }

                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt));
            }
        }

        // if no body lines, then this is an abstract method
        if (bodyLines.size() == 0 || isNative()) {
            sb.append(';');
        } else {
            sb.append(" {"); //$NON-NLS-1$
            indentLevel++;

            ListIterator<String> listIter = bodyLines.listIterator();
            while (listIter.hasNext()) {
                String line = listIter.next();
                if (line.startsWith("}")) { //$NON-NLS-1$
                    indentLevel--;
                }

                OutputUtilities.newLine(sb);
                OutputUtilities.javaIndent(sb, indentLevel);
                sb.append(line);

                if ((line.endsWith("{") && !line.startsWith("switch")) //$NON-NLS-1$ //$NON-NLS-2$
                        || line.endsWith(":")) { //$NON-NLS-1$
                    indentLevel++;
                }

                if (line.startsWith("break")) { //$NON-NLS-1$
                    // if the next line is '}', then don't outdent
                    if (listIter.hasNext()) {
                        String nextLine = listIter.next();
                        if (nextLine.startsWith("}")) { //$NON-NLS-1$
                            indentLevel++;
                        }

                        // set back to the previous element
                        listIter.previous();
                    }
                    indentLevel--;
                }
            }

            indentLevel--;
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append('}');
        }

        return sb.toString();
    }

    /**
     * Checks if is constructor.
     *
     * @return Returns the constructor.
     */
    public boolean isConstructor() {
        return constructor;
    }

    /**
     * Sets the constructor.
     *
     * @param constructor
     *            The constructor to set.
     */
    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    /**
     * Gets the name.
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type parameters.
     *
     * @return the type parameters
     */
    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    /**
     * Adds the type parameter.
     *
     * @param typeParameter
     *            the type parameter
     */
    public void addTypeParameter(TypeParameter typeParameter) {
        typeParameters.add(typeParameter);
    }

    /**
     * Adds the parameter.
     *
     * @param index
     *            the index
     * @param typeParameter
     *            the type parameter
     */
    public void addTypeParameter(int index, TypeParameter typeParameter) {
        typeParameters.add(index, typeParameter);
    }

    /**
     * Gets the parameters.
     *
     * @return the parameters
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * Adds the parameter.
     *
     * @param parameter
     *            the parameter
     */
    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    /**
     * Adds the parameter.
     *
     * @param index
     *            the index
     * @param parameter
     *            the parameter
     */
    public void addParameter(int index, Parameter parameter) {
        parameters.add(index, parameter);
    }

    /**
     * Gets the return type.
     *
     * @return Returns the returnType.
     */
    public FullyQualifiedJavaType getReturnType() {
        return returnType;
    }

    /**
     * Sets the return type.
     *
     * @param returnType
     *            The returnType to set.
     */
    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    /**
     * Gets the exceptions.
     *
     * @return Returns the exceptions.
     */
    public List<FullyQualifiedJavaType> getExceptions() {
        return exceptions;
    }

    /**
     * Adds the exception.
     *
     * @param exception
     *            the exception
     */
    public void addException(FullyQualifiedJavaType exception) {
        exceptions.add(exception);
    }

    /**
     * Checks if is synchronized.
     *
     * @return true, if is synchronized
     */
    public boolean isSynchronized() {
        return isSynchronized;
    }

    /**
     * Sets the synchronized.
     *
     * @param isSynchronized
     *            the new synchronized
     */
    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    /**
     * Checks if is native.
     *
     * @return true, if is native
     */
    public boolean isNative() {
        return isNative;
    }

    /**
     * Sets the native.
     *
     * @param isNative
     *            the new native
     */
    public void setNative(boolean isNative) {
        this.isNative = isNative;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
