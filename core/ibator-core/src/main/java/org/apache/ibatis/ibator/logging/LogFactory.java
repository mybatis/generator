/*
 *  Copyright 2009 The Apache Software Foundation
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
package org.apache.ibatis.ibator.logging;

import org.apache.ibatis.ibator.internal.IbatorObjectFactory;
import org.apache.ibatis.ibator.internal.util.messages.Messages;


/**
 * Factory for creating loggers.  Uses runtime introspection
 * to determine the AbstractLogFactory implementation.
 * 
 * @author Jeff Butler
 *
 */
public class LogFactory {
    private static AbstractLogFactory logFactory;
    
    static {
        try {
            IbatorObjectFactory.internalClassForName("org.apache.log4j.Logger"); //$NON-NLS-1$
            logFactory = new Log4jLoggingLogFactory();
        } catch (Exception e) {
            logFactory = new JdkLoggingLogFactory();
        }
    }

    public static Log getLog(Class<?> clazz) {
        try {
            return logFactory.getLog(clazz);
        } catch (Throwable t) {
            throw new RuntimeException(Messages.getString("RuntimeError.21", //$NON-NLS-1$
                    clazz.getName(), t.getMessage()), t);
        }
    }

    /**
     * This method will switch the logging implementation to Java native logging.
     * This is useful in situations
     * where you want to use Java native logging to log Ibator activity but
     * Log4J is on the classpath. Note that this method is
     * only effective for log classes obtained after calling this method. If you
     * intend to use this method you should call it before calling any other
     * Ibator method.
     */
    public static synchronized void forceJavaLogging() {
        logFactory = new JdkLoggingLogFactory();
    }
    
    private static class JdkLoggingLogFactory implements AbstractLogFactory {
        public Log getLog(Class<?> clazz) {
            return new JdkLoggingImpl(clazz);
        }
    }
    
    private static class Log4jLoggingLogFactory implements AbstractLogFactory {
        public Log getLog(Class<?> clazz) {
            return new Log4jImpl(clazz);
        }
    }
    
    public static void setLogFactory(AbstractLogFactory logFactory) {
        LogFactory.logFactory = logFactory;
    }
}
