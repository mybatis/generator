package org.mybatis.generator.maven;

import org.apache.maven.plugin.Mojo;
import org.mybatis.generator.logging.AbstractLogFactory;
import org.mybatis.generator.logging.Log;

public class MavenLogFactory implements AbstractLogFactory {

    private final MavenLogImpl logImplementation;

    MavenLogFactory(Mojo mojo) {
        logImplementation = new MavenLogImpl(mojo.getLog());
    }

    public Log getLog(Class<?> aClass) {
        return logImplementation;
    }
}
