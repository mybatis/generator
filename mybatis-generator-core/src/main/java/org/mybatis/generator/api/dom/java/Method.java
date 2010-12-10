/*
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.api.dom.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * @author Jeff Butler
 */
public class Method extends JavaElement {

    private List<String> bodyLines;

    private boolean constructor;

    private FullyQualifiedJavaType returnType;

    private String name;

    private List<Parameter> parameters;

    private List<FullyQualifiedJavaType> exceptions;

    /**
     *  
     */
    public Method() {
        // use a default name to avoid malformed code
        this("bar"); //$NON-NLS-1$
    }
    
    public Method(String name) {
        super();
        bodyLines = new ArrayList<String>();
        parameters = new ArrayList<Parameter>();
        exceptions = new ArrayList<FullyQualifiedJavaType>();
        this.name = name;
    }

    /**
     * @return Returns the bodyLines.
     */
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

    public String getFormattedContent(int indentLevel, boolean interfaceMethod) {
        StringBuilder sb = new StringBuilder();

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        OutputUtilities.javaIndent(sb, indentLevel);

        if (!interfaceMethod) {
            sb.append(getVisibility().getValue());

            if (isStatic()) {
                sb.append("static "); //$NON-NLS-1$
            }

            if (isFinal()) {
                sb.append("final "); //$NON-NLS-1$
            }

            if (bodyLines.size() == 0) {
                sb.append("abstract "); //$NON-NLS-1$
            }
        }

        if (!constructor) {
            if (getReturnType() == null) {
                sb.append("void"); //$NON-NLS-1$
            } else {
                sb.append(getReturnType().getShortName());
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

            sb.append(parameter.getFormattedContent());
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

                sb.append(fqjt.getShortName());
            }
        }

        // if no body lines, then this is an abstract method
        if (bodyLines.size() == 0) {
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
     * @return Returns the constructor.
     */
    public boolean isConstructor() {
        return constructor;
    }

    /**
     * @param constructor
     *            The constructor to set.
     */
    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * @return Returns the returnType.
     */
    public FullyQualifiedJavaType getReturnType() {
        return returnType;
    }

    /**
     * @param returnType
     *            The returnType to set.
     */
    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    /**
     * @return Returns the exceptions.
     */
    public List<FullyQualifiedJavaType> getExceptions() {
        return exceptions;
    }

    public void addException(FullyQualifiedJavaType exception) {
        exceptions.add(exception);
    }
}
