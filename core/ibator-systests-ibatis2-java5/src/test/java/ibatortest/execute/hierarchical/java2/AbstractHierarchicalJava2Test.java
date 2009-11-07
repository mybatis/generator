package ibatortest.execute.hierarchical.java2;

import ibatortest.AbstractTest;
import ibatortest.generated.hierarchical.java2.dao.AwfulTableDAO;
import ibatortest.generated.hierarchical.java2.dao.AwfulTableDAOImpl;
import ibatortest.generated.hierarchical.java2.dao.FieldsblobsDAO;
import ibatortest.generated.hierarchical.java2.dao.FieldsblobsDAOImpl;
import ibatortest.generated.hierarchical.java2.dao.FieldsonlyDAO;
import ibatortest.generated.hierarchical.java2.dao.FieldsonlyDAOImpl;
import ibatortest.generated.hierarchical.java2.dao.PkblobsDAO;
import ibatortest.generated.hierarchical.java2.dao.PkblobsDAOImpl;
import ibatortest.generated.hierarchical.java2.dao.PkfieldsDAO;
import ibatortest.generated.hierarchical.java2.dao.PkfieldsDAOImpl;
import ibatortest.generated.hierarchical.java2.dao.PkfieldsblobsDAO;
import ibatortest.generated.hierarchical.java2.dao.PkfieldsblobsDAOImpl;
import ibatortest.generated.hierarchical.java2.dao.PkonlyDAO;
import ibatortest.generated.hierarchical.java2.dao.PkonlyDAOImpl;

public abstract class AbstractHierarchicalJava2Test extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "ibatortest/execute/hierarchical/java2/SqlMapConfig.xml", null);
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
