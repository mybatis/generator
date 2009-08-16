/*
 *  Copyright 2007 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.ibatis.abator.ant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.abator.internal.util.StringUtility;
import org.apache.ibatis.abator.internal.util.messages.Messages;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * This task executes an SQL script.  It is used during the Abator build
 * because the built in Ant SQL task treats some of the columns in "awful table"
 * as properties.
 * 
 * This task is very simplistic and not intended for general use.
 * 
 * @author Jeff Butler
 *
 */
public class SqlScriptRunnerTask extends Task {
    
    private String driver;
    private String url;
    private String userid;
    private String password;
    private String src;

    public void execute() throws BuildException {
        
        Connection connection = null;
        
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userid, password);
            
            Statement statement = connection.createStatement();
            
            BufferedReader br = new BufferedReader(new FileReader(src));
            
            String sql;
            
            while ((sql = readStatement(br)) != null) {
                statement.execute(sql);
            }
            
            closeStatement(statement);
            connection.commit();
            br.close();
        } catch (Exception e) {
            throw new BuildException(e.getMessage());
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
                sb.append(line.substring(0, line.length() - 1));
                break;
            } else {
                sb.append(line);
            }
        }
        
        String s = sb.toString().trim();

        if (s.length() > 0) {
            log(Messages.getString("Progress.13", s), Project.MSG_DEBUG); //$NON-NLS-1$
        }
        
        return s.length() > 0 ? s : null;
    }
}
