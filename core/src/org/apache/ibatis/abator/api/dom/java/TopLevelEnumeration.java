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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.ibatis.abator.api.dom.OutputUtilities;
import org.apache.ibatis.abator.internal.util.messages.Messages;

/**
 * @author Jeff Butler
 *
 */
public class TopLevelEnumeration extends InnerEnum implements CompilationUnit {
    private Set importedTypes;

    private List fileCommentLines;
    
    /**
     * @param type
     */
    public TopLevelEnumeration(FullyQualifiedJavaType type) {
        super(type);
        importedTypes = new TreeSet();
        fileCommentLines = new ArrayList();
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.dom.java.CompilationUnit#getFormattedContent()
     */
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

        sb.append(super.getFormattedContent(0));

        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.dom.java.CompilationUnit#getImportedTypes()
     */
    public Set getImportedTypes() {
        return Collections.unmodifiableSet(importedTypes);
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.dom.java.CompilationUnit#getSuperClass()
     */
    public FullyQualifiedJavaType getSuperClass() {
        throw new UnsupportedOperationException(Messages.getString("RuntimeError.11")); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.dom.java.CompilationUnit#isJavaInterface()
     */
    public boolean isJavaInterface() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.dom.java.CompilationUnit#isJavaEnumeration()
     */
    public boolean isJavaEnumeration() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.dom.java.CompilationUnit#addImportedType(org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType)
     */
    public void addImportedType(FullyQualifiedJavaType importedType) {
        if (importedType.isExplicitlyImported() &&
                !importedType.getPackageName().equals(getType().getPackageName())) {
            importedTypes.add(importedType);
        }
    }

    public void addFileCommentLine(String commentLine) {
        fileCommentLines.add(commentLine);
    }

    public List getFileCommentLines() {
        return fileCommentLines;
    }
}
