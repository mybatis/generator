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

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.mybatis.generator.eclipse.ui.launcher.tabs.ConfigurationTab;
import org.mybatis.generator.eclipse.ui.launcher.tabs.SqlScriptTab;
import org.mybatis.generator.eclipse.ui.launcher.tabs.UserClasspathTab;

public class GeneratorTabGroup extends AbstractLaunchConfigurationTabGroup {

    @Override
    public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
                new ConfigurationTab(),
                new SqlScriptTab(),
                new UserClasspathTab(),
                new CommonTab()
        };
        setTabs(tabs);
    }
}
