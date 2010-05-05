package ibatortest.execute.miscellaneous;

import ibatortest.AbstractTest;
import ibatortest.generated.miscellaneous.dao.MyObjectDAO;
import ibatortest.generated.miscellaneous.dao.RegexrenameDAO;
import ibatortest.generated.miscellaneous.dao.impl.MyObjectDAOImpl;
import ibatortest.generated.miscellaneous.dao.impl.RegexrenameDAOImpl;

public abstract class AbstractMiscellaneousTest extends AbstractTest {

    public void setUp() throws Exception {
        super.setUp();
        initSqlMapClient("ibatortest/execute/miscellaneous/SqlMapConfig.xml",
                null);
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
