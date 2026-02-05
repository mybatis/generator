/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.java.CompilationUnit;

public class GeneratedJavaFile extends GeneratedFile {
    private final CompilationUnit compilationUnit;
    private final boolean isMergeable;

    public GeneratedJavaFile(CompilationUnit compilationUnit, String targetProject) {
        this(compilationUnit, targetProject, true);
    }

    public GeneratedJavaFile(CompilationUnit compilationUnit, String targetProject, boolean isMergeable) {
        super(targetProject);
        this.compilationUnit = compilationUnit;
        this.isMergeable = isMergeable;
    }

    @Override
    public String getFileName() {
        return compilationUnit.getType().getShortNameWithoutTypeArguments() + ".java"; //$NON-NLS-1$
    }

    @Override
    public String getTargetPackage() {
        return compilationUnit.getType().getPackageName();
    }

    /**
     * Return the compilation unit created for this file. The compilation unit will be run through the
     * Java formatter before it is written to disk.
     *
     * @return the CompilationUnit associated with this file.
     */
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    /**
     * A Java file is mergeable if the getCompilationUnit() method returns a valid compilation unit.
     *
     * @return true if this file is mergeable
     */
    @Override
    public boolean isMergeable() {
        return isMergeable;
    }
}
