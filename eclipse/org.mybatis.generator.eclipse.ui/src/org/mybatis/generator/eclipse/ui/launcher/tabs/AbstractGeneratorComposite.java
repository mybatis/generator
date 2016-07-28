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
package org.mybatis.generator.eclipse.ui.launcher.tabs;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.mybatis.generator.eclipse.ui.Activator;
import org.mybatis.generator.eclipse.ui.Messages;
import org.mybatis.generator.eclipse.ui.launcher.GeneratorLaunchConstants;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * This abstract class includes support and UI for selecting a file from the
 * workspace or file system.
 * 
 * This class is heavily influenced by the Eclipse XSL launcher UI and I am
 * grateful for the inspiration.
 * 
 * @author Jeff Butler
 *
 */
public abstract class AbstractGeneratorComposite extends Composite implements GeneratorLaunchConstants {

    protected Text txtFileName;
    private Button btnBrowseWorkplace;
    private Button btnBrowseFileSystem;

    private ISelectionStatusValidator selectionStatusVerifier = new ISelectionStatusValidator() {
        @Override
        public IStatus validate(Object[] selection) {
            if (selection.length == 0) {
                return new Status(Status.ERROR, Activator.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
            }
            for (int i = 0; i < selection.length; i++) {
                if (!(selection[i] instanceof IFile)) {
                    return new Status(Status.ERROR, Activator.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
                }
            }
            return new Status(Status.OK, Activator.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
        }
    };

    public AbstractGeneratorComposite(Composite parent, int style) {
        super(parent, style);
    }

    protected Composite createFileNameGroup(Composite parent, String groupText) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(groupText);

        GridLayout groupLayout = new GridLayout(2, false);
        group.setLayout(groupLayout);
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        group.setFont(parent.getFont());

        createFileNameTextbox(group);
        createFileNameBrowseButtons(group);

        return group;
    }

    private void createFileNameTextbox(Composite parent) {
        Composite fileNameComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        layout.marginLeft = 0;
        layout.marginHeight = 0;
        fileNameComposite.setLayout(layout);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        fileNameComposite.setLayoutData(gd);

        txtFileName = new Text(fileNameComposite, SWT.SINGLE | SWT.BORDER);
        txtFileName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                updateLaunchConfigurationDialog();
            }
        });
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        txtFileName.setLayoutData(gd);
        txtFileName.setFont(parent.getFont());
    }

    private void createFileNameBrowseButtons(Composite parent) {
        new Label(parent, SWT.NONE);

        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        buttonComposite.setLayout(layout);
        GridData gd = new GridData(SWT.END, SWT.CENTER, false, false);
        buttonComposite.setLayoutData(gd);
        buttonComposite.setFont(parent.getFont());

        btnBrowseWorkplace = new Button(buttonComposite, SWT.NONE);
        btnBrowseWorkplace.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IPath chosenFile = chooseFileFromWorkspace();
                if (chosenFile != null) {
                    txtFileName.setText("${workspace_loc:" + chosenFile.toString() + "}"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        });
        btnBrowseWorkplace.setText(Messages.FILE_PICKER_BROWSE_WORKSPACE);

        btnBrowseFileSystem = new Button(buttonComposite, SWT.NONE);
        btnBrowseFileSystem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                chooseFileFromFileSystem();
            }
        });
        btnBrowseFileSystem.setText(Messages.FILE_PICKER_BROWSE_FILE_SYSTEM);
    }

    protected IPath chooseFileFromWorkspace() {
        ElementTreeSelectionDialog esd = new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(),
                new WorkbenchContentProvider());
        esd.setTitle(getDialogTitle());
        esd.setMessage(getDialogMessage());
        esd.setAllowMultiple(false);
        esd.setValidator(selectionStatusVerifier);
        esd.addFilter(getViewerFilter());
        esd.setInput(ResourcesPlugin.getWorkspace().getRoot());
        esd.setInitialSelection(getWorkspaceResource());
        int rc = esd.open();
        if (rc == 0) {
            Object[] elements = esd.getResult();
            if (elements.length > 0) {
                return ((IResource) elements[0]).getFullPath();
            }
        }

        return null;
    }

    private IResource getWorkspaceResource() {
        String path = txtFileName.getText();
        if (path.length() > 0) {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            try {
                if (path.startsWith("${workspace_loc:")) { //$NON-NLS-1$
                    IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
                    path = manager.performStringSubstitution(path, false);
                }
                File f = new File(path);
                IFile[] files;
                files = root.findFilesForLocationURI(f.toURI());
                if (files.length <= 0) {
                    return null;
                }
                return files[0];
            } catch (CoreException e) {
                return null;
            }
        }
        return null;
    }

    protected void chooseFileFromFileSystem() {
        String currentWorkingDir = txtFileName.getText();
        String selected = null;
        FileDialog dialog = new FileDialog(getShell());
        if (StringUtility.stringHasValue(currentWorkingDir)) {
            File path = new File(currentWorkingDir);
            if (path.exists()) {
                dialog.setFilterPath(currentWorkingDir);
            }
        }
        dialog.setText(getDialogTitle());
        dialog.setFilterExtensions(getAcceptableFileExtension());
        selected = dialog.open();
        if (selected != null) {
            txtFileName.setText(selected);
        }
    }

    protected abstract void updateLaunchConfigurationDialog();
    protected abstract String getDialogTitle();
    protected abstract String getDialogMessage();
    protected abstract String[] getAcceptableFileExtension();
    protected abstract ViewerFilter getViewerFilter();
}
