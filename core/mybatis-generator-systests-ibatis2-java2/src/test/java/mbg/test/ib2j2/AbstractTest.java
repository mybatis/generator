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
package mbg.test.ib2j2;

import java.io.Reader;
import java.util.Properties;

import junit.framework.TestCase;
import mbg.test.common.util.TestUtilities;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * @author Jeff Butler
 *
 */
public abstract class AbstractTest extends TestCase {

    private SqlMapClient sqlMapClient;

    protected void initSqlMapClient(String configFile, Properties props) throws Exception {
        Reader reader = Resources.getResourceAsReader(configFile);
        sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader, props);
        reader.close();
    }

    protected void setUp() throws Exception {
        TestUtilities.createDatabase();
    }

    protected SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }
}
