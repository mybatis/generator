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

import static org.mybatis.generator.eclipse.ui.launcher.tabs.LauncherUtils.getBooleanOrFalse;
import static org.mybatis.generator.eclipse.ui.launcher.tabs.LauncherUtils.getTextOrBlank;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mybatis.generator.eclipse.ui.Messages;

/**
 * It is a bit of an extravagance to have this in a separate class from the tab,
 * but doing so allows us to use the Eclipse SWT designer.
 * 
 * @author Jeff Butler
 *
 */
public class SqlScriptComposite extends AbstractGeneratorComposite {
    private Text txtJdbcDriver;
    private Text txtJdbcURL;
    private Text txtUserID;
    private Text txtPassword;
    private Button btnSecureStorage;
    private SqlScriptTab sqlScriptTab;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public SqlScriptComposite(Composite parent, int style, SqlScriptTab sqlScriptTab) {
        super(parent, style);
        this.sqlScriptTab = sqlScriptTab;
        setLayout(new GridLayout(1, false));
        
        createInformationSection(this);
        createFileNameGroup(this, Messages.SQL_SCRIPT_TAB_FILE_GROUP_TITLE);
        createConnectionGroup(this);
    }
    
    private void createInformationSection(Composite parent) {
        new Label(parent, SWT.NONE);
        Label info = new Label(parent, SWT.NONE);
        info.setText(Messages.SQL_SCRIPT_TAB_HELP_TEXT);
        new Label(parent, SWT.NONE);
    }
    
    private void createConnectionGroup(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(Messages.SQL_SCRIPT_TAB_JDBC_CONNECTION_GROUP_TITLE);
        GridLayout groupLayout = new GridLayout(2, false);
        group.setLayout(groupLayout);
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        group.setFont(parent.getFont());
        
        Label lblJdbcDriverClass = new Label(group, SWT.NONE);
        lblJdbcDriverClass.setText(Messages.SQL_SCRIPT_TAB_JDBC_DRIVER_LABEL);
        new Label(group, SWT.NONE);
        
        txtJdbcDriver = new Text(group, SWT.BORDER);
        txtJdbcDriver.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                sqlScriptTab.updateLaunchConfigurationDialog();
            }
        });
        GridData gd_txtJdbcDriver = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_txtJdbcDriver.horizontalIndent = 30;
        txtJdbcDriver.setLayoutData(gd_txtJdbcDriver);
        new Label(group, SWT.NONE);
        
        Label lblJdbcUrl = new Label(group, SWT.NONE);
        lblJdbcUrl.setText(Messages.SQL_SCRIPT_TAB_JDBC_URL_LABEL);
        new Label(group, SWT.NONE);
        
        txtJdbcURL = new Text(group, SWT.BORDER);
        txtJdbcURL.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                sqlScriptTab.updateLaunchConfigurationDialog();
            }
        });
        GridData gd_txtJdbcURL = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_txtJdbcURL.horizontalIndent = 30;
        txtJdbcURL.setLayoutData(gd_txtJdbcURL);
        new Label(group, SWT.NONE);
        
        Label lblUserId = new Label(group, SWT.NONE);
        lblUserId.setText(Messages.SQL_SCRIPT_TAB_USERID_LABEL);
        new Label(group, SWT.NONE);
        
        txtUserID = new Text(group, SWT.BORDER);
        txtUserID.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                sqlScriptTab.updateLaunchConfigurationDialog();
            }
        });
        GridData gd_txtUserID = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_txtUserID.horizontalIndent = 30;
        txtUserID.setLayoutData(gd_txtUserID);
        new Label(group, SWT.NONE);
        
        Label lblPassword = new Label(group, SWT.NONE);
        lblPassword.setText(Messages.SQL_SCRIPT_TAB_PASSWORD_LABEL);
        new Label(group, SWT.NONE);
        
        txtPassword = new Text(group, SWT.PASSWORD | SWT.BORDER);
        txtPassword.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                sqlScriptTab.updateLaunchConfigurationDialog();
            }
        });
        GridData gd_txtPassword = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_txtPassword.horizontalIndent = 30;
        txtPassword.setLayoutData(gd_txtPassword);
        new Label(group, SWT.NONE);
        
        btnSecureStorage = new Button(group, SWT.CHECK);
        btnSecureStorage.setText(Messages.SQL_SCRIPT_TAB_SECURE_STORAGE);
        btnSecureStorage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                sqlScriptTab.updateLaunchConfigurationDialog();
            }
        });
        new Label(group, SWT.NONE);
    }
    
    public boolean isValid() {
        String fileName = txtFileName.getText();
        if (!stringHasValue(fileName)) {
            // if no filename is specified, then everything else is ignored anyway
            return true;
        }
        
        try {
            String fullPath = VariablesPlugin.getDefault().getStringVariableManager()
                    .performStringSubstitution(fileName);
            File file = new File(fullPath);
            if (!file.exists()) {
                sqlScriptTab.setErrorMessage(Messages.FILE_PICKER_FILE_DOESNT_EXIST);
                return false;
            }
            
            if (!stringHasValue(txtJdbcDriver.getText())) {
                sqlScriptTab.setErrorMessage(Messages.SQL_SCRIPT_TAB_JDBC_DRIVER_REQUIRED);
                return false;
            }
            
            if (!stringHasValue(txtJdbcURL.getText())) {
                sqlScriptTab.setErrorMessage(Messages.SQL_SCRIPT_TAB_JDBC_URL_REQUIRED);
                return false;
            }
        } catch (CoreException e) {
            return false;
        }
        
        return true;
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
        txtFileName.setText(getTextOrBlank(configuration, ATTR_SQL_SCRIPT_FILE_NAME));
        txtJdbcDriver.setText(getTextOrBlank(configuration, ATTR_SQL_SCRIPT_DRIVER_CLASS));
        txtJdbcURL.setText(getTextOrBlank(configuration, ATTR_SQL_SCRIPT_CONNECTION_URL));
        btnSecureStorage.setSelection(getBooleanOrFalse(configuration, ATTR_SQL_SCRIPT_SECURE_CREDENTIALS));

        txtUserID.setText(LauncherUtils.getUserId(configuration));
        txtPassword.setText(LauncherUtils.getPassword(configuration));
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(ATTR_SQL_SCRIPT_FILE_NAME, txtFileName.getText());
        configuration.setAttribute(ATTR_SQL_SCRIPT_DRIVER_CLASS, txtJdbcDriver.getText());
        configuration.setAttribute(ATTR_SQL_SCRIPT_CONNECTION_URL, txtJdbcURL.getText());
        configuration.setAttribute(ATTR_SQL_SCRIPT_SECURE_CREDENTIALS, btnSecureStorage.getSelection());
        LauncherUtils.setUserId(configuration, txtUserID.getText(), sqlScriptTab.getShell());
        LauncherUtils.setPassword(configuration, txtPassword.getText(), sqlScriptTab.getShell());
    }

    @Override
    protected void updateLaunchConfigurationDialog() {
        sqlScriptTab.updateLaunchConfigurationDialog();
    }

    @Override
    protected String getDialogTitle() {
        return Messages.SQL_SCRIPT_TAB_FILE_PICKER_DIALOG_TITLE;
    }

    @Override
    protected String[] getAcceptableFileExtension() {
        return new String[] {"*.*", "*.sql"}; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    protected String getDialogMessage() {
        return Messages.SQL_SCRIPT_TAB_FILE_PICKER_DIALOG_MESSAGE;
    }

    @Override
    protected ViewerFilter getViewerFilter() {
        return new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IContainer) {
                    return true;
                }

                if (element instanceof IFile) {
                    return true;
                }
                return false;
            }
        };
    }
}
