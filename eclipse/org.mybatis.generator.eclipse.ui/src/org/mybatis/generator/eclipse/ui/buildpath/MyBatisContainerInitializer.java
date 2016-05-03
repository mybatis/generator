package org.mybatis.generator.eclipse.ui.buildpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class MyBatisContainerInitializer extends ClasspathContainerInitializer {

    @Override
    public void initialize(IPath containerPath, IJavaProject project)
            throws CoreException {
        MyBatisContainer container = new MyBatisContainer(containerPath, project);
        
        JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project},
                new IClasspathContainer[] {container}, null);
    }
}
