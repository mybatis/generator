package abatortest.java2.execute.conditional.java2;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import abatortest.java2.BaseTest;
import abatortest.java2.generated.conditional.java2.dao.AwfulTableDAO;
import abatortest.java2.generated.conditional.java2.dao.AwfulTableDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.FieldsblobsDAO;
import abatortest.java2.generated.conditional.java2.dao.FieldsblobsDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.FieldsonlyDAO;
import abatortest.java2.generated.conditional.java2.dao.FieldsonlyDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkblobsDAO;
import abatortest.java2.generated.conditional.java2.dao.PkblobsDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsDAO;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsblobsDAO;
import abatortest.java2.generated.conditional.java2.dao.PkfieldsblobsDAOImpl;
import abatortest.java2.generated.conditional.java2.dao.PkonlyDAO;
import abatortest.java2.generated.conditional.java2.dao.PkonlyDAOImpl;
import abatortest.java2.generated.conditional.java2.model.AwfulTable;
import abatortest.java2.generated.conditional.java2.model.FieldsblobsExample;
import abatortest.java2.generated.conditional.java2.model.FieldsblobsWithBLOBs;
import abatortest.java2.generated.conditional.java2.model.Fieldsonly;
import abatortest.java2.generated.conditional.java2.model.FieldsonlyExample;
import abatortest.java2.generated.conditional.java2.model.Pkblobs;
import abatortest.java2.generated.conditional.java2.model.PkblobsExample;
import abatortest.java2.generated.conditional.java2.model.Pkfields;
import abatortest.java2.generated.conditional.java2.model.PkfieldsKey;
import abatortest.java2.generated.conditional.java2.model.Pkfieldsblobs;
import abatortest.java2.generated.conditional.java2.model.PkfieldsblobsExample;
import abatortest.java2.generated.conditional.java2.model.PkonlyExample;
import abatortest.java2.generated.conditional.java2.model.PkonlyKey;

public class InsertTests extends BaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "abatortest/java2/execute/conditional/java2/SqlMapConfig.xml",
                null);
    }

    public void testFieldsOnlyInsert() {
        FieldsonlyDAO dao = new FieldsonlyDAOImpl(sqlMapClient);
    
        try {
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(new Double(11.22));
            record.setFloatfield(new Double(33.44));
            record.setIntegerfield(new Integer(5));
            dao.insert(record);
    
            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldEqualTo(new Integer(5));
    
            List answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
    
            Fieldsonly returnedRecord = (Fieldsonly) answer.get(0);
            assertEquals(record.getIntegerfield(), returnedRecord
                    .getIntegerfield());
            assertEquals(record.getDoublefield(), returnedRecord
                    .getDoublefield());
            assertEquals(record.getFloatfield(), returnedRecord.getFloatfield());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKOnlyInsert() {
        PkonlyDAO dao = new PkonlyDAOImpl(sqlMapClient);
    
        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(new Integer(1));
            key.setSeqNum(new Integer(3));
            dao.insert(key);
    
            PkonlyExample example = new PkonlyExample();
            List answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
    
            PkonlyKey returnedRecord = (PkonlyKey) answer.get(0);
            assertEquals(key.getId(), returnedRecord.getId());
            assertEquals(key.getSeqNum(), returnedRecord.getSeqNum());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsInsert() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setDatefield(new Date());
            record.setDecimal100field(new Long(10L));
            record.setDecimal155field(new BigDecimal("15.12345"));
            record.setDecimal30field(new Short((short) 3));
            record.setDecimal60field(new Integer(6));
            record.setFirstname("Jeff");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            record.setLastname("Butler");
            record.setTimefield(new Date());
            record.setTimestampfield(new Date());
    
            dao.insert(record);
    
            PkfieldsKey key = new PkfieldsKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));
    
            Pkfields returnedRecord = dao.selectByPrimaryKey(key);
            assertNotNull(returnedRecord);
    
            assertTrue(datesAreEqual(record.getDatefield(), returnedRecord
                    .getDatefield()));
            assertEquals(record.getDecimal100field(), returnedRecord
                    .getDecimal100field());
            assertEquals(record.getDecimal155field(), returnedRecord
                    .getDecimal155field());
            assertEquals(record.getDecimal30field(), returnedRecord
                    .getDecimal30field());
            assertEquals(record.getDecimal60field(), returnedRecord
                    .getDecimal60field());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(timesAreEqual(record.getTimefield(), returnedRecord
                    .getTimefield()));
            assertEquals(record.getTimestampfield(), returnedRecord
                    .getTimestampfield());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsInsert() {
        PkblobsDAO dao = new PkblobsDAOImpl(sqlMapClient);
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            PkblobsExample example = new PkblobsExample();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            Pkblobs returnedRecord = (Pkblobs) answer.get(0);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsInsert() {
        PkfieldsblobsDAO dao = new PkfieldsblobsDAOImpl(sqlMapClient);
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            Pkfieldsblobs returnedRecord = (Pkfieldsblobs) answer.get(0);
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsBlobsInsert() {
        FieldsblobsDAO dao = new FieldsblobsDAOImpl(sqlMapClient);
    
        try {
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            FieldsblobsExample example = new FieldsblobsExample();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            FieldsblobsWithBLOBs returnedRecord = (FieldsblobsWithBLOBs) answer
                    .get(0);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableInsert() {
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
            assertEquals(57, generatedCustomerId.intValue());
    
            AwfulTable returnedRecord = dao
                    .selectByPrimaryKey(generatedCustomerId);
    
            assertEquals(generatedCustomerId, returnedRecord.getCustomerId());
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
