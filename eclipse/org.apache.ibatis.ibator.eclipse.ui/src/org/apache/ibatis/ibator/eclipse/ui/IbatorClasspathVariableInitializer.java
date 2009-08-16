/*
 *  Copyright 2008 The Apache Software Foundation
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
package org.apache.ibatis.ibator.eclipse.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.framework.Bundle;

/**
 * @author Jeff Butler
 *
 */
public class IbatorClasspathVariableInitializer extends
        ClasspathVariableInitializer {

    public static final String IBATOR_JAR = "IBATOR_JAR"; //$NON-NLS-1$
    public static final String IBATOR_JAR_SRC = "IBATOR_JAR_SRC"; //$NON-NLS-1$
    
    /**
     * 
     */
    public IbatorClasspathVariableInitializer() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ClasspathVariableInitializer#initialize(java.lang.String)
     */
    @Override
    public void initialize(String variable) {
        if (IBATOR_JAR.equals(variable)) {
            initializeIbatorJar();
        } else if (IBATOR_JAR_SRC.equals(variable)) {
            initializeIbatorJarSource();
        }
    }
    
    private void initializeIbatorJar() {
        Bundle bundle= Platform.getBundle("org.apache.ibatis.ibator.core"); //$NON-NLS-1$
        if (bundle == null) {
            JavaCore.removeClasspathVariable(IBATOR_JAR, null);
            return;
        }
        URL installLocation= bundle.getEntry("/ibator.jar"); //$NON-NLS-1$
        URL local= null;
        try {
            local= FileLocator.toFileURL(installLocation);
        } catch (IOException e) {
            JavaCore.removeClasspathVariable(IBATOR_JAR, null);
            return;
        }
        try {
            String fullPath= new File(local.getPath()).getAbsolutePath();
            JavaCore.setClasspathVariable(IBATOR_JAR, new Path(fullPath), null);
        } catch (JavaModelException e1) {
            JavaCore.removeClasspathVariable(IBATOR_JAR, null);
        }
    }

    private void initializeIbatorJarSource() {
        Bundle bundle= Platform.getBundle("org.apache.ibatis.ibator.core"); //$NON-NLS-1$
        if (bundle == null) {
            JavaCore.removeClasspathVariable(IBATOR_JAR_SRC, null);
            return;
        }
        URL installLocation= bundle.getEntry("/ibator-src.zip"); //$NON-NLS-1$
        URL local= null;
        try {
            local= FileLocator.toFileURL(installLocation);
        } catch (IOException e) {
            JavaCore.removeClasspathVariable(IBATOR_JAR_SRC, null);
            return;
        }
        try {
            String fullPath= new File(local.getPath()).getAbsolutePath();
            JavaCore.setClasspathVariable(IBATOR_JAR_SRC, new Path(fullPath), null);
        } catch (JavaModelException e1) {
            JavaCore.removeClasspathVariable(IBATOR_JAR_SRC, null);
        }
    }
}
