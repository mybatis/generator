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
package org.mybatis.generator.eclipse.core.tests.callback;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mybatis.generator.eclipse.core.tests.callback.WorkspaceUtilities.createJavaProject;
import static org.mybatis.generator.eclipse.core.tests.callback.WorkspaceUtilities.deleteProject;
import static org.mybatis.generator.eclipse.core.tests.callback.WorkspaceUtilities.getWorkspace;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.generator.eclipse.core.callback.EclipseShellCallback;
import org.mybatis.generator.exception.ShellException;

public class EclipseShellCallbackTest {

    private static IJavaProject javaProject;

    @BeforeClass
    public static void setup() throws CoreException {
        javaProject = createJavaProject("P", new String[] { "src/main/java", "generatedsrc" }, "bin", "1.5");
    }
    
    @AfterClass
    public static void tearDown() throws CoreException {
        deleteProject("P");
    }

    @Test
    public void testCalculatingDirectoryOnDefaultSourceFolder() throws ShellException {
        EclipseShellCallback callback = new EclipseShellCallback();
        File directory = callback.getDirectory(javaProject.getElementName(), "org.mybatis.test");
        assertThat(directory, is(notNullValue()));
        assertThat(directory.exists(), is(true));
        
        IPath actualPath = new Path(directory.getAbsolutePath());
        IPath workspacePath = getWorkspace().getRoot().getLocation();
        IPath expectedPath = workspacePath.append(javaProject.getElementName())
                .append("src")
                .append("main")
                .append("java")
                .append("org")
                .append("mybatis")
                .append("test");
        assertThat(expectedPath, is(equalTo(actualPath)));
    }

    @Test
    public void testCalculatingDirectoryOnSpecificSourceFolder() throws ShellException {
        EclipseShellCallback callback = new EclipseShellCallback();
        File directory = callback.getDirectory(javaProject.getElementName() + "/generatedsrc", "org.mybatis.test");
        assertThat(directory, is(notNullValue()));
        assertThat(directory.exists(), is(true));
        
        IPath actualPath = new Path(directory.getAbsolutePath());
        IPath workspacePath = getWorkspace().getRoot().getLocation();
        IPath expectedPath = workspacePath.append(javaProject.getElementName())
                .append("generatedsrc")
                .append("org")
                .append("mybatis")
                .append("test");
        assertThat(expectedPath, is(equalTo(actualPath)));
    }

    @Test(expected=ShellException.class)
    public void testCalculatingDirectoryOnNonExistingSourceFolder() throws ShellException {
        EclipseShellCallback callback = new EclipseShellCallback();
        callback.getDirectory(javaProject.getElementName() + "/othersrc", "org.mybatis.test");
    }
}
