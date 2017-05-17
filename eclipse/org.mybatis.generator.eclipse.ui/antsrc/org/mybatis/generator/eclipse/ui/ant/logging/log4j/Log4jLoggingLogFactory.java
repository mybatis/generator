package org.mybatis.generator.eclipse.ui.ant.logging.log4j;

import org.mybatis.generator.logging.AbstractLogFactory;
import org.mybatis.generator.logging.Log;

public class Log4jLoggingLogFactory implements AbstractLogFactory {
    @Override
    public Log getLog(Class<?> clazz) {
        return new Log4jImpl(clazz);
    }
}