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
package org.apache.ibatis.ibator.internal;

import java.io.File;
import java.util.StringTokenizer;

import org.apache.ibatis.ibator.api.ShellCallback;
import org.apache.ibatis.ibator.exception.ShellException;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

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
     * @see org.apache.ibatis.ibator.api.ShellCallback#getDirectory(java.lang.String, java.lang.String)
     */
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
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
        
        StringBuilder sb = new StringBuilder();
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
     * @see org.apache.ibatis.ibator.api.ShellCallback#refreshProject(java.lang.String)
     */
    public void refreshProject(String project) {
        // nothing to do in the default shell callback
    }
    
    /* (non-Javadoc)
     * @see org.apache.ibatis.ibator.api.ShellCallback#isMergeSupported()
     */
    public boolean isMergeSupported() {
        return false;
    }

    public boolean isOverwriteEnabled() {
        return overwrite;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.ibatis.ibator.api.ShellCallback#mergeJavaFile(java.lang.String, java.lang.String, java.lang.String[])
     */
    public String mergeJavaFile(String newFileSource,
            String existingFileFullPath, String[] javadocTags)
            throws ShellException {
        throw new UnsupportedOperationException();
    }
}
