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
package org.mybatis.generator.eclipse.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    public static String FILE_PICKER_BROWSE_FILE_SYSTEM;
    public static String FILE_PICKER_BROWSE_WORKSPACE;
    public static String FILE_PICKER_FILE_DOESNT_EXIST;

    public static String CONFIGURATION_TAB_INVALID_MESSAGE;
    public static String CONFIGURATION_TAB_FILE_GROUP_TITLE;
    public static String CONFIGURATION_TAB_FILE_PICKER_DIALOG_MESSAGE;
    public static String CONFIGURATION_TAB_FILE_PICKER_DIALOG_TITLE;
    public static String CONFIGURATION_TAB_NAME;

    public static String SQL_SCRIPT_TAB_FILE_GROUP_TITLE;
    public static String SQL_SCRIPT_TAB_FILE_PICKER_DIALOG_MESSAGE;
    public static String SQL_SCRIPT_TAB_FILE_PICKER_DIALOG_TITLE;
    public static String SQL_SCRIPT_TAB_HELP_TEXT;
    public static String SQL_SCRIPT_TAB_NAME;
    public static String SQL_SCRIPT_TAB_JDBC_CONNECTION_GROUP_TITLE;
    public static String SQL_SCRIPT_TAB_JDBC_DRIVER_LABEL;
    public static String SQL_SCRIPT_TAB_JDBC_URL_LABEL;
    public static String SQL_SCRIPT_TAB_USERID_LABEL;
    public static String SQL_SCRIPT_TAB_PASSWORD_LABEL;
    public static String SQL_SCRIPT_TAB_JDBC_DRIVER_REQUIRED;
    public static String SQL_SCRIPT_TAB_JDBC_URL_REQUIRED;
    public static String SQL_SCRIPT_TAB_SECURE_STORAGE;
    
    public static String LAUNCH_CONFIGURATION_SELECTOR_TITLE;
    public static String LAUNCH_CONFIGURATION_SELECTOR_MESSAGE;
    public static String LAUNCH_ERROR_ERROR_GENERATING_ANT_FILE;

    public static String SECURE_STORAGE_ERROR_DIALOG_TITLE;
    public static String SECURE_STORAGE_ERROR_DIALOG_MESSAGE;
    public static String SECURE_STORAGE_ERROR_LOG_MESSAGE;
    
    static {
        NLS.initializeMessages("org.mybatis.generator.eclipse.ui.messages", Messages.class); //$NON-NLS-1$
    }
}
