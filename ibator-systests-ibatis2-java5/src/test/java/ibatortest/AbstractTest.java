/*
 *  Copyright 2006 The Apache Software Foundation
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

package ibatortest;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import junit.framework.TestCase;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * @author Jeff Butler
 *
 */
public abstract class AbstractTest extends TestCase {

    private SqlMapClient sqlMapClient;
    private static DateFormat dateOnlyFormat = SimpleDateFormat.getDateInstance();
    private static DateFormat timeOnlyFormat = SimpleDateFormat.getTimeInstance();

    protected void initSqlMapClient(String configFile, Properties props) throws Exception {
        Reader reader = Resources.getResourceAsReader(configFile);
        sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader, props);
        reader.close();
    }

    @Override
    protected void setUp() throws Exception {
        Connection conn = null;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa",
                    "");

            Reader reader = Resources.getResourceAsReader("CreateDB.sql");

            ScriptRunner runner = new ScriptRunner(conn, true, false);
            runner.setLogWriter(null);
            runner.setErrorLogWriter(null);
            runner.runScript(reader);
            conn.commit();

            reader.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    protected byte[] generateRandomBlob() {
        Random random = new Random(System.currentTimeMillis());
        
        byte[] answer = new byte[256];
        
        random.nextBytes(answer);
        
        return answer;
    }

    protected boolean blobsAreEqual(byte[] blob1, byte[] blob2) {
        if (blob1 == null) {
            return blob2 == null;
        }
        
        if (blob2 == null) {
            return blob1 == null;
        }
        
        boolean rc = blob1.length == blob2.length;
        
        if (rc) {
            for (int i = 0; i < blob1.length; i++) {
                if (blob1[i] != blob2[i]) {
                    rc = false;
                    break;
                }
            }
        }
        
        return rc;
    }
    
    protected boolean datesAreEqual(Date date1, Date date2) {
        if (date1 == null) {
            return date2 == null;
        }
        
        if (date2 == null) {
            return date1 == null;
        }
        
        return dateOnlyFormat.format(date1).equals(dateOnlyFormat.format(date2));
        
    }
    
    protected boolean timesAreEqual(Date date1, Date date2) {
        if (date1 == null) {
            return date2 == null;
        }
        
        if (date2 == null) {
            return date1 == null;
        }
        
        return timeOnlyFormat.format(date1).equals(timeOnlyFormat.format(date2));
        
    }

    protected SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }
}
