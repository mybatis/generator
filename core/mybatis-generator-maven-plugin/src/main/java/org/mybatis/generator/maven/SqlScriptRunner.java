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
package org.mybatis.generator.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

/**
 * This class is used to execute an SQL script before a code generation
 * run if necessary.  Note that this class mainly exists to support the
 * MyBatis Generator build.  It is intentionally not documented and not
 * supported.
 * 
 * @author Jeff Butler
 */
public class SqlScriptRunner {
    private String driver;
    private String url;
    private String userid;
    private String password;
    private String sourceFile;
    private Log log;

    public SqlScriptRunner(String sourceFile, String driver, String url,
            String userId, String password) throws MojoExecutionException {
        
        if (!StringUtility.stringHasValue(sourceFile)) {
            throw new MojoExecutionException("SQL script file is required");
        }
        
        if (!StringUtility.stringHasValue(driver)) {
            throw new MojoExecutionException("JDBC Driver is required");
        }
        
        if (!StringUtility.stringHasValue(url)) {
            throw new MojoExecutionException("JDBC URL is required");
        }
        
        this.sourceFile = sourceFile;
        this.driver = driver;
        this.url = url;
        this.userid = userId;
        this.password = password;
    }

    public void executeScript() throws MojoExecutionException {

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
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException("Class not found: " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException("File note found: " + sourceFile);
        } catch (SQLException e) {
            throw new MojoExecutionException("SqlException: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new MojoExecutionException("IOException: " + e.getMessage(), e);
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

            if (!StringUtility.stringHasValue(line)) {
                continue;
            }

            if (line.endsWith(";")) { //$NON-NLS-1$
                sb.append(' ');
                sb.append(line.substring(0, line.length() - 1));
                break;
            } else {
                sb.append(' ');
                sb.append(line);
            }
        }

        String s = sb.toString().trim();

        if (s.length() > 0) {
            log.debug((Messages.getString("Progress.13", s))); //$NON-NLS-1$
        }

        return s.length() > 0 ? s : null;
    }

    public void setLog(Log log) {
        this.log = log;
    }
    
    private BufferedReader getScriptReader() throws MojoExecutionException, FileNotFoundException {
        BufferedReader answer;
        
        if (sourceFile.startsWith("classpath:")) {
            String resource = sourceFile.substring("classpath:".length());
            InputStream is = 
                Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (is == null) {
                throw new MojoExecutionException("SQL script file does not exist: " + resource);
            }
            answer = new BufferedReader(new InputStreamReader(is));
        } else {
            File file = new File(sourceFile);
            if (!file.exists()) {
                throw new MojoExecutionException("SQL script file does not exist");
            }
            answer = new BufferedReader(new FileReader(file));
        }
        
        return answer;
    }
}
