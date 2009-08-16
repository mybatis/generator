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
package org.apache.ibatis.abator.api;

import java.util.Set;

import org.apache.ibatis.abator.api.dom.java.CompilationUnit;
import org.apache.ibatis.abator.api.dom.java.FullyQualifiedJavaType;


/**
 * @author Jeff Butler
 */
public class GeneratedJavaFile extends GeneratedFile {
    private CompilationUnit compilationUnit;
    
	/**
	 *  Default constructor
	 */
	public GeneratedJavaFile(CompilationUnit compilationUnit,
            String targetProject) {
		super(targetProject);
        this.compilationUnit = compilationUnit;
	}

	public Set getImportedTypes() {
		return compilationUnit.getImportedTypes();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.apache.ibatis.abator.api.GeneratedFile#getFormattedContent()
	 */
	public String getFormattedContent() {
	    return compilationUnit.getFormattedContent();
	}

	public Set getSuperInterfaceTypes() {
		return compilationUnit.getSuperInterfaceTypes();
	}

    /**
     * @return Returns the superClass.
     */
    public FullyQualifiedJavaType getSuperClass() {
        return compilationUnit.getSuperClass();
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.GeneratedFile#getFileName()
     */
    public String getFileName() {
        return compilationUnit.getType().getShortName() + ".java"; //$NON-NLS-1$
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.GeneratedFile#getTargetPackage()
     */
    public String getTargetPackage() {
        return compilationUnit.getType().getPackageName();
    }
    
    public boolean isJavaInterface() {
        return compilationUnit.isJavaInterface();
    }
}
