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
package org.mybatis.generator.api;

import java.io.File;

import org.mybatis.generator.exception.ShellException;

/**
 * This interface defines methods that a shell should support to enable
 * the generator
 * to work. A "shell" is defined as the execution environment (i.e. an
 * Eclipse plugin, and Ant task, a NetBeans plugin, etc.)
 * 
 * The default ShellCallback that is very low function and does
 * not support the merging of Java files. The default shell callback is 
 * appropriate for use in well controlled environments where no changes
 * made to generated Java files.
 * 
 * @author Jeff Butler
 */
public interface ShellCallback {
    /**
     * This method is called to ask the shell to resolve a
     * project/package combination into a directory on the file system.
     * This method is called repeatedly (once for each generated file), so it
     * would be wise for an implementing class to cache results.
     * 
     * The returned <code>java.io.File</code> object:
     * <ul>
     * <li>Must be a directory</li>
     * <li>Must exist</li>
     * </ul>
     * 
     * The default shell callback interprets both values as directories and
     * simply concatenates the two values to generate the default directory.
     * 
     * @param targetProject
     * @param targetPackage
     * @return the directory (must exist)
     * @throws ShellException
     *             if the project/package cannot be resolved into a directory on
     *             the file system. In this case, the generator will not save the file
     *             it is currently working on. The generator will add the exception
     *             message to the list of warnings automatically.
     */
    File getDirectory(String targetProject, String targetPackage)
            throws ShellException;

    /**
     * This method is called if a newly generated Java file would
     * overwrite an existing file. This method should return the merged source
     * (formatted). The generator will write the merged source as-is to the file
     * system.
     * 
     * A merge typically follows these steps:
     * <ol>
     * <li>Delete any methods/fields in the existing file that have the
     * specified JavaDoc tag</li>
     * <li>Add any new super interfaces from the new file into the existing file
     * </li>
     * <li>Make sure that the existing file's super class matches the new file</li>
     * <li>Make sure that the existing file is of the same type as the existing
     * file (either interface or class)</li>
     * <li>Add any new imports from the new file into the existing file</li>
     * <li>Add all methods and fields from the new file into the existing file</li>
     * <li>Format the resulting source string</li>
     * </ol>
     * 
     * This method is called only if you return <code>true</code> from
     * <code>isMergeSupported()</code>.
     * 
     * @param newFileSource
     *            the source of the newly generated Java file
     * @param existingFileFullPath
     *            the fully qualified path name of the existing Java file
     * @param javadocTags
     *            the JavaDoc tags that denotes which methods and fields in the
     *            old file to delete (if the Java element has any of these tags,
     *            the element is eligible for merge)
     * @return the merged source, properly formatted. The source will be saved
     *         exactly as returned from this method.
     * @throws ShellException
     *             if the file cannot be merged for some reason. If this
     *             exception is thrown, nothing will be saved and the
     *             existing file will remain undisturbed. The generator will add the
     *             exception message to the list of warnings automatically.
     */
    String mergeJavaFile(String newFileSource, String existingFileFullPath,
            String[] javadocTags) throws ShellException;

    /**
     * After all files are saved to the file system, this method is called
     * once for each unique project that was affected by the generation
     * run. This method is useful if your IDE needs to be informed that file
     * system objects have been created or updated. If you are running 
     * outside of an IDE, your implementation need not do anything in this
     * method.
     * 
     * @param project
     *            the project to be refreshed
     */
    void refreshProject(String project);

    /**
     * Return true if the callback supports Java merging, otherwise false.
     * The <code>mergeJavaFile()</code> method will be called only if this
     * method returns <code>true</code>.
     * 
     * @return a boolean specifying whether Java merge is supported or not
     */
    boolean isMergeSupported();

    /**
     * Return true if the generator should overwrite an existing file if one exists.
     * This method will be called only if <code>isMergeSupported()</code>
     * returns <code>false</code> and a file exists that would be overwritten by
     * a generated file. If you return <code>true</code>, then we will log a
     * warning specifying what file was overwritten.
     * 
     * @return true if you want to overwrite existing files
     */
    boolean isOverwriteEnabled();
}
