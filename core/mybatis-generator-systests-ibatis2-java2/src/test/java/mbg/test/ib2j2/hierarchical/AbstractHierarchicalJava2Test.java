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
package mbg.test.ib2j2.hierarchical;

import mbg.test.ib2j2.AbstractTest;
import mbg.test.ib2j2.generated.hierarchical.dao.AwfulTableDAO;
import mbg.test.ib2j2.generated.hierarchical.dao.AwfulTableDAOImpl;
import mbg.test.ib2j2.generated.hierarchical.dao.FieldsblobsDAO;
import mbg.test.ib2j2.generated.hierarchical.dao.FieldsblobsDAOImpl;
import mbg.test.ib2j2.generated.hierarchical.dao.PkblobsDAO;
import mbg.test.ib2j2.generated.hierarchical.dao.PkblobsDAOImpl;
import mbg.test.ib2j2.generated.hierarchical.dao.PkfieldsDAO;
import mbg.test.ib2j2.generated.hierarchical.dao.PkfieldsDAOImpl;
import mbg.test.ib2j2.generated.hierarchical.dao.PkfieldsblobsDAO;
import mbg.test.ib2j2.generated.hierarchical.dao.PkfieldsblobsDAOImpl;
import mbg.test.ib2j2.generated.hierarchical.dao.PkonlyDAO;
import mbg.test.ib2j2.generated.hierarchical.dao.PkonlyDAOImpl;
import mbg.test.ib2j2.generated.hierarchical.dao.subpackage.FieldsonlyDAO;
import mbg.test.ib2j2.generated.hierarchical.dao.subpackage.FieldsonlyDAOImpl;

public abstract class AbstractHierarchicalJava2Test extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "mbg/test/ib2j2/hierarchical/SqlMapConfig.xml",
                null);
    }
    
    protected FieldsonlyDAO getFieldsonlyDAO() {
        FieldsonlyDAOImpl dao = new FieldsonlyDAOImpl(getSqlMapClient());
        return dao;
    }

    protected PkonlyDAO getPkonlyDAO() {
        PkonlyDAOImpl dao = new PkonlyDAOImpl(getSqlMapClient());
        return dao;
    }

    protected PkfieldsDAO getPkfieldsDAO() {
        PkfieldsDAOImpl dao = new PkfieldsDAOImpl(getSqlMapClient());
        return dao;
    }

    protected PkblobsDAO getPkblobsDAO() {
        PkblobsDAOImpl dao = new PkblobsDAOImpl(getSqlMapClient());
        return dao;
    }

    protected PkfieldsblobsDAO getPkfieldsblobsDAO() {
        PkfieldsblobsDAOImpl dao = new PkfieldsblobsDAOImpl(getSqlMapClient());
        return dao;
    }

    protected FieldsblobsDAO getFieldsblobsDAO() {
        FieldsblobsDAOImpl dao = new FieldsblobsDAOImpl(getSqlMapClient());
        return dao;
    }

    protected AwfulTableDAO getAwfulTableDAO() {
        AwfulTableDAOImpl dao = new AwfulTableDAOImpl(getSqlMapClient());
        return dao;
    }
}
