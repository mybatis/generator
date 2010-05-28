/*
 *  Copyright 2005 The Apache Software Foundation
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
package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.java.CompilationUnit;

/**
 * @author Jeff Butler
 */
public class GeneratedJavaFile extends GeneratedFile {
    private CompilationUnit compilationUnit;

    /**
     * Default constructor
     */
    public GeneratedJavaFile(CompilationUnit compilationUnit,
            String targetProject) {
        super(targetProject);
        this.compilationUnit = compilationUnit;
    }

    @Override
    public String getFormattedContent() {
        return compilationUnit.getFormattedContent();
    }

    @Override
    public String getFileName() {
        return compilationUnit.getType().getShortName() + ".java"; //$NON-NLS-1$
    }

    public String getTargetPackage() {
        return compilationUnit.getType().getPackageName();
    }

    /**
     * This method is required by the Eclipse Java merger. If you are not
     * running in Eclipse, or some other system that implements the Java merge
     * function, you may return null from this method.
     * 
     * @return the CompilationUnit associated with this file, or null if the
     *         file is not mergeable.
     */
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    /**
     * A Java file is mergeable if the getCompilationUnit() method returns a
     * valid compilation unit.
     * 
     */
    @Override
    public boolean isMergeable() {
        return true;
    }
}
