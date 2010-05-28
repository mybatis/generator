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

package org.mybatis.generator.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.mybatis.generator.internal.NullProgressCallback;

/**
 * This callback logs progress messages with the Ant logger
 * 
 * @author Jeff Butler
 * 
 */
public class AntProgressCallback extends NullProgressCallback {

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

    @Override
    public void startTask(String subTaskName) {
        if (verbose) {
            task.log(subTaskName, Project.MSG_VERBOSE);
        }
    }
}
