/**
 *    Copyright 2006-2015 the original author or authors.
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
package org.mybatis.generator.logging;

import org.apache.log4j.Logger;

/**
 * 
 * @author Clinton Begin
 * 
 */
public class Log4jImpl implements Log {

    private Logger log;

    public Log4jImpl(Class<?> clazz) {
        log = Logger.getLogger(clazz);
    }

    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public void error(String s, Throwable e) {
        log.error(s, e);
    }

    public void error(String s) {
        log.error(s);
    }

    public void debug(String s) {
        log.debug(s);
    }

    public void warn(String s) {
        log.warn(s);
    }
}
