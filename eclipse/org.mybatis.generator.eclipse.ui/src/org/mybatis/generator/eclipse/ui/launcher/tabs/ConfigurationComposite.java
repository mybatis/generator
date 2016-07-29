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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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
        txtFileName.setText(getTextOrBlank(configuration, ATTR_CONFIGURATION_FILE_NAME));
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(ATTR_CONFIGURATION_FILE_NAME, txtFileName.getText());
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
