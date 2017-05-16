package org.mybatis.generator.logging.jdk14;

import org.mybatis.generator.logging.AbstractLogFactory;
import org.mybatis.generator.logging.Log;

public class Jdk14LoggingLogFactory implements AbstractLogFactory {
    @Override
    public Log getLog(Class<?> clazz) {
        return new Jdk14LoggingImpl(clazz);
    }
}