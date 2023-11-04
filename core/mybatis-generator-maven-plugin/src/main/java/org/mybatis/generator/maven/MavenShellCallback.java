/*
 *    Copyright 2006-2023 the original author or authors.
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
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * Shell callback that calculates the Maven output directory.
 *
 * @author Jeff Butler
 */
public class MavenShellCallback extends DefaultShellCallback {
    private final MyBatisGeneratorMojo mybatisGeneratorMojo;

    public MavenShellCallback(MyBatisGeneratorMojo mybatisGeneratorMojo, boolean overwrite) {
        super(overwrite);
        this.mybatisGeneratorMojo = mybatisGeneratorMojo;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        if ("MAVEN".equals(targetProject)) {
            // targetProject is the output directory from the MyBatis generator
            // Mojo. It will be created if necessary

            File project = mybatisGeneratorMojo.getOutputDirectory();
            if (!project.exists()) {
                boolean rc = project.mkdirs();
                if (!rc) {
                    throw new ShellException(getString("Warning.10", //$NON-NLS-1$
                            project.getAbsolutePath()));
                }
            }

            return super.getDirectory(project.getAbsolutePath(), targetPackage);
        } else {
            return super.getDirectory(targetProject, targetPackage);
        }
    }
}
