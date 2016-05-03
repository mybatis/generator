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
package org.mybatis.generator.maven;

import org.apache.maven.plugin.logging.Log;
import org.mybatis.generator.internal.NullProgressCallback;

/**
 * This callback logs progress messages with the Maven logger
 * 
 * @author Jeff Butler
 *
 */
public class MavenProgressCallback extends NullProgressCallback {

    private Log log;
    private boolean verbose;

    /**
     * 
     */
    public MavenProgressCallback(Log log, boolean verbose) {
        super();
        this.log = log;
        this.verbose = verbose;
    }

    @Override
    public void startTask(String subTaskName) {
        if (verbose) {
            log.info(subTaskName);
        }
    }
}
