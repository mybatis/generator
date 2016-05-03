/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mbg.test.ib2j5.flat;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import mbg.test.ib2j5.generated.flat.dao.AwfulTableDAO;
import mbg.test.ib2j5.generated.flat.dao.FieldsblobsDAO;
import mbg.test.ib2j5.generated.flat.dao.FieldsonlyDAO;
import mbg.test.ib2j5.generated.flat.dao.PkblobsDAO;
import mbg.test.ib2j5.generated.flat.dao.PkfieldsDAO;
import mbg.test.ib2j5.generated.flat.dao.PkfieldsblobsDAO;
import mbg.test.ib2j5.generated.flat.dao.PkonlyDAO;
import mbg.test.ib2j5.generated.flat.model.AwfulTable;
import mbg.test.ib2j5.generated.flat.model.AwfulTableExample;
import mbg.test.ib2j5.generated.flat.model.Fieldsblobs;
import mbg.test.ib2j5.generated.flat.model.FieldsblobsExample;
import mbg.test.ib2j5.generated.flat.model.Fieldsonly;
import mbg.test.ib2j5.generated.flat.model.FieldsonlyExample;
import mbg.test.ib2j5.generated.flat.model.Pkblobs;
import mbg.test.ib2j5.generated.flat.model.PkblobsExample;
import mbg.test.ib2j5.generated.flat.model.Pkfields;
import mbg.test.ib2j5.generated.flat.model.PkfieldsExample;
import mbg.test.ib2j5.generated.flat.model.Pkfieldsblobs;
import mbg.test.ib2j5.generated.flat.model.PkfieldsblobsExample;
import mbg.test.ib2j5.generated.flat.model.Pkonly;
import mbg.test.ib2j5.generated.flat.model.PkonlyExample;

import org.junit.Test;
/**
 * 
 * @author Jeff Butler
 *
 */
public class UpdateByExampleTest extends AbstractFlatJava5Test {

    @Test
    public void testFieldsOnlyUpdateByExampleSelective() {
        FieldsonlyDAO dao = getFieldsonlyDAO();

        try {
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            dao.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(44.55);
            record.setFloatfield(66.77);
            record.setIntegerfield(8);
            dao.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(88.99);
            record.setFloatfield(100.111);
            record.setIntegerfield(9);
            dao.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(99d);
            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);
            
            int rows = dao.updateByExampleSelective(record, example);
            assertEquals(2, rows);

            example.clear();
            example.createCriteria().andIntegerfieldEqualTo(5);
            List<Fieldsonly> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(record.getDoublefield(), 11.22, 0.001);
            assertEquals(record.getFloatfield(), 33.44, 0.001);
            assertEquals(record.getIntegerfield().intValue(), 5);
            
            example.clear();
            example.createCriteria().andIntegerfieldEqualTo(8);
            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(record.getDoublefield(), 99, 0.001);
            assertEquals(record.getFloatfield(), 66.77, 0.001);
            assertEquals(record.getIntegerfield().intValue(), 8);
            
            example.clear();
            example.createCriteria().andIntegerfieldEqualTo(9);
            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(record.getDoublefield(), 99, 0.001);
            assertEquals(record.getFloatfield(), 100.111, 0.001);
            assertEquals(record.getIntegerfield().intValue(), 9);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsOnlyUpdateByExample() {
        FieldsonlyDAO dao = getFieldsonlyDAO();

        try {
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            dao.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(44.55);
            record.setFloatfield(66.77);
            record.setIntegerfield(8);
            dao.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(88.99);
            record.setFloatfield(100.111);
            record.setIntegerfield(9);
            dao.insert(record);

            record = new Fieldsonly();
            record.setIntegerfield(22);
            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldEqualTo(5);
            
            int rows = dao.updateByExample(record, example);
            assertEquals(1, rows);

            example.clear();
            example.createCriteria().andIntegerfieldEqualTo(22);
            List<Fieldsonly> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertNull(record.getDoublefield());
            assertNull(record.getFloatfield());
            assertEquals(record.getIntegerfield().intValue(), 22);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlyUpdateByExampleSelective() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            Pkonly key = new Pkonly();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            key = new Pkonly();
            key.setId(5);
            key.setSeqNum(6);
            dao.insert(key);

            key = new Pkonly();
            key.setId(7);
            key.setSeqNum(8);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            key = new Pkonly();
            key.setSeqNum(3);
            int rows = dao.updateByExampleSelective(key, example);
            assertEquals(2, rows);

            example.clear();
            example.createCriteria()
                .andIdEqualTo(5)
                .andSeqNumEqualTo(3);
            
            rows = dao.countByExample(example);
            assertEquals(1, rows);
            
            example.clear();
            example.createCriteria()
                .andIdEqualTo(7)
                .andSeqNumEqualTo(3);
            
            rows = dao.countByExample(example);
            assertEquals(1, rows);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlyUpdateByExample() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            Pkonly key = new Pkonly();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            key = new Pkonly();
            key.setId(5);
            key.setSeqNum(6);
            dao.insert(key);

            key = new Pkonly();
            key.setId(7);
            key.setSeqNum(8);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria()
                .andIdEqualTo(7);
            key = new Pkonly();
            key.setSeqNum(3);
            key.setId(22);
            int rows = dao.updateByExample(key, example);
            assertEquals(1, rows);

            example.clear();
            example.createCriteria()
                .andIdEqualTo(22)
                .andSeqNumEqualTo(3);
            
            rows = dao.countByExample(example);
            assertEquals(1, rows);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsUpdateByExampleSelective() {
        PkfieldsDAO dao = getPkfieldsDAO();
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bob");
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);
    
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Fred");
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andLastnameLike("J%");
            int rows = dao.updateByExampleSelective(record, example);
            assertEquals(1, rows);
            
            example.clear();
            example.createCriteria()
                .andFirstnameEqualTo("Fred")
                .andLastnameEqualTo("Jones")
                .andId1EqualTo(3)
                .andId2EqualTo(4);
    
            rows = dao.countByExample(example);
            assertEquals(1, rows);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsUpdateByExample() {
        PkfieldsDAO dao = getPkfieldsDAO();
    
        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);
    
            record = new Pkfields();
            record.setFirstname("Bob");
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);
    
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Fred");
            record.setId1(3);
            record.setId2(4);
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria()
                .andId1EqualTo(3)
                .andId2EqualTo(4);
            
            int rows = dao.updateByExample(record, example);
            assertEquals(1, rows);
            
            example.clear();
            example.createCriteria()
                .andFirstnameEqualTo("Fred")
                .andLastnameIsNull()
                .andId1EqualTo(3)
                .andId2EqualTo(4);
    
            rows = dao.countByExample(example);
            assertEquals(1, rows);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsUpdateByExampleSelective() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkblobs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            Pkblobs newRecord = new Pkblobs();
            newRecord.setBlob1(generateRandomBlob());
            
            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            int rows = dao.updateByExampleSelective(newRecord, example);
            assertEquals(1, rows);
            
            List<Pkblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Pkblobs returnedRecord = answer.get(0);
            
            assertEquals(6, returnedRecord.getId().intValue());
            assertTrue(blobsAreEqual(newRecord.getBlob1(), returnedRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsUpdateByExampleWithoutBLOBs() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkblobs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            Pkblobs newRecord = new Pkblobs();
            newRecord.setId(8);
            
            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            int rows = dao.updateByExampleWithoutBLOBs(newRecord, example);
            assertEquals(1, rows);
            
            List<Pkblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Pkblobs returnedRecord = answer.get(0);
            
            assertEquals(8, returnedRecord.getId().intValue());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsUpdateByExampleWithBLOBs() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkblobs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            Pkblobs newRecord = new Pkblobs();
            newRecord.setId(8);
            
            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            int rows = dao.updateByExampleWithBLOBs(newRecord, example);
            assertEquals(1, rows);
            
            List<Pkblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Pkblobs returnedRecord = answer.get(0);
            
            assertEquals(8, returnedRecord.getId().intValue());
            assertNull(returnedRecord.getBlob1());
            assertNull(returnedRecord.getBlob2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByExampleSelective() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            Pkfieldsblobs newRecord = new Pkfieldsblobs();
            newRecord.setFirstname("Fred");
            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId1NotEqualTo(3);
            int rows = dao.updateByExampleSelective(newRecord, example);
            assertEquals(1, rows);
    
            List<Pkfieldsblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Pkfieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByExampleWithoutBLOBs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            Pkfieldsblobs newRecord = new Pkfieldsblobs();
            newRecord.setId1(5);
            newRecord.setId2(8);
            newRecord.setFirstname("Fred");
            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId1EqualTo(5);
            int rows = dao.updateByExampleWithoutBLOBs(newRecord, example);
            assertEquals(1, rows);
    
            List<Pkfieldsblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Pkfieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertNull(returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByExampleWithBLOBs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);
    
            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            Pkfieldsblobs newRecord = new Pkfieldsblobs();
            newRecord.setId1(3);
            newRecord.setId2(8);
            newRecord.setFirstname("Fred");
            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId1EqualTo(3);
            int rows = dao.updateByExampleWithBLOBs(newRecord, example);
            assertEquals(1, rows);
    
            List<Pkfieldsblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Pkfieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertNull(returnedRecord.getLastname());
            assertNull(returnedRecord.getBlob1());
            
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsUpdateByExampleSelective() {
        FieldsblobsDAO dao = getFieldsblobsDAO();
    
        try {
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Fieldsblobs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            Fieldsblobs newRecord = new Fieldsblobs();
            newRecord.setLastname("Doe");
            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = dao.updateByExampleSelective(newRecord, example);
            assertEquals(1, rows);
            
            List<Fieldsblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Fieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsUpdateByExampleWithoutBLOBs() {
        FieldsblobsDAO dao = getFieldsblobsDAO();
    
        try {
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Fieldsblobs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            Fieldsblobs newRecord = new Fieldsblobs();
            newRecord.setFirstname("Scott");
            newRecord.setLastname("Doe");
            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = dao.updateByExampleWithoutBLOBs(newRecord, example);
            assertEquals(1, rows);
            
            List<Fieldsblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Fieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsUpdateByExampleWithBLOBs() {
        FieldsblobsDAO dao = getFieldsblobsDAO();
    
        try {
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);
    
            record = new Fieldsblobs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            Fieldsblobs newRecord = new Fieldsblobs();
            newRecord.setFirstname("Scott");
            newRecord.setLastname("Doe");
            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = dao.updateByExampleWithBLOBs(newRecord, example);
            assertEquals(1, rows);
            
            List<Fieldsblobs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
            
            Fieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertNull(returnedRecord.getBlob1());
            assertNull(returnedRecord.getBlob2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableUpdateByExampleSelective() {
        AwfulTableDAO dao = getAwfulTableDAO();
    
        try {
            AwfulTable record = new AwfulTable();
            record.seteMail("fred@fred.com");
            record.setEmailaddress("alsofred@fred.com");
            record.setFirstFirstName("fred1");
            record.setFrom("from field");
            record.setId1(1);
            record.setId2(2);
            record.setId5(5);
            record.setId6(6);
            record.setId7(7);
            record.setSecondFirstName("fred2");
            record.setThirdFirstName("fred3");
    
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("fred2@fred.com");
            record.setEmailaddress("alsofred2@fred.com");
            record.setFirstFirstName("fred11");
            record.setFrom("from from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("fred22");
            record.setThirdFirstName("fred33");
    
            dao.insert(record);
    
            AwfulTable newRecord = new AwfulTable();
            newRecord.setFirstFirstName("Alonzo");
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andEMailLike("fred2@%");
            int rows = dao.updateByExampleSelective(newRecord, example);
            assertEquals(1, rows);
    
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());

            AwfulTable returnedRecord = answer.get(0);
            
            assertEquals(record.getCustomerId(), returnedRecord.getCustomerId());
            assertEquals(record.geteMail(), returnedRecord.geteMail());
            assertEquals(record.getEmailaddress(), returnedRecord.getEmailaddress());
            assertEquals(newRecord.getFirstFirstName(), returnedRecord.getFirstFirstName());
            assertEquals(record.getFrom(), returnedRecord.getFrom());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getId5(), returnedRecord.getId5());
            assertEquals(record.getId6(), returnedRecord.getId6());
            assertEquals(record.getId7(), returnedRecord.getId7());
            assertEquals(record.getSecondFirstName(), returnedRecord.getSecondFirstName());
            assertEquals(record.getThirdFirstName(), returnedRecord.getThirdFirstName());
            
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableUpdateByExample() {
        AwfulTableDAO dao = getAwfulTableDAO();
    
        try {
            AwfulTable record = new AwfulTable();
            record.seteMail("fred@fred.com");
            record.setEmailaddress("alsofred@fred.com");
            record.setFirstFirstName("fred1");
            record.setFrom("from field");
            record.setId1(1);
            record.setId2(2);
            record.setId5(5);
            record.setId6(6);
            record.setId7(7);
            record.setSecondFirstName("fred2");
            record.setThirdFirstName("fred3");
    
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("fred2@fred.com");
            record.setEmailaddress("alsofred2@fred.com");
            record.setFirstFirstName("fred11");
            record.setFrom("from from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("fred22");
            record.setThirdFirstName("fred33");
    
            dao.insert(record);
    
            AwfulTable newRecord = new AwfulTable();
            newRecord.setFirstFirstName("Alonzo");
            newRecord.setCustomerId(58);
            newRecord.setId1(111);
            newRecord.setId2(222);
            newRecord.setId5(555);
            newRecord.setId6(666);
            newRecord.setId7(777);
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andEMailLike("fred2@%");
            int rows = dao.updateByExample(newRecord, example);
            assertEquals(1, rows);

            example.clear();
            example.createCriteria().andCustomerIdEqualTo(58);
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());

            AwfulTable returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getCustomerId(), returnedRecord.getCustomerId());
            assertNull(returnedRecord.geteMail());
            assertNull(returnedRecord.getEmailaddress());
            assertEquals(newRecord.getFirstFirstName(), returnedRecord.getFirstFirstName());
            assertNull(returnedRecord.getFrom());
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
            assertEquals(newRecord.getId5(), returnedRecord.getId5());
            assertEquals(newRecord.getId6(), returnedRecord.getId6());
            assertEquals(newRecord.getId7(), returnedRecord.getId7());
            assertNull(returnedRecord.getSecondFirstName());
            assertNull(returnedRecord.getThirdFirstName());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}
