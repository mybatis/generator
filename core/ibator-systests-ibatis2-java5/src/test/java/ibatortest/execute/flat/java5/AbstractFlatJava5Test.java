package ibatortest.execute.flat.java5;

import ibatortest.AbstractTest;
import ibatortest.generated.flat.java5.dao.AwfulTableDAO;
import ibatortest.generated.flat.java5.dao.AwfulTableDAOImpl;
import ibatortest.generated.flat.java5.dao.FieldsblobsDAO;
import ibatortest.generated.flat.java5.dao.FieldsblobsDAOImpl;
import ibatortest.generated.flat.java5.dao.FieldsonlyDAO;
import ibatortest.generated.flat.java5.dao.FieldsonlyDAOImpl;
import ibatortest.generated.flat.java5.dao.PkblobsDAO;
import ibatortest.generated.flat.java5.dao.PkblobsDAOImpl;
import ibatortest.generated.flat.java5.dao.PkfieldsDAO;
import ibatortest.generated.flat.java5.dao.PkfieldsDAOImpl;
import ibatortest.generated.flat.java5.dao.PkfieldsblobsDAO;
import ibatortest.generated.flat.java5.dao.PkfieldsblobsDAOImpl;
import ibatortest.generated.flat.java5.dao.PkonlyDAO;
import ibatortest.generated.flat.java5.dao.PkonlyDAOImpl;

public abstract class AbstractFlatJava5Test extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient("ibatortest/execute/flat/java5/SqlMapConfig.xml", null);
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
