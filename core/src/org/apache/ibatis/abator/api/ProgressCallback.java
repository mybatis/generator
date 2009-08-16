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
package org.apache.ibatis.abator.api;

/**
 * This interface can be implemented to return progress information from the file generation
 * process.  This interface is loosely based on the standard Eclipse IProgressMonitor interface,
 * but does not implement all its methods.
 * 
 * During the execution of a long running method, Abator will call the 
 * <code>setNumberOfSubTasks</code> method first, and then repeatedly call <code>startSubTask</code.
 * When the long running method is complete, Abator will call <code>finished</code>.
 * Periodically, Abator will call <code>checkCancel</code> to see if the method should
 * be cancelled.
 * 
 * @author Jeff Butler
 */
public interface ProgressCallback {
    /**
     * Called to designate the maximum number of startSubTask messages that will be sent.
     * It is not guaranteed that this number startSubTask messages will be sent.  The
     * actual number of messages depends on the objects generated from each table.
     * 
     * @param totalSubTasks
     */
    
    void setNumberOfSubTasks(int totalSubTasks);
    
    /**
     * Called to denote the beginning of another task
     * 
     * @param subTaskName a descriptive name of the current work step
     */
    void startSubTask(String subTaskName);
    
    /**
     * Abator calls this method when all subtasks are finished
     */
    void finished();

    /**
     * Abator will call this method periodically during a long running method.
     * If the the implementation throws InterruptedException, then the method
     * will be cancelled.  Any files that have already been saved will remain on
     * the file system.
     * 
     * @throws InterruptedException if the main task should finish
     */
    void checkCancel() throws InterruptedException;
}
