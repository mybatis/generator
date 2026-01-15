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
package org.mybatis.generator.codegen;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.sql.Connection;
import java.sql.SQLException;

import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.exception.InternalException;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.ObjectFactory;

public final class ConnectionUtility {
    private ConnectionUtility() {
        // utility class - no instances
    }

    /**
     * This method creates a new JDBC connection from the values specified in the configuration file.
     *
     * <p>If you call this method, then you are responsible for closing the connection when you are done with it.
     * The best practice is to call the method in a try with resources to ensure proper cleanup.
     *
     * @param context the context containing the connection configuration
     * @return a new connection created from the values in the configuration file
     *
     * @throws SQLException if any error occurs while creating the connection
     */
    public static Connection getConnection(Context context) throws SQLException {
        // if both configs are null, it is an internal error - we should have caught that with validation

        JDBCConnectionConfiguration jdbcConfig = context.getJDBCConnectionConfiguration();
        if (jdbcConfig != null) {
            return new JDBCConnectionFactory(jdbcConfig).getConnection();
        } else {
            ConnectionFactoryConfiguration config = context.getConnectionFactoryConfiguration();
            if (config == null) {
                throw new InternalException(getString("RuntimeError.23", context.getId())); //$NON-NLS-1$
            }

            return ObjectFactory.createConnectionFactory(config).getConnection();
        }
    }
}
