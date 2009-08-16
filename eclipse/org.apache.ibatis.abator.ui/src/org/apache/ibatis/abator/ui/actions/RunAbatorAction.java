/*
 * Copyright 2005 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.ibatis.abator.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.abator.ui.content.AbatorConfigurationFileAdapter;
import org.apache.ibatis.abator.ui.plugin.AbatorUIPlugin;
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

public class RunAbatorAction implements IObjectActionDelegate {

    private IFile selectedFile;

    /**
     * Constructor for Action1.
     */
    public RunAbatorAction() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void run(IAction action) {
        Shell shell = new Shell();

        try {
            List warnings = new ArrayList();
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

            IRunnableWithProgress thread = new AbatorRunner(warnings);

            dialog.run(true, true, thread);

            if (warnings.size() > 0) {
                MultiStatus ms = new MultiStatus(AbatorUIPlugin.getPluginId(),
                        Status.WARNING, "Generation Warnings Occured", null);

                Iterator iter = warnings.iterator();
                while (iter.hasNext()) {
                    Status status = new Status(Status.WARNING, AbatorUIPlugin
                            .getPluginId(), Status.WARNING, (String) iter
                            .next(), null);
                    ms.add(status);
                }

                ErrorDialog.openError(shell, "Abator for iBATIS",
                        "Run Complete With Warninigs", ms, Status.WARNING);
            }
        } catch (Exception e) {
            handleException(e, shell);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        StructuredSelection ss = (StructuredSelection) selection;
        AbatorConfigurationFileAdapter adapter = (AbatorConfigurationFileAdapter) ss.getFirstElement();
        if (adapter != null) {
            selectedFile = adapter.getBaseFile();
        }
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
            status = new Status(IStatus.CANCEL, AbatorUIPlugin.getPluginId(),
                    IStatus.CANCEL, "Cancelled by User", exceptionToHandle);
        } else if (exceptionToHandle instanceof CoreException) {
            status = ((CoreException) exceptionToHandle).getStatus();
        } else {
            String message = "Unexpected error while running Abator.";

            status = new Status(IStatus.ERROR, AbatorUIPlugin.getPluginId(),
                    IStatus.ERROR, message, exceptionToHandle);

            AbatorUIPlugin.getDefault().getLog().log(status);
        }

        ErrorDialog.openError(shell, "Abator for iBATIS", "Generation Failed",
                status, IStatus.ERROR | IStatus.CANCEL);
    }

    private class AbatorRunner implements IRunnableWithProgress {
        private List warnings;

        public AbatorRunner(List warnings) {
            this.warnings = warnings;
        }

        public void run(IProgressMonitor monitor)
                throws InvocationTargetException, InterruptedException {
            try {
                RunAbatorThread thread = new RunAbatorThread(selectedFile
                        .getLocation().toFile(), warnings);

                ResourcesPlugin.getWorkspace().run(thread, monitor);
            } catch (CoreException e) {
                throw new InvocationTargetException(e);
            }
        }
    }
}
