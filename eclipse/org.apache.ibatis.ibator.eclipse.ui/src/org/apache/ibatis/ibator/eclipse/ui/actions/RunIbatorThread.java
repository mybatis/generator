/*
 *  Copyright 2008 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.ibatis.ibator.eclipse.ui.actions;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.ibator.api.Ibator;
import org.apache.ibatis.ibator.config.IbatorConfiguration;
import org.apache.ibatis.ibator.config.xml.IbatorConfigurationParser;
import org.apache.ibatis.ibator.eclipse.core.callback.EclipseProgressCallback;
import org.apache.ibatis.ibator.eclipse.core.callback.EclipseShellCallback;
import org.apache.ibatis.ibator.eclipse.ui.IbatorUIPlugin;
import org.apache.ibatis.ibator.exception.InvalidConfigurationException;
import org.apache.ibatis.ibator.exception.XMLParserException;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;

/**
 * @author Jeff Butler
 *
 */
public class RunIbatorThread implements IWorkspaceRunnable {
    private File inputFile;

    private List<String> warnings;

    /**
     *  
     */
    public RunIbatorThread(File inputFile, List<String> warnings) {
        super();
        this.inputFile = inputFile;
        this.warnings = warnings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.resources.IWorkspaceRunnable#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void run(IProgressMonitor monitor) throws CoreException {
        SubMonitor subMonitor = SubMonitor.convert(monitor, 1000);
        subMonitor.beginTask("Generating iBATIS Artifacts:", 1000);

        try {
            subMonitor.subTask("Parsing Configuration");

            IbatorConfigurationParser cp = new IbatorConfigurationParser(
                    warnings);
            IbatorConfiguration config = cp.parseIbatorConfiguration(inputFile);

            subMonitor.worked(50);

            Ibator ibator = new Ibator(config, new EclipseShellCallback(), warnings);            
            monitor.subTask("Generating Files from Database Tables");
            SubMonitor spm = subMonitor.newChild(950);
            ibator.generate(new EclipseProgressCallback(spm));

        } catch (InterruptedException e) {
            throw new OperationCanceledException();
        } catch (SQLException e) {
            Status status = new Status(IStatus.ERROR, IbatorUIPlugin.PLUGIN_ID,
                    IStatus.ERROR, e.getMessage(), e);
            IbatorUIPlugin.getDefault().getLog().log(status);
            throw new CoreException(status);
        } catch (IOException e) {
            Status status = new Status(IStatus.ERROR, IbatorUIPlugin.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e);
            IbatorUIPlugin.getDefault().getLog().log(status);
            throw new CoreException(status);
        } catch (XMLParserException e) {
            List<String> errors = e.getErrors();
            MultiStatus multiStatus = new MultiStatus(IbatorUIPlugin.PLUGIN_ID, IStatus.ERROR,
                    "XML Parser Errors\n  See Details for more Information",
                    null);

            Iterator<String> iter = errors.iterator();
            while (iter.hasNext()) {
                Status message = new Status(IStatus.ERROR, IbatorUIPlugin.PLUGIN_ID, IStatus.ERROR, iter.next(),
                        null);

                multiStatus.add(message);
            }
            throw new CoreException(multiStatus);
        } catch (InvalidConfigurationException e) {
            List<String> errors = e.getErrors();

            MultiStatus multiStatus = new MultiStatus(
                    IbatorUIPlugin.PLUGIN_ID,
                    IStatus.ERROR,
                    "Invalid Configuration\n  See Details for more Information",
                    null);

            Iterator<String> iter = errors.iterator();
            while (iter.hasNext()) {
                Status message = new Status(IStatus.ERROR, IbatorUIPlugin.PLUGIN_ID, IStatus.ERROR, iter.next(),
                        null);

                multiStatus.add(message);
            }
            throw new CoreException(multiStatus);
        } finally {
            monitor.done();
        }
    }
}
