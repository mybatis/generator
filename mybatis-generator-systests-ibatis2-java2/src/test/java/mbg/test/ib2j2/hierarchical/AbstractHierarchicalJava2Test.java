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
