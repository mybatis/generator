/*
 *  Copyright 2005 The Apache Software Foundation
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
package org.apache.ibatis.abator.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ClasspathVariableInitializer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.framework.Bundle;

/**
 * This class initializes the ABATOR_HOME classpath variable.  This function will
 * not be made available until the Eclipse 3.1 version of the plugin.
 * 
 * @author Jeff Butler
 */
public class AbatorHomeInitializer extends ClasspathVariableInitializer {

	private static final String ABATOR_HOME = "ABATOR_HOME"; //$NON-NLS-1$
	/**
	 * 
	 */
	public AbatorHomeInitializer() {
		super();
	}

	public void initialize(String variable) {
		Bundle bundle= Platform.getBundle("org.apache.ibatis.abator.core"); //$NON-NLS-1$
		if (bundle == null) {
			JavaCore.removeClasspathVariable(ABATOR_HOME, null);
			return;
		}
		URL installLocation= bundle.getEntry("/"); //$NON-NLS-1$
		URL local= null;
		try {
			local= FileLocator.toFileURL(installLocation);
		} catch (IOException e) {
			JavaCore.removeClasspathVariable(ABATOR_HOME, null);
			return;
		}
		try {
			String fullPath= new File(local.getPath()).getAbsolutePath();
			JavaCore.setClasspathVariable(ABATOR_HOME, new Path(fullPath), null);
		} catch (JavaModelException e1) {
			JavaCore.removeClasspathVariable(ABATOR_HOME, null);
		}
	}
}
