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

import mbg.test.common.util.TestUtilities;
import mbg.test.ib2j2.generated.conditional.dao.AwfulTableDAO;
import mbg.test.ib2j2.generated.conditional.dao.FieldsblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.FieldsonlyDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkfieldsblobsDAO;
import mbg.test.ib2j2.generated.conditional.dao.PkonlyDAO;
import mbg.test.ib2j2.generated.conditional.model.AwfulTable;
import mbg.test.ib2j2.generated.conditional.model.AwfulTableExample;
import mbg.test.ib2j2.generated.conditional.model.FieldsblobsExample;
import mbg.test.ib2j2.generated.conditional.model.FieldsblobsWithBLOBs;
import mbg.test.ib2j2.generated.conditional.model.Fieldsonly;
import mbg.test.ib2j2.generated.conditional.model.FieldsonlyExample;
import mbg.test.ib2j2.generated.conditional.model.Pkblobs;
import mbg.test.ib2j2.generated.conditional.model.PkblobsExample;
import mbg.test.ib2j2.generated.conditional.model.Pkfields;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsExample;
import mbg.test.ib2j2.generated.conditional.model.Pkfieldsblobs;
import mbg.test.ib2j2.generated.conditional.model.PkfieldsblobsExample;
import mbg.test.ib2j2.generated.conditional.model.PkonlyExample;
import mbg.test.ib2j2.generated.conditional.model.PkonlyKey;

public class CountByExampleTest extends AbstractConditionalJava2Test {

    public void testFieldsOnlyCountByExample() {
        FieldsonlyDAO dao = getFieldsonlyDAO();
    
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
    
            int rows = dao.countByExample(example);
            assertEquals(2, rows);
    
            example.clear();
            rows = dao.countByExample(example);
            assertEquals(3, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKOnlyCountByExample() {
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
    
            key = new PkonlyKey();
            key.setId(new Integer(7));
            key.setSeqNum(new Integer(8));
            dao.insert(key);
    
            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(new Integer(4));
            int rows = dao.countByExample(example);
            assertEquals(2, rows);
    
            example.clear();
            rows = dao.countByExample(example);
            assertEquals(3, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsBlobsCountByExample() {
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
            example.createCriteria().andId1NotEqualTo(new Integer(3));
            int rows = dao.countByExample(example);
            assertEquals(1, rows);
    
            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKFieldsCountByExample() {
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
    
            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andLastnameLike("J%");
            int rows = dao.countByExample(example);
            assertEquals(1, rows);
    
            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testPKBlobsCountByExample() {
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
    
            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdLessThan(new Integer(4));
            int rows = dao.countByExample(example);
            assertEquals(1, rows);
    
            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testFieldsBlobsCountByExample() {
        FieldsblobsDAO dao = getFieldsblobsDAO();
    
        try {
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(TestUtilities.generateRandomBlob());
            record.setBlob2(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            record = new FieldsblobsWithBLOBs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(TestUtilities.generateRandomBlob());
            record.setBlob2(TestUtilities.generateRandomBlob());
            dao.insert(record);
    
            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = dao.countByExample(example);
            assertEquals(1, rows);
    
            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testAwfulTableCountByExample() {
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
    
            dao.insert(record);
    
            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andEMailLike("fred@%");
            int rows = dao.countByExample(example);
            assertEquals(1, rows);
    
            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
