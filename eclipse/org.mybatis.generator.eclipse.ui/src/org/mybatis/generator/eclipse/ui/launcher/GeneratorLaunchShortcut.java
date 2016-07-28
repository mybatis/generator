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
package org.mybatis.generator.eclipse.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.mybatis.generator.eclipse.ui.Activator;
import org.mybatis.generator.eclipse.ui.Messages;

public class GeneratorLaunchShortcut implements ILaunchShortcut {

    @Override
    public void launch(ISelection selection, String mode) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection iss = (IStructuredSelection) selection;
            searchAndLaunch(iss.toArray(), mode);
        }
    }

    @Override
    public void launch(IEditorPart editor, String mode) {
        IEditorInput input = editor.getEditorInput();
        if (input != null) {
            IFile file = input.getAdapter(IFile.class);
            if (file != null) {
                searchAndLaunch(new Object[] { file }, mode);
            }
        }
    }

    private void searchAndLaunch(Object[] objects, String mode) {
        IFile file = null;
        if (objects.length > 0) {
            // just pick the first one (there should only be one)
            IResource resource = (IResource) objects[0];
            if (resource.getType() == IResource.FILE) {
                file = (IFile) resource;
            }
        }

        if (file != null) {
            ILaunchConfiguration config = null;
            try {
                config = findOrCreateLaunchConfiguration(file);
                if (config != null) {
                    DebugUITools.launch(config, mode);
                }
            } catch (CoreException e) {
                Activator.getDefault().getLog().log(e.getStatus());
            }
        }
    }

    private ILaunchConfiguration findOrCreateLaunchConfiguration(IFile file) throws CoreException {
        ILaunchConfigurationType ctype = getLaunchConfigurationType();

        ILaunchConfiguration[] configs = getLaunchManager().getLaunchConfigurations(ctype);
        List<ILaunchConfiguration> candidateConfigs = new ArrayList<ILaunchConfiguration>(configs.length);
        for (ILaunchConfiguration config : configs) {
            String configFile = config.getAttribute(GeneratorLaunchConstants.ATTR_CONFIGURATION_FILE_NAME,
                    (String) null);
            try {
                configFile = VariablesPlugin.getDefault().getStringVariableManager()
                        .performStringSubstitution(configFile);
            } catch (CoreException e) {
                continue;
            }
            Path path = new Path(configFile);
            if (path.equals(file.getLocation())) {
                candidateConfigs.add(config);
            }
        }

        ILaunchConfiguration config;
        if (candidateConfigs.size() > 1) {
            config = chooseConfiguration(candidateConfigs);
        } else if (candidateConfigs.size() == 1) {
            config = candidateConfigs.get(0);
        } else {
            config = createConfiguration(file);
        }

        return config;
    }

    private ILaunchConfiguration chooseConfiguration(List<ILaunchConfiguration> candidates) {
        IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), labelProvider);
        dialog.setElements(candidates.toArray());
        dialog.setTitle(Messages.LAUNCH_CONFIGURATION_SELECTOR_TITLE);
        dialog.setMessage(Messages.LAUNCH_CONFIGURATION_SELECTOR_MESSAGE);
        dialog.setMultipleSelection(false);
        int result = dialog.open();
        labelProvider.dispose();
        if (result == Window.OK) {
            return (ILaunchConfiguration) dialog.getFirstResult();
        }
        return null;
    }

    private Shell getShell() {
        IWorkbenchWindow window = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow();
        if (window != null) {
            return window.getShell();
        }
        
        return null;
    }

    private ILaunchConfiguration createConfiguration(IFile file) throws CoreException {
        ILaunchConfigurationType configType = getLaunchConfigurationType();

        String variableExpression = VariablesPlugin.getDefault().getStringVariableManager()
                .generateVariableExpression("workspace_loc", file.getFullPath().toPortableString()); //$NON-NLS-1$
        
        String namePrefix = String.format("%s-%s", file.getProject().getName(), file.getName()); //$NON-NLS-1$

        ILaunchConfigurationWorkingCopy wc = configType.newInstance(null,
                getLaunchManager().generateLaunchConfigurationName(namePrefix));
        wc.setAttribute(GeneratorLaunchConstants.ATTR_CONFIGURATION_FILE_NAME, variableExpression);
        wc.setMappedResources(new IResource[] { file.getProject() });
        ILaunchConfiguration config = wc.doSave();
        return config;
    }
    
    private ILaunchConfigurationType getLaunchConfigurationType() {
        ILaunchConfigurationType configType = getLaunchManager()
                .getLaunchConfigurationType("org.mybatis.generator.eclipse.launching.LaunchConfigurationType"); //$NON-NLS-1$
        return configType;
    }
    
    private ILaunchManager getLaunchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }
}
