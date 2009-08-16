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
package org.apache.ibatis.abator.api.dom.java;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeff Butler
 */
public abstract class JavaElement {
    private List javaDocLines;

    private JavaVisibility visibility;

    private boolean modifierStatic;

    private boolean modifierFinal;
    
    private List annotations;

    /**
     *  
     */
    public JavaElement() {
        super();
        javaDocLines = new ArrayList();
        annotations = new ArrayList();
    }

    /**
     * @return Returns the javaDocLines.
     */
    public List getJavaDocLines() {
        return javaDocLines;
    }
    
    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }
    
    public List getAnnotations() {
        return annotations;
    }
    
    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }

    /**
     * @return Returns the modifierFinal.
     */
    public boolean isModifierFinal() {
        return modifierFinal;
    }

    /**
     * @param modifierFinal
     *            The modifierFinal to set.
     */
    public void setModifierFinal(boolean modifierFinal) {
        this.modifierFinal = modifierFinal;
    }

    /**
     * @return Returns the modifierStatic.
     */
    public boolean isModifierStatic() {
        return modifierStatic;
    }

    /**
     * @param modifierStatic
     *            The modifierStatic to set.
     */
    public void setModifierStatic(boolean modifierStatic) {
        this.modifierStatic = modifierStatic;
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
}
