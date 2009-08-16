package abatortest.java2.execute.conditional.java2;

import java.sql.SQLException;
import java.util.List;

import abatortest.java2.BaseTest;
import abatortest.java2.generated.conditional.java2.dao.AwfulTableDAO;
import abatortest.java2.generated.conditional.java2.dao.AwfulTableDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkblobsDAO;
import abatortest.java2.generated.conditional.java2.dao.PkblobsDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsDAO;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsblobsDAO;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsblobsDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkonlyDAO;
import abatortest.java2.generated.conditional.java2.dao.PkonlyDAOImpl;
import abatortest.java2.generated.conditional.java2.model.AwfulTable;
import abatortest.java2.generated.conditional.java2.model.AwfulTableExample;
import abatortest.java2.generated.conditional.java2.model.Pkblobs;
import abatortest.java2.generated.conditional.java2.model.PkblobsExample;
import abatortest.java2.generated.conditional.java2.model.Pkfields;
import abatortest.java2.generated.conditional.java2.model.PkfieldsExample;
import abatortest.java2.generated.conditional.java2.model.PkfieldsKey;
import abatortest.java2.generated.conditional.java2.model.Pkfieldsblobs;
import abatortest.java2.generated.conditional.java2.model.PkfieldsblobsExample;
import abatortest.java2.generated.conditional.java2.model.PkfieldsblobsKey;
import abatortest.java2.generated.conditional.java2.model.PkonlyExample;
import abatortest.java2.generated.conditional.java2.model.PkonlyKey;

public class DeleteByPrimaryKeyTests extends BaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "abatortest/java2/execute/conditional/java2/SqlMapConfig.xml",
                null);
    }

    public void testPKOnlyDeleteByPrimaryKey() {
        PkonlyDAO dao = new PkonlyDAOImpl(sqlMapClient);
    
        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(new Integer(1));
            key.setSeqNum(new Integer(3));
            dao.insert(key);
    
            key = new PkonlyKey();
            key.setId(new Integer(5));
            key.setSeqNum(new Integer(6));
            dao.insert(key);
    
            PkonlyExample example = new PkonlyExample();
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
    
            key = new PkonlyKey();
            key.setId(new Integer(5));
            key.setSeqNum(new Integer(6));
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);
    
            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKfieldsDeleteByPrimaryKey() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
    
            dao.insert(record);
    
            PkfieldsKey key = new PkfieldsKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));
    
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);
    
            PkfieldsExample example = new PkfieldsExample();
            List answer = dao.selectByExample(example);
            assertEquals(0, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsDeleteByPrimaryKey() {
        PkblobsDAO dao = new PkblobsDAOImpl(sqlMapClient);
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            PkblobsExample example = new PkblobsExample();
            List answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
    
            int rows = dao.deleteByPrimaryKey(new Integer(3));
            assertEquals(1, rows);
    
            example = new PkblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(0, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsDeleteByPrimaryKey() {
        PkfieldsblobsDAO dao = new PkfieldsblobsDAOImpl(sqlMapClient);
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkfieldsblobs();
            record.setId1(new Integer(5));
            record.setId2(new Integer(6));
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());
    
            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(new Integer(5));
            key.setId2(new Integer(6));
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);
    
            example = new PkfieldsblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableDeleteByPrimaryKey() {
        AwfulTableDAO dao = new AwfulTableDAOImpl(sqlMapClient);
    
        try {
            AwfulTable record = new AwfulTable();
            record.seteMail("fred@fred.com");
            record.setEmailaddress("alsofred@fred.com");
            record.setFirstFirstName("fred1");
            record.setFourthFirstName("fred4");
            record.setFrom("from field");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            record.setId5(new Integer(5));
            record.setId6(new Integer(6));
            record.setId7(new Integer(7));
            record.setSecondCustomerId(new Integer(567));
            record.setSecondFirstName("fred2");
            record.setThirdFirstName("fred3");
    
            Integer generatedCustomerId = dao.insert(record);
    
            int rows = dao.deleteByPrimaryKey(generatedCustomerId);
            assertEquals(1, rows);
    
            AwfulTableExample example = new AwfulTableExample();
            List answer = dao.selectByExample(example);
            assertEquals(0, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

}
