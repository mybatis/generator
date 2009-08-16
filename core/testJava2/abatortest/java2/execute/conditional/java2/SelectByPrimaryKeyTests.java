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
import abatortest.java2.generated.conditional.java2.model.AwfulTable;
import abatortest.java2.generated.conditional.java2.model.Pkblobs;
import abatortest.java2.generated.conditional.java2.model.Pkfields;
import abatortest.java2.generated.conditional.java2.model.PkfieldsKey;
import abatortest.java2.generated.conditional.java2.model.Pkfieldsblobs;
import abatortest.java2.generated.conditional.java2.model.PkfieldsblobsExample;
import abatortest.java2.generated.conditional.java2.model.PkfieldsblobsKey;

public class SelectByPrimaryKeyTests extends BaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "abatortest/java2/execute/conditional/java2/SqlMapConfig.xml",
                null);
    }

    public void testPKFieldsSelectByPrimaryKey() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bob");
            record.setLastname("Jones");
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            dao.insert(record);
    
            PkfieldsKey key = new PkfieldsKey();
            key.setId1(new Integer(3));
            key.setId2(new Integer(4));
            Pkfields newRecord = dao.selectByPrimaryKey(key);
    
            assertNotNull(newRecord);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsSelectByPrimaryKey() {
        PkblobsDAO dao = new PkblobsDAOImpl(sqlMapClient);
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkblobs();
            record.setId(new Integer(6));
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            Pkblobs newRecord = dao.selectByPrimaryKey(new Integer(6));
            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsSelectByPrimaryKey() {
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
            Pkfieldsblobs newRecord = dao.selectByPrimaryKey(key);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByPrimaryKey() {
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
    
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("fred2@fred.com");
            record.setEmailaddress("alsofred2@fred.com");
            record.setFirstFirstName("fred11");
            record.setFourthFirstName("fred44");
            record.setFrom("from from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("fred22");
            record.setThirdFirstName("fred33");
    
            Integer generatedKey = dao.insert(record);
    
            AwfulTable returnedRecord = dao.selectByPrimaryKey(generatedKey);
    
            assertNotNull(returnedRecord);
            assertEquals(record.getCustomerId(), returnedRecord.getCustomerId());
            assertEquals(record.geteMail(), returnedRecord.geteMail());
            assertEquals(record.getEmailaddress(), returnedRecord
                    .getEmailaddress());
            assertEquals(record.getFirstFirstName(), returnedRecord
                    .getFirstFirstName());
            assertEquals(record.getFourthFirstName(), returnedRecord
                    .getFourthFirstName());
            assertEquals(record.getFrom(), returnedRecord.getFrom());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getId5(), returnedRecord.getId5());
            assertEquals(record.getId6(), returnedRecord.getId6());
            assertEquals(record.getId7(), returnedRecord.getId7());
            assertEquals(record.getSecondCustomerId(), returnedRecord
                    .getSecondCustomerId());
            assertEquals(record.getSecondFirstName(), returnedRecord
                    .getSecondFirstName());
            assertEquals(record.getThirdFirstName(), returnedRecord
                    .getThirdFirstName());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

}
