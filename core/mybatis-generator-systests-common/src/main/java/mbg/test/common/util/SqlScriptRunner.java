/*
 *    Copyright 2006-2025 the original author or authors.
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
package mbg.test.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used to execute an SQL script before a code generation
 * run if necessary.  Note that this class mainly exists to support the
 * MyBatis generator build.  It is intentionally not documented and not
 * supported.
 *
 * @author Jeff Butler
 */
public class SqlScriptRunner {
    private String driver;
    private final String url;
    private final String userid;
    private String password;
    private final String sourceFile;

    public SqlScriptRunner(String sourceFile, String driver, String url,
            String userId, String password) throws Exception {

        if (sourceFile == null || sourceFile.isEmpty()) {
            throw new Exception("SQL script file is required");
        }

        if (driver == null || driver.isEmpty()) {
            throw new Exception("JDBC Driver is required");
        }

        if (url == null || url.isEmpty()) {
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

            BufferedReader br = getScriptReader();

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
            }
        }
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    private String readStatement(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("--")) { //$NON-NLS-1$
                continue;
            }

            if (line.isEmpty()) {
                continue;
            }

            if (line.endsWith(";")) { //$NON-NLS-1$
                sb.append(line, 0, line.length() - 1);
                break;
            } else {
                sb.append(' ');
                sb.append(line);
            }
        }

        String s = sb.toString().trim();

        return s.isEmpty() ? null : s;
    }

    private BufferedReader getScriptReader() throws Exception {
        BufferedReader answer;

        if (sourceFile.startsWith("classpath:")) {
            String resource = sourceFile.substring("classpath:".length());
            InputStream is =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (is == null) {
                throw new Exception("SQL script file does not exist: " + resource);
            }
            answer = new BufferedReader(new InputStreamReader(is));
        } else {
            Path file = Path.of(sourceFile);
            if (Files.notExists(file)) {
                throw new Exception("SQL script file does not exist");
            }
            answer = Files.newBufferedReader(file);
        }

        return answer;
    }
}
