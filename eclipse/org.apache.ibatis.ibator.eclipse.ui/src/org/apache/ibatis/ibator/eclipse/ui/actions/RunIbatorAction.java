package org.apache.ibatis.ibator.eclipse.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.ibator.eclipse.ui.IbatorUIPlugin;
import org.apache.ibatis.ibator.eclipse.ui.content.IbatorConfigurationFileAdapter;
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

public class RunIbatorAction implements IObjectActionDelegate{

    private IFile selectedFile;
    
    public RunIbatorAction() {
    }

    public void run(IAction action) {
        Shell shell = new Shell();

        try {
            List<String> warnings = new ArrayList<String>();
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

            IRunnableWithProgress thread = new IbatorRunner(warnings);

            dialog.run(true, true, thread);

            if (warnings.size() > 0) {
                MultiStatus ms = new MultiStatus(IbatorUIPlugin.PLUGIN_ID,
                        Status.WARNING, "Generation Warnings Occured", null);

                Iterator<String> iter = warnings.iterator();
                while (iter.hasNext()) {
                    Status status = new Status(Status.WARNING, IbatorUIPlugin
                            .PLUGIN_ID, Status.WARNING, iter.next(), null);
                    ms.add(status);
                }

                ErrorDialog.openError(shell, "Ibator for iBATIS",
                        "Run Complete With Warninigs", ms, Status.WARNING);
            }
        } catch (Exception e) {
            handleException(e, shell);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        StructuredSelection ss = (StructuredSelection) selection;
        IbatorConfigurationFileAdapter adapter = (IbatorConfigurationFileAdapter) ss.getFirstElement();
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
            status = new Status(IStatus.CANCEL, IbatorUIPlugin.PLUGIN_ID,
                    IStatus.CANCEL, "Cancelled by User", exceptionToHandle);
        } else if (exceptionToHandle instanceof CoreException) {
            status = ((CoreException) exceptionToHandle).getStatus();
        } else {
            String message = "Unexpected error while running Ibator.";

            status = new Status(IStatus.ERROR, IbatorUIPlugin.PLUGIN_ID,
                    IStatus.ERROR, message, exceptionToHandle);

            IbatorUIPlugin.getDefault().getLog().log(status);
        }

        ErrorDialog.openError(shell, "Ibator for iBATIS", "Generation Failed",
                status, IStatus.ERROR | IStatus.CANCEL);
    }
    
    private class IbatorRunner implements IRunnableWithProgress {
        private List<String> warnings;

        public IbatorRunner(List<String> warnings) {
            this.warnings = warnings;
        }

        public void run(IProgressMonitor monitor)
                throws InvocationTargetException, InterruptedException {
            try {
                RunIbatorThread thread = new RunIbatorThread(selectedFile
                        .getLocation().toFile(), warnings);

                ResourcesPlugin.getWorkspace().run(thread, monitor);
            } catch (CoreException e) {
                throw new InvocationTargetException(e);
            }
        }
    }
}
