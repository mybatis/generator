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
package mbg.test.mb3.dsql.v2.miscellaneous;

import static mbg.test.common.util.TestUtilities.datesAreEqual;
import static mbg.test.mb3.generated.dsql.v2.miscellaneous.mapper.MyObjectDynamicSqlSupport.myObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import mbg.test.common.FirstName;
import mbg.test.common.MyTime;
import mbg.test.mb3.common.TestEnum;
import mbg.test.mb3.generated.dsql.v2.miscellaneous.mapper.EnumtestMapper;
import mbg.test.mb3.generated.dsql.v2.miscellaneous.mapper.MyObjectMapper;
import mbg.test.mb3.generated.dsql.v2.miscellaneous.mapper.RegexrenameMapper;
import mbg.test.mb3.generated.dsql.v2.miscellaneous.model.Enumtest;
import mbg.test.mb3.generated.dsql.v2.miscellaneous.model.MyObject;
import mbg.test.mb3.generated.dsql.v2.miscellaneous.model.Regexrename;

/**
 * @author Jeff Butler
 * 
 */
public class MiscellaneousTest extends AbstractAnnotatedMiscellaneousTest {

    @Test
    public void testMyObjectinsertMyObject() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            Optional<MyObject> returnedRecord = mapper.selectByPrimaryKey(2, 1);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertTrue(datesAreEqual(record.getStartDate(), rr.getStartDate()));
                assertEquals(record.getDecimal100field(), rr.getDecimal100field());
                assertEquals(record.getDecimal155field(), rr.getDecimal155field());
                assertEquals(record.getDecimal60field(), rr.getDecimal60field());
                assertEquals(record.getFirstname(), rr.getFirstname());
                assertEquals(record.getId1(), rr.getId1());
                assertEquals(record.getId2(), rr.getId2());
                assertEquals(record.getLastname(), rr.getLastname());
                assertEquals(record.getTimefield(), rr.getTimefield());
                assertEquals(record.getTimestampfield(), rr.getTimestampfield());
            });
        }
    }

    @Test
    public void testMyObjectUpdateByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            Optional<MyObject> record2 = mapper.selectByPrimaryKey(2, 1);

            assertThat(record2).hasValueSatisfying(r2 -> {
                assertEquals(record.getFirstname(), r2.getFirstname());
                assertEquals(record.getLastname(), r2.getLastname());
                assertEquals(record.getId1(), r2.getId1());
                assertEquals(record.getId2(), r2.getId2());
            });
        }
    }

    @Test
    public void testMyObjectUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            Optional<MyObject> returnedRecord = mapper.selectByPrimaryKey(2, 1);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertTrue(datesAreEqual(newRecord.getStartDate(), rr.getStartDate()));
                assertEquals(record.getDecimal100field(), rr.getDecimal100field());
                assertEquals(record.getDecimal155field(), rr.getDecimal155field());

                // with columns mapped to primitive types, the column is always
                // updated
                assertEquals(newRecord.getDecimal60field(), rr.getDecimal60field());

                assertEquals(newRecord.getFirstname(), rr.getFirstname());
                assertEquals(record.getId1(), rr.getId1());
                assertEquals(record.getId2(), rr.getId2());
                assertEquals(record.getLastname(), rr.getLastname());
                assertEquals(record.getTimefield(), rr.getTimefield());
                assertEquals(record.getTimestampfield(), rr.getTimestampfield());
            });
        }
    }

    @Test
    public void testMyObjectDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            List<MyObject> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(0, answer.size());
        }
    }

    @Test
    public void testMyObjectDeleteByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            List<MyObject> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(myObject.lastname, isLike("J%")));
            
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testMyObjectSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            MyObjectMapper mapper = sqlSession.getMapper(MyObjectMapper.class);
            MyObject record = new MyObject();
            FirstName fn = new FirstName();
            fn.setValue("Jeff");
            record.setFirstname(fn);
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            MyObject record1 = new MyObject();
            fn = new FirstName();
            fn.setValue("Bob");
            record1.setFirstname(fn);
            record1.setLastname("Jones");
            record1.setId1(3);
            record1.setId2(4);
            mapper.insert(record1);

            Optional<MyObject> newRecord = mapper.selectByPrimaryKey(4, 3);

            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.getFirstname(), nr.getFirstname());
                assertEquals(record1.getLastname(), nr.getLastname());
                assertEquals(record1.getId1(), nr.getId1());
                assertEquals(record1.getId2(), nr.getId2());
            });
        }
    }

    @Test
    public void testMyObjectSelectByExampleLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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
            
            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.firstname, isLike(fn1))
                    .orderBy(myObject.id1, myObject.id2));
            
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
        }
    }

    @Test
    public void testMyObjectSelectByExampleNotLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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
            
            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.firstname, isNotLike(fn1))
                    .orderBy(myObject.id1, myObject.id2));
            
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
        }
    }

    @Test
    public void testMyObjectSelectByExampleComplexLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.firstname, isLike(fn1), and(myObject.id2, isEqualTo(3)))
                    .or(myObject.firstname, isLike(fn2))
                    .orderBy(myObject.id1, myObject.id2));
            
            
            assertEquals(2, answer.size());
            MyObject returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        }
    }

    @Test
    public void testMyObjectSelectByExampleIn() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.id2, isIn(ids))
                    .orderBy(myObject.id1, myObject.id2));
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
        }
    }

    @Test
    public void testMyObjectSelectByExampleBetween() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.id2, isBetween(1).and(3))
                    .orderBy(myObject.id1, myObject.id2));
            assertEquals(6, answer.size());
        }
    }

    @Test
    public void testMyObjectSelectByExampleTimeEquals() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            List<MyObject> results = mapper.select(dsl ->
                    dsl.where(myObject.timefield, isEqualTo(myTime)));
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
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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
            
            FirstName fn1 = new FirstName();
            fn1.setValue("B%");
            
            int rows = mapper.update(dsl ->
                MyObjectMapper.updateSelectiveColumns(newRecord, dsl)
                .where(myObject.firstname, isLike(fn1)));
            assertEquals(1, rows);

            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.firstname, isLike(fn1)));
            assertEquals(1, answer.size());
            
            MyObject returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
        }
    }

    @Test
    public void testMyObjectUpdateByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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
            
            int rows = mapper.update(dsl ->
                MyObjectMapper.updateAllColumns(newRecord, dsl)
                .where(myObject.id1, isEqualTo(3), and(myObject.id2, isEqualTo(4))));
            assertEquals(1, rows);

            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.id1, isEqualTo(3), and(myObject.id2, isEqualTo(4))));
            assertEquals(1, answer.size());
            
            MyObject returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertNull(returnedRecord.getFirstname());
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
        }
    }
    
    @Test
    public void testThatMultiRowInsertMethodsAreNotGenerated() {
        // regex rename has a generated key, but it is not JDBC. So it should be
        // ignored by the generator
        assertThrows(NoSuchMethodException.class, () -> {
            RegexrenameMapper.class.getMethod("insertMultiple", Collection.class);
        });
        
        assertThrows(NoSuchMethodException.class, () -> {
            RegexrenameMapper.class.getMethod("insertMultiple", MultiRowInsertStatementProvider.class);
        });
        
        assertThrows(NoSuchMethodException.class, () -> {
            RegexrenameMapper.class.getMethod("insertMultiple", String.class, List.class);
        });
    }
    
    @Test
    public void testThatRowBoundsMethodsAreNotGenerated() {
        // regex rename has the rowbounds plugin, but that plugin is disabled for MyBatisDynamicSQLV2
        assertThrows(NoSuchMethodException.class, () -> {
            RegexrenameMapper.class.getMethod("selectManyWithRowbounds", SelectStatementProvider.class, RowBounds.class);
        });
        
        assertThrows(NoSuchMethodException.class, () -> {
            RegexrenameMapper.class.getMethod("selectManyWithRowbounds", RowBounds.class);
        });
        
        assertThrows(NoSuchMethodException.class, () -> {
            RegexrenameMapper.class.getMethod("selectByExample", RowBounds.class);
        });
        
        assertThrows(NoSuchMethodException.class, () -> {
            RegexrenameMapper.class.getMethod("selectDistinctByExample", RowBounds.class);
        });
    }
    
    @Test
    public void testRegexRenameInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RegexrenameMapper mapper = sqlSession.getMapper(RegexrenameMapper.class);
            Regexrename record = new Regexrename();
            record.setAddress("123 Main Street");
            record.setName("Fred");
            record.setZipCode("99999");
            
            mapper.insert(record);
            // test generated id
            assertEquals(1, record.getId());
            
            Optional<Regexrename> returnedRecord = mapper.selectByPrimaryKey(1);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.getAddress(), rr.getAddress());
                assertEquals(1, rr.getId().intValue());
                assertEquals(record.getName(), rr.getName());
                assertEquals(record.getZipCode(), rr.getZipCode());
            });
        }
    }
    
    @Test
    public void testRegexRenameInsertSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RegexrenameMapper mapper = sqlSession.getMapper(RegexrenameMapper.class);
            Regexrename record = new Regexrename();
            record.setZipCode("99999");
            
            mapper.insertSelective(record);
            assertEquals(1, record.getId());
            
            Optional<Regexrename> returnedRecord = mapper.selectByPrimaryKey(1);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertNull(rr.getAddress());
                assertEquals(record.getId(), rr.getId());
                assertNull(rr.getName());
                assertEquals(record.getZipCode(), rr.getZipCode());
            });
        }
    }
    
    @Test
    public void testMyObjectSelectByExampleLikeInsensitive() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
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

            List<MyObject> answer = mapper.select(dsl ->
                    dsl.where(myObject.lastname, isLike("RU%"))
                    .orderBy(myObject.id1, myObject.id2));
            assertEquals(0, answer.size());
            
            answer = mapper.select(dsl ->
                    dsl.where(myObject.lastname, isLikeCaseInsensitive("RU%")));
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
        }
    }
    
    @Test
    public void testEnum() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EnumtestMapper mapper = sqlSession.getMapper(EnumtestMapper.class);
            
            Enumtest enumTest = new Enumtest();
            enumTest.setId(1);
            enumTest.setName(TestEnum.FRED);
            int rows = mapper.insert(enumTest);
            assertEquals(1, rows);
            
            List<Enumtest> returnedRecords = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, returnedRecords.size());
            
            Enumtest returnedRecord = returnedRecords.get(0);
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(TestEnum.FRED, returnedRecord.getName());
        }
    }
    
    @Test
    public void testEnumInsertMultiple() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            EnumtestMapper mapper = sqlSession.getMapper(EnumtestMapper.class);
            List<Enumtest> records = new ArrayList<>();
            
            Enumtest enumTest = new Enumtest();
            enumTest.setId(1);
            enumTest.setName(TestEnum.FRED);
            records.add(enumTest);
            
            enumTest = new Enumtest();
            enumTest.setId(2);
            enumTest.setName(TestEnum.BARNEY);
            records.add(enumTest);
            
            int rows = mapper.insertMultiple(records);
            assertEquals(2, rows);
            
            List<Enumtest> returnedRecords = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, returnedRecords.size());
            
            Enumtest returnedRecord = returnedRecords.get(0);
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(TestEnum.FRED, returnedRecord.getName());
        }
    }
}
