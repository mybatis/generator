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

import org.mybatis.generator.eclipse.ui.Activator;

public interface GeneratorLaunchConstants {
    public static final String ATTR_CONFIGURATION_FILE_NAME = Activator.PLUGIN_ID + ".CONFIG_TAB.FILE_NAME"; //$NON-NLS-1$
    
    public static final String ATTR_SQL_SCRIPT_FILE_NAME = Activator.PLUGIN_ID + ".SQL_TAB.FILE_NAME"; //$NON-NLS-1$
    public static final String ATTR_SQL_SCRIPT_DRIVER_CLASS = Activator.PLUGIN_ID + ".SQL_TAB.DRIVER_CLASS"; //$NON-NLS-1$
    public static final String ATTR_SQL_SCRIPT_CONNECTION_URL = Activator.PLUGIN_ID + ".SQL_TAB.CONNECTION_URL"; //$NON-NLS-1$
    public static final String ATTR_SQL_SCRIPT_USERID = Activator.PLUGIN_ID + ".SQL_TAB.USER_ID"; //$NON-NLS-1$
    public static final String ATTR_SQL_SCRIPT_PASSWORD = Activator.PLUGIN_ID + ".SQL_TAB.PASSWORD"; //$NON-NLS-1$
    public static final String ATTR_SQL_SCRIPT_SECURE_CREDENTIALS = Activator.PLUGIN_ID + ".SQL_TAB.SECURE_CREDENTIALS"; //$NON-NLS-1$
}
