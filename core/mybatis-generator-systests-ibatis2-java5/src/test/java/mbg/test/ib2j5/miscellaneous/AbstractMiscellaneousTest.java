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
package mbg.test.ib2j5.miscellaneous;

import mbg.test.ib2j5.AbstractTest;
import mbg.test.ib2j5.generated.miscellaneous.dao.MyObjectDAO;
import mbg.test.ib2j5.generated.miscellaneous.dao.RegexrenameDAO;
import mbg.test.ib2j5.generated.miscellaneous.dao.impl.MyObjectDAOImpl;
import mbg.test.ib2j5.generated.miscellaneous.dao.impl.RegexrenameDAOImpl;

public abstract class AbstractMiscellaneousTest extends AbstractTest {

    public void setUp() throws Exception {
        super.setUp();
        initSqlMapClient("mbg/test/ib2j5/miscellaneous/SqlMapConfig.xml",
                null);
    }
    
    protected MyObjectDAO getMyObjectDAO() {
        MyObjectDAOImpl dao = new MyObjectDAOImpl(getSqlMapClient());
        return dao;
    }
    
    protected RegexrenameDAO getRegexrenameDAO() {
        RegexrenameDAOImpl dao = new RegexrenameDAOImpl(getSqlMapClient());
        return dao;
    }
}
