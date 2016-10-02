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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * A lot of this code was originally copied from org.eclipse.jdt.core.tests.util and
 * other classes in the eclipse test projects.
 * 
 * Method in this class should only be used by the ShellCallbackTests because they
 * require a project in the workspace.
 * 
 */
public class WorkspaceUtilities {
    private static int DELETE_MAX_WAIT = 10000;
    private static int DELETE_MAX_TIME = 0;

    private static IProject getProject(String project) {
        return getWorkspaceRoot().getProject(project);
    }

    private static IProject createProject(String projectName) throws CoreException {
        final IProject project = getWorkspaceRoot().getProject(projectName);
        IWorkspaceRunnable create = new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {
                project.create(null);
                project.open(null);
            }
        };
        getWorkspace().run(create, null);
        return project;
    }

    static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    private static IWorkspaceRoot getWorkspaceRoot() {
        return getWorkspace().getRoot();
    }

    private static void addJavaNature(String projectName) throws CoreException {
        IProject project = getWorkspaceRoot().getProject(projectName);
        IProjectDescription description = project.getDescription();
        description.setNatureIds(new String[] { JavaCore.NATURE_ID });
        project.setDescription(description, null);
    }

    static IJavaProject createJavaProject(final String projectName, final String[] sourceFolders,
            final String projectOutput, final String compliance) throws CoreException {

        final IJavaProject[] result = new IJavaProject[1];
        IWorkspaceRunnable create = new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {
                // create project
                createProject(projectName);

                // set java nature
                addJavaNature(projectName);

                // create classpath entries
                IProject project = getWorkspaceRoot().getProject(projectName);
                IPath projectPath = project.getFullPath();
                int sourceLength = sourceFolders == null ? 0 : sourceFolders.length;
                IClasspathEntry[] entries = new IClasspathEntry[sourceLength];
                for (int i = 0; i < sourceLength; i++) {
                    IPath sourcePath = new Path(sourceFolders[i]);
                    int segmentCount = sourcePath.segmentCount();
                    if (segmentCount > 0) {
                        // create folder and its parents
                        IContainer container = project;
                        for (int j = 0; j < segmentCount; j++) {
                            IFolder folder = container.getFolder(new Path(sourcePath.segment(j)));
                            if (!folder.exists()) {
                                folder.create(true, true, null);
                            }
                            container = folder;
                        }
                    }
                    // create source entry
                    entries[i] = JavaCore.newSourceEntry(projectPath.append(sourcePath));
                }

                // create project's output folder
                IPath outputPath = new Path(projectOutput);
                if (outputPath.segmentCount() > 0) {
                    IFolder output = project.getFolder(outputPath);
                    if (!output.exists()) {
                        output.create(true, true, monitor);
                    }
                }

                // set classpath and output location
                IJavaProject javaProject = JavaCore.create(project);
                javaProject.setRawClasspath(entries, projectPath.append(outputPath), monitor);

                // set compliance level options
                if ("1.5".equals(compliance)) {
                    Map<String, String> options = new HashMap<String, String>();
                    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
                    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
                    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
                    javaProject.setOptions(options);
                } else if ("1.6".equals(compliance)) {
                    Map<String, String> options = new HashMap<String, String>();
                    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
                    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
                    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
                    javaProject.setOptions(options);
                } else if ("1.7".equals(compliance)) {
                    Map<String, String> options = new HashMap<String, String>();
                    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
                    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
                    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
                    javaProject.setOptions(options);
                } else if ("1.8".equals(compliance)) {
                    Map<String, String> options = new HashMap<String, String>();
                    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
                    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
                    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
                    javaProject.setOptions(options);
                }

                result[0] = javaProject;
            }
        };
        getWorkspace().run(create, null);
        return result[0];
    }

    static void deleteProject(String projectName) throws CoreException {
        IProject project = getProject(projectName);
        if (project.exists() && !project.isOpen()) { // force opening so that
                                                     // project can be deleted
                                                     // without logging (see bug
                                                     // 23629)
            project.open(null);
        }
        deleteResource(project);
    }

    private static void deleteResource(IResource resource) throws CoreException {
        int retryCount = 0; // wait 1 minute at most
        IStatus status = null;
        while (++retryCount <= 6) {
            status = WorkspaceUtilities.delete(resource);
            if (status.isOK()) {
                return;
            }
            System.gc();
        }
        throw new CoreException(status);
    }

    private static boolean isResourceDeleted(IResource resource) {
        return !resource.isAccessible() && getParentChildResource(resource) == null;
    }

    private static IResource getParentChildResource(IResource resource) {
        IContainer parent = resource.getParent();
        if (parent == null || !parent.exists()) {
            return null;
        }
        
        try {
            IResource[] members = parent.members();
            int length = members == null ? 0 : members.length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    if (members[i] == resource) {
                        return members[i];
                    } else if (members[i].equals(resource)) {
                        return members[i];
                    } else if (members[i].getFullPath().equals(resource.getFullPath())) {
                        return members[i];
                    }
                }
            }
        } catch (CoreException ce) {
            // skip
        }
        return null;
    }

    static IStatus delete(IResource resource) {
        IStatus status = null;
        try {
            resource.delete(true, null);
            if (isResourceDeleted(resource)) {
                return Status.OK_STATUS;
            }
        } catch (CoreException e) {
            status = e.getStatus();
        }
        boolean deleted = waitUntilResourceDeleted(resource);
        if (deleted) {
            return Status.OK_STATUS;
        }
        if (status != null) {
            return status;
        }
        return new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, "Cannot delete resource " + resource);
    }

    private static boolean waitUntilResourceDeleted(IResource resource) {
        IPath location = resource.getLocation();
        if (location == null) {
            return false;
        }
        File file = location.toFile();
        int count = 0;
        int delay = 10; // ms
        int maxRetry = DELETE_MAX_WAIT / delay;
        int time = 0;
        while (count < maxRetry) {
            try {
                count++;
                Thread.sleep(delay);
                time += delay;
                if (time > DELETE_MAX_TIME)
                    DELETE_MAX_TIME = time;
                if (resource.isAccessible()) {
                    try {
                        resource.delete(true, null);
                        if (isResourceDeleted(resource) && isFileDeleted(file)) {
                            // SUCCESS
                            return true;
                        }
                    } catch (CoreException e) {
                        // skip
                    }
                }
                if (isResourceDeleted(resource) && isFileDeleted(file)) {
                    // SUCCESS
                    return true;
                }
                // Increment waiting delay exponentially
                if (count >= 10 && delay <= 100) {
                    count = 1;
                    delay *= 10;
                    maxRetry = DELETE_MAX_WAIT / delay;
                    if ((DELETE_MAX_WAIT % delay) != 0) {
                        maxRetry++;
                    }
                }
            } catch (InterruptedException ie) {
                break; // end loop
            }
        }
        return false;
    }

    private static boolean isFileDeleted(File file) {
        return !file.exists() && getParentChildFile(file) == null;
    }

    private static File getParentChildFile(File file) {
        File parent = file.getParentFile();
        if (parent == null || !parent.exists()) {
            return null;
        }
        File[] files = parent.listFiles();
        int length = files == null ? 0 : files.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (files[i] == file) {
                    return files[i];
                } else if (files[i].equals(file)) {
                    return files[i];
                } else if (files[i].getPath().equals(file.getPath())) {
                    return files[i];
                }
            }
        }
        return null;
    }
}
