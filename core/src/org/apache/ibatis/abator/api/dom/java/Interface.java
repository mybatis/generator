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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.ibatis.abator.api.dom.OutputUtilities;

/**
 * @author Jeff Butler
 */
public class Interface extends JavaElement implements CompilationUnit {
    private Set importedTypes;

    private FullyQualifiedJavaType type;

    private Set superInterfaceTypes;

    private List methods;
    
    private List fileCommentLines;

    /**
     *  
     */
    public Interface(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        superInterfaceTypes = new LinkedHashSet();
        methods = new ArrayList();
        importedTypes = new TreeSet();
        fileCommentLines = new ArrayList();
    }

    public Set getImportedTypes() {
        return Collections.unmodifiableSet(importedTypes);
    }

    public void addImportedType(FullyQualifiedJavaType importedType) {
        if (importedType.isExplicitlyImported() &&
                !importedType.getPackageName().equals(type.getPackageName())) {
            importedTypes.add(importedType);
        }
    }

    public String getFormattedContent() {
        StringBuffer sb = new StringBuffer();

        Iterator iter = fileCommentLines.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next());
            OutputUtilities.newLine(sb);
        }

        if (getType().getPackageName() != null
                && getType().getPackageName().length() > 0) {
            sb.append("package "); //$NON-NLS-1$
            sb.append(getType().getPackageName());
            sb.append(';');
            OutputUtilities.newLine(sb);
            OutputUtilities.newLine(sb);
        }

        iter = importedTypes.iterator();
        while (iter.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) iter.next();
            if (fqjt.isExplicitlyImported()) {
                sb.append("import "); //$NON-NLS-1$
                sb.append(fqjt.getFullyQualifiedName());
                sb.append(';');
                OutputUtilities.newLine(sb);
            }
        }

        if (importedTypes.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        int indentLevel = 0;

        iter = getJavaDocLines().iterator();
        while (iter.hasNext()) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(iter.next());
            OutputUtilities.newLine(sb);
        }

        iter = getAnnotations().iterator();
        while (iter.hasNext()) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(iter.next());
            OutputUtilities.newLine(sb);
        }
        
        OutputUtilities.javaIndent(sb, indentLevel);
        if (getVisibility() == JavaVisibility.PRIVATE) {
            sb.append("private "); //$NON-NLS-1$
        } else if (getVisibility() == JavaVisibility.PROTECTED) {
            sb.append("protected "); //$NON-NLS-1$
        } else if (getVisibility() == JavaVisibility.PUBLIC) {
            sb.append("public "); //$NON-NLS-1$
        }

        if (isModifierStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (isModifierFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        sb.append("interface "); //$NON-NLS-1$
        sb.append(getType().getShortName());

        if (getSuperInterfaceTypes().size() > 0) {
            sb.append(" extends "); //$NON-NLS-1$

            iter = getSuperInterfaceTypes().iterator();
            boolean comma = false;
            while (iter.hasNext()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }

                FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) iter
                        .next();
                sb.append(fqjt.getShortName());
            }
        }

        sb.append(" {"); //$NON-NLS-1$
        indentLevel++;

        iter = getMethods().iterator();
        while (iter.hasNext()) {
            OutputUtilities.newLine(sb);
            Method method = (Method) iter.next();
            sb.append(method.getFormattedContent(indentLevel, true));
            if (iter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        indentLevel--;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }

    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }

    /**
     * @return Returns the methods.
     */
    public List getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * @return Returns the type.
     */
    public FullyQualifiedJavaType getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.internal.java.dom.CompilationUnit#getSuperClass()
     */
    public FullyQualifiedJavaType getSuperClass() {
        // interfaces do not have superclasses
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.internal.java.dom.CompilationUnit#getSuperInterfaceTypes()
     */
    public Set getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.internal.java.dom.CompilationUnit#isJavaInterface()
     */
    public boolean isJavaInterface() {
        return true;
    }

    public boolean isJavaEnumeration() {
        return false;
    }

    public void addFileCommentLine(String commentLine) {
        fileCommentLines.add(commentLine);
    }

    public List getFileCommentLines() {
        return fileCommentLines;
    }
}
