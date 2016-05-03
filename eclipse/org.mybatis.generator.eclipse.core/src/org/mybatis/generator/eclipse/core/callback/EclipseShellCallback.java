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
package org.mybatis.generator.eclipse.core.callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.eclipse.core.merge.JavaFileMerger;
import org.mybatis.generator.exception.ShellException;

/**
 * @author Jeff Butler
 */
public class EclipseShellCallback implements ShellCallback {
    private Map<String, IJavaProject> projects;

    private Map<String, IFolder> folders;

    private Map<String, IPackageFragmentRoot> sourceFolders;

    /**
     * 
     */
    public EclipseShellCallback() {
        super();
        projects = new HashMap<String, IJavaProject>();
        folders = new HashMap<String, IFolder>();
        sourceFolders = new HashMap<String, IPackageFragmentRoot>();
    }

    /*
     * (non-Javadoc)
     * @see org.mybatis.generator.api.ShellCallback#getDirectory(java.lang.String, java.lang.String)
     */
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        if (targetProject.startsWith("/") || targetProject.startsWith("\\")) {
            StringBuffer sb = new StringBuffer();
            sb.append("targetProject ");
            sb.append(targetProject);
            sb.append(" is invalid - it cannot start with / or \\");

            throw new ShellException(sb.toString());
        }
        
        IFolder folder = getFolder(targetProject, targetPackage);

        return folder.getRawLocation().toFile();
    }

    /*
     * (non-Javadoc)
     * @see org.mybatis.generator.api.ShellCallback#refreshProject(java.lang.String)
     */
    public void refreshProject(String project) {
        try {
            IPackageFragmentRoot root = getSourceFolder(project);
            root.getCorrespondingResource().refreshLocal(
                    IResource.DEPTH_INFINITE, null);
        } catch (Exception e) {
            // ignore
            ;
        }
    }

    private IJavaProject getJavaProject(String javaProjectName)
            throws ShellException {
        IJavaProject javaProject = projects.get(javaProjectName);
        if (javaProject == null) {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IProject project = root.getProject(javaProjectName);

            if (project.exists()) {
                boolean isJavaProject;
                try {
                    isJavaProject = project.hasNature(JavaCore.NATURE_ID);
                } catch (CoreException e) {
                    throw new ShellException(e.getStatus().getMessage(), e);
                }
                if (isJavaProject) {
                    javaProject = JavaCore.create(project);
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Project ");
                    sb.append(javaProjectName);
                    sb.append(" is not a Java project");

                    throw new ShellException(sb.toString());
                }
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("Project ");
                sb.append(javaProjectName);
                sb.append(" does not exist");

                throw new ShellException(sb.toString());
            }

            projects.put(javaProjectName, javaProject);
        }

        return javaProject;
    }

    private IFolder getFolder(String targetProject, String targetPackage)
            throws ShellException {
        String key = targetProject + targetPackage;
        IFolder folder = folders.get(key);
        if (folder == null) {
            IPackageFragmentRoot root = getSourceFolder(targetProject);
            IPackageFragment packageFragment = getPackage(root, targetPackage);

            try {
                folder = (IFolder) packageFragment.getCorrespondingResource();

                folders.put(key, folder);
            } catch (CoreException e) {
                throw new ShellException(e.getStatus().getMessage(), e);
            }
        }

        return folder;
    }

    /**
     * This method returns the first modifiable package fragment root in the
     * java project
     * 
     * @param javaProject
     * @return
     */
    private IPackageFragmentRoot getFirstSourceFolder(IJavaProject javaProject)
            throws ShellException {

        // find the first non-JAR package fragment root
        IPackageFragmentRoot[] roots;
        try {
            roots = javaProject.getPackageFragmentRoots();
        } catch (CoreException e) {
            throw new ShellException(e.getStatus().getMessage(), e);
        }

        IPackageFragmentRoot srcFolder = null;
        for (int i = 0; i < roots.length; i++) {
            if (roots[i].isArchive() || roots[i].isReadOnly()
                    || roots[i].isExternal()) {
                continue;
            } else {
                srcFolder = roots[i];
                break;
            }
        }

        if (srcFolder == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("Cannot find source folder for project ");
            sb.append(javaProject.getElementName());

            throw new ShellException(sb.toString());
        }

        return srcFolder;
    }

    private IPackageFragmentRoot getSpecificSourceFolder(
            IJavaProject javaProject, String targetProject)
            throws ShellException {

        try {
            Path path = new Path("/" + targetProject);
            IPackageFragmentRoot pfr = javaProject
                    .findPackageFragmentRoot(path);
            if (pfr == null) {
                StringBuffer sb = new StringBuffer();
                sb.append("Cannot find source folder ");
                sb.append(targetProject);

                throw new ShellException(sb.toString());
            }

            return pfr;
        } catch (CoreException e) {
            throw new ShellException(e.getStatus().getMessage(), e);
        }
    }

    private IPackageFragment getPackage(IPackageFragmentRoot srcFolder,
            String packageName) throws ShellException {

        IPackageFragment fragment = srcFolder.getPackageFragment(packageName);

        try {
            if (!fragment.exists()) {
                fragment = srcFolder.createPackageFragment(packageName, true,
                        null);
            }

            fragment.getCorrespondingResource().refreshLocal(
                    IResource.DEPTH_ONE, null);
        } catch (CoreException e) {
            throw new ShellException(e.getStatus().getMessage(), e);
        }

        return fragment;
    }

    /*
     * (non-Javadoc)
     * @see org.mybatis.generator.api.ShellCallback#isMergeSupported()
     */
    public boolean isMergeSupported() {
        return true;
    }

    private IPackageFragmentRoot getSourceFolder(String targetProject)
            throws ShellException {
        IPackageFragmentRoot answer = sourceFolders.get(targetProject);
        if (answer == null) {
            // first parse the targetProject into project and source folder
            // values
            int index = targetProject.indexOf('/');
            if (index == -1) {
                index = targetProject.indexOf('\\');
            }

            if (index == -1) {
                // no source folder specified
                IJavaProject javaProject = getJavaProject(targetProject);
                answer = getFirstSourceFolder(javaProject);
            } else {
                IJavaProject javaProject = getJavaProject(targetProject
                        .substring(0, index));
                answer = getSpecificSourceFolder(javaProject, targetProject);
            }

            sourceFolders.put(targetProject, answer);
        }

        return answer;
    }

    public boolean isOverwriteEnabled() {
        return false;
    }

    public String mergeJavaFile(String newFileSource,
            String existingFileFullPath, String[] javadocTags, String fileEncoding)
            throws ShellException {
        JavaFileMerger merger = new JavaFileMerger(newFileSource, existingFileFullPath, javadocTags, fileEncoding);
        return merger.getMergedSource();
    }
}
