package org.mybatis.generator.eclipse.ui.ant;

import java.io.IOException;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

public class DebugBuildListener implements BuildListener {
    private IOConsole console = new IOConsole("MyBatis Generator", null); //$NON-NLS-1$
    private IOConsoleOutputStream outputStream = console.newOutputStream();
    
    public DebugBuildListener() {
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] {console});
    }

    @Override
    public void buildStarted(BuildEvent event) {
        writeMessage("MyBatis Generator Started"); //$NON-NLS-1$
    }

    @Override
    public void buildFinished(BuildEvent event) {
        writeMessage("MyBatis Generator Finished"); //$NON-NLS-1$
    }

    @Override
    public void targetStarted(BuildEvent event) {
        writeMessage("Target " + event.getTarget().getName() + " - Started"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void targetFinished(BuildEvent event) {
        writeMessage("Target " + event.getTarget().getName() + " - Finished"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void taskStarted(BuildEvent event) {
        writeMessage("Task " + event.getTask().getTaskName() + " - Started"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void taskFinished(BuildEvent event) {
        writeMessage("Task " + event.getTask().getTaskName() + " - Finished"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void messageLogged(BuildEvent event) {
        writeMessage("  " + event.getMessage()); //$NON-NLS-1$
    }

    private void writeMessage(String message) {
        try {
            outputStream.write(message);
            outputStream.write('\n');
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
