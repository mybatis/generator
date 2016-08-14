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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.mybatis.generator.eclipse.ui.Activator;
import org.mybatis.generator.eclipse.ui.Messages;
import org.mybatis.generator.eclipse.ui.launcher.GeneratorLaunchConstants;

public class LauncherUtils {

    public static String getTextOrBlank(ILaunchConfiguration configuration, String attribute) {
        String text;
        
        try {
            text = configuration.getAttribute(attribute, ""); //$NON-NLS-1$
        } catch (CoreException e) {
            text = ""; //$NON-NLS-1$
        }
        
        return text;
    }
    
    public static String getTextOrBlank(ISecurePreferences node, String attribute) {
        String text;
        
        try {
            text = node.get(attribute, ""); //$NON-NLS-1$
        } catch (StorageException e) {
            text = ""; //$NON-NLS-1$
        }
        
        return text;
    }

    public static boolean getBooleanOrFalse(ILaunchConfiguration configuration, String attribute) {
        boolean answer;
        
        try {
            answer = configuration.getAttribute(attribute, false);
        } catch (CoreException e) {
            answer = false;
        }
        
        return answer;
    }
    
    public static void setPassword(ILaunchConfigurationWorkingCopy configuration, String password, Shell shell) {
        boolean secure = getBooleanOrFalse(configuration, GeneratorLaunchConstants.ATTR_SQL_SCRIPT_SECURE_CREDENTIALS);
        if (secure) {
            ISecurePreferences node = getSecurePreferencesNode();
            try {
                node.put("password", password, true); //$NON-NLS-1$
            } catch (StorageException e) {
                logException(shell, e);
            }
        } else {
            configuration.setAttribute(GeneratorLaunchConstants.ATTR_SQL_SCRIPT_PASSWORD, password);
        }
    }
    
    public static String getPassword(ILaunchConfiguration configuration) {
        boolean secure = getBooleanOrFalse(configuration, GeneratorLaunchConstants.ATTR_SQL_SCRIPT_SECURE_CREDENTIALS);
        String password;
        if (secure) {
            ISecurePreferences node = getSecurePreferencesNode();
            password = getTextOrBlank(node, "password"); //$NON-NLS-1$
        } else {
            password = getTextOrBlank(configuration, GeneratorLaunchConstants.ATTR_SQL_SCRIPT_PASSWORD);
        }
        return password;
    }

    public static void setUserId(ILaunchConfigurationWorkingCopy configuration, String userId, Shell shell) {
        boolean secure = getBooleanOrFalse(configuration, GeneratorLaunchConstants.ATTR_SQL_SCRIPT_SECURE_CREDENTIALS);
        if (secure) {
            ISecurePreferences node = getSecurePreferencesNode();
            try {
                node.put("user", userId, false); //$NON-NLS-1$
            } catch (StorageException e) {
                logException(shell, e);
            }
        } else {
            configuration.setAttribute(GeneratorLaunchConstants.ATTR_SQL_SCRIPT_USERID, userId);
        }
    }
    
    public static String getUserId(ILaunchConfiguration configuration) {
        boolean secure = getBooleanOrFalse(configuration, GeneratorLaunchConstants.ATTR_SQL_SCRIPT_SECURE_CREDENTIALS);
        String userId;
        if (secure) {
            ISecurePreferences node = getSecurePreferencesNode();
            userId = getTextOrBlank(node, "user"); //$NON-NLS-1$
        } else {
            userId = getTextOrBlank(configuration, GeneratorLaunchConstants.ATTR_SQL_SCRIPT_USERID);
        }
        return userId;
    }

    private static void logException(Shell shell, StorageException e) {
        MessageDialog.openError(shell,
                Messages.SECURE_STORAGE_ERROR_DIALOG_TITLE,
                Messages.SECURE_STORAGE_ERROR_DIALOG_MESSAGE);
        Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
                Messages.SECURE_STORAGE_ERROR_LOG_MESSAGE, e);
        Activator.getDefault().getLog().log(status);
    }
    
    private static ISecurePreferences getSecurePreferencesNode() {
        ISecurePreferences root = SecurePreferencesFactory.getDefault();
        ISecurePreferences node = root.node("/org.mybatis.generator/sqlscript"); //$NON-NLS-1$
        return node;
    }
}
