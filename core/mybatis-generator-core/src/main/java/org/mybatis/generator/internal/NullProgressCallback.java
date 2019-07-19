/**
 *    Copyright 2006-2019 the original author or authors.
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
package org.mybatis.generator.internal;

import org.mybatis.generator.api.ProgressCallback;

/**
 * This class implements a progress callback that does nothing. It is used when
 * the client passes in a null for the ProgressCallback.
 * 
 * @author Jeff Butler
 */
public class NullProgressCallback implements ProgressCallback {

    public NullProgressCallback() {
        super();
    }

    @Override
    public void generationStarted(int totalTasks) {
        // nothing to do in the null callback
    }

    @Override
    public void introspectionStarted(int totalTasks) {
        // nothing to do in the null callback
    }

    @Override
    public void saveStarted(int totalTasks) {
        // nothing to do in the null callback
    }

    @Override
    public void startTask(String taskName) {
        // nothing to do in the null callback
    }

    @Override
    public void checkCancel() throws InterruptedException {
        // nothing to do in the null callback
    }

    @Override
    public void done() {
        // nothing to do in the null callback
    }
}
