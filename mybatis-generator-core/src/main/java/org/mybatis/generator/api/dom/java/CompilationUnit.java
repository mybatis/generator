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

import java.util.List;
import java.util.Set;

/**
 * This interface describes methods common to all Java compilation units (Java
 * classes, interfaces, and enums).
 * 
 * @author Jeff Butler
 */
public interface CompilationUnit {
    String getFormattedContent();

    Set<FullyQualifiedJavaType> getImportedTypes();
    
    Set<String> getStaticImports();

    FullyQualifiedJavaType getSuperClass();

    boolean isJavaInterface();

    boolean isJavaEnumeration();

    Set<FullyQualifiedJavaType> getSuperInterfaceTypes();

    FullyQualifiedJavaType getType();

    void addImportedType(FullyQualifiedJavaType importedType);

    void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes);
    
    void addStaticImport(String staticImport);
    
    void addStaticImports(Set<String> staticImports);

    /**
     * Comments will be written at the top of the file as is, we do not append
     * any start or end comment characters.
     * 
     * Note that in the Eclipse plugin, file comments will not be merged.
     * 
     * @param commentLine
     */
    void addFileCommentLine(String commentLine);

    List<String> getFileCommentLines();
}
