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

public class Method extends JavaElement {

    private List<String> bodyLines;

    private boolean constructor;

    private FullyQualifiedJavaType returnType;

    private String name;

    private List<TypeParameter> typeParameters;

    private List<Parameter> parameters;

    private List<FullyQualifiedJavaType> exceptions;

    private boolean isSynchronized;

    private boolean isNative;

    private boolean isDefault;

    public Method(String name) {
        super();
        bodyLines = new ArrayList<>();
        typeParameters = new ArrayList<>();
        parameters = new ArrayList<>();
        exceptions = new ArrayList<>();
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
        bodyLines = new ArrayList<>();
        typeParameters = new ArrayList<>();
        parameters = new ArrayList<>();
        exceptions = new ArrayList<>();
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

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void addBodyLine(String line) {
        bodyLines.add(line);
    }

    public void addBodyLine(int index, String line) {
        bodyLines.add(index, line);
    }

    public void addBodyLines(Collection<String> lines) {
        bodyLines.addAll(lines);
    }

    public void addBodyLines(int index, Collection<String> lines) {
        bodyLines.addAll(index, lines);
    }

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

    public boolean isConstructor() {
        return constructor;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void addTypeParameter(TypeParameter typeParameter) {
        typeParameters.add(typeParameter);
    }

    public void addTypeParameter(int index, TypeParameter typeParameter) {
        typeParameters.add(index, typeParameter);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    public void addParameter(int index, Parameter parameter) {
        parameters.add(index, parameter);
    }

    public FullyQualifiedJavaType getReturnType() {
        return returnType;
    }

    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    public List<FullyQualifiedJavaType> getExceptions() {
        return exceptions;
    }

    public void addException(FullyQualifiedJavaType exception) {
        exceptions.add(exception);
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public boolean isNative() {
        return isNative;
    }

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
