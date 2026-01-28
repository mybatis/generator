/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.exception.InternalException;

/**
 * This class assumes that classes are cached elsewhere for performance reasons,
 * but also to make sure that any native libraries are only loaded one time.
 * This avoids the dreaded UnsatisfiedLinkError library loaded in another
 * classloader.
 *
 * @author Jeff Butler
 */
public class JDBCConnectionFactory {

    private final JDBCConnectionConfiguration config;

    /**
     * This constructor is called when there is a JDBCConnectionConfiguration
     * specified in the configuration.
     *
     * @param config
     *            the configuration
     */
    public JDBCConnectionFactory(JDBCConnectionConfiguration config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {

        Properties props = new Properties();

        config.getUserId().ifPresent(s -> props.setProperty("user", s)); //$NON-NLS-1$
        config.getPassword().ifPresent(s -> props.setProperty("password", s)); //$NON-NLS-1$
        props.putAll(config.getProperties());

        Driver driver = getDriver();
        Connection conn = driver.connect(config.getConnectionURL(), props);

        if (conn == null) {
            throw new SQLException(getString("RuntimeError.7")); //$NON-NLS-1$
        }

        return conn;
    }

    private Driver getDriver() {
        Driver driver;

        try {
            Class<Driver> clazz = ObjectFactory.externalClassForName(config.getDriverClass(), Driver.class);
            driver = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new InternalException(getString("RuntimeError.8"), e); //$NON-NLS-1$
        }

        return driver;
    }
}
