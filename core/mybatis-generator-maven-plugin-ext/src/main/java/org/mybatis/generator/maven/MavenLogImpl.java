/**
 *    Copyright 2006-2017 the original author or authors.
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

public class MavenLogImpl implements org.mybatis.generator.logging.Log {
    private final Log mavenLog;

    MavenLogImpl(Log log) {
        mavenLog = log;
    }

    @Override
    public boolean isDebugEnabled() {
        return mavenLog.isDebugEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        mavenLog.error(s, e);
    }

    @Override
    public void error(String s) {
        mavenLog.error(s);
    }

    @Override
    public void debug(String s) {
        mavenLog.debug(s);
    }

    @Override
    public void warn(String s) {
        mavenLog.warn(s);
    }

}
