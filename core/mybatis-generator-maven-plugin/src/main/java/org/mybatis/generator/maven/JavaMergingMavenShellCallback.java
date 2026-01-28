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

import java.io.File;

import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.JavaMergingShellCallback;

/**
 * Shell callback that calculates the Maven output directory and supports Java merging.
 *
 * @author Jeff Butler
 */
public class JavaMergingMavenShellCallback extends JavaMergingShellCallback {
    private final MyBatisGeneratorMojo mybatisGeneratorMojo;

    public JavaMergingMavenShellCallback(MyBatisGeneratorMojo mybatisGeneratorMojo, boolean overwrite) {
        super(overwrite);
        this.mybatisGeneratorMojo = mybatisGeneratorMojo;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        var calculatedTargetProject = ShellCallbackUtilities.calculateTargetProject(targetProject,
                mybatisGeneratorMojo.getOutputDirectory());
        return super.getDirectory(calculatedTargetProject, targetPackage);
    }
}
