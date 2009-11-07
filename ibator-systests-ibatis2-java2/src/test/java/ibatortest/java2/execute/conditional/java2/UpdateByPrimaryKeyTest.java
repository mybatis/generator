package ibatortest.java2.execute.conditional.java2;

import ibatortest.java2.generated.conditional.java2.dao.AwfulTableDAO;
import ibatortest.java2.generated.conditional.java2.dao.PkblobsDAO;
import ibatortest.java2.generated.conditional.java2.dao.PkfieldsDAO;
import ibatortest.java2.generated.conditional.java2.dao.PkfieldsblobsDAO;
import ibatortest.java2.generated.conditional.java2.model.AwfulTable;
import ibatortest.java2.generated.conditional.java2.model.Pkblobs;
import ibatortest.java2.generated.conditional.java2.model.Pkfields;
import ibatortest.java2.generated.conditional.java2.model.PkfieldsKey;
import ibatortest.java2.generated.conditional.java2.model.Pkfieldsblobs;
import ibatortest.java2.generated.conditional.java2.model.PkfieldsblobsKey;

public class UpdateByPrimaryKeyTest extends AbstractConditionalJava2Test {

    public void testPKFieldsUpdateByPrimaryKey() {
        PkfieldsDAO dao = getPkfieldsDAO();
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
    
            dao.insert(record);
    
            record.setFirstname("Scott");
            record.setLastname("Jones");
    
            int rows = dao.updateByPrimaryKey(record);
            assertEquals(1, rows);
    
            PkfieldsKey key = new PkfieldsKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));
    
            Pkfields record2 = dao.selectByPrimaryKey(key);
    
            assertEquals(record.getFirstname(), record2.getFirstname());
            assertEquals(record.getLastname(), record2.getLastname());
            assertEquals(record.getId1(), record2.getId1());
            assertEquals(record.getId2(), record2.getId2());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsUpdateByPrimaryKeySelective() {
        PkfieldsDAO dao = getPkfieldsDAO();
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setDecimal60field(new Integer(5));
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
    
            dao.insert(record);
    
            Pkfields newRecord = new Pkfields();
            newRecord.setId1(new Integer(1));
            newRecord.setId2(new Integer(2));
            newRecord.setFirstname("Scott");
            newRecord.setDecimal60field(new Integer(4));
    
            int rows = dao.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);
    
            PkfieldsKey key = new PkfieldsKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));
    
            Pkfields returnedRecord = dao.selectByPrimaryKey(key);
    
            assertTrue(datesAreEqual(record.getDatefield(), returnedRecord
                    .getDatefield()));
            assertEquals(record.getDecimal100field(), returnedRecord
                    .getDecimal100field());
            assertEquals(record.getDecimal155field(), returnedRecord
                    .getDecimal155field());
            assertEquals(record.getDecimal30field(), returnedRecord
                    .getDecimal30field());
            assertEquals(newRecord.getDecimal60field(), returnedRecord
                    .getDecimal60field());
            assertEquals(newRecord.getFirstname(), returnedRecord
                    .getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(timesAreEqual(record.getTimefield(), returnedRecord
                    .getTimefield()));
            assertEquals(record.getTimestampfield(), returnedRecord
                    .getTimestampfield());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsUpdateByPrimaryKeyWithBLOBs() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.updateByPrimaryKey(record);
    
            Pkblobs newRecord = dao.selectByPrimaryKey(new Integer(3));
    
            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsUpdateByPrimaryKeySelective() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            Pkblobs newRecord = new Pkblobs();
            newRecord.setId(new Integer(3));
            newRecord.setBlob2(generateRandomBlob());
            dao.updateByPrimaryKeySelective(newRecord);
    
            Pkblobs returnedRecord = dao.selectByPrimaryKey(new Integer(3));
            assertNotNull(returnedRecord);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(newRecord.getBlob2(), returnedRecord
                    .getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsUpdateByPrimaryKeyWithBLOBs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            Pkfieldsblobs updateRecord = new Pkfieldsblobs();
            updateRecord.setId1(new Integer(3));
            updateRecord.setId2(new Integer(4));
            updateRecord.setFirstname("Scott");
            updateRecord.setLastname("Jones");
            updateRecord.setBlob1(generateRandomBlob());
    
            int rows = dao.updateByPrimaryKeyWithBLOBs(updateRecord);
            assertEquals(1, rows);
    
            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(new Integer(3));
            key.setId2(new Integer(4));
            Pkfieldsblobs newRecord = dao.selectByPrimaryKey(key);
            assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertTrue(blobsAreEqual(updateRecord.getBlob1(), newRecord
                    .getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsUpdateByPrimaryKeyWithoutBLOBs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            Pkfieldsblobs updateRecord = new Pkfieldsblobs();
            updateRecord.setId1(new Integer(3));
            updateRecord.setId2(new Integer(4));
            updateRecord.setFirstname("Scott");
            updateRecord.setLastname("Jones");
    
            int rows = dao.updateByPrimaryKeyWithoutBLOBs(updateRecord);
            assertEquals(1, rows);
    
            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(new Integer(3));
            key.setId2(new Integer(4));
            Pkfieldsblobs newRecord = dao.selectByPrimaryKey(key);
            assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsUpdateByPrimaryKeySelective() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            Pkfieldsblobs updateRecord = new Pkfieldsblobs();
            updateRecord.setId1(new Integer(3));
            updateRecord.setId2(new Integer(4));
            updateRecord.setLastname("Jones");
    
            int rows = dao.updateByPrimaryKeySelective(updateRecord);
            assertEquals(1, rows);
    
            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(new Integer(3));
            key.setId2(new Integer(4));
            Pkfieldsblobs returnedRecord = dao.selectByPrimaryKey(key);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), returnedRecord
                    .getLastname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableUpdateByPrimaryKey() {
        AwfulTableDAO dao = getAwfulTableDAO();
    
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
    
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
    
            int rows = dao.updateByPrimaryKey(record);
            assertEquals(1, rows);
    
            AwfulTable returnedRecord = dao.selectByPrimaryKey(generatedCustomerId);
    
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableUpdateByPrimaryKeySelective() {
        AwfulTableDAO dao = getAwfulTableDAO();
    
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
    
            AwfulTable newRecord = new AwfulTable();
            newRecord.setCustomerId(generatedCustomerId);
            newRecord.setId1(new Integer(11));
            newRecord.setId2(new Integer(22));
    
            int rows = dao.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);
    
            AwfulTable returnedRecord = dao.selectByPrimaryKey(generatedCustomerId);
    
            assertEquals(generatedCustomerId, returnedRecord.getCustomerId());
            assertEquals(record.geteMail(), returnedRecord.geteMail());
            assertEquals(record.getEmailaddress(), returnedRecord
                    .getEmailaddress());
            assertEquals(record.getFirstFirstName(), returnedRecord
                    .getFirstFirstName());
            assertEquals(record.getFourthFirstName(), returnedRecord
                    .getFourthFirstName());
            assertEquals(record.getFrom(), returnedRecord.getFrom());
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
            assertEquals(record.getId5(), returnedRecord.getId5());
            assertEquals(record.getId6(), returnedRecord.getId6());
            assertEquals(record.getId7(), returnedRecord.getId7());
            assertEquals(record.getSecondCustomerId(), returnedRecord
                    .getSecondCustomerId());
            assertEquals(record.getSecondFirstName(), returnedRecord
                    .getSecondFirstName());
            assertEquals(record.getThirdFirstName(), returnedRecord
                    .getThirdFirstName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
