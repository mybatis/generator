package abatortest.java2.execute.conditional.java2;

import java.sql.SQLException;
import java.util.ArrayList;
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
import abatortest.java2.generated.conditional.java2.model.AwfulTableExample;
import abatortest.java2.generated.conditional.java2.model.Fieldsblobs;
import abatortest.java2.generated.conditional.java2.model.FieldsblobsExample;
import abatortest.java2.generated.conditional.java2.model.FieldsblobsWithBLOBs;
import abatortest.java2.generated.conditional.java2.model.Fieldsonly;
import abatortest.java2.generated.conditional.java2.model.FieldsonlyExample;
import abatortest.java2.generated.conditional.java2.model.Pkblobs;
import abatortest.java2.generated.conditional.java2.model.PkblobsExample;
import abatortest.java2.generated.conditional.java2.model.Pkfields;
import abatortest.java2.generated.conditional.java2.model.PkfieldsExample;
import abatortest.java2.generated.conditional.java2.model.Pkfieldsblobs;
import abatortest.java2.generated.conditional.java2.model.PkfieldsblobsExample;
import abatortest.java2.generated.conditional.java2.model.PkonlyExample;
import abatortest.java2.generated.conditional.java2.model.PkonlyKey;

public class SelectByExampleTests extends BaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "abatortest/java2/execute/conditional/java2/SqlMapConfig.xml",
                null);
    }

    public void testFieldsOnlySelectByExample() {
        FieldsonlyDAO dao = new FieldsonlyDAOImpl(sqlMapClient);
    
        try {
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(new Double(11.22));
            record.setFloatfield(new Double(33.44));
            record.setIntegerfield(new Integer(5));
            dao.insert(record);
    
            record = new Fieldsonly();
            record.setDoublefield(new Double(44.55));
            record.setFloatfield(new Double(66.77));
            record.setIntegerfield(new Integer(8));
            dao.insert(record);
    
            record = new Fieldsonly();
            record.setDoublefield(new Double(88.99));
            record.setFloatfield(new Double(100.111));
            record.setIntegerfield(new Integer(9));
            dao.insert(record);
    
            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(new Integer(5));
    
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
    
            example = new FieldsonlyExample();
            answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsOnlySelectByExampleNoCriteria() {
        FieldsonlyDAO dao = new FieldsonlyDAOImpl(sqlMapClient);
    
        try {
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(new Double(11.22));
            record.setFloatfield(new Double(33.44));
            record.setIntegerfield(new Integer(5));
            dao.insert(record);
    
            record = new Fieldsonly();
            record.setDoublefield(new Double(44.55));
            record.setFloatfield(new Double(66.77));
            record.setIntegerfield(new Integer(8));
            dao.insert(record);
    
            record = new Fieldsonly();
            record.setDoublefield(new Double(88.99));
            record.setFloatfield(new Double(100.111));
            record.setIntegerfield(new Integer(9));
            dao.insert(record);
    
            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria();
    
            List answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKOnlySelectByExample() {
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
    
            key = new PkonlyKey();
            key.setId(new Integer(7));
            key.setSeqNum(new Integer(8));
            dao.insert(key);
    
            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(new Integer(4));
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKOnlySelectByExampleNoCriteria() {
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
    
            key = new PkonlyKey();
            key.setId(new Integer(7));
            key.setSeqNum(new Integer(8));
            dao.insert(key);
    
            PkonlyExample example = new PkonlyExample();
            example.createCriteria();
            List answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsSelectByExampleLike() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameLike("B%");
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            Pkfields returnedRecord = (Pkfields) answer.get(0);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsSelectByExampleNotLike() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameNotLike("B%");
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            Pkfields returnedRecord = (Pkfields) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(2);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsSelectByExampleComplexLike() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameLike("B%").andId2EqualTo(
                    new Integer(3));
            example.or(example.createCriteria().andFirstnameLike("Wi%"));
    
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
            Pkfields returnedRecord = (Pkfields) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsSelectByExampleIn() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            List ids = new ArrayList();
            ids.add(new Integer(1));
            ids.add(new Integer(3));
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andId2In(ids);
    
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectByExample(example);
            assertEquals(4, answer.size());
            Pkfields returnedRecord = (Pkfields) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (Pkfields) answer.get(3);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsSelectByExampleBetween() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andId2Between(new Integer(1),
                    new Integer(3));
    
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectByExample(example);
            assertEquals(6, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsSelectByExampleNoCriteria() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insert(record);
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria();
    
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectByExample(example);
            assertEquals(6, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsSelectByExampleEscapedFields() {
        PkfieldsDAO dao = new PkfieldsDAOImpl(sqlMapClient);
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            record.setWierdField(new Integer(11));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            record.setWierdField(new Integer(22));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            record.setWierdField(new Integer(33));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            record.setWierdField(new Integer(44));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            record.setWierdField(new Integer(55));
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            record.setWierdField(new Integer(66));
            dao.insert(record);
    
            List values = new ArrayList();
            values.add(new Integer(11));
            values.add(new Integer(22));
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andWierdFieldLessThan(new Integer(40))
                    .andWierdFieldIn(values);
    
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsSelectByExampleWithoutBlobs() {
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
    
            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(new Integer(4));
            List answer = dao.selectByExampleWithoutBLOBs(example);
    
            assertEquals(1, answer.size());
    
            Pkblobs key = (Pkblobs) answer.get(0);
            assertEquals(6, key.getId().intValue());
            assertNull(key.getBlob1());
            assertNull(key.getBlob2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsSelectByExampleWithoutBlobsNoCriteria() {
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
    
            PkblobsExample example = new PkblobsExample();
            example.createCriteria();
            List answer = dao.selectByExampleWithoutBLOBs(example);
    
            assertEquals(2, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsSelectByExampleWithBlobs() {
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
    
            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(new Integer(4));
            List answer = dao.selectByExampleWithBLOBs(example);
    
            assertEquals(1, answer.size());
    
            Pkblobs newRecord = (Pkblobs) answer.get(0);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsSelectByExampleWithoutBlobs() {
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
            example.createCriteria().andId2EqualTo(new Integer(6));
            List answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
    
            Pkfieldsblobs newRecord = (Pkfieldsblobs) answer.get(0);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertNull(newRecord.getBlob1());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsSelectByExampleWithBlobs() {
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
            example.createCriteria().andId2EqualTo(new Integer(6));
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            Pkfieldsblobs newRecord = (Pkfieldsblobs) answer.get(0);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
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
            example.createCriteria();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsBlobsSelectByExampleWithoutBlobs() {
        FieldsblobsDAO dao = new FieldsblobsDAOImpl(sqlMapClient);
    
        try {
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new FieldsblobsWithBLOBs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            List answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
    
            Fieldsblobs newRecord = (Fieldsblobs) answer.get(0);
            assertFalse(newRecord instanceof FieldsblobsWithBLOBs);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsBlobsSelectByExampleWithBlobs() {
        FieldsblobsDAO dao = new FieldsblobsDAOImpl(sqlMapClient);
    
        try {
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new FieldsblobsWithBLOBs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            FieldsblobsWithBLOBs newRecord = (FieldsblobsWithBLOBs) answer
                    .get(0);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        FieldsblobsDAO dao = new FieldsblobsDAOImpl(sqlMapClient);
    
        try {
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new FieldsblobsWithBLOBs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByExampleLike() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFourthFirstName("wilma4");
            record.setFrom("from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFourthFirstName("pebbles4");
            record.setFrom("from field");
            record.setId1(new Integer(111));
            record.setId2(new Integer(222));
            record.setId5(new Integer(555));
            record.setId6(new Integer(666));
            record.setId7(new Integer(777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFourthFirstName("barney4");
            record.setFrom("from field");
            record.setId1(new Integer(1111));
            record.setId2(new Integer(2222));
            record.setId5(new Integer(5555));
            record.setId6(new Integer(6666));
            record.setId7(new Integer(7777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFourthFirstName("betty4");
            record.setFrom("from field");
            record.setId1(new Integer(11111));
            record.setId2(new Integer(22222));
            record.setId5(new Integer(55555));
            record.setId6(new Integer(66666));
            record.setId7(new Integer(77777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFourthFirstName("bammbamm4");
            record.setFrom("from field");
            record.setId1(new Integer(111111));
            record.setId2(new Integer(222222));
            record.setId5(new Integer(555555));
            record.setId6(new Integer(666666));
            record.setId7(new Integer(777777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);
    
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andFirstFirstNameLike("b%");
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            AwfulTable returnedRecord = (AwfulTable) answer.get(0);
            assertEquals(1111, returnedRecord.getId1().intValue());
            assertEquals(2222, returnedRecord.getId2().intValue());
            returnedRecord = (AwfulTable) answer.get(1);
            assertEquals(11111, returnedRecord.getId1().intValue());
            assertEquals(22222, returnedRecord.getId2().intValue());
            returnedRecord = (AwfulTable) answer.get(2);
            assertEquals(111111, returnedRecord.getId1().intValue());
            assertEquals(222222, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByExampleNotLike() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFourthFirstName("wilma4");
            record.setFrom("from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFourthFirstName("pebbles4");
            record.setFrom("from field");
            record.setId1(new Integer(111));
            record.setId2(new Integer(222));
            record.setId5(new Integer(555));
            record.setId6(new Integer(666));
            record.setId7(new Integer(777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFourthFirstName("barney4");
            record.setFrom("from field");
            record.setId1(new Integer(1111));
            record.setId2(new Integer(2222));
            record.setId5(new Integer(5555));
            record.setId6(new Integer(6666));
            record.setId7(new Integer(7777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFourthFirstName("betty4");
            record.setFrom("from field");
            record.setId1(new Integer(11111));
            record.setId2(new Integer(22222));
            record.setId5(new Integer(55555));
            record.setId6(new Integer(66666));
            record.setId7(new Integer(77777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFourthFirstName("bammbamm4");
            record.setFrom("from field");
            record.setId1(new Integer(111111));
            record.setId2(new Integer(222222));
            record.setId5(new Integer(555555));
            record.setId6(new Integer(666666));
            record.setId7(new Integer(777777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);
    
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andFirstFirstNameNotLike("b%");
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            AwfulTable returnedRecord = (AwfulTable) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (AwfulTable) answer.get(1);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
            returnedRecord = (AwfulTable) answer.get(2);
            assertEquals(111, returnedRecord.getId1().intValue());
            assertEquals(222, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByExampleComplexLike() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFourthFirstName("wilma4");
            record.setFrom("from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFourthFirstName("pebbles4");
            record.setFrom("from field");
            record.setId1(new Integer(111));
            record.setId2(new Integer(222));
            record.setId5(new Integer(555));
            record.setId6(new Integer(666));
            record.setId7(new Integer(777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFourthFirstName("barney4");
            record.setFrom("from field");
            record.setId1(new Integer(1111));
            record.setId2(new Integer(2222));
            record.setId5(new Integer(5555));
            record.setId6(new Integer(6666));
            record.setId7(new Integer(7777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFourthFirstName("betty4");
            record.setFrom("from field");
            record.setId1(new Integer(11111));
            record.setId2(new Integer(22222));
            record.setId5(new Integer(55555));
            record.setId6(new Integer(66666));
            record.setId7(new Integer(77777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFourthFirstName("bammbamm4");
            record.setFrom("from field");
            record.setId1(new Integer(111111));
            record.setId2(new Integer(222222));
            record.setId5(new Integer(555555));
            record.setId6(new Integer(666666));
            record.setId7(new Integer(777777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);
    
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andFirstFirstNameLike("b%").andId2EqualTo(new Integer(222222));
            example.or(example.createCriteria().andFirstFirstNameLike("wi%"));
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
            AwfulTable returnedRecord = (AwfulTable) answer.get(0);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
            returnedRecord = (AwfulTable) answer.get(1);
            assertEquals(111111, returnedRecord.getId1().intValue());
            assertEquals(222222, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByExampleIn() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFourthFirstName("wilma4");
            record.setFrom("from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFourthFirstName("pebbles4");
            record.setFrom("from field");
            record.setId1(new Integer(111));
            record.setId2(new Integer(222));
            record.setId5(new Integer(555));
            record.setId6(new Integer(666));
            record.setId7(new Integer(777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFourthFirstName("barney4");
            record.setFrom("from field");
            record.setId1(new Integer(1111));
            record.setId2(new Integer(2222));
            record.setId5(new Integer(5555));
            record.setId6(new Integer(6666));
            record.setId7(new Integer(7777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFourthFirstName("betty4");
            record.setFrom("from field");
            record.setId1(new Integer(11111));
            record.setId2(new Integer(22222));
            record.setId5(new Integer(55555));
            record.setId6(new Integer(66666));
            record.setId7(new Integer(77777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFourthFirstName("bammbamm4");
            record.setFrom("from field");
            record.setId1(new Integer(111111));
            record.setId2(new Integer(222222));
            record.setId5(new Integer(555555));
            record.setId6(new Integer(666666));
            record.setId7(new Integer(777777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);
    
            List ids = new ArrayList();
            ids.add(new Integer(1));
            ids.add(new Integer(11));
    
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andId1In(ids);
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List answer = dao.selectByExample(example);
            
            assertEquals(2, answer.size());
            AwfulTable returnedRecord = (AwfulTable) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (AwfulTable) answer.get(1);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByExampleBetween() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFourthFirstName("wilma4");
            record.setFrom("from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFourthFirstName("pebbles4");
            record.setFrom("from field");
            record.setId1(new Integer(111));
            record.setId2(new Integer(222));
            record.setId5(new Integer(555));
            record.setId6(new Integer(666));
            record.setId7(new Integer(777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFourthFirstName("barney4");
            record.setFrom("from field");
            record.setId1(new Integer(1111));
            record.setId2(new Integer(2222));
            record.setId5(new Integer(5555));
            record.setId6(new Integer(6666));
            record.setId7(new Integer(7777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFourthFirstName("betty4");
            record.setFrom("from field");
            record.setId1(new Integer(11111));
            record.setId2(new Integer(22222));
            record.setId5(new Integer(55555));
            record.setId6(new Integer(66666));
            record.setId7(new Integer(77777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFourthFirstName("bammbamm4");
            record.setFrom("from field");
            record.setId1(new Integer(111111));
            record.setId2(new Integer(222222));
            record.setId5(new Integer(555555));
            record.setId6(new Integer(666666));
            record.setId7(new Integer(777777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);
    
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andId1Between(new Integer(1), new Integer(1000));
            List answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByExampleNoCriteria() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFourthFirstName("wilma4");
            record.setFrom("from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFourthFirstName("pebbles4");
            record.setFrom("from field");
            record.setId1(new Integer(111));
            record.setId2(new Integer(222));
            record.setId5(new Integer(555));
            record.setId6(new Integer(666));
            record.setId7(new Integer(777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFourthFirstName("barney4");
            record.setFrom("from field");
            record.setId1(new Integer(1111));
            record.setId2(new Integer(2222));
            record.setId5(new Integer(5555));
            record.setId6(new Integer(6666));
            record.setId7(new Integer(7777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFourthFirstName("betty4");
            record.setFrom("from field");
            record.setId1(new Integer(11111));
            record.setId2(new Integer(22222));
            record.setId5(new Integer(55555));
            record.setId6(new Integer(66666));
            record.setId7(new Integer(77777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFourthFirstName("bammbamm4");
            record.setFrom("from field");
            record.setId1(new Integer(111111));
            record.setId2(new Integer(222222));
            record.setId5(new Integer(555555));
            record.setId6(new Integer(666666));
            record.setId7(new Integer(777777));
            record.setSecondCustomerId(new Integer(567567));
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);
    
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria();
            example.setOrderByClause("\"A_CuStOmEr iD\" desc");
            
            List answer = dao.selectByExample(example);
            assertEquals(6, answer.size());
            AwfulTable returnedRecord = (AwfulTable) answer.get(0);
            assertEquals(111111, returnedRecord.getId1().intValue());
            returnedRecord = (AwfulTable) answer.get(1);
            assertEquals(11111, returnedRecord.getId1().intValue());
            returnedRecord = (AwfulTable) answer.get(2);
            assertEquals(1111, returnedRecord.getId1().intValue());
            returnedRecord = (AwfulTable) answer.get(3);
            assertEquals(111, returnedRecord.getId1().intValue());
            returnedRecord = (AwfulTable) answer.get(4);
            assertEquals(11, returnedRecord.getId1().intValue());
            returnedRecord = (AwfulTable) answer.get(5);
            assertEquals(1, returnedRecord.getId1().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}
