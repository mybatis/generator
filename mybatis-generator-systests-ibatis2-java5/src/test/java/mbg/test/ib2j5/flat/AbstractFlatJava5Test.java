package mbg.test.ib2j5.flat;

import mbg.test.ib2j5.AbstractTest;
import mbg.test.ib2j5.generated.flat.dao.AwfulTableDAO;
import mbg.test.ib2j5.generated.flat.dao.AwfulTableDAOImpl;
import mbg.test.ib2j5.generated.flat.dao.FieldsblobsDAO;
import mbg.test.ib2j5.generated.flat.dao.FieldsblobsDAOImpl;
import mbg.test.ib2j5.generated.flat.dao.FieldsonlyDAO;
import mbg.test.ib2j5.generated.flat.dao.FieldsonlyDAOImpl;
import mbg.test.ib2j5.generated.flat.dao.PkblobsDAO;
import mbg.test.ib2j5.generated.flat.dao.PkblobsDAOImpl;
import mbg.test.ib2j5.generated.flat.dao.PkfieldsDAO;
import mbg.test.ib2j5.generated.flat.dao.PkfieldsDAOImpl;
import mbg.test.ib2j5.generated.flat.dao.PkfieldsblobsDAO;
import mbg.test.ib2j5.generated.flat.dao.PkfieldsblobsDAOImpl;
import mbg.test.ib2j5.generated.flat.dao.PkonlyDAO;
import mbg.test.ib2j5.generated.flat.dao.PkonlyDAOImpl;

public abstract class AbstractFlatJava5Test extends AbstractTest {

    public void setUp() throws Exception {
        super.setUp();
        initSqlMapClient("mbg/test/ib2j5/flat/SqlMapConfig.xml", null);
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
