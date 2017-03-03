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

import static org.mybatis.generator.api.dom.OutputUtilities.calculateImports;
import static org.mybatis.generator.api.dom.OutputUtilities.newLine;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Class Interface.
 *
 * @author Jeff Butler
 */
public class Interface extends InnerInterface implements CompilationUnit {
    
    /** The imported types. */
    private Set<FullyQualifiedJavaType> importedTypes;
    
    /** The static imports. */
    private Set<String> staticImports;

    /** The file comment lines. */
    private List<String> fileCommentLines;

    /**
     * Instantiates a new interface.
     *
     * @param type
     *            the type
     */
    public Interface(FullyQualifiedJavaType type) {
        super(type);
        importedTypes = new TreeSet<FullyQualifiedJavaType>();
        fileCommentLines = new ArrayList<String>();
        staticImports = new TreeSet<String>();
    }

    /**
     * Instantiates a new interface.
     *
     * @param type
     *            the type
     */
    public Interface(String type) {
        this(new FullyQualifiedJavaType(type));
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#getImportedTypes()
     */
    public Set<FullyQualifiedJavaType> getImportedTypes() {
        return importedTypes;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#addImportedType(org.mybatis.generator.api.dom.java.FullyQualifiedJavaType)
     */
    public void addImportedType(FullyQualifiedJavaType importedType) {
        if (importedType.isExplicitlyImported()
                && !importedType.getPackageName().equals(getType().getPackageName())) {
            importedTypes.add(importedType);
        }
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#getFormattedContent()
     */
    public String getFormattedContent() {

        return getFormattedContent(0, this);
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

        for (String commentLine : fileCommentLines) {
            sb.append(commentLine);
            newLine(sb);
        }

        if (stringHasValue(getType().getPackageName())) {
            sb.append("package "); //$NON-NLS-1$
            sb.append(getType().getPackageName());
            sb.append(';');
            newLine(sb);
            newLine(sb);
        }

        for (String staticImport : staticImports) {
            sb.append("import static "); //$NON-NLS-1$
            sb.append(staticImport);
            sb.append(';');
            newLine(sb);
        }
        
        if (staticImports.size() > 0) {
            newLine(sb);
        }
        
        Set<String> importStrings = calculateImports(importedTypes);
        for (String importString : importStrings) {
            sb.append(importString);
            newLine(sb);
        }

        if (importStrings.size() > 0) {
            newLine(sb);
        }

        sb.append(super.getFormattedContent(0, this));

        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#addFileCommentLine(java.lang.String)
     */
    public void addFileCommentLine(String commentLine) {
        fileCommentLines.add(commentLine);
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#getFileCommentLines()
     */
    public List<String> getFileCommentLines() {
        return fileCommentLines;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#addImportedTypes(java.util.Set)
     */
    public void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes) {
        this.importedTypes.addAll(importedTypes);
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#getStaticImports()
     */
    public Set<String> getStaticImports() {
        return staticImports;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#addStaticImport(java.lang.String)
     */
    public void addStaticImport(String staticImport) {
        staticImports.add(staticImport);
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.java.CompilationUnit#addStaticImports(java.util.Set)
     */
    public void addStaticImports(Set<String> staticImports) {
        this.staticImports.addAll(staticImports);
    }
}
