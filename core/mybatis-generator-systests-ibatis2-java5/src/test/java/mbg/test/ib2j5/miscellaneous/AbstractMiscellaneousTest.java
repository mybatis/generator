package mbg.test.ib2j5.miscellaneous;

import mbg.test.ib2j5.AbstractTest;
import mbg.test.ib2j5.generated.miscellaneous.dao.MyObjectDAO;
import mbg.test.ib2j5.generated.miscellaneous.dao.RegexrenameDAO;
import mbg.test.ib2j5.generated.miscellaneous.dao.impl.MyObjectDAOImpl;
import mbg.test.ib2j5.generated.miscellaneous.dao.impl.RegexrenameDAOImpl;

public abstract class AbstractMiscellaneousTest extends AbstractTest {

    public void setUp() throws Exception {
        super.setUp();
        initSqlMapClient("mbg/test/ib2j5/miscellaneous/SqlMapConfig.xml",
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
