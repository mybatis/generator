/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package org.mybatis.generator.eclipse.ui.callbacks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mybatis.generator.eclipse.ui.callbacks.WorkspaceUtilities.createJavaProject;
import static org.mybatis.generator.eclipse.ui.callbacks.WorkspaceUtilities.deleteProject;
import static org.mybatis.generator.eclipse.ui.callbacks.WorkspaceUtilities.getWorkspace;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.exception.ShellException;

public class EclipseShellCallbackTest {

    private static IJavaProject javaProject;

    @BeforeAll
    public static void setup() throws CoreException {
        javaProject = createJavaProject("P", new String[] { "src/main/java", "generatedsrc" }, "bin", "1.5");
    }
    
    @AfterAll
    public static void tearDown() throws CoreException {
        deleteProject("P");
    }

    @Test
    public void testCalculatingDirectoryOnDefaultSourceFolder() throws ShellException {
        EclipseShellCallback callback = new EclipseShellCallback();
        File directory = callback.getDirectory(javaProject.getElementName(), "org.mybatis.test");
        assertThat(directory).isNotNull();
        assertThat(directory.exists()).isTrue();
        
        IPath actualPath = new Path(directory.getAbsolutePath());
        IPath workspacePath = getWorkspace().getRoot().getLocation();
        IPath expectedPath = workspacePath.append(javaProject.getElementName())
                .append("src")
                .append("main")
                .append("java")
                .append("org")
                .append("mybatis")
                .append("test");
        assertThat(expectedPath).isEqualTo(actualPath);
    }

    @Test
    public void testCalculatingDirectoryOnSpecificSourceFolder() throws ShellException {
        EclipseShellCallback callback = new EclipseShellCallback();
        File directory = callback.getDirectory(javaProject.getElementName() + "/generatedsrc", "org.mybatis.test");
        assertThat(directory).isNotNull();
        assertThat(directory.exists()).isTrue();
        
        IPath actualPath = new Path(directory.getAbsolutePath());
        IPath workspacePath = getWorkspace().getRoot().getLocation();
        IPath expectedPath = workspacePath.append(javaProject.getElementName())
                .append("generatedsrc")
                .append("org")
                .append("mybatis")
                .append("test");
        assertThat(expectedPath).isEqualTo(actualPath);
    }

    @Test
    public void testCalculatingDirectoryOnNonExistingSourceFolder() throws ShellException {
        EclipseShellCallback callback = new EclipseShellCallback();
        Assertions.assertThrows(ShellException.class, () -> {
            callback.getDirectory(javaProject.getElementName() + "/othersrc", "org.mybatis.test");
        });
    }
}
