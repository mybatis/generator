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
package org.apache.ibatis.abator.internal;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.ibatis.abator.api.GeneratedJavaFile;
import org.apache.ibatis.abator.api.ShellCallback;
import org.apache.ibatis.abator.exception.ShellException;
import org.apache.ibatis.abator.internal.util.messages.Messages;

/**
 * @author Jeff Butler
 */
public class DefaultShellCallback implements ShellCallback {
    private boolean overwrite;
    
    /**
     *  
     */
    public DefaultShellCallback(boolean overwrite) {
        super();
        this.overwrite = overwrite;
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ShellCallback#getDirectory(java.lang.String, java.lang.String, java.util.List)
     */
    public File getDirectory(String targetProject, String targetPackage,
            List warnings) throws ShellException {
        // targetProject is interpreted as a directory that must exist
        //
        // targetPackage is interpreted as a sub directory, but in package
        // format (with dots instead of slashes).  The sub directory will be created
        // if it does not already exist
        
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new ShellException(Messages.getString("Warning.9", //$NON-NLS-1$
                    targetProject));
        }
        
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }
        
        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(Messages.getString("Warning.10", //$NON-NLS-1$
                        directory.getAbsolutePath()));
            }
        }
        
        return directory;
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ShellCallback#mergeJavaFile(org.apache.ibatis.abator.api.GeneratedJavaFile, java.lang.String, java.util.List)
     */
    public String mergeJavaFile(GeneratedJavaFile newFile, String javadocTag,
            List warnings) throws ShellException {
        if (overwrite) {
            File directory = getDirectory(newFile.getTargetProject(), newFile.getTargetPackage(), warnings);
            File file = new File(directory, newFile.getFileName());
            warnings.add(Messages.getString("Warning.11", //$NON-NLS-1$
                    file.getAbsolutePath()));
            
            return newFile.getFormattedContent();
        } else {
            return null;
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ShellCallback#refreshProject(java.lang.String)
     */
    public void refreshProject(String project) {
        // nothing to do in the default shell callback
    }
    
    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ShellCallback#mergeSupported()
     */
    public boolean mergeSupported() {
        return overwrite;
    }
}
