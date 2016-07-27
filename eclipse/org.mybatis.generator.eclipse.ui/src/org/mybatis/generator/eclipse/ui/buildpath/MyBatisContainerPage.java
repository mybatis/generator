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
package org.mybatis.generator.eclipse.ui.buildpath;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MyBatisContainerPage extends WizardPage
    implements IClasspathContainerPage {

    public MyBatisContainerPage() {
        super("MyBatis Generator");
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        
        Label label = new Label(composite, SWT.NONE);
        label.setText("This will add MyBatis Generator to your classpath");
        
        setControl(composite);
    }

    public boolean finish() {
        // TODO Auto-generated method stub
        return true;
    }

    public IClasspathEntry getSelection() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSelection(IClasspathEntry containerEntry) {
        // TODO Auto-generated method stub

    }
}
