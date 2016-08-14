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
package org.mybatis.generator.eclipse.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewConfigFileWizardPage1 extends WizardPage {
    private Text locationText;
    private Text fileText;
    private ISelection selection;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param selection
     *            - the current selection
     */
    public NewConfigFileWizardPage1(ISelection selection) {
        super("wizardPage");
        setTitle("MyBatis Generator Configuration File");
        setDescription("This wizard creates a new MyBatis Generator configuration file.");
        this.selection = selection;
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText("&Location:");

        locationText = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        locationText.setLayoutData(gd);
        locationText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        label = new Label(container, SWT.NULL);
        label.setText("&File name:");

        fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gd);
        fileText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        initialize();
        dialogChanged();
        setControl(container);
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */

    private void initialize() {
        if (selection != null && selection.isEmpty() == false
                && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if (ssel.size() > 1)
                return;
            Object obj = ssel.getFirstElement();

            IResource resource = null;

            if (obj instanceof IResource) {
                resource = (IResource) obj;
            } else if (obj instanceof IJavaElement) {
                resource = ((IJavaElement) obj).getResource();
            }

            if (resource != null) {
                IContainer container;
                if (resource instanceof IContainer) {
                    container = (IContainer) resource;
                } else {
                    container = resource.getParent();
                }

                locationText.setText(container.getFullPath().toString());
            }
        }
        fileText.setText("generatorConfig.xml"); //$NON-NLS-1$
    }

    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */

    private void handleBrowse() {
        ContainerSelectionDialog dialog = new ContainerSelectionDialog(
                getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
                "Select new file container");
        if (dialog.open() == ContainerSelectionDialog.OK) {
            Object[] result = dialog.getResult();
            if (result.length == 1) {
                locationText.setText(((Path) result[0]).toOSString());
            }
        }
    }

    /**
     * Ensures that both text fields are set.
     */

    private void dialogChanged() {
        String location = getLocation();
        String fileName = getFileName();

        Path path = new Path(location);

        if (location.length() == 0) {
            updateStatus("File location must be specified");
            return;
        }

        if (fileName.length() == 0) {
            updateStatus("File name must be specified");
            return;
        }

        int segmentCount = path.segmentCount();
        if (segmentCount < 2) {
            // this is a project - check for it's existance
            IProject project = ResourcesPlugin.getWorkspace().getRoot()
                    .getProject(location);
            if (!project.exists()) {
                updateStatus("Project does not exist");
                return;
            }

            IFile file = project.getFile(fileName);
            if (file.exists()) {
                updateStatus("File Already Exists");
                return;
            }
        } else {
            IFolder folder = ResourcesPlugin.getWorkspace().getRoot()
                    .getFolder(path);
            if (!folder.exists()) {
                updateStatus("Location does not exist");
                return;
            }

            IFile file = folder.getFile(fileName);
            if (file.exists()) {
                updateStatus("File Already Exists");
                return;
            }
        }

        int dotLoc = fileName.lastIndexOf('.');
        if (dotLoc != -1) {
            String ext = fileName.substring(dotLoc + 1);
            if (ext.equalsIgnoreCase("xml") == false) {
                updateStatus("File extension must be \"xml\"");
                return;
            }
        } else {
            updateStatus("File extension must be \"xml\"");
            return;
        }

        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getLocation() {
        return locationText.getText();
    }

    public String getFileName() {
        return fileText.getText();
    }
}