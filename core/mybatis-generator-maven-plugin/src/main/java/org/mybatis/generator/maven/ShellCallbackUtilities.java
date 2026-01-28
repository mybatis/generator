/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.maven;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;

import org.mybatis.generator.exception.ShellException;

public class ShellCallbackUtilities {
    public static String calculateTargetProject(String targetProject, File mavenOutputDirectory)
            throws ShellException {
        if ("MAVEN".equals(targetProject)) { //$NON-NLS-1$
            // targetProject is the output directory from the MyBatis generator
            // Mojo. It will be created if necessary

            if (!mavenOutputDirectory.exists()) {
                boolean rc = mavenOutputDirectory.mkdirs();
                if (!rc) {
                    throw new ShellException(getString("Warning.10", //$NON-NLS-1$
                            mavenOutputDirectory.getAbsolutePath()));
                }
            }

            return mavenOutputDirectory.getAbsolutePath();
        } else {
            return targetProject;
        }
    }
}
