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
package org.mybatis.generator.api;

import java.io.File;

import org.mybatis.generator.exception.ShellException;

/**
 * This interface defines methods that a shell should support to enable
 * the generator to work. A "shell" is defined as the execution environment (i.e. an
 * Eclipse plugin, and Ant task, a Maven Mojo, etc.)
 *
 * @author Jeff Butler
 */
public interface ShellCallback {

    /**
     * This method is called to ask the shell to resolve a project/package combination into a directory on the file
     * system. This method is called repeatedly (once for each generated file), so it would be wise for an implementing
     * class to cache results.
     *
     * <p>The returned <code>java.io.File</code> object:
     * <ul>
     *   <li>Must be a directory</li>
     *   <li>Must exist</li>
     * </ul>
     *
     * <p>The default shell callback interprets both values as directories and simply concatenates the two values to
     * generate the default directory.
     *
     * @param targetProject
     *            the target project
     * @param targetPackage
     *            the target package
     *
     * @return the directory (must exist)
     *
     * @throws ShellException
     *             if the project/package cannot be resolved into a directory on the file system. In this case, the
     *             generator will not save the file it is currently working on. The generator will add the exception
     *             message to the list of warnings automatically.
     */
    File getDirectory(String targetProject, String targetPackage)
            throws ShellException;

    /**
     * After all files are saved to the file system, this method is called
     * once for each unique project affected by the generation run.
     * This method is useful if your IDE needs to be informed that file
     * system objects have been created or updated. If you are running
     * outside an IDE, your implementation need not do anything in this
     * method.
     *
     * @param project the project to be refreshed
     */
    default void refreshProject(String project) {}
}
