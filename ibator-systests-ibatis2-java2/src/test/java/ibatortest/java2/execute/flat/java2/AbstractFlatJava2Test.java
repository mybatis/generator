package ibatortest.java2.execute.flat.java2;

import ibatortest.java2.AbstractTest;
import ibatortest.java2.generated.flat.java2.dao.AwfulTableDAO;
import ibatortest.java2.generated.flat.java2.dao.AwfulTableDAOImpl;
import ibatortest.java2.generated.flat.java2.dao.FieldsblobsDAO;
import ibatortest.java2.generated.flat.java2.dao.FieldsblobsDAOImpl;
import ibatortest.java2.generated.flat.java2.dao.FieldsonlyDAO;
import ibatortest.java2.generated.flat.java2.dao.FieldsonlyDAOImpl;
import ibatortest.java2.generated.flat.java2.dao.PkblobsDAO;
import ibatortest.java2.generated.flat.java2.dao.PkblobsDAOImpl;
import ibatortest.java2.generated.flat.java2.dao.PkfieldsDAO;
import ibatortest.java2.generated.flat.java2.dao.PkfieldsDAOImpl;
import ibatortest.java2.generated.flat.java2.dao.PkfieldsblobsDAO;
import ibatortest.java2.generated.flat.java2.dao.PkfieldsblobsDAOImpl;
import ibatortest.java2.generated.flat.java2.dao.PkonlyDAO;
import ibatortest.java2.generated.flat.java2.dao.PkonlyDAOImpl;

public abstract class AbstractFlatJava2Test extends AbstractTest {
    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient("ibatortest/java2/execute/flat/java2/SqlMapConfig.xml", null);
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
