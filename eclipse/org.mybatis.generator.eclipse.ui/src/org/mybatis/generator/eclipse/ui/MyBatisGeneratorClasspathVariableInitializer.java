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
package org.mybatis.generator.eclipse.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
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
public class MyBatisGeneratorClasspathVariableInitializer extends
        ClasspathVariableInitializer {

    public static final String MBG_HOME = "MBG_HOME"; //$NON-NLS-1$
    public static final String MBG_SRC_HOME = "MBG_SRC_HOME"; //$NON-NLS-1$
    
    /**
     * 
     */
    public MyBatisGeneratorClasspathVariableInitializer() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ClasspathVariableInitializer#initialize(java.lang.String)
     */
    @Override
    public void initialize(String variable) {
        if (MBG_HOME.equals(variable)) {
            initializeGeneratorJar();
        } else if (MBG_SRC_HOME.equals(variable)) {
            initializeGeneratorJarSource();
        }
    }
    
    private void initializeGeneratorJar() {
    	IPath path = getGeneratorPath();
        if (path == null) {
            JavaCore.removeClasspathVariable(MBG_HOME, null);
            return;
        }
        
        try {
            JavaCore.setClasspathVariable(MBG_HOME, path, null);
        } catch (JavaModelException e) {
            JavaCore.removeClasspathVariable(MBG_HOME, null);
        }
    }

    private void initializeGeneratorJarSource() {
    	IPath path = getGeneratorSourcePath();
        if (path == null) {
            JavaCore.removeClasspathVariable(MBG_SRC_HOME, null);
            return;
        }

        try {
            JavaCore.setClasspathVariable(MBG_SRC_HOME, path, null);
        } catch (JavaModelException e1) {
            JavaCore.removeClasspathVariable(MBG_SRC_HOME, null);
        }
    }
    
    public static IPath getGeneratorPath() {
        Bundle bundle= Platform.getBundle("org.mybatis.generator.core"); //$NON-NLS-1$
        if (bundle == null) {
            return null;
        }
        
        try {
            URL devPath = bundle.getEntry("bin/");
            File fullPath;
            if (devPath != null) {
                devPath = FileLocator.toFileURL(devPath);
                fullPath = new File(devPath.toURI());
            } else {
                fullPath = FileLocator.getBundleFile(bundle);
            }
            
            return new Path(fullPath.getAbsolutePath());
        } catch (IOException e) {
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    public static IPath getGeneratorSourcePath() {
        Bundle bundle= Platform.getBundle("org.mybatis.generator.core.source"); //$NON-NLS-1$
        if (bundle == null) {
            return null;
        }

        try {
            File fullPath = FileLocator.getBundleFile(bundle);
            return new Path(fullPath.getAbsolutePath());
        } catch (IOException e) {
            return null;
        }
    }
}
