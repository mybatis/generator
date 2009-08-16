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
package org.apache.ibatis.abator.internal;

import org.apache.ibatis.abator.api.ProgressCallback;

/**
 * This class implements a progress callback that does nothing.  It is used when the
 * Abator client passes in a null for the ProgressCallback.
 * 
 * @author Jeff Butler
 */
public class NullProgressCallback implements ProgressCallback {

    /**
     * 
     */
    public NullProgressCallback() {
        super();
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ProgressCallback#finished()
     */
    public void finished() {
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.ibatis.abator.api.ProgressCallback#checkCancel()
     */
    public void checkCancel() throws InterruptedException {
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
    }
}
