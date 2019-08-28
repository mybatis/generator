/**
 *    Copyright 2006-2019 the original author or authors.
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
package mbg.test.mb3.dsql.v1.miscellaneous;

import static mbg.test.common.util.TestUtilities.datesAreEqual;
import static mbg.test.mb3.generated.dsql.v1.miscellaneous.mapper.MyObjectDynamicSqlSupport.myObject;
import static mbg.test.mb3.generated.dsql.v1.miscellaneous.mapper.RegexrenameDynamicSqlSupport.regexrename;
import static org.junit.jupiter.api.Assertions.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import mbg.test.common.FirstName;
import mbg.test.common.MyTime;
import mbg.test.mb3.common.TestEnum;
import mbg.test.mb3.generated.dsql.v1.miscellaneous.mapper.EnumtestMapper;
import mbg.test.mb3.generated.dsql.v1.miscellaneous.mapper.MyObjectMapper;
import mbg.test.mb3.generated.dsql.v1.miscellaneous.mapper.RegexrenameMapper;
import mbg.test.mb3.generated.dsql.v1.miscellaneous.model.Enumtest;
import mbg.test.mb3.generated.dsql.v1.miscellaneous.model.MyObject;
import mbg.test.mb3.generated.dsql.v1.miscellaneous.model.Regexrename;

/**
 * @author Jeff Butler
 * 
 */
public class MiscellaneousTest extends AbstractAnnotatedMiscellaneousTest {

    @Test
    public void testMyObjectinsertMyObject() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            record.setStartDate(new Date());
            record.setDecimal100field(10L);
            record.setDecimal155field(15.12345);
            record.setDecimal60field(6);
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setId1(1);
            record.setId2(2);
            record.setLastname("Butler");

            MyTime myTime = new MyTime();
            myTime.setHours(12);
            myTime.setMinutes(34);
            myTime.setSeconds(05);
            record.setTimefield(myTime);
            record.setTimestampfield(new Date());

            mapper.insert(record);

            MyObject returnedRecord = mapper.selectByPrimaryKey(2, 1);
            assertNotNull(returnedRecord);

            assertTrue(datesAreEqual(record.getStartDate(), returnedRecord
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectUpdateByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);

            mapper.insert(record);

            fn = new FirstName();
            fn.setValue("Scott");
            record.setFirstname(fn);
            record.setLastname("Jones");

            int rows = mapper.updateByPrimaryKey(record);
            assertEquals(1, rows);

            MyObject record2 = mapper.selectByPrimaryKey(2, 1);

            assertEquals(record.getFirstname(), record2.getFirstname());
            assertEquals(record.getLastname(), record2.getLastname());
            assertEquals(record.getId1(), record2.getId1());
            assertEquals(record.getId2(), record2.getId2());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setDecimal60field(5);
            record.setId1(1);
            record.setId2(2);

            mapper.insert(record);

            MyObject newRecord = new MyObject();
            newRecord.setId1(1);
            newRecord.setId2(2);
            fn = new FirstName();
            fn.setValue("Scott");
            newRecord.setFirstname(fn);
            record.setStartDate(new Date());

            int rows = mapper.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);

            MyObject returnedRecord = mapper.selectByPrimaryKey(2, 1);

            assertTrue(datesAreEqual(newRecord.getStartDate(), returnedRecord
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectDeleteByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);

            mapper.insert(record);

            int rows = mapper.deleteByPrimaryKey(2, 1);
            assertEquals(1, rows);

            List<MyObject> answer = mapper.selectByExample()
                    .build()
                    .execute();
            assertEquals(0, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectDeleteByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);

            mapper.insert(record);

            List<MyObject> answer = mapper.selectByExample()
                    .build()
                    .execute();
            assertEquals(2, answer.size());

            int rows = mapper.deleteByExample()
                    .where(myObject.lastname, isLike("J%"))
                    .build()
                    .execute();
            
            assertEquals(1, rows);

            answer = mapper.selectByExample()
                    .build()
                    .execute();
            assertEquals(1, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectSelectByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);
            mapper.insert(record);

            MyObject newRecord = mapper.selectByPrimaryKey(4, 3);

            assertNotNull(newRecord);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectSelectByExampleLike() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            fn = new FirstName();
            fn.setValue("B%");
            
            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.firstname, isLike(fn))
                    .orderBy(myObject.id1, myObject.id2)
                    .build()
                    .execute();
            
            assertEquals(3, answer.size());
            MyObject returnedRecord = answer.get(0);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectSelectByExampleNotLike() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            fn = new FirstName();
            fn.setValue("B%");
            
            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.firstname, isNotLike(fn))
                    .orderBy(myObject.id1, myObject.id2)
                    .build()
                    .execute();
            
            assertEquals(3, answer.size());
            MyObject returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectSelectByExampleComplexLike() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            FirstName fn1 = new FirstName();
            fn1.setValue("B%");
            FirstName fn2 = new FirstName();
            fn2.setValue("W%");

            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.firstname, isLike(fn1), and(myObject.id2, isEqualTo(3)))
                    .or(myObject.firstname, isLike(fn2))
                    .orderBy(myObject.id1, myObject.id2)
                    .build()
                    .execute();
            
            
            assertEquals(2, answer.size());
            MyObject returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectSelectByExampleIn() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<Integer> ids = new ArrayList<>();
            ids.add(1);
            ids.add(3);

            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.id2, isIn(ids))
                    .orderBy(myObject.id1, myObject.id2)
                    .build()
                    .execute();
            assertEquals(4, answer.size());
            MyObject returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            assertEquals("Flintstone", returnedRecord.getLastname());
            
            returnedRecord = answer.get(1);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
            assertEquals("Flintstone", returnedRecord.getLastname());
            
            returnedRecord = answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            assertEquals("Rubble", returnedRecord.getLastname());
            
            returnedRecord = answer.get(3);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
            assertEquals("Rubble", returnedRecord.getLastname());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectSelectByExampleBetween() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.id2, isBetween(1).and(3))
                    .orderBy(myObject.id1, myObject.id2)
                    .build()
                    .execute();
            assertEquals(6, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectSelectByExampleTimeEquals() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            record.setStartDate(new Date());
            record.setDecimal100field(10L);
            record.setDecimal155field(15.12345);
            record.setDecimal60field(6);
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setId1(1);
            record.setId2(2);
            record.setLastname("Butler");

            MyTime myTime = new MyTime();
            myTime.setHours(12);
            myTime.setMinutes(34);
            myTime.setSeconds(05);
            record.setTimefield(myTime);
            record.setTimestampfield(new Date());

            mapper.insert(record);

            List<MyObject> results = mapper.selectByExample()
                    .where(myObject.timefield, isEqualTo(myTime))
                    .build()
                    .execute();
            assertEquals(1, results.size());
            MyObject returnedRecord = results.get(0);

            assertTrue(datesAreEqual(record.getStartDate(), returnedRecord
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldIgnored() {
        assertThrows(NoSuchFieldException.class, () -> {
            MyObject.class.getDeclaredField("decimal30field");
        });
    }

    @Test
    public void testMyObjectUpdateByExampleSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);

            mapper.insert(record);

            MyObject newRecord = new MyObject();
            newRecord.setLastname("Barker");
            
            fn = new FirstName();
            fn.setValue("B%");
            
            int rows = mapper.updateByExampleSelective(newRecord)
                    .where(myObject.firstname, isLike(fn))
                    .build()
                    .execute();
            assertEquals(1, rows);

            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.firstname, isLike(fn))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            MyObject returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMyObjectUpdateByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record.setFirstname(fn);
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);

            mapper.insert(record);

            MyObject newRecord = new MyObject();
            newRecord.setLastname("Barker");
            newRecord.setId1(3);
            newRecord.setId2(4);
            
            int rows = mapper.updateByExample(newRecord)
                    .where(myObject.id1, isEqualTo(3), and(myObject.id2, isEqualTo(4)))
                    .build()
                    .execute();
                            
            assertEquals(1, rows);

            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.id1, isEqualTo(3), and(myObject.id2, isEqualTo(4)))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            MyObject returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertNull(returnedRecord.getFirstname());
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testRegexRenameInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        
        try {
            RegexrenameMapper mapper = sqlSession.getMapper(RegexrenameMapper.class);
            Regexrename record = new Regexrename();
            record.setAddress("123 Main Street");
            record.setName("Fred");
            record.setZipCode("99999");
            
            mapper.insert(record);
            
            Regexrename returnedRecord = mapper.selectByPrimaryKey(1);
            
            assertEquals(record.getAddress(), returnedRecord.getAddress());
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(record.getName(), returnedRecord.getName());
            assertEquals(record.getZipCode(), returnedRecord.getZipCode());
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testRegexRenameRowbounds() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        
        try {
            RegexrenameMapper mapper = sqlSession.getMapper(RegexrenameMapper.class);
            Regexrename record = new Regexrename();
            record.setId(1);
            record.setAddress("123 Main Street");
            record.setName("Fred");
            record.setZipCode("99999");
            mapper.insert(record);
            
            record = new Regexrename();
            record.setId(2);
            record.setAddress("234 Elm Street");
            record.setName("Barney");
            record.setZipCode("99999");
            mapper.insert(record);
            
            record = new Regexrename();
            record.setId(3);
            record.setAddress("345 Maple Street");
            record.setName("Wilma");
            record.setZipCode("99999");
            mapper.insert(record);
            
            record = new Regexrename();
            record.setId(4);
            record.setAddress("456 Oak Street");
            record.setName("Betty");
            record.setZipCode("99999");
            mapper.insert(record);
            
            RowBounds rowBounds = new RowBounds(0, 2);
            List<Regexrename> records = mapper.selectByExample(rowBounds)
                    .orderBy(regexrename.id)
                    .build()
                    .execute();
            
            assertEquals(2, records.size());
            assertEquals(records.get(0).getId().intValue(), 1);
            assertEquals(records.get(1).getId().intValue(), 2);

            rowBounds = new RowBounds(2, 4);
            records = mapper.selectByExample(rowBounds)
                    .orderBy(regexrename.id)
                    .build()
                    .execute();
            
            assertEquals(2, records.size());
            assertEquals(records.get(0).getId().intValue(), 3);
            assertEquals(records.get(1).getId().intValue(), 4);
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testRegexRenameInsertSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        
        try {
            RegexrenameMapper mapper = sqlSession.getMapper(RegexrenameMapper.class);
            Regexrename record = new Regexrename();
            record.setZipCode("99999");
            
            mapper.insertSelective(record);
            Integer key = 1;
            assertEquals(key, record.getId());
            
            Regexrename returnedRecord = mapper.selectByPrimaryKey(key);
            
            assertNull(returnedRecord.getAddress());
            assertEquals(record.getId(), returnedRecord.getId());
            assertNull(record.getName(), returnedRecord.getName());
            assertEquals(record.getZipCode(), returnedRecord.getZipCode());
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testMyObjectSelectByExampleLikeInsensitive() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Fred");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Wilma");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Pebbles");
            record.setFirstname(fn);
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Barney");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Betty");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new MyObject();
            fn = new FirstName();
            fn.setValue("Bamm Bamm");
            record.setFirstname(fn);
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<MyObject> answer = mapper.selectByExample()
                    .where(myObject.lastname, isLike("RU%"))
                    .orderBy(myObject.id1, myObject.id2)
                    .build()
                    .execute();
            assertEquals(0, answer.size());
            
            answer = mapper.selectByExample()
                    .where(myObject.lastname, isLikeCaseInsensitive("RU%"))
                    .build()
                    .execute();
            assertEquals(3, answer.size());
            
            MyObject returnedRecord = answer.get(0);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(1, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testEnum() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EnumtestMapper mapper = sqlSession.getMapper(EnumtestMapper.class);
            
            Enumtest enumTest = new Enumtest();
            enumTest.setId(1);
            enumTest.setName(TestEnum.FRED);
            int rows = mapper.insert(enumTest);
            assertEquals(1, rows);
            
            List<Enumtest> returnedRecords = mapper.selectByExample()
                    .build()
                    .execute();
            assertEquals(1, returnedRecords.size());
            
            Enumtest returnedRecord = returnedRecords.get(0);
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(TestEnum.FRED, returnedRecord.getName());
        } finally {
            sqlSession.close();
        }
    }
}
