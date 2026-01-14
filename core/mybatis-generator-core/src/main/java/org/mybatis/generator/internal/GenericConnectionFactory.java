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
import java.util.Objects;
import java.util.Properties;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.ConnectionFactory;

public class GenericConnectionFactory implements ConnectionFactory {
    private @Nullable String connectionURL;
    private @Nullable String driverClass;
    private final Properties otherProperties = new Properties();

    @Override
    public Connection getConnection() throws SQLException {

        Properties props = new Properties();

        props.putAll(otherProperties);

        Driver driver = getDriver();
        Connection conn = driver.connect(connectionURL, props);

        if (conn == null) {
            throw new SQLException(getString("RuntimeError.7")); //$NON-NLS-1$
        }

        return conn;
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        connectionURL = properties.getProperty("connectionURL"); //$NON-NLS-1$
        driverClass = properties.getProperty("driverClass"); //$NON-NLS-1$

        otherProperties.putAll(properties);

        // remove all the properties that we have specific attributes for
        otherProperties.remove("connectionURL"); //$NON-NLS-1$
        otherProperties.remove("driverClass"); //$NON-NLS-1$
    }

    private Driver getDriver() {
        Driver driver;

        try {
            Class<Driver> clazz = ObjectFactory.externalClassForName(Objects.requireNonNull(driverClass), Driver.class);
            driver = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.8"), e); //$NON-NLS-1$
        }

        return driver;
    }
}
