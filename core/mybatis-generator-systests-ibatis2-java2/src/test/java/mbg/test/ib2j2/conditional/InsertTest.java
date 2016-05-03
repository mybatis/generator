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
package mbg.test.ib2j2.conditional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import mbg.test.common.util.TestUtilities;
import mbg.test.ib2j2.generated.conditional.dao.AwfulTableDAO;
import mbg.test.ib2j2.generated.conditional.dao.FieldsblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.FieldsonlyDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkonlyDAO;
import mbg.test.ib2j2.generated.conditional.model.AwfulTable;
import mbg.test.ib2j2.generated.conditional.model.FieldsblobsExample;
import mbg.test.ib2j2.generated.conditional.model.FieldsblobsWithBLOBs;
import mbg.test.ib2j2.generated.conditional.model.Fieldsonly;
import mbg.test.ib2j2.generated.conditional.model.FieldsonlyExample;
import mbg.test.ib2j2.generated.conditional.model.Pkblobs;
import mbg.test.ib2j2.generated.conditional.model.PkblobsExample;
import mbg.test.ib2j2.generated.conditional.model.Pkfields;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsKey;
import mbg.test.ib2j2.generated.conditional.model.Pkfieldsblobs;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsblobsExample;
import mbg.test.ib2j2.generated.conditional.model.PkonlyExample;
import mbg.test.ib2j2.generated.conditional.model.PkonlyKey;

public class InsertTest extends AbstractConditionalJava2Test {

    public void testFieldsOnlyInsert() {
        FieldsonlyDAO dao = getFieldsonlyDAO();
    
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKOnlyInsert() {
        PkonlyDAO dao = getPkonlyDAO();
    
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsInsert() {
        PkfieldsDAO dao = getPkfieldsDAO();
    
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
    
            assertTrue(TestUtilities.datesAreEqual(record.getDatefield(), returnedRecord
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
            assertTrue(TestUtilities.timesAreEqual(record.getTimefield(), returnedRecord
                    .getTimefield()));
            assertEquals(record.getTimestampfield(), returnedRecord
                    .getTimestampfield());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsInsert() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(TestUtilities.generateRandomBlob());
            record.setBlob2(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            PkblobsExample example = new PkblobsExample();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            Pkblobs returnedRecord = (Pkblobs) answer.get(0);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsInsert() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            Pkfieldsblobs returnedRecord = (Pkfieldsblobs) answer.get(0);
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsBlobsInsert() {
        FieldsblobsDAO dao = getFieldsblobsDAO();
    
        try {
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(TestUtilities.generateRandomBlob());
            record.setBlob2(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            FieldsblobsExample example = new FieldsblobsExample();
            List answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());
    
            FieldsblobsWithBLOBs returnedRecord = (FieldsblobsWithBLOBs) answer
                    .get(0);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableInsert() {
        AwfulTableDAO dao = getAwfulTableDAO();
    
        try {
            AwfulTable record = new AwfulTable();
    
            record.seteMail("fred@fred.com");
            record.setEmailaddress("alsofred@fred.com");
            record.setFirstFirstName("fred1");
            record.setFrom("from field");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            record.setId5(new Integer(5));
            record.setId6(new Integer(6));
            record.setId7(new Integer(7));
            record.setSecondFirstName("fred2");
            record.setThirdFirstName("fred3");
            record.setActive(true);
    
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
            assertEquals(record.getFrom(), returnedRecord.getFrom());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getId5(), returnedRecord.getId5());
            assertEquals(record.getId6(), returnedRecord.getId6());
            assertEquals(record.getId7(), returnedRecord.getId7());
            assertEquals(record.getSecondFirstName(), returnedRecord
                    .getSecondFirstName());
            assertEquals(record.getThirdFirstName(), returnedRecord
                    .getThirdFirstName());
            assertEquals(record.isActive(), returnedRecord
                    .isActive());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableInsertSelective() {
        AwfulTableDAO dao = getAwfulTableDAO();

        try {
            AwfulTable record = new AwfulTable();

            record.seteMail("fred@fred.com");
            record.setEmailaddress("alsofred@fred.com");
            record.setFrom("from field");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            record.setId5(new Integer(5));
            record.setId6(new Integer(6));
            record.setId7(new Integer(7));
            record.setSecondFirstName("fred2");
            record.setThirdFirstName("fred3");

            Integer generatedCustomerId = dao.insertSelective(record);
            assertEquals(57, generatedCustomerId.intValue());

            AwfulTable returnedRecord = dao
                    .selectByPrimaryKey(generatedCustomerId);

            assertEquals(generatedCustomerId, returnedRecord.getCustomerId());
            assertEquals(record.geteMail(), returnedRecord.geteMail());
            assertEquals(record.getEmailaddress(), returnedRecord
                    .getEmailaddress());
            assertEquals("Mabel", returnedRecord.getFirstFirstName());
            assertEquals(record.getFrom(), returnedRecord.getFrom());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getId5(), returnedRecord.getId5());
            assertEquals(record.getId6(), returnedRecord.getId6());
            assertEquals(record.getId7(), returnedRecord.getId7());
            assertEquals(record.getSecondFirstName(), returnedRecord
                    .getSecondFirstName());
            assertEquals(record.getThirdFirstName(), returnedRecord
                    .getThirdFirstName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
