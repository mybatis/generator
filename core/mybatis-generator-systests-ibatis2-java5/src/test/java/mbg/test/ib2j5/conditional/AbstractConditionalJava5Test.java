/**
 *    Copyright 2006-2015 the original author or authors.
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
package mbg.test.ib2j5.conditional;

import mbg.test.ib2j5.AbstractTest;
import mbg.test.ib2j5.generated.conditional.dao.AwfulTableDAO;
import mbg.test.ib2j5.generated.conditional.dao.AwfulTableDAOImpl;
import mbg.test.ib2j5.generated.conditional.dao.FieldsblobsDAO;
import mbg.test.ib2j5.generated.conditional.dao.FieldsblobsDAOImpl;
import mbg.test.ib2j5.generated.conditional.dao.FieldsonlyDAO;
import mbg.test.ib2j5.generated.conditional.dao.FieldsonlyDAOImpl;
import mbg.test.ib2j5.generated.conditional.dao.PkblobsDAO;
import mbg.test.ib2j5.generated.conditional.dao.PkblobsDAOImpl;
import mbg.test.ib2j5.generated.conditional.dao.PkfieldsDAO;
import mbg.test.ib2j5.generated.conditional.dao.PkfieldsDAOImpl;
import mbg.test.ib2j5.generated.conditional.dao.PkfieldsblobsDAO;
import mbg.test.ib2j5.generated.conditional.dao.PkfieldsblobsDAOImpl;
import mbg.test.ib2j5.generated.conditional.dao.PkonlyDAO;
import mbg.test.ib2j5.generated.conditional.dao.PkonlyDAOImpl;

public abstract class AbstractConditionalJava5Test extends AbstractTest {

    public void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "mbg/test/ib2j5/conditional/SqlMapConfig.xml", null);
    }
    
    protected FieldsonlyDAO getFieldsonlyDAO() {
        FieldsonlyDAOImpl dao = new FieldsonlyDAOImpl();
        dao.setSqlMapClient(getSqlMapClient());
        return dao;
    }
    
    protected PkonlyDAO getPkonlyDAO() {
        PkonlyDAOImpl dao = new PkonlyDAOImpl();
        dao.setSqlMapClient(getSqlMapClient());
        return dao;
    }

    protected PkfieldsDAO getPkfieldsDAO() {
        PkfieldsDAOImpl dao = new PkfieldsDAOImpl();
        dao.setSqlMapClient(getSqlMapClient());
        return dao;
    }

    protected PkblobsDAO getPkblobsDAO() {
        PkblobsDAOImpl dao = new PkblobsDAOImpl();
        dao.setSqlMapClient(getSqlMapClient());
        return dao;
    }

    protected PkfieldsblobsDAO getPkfieldsblobsDAO() {
        PkfieldsblobsDAOImpl dao = new PkfieldsblobsDAOImpl();
        dao.setSqlMapClient(getSqlMapClient());
        return dao;
    }

    protected FieldsblobsDAO getFieldsblobsDAO() {
        FieldsblobsDAOImpl dao = new FieldsblobsDAOImpl();
        dao.setSqlMapClient(getSqlMapClient());
        return dao;
    }

    protected AwfulTableDAO getAwfulTableDAO() {
        AwfulTableDAOImpl dao = new AwfulTableDAOImpl();
        dao.setSqlMapClient(getSqlMapClient());
        return dao;
    }
}
