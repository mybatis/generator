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
package org.mybatis.generator.eclipse.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.eclipse.ui.Activator;

public class NewConfigFileWizard extends Wizard implements INewWizard {
    private NewConfigFileWizardPage1 page;
    private ISelection selection;

    /**
     * Constructor for NewConfigFileWizard.
     */
    public NewConfigFileWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {
        page = new NewConfigFileWizardPage1(selection);
        addPage(page);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We
     * will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        final String containerName = page.getLocation();
        final String fileName = page.getFileName();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor)
                    throws InvocationTargetException {
                try {
                    doFinish(containerName, fileName, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error",
                    realException.getMessage());
            return false;
        }
        return true;
    }

    /**
     * The worker method. It will find the container, create the file if missing
     * or just replace its contents, and open the editor on the newly created
     * file.
     */
    private void doFinish(String containerName, String fileName,
            IProgressMonitor monitor) throws CoreException {
        // create a sample file
        monitor.beginTask("Creating " + fileName, 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(containerName));
        if (!resource.exists() || !(resource instanceof IContainer)) {
            throwCoreException("Container \"" + containerName
                    + "\" does not exist.");
        }
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        try {
            InputStream stream = openContentStream();
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
        }
        monitor.worked(1);
        monitor.setTaskName("Opening file for editing...");
        getShell().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                IWorkbenchPage page = PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow().getActivePage();
                try {
                    IDE.openEditor(page, file, true);
                } catch (PartInitException e) {
                }
            }
        });
        monitor.worked(1);
    }

    /**
     * We will initialize file contents with a sample text.
     */
    private InputStream openContentStream() {

        Document document = new Document(XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID);

        XmlElement generatorConfiguration = new XmlElement("generatorConfiguration"); //$NON-NLS-1$
        document.setRootElement(generatorConfiguration);

        XmlElement context = new XmlElement("context"); //$NON-NLS-1$
        context.addAttribute(new Attribute("id", "context1")); //$NON-NLS-1$ //$NON-NLS-2$
        generatorConfiguration.addElement(context);

        XmlElement jdbcConnection = new XmlElement("jdbcConnection"); //$NON-NLS-1$
        jdbcConnection.addAttribute(new Attribute("driverClass", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        jdbcConnection.addAttribute(new Attribute("connectionURL", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        jdbcConnection.addAttribute(new Attribute("userId", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        jdbcConnection.addAttribute(new Attribute("password", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        context.addElement(jdbcConnection);

        XmlElement javaModelGenerator = new XmlElement("javaModelGenerator"); //$NON-NLS-1$
        javaModelGenerator.addAttribute(new Attribute("targetPackage", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        javaModelGenerator.addAttribute(new Attribute("targetProject", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        context.addElement(javaModelGenerator);

        XmlElement sqlMapGenerator = new XmlElement("sqlMapGenerator"); //$NON-NLS-1$
        sqlMapGenerator.addAttribute(new Attribute("targetPackage", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        sqlMapGenerator.addAttribute(new Attribute("targetProject", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        context.addElement(sqlMapGenerator);

        XmlElement javaClientGenerator = new XmlElement("javaClientGenerator"); //$NON-NLS-1$
        javaClientGenerator.addAttribute(new Attribute("targetPackage", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        javaClientGenerator.addAttribute(new Attribute("targetProject", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        javaClientGenerator.addAttribute(new Attribute("type", "XMLMAPPER")); //$NON-NLS-1$ //$NON-NLS-2$
        context.addElement(javaClientGenerator);

        XmlElement table = new XmlElement("table"); //$NON-NLS-1$
        table.addAttribute(new Attribute("schema", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        table.addAttribute(new Attribute("tableName", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        XmlElement columnOverride = new XmlElement("columnOverride"); //$NON-NLS-1$
        columnOverride.addAttribute(new Attribute("column", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        columnOverride.addAttribute(new Attribute("property", "???")); //$NON-NLS-1$ //$NON-NLS-2$
        table.addElement(columnOverride);
        context.addElement(table);

        return new ByteArrayInputStream(new DefaultXmlFormatter().getFormattedContent(document).getBytes());
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
                IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize
     * from it.
     */
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

    public void init(IWorkbench workbench, ISelection selection) {
        this.selection = selection;
    }
}