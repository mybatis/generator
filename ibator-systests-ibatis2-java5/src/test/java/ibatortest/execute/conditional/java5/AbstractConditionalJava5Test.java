package ibatortest.execute.conditional.java5;

import ibatortest.AbstractTest;
import ibatortest.generated.conditional.java5.dao.AwfulTableDAO;
import ibatortest.generated.conditional.java5.dao.AwfulTableDAOImpl;
import ibatortest.generated.conditional.java5.dao.FieldsblobsDAO;
import ibatortest.generated.conditional.java5.dao.FieldsblobsDAOImpl;
import ibatortest.generated.conditional.java5.dao.FieldsonlyDAO;
import ibatortest.generated.conditional.java5.dao.FieldsonlyDAOImpl;
import ibatortest.generated.conditional.java5.dao.PkblobsDAO;
import ibatortest.generated.conditional.java5.dao.PkblobsDAOImpl;
import ibatortest.generated.conditional.java5.dao.PkfieldsDAO;
import ibatortest.generated.conditional.java5.dao.PkfieldsDAOImpl;
import ibatortest.generated.conditional.java5.dao.PkfieldsblobsDAO;
import ibatortest.generated.conditional.java5.dao.PkfieldsblobsDAOImpl;
import ibatortest.generated.conditional.java5.dao.PkonlyDAO;
import ibatortest.generated.conditional.java5.dao.PkonlyDAOImpl;

public abstract class AbstractConditionalJava5Test extends AbstractTest {

    public void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "ibatortest/execute/conditional/java5/SqlMapConfig.xml", null);
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
