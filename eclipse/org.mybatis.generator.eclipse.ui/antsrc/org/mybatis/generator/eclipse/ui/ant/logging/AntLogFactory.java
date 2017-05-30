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
package org.mybatis.generator.eclipse.ui.ant.logging;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import org.mybatis.generator.eclipse.ui.ant.logging.commons.JakartaCommonsLoggingLogFactory;
import org.mybatis.generator.eclipse.ui.ant.logging.log4j.Log4jLoggingLogFactory;
import org.mybatis.generator.eclipse.ui.ant.logging.log4j2.Log4j2LoggingLogFactory;
import org.mybatis.generator.eclipse.ui.ant.logging.slf4j.Slf4jLoggingLogFactory;
import org.mybatis.generator.logging.AbstractLogFactory;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.jdk14.Jdk14LoggingLogFactory;
import org.mybatis.generator.logging.nologging.NoLoggingLogFactory;

/**
 * Factory for creating loggers
 * 
 * @author Jeff Butler
 * 
 */
public class AntLogFactory implements AbstractLogFactory {
    private AbstractLogFactory logFactory;

    public AntLogFactory(String loggingImplementation) throws LogException {
        if (loggingImplementation == null || loggingImplementation.length() == 0) {
            calculateDefaultImplementation();
        } else {
            if ("SLF4J".equals(loggingImplementation)) { //$NON-NLS-1$
                setImplementation(new Slf4jLoggingLogFactory());
            } else if ("COMMONS_LOGGING".equals(loggingImplementation)) { //$NON-NLS-1$
                setImplementation(new JakartaCommonsLoggingLogFactory());
            } else if ("LOG4J2".equals(loggingImplementation)) { //$NON-NLS-1$
                setImplementation(new Log4j2LoggingLogFactory());
            } else if ("LOG4J".equals(loggingImplementation)) { //$NON-NLS-1$
                setImplementation(new Log4jLoggingLogFactory());
            } else if ("JDK".equals(loggingImplementation)) { //$NON-NLS-1$
                setImplementation(new Jdk14LoggingLogFactory());
            } else {
                setImplementation(new NoLoggingLogFactory());
            }
        }
    }

    private void calculateDefaultImplementation() {
        tryImplementation(new Slf4jLoggingLogFactory());
        tryImplementation(new JakartaCommonsLoggingLogFactory());
        tryImplementation(new Log4j2LoggingLogFactory());
        tryImplementation(new Log4jLoggingLogFactory());
        tryImplementation(new Jdk14LoggingLogFactory());
        tryImplementation(new NoLoggingLogFactory());
    }

    @Override
    public Log getLog(Class<?> clazz) {
        try {
            return logFactory.getLog(clazz);
        } catch (Throwable t) {
            throw new RuntimeException(getString("RuntimeError.21", //$NON-NLS-1$
                    clazz.getName(), t.getMessage()), t);
        }
    }

    private void tryImplementation(AbstractLogFactory factory) {
        if (logFactory == null) {
            try {
                setImplementation(factory);
            } catch (LogException e) {
                // ignore
            }
        }
    }

    private void setImplementation(AbstractLogFactory factory) throws LogException {
        try {
            Log log = factory.getLog(AntLogFactory.class);
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + factory + "' adapter."); //$NON-NLS-1$ //$NON-NLS-2$
            }
            logFactory = factory;
        } catch (Throwable t) {
            throw new LogException("Error setting Log implementation.  Cause: " + t.getMessage(), t); //$NON-NLS-1$
        }
    }
}
