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
package org.mybatis.generator.eclipse.ui.content;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.mybatis.generator.eclipse.ui.MyBatisGeneratorClasspathVariableInitializer;

/**
 * This class provides a factory for different adapters used in the plugin.
 * Namely, this class decides if any particular file is a MyBatis
 * Generator configuration file or not.
 * 
 * @author Jeff Butler
 */
public class AdapterFactory implements IAdapterFactory {

    public AdapterFactory() {
        super();
    }

    @SuppressWarnings("rawtypes")
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof IFile
                && adapterType == ConfigurationFileAdapter.class) {
            if (isConfigurationFile((IFile) adaptableObject)) {
                return new ConfigurationFileAdapter(
                        (IFile) adaptableObject);
            }
        } else if (adaptableObject instanceof IJavaProject) {
            if (adapterType == JavaProjectAdapter.class) {
                if (!isGeneratorProject((IJavaProject) adaptableObject)) {
                    return new JavaProjectAdapter((IJavaProject) adaptableObject);
                }
            }
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
    public Class[] getAdapterList() {
        return new Class[] { ConfigurationFileAdapter.class,
                JavaProjectAdapter.class};
    }

    private boolean isConfigurationFile(IFile file) {
        String fileName = file.getName();
        if (fileName.length() > 4) {
            String extension = fileName.substring(fileName.length() - 4);
            if (!extension.equalsIgnoreCase(".xml")) { //$NON-NLS-1$
                return false;
            }
        } else {
            return false;
        }

        InputStream is;
        try {
            is = file.getContents();
        } catch (CoreException e) {
            return false;
        }
        
        ConfigVerifyer verifyer = new ConfigVerifyer(is);
        
        boolean rc = verifyer.isConfigFile();
        
        try {
            is.close();
        } catch (IOException e) {
            // ignore
            ;
        }
        
        return rc;
    }

    /**
     * returns true if the project has Ibator on it's classpath
     * 
     * @param project
     * @return
     */
    private boolean isGeneratorProject(IJavaProject project) {
        boolean rc = false;
        
        try {
            IClasspathEntry[] classpath = project.getRawClasspath();
            for (IClasspathEntry iClasspathEntry : classpath) {
                if (iClasspathEntry.getEntryKind() != IClasspathEntry.CPE_VARIABLE) {
                    continue;
                }
                
                IPath path = iClasspathEntry.getPath();
                if (path.segment(0).equals(MyBatisGeneratorClasspathVariableInitializer.MBG_HOME)) {
                    rc = true;
                    break;
                }
            }
        } catch (Exception e) {
            // ignore
            ;
        }
        
        return rc;
    }
}
