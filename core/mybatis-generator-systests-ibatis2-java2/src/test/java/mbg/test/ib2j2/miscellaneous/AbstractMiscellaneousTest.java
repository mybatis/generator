package mbg.test.ib2j2.miscellaneous;

import mbg.test.ib2j2.AbstractTest;
import mbg.test.ib2j2.generated.miscellaneous.dao.MyObjectDAO;
import mbg.test.ib2j2.generated.miscellaneous.dao.MyObjectDAOImpl;
import mbg.test.ib2j2.generated.miscellaneous.dao.RegexrenameDAO;
import mbg.test.ib2j2.generated.miscellaneous.dao.RegexrenameDAOImpl;

public abstract class AbstractMiscellaneousTest extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "mbg/test/ib2j2/miscellaneous/SqlMapConfig.xml", null);
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
