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

import java.util.List;

import mbg.test.common.util.TestUtilities;
import mbg.test.ib2j2.generated.conditional.dao.AwfulTableDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsblobsDAO;
import mbg.test.ib2j2.generated.conditional.model.AwfulTable;
import mbg.test.ib2j2.generated.conditional.model.Pkblobs;
import mbg.test.ib2j2.generated.conditional.model.Pkfields;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsKey;
import mbg.test.ib2j2.generated.conditional.model.Pkfieldsblobs;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsblobsExample;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsblobsKey;

public class SelectByPrimaryKeyTest extends AbstractConditionalJava2Test {

    public void testPKFieldsSelectByPrimaryKey() {
        PkfieldsDAO dao = getPkfieldsDAO();
    
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsSelectByPrimaryKey() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(TestUtilities.generateRandomBlob());
            record.setBlob2(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            record = new Pkblobs();
            record.setId(new Integer(6));
            record.setBlob1(TestUtilities.generateRandomBlob());
            record.setBlob2(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            Pkblobs newRecord = dao.selectByPrimaryKey(new Integer(6));
            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsSelectByPrimaryKey() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();
    
        try {
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            record = new Pkfieldsblobs();
            record.setId1(new Integer(5));
            record.setId2(new Integer(6));
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(TestUtilities.generateRandomBlob());
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
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableSelectByPrimaryKey() {
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
    
            dao.insert(record);
    
            record = new AwfulTable();
            record.seteMail("fred2@fred.com");
            record.setEmailaddress("alsofred2@fred.com");
            record.setFirstFirstName("fred11");
            record.setFrom("from from field");
            record.setId1(new Integer(11));
            record.setId2(new Integer(22));
            record.setId5(new Integer(55));
            record.setId6(new Integer(66));
            record.setId7(new Integer(77));
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
