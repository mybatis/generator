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
