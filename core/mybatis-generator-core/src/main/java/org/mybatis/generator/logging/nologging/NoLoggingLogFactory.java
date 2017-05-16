package org.mybatis.generator.logging.nologging;

import org.mybatis.generator.logging.AbstractLogFactory;
import org.mybatis.generator.logging.Log;

public class NoLoggingLogFactory implements AbstractLogFactory {
    @Override
    public Log getLog(Class<?> clazz) {
        return new NoLoggingImpl(clazz);
    }
}