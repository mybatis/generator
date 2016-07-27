/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.eclipse.ui.buildpath;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class MyBatisContainer implements IClasspathContainer {

    private IPath _path;

    public MyBatisContainer(IPath path, IJavaProject project) {
        super();
        _path = path;
    }

    public IClasspathEntry[] getClasspathEntries() {
        IClasspathEntry[] answer = new IClasspathEntry[1];

        // TODO - find source path
        answer[0] = getGeneratorEntry();

        return answer;
    }

    public String getDescription() {
        return "MyBatis Generator";
    }

    public int getKind() {
        return IClasspathContainer.K_APPLICATION;
    }

    public IPath getPath() {
        return _path;
    }

    private IClasspathEntry getGeneratorEntry() {
        Bundle bundle = Platform.getBundle("org.mybatis.generator.core"); //$NON-NLS-1$
        if (bundle == null) {
            return null;
        }
        
        try {
            URL devPath = bundle.getEntry("bin/");
            File fullPath;
            IClasspathEntry answer;
            
            fullPath = FileLocator.getBundleFile(bundle);
            
            if (devPath != null) {
                // we are in development mode
                devPath = FileLocator.toFileURL(devPath);
                fullPath = new File(devPath.toURI());
                
                answer = JavaCore.newLibraryEntry(new Path(fullPath.getAbsolutePath()), null, new Path("/"));
            } else {
                answer = JavaCore.newLibraryEntry(new Path(FileLocator.getBundleFile(bundle).getAbsolutePath()), null, new Path("/"));
                fullPath = FileLocator.getBundleFile(bundle);
            }

            return answer;
        } catch (IOException e) {
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
