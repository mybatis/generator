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
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.exception.ShellException;

import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.Indentation;
import com.github.javaparser.printer.configuration.Indentation.IndentType;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import com.github.javaparser.printer.configuration.imports.EclipseImportOrderingStrategy;

/**
 * @author Jeff Butler
 */
public class EclipseShellCallback implements ShellCallback {
    private Map<String, IJavaProject> projects;

    private Map<String, IFolder> folders;

    private Map<String, IPackageFragmentRoot> sourceFolders;
    
    private final PrinterConfiguration printerConfiguration;

    /**
     * 
     */
    public EclipseShellCallback() {
        printerConfiguration = calculatePrinterConfiguration();
        projects = new HashMap<>();
        folders = new HashMap<>();
        sourceFolders = new HashMap<>();
    }
    
    private PrinterConfiguration calculatePrinterConfiguration() {
        // tab, space, mixed
        String tabCharacter = JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR);
        // size of one tabulation
        String tabSize = JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE);
        int iTabSize = Integer.parseInt(tabSize);
        // only used in "mixed" - number of characters that represent a tab
        String indentationSize = JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_INDENTATION_SIZE);
        int iIndentationSize = Integer.parseInt(indentationSize);

        PrinterConfiguration printerConfiguration = new DefaultPrinterConfiguration();
        printerConfiguration.addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.SORT_IMPORTS_STRATEGY, new EclipseImportOrderingStrategy()));
        printerConfiguration.addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.ORDER_IMPORTS, true));
        
        Indentation indentation;
        if ("tab".equalsIgnoreCase(tabCharacter)) {
            // JavaParser only supports a tab size of four
            var ts = iTabSize / 4;
            ts = ts == 0 ? 1 : ts;
            indentation = new Indentation(IndentType.TABS, ts);
        } else if ("mixed".equalsIgnoreCase(tabCharacter)) {
            indentation = new Indentation(IndentType.SPACES, iIndentationSize);
        } else {
            indentation = new Indentation(IndentType.SPACES, iTabSize);
        }
        printerConfiguration.addOption(new DefaultConfigurationOption(
                DefaultPrinterConfiguration.ConfigOption.INDENTATION, indentation));
        
        return printerConfiguration;
        
    }

    @Override
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
    @Override
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
    @Override
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

    @Override
    public boolean isOverwriteEnabled() {
        return false;
    }

    @Override
    public PrinterConfiguration getMergedJavaFilePrinterConfiguration() {
        return printerConfiguration;
    }
}
