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
package org.mybatis.generator.logging.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mybatis.generator.logging.Log;

/**
 * @author Eduardo Macarron
 */
public class Log4jImpl implements Log {

    private static final String FQCN = Log4jImpl.class.getName();

    private Logger log;

    public Log4jImpl(Class<?> clazz) {
        log = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        log.log(FQCN, Level.ERROR, s, e);
    }

    @Override
    public void error(String s) {
        log.log(FQCN, Level.ERROR, s, null);
    }

    @Override
    public void debug(String s) {
        log.log(FQCN, Level.DEBUG, s, null);
    }

    @Override
    public void warn(String s) {
        log.log(FQCN, Level.WARN, s, null);
    }

}
