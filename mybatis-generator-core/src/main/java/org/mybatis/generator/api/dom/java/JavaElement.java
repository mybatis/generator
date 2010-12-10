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
import java.util.List;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * @author Jeff Butler
 */
public abstract class JavaElement {
    private List<String> javaDocLines;

    private JavaVisibility visibility = JavaVisibility.DEFAULT;

    private boolean isStatic;

    private boolean isFinal;

    private List<String> annotations;

    /**
     *  
     */
    public JavaElement() {
        super();
        javaDocLines = new ArrayList<String>();
        annotations = new ArrayList<String>();
    }

    /**
     * @return Returns the javaDocLines.
     */
    public List<String> getJavaDocLines() {
        return javaDocLines;
    }

    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }

    /**
     * @return Returns the visibility.
     */
    public JavaVisibility getVisibility() {
        return visibility;
    }

    /**
     * @param visibility
     *            The visibility to set.
     */
    public void setVisibility(JavaVisibility visibility) {
        this.visibility = visibility;
    }

    public void addSuppressTypeWarningsAnnotation() {
        addAnnotation("@SuppressWarnings(\"unchecked\")"); //$NON-NLS-1$
    }

    public void addFormattedJavadoc(StringBuilder sb, int indentLevel) {
        for (String javaDocLine : javaDocLines) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtilities.newLine(sb);
        }
    }

    public void addFormattedAnnotations(StringBuilder sb, int indentLevel) {
        for (String annotation : annotations) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(annotation);
            OutputUtilities.newLine(sb);
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
}
