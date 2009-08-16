/*
 *  Copyright 2006 The Apache Software Foundation
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

package org.apache.ibatis.abator.ant;

import org.apache.ibatis.abator.api.ProgressCallback;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * This callback logs progress messages with the Ant logger
 * 
 * @author Jeff Butler
 *
 */
public class AntProgressCallback implements ProgressCallback {

    private Task task;
    private boolean verbose;
    
    /**
     * 
     */
    public AntProgressCallback(Task task, boolean verbose) {
        super();
        this.task = task;
        this.verbose = verbose;
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ProgressCallback#setNumberOfSubTasks(int)
     */
    public void setNumberOfSubTasks(int totalSubTasks) {
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ProgressCallback#startSubTask(java.lang.String)
     */
    public void startSubTask(String subTaskName) {
        if (verbose) {
            task.log(subTaskName, Project.MSG_VERBOSE);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ProgressCallback#finished()
     */
    public void finished() {
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ProgressCallback#checkCancel()
     */
    public void checkCancel() throws InterruptedException {
    }
}
