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
package org.mybatis.generator.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public interface ConnectionFactory {

    /**
     * Should return a connection to the database in use for this context.
     * The generator will call this method only one time for each context.
     * The generator will close the connection.
     * 
     * @return the connection
     * @throws SQLException if there is some error obtaining the connection
     */
    Connection getConnection() throws SQLException;

    /**
     * Adds properties for this instance from any properties configured in the
     * ConnectionFactory.
     * 
     * <p>This method will be called before any of the get methods.
     * 
     * @param properties
     *            All properties from the configuration
     */
    void addConfigurationProperties(Properties properties);
}
