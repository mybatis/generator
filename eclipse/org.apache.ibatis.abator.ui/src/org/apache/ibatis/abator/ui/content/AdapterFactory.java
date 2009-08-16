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
package org.apache.ibatis.abator.ui.content;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * This class provides a factor for different adapters used in the plugin.
 * Namely, this class decides if any particular file is an abator configuration
 * file or not.
 * 
 * @author Jeff Butler
 */
public class AdapterFactory implements IAdapterFactory {

    public AdapterFactory() {
        super();
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof IFile
                && adapterType == AbatorConfigurationFileAdapter.class) {
            if (isAbatorConfigurationFile((IFile) adaptableObject)) {
                return new AbatorConfigurationFileAdapter(
                        (IFile) adaptableObject);
            }
        }

        return null;
    }

    public Class[] getAdapterList() {
        return new Class[] { AbatorConfigurationFileAdapter.class };
    }

    private boolean isAbatorConfigurationFile(IFile file) {
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
        
        AbatorConfigVerifyer verifyer = new AbatorConfigVerifyer(is);
        
        boolean rc = verifyer.isAbatorConfigFile();
        
        try {
            is.close();
        } catch (IOException e) {
            // ignore
            ;
        }
        
        return rc;
    }
}
