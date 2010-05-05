package ibatortest.execute.flat.java2;

import ibatortest.AbstractTest;
import ibatortest.generated.flat.java2.dao.AwfulTableDAO;
import ibatortest.generated.flat.java2.dao.AwfulTableDAOImpl;
import ibatortest.generated.flat.java2.dao.FieldsblobsDAO;
import ibatortest.generated.flat.java2.dao.FieldsblobsDAOImpl;
import ibatortest.generated.flat.java2.dao.FieldsonlyDAO;
import ibatortest.generated.flat.java2.dao.FieldsonlyDAOImpl;
import ibatortest.generated.flat.java2.dao.PkblobsDAO;
import ibatortest.generated.flat.java2.dao.PkblobsDAOImpl;
import ibatortest.generated.flat.java2.dao.PkfieldsDAO;
import ibatortest.generated.flat.java2.dao.PkfieldsDAOImpl;
import ibatortest.generated.flat.java2.dao.PkfieldsblobsDAO;
import ibatortest.generated.flat.java2.dao.PkfieldsblobsDAOImpl;
import ibatortest.generated.flat.java2.dao.PkonlyDAO;
import ibatortest.generated.flat.java2.dao.PkonlyDAOImpl;

public abstract class AbstractFlatJava2Test extends AbstractTest {
    
    public void setUp() throws Exception {
        super.setUp();
        initSqlMapClient("ibatortest/execute/flat/java2/SqlMapConfig.xml", null);
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
