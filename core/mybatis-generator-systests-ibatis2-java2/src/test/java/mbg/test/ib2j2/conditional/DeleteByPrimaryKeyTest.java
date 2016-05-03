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
import mbg.test.ib2j2.generated.conditional.dao.PkonlyDAO;
import mbg.test.ib2j2.generated.conditional.model.AwfulTable;
import mbg.test.ib2j2.generated.conditional.model.AwfulTableExample;
import mbg.test.ib2j2.generated.conditional.model.Pkblobs;
import mbg.test.ib2j2.generated.conditional.model.PkblobsExample;
import mbg.test.ib2j2.generated.conditional.model.Pkfields;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsExample;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsKey;
import mbg.test.ib2j2.generated.conditional.model.Pkfieldsblobs;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsblobsExample;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsblobsKey;
import mbg.test.ib2j2.generated.conditional.model.PkonlyExample;
import mbg.test.ib2j2.generated.conditional.model.PkonlyKey;

public class DeleteByPrimaryKeyTest extends AbstractConditionalJava2Test {

    public void testPKOnlyDeleteByPrimaryKey() {
        PkonlyDAO dao = getPkonlyDAO();
    
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKfieldsDeleteByPrimaryKey() {
        PkfieldsDAO dao = getPkfieldsDAO();
    
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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsDeleteByPrimaryKey() {
        PkblobsDAO dao = getPkblobsDAO();
    
        try {
            Pkblobs record = new Pkblobs();
            record.setId(new Integer(3));
            record.setBlob1(TestUtilities.generateRandomBlob());
            record.setBlob2(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            PkblobsExample example = new PkblobsExample();
            List answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
    
            int rows = dao.deleteByPrimaryKey(new Integer(3));
            assertEquals(1, rows);
    
            example = new PkblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(0, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsDeleteByPrimaryKey() {
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
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);
    
            example = new PkfieldsblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableDeleteByPrimaryKey() {
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
    
            Integer generatedCustomerId = dao.insert(record);
    
            int rows = dao.deleteByPrimaryKey(generatedCustomerId);
            assertEquals(1, rows);
    
            AwfulTableExample example = new AwfulTableExample();
            List answer = dao.selectByExample(example);
            assertEquals(0, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
