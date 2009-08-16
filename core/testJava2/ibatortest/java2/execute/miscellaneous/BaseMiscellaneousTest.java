package ibatortest.java2.execute.miscellaneous;

import ibatortest.java2.BaseTest;
import ibatortest.java2.generated.miscellaneous.dao.MyObjectDAO;
import ibatortest.java2.generated.miscellaneous.dao.MyObjectDAOImpl;

public class BaseMiscellaneousTest extends BaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "ibatortest/java2/execute/miscellaneous/SqlMapConfig.xml", null);
    }
    
    protected MyObjectDAO getMyObjectDAO() {
        MyObjectDAOImpl dao = new MyObjectDAOImpl(getSqlMapClient());
        return dao;
    }
}
