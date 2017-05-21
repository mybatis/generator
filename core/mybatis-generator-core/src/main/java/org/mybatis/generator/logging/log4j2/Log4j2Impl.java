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
package org.mybatis.generator.logging.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.mybatis.generator.logging.Log;

/**
 * @author Eduardo Macarron
 */
public class Log4j2Impl implements Log {

    private Log log;

    public Log4j2Impl(Class<?> clazz) {
        Logger logger = LogManager.getLogger(clazz);

        if (logger instanceof AbstractLogger) {
            log = new Log4j2AbstractLoggerImpl((AbstractLogger) logger);
        } else {
            log = new Log4j2LoggerImpl(logger);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        log.error(s, e);
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void debug(String s) {
        log.debug(s);
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

}
