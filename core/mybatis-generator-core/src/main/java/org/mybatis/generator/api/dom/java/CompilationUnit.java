/**
 *    Copyright 2006-2016 the original author or authors.
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

import java.util.List;
import java.util.Set;

/**
 * This interface describes methods common to all Java compilation units (Java
 * classes, interfaces, and enums).
 * 
 * @author Jeff Butler
 */
public interface CompilationUnit {
    
    /**
     * Gets the formatted content.
     *
     * @return the formatted content
     */
    String getFormattedContent();

    /**
     * Gets the imported types.
     *
     * @return the imported types
     */
    Set<FullyQualifiedJavaType> getImportedTypes();
    
    /**
     * Gets the static imports.
     *
     * @return the static imports
     */
    Set<String> getStaticImports();

    /**
     * Gets the super class.
     *
     * @return the super class
     */
    FullyQualifiedJavaType getSuperClass();

    /**
     * Checks if is java interface.
     *
     * @return true, if is java interface
     */
    boolean isJavaInterface();

    /**
     * Checks if is java enumeration.
     *
     * @return true, if is java enumeration
     */
    boolean isJavaEnumeration();

    /**
     * Gets the super interface types.
     *
     * @return the super interface types
     */
    Set<FullyQualifiedJavaType> getSuperInterfaceTypes();

    /**
     * Gets the type.
     *
     * @return the type
     */
    FullyQualifiedJavaType getType();

    /**
     * Adds the imported type.
     *
     * @param importedType
     *            the imported type
     */
    void addImportedType(FullyQualifiedJavaType importedType);

    /**
     * Adds the imported types.
     *
     * @param importedTypes
     *            the imported types
     */
    void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes);
    
    /**
     * Adds the static import.
     *
     * @param staticImport
     *            the static import
     */
    void addStaticImport(String staticImport);
    
    /**
     * Adds the static imports.
     *
     * @param staticImports
     *            the static imports
     */
    void addStaticImports(Set<String> staticImports);

    /**
     * Comments will be written at the top of the file as is, we do not append any start or end comment characters.
     * 
     * Note that in the Eclipse plugin, file comments will not be merged.
     *
     * @param commentLine
     *            the comment line
     */
    void addFileCommentLine(String commentLine);

    /**
     * Gets the file comment lines.
     *
     * @return the file comment lines
     */
    List<String> getFileCommentLines();
}
