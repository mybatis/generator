package ibatortest.java2.execute.miscellaneous;

import ibatortest.java2.AbstractTest;
import ibatortest.java2.generated.miscellaneous.dao.MyObjectDAO;
import ibatortest.java2.generated.miscellaneous.dao.MyObjectDAOImpl;
import ibatortest.java2.generated.miscellaneous.dao.RegexrenameDAO;
import ibatortest.java2.generated.miscellaneous.dao.RegexrenameDAOImpl;

public abstract class AbstractMiscellaneousTest extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "ibatortest/java2/execute/miscellaneous/SqlMapConfig.xml", null);
    }
    
    protected MyObjectDAO getMyObjectDAO() {
        MyObjectDAOImpl dao = new MyObjectDAOImpl(getSqlMapClient());
        return dao;
    }
    
    protected RegexrenameDAO getRegexrenameDAO() {
        RegexrenameDAOImpl dao = new RegexrenameDAOImpl(getSqlMapClient());
        return dao;
    }
}
