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

import static org.mybatis.generator.eclipse.ui.launcher.tabs.LauncherUtils.getTextOrBlank;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.mybatis.generator.eclipse.ui.Messages;
import org.mybatis.generator.eclipse.ui.content.ConfigVerifyer;

/**
 * It is a bit of an extravagance to have this in a separate class from the tab,
 * but doing so allows us to use the Eclipse SWT designer.
 * 
 * @author Jeff Butler
 *
 */
public class ConfigurationComposite extends AbstractGeneratorComposite {
    private ConfigurationTab configurationTab;
    private Map<LoggingButtonData, Button> loggingButtonMap = new HashMap<>();

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public ConfigurationComposite(Composite parent, int style, final ConfigurationTab configurationTab) {
        super(parent, style);
        this.configurationTab = configurationTab;
        setLayout(new GridLayout(1, false));
        createFileNameGroup(this, Messages.CONFIGURATION_TAB_FILE_GROUP_TITLE);
        createLoggingSelectorGroup();
    }

    private void createLoggingSelectorGroup() {
        Group loggingButtonGroup = new Group(this, SWT.NONE);
        loggingButtonGroup.setText(Messages.CONFIGURATION_TAB_LOGGER_GROUP_TITLE);

        GridLayout groupLayout = new GridLayout(LoggingButtonData.values().length, false);
        loggingButtonGroup.setLayout(groupLayout);
        loggingButtonGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        loggingButtonGroup.setFont(this.getFont());
        
        SelectionListener listener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                if (button.getSelection()) {
                    updateLaunchConfigurationDialog();
                }
            }
        };
        
        for (LoggingButtonData data : LoggingButtonData.values()) {
            Button b = new Button(loggingButtonGroup, SWT.RADIO);
            b.setText(data.displayText());
            b.setData(data);
            b.addSelectionListener(listener);
            loggingButtonMap.put(data, b);
        }
    }

    public boolean isValid() {
        try {
            String fileName = txtFileName.getText();
            String fullPath = VariablesPlugin.getDefault().getStringVariableManager()
                    .performStringSubstitution(fileName);
            File file = new File(fullPath);
            if (file.exists()) {
                ConfigVerifyer cv = new ConfigVerifyer(file);
                if (cv.isConfigurationFile()) {
                    configurationTab.setErrorMessage(null);
                    return true;
                } else {
                    configurationTab.setErrorMessage(Messages.CONFIGURATION_TAB_INVALID_MESSAGE);
                    return false;
                }
            } else {
                configurationTab.setErrorMessage(Messages.FILE_PICKER_FILE_DOESNT_EXIST);
                return false;
            }
        } catch (CoreException e) {
            return false;
        }
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
        selectLoggingButton(getTextOrBlank(configuration, ATTR_LOGGING_IMPLEMENTATION));
        txtFileName.setText(getTextOrBlank(configuration, ATTR_CONFIGURATION_FILE_NAME));
        try {
            javaProjectName = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
        } catch (CoreException e) {
            javaProjectName = null;
        }
    }
    
    private void selectLoggingButton(String setting) {
        deselectAllLoggingButtons();

        if ("".equals(setting)) {
            selectDefaultLoggingButton();
            return;
        }
        
        try {
            LoggingButtonData data = LoggingButtonData.valueOf(setting);
            Button button = loggingButtonMap.get(data);
            button.setSelection(true);
        } catch (Exception e) {
            selectDefaultLoggingButton();
        }
    }
    
    private void deselectAllLoggingButtons() {
        for (Button button : loggingButtonMap.values()) {
            button.setSelection(false);
        }
    }

    private void selectDefaultLoggingButton() {
        for (Button button : loggingButtonMap.values()) {
            LoggingButtonData data = (LoggingButtonData) button.getData();
            if (data.isDefault()) {
                button.setSelection(true);
                break;
            }
        }
    }
    
    private Button getSelectedLoggingButton() {
        Button rc = null;
        for (Button button : loggingButtonMap.values()) {
            if (button.getSelection()) {
                rc = button;
            }
        }
        return rc;
    }
    
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        Button button = getSelectedLoggingButton();
        LoggingButtonData data = (LoggingButtonData) button.getData();
        if (data.isDefault()) {
            configuration.removeAttribute(ATTR_LOGGING_IMPLEMENTATION);
        } else {
            configuration.setAttribute(ATTR_LOGGING_IMPLEMENTATION, data.name());
        }
        
        configuration.setAttribute(ATTR_CONFIGURATION_FILE_NAME, txtFileName.getText());
        configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProjectName);
    }

    @Override
    protected void updateLaunchConfigurationDialog() {
        configurationTab.updateLaunchConfigurationDialog();
    }

    @Override
    protected String getDialogTitle() {
        return Messages.CONFIGURATION_TAB_FILE_PICKER_DIALOG_TITLE;
    }

    @Override
    protected String[] getAcceptableFileExtension() {
        return new String[] { "*.*", "*.xml" }; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    protected String getDialogMessage() {
        return Messages.CONFIGURATION_TAB_FILE_PICKER_DIALOG_MESSAGE;
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
                    IFile file = (IFile) element;
                    ConfigVerifyer cv = new ConfigVerifyer(file);
                    return cv.isConfigurationFile();
                }
                return false;
            }
        };
    }
}
