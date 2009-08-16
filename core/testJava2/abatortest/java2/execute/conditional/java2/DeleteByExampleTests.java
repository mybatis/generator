package abatortest.java2.execute.conditional.java2;

import java.sql.SQLException;
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

public class DeleteByExampleTests extends BaseTest {

    protected void setUp() throws Exception {
        super.setUp();
        initSqlMapClient(
                "abatortest/java2/execute/conditional/java2/SqlMapConfig.xml",
                null);
    }

    public void testFieldsOnlyDeleteByExample() {
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
    
            int rows = dao.deleteByExample(example);
            assertEquals(2, rows);
    
            example = new FieldsonlyExample();
            List answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKOnlyDeleteByExample() {
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
            int rows = dao.deleteByExample(example);
            assertEquals(2, rows);
    
            example = new PkonlyExample();
            List answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsDeleteByExample() {
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
    
            PkfieldsExample example = new PkfieldsExample();
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
    
            example = new PkfieldsExample();
            example.createCriteria().andLastnameLike("J%");
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);
    
            example = new PkfieldsExample();
            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsDeleteByExample() {
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
            List answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());
    
            example = new PkblobsExample();
            example.createCriteria().andIdLessThan(new Integer(4));
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);
    
            example = new PkblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsDeleteByExample() {
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
    
            example = new PkfieldsblobsExample();
            example.createCriteria().andId1NotEqualTo(new Integer(3));
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);
    
            example = new PkfieldsblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsBlobsDeleteByExample() {
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
            List answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());
    
            example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);
    
            example = new FieldsblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableDeleteByExample() {
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
    
            dao.insert(record);
    
            AwfulTableExample example = new AwfulTableExample();
            List answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
    
            example = new AwfulTableExample();
            example.createCriteria().andEMailLike("fred@%");
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);
    
            example.clear();
            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

}
