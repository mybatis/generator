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
