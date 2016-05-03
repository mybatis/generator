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
package org.mybatis.generator.api;


/**
 * Abstract class that holds information common to all generated files.
 * 
 * @author Jeff Butler
 */
public abstract class GeneratedFile {
    
    /** The target project. */
    protected String targetProject;

    /**
     * Instantiates a new generated file.
     *
     * @param targetProject
     *            the target project
     */
    public GeneratedFile(String targetProject) {
        super();
        this.targetProject = targetProject;
    }

    /**
     * This method returns the entire contents of the generated file. Clients
     * can simply save the value returned from this method as the file contents.
     * Subclasses such as @see org.mybatis.generator.api.GeneratedJavaFile offer
     * more fine grained access to file parts, but still implement this method
     * in the event that the entire contents are desired.
     * 
     * @return Returns the content.
     */
    public abstract String getFormattedContent();

    /**
     * Get the file name (without any path). Clients should use this method to
     * determine how to save the results.
     * 
     * @return Returns the file name.
     */
    public abstract String getFileName();

    /**
     * Gets the target project. Clients can call this method to determine how to
     * save the results.
     * 
     * @return the target project
     */
    public String getTargetProject() {
        return targetProject;
    }

    /**
     * Get the target package for the file. Clients should use this method to
     * determine how to save the results.
     * 
     * @return Returns the target project.
     */
    public abstract String getTargetPackage();

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getFormattedContent();
    }

    /**
     * Checks if is mergeable.
     *
     * @return true, if is mergeable
     */
    public abstract boolean isMergeable();
}
