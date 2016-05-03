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
package org.mybatis.generator.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 
 * @author Clinton Begin
 * @author Jeff Butler
 * 
 */
public class JdkLoggingImpl implements Log {

    private Logger log;

    public JdkLoggingImpl(Class<?> clazz) {
        log = Logger.getLogger(clazz.getName());
    }

    public boolean isDebugEnabled() {
        return log.isLoggable(Level.FINE);
    }

    public void error(String s, Throwable e) {
        LogRecord lr = new LogRecord(Level.SEVERE, s);
        lr.setSourceClassName(log.getName());
        lr.setThrown(e);

        log.log(lr);
    }

    public void error(String s) {
        LogRecord lr = new LogRecord(Level.SEVERE, s);
        lr.setSourceClassName(log.getName());

        log.log(lr);
    }

    public void debug(String s) {
        LogRecord lr = new LogRecord(Level.FINE, s);
        lr.setSourceClassName(log.getName());

        log.log(lr);
    }

    public void warn(String s) {
        LogRecord lr = new LogRecord(Level.WARNING, s);
        lr.setSourceClassName(log.getName());

        log.log(lr);
    }
}
