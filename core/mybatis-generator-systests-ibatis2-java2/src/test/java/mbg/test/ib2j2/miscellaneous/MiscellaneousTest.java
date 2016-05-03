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
package mbg.test.ib2j2.miscellaneous;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mbg.test.common.FirstName;
import mbg.test.common.MyTime;
import mbg.test.common.util.TestUtilities;
import mbg.test.ib2j2.generated.miscellaneous.dao.MyObjectDAO;
import mbg.test.ib2j2.generated.miscellaneous.dao.RegexrenameDAO;
import mbg.test.ib2j2.generated.miscellaneous.model.MyObject;
import mbg.test.ib2j2.generated.miscellaneous.model.MyObjectExample;
import mbg.test.ib2j2.generated.miscellaneous.model.MyObjectKey;
import mbg.test.ib2j2.generated.miscellaneous.model.Regexrename;

/**
 * @author Jeff Butler
 * 
 */
public class MiscellaneousTest extends AbstractMiscellaneousTest {

    public void testMyObjectInsert() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            record.setStartDate(new Date());
            record.setDecimal100field(new Long(10L));
            record.setDecimal155field(new Double(15.12345));
            record.setDecimal60field(6);
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            record.setLastname("Butler");

            MyTime myTime = new MyTime();
            myTime.setHours(12);
            myTime.setMinutes(34);
            myTime.setSeconds(05);
            record.setTimefield(myTime);
            record.setTimestampfield(new Date());

            dao.insertMyObject(record);

            MyObjectKey key = new MyObjectKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));

            MyObject returnedRecord = dao.selectMyObjectByPrimaryKey(key);
            assertNotNull(returnedRecord);

            assertTrue(TestUtilities.datesAreEqual(record.getStartDate(), returnedRecord
                    .getStartDate()));
            assertEquals(record.getDecimal100field(), returnedRecord
                    .getDecimal100field());
            assertEquals(record.getDecimal155field(), returnedRecord
                    .getDecimal155field());
            assertEquals(record.getDecimal60field(), returnedRecord
                    .getDecimal60field());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertEquals(record.getTimefield(), returnedRecord.getTimefield());
            assertEquals(record.getTimestampfield(), returnedRecord
                    .getTimestampfield());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectUpdateByPrimaryKey() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));

            dao.insertMyObject(record);

            fn = new FirstName();
            fn.setValue("Scott");
            record.setFirstname(fn);
            record.setLastname("Jones");

            int rows = dao.updateMyObjectByPrimaryKey(record);
            assertEquals(1, rows);

            MyObjectKey key = new MyObjectKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));

            MyObject record2 = dao.selectMyObjectByPrimaryKey(key);

            assertEquals(record.getFirstname(), record2.getFirstname());
            assertEquals(record.getLastname(), record2.getLastname());
            assertEquals(record.getId1(), record2.getId1());
            assertEquals(record.getId2(), record2.getId2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectUpdateByPrimaryKeySelective() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setDecimal60field(5);
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));

            dao.insertMyObject(record);

            MyObject newRecord = new MyObject();
            newRecord.setId1(new Integer(1));
            newRecord.setId2(new Integer(2));
            fn = new FirstName();
            fn.setValue("Scott");
            newRecord.setFirstname(fn);
            record.setStartDate(new Date());

            int rows = dao.updateMyObjectByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);

            MyObjectKey key = new MyObjectKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));

            MyObject returnedRecord = dao.selectMyObjectByPrimaryKey(key);

            assertTrue(TestUtilities.datesAreEqual(newRecord.getStartDate(), returnedRecord
                    .getStartDate()));
            assertEquals(record.getDecimal100field(), returnedRecord
                    .getDecimal100field());
            assertEquals(record.getDecimal155field(), returnedRecord
                    .getDecimal155field());

            // with columns mapped to primitive types, the column is always
            // updated
            assertEquals(newRecord.getDecimal60field(), returnedRecord
                    .getDecimal60field());

            assertEquals(newRecord.getFirstname(), returnedRecord
                    .getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertEquals(record.getTimefield(), returnedRecord.getTimefield());
            assertEquals(record.getTimestampfield(), returnedRecord
                    .getTimestampfield());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectDeleteByPrimaryKey() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));

            dao.insertMyObject(record);

            MyObjectKey key = new MyObjectKey();
            key.setId1(new Integer(1));
            key.setId2(new Integer(2));

            int rows = dao.deleteMyObjectByPrimaryKey(key);
            assertEquals(1, rows);

            MyObjectExample example = new MyObjectExample();
            List answer = dao.selectMyObjectByExample(example);
            assertEquals(0, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectDeleteByExample() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));

            dao.insertMyObject(record);

            MyObjectExample example = new MyObjectExample();
            List answer = dao.selectMyObjectByExample(example);
            assertEquals(2, answer.size());

            example = new MyObjectExample();
            example.createCriteria().andLastnameLike("J%");
            int rows = dao.deleteMyObjectByExample(example);
            assertEquals(1, rows);

            example = new MyObjectExample();
            answer = dao.selectMyObjectByExample(example);
            assertEquals(1, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectSelectByPrimaryKey() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));
            dao.insertMyObject(record);

            MyObjectKey key = new MyObjectKey();
            key.setId1(new Integer(3));
            key.setId2(new Integer(4));
            MyObject newRecord = dao.selectMyObjectByPrimaryKey(key);

            assertNotNull(newRecord);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectSelectByExampleLike() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            MyObjectExample example = new MyObjectExample();
            fn = new FirstName();
            fn.setValue("B%");
            example.createCriteria().andFirstnameLike(fn);
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectMyObjectByExample(example);
            assertEquals(3, answer.size());
            MyObject returnedRecord = (MyObject) answer.get(0);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectSelectByExampleNotLike() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            MyObjectExample example = new MyObjectExample();
            fn = new FirstName();
            fn.setValue("B%");
            example.createCriteria().andFirstnameNotLike(fn);
            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectMyObjectByExample(example);
            assertEquals(3, answer.size());
            MyObject returnedRecord = (MyObject) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(2);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectSelectByExampleComplexLike() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            MyObjectExample example = new MyObjectExample();
            fn = new FirstName();
            fn.setValue("B%");
            example.createCriteria().andFirstnameLike(fn).andId2EqualTo(
                    new Integer(3));
            fn = new FirstName();
            fn.setValue("W%");
            example.or(example.createCriteria().andFirstnameLike(fn));

            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectMyObjectByExample(example);
            assertEquals(2, answer.size());
            MyObject returnedRecord = (MyObject) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectSelectByExampleIn() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            List ids = new ArrayList();
            ids.add(new Integer(1));
            ids.add(new Integer(3));

            MyObjectExample example = new MyObjectExample();
            example.createCriteria().andId2In(ids);

            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectMyObjectByExample(example);
            assertEquals(4, answer.size());
            MyObject returnedRecord = (MyObject) answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = (MyObject) answer.get(3);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectSelectByExampleBetween() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(new Integer(1));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(1));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(new Integer(2));
            record.setId2(new Integer(3));
            dao.insertMyObject(record);

            MyObjectExample example = new MyObjectExample();
            example.createCriteria().andId2Between(new Integer(1),
                    new Integer(3));

            example.setOrderByClause("ID1, ID2");
            List answer = dao.selectMyObjectByExample(example);
            assertEquals(6, answer.size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectSelectByExampleTimeEquals() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            record.setStartDate(new Date());
            record.setDecimal100field(new Long(10L));
            record.setDecimal155field(new Double(15.12345));
            record.setDecimal60field(6);
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            record.setLastname("Butler");

            MyTime myTime = new MyTime();
            myTime.setHours(12);
            myTime.setMinutes(34);
            myTime.setSeconds(05);
            record.setTimefield(myTime);
            record.setTimestampfield(new Date());

            dao.insertMyObject(record);

            MyObjectExample example = new MyObjectExample();
            example.createCriteria().andTimefieldEqualTo(myTime);
            List results = dao.selectMyObjectByExample(example);
            assertEquals(1, results.size());
            MyObject returnedRecord = (MyObject) results.get(0);

            assertTrue(TestUtilities.datesAreEqual(record.getStartDate(), returnedRecord
                    .getStartDate()));
            assertEquals(record.getDecimal100field(), returnedRecord
                    .getDecimal100field());
            assertEquals(record.getDecimal155field(), returnedRecord
                    .getDecimal155field());
            assertEquals(record.getDecimal60field(), returnedRecord
                    .getDecimal60field());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertEquals(record.getTimefield(), returnedRecord.getTimefield());
            assertEquals(record.getTimestampfield(), returnedRecord
                    .getTimestampfield());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testFieldIgnored() {
        try {
            MyObject.class.getDeclaredField("decimal30field");
            fail("decimal30field should be ignored");
        } catch (NoSuchFieldException e) {
            // ignore (normal case)
        }
    }

    public void testMyObjectUpdateByExampleSelective() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));

            dao.insertMyObject(record);

            MyObject newRecord = new MyObject();
            newRecord.setLastname("Barker");
            
            MyObjectExample example = new MyObjectExample();
            fn = new FirstName();
            fn.setValue("B%");
            example.createCriteria().andFirstnameLike(fn);
            int rows = dao.updateMyObjectByExampleSelective(newRecord, example);
            assertEquals(1, rows);

            List answer = dao.selectMyObjectByExample(example);
            assertEquals(1, answer.size());
            
            MyObject returnedRecord = (MyObject) answer.get(0);
            
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testMyObjectUpdateByExample() {
        MyObjectDAO dao = getMyObjectDAO();

        try {
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(new Integer(1));
            record.setId2(new Integer(2));
            dao.insertMyObject(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(new Integer(3));
            record.setId2(new Integer(4));

            dao.insertMyObject(record);

            MyObject newRecord = new MyObject();
            newRecord.setLastname("Barker");
            newRecord.setId1(new Integer(3));
            newRecord.setId2(new Integer(4));
            
            MyObjectExample example = new MyObjectExample();
            example.createCriteria()
                .andId1EqualTo(new Integer(3))
                .andId2EqualTo(new Integer(4));
            int rows = dao.updateMyObjectByExample(newRecord, example);
            assertEquals(1, rows);

            List answer = dao.selectMyObjectByExample(example);
            assertEquals(1, answer.size());
            
            MyObject returnedRecord = (MyObject) answer.get(0);
            
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertNull(returnedRecord.getFirstname());
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testRegexRenameInsert() {
        RegexrenameDAO dao = getRegexrenameDAO();
        
        try {
            Regexrename record = new Regexrename();
            record.setAddress("123 Main Street");
            record.setName("Fred");
            record.setZipCode("99999");
            
            dao.insertRegexrename(record);
            
            Regexrename returnedRecord = dao.selectRegexrenameByPrimaryKey(new Integer(1));
            
            assertEquals(record.getAddress(), returnedRecord.getAddress());
            assertEquals(record.getId(), returnedRecord.getId());
            assertEquals(record.getName(), returnedRecord.getName());
            assertEquals(record.getZipCode(), returnedRecord.getZipCode());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    public void testRegexRenameInsertSelective() {
        RegexrenameDAO dao = getRegexrenameDAO();
        
        try {
            Regexrename record = new Regexrename();
            record.setZipCode("99999");
            
            dao.insertRegexrenameSelective(record);
            Integer key = new Integer(1);
            assertEquals(key, record.getId());
            
            Regexrename returnedRecord = dao.selectRegexrenameByPrimaryKey(key);
            
            assertNull(returnedRecord.getAddress());
            assertEquals(record.getId(), returnedRecord.getId());
            assertNull(returnedRecord.getName());
            assertEquals(record.getZipCode(), returnedRecord.getZipCode());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}
