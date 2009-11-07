/*
 *  Copyright 2009 The Apache Software Foundation
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

package org.apache.ibatis.ibator.maven;

import java.io.File;
import java.util.StringTokenizer;

import org.apache.ibatis.ibator.exception.ShellException;
import org.apache.ibatis.ibator.internal.DefaultShellCallback;
import org.apache.ibatis.ibator.internal.util.messages.Messages;

/**
 * @author Jeff Butler
 */
public class MavenShellCallback extends DefaultShellCallback {
    private IbatorMojo ibatorMojo;

    /**
     * @param overwrite
     */
    public MavenShellCallback(IbatorMojo ibatorMojo, boolean overwrite) {
        super(overwrite);
        this.ibatorMojo = ibatorMojo;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage)
            throws ShellException {
        // targetProject is the output directory from the ibator Mojo.
        // it will be created if necessary
        //
        // targetPackage is interpreted as a sub directory, but in package
        // format (with dots instead of slashes).  The sub directory will be created
        // if it does not already exist
        
        File project = ibatorMojo.getOutputDirectory();
        if (!project.exists()) {
            project.mkdirs();
        }
        
        if (!project.isDirectory()) {
            throw new ShellException(Messages.getString("Warning.9", //$NON-NLS-1$
                    project.getAbsolutePath()));
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
}
