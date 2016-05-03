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
package org.mybatis.generator;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used to execute an SQL script before a code generation
 * run.
 * 
 * @author Jeff Butler
 */
public class SqlScriptRunner {
    private String driver;
    private String url;
    private String userid;
    private String password;
    private InputStream sourceFile;

    public SqlScriptRunner(InputStream sourceFile, String driver, String url,
            String userId, String password) throws Exception {
        
        if (!stringHasValue(driver)) {
            throw new Exception("JDBC Driver is required");
        }
        
        if (!stringHasValue(url)) {
            throw new Exception("JDBC URL is required");
        }
        
        this.sourceFile = sourceFile;
        this.driver = driver;
        this.url = url;
        this.userid = userId;
        this.password = password;
    }

    public void executeScript() throws Exception {

        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userid, password);

            Statement statement = connection.createStatement();

            BufferedReader br = new BufferedReader(new InputStreamReader(sourceFile));

            String sql;

            while ((sql = readStatement(br)) != null) {
                statement.execute(sql);
            }

            closeStatement(statement);
            connection.commit();
            br.close();
        } finally {
            closeConnection(connection);
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
                ;
            }
        }
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
                ;
            }
        }
    }

    private String readStatement(BufferedReader br) throws IOException {
        StringBuffer sb = new StringBuffer();

        String line;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("--")) { //$NON-NLS-1$
                continue;
            }

            if (!stringHasValue(line)) {
                continue;
            }

            if (line.endsWith(";")) { //$NON-NLS-1$
                sb.append(line.substring(0, line.length() - 1));
                break;
            } else {
                sb.append(' ');
                sb.append(line);
            }
        }

        String s = sb.toString().trim();

        return s.length() > 0 ? s : null;
    }
}
