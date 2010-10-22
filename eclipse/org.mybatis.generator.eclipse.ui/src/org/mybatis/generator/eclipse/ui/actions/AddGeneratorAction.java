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

package org.mybatis.generator.eclipse.ui.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mybatis.generator.eclipse.ui.MyBatisGeneratorClasspathVariableInitializer;
import org.mybatis.generator.eclipse.ui.content.JavaProjectAdapter;

/**
 * @author Jeff Butler
 *
 */
public class AddGeneratorAction implements IObjectActionDelegate {
    
    private IJavaProject iJavaProject;

    /* (non-Javadoc)
     * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        IPath jarPath = MyBatisGeneratorClasspathVariableInitializer.getGeneratorPath();
        IPath srcPath = MyBatisGeneratorClasspathVariableInitializer.getGeneratorSourcePath();
        
        IClasspathEntry newEntry = JavaCore.newLibraryEntry(jarPath, srcPath, null);
        
        try {
            IClasspathEntry[] oldClasspath = iJavaProject.getRawClasspath();
            IClasspathEntry[] newClasspath = new IClasspathEntry[oldClasspath.length + 1];
            System.arraycopy(oldClasspath, 0, newClasspath, 0, oldClasspath.length);
            newClasspath[oldClasspath.length] = newEntry;
            iJavaProject.setRawClasspath(newClasspath, null);
        } catch (Exception e) {
            // ignore
            ;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        StructuredSelection ss = (StructuredSelection) selection;
        JavaProjectAdapter project = (JavaProjectAdapter) ss.getFirstElement();
        if (project != null) {
            iJavaProject = project.getJavaProject();
        }
    }
}
