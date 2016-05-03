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
package mbg.test.ib2j5.hierarchical;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.datesAreEqual;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static mbg.test.common.util.TestUtilities.timesAreEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mbg.test.ib2j5.generated.hierarchical.dao.AwfulTableDAO;
import mbg.test.ib2j5.generated.hierarchical.dao.FieldsblobsDAO;
import mbg.test.ib2j5.generated.hierarchical.dao.PkblobsDAO;
import mbg.test.ib2j5.generated.hierarchical.dao.PkfieldsDAO;
import mbg.test.ib2j5.generated.hierarchical.dao.PkfieldsblobsDAO;
import mbg.test.ib2j5.generated.hierarchical.dao.PkonlyDAO;
import mbg.test.ib2j5.generated.hierarchical.dao.subpackage.FieldsonlyDAO;
import mbg.test.ib2j5.generated.hierarchical.model.AwfulTable;
import mbg.test.ib2j5.generated.hierarchical.model.AwfulTableExample;
import mbg.test.ib2j5.generated.hierarchical.model.AwfulTableKey;
import mbg.test.ib2j5.generated.hierarchical.model.Fieldsblobs;
import mbg.test.ib2j5.generated.hierarchical.model.FieldsblobsExample;
import mbg.test.ib2j5.generated.hierarchical.model.FieldsblobsWithBLOBs;
import mbg.test.ib2j5.generated.hierarchical.model.PkblobsExample;
import mbg.test.ib2j5.generated.hierarchical.model.PkblobsKey;
import mbg.test.ib2j5.generated.hierarchical.model.PkblobsWithBLOBs;
import mbg.test.ib2j5.generated.hierarchical.model.Pkfields;
import mbg.test.ib2j5.generated.hierarchical.model.PkfieldsExample;
import mbg.test.ib2j5.generated.hierarchical.model.PkfieldsKey;
import mbg.test.ib2j5.generated.hierarchical.model.Pkfieldsblobs;
import mbg.test.ib2j5.generated.hierarchical.model.PkfieldsblobsExample;
import mbg.test.ib2j5.generated.hierarchical.model.PkfieldsblobsKey;
import mbg.test.ib2j5.generated.hierarchical.model.PkfieldsblobsWithBLOBs;
import mbg.test.ib2j5.generated.hierarchical.model.PkonlyExample;
import mbg.test.ib2j5.generated.hierarchical.model.PkonlyKey;
import mbg.test.ib2j5.generated.hierarchical.model.subpackage.Fieldsonly;
import mbg.test.ib2j5.generated.hierarchical.model.subpackage.FieldsonlyExample;

import org.junit.Test;

/**
 * @author Jeff Butler
 * 
 */
public class HierarchicalJava5Test extends AbstractHierarchicalJava5Test {

    @Test
    public void testFieldsOnlyInsert() {
        FieldsonlyDAO dao = getFieldsonlyDAO();

        try {
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            dao.insert(record);

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldEqualTo(5);

            List<Fieldsonly> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());

            Fieldsonly returnedRecord = answer.get(0);
            assertEquals(record.getIntegerfield(), returnedRecord
                    .getIntegerfield());
            assertEquals(record.getDoublefield(), returnedRecord
                    .getDoublefield());
            assertEquals(record.getFloatfield(), returnedRecord.getFloatfield());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsOnlySelectByExample() {
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

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);

            List<Fieldsonly> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());

            example = new FieldsonlyExample();
            answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsOnlySelectByExampleNoCriteria() {
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

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria();

            List<Fieldsonly> answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsOnlyDeleteByExample() {
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

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);

            int rows = dao.deleteByExample(example);
            assertEquals(2, rows);

            example = new FieldsonlyExample();
            List<Fieldsonly> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsOnlyCountByExample() {
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

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);
            int rows = dao.countByExample(example);
            assertEquals(2, rows);

            example.clear();
            rows = dao.countByExample(example);
            assertEquals(3, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlyInsert() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            List<PkonlyKey> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());

            PkonlyKey returnedRecord = answer.get(0);
            assertEquals(key.getId(), returnedRecord.getId());
            assertEquals(key.getSeqNum(), returnedRecord.getSeqNum());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlyDeleteByPrimaryKey() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(5);
            key.setSeqNum(6);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            List<PkonlyKey> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());

            key = new PkonlyKey();
            key.setId(5);
            key.setSeqNum(6);
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlyDeleteByExample() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(5);
            key.setSeqNum(6);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(7);
            key.setSeqNum(8);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            int rows = dao.deleteByExample(example);
            assertEquals(2, rows);

            example = new PkonlyExample();
            List<PkonlyKey> answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlySelectByExample() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(5);
            key.setSeqNum(6);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(7);
            key.setSeqNum(8);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            List<PkonlyKey> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlySelectByExampleNoCriteria() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(5);
            key.setSeqNum(6);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(7);
            key.setSeqNum(8);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria();
            List<PkonlyKey> answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKOnlyCountByExample() {
        PkonlyDAO dao = getPkonlyDAO();

        try {
            PkonlyKey key = new PkonlyKey();
            key.setId(1);
            key.setSeqNum(3);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(5);
            key.setSeqNum(6);
            dao.insert(key);

            key = new PkonlyKey();
            key.setId(7);
            key.setSeqNum(8);
            dao.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            int rows = dao.countByExample(example);
            assertEquals(2, rows);

            example.clear();
            rows  = dao.countByExample(example);
            assertEquals(3, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsInsert() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setDatefield(new Date());
            record.setDecimal100field(10L);
            record.setDecimal155field(new BigDecimal("15.12345"));
            record.setDecimal30field((short) 3);
            record.setDecimal60field(6);
            record.setFirstname("Jeff");
            record.setId1(1);
            record.setId2(2);
            record.setLastname("Butler");
            record.setTimefield(new Date());
            record.setTimestampfield(new Date());

            dao.insert(record);

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKey() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);

            dao.insert(record);

            record.setFirstname("Scott");
            record.setLastname("Jones");

            int rows = dao.updateByPrimaryKey(record);
            assertEquals(1, rows);

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

            Pkfields record2 = dao.selectByPrimaryKey(key);

            assertEquals(record.getFirstname(), record2.getFirstname());
            assertEquals(record.getLastname(), record2.getLastname());
            assertEquals(record.getId1(), record2.getId1());
            assertEquals(record.getId2(), record2.getId2());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKeySelective() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setDecimal60field(5);
            record.setId1(1);
            record.setId2(2);

            dao.insert(record);

            Pkfields newRecord = new Pkfields();
            newRecord.setId1(1);
            newRecord.setId2(2);
            newRecord.setFirstname("Scott");
            newRecord.setDecimal60field(4);

            int rows = dao.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

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

    @Test
    public void testPKfieldsDeleteByPrimaryKey() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);

            dao.insert(record);

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            PkfieldsExample example = new PkfieldsExample();
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(0, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsDeleteByExample() {
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

            PkfieldsExample example = new PkfieldsExample();
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());

            example = new PkfieldsExample();
            example.createCriteria().andLastnameLike("J%");
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);

            example = new PkfieldsExample();
            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsSelectByPrimaryKey() {
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

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(3);
            key.setId2(4);
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

    @Test
    public void testPKFieldsSelectByExampleLike() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            dao.insert(record);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameLike("B%");
            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNotLike() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            dao.insert(record);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameNotLike("B%");
            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleComplexLike() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            dao.insert(record);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameLike("B%").andId2EqualTo(3);
            example.or(example.createCriteria().andFirstnameLike("Wi%"));

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleIn() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            dao.insert(record);

            List<Integer> ids = new ArrayList<Integer>();
            ids.add(1);
            ids.add(3);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andId2In(ids);

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(4, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(3);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleBetween() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            dao.insert(record);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andId2Between(1, 3);

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(6, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNoCriteria() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            dao.insert(record);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria();

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(6, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleEscapedFields() {
        PkfieldsDAO dao = getPkfieldsDAO();

        try {
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            record.setWierdField(11);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            record.setWierdField(22);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            record.setWierdField(33);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            record.setWierdField(44);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            record.setWierdField(55);
            dao.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            record.setWierdField(66);
            dao.insert(record);

            List<Integer> values = new ArrayList<Integer>();
            values.add(11);
            values.add(22);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andWierdFieldLessThan(40).andWierdFieldIn(
                    values);

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsCountByExample() {
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

    @Test
    public void testPKBlobsInsert() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<PkblobsWithBLOBs> answer = dao
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            PkblobsWithBLOBs returnedRecord = answer.get(0);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeyWithBLOBs() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            int rows = dao.updateByPrimaryKey(record);
            assertEquals(1, rows);

            PkblobsKey key = new PkblobsKey();
            key.setId(3);

            PkblobsWithBLOBs newRecord = dao.selectByPrimaryKey(key);

            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeySelective() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsWithBLOBs newRecord = new PkblobsWithBLOBs();
            newRecord.setId(3);
            newRecord.setBlob2(generateRandomBlob());
            dao.updateByPrimaryKeySelective(newRecord);

            PkblobsKey key = new PkblobsKey();
            key.setId(3);

            PkblobsWithBLOBs returnedRecord = dao.selectByPrimaryKey(key);
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

    @Test
    public void testPKBlobsDeleteByPrimaryKey() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<PkblobsKey> answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());

            PkblobsKey key = new PkblobsKey();
            key.setId(3);
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            example = new PkblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(0, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsDeleteByExample() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            record = new PkblobsWithBLOBs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<PkblobsKey> answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());

            example = new PkblobsExample();
            example.createCriteria().andIdLessThan(4);
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);

            example = new PkblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsSelectByPrimaryKey() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            record = new PkblobsWithBLOBs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsKey key = new PkblobsKey();
            key.setId(6);
            PkblobsWithBLOBs newRecord = dao.selectByPrimaryKey(key);
            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithoutBlobs() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            record = new PkblobsWithBLOBs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            List<PkblobsKey> answer = dao.selectByExampleWithoutBLOBs(example);

            assertEquals(1, answer.size());

            PkblobsKey key = answer.get(0);
            assertFalse(key instanceof PkblobsWithBLOBs);
            assertEquals(6, key.getId().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithoutBlobsNoCriteria() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            record = new PkblobsWithBLOBs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria();
            List<PkblobsKey> answer = dao.selectByExampleWithoutBLOBs(example);

            assertEquals(2, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithBlobs() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            record = new PkblobsWithBLOBs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            List<PkblobsWithBLOBs> answer = dao
                    .selectByExampleWithBLOBs(example);

            assertEquals(1, answer.size());

            PkblobsWithBLOBs newRecord = answer.get(0);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKBlobsCountByExample() {
        PkblobsDAO dao = getPkblobsDAO();

        try {
            PkblobsWithBLOBs record = new PkblobsWithBLOBs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            record = new PkblobsWithBLOBs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdLessThan(4);
            int rows = dao.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsInsert() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<PkfieldsblobsWithBLOBs> answer = dao
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            PkfieldsblobsWithBLOBs returnedRecord = answer.get(0);
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeyWithBLOBs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsWithBLOBs updateRecord = new PkfieldsblobsWithBLOBs();
            updateRecord.setId1(3);
            updateRecord.setId2(4);
            updateRecord.setFirstname("Scott");
            updateRecord.setLastname("Jones");
            updateRecord.setBlob1(generateRandomBlob());

            int rows = dao.updateByPrimaryKey(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(3);
            key.setId2(4);
            PkfieldsblobsWithBLOBs newRecord = dao.selectByPrimaryKey(key);
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

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeyWithoutBLOBs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs();
            updateRecord.setId1(3);
            updateRecord.setId2(4);
            updateRecord.setFirstname("Scott");
            updateRecord.setLastname("Jones");

            int rows = dao.updateByPrimaryKey(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(3);
            key.setId2(4);
            PkfieldsblobsWithBLOBs newRecord = dao.selectByPrimaryKey(key);
            assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeySelective() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsWithBLOBs updateRecord = new PkfieldsblobsWithBLOBs();
            updateRecord.setId1(3);
            updateRecord.setId2(4);
            updateRecord.setLastname("Jones");

            int rows = dao.updateByPrimaryKeySelective(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(3);
            key.setId2(4);
            PkfieldsblobsWithBLOBs returnedRecord = dao.selectByPrimaryKey(key);
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

    @Test
    public void testPKFieldsBlobsDeleteByPrimaryKey() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            record = new PkfieldsblobsWithBLOBs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = dao
                    .selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());

            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(5);
            key.setId2(6);
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            example = new PkfieldsblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsDeleteByExample() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            record = new PkfieldsblobsWithBLOBs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = dao
                    .selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());

            example = new PkfieldsblobsExample();
            example.createCriteria().andId1NotEqualTo(3);
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);

            example = new PkfieldsblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByPrimaryKey() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            record = new PkfieldsblobsWithBLOBs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = dao
                    .selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());

            PkfieldsblobsKey key = new PkfieldsblobsKey();
            key.setId1(5);
            key.setId2(6);
            PkfieldsblobsWithBLOBs newRecord = dao.selectByPrimaryKey(key);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithoutBlobs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            record = new PkfieldsblobsWithBLOBs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId2EqualTo(6);
            List<Pkfieldsblobs> answer = dao
                    .selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());

            Pkfieldsblobs newRecord = answer.get(0);
            assertFalse(newRecord instanceof PkfieldsblobsWithBLOBs);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobs() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            record = new PkfieldsblobsWithBLOBs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId2EqualTo(6);
            List<PkfieldsblobsWithBLOBs> answer = dao
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            PkfieldsblobsWithBLOBs newRecord = answer.get(0);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            record = new PkfieldsblobsWithBLOBs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria();
            List<PkfieldsblobsWithBLOBs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPKFieldsBlobsCountByExample() {
        PkfieldsblobsDAO dao = getPkfieldsblobsDAO();

        try {
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            record = new PkfieldsblobsWithBLOBs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            dao.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId1NotEqualTo(3);
            int rows = dao.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsInsert() {
        FieldsblobsDAO dao = getFieldsblobsDAO();

        try {
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            dao.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            List<FieldsblobsWithBLOBs> answer = dao
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            FieldsblobsWithBLOBs returnedRecord = answer.get(0);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsDeleteByExample() {
        FieldsblobsDAO dao = getFieldsblobsDAO();

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
            List<Fieldsblobs> answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(2, answer.size());

            example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);

            example = new FieldsblobsExample();
            answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithoutBlobs() {
        FieldsblobsDAO dao = getFieldsblobsDAO();

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
            List<Fieldsblobs> answer = dao.selectByExampleWithoutBLOBs(example);
            assertEquals(1, answer.size());

            Fieldsblobs newRecord = answer.get(0);
            assertFalse(newRecord instanceof FieldsblobsWithBLOBs);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobs() {
        FieldsblobsDAO dao = getFieldsblobsDAO();

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
            List<FieldsblobsWithBLOBs> answer = dao
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            FieldsblobsWithBLOBs newRecord = answer.get(0);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        FieldsblobsDAO dao = getFieldsblobsDAO();

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
            List<FieldsblobsWithBLOBs> answer = dao.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldsBlobsCountByExample() {
        FieldsblobsDAO dao = getFieldsblobsDAO();

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
            int rows = dao.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = dao.countByExample(example);
            assertEquals(2, rows);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableInsert() {
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

            Integer generatedCustomerId = dao.insert(record);
            assertEquals(57, generatedCustomerId.intValue());

            AwfulTableKey key = new AwfulTableKey();
            key.setCustomerId(generatedCustomerId);
            AwfulTable returnedRecord = dao.selectByPrimaryKey(key);

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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableInsertSelective() {
        AwfulTableDAO dao = getAwfulTableDAO();

        try {
            AwfulTable record = new AwfulTable();

            record.seteMail("fred@fred.com");
            record.setEmailaddress("alsofred@fred.com");
            record.setFrom("from field");
            record.setId1(1);
            record.setId2(2);
            record.setId5(5);
            record.setId6(6);
            record.setId7(7);
            record.setSecondFirstName("fred2");
            record.setThirdFirstName("fred3");

            Integer generatedCustomerId = dao.insertSelective(record);
            assertEquals(57, generatedCustomerId.intValue());

            AwfulTableKey key = new AwfulTableKey();
            key.setCustomerId(generatedCustomerId);
            AwfulTable returnedRecord = dao.selectByPrimaryKey(key);

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
    
    @Test
    public void testAwfulTableUpdateByPrimaryKey() {
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

            Integer generatedCustomerId = dao.insert(record);

            record.setId1(11);
            record.setId2(22);

            int rows = dao.updateByPrimaryKey(record);
            assertEquals(1, rows);

            AwfulTableKey key = new AwfulTableKey();
            key.setCustomerId(generatedCustomerId);
            AwfulTable returnedRecord = dao.selectByPrimaryKey(key);

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
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableUpdateByPrimaryKeySelective() {
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

            Integer generatedCustomerId = dao.insert(record);

            AwfulTable newRecord = new AwfulTable();
            newRecord.setCustomerId(generatedCustomerId);
            newRecord.setId1(11);
            newRecord.setId2(22);

            int rows = dao.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);

            AwfulTableKey key = new AwfulTableKey();
            key.setCustomerId(generatedCustomerId);
            AwfulTable returnedRecord = dao.selectByPrimaryKey(key);

            assertEquals(generatedCustomerId, returnedRecord.getCustomerId());
            assertEquals(record.geteMail(), returnedRecord.geteMail());
            assertEquals(record.getEmailaddress(), returnedRecord
                    .getEmailaddress());
            assertEquals(record.getFirstFirstName(), returnedRecord
                    .getFirstFirstName());
            assertEquals(record.getFrom(), returnedRecord.getFrom());
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
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

    @Test
    public void testAwfulTableDeleteByPrimaryKey() {
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

            Integer generatedCustomerId = dao.insert(record);

            AwfulTableKey key = new AwfulTableKey();
            key.setCustomerId(generatedCustomerId);
            int rows = dao.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            AwfulTableExample example = new AwfulTableExample();
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(0, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableDeleteByExample() {
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

            AwfulTableExample example = new AwfulTableExample();
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());

            example = new AwfulTableExample();
            example.createCriteria().andEMailLike("fred@%");
            int rows = dao.deleteByExample(example);
            assertEquals(1, rows);

            example.clear();
            answer = dao.selectByExample(example);
            assertEquals(1, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableSelectByPrimaryKey() {
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

            Integer generatedKey = dao.insert(record);

            AwfulTableKey key = new AwfulTableKey();
            key.setCustomerId(generatedKey);
            AwfulTable returnedRecord = dao.selectByPrimaryKey(key);

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

    @Test
    public void testAwfulTableSelectByExampleLike() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFrom("from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFrom("from field");
            record.setId1(111);
            record.setId2(222);
            record.setId5(555);
            record.setId6(666);
            record.setId7(777);
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFrom("from field");
            record.setId1(1111);
            record.setId2(2222);
            record.setId5(5555);
            record.setId6(6666);
            record.setId7(7777);
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFrom("from field");
            record.setId1(11111);
            record.setId2(22222);
            record.setId5(55555);
            record.setId6(66666);
            record.setId7(77777);
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFrom("from field");
            record.setId1(111111);
            record.setId2(222222);
            record.setId5(555555);
            record.setId6(666666);
            record.setId7(777777);
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);

            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andFirstFirstNameLike("b%");
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(1111, returnedRecord.getId1().intValue());
            assertEquals(2222, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(11111, returnedRecord.getId1().intValue());
            assertEquals(22222, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(111111, returnedRecord.getId1().intValue());
            assertEquals(222222, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleNotLike() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFrom("from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFrom("from field");
            record.setId1(111);
            record.setId2(222);
            record.setId5(555);
            record.setId6(666);
            record.setId7(777);
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFrom("from field");
            record.setId1(1111);
            record.setId2(2222);
            record.setId5(5555);
            record.setId6(6666);
            record.setId7(7777);
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFrom("from field");
            record.setId1(11111);
            record.setId2(22222);
            record.setId5(55555);
            record.setId6(66666);
            record.setId7(77777);
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFrom("from field");
            record.setId1(111111);
            record.setId2(222222);
            record.setId5(555555);
            record.setId6(666666);
            record.setId7(777777);
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);

            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andFirstFirstNameNotLike("b%");
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(111, returnedRecord.getId1().intValue());
            assertEquals(222, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testAwfulTableSelectByExampleComplexLike() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFrom("from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFrom("from field");
            record.setId1(111);
            record.setId2(222);
            record.setId5(555);
            record.setId6(666);
            record.setId7(777);
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFrom("from field");
            record.setId1(1111);
            record.setId2(2222);
            record.setId5(5555);
            record.setId6(6666);
            record.setId7(7777);
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFrom("from field");
            record.setId1(11111);
            record.setId2(22222);
            record.setId5(55555);
            record.setId6(66666);
            record.setId7(77777);
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFrom("from field");
            record.setId1(111111);
            record.setId2(222222);
            record.setId5(555555);
            record.setId6(666666);
            record.setId7(777777);
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);

            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andFirstFirstNameLike("b%").andId2EqualTo(222222);
            example.or(example.createCriteria().andFirstFirstNameLike("wi%"));
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(2, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(111111, returnedRecord.getId1().intValue());
            assertEquals(222222, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleIn() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFrom("from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFrom("from field");
            record.setId1(111);
            record.setId2(222);
            record.setId5(555);
            record.setId6(666);
            record.setId7(777);
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFrom("from field");
            record.setId1(1111);
            record.setId2(2222);
            record.setId5(5555);
            record.setId6(6666);
            record.setId7(7777);
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFrom("from field");
            record.setId1(11111);
            record.setId2(22222);
            record.setId5(55555);
            record.setId6(66666);
            record.setId7(77777);
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFrom("from field");
            record.setId1(111111);
            record.setId2(222222);
            record.setId5(555555);
            record.setId6(666666);
            record.setId7(777777);
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);

            List<Integer> ids = new ArrayList<Integer>();
            ids.add(1);
            ids.add(11);

            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andId1In(ids);
            example.setOrderByClause("\"A_CuStOmEr iD\"");
            List<AwfulTable> answer = dao.selectByExample(example);
            
            assertEquals(2, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleBetween() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFrom("from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFrom("from field");
            record.setId1(111);
            record.setId2(222);
            record.setId5(555);
            record.setId6(666);
            record.setId7(777);
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFrom("from field");
            record.setId1(1111);
            record.setId2(2222);
            record.setId5(5555);
            record.setId6(6666);
            record.setId7(7777);
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFrom("from field");
            record.setId1(11111);
            record.setId2(22222);
            record.setId5(55555);
            record.setId6(66666);
            record.setId7(77777);
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFrom("from field");
            record.setId1(111111);
            record.setId2(222222);
            record.setId5(555555);
            record.setId6(666666);
            record.setId7(777777);
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);

            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria().andId1Between(1, 1000);
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(3, answer.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleNoCriteria() {
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
            record.seteMail("wilma@wilma.com");
            record.setEmailaddress("alsoWilma@wilma.com");
            record.setFirstFirstName("wilma1");
            record.setFrom("from field");
            record.setId1(11);
            record.setId2(22);
            record.setId5(55);
            record.setId6(66);
            record.setId7(77);
            record.setSecondFirstName("wilma2");
            record.setThirdFirstName("wilma3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("pebbles@pebbles.com");
            record.setEmailaddress("alsoPebbles@pebbles.com");
            record.setFirstFirstName("pebbles1");
            record.setFrom("from field");
            record.setId1(111);
            record.setId2(222);
            record.setId5(555);
            record.setId6(666);
            record.setId7(777);
            record.setSecondFirstName("pebbles2");
            record.setThirdFirstName("pebbles3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("barney@barney.com");
            record.setEmailaddress("alsoBarney@barney.com");
            record.setFirstFirstName("barney1");
            record.setFrom("from field");
            record.setId1(1111);
            record.setId2(2222);
            record.setId5(5555);
            record.setId6(6666);
            record.setId7(7777);
            record.setSecondFirstName("barney2");
            record.setThirdFirstName("barney3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("betty@betty.com");
            record.setEmailaddress("alsoBetty@betty.com");
            record.setFirstFirstName("betty1");
            record.setFrom("from field");
            record.setId1(11111);
            record.setId2(22222);
            record.setId5(55555);
            record.setId6(66666);
            record.setId7(77777);
            record.setSecondFirstName("betty2");
            record.setThirdFirstName("betty3");
            dao.insert(record);

            record = new AwfulTable();
            record.seteMail("bammbamm@bammbamm.com");
            record.setEmailaddress("alsoBammbamm@bammbamm.com");
            record.setFirstFirstName("bammbamm1");
            record.setFrom("from field");
            record.setId1(111111);
            record.setId2(222222);
            record.setId5(555555);
            record.setId6(666666);
            record.setId7(777777);
            record.setSecondFirstName("bammbamm2");
            record.setThirdFirstName("bammbamm3");
            dao.insert(record);

            AwfulTableExample example = new AwfulTableExample();
            example.createCriteria();
            example.setOrderByClause("\"A_CuStOmEr iD\" desc");
            
            List<AwfulTable> answer = dao.selectByExample(example);
            assertEquals(6, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(111111, returnedRecord.getId1().intValue());
            returnedRecord = answer.get(1);
            assertEquals(11111, returnedRecord.getId1().intValue());
            returnedRecord = answer.get(2);
            assertEquals(1111, returnedRecord.getId1().intValue());
            returnedRecord = answer.get(3);
            assertEquals(111, returnedRecord.getId1().intValue());
            returnedRecord = answer.get(4);
            assertEquals(11, returnedRecord.getId1().intValue());
            returnedRecord = answer.get(5);
            assertEquals(1, returnedRecord.getId1().intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAwfulTableCountByExample() {
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
