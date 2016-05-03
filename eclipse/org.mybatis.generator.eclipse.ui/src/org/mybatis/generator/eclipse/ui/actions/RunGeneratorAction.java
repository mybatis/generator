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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mybatis.generator.eclipse.ui.Activator;
import org.mybatis.generator.eclipse.ui.content.ConfigurationFileAdapter;

/**
 * 
 * @author Jeff Butler
 *
 */
public class RunGeneratorAction implements IObjectActionDelegate{

    private IFile selectedFile;
    
    public RunGeneratorAction() {
    }

    public void run(IAction action) {
        Shell shell = new Shell();

        try {
            List<String> warnings = new ArrayList<String>();
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

            IRunnableWithProgress thread = new GeneratorRunner(warnings);

            dialog.run(true, true, thread);

            if (warnings.size() > 0) {
                MultiStatus ms = new MultiStatus(Activator.PLUGIN_ID,
                        Status.WARNING, "Generation Warnings Occured", null);

                Iterator<String> iter = warnings.iterator();
                while (iter.hasNext()) {
                    Status status = new Status(Status.WARNING, Activator
                            .PLUGIN_ID, Status.WARNING, iter.next(), null);
                    ms.add(status);
                }

                ErrorDialog.openError(shell, "MyBatis Generator",
                        "Run Complete With Warnings", ms, Status.WARNING);
            }
        } catch (Exception e) {
            handleException(e, shell);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        StructuredSelection ss = (StructuredSelection) selection;
        ConfigurationFileAdapter adapter = (ConfigurationFileAdapter) ss.getFirstElement();
        if (adapter != null) {
            selectedFile = adapter.getBaseFile();
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    private void handleException(Exception exception, Shell shell) {
        IStatus status;

        Throwable exceptionToHandle;
        if (exception instanceof InvocationTargetException) {
            exceptionToHandle = ((InvocationTargetException) exception)
                    .getCause();
        } else {
            exceptionToHandle = exception;
        }

        if (exceptionToHandle instanceof InterruptedException) {
            status = new Status(IStatus.CANCEL, Activator.PLUGIN_ID,
                    IStatus.CANCEL, "Cancelled by User", exceptionToHandle);
        } else if (exceptionToHandle instanceof CoreException) {
            status = ((CoreException) exceptionToHandle).getStatus();
        } else {
            String message = "Unexpected error while running MyBatis Generator.";

            status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
                    IStatus.ERROR, message, exceptionToHandle);

            Activator.getDefault().getLog().log(status);
        }

        ErrorDialog.openError(shell, "MyBatis Generator", "Generation Failed",
                status, IStatus.ERROR | IStatus.CANCEL);
    }
    
    private class GeneratorRunner implements IRunnableWithProgress {
        private List<String> warnings;

        public GeneratorRunner(List<String> warnings) {
            this.warnings = warnings;
        }

        public void run(IProgressMonitor monitor)
                throws InvocationTargetException, InterruptedException {
            try {
                RunGeneratorThread thread = new RunGeneratorThread(selectedFile, warnings);

                ResourcesPlugin.getWorkspace().run(thread, monitor);
            } catch (CoreException e) {
                throw new InvocationTargetException(e);
            }
        }
    }
}
