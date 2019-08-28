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
package mbg.test.mb3.dsql.v2;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static mbg.test.mb3.generated.dsql.v2.mapper.AwfulTableDynamicSqlSupport.awfulTable;
import static mbg.test.mb3.generated.dsql.v2.mapper.FieldsblobsDynamicSqlSupport.fieldsblobs;
import static mbg.test.mb3.generated.dsql.v2.mapper.FieldsonlyDynamicSqlSupport.fieldsonly;
import static mbg.test.mb3.generated.dsql.v2.mapper.PkblobsDynamicSqlSupport.pkblobs;
import static mbg.test.mb3.generated.dsql.v2.mapper.PkfieldsDynamicSqlSupport.pkfields;
import static mbg.test.mb3.generated.dsql.v2.mapper.PkfieldsblobsDynamicSqlSupport.pkfieldsblobs;
import static mbg.test.mb3.generated.dsql.v2.mapper.PkonlyDynamicSqlSupport.pkonly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isBetween;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isIn;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isLike;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotLike;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;

import mbg.test.mb3.generated.dsql.v2.mapper.AwfulTableMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.PkblobsMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.PkonlyMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.mbgtest.IdMapper;
import mbg.test.mb3.generated.dsql.v2.mapper.mbgtest.TranslationMapper;
import mbg.test.mb3.generated.dsql.v2.model.AwfulTable;
import mbg.test.mb3.generated.dsql.v2.model.Fieldsblobs;
import mbg.test.mb3.generated.dsql.v2.model.Fieldsonly;
import mbg.test.mb3.generated.dsql.v2.model.Pkblobs;
import mbg.test.mb3.generated.dsql.v2.model.Pkfields;
import mbg.test.mb3.generated.dsql.v2.model.Pkfieldsblobs;
import mbg.test.mb3.generated.dsql.v2.model.Pkonly;
import mbg.test.mb3.generated.dsql.v2.model.mbgtest.Id;
import mbg.test.mb3.generated.dsql.v2.model.mbgtest.Translation;

/**
 * @author Jeff Butler
 * 
 */
public class DynamicSqlTest extends AbstractTest {

    @Test
    public void testFieldsOnlyInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(fieldsonly.integerfield, isEqualTo(5)));
            assertThat(answer.size()).isEqualTo(1);

            Fieldsonly returnedRecord = answer.get(0);
            assertThat(returnedRecord.getIntegerfield()).isEqualTo(record.getIntegerfield());
            assertThat(returnedRecord.getDoublefield()).isEqualTo(record.getDoublefield());
            assertThat(returnedRecord.getFloatfield()).isEqualTo(record.getFloatfield());
        }
    }

    @Test
    public void testFieldsOnlyselect() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(44.55);
            record.setFloatfield(66.77);
            record.setIntegerfield(8);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(88.99);
            record.setFloatfield(100.111);
            record.setIntegerfield(9);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(fieldsonly.integerfield, isGreaterThan(5)));
            assertThat(answer.size()).isEqualTo(2);
            
            answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(3);
        }
    }

    @Test
    public void testFieldsOnlySelectByExamplewithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            
            Fieldsonly record1 = new Fieldsonly();
            record1.setDoublefield(11.22);
            record1.setFloatfield(33.44);
            record1.setIntegerfield(5);

            Fieldsonly record2 = new Fieldsonly();
            record2.setDoublefield(44.55);
            record2.setFloatfield(66.77);
            record2.setIntegerfield(8);

            Fieldsonly record3 = new Fieldsonly();
            record3.setDoublefield(88.99);
            record3.setFloatfield(100.111);
            record3.setIntegerfield(9);

            mapper.insertMultiple(Arrays.asList(record1, record2, record3));
            
            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(fieldsonly.integerfield, isGreaterThan(5)));
            assertThat(answer.size()).isEqualTo(2);

            answer = mapper.select(SelectDSLCompleter.allRowsOrderedBy(fieldsonly.integerfield));
            assertThat(answer.size()).isEqualTo(3);
            assertThat(answer.get(0).getIntegerfield()).isEqualTo(5);
            assertThat(answer.get(1).getIntegerfield()).isEqualTo(8);
            assertThat(answer.get(2).getIntegerfield()).isEqualTo(9);
        }
    }

    @Test
    public void testFieldsOnlySelectByExampleDistinct() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            mapper.insert(record);
            mapper.insert(record);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(44.55);
            record.setFloatfield(66.77);
            record.setIntegerfield(8);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(88.99);
            record.setFloatfield(100.111);
            record.setIntegerfield(9);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.selectDistinct(dsl ->
                    dsl.where(fieldsonly.integerfield, isEqualTo(5)));
            assertThat(answer.size()).isEqualTo(1);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(5);
        }
    }

    @Test
    public void testFieldsOnlySelectByExampleNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(44.55);
            record.setFloatfield(66.77);
            record.setIntegerfield(8);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(88.99);
            record.setFloatfield(100.111);
            record.setIntegerfield(9);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(3);
        }
    }

    @Test
    public void testFieldsOnlydelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(44.55);
            record.setFloatfield(66.77);
            record.setIntegerfield(8);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(88.99);
            record.setFloatfield(100.111);
            record.setIntegerfield(9);
            mapper.insert(record);

            int rows = mapper.delete(dsl ->
                    dsl.where(fieldsonly.integerfield, isGreaterThan(5)));
            assertThat(rows).isEqualTo(2);

            List<Fieldsonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(1);
        }
    }

    @Test
    public void testFieldsOnlycount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(11.22);
            record.setFloatfield(33.44);
            record.setIntegerfield(5);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(44.55);
            record.setFloatfield(66.77);
            record.setIntegerfield(8);
            mapper.insert(record);

            record = new Fieldsonly();
            record.setDoublefield(88.99);
            record.setFloatfield(100.111);
            record.setIntegerfield(9);
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(fieldsonly.integerfield, isGreaterThan(5)));
            assertThat(rows).isEqualTo(2);

            rows = mapper.count(CountDSLCompleter.allRows());
            assertThat(rows).isEqualTo(3);
        }
    }

    @Test
    public void testPKOnlyInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            List<Pkonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(1);

            Pkonly returnedRecord = answer.get(0);
            assertThat(returnedRecord.getId()).isEqualTo(key.getId());
            assertThat(returnedRecord.getSeqNum()).isEqualTo(key.getSeqNum());
        }
    }

    @Test
    public void testPKOnlyDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            int rows = mapper.insert(key);

            key = new Pkonly(5, 6);
            rows = mapper.insert(key);

            List<Pkonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(2);

            rows = mapper.deleteByPrimaryKey(5, 6);
            assertThat(rows).isEqualTo(1);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(1);
        }
    }

    @Test
    public void testPKOnlydelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            int rows = mapper.delete(dsl ->
                    dsl.where(pkonly.id, isGreaterThan(4)));
            assertThat(rows).isEqualTo(2);

            List<Pkonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(1);
        }
    }

    @Test
    public void testPKOnlyselect() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            List<Pkonly> answer = mapper.select(dsl ->
                    dsl.where(pkonly.id, isGreaterThan(4))
                    .orderBy(pkonly.id));
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).getId().intValue()).isEqualTo(5);
            assertThat(answer.get(0).getSeqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).getId().intValue()).isEqualTo(7);
            assertThat(answer.get(1).getSeqNum().intValue()).isEqualTo(8);
        }
    }

    @Test
    public void testPKOnlySelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);

            mapper.insertMultiple(Arrays.asList(new Pkonly(1,3), new Pkonly(5, 6), new Pkonly(7, 8)));
            
            List<Pkonly> answer = mapper.select(dsl ->
                    dsl.where(pkonly.id, isGreaterThan(4))
                    .orderBy(pkonly.id));
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).getId().intValue()).isEqualTo(5);
            assertThat(answer.get(0).getSeqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).getId().intValue()).isEqualTo(7);
            assertThat(answer.get(1).getSeqNum().intValue()).isEqualTo(8);
        }
    }

    @Test
    public void testPKOnlySelectByExampleBackwards() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            List<Pkonly> answer = mapper.select(c ->
                c.where(pkonly.id, isGreaterThan(4))
                .orderBy(pkonly.id)
            );
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).getId().intValue()).isEqualTo(5);
            assertThat(answer.get(0).getSeqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).getId().intValue()).isEqualTo(7);
            assertThat(answer.get(1).getSeqNum().intValue()).isEqualTo(8);
        }
    }

    @Test
    public void testPKOnlySelectByExampleWithBackwardsResults() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            List<Pkonly> answer = mapper.select(c ->
                c.where(pkonly.id, isGreaterThan(4))
                .orderBy(pkonly.id)
            );
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).getId().intValue()).isEqualTo(5);
            assertThat(answer.get(0).getSeqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).getId().intValue()).isEqualTo(7);
            assertThat(answer.get(1).getSeqNum().intValue()).isEqualTo(8);
        }
    }

    @Test
    public void testPKOnlySelectByExampleNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            List<Pkonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(3, answer.size());
        }
    }

    @Test
    public void testPKOnlycount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            long rows = mapper.count(dsl ->
                    dsl.where(pkonly.id, isGreaterThan(4)));
            assertEquals(2, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(3, rows);
        }
    }

    @Test
    public void testPKFieldsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setDatefield(LocalDate.now());
            record.setDecimal100field(10L);
            record.setDecimal155field(new BigDecimal("15.12345"));
            record.setDecimal30field((short) 3);
            record.setDecimal60field(6);
            record.setFirstname("Jeff");
            record.setId1(1);
            record.setId2(2);
            record.setLastname("Butler");
            record.setTimefield(LocalTime.of(13, 2, 4));
            record.setTimestampfield(LocalDateTime.now());
            record.setStringboolean(true);

            mapper.insert(record);

            Optional<Pkfields> returnedRecord = mapper.selectByPrimaryKey(2, 1);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.getDatefield(), rr.getDatefield());
                assertEquals(record.getDecimal100field(), rr.getDecimal100field());
                assertEquals(record.getDecimal155field(), rr.getDecimal155field());
                assertEquals(record.getDecimal30field(), rr.getDecimal30field());
                assertEquals(record.getDecimal60field(), rr.getDecimal60field());
                assertEquals(record.getFirstname(), rr.getFirstname());
                assertEquals(record.getId1(), rr.getId1());
                assertEquals(record.getId2(), rr.getId2());
                assertEquals(record.getLastname(), rr.getLastname());
                assertEquals(record.getTimefield(), rr.getTimefield());
                assertEquals(record.getTimestampfield(), rr.getTimestampfield());
                assertEquals(record.isStringboolean(), rr.isStringboolean());
            });
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);

            mapper.insert(record);

            record.setFirstname("Scott");
            record.setLastname("Jones");

            int rows = mapper.updateByPrimaryKey(record);
            assertEquals(1, rows);

            Optional<Pkfields> record2 = mapper.selectByPrimaryKey(2, 1);

            assertThat(record2).hasValueSatisfying(r2 -> {
                assertEquals(record.getFirstname(), r2.getFirstname());
                assertEquals(record.getLastname(), r2.getLastname());
                assertEquals(record.getId1(), r2.getId1());
                assertEquals(record.getId2(), r2.getId2());
            });
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setDecimal60field(5);
            record.setId1(1);
            record.setId2(2);

            mapper.insert(record);

            Pkfields newRecord = new Pkfields();
            newRecord.setId1(1);
            newRecord.setId2(2);
            newRecord.setFirstname("Scott");
            newRecord.setDecimal60field(4);

            int rows = mapper.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);

            Optional<Pkfields> returnedRecord = mapper.selectByPrimaryKey(2, 1);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.getDatefield(), rr.getDatefield());
                assertEquals(record.getDecimal100field(), rr.getDecimal100field());
                assertEquals(record.getDecimal155field(), rr.getDecimal155field());
                assertEquals(record.getDecimal30field(), rr.getDecimal30field());
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
    public void testPKfieldsDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);

            mapper.insert(record);

            int rows = mapper.deleteByPrimaryKey(2, 1);
            assertEquals(1, rows);

            List<Pkfields> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(0, answer.size());
        }
    }

    @Test
    public void testPKFieldsdelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bob");
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);

            mapper.insert(record);

            List<Pkfields> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(pkfields.lastname, isLike("J%")));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            Pkfields record1 = new Pkfields();
            record1.setFirstname("Bob");
            record1.setLastname("Jones");
            record1.setId1(3);
            record1.setId2(4);
            mapper.insert(record1);

            Optional<Pkfields> newRecord = mapper.selectByPrimaryKey(4, 3);

            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.getFirstname(), nr.getFirstname());
                assertEquals(record1.getLastname(), nr.getLastname());
                assertEquals(record1.getId1(), nr.getId1());
                assertEquals(record1.getId2(), nr.getId2());
            });
        }
    }

    @Test
    public void testPKFieldsSelectByExampleLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(pkfields.firstname, isLike("B%"))
                    .orderBy(pkfields.id1, pkfields.id2));
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
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNotLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(pkfields.firstname, isNotLike("B%"))
                    .orderBy(pkfields.id1, pkfields.id2));
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
        }
    }

    @Test
    public void testPKFieldsSelectByExampleComplexLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(pkfields.firstname, isLike("B%"), and(pkfields.id2, isEqualTo(3)))
                    .or(pkfields.firstname, isLike("Wi%"))
                    .orderBy(pkfields.id1, pkfields.id2));
                    
            assertEquals(2, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.getId1().intValue());
            assertEquals(3, returnedRecord.getId2().intValue());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleIn() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(pkfields.id2, isIn(1, 3))
                    .orderBy(pkfields.id1, pkfields.id2));
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
        }
    }

    @Test
    public void testPKFieldsSelectByExampleBetween() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(pkfields.id2, isBetween(1).and(3))
                    .orderBy(pkfields.id1, pkfields.id2));
            assertEquals(6, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(
                    SelectDSLCompleter.allRowsOrderedBy(pkfields.id1, pkfields.id2));

            assertEquals(6, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNoCriteriaWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            
            Collection<Pkfields> records = new ArrayList<>();
            
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            records.add(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            records.add(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            records.add(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            records.add(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            records.add(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            records.add(record);

            int rowsInserted = mapper.insertMultiple(records);
            
            assertEquals(6, rowsInserted);
            
            List<Pkfields> answer = mapper.select(
                    SelectDSLCompleter.allRowsOrderedBy(pkfields.id1, pkfields.id2));

            assertEquals(6, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleEscapedFields() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Fred");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(1);
            record.setWierdField(11);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Wilma");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(2);
            record.setWierdField(22);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Pebbles");
            record.setLastname("Flintstone");
            record.setId1(1);
            record.setId2(3);
            record.setWierdField(33);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Barney");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(1);
            record.setWierdField(44);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Betty");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(2);
            record.setWierdField(55);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bamm Bamm");
            record.setLastname("Rubble");
            record.setId1(2);
            record.setId2(3);
            record.setWierdField(66);
            mapper.insert(record);

            List<Integer> values = new ArrayList<>();
            values.add(11);
            values.add(22);

            List<Pkfields> answer = mapper.select(DSL ->
                    DSL.where(pkfields.wierdField, isLessThan(40))
                    .and(pkfields.wierdField, isIn(11, 22))
                    .orderBy(pkfields.id1, pkfields.id2));
                            
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKFieldscount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setId1(1);
            record.setId2(2);
            mapper.insert(record);

            record = new Pkfields();
            record.setFirstname("Bob");
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(pkfields.lastname, isLike("J%")));
            
            assertEquals(1, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testPKBlobsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Pkblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());

            Pkblobs returnedRecord = answer.get(0);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeyWithBLOBs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            Pkblobs record1 = new Pkblobs();
            record1.setId(3);
            record1.setBlob1(generateRandomBlob());
            record1.setBlob2(generateRandomBlob());
            int rows = mapper.updateByPrimaryKey(record1);
            assertEquals(1, rows);

            Optional<Pkblobs> newRecord = mapper.selectByPrimaryKey(3);
            
            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.getId(), nr.getId());
                assertTrue(blobsAreEqual(record1.getBlob1(), nr.getBlob1()));
                assertTrue(blobsAreEqual(record1.getBlob2(), nr.getBlob2()));
            });
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            Pkblobs newRecord = new Pkblobs();
            newRecord.setId(3);
            newRecord.setBlob2(generateRandomBlob());
            mapper.updateByPrimaryKeySelective(newRecord);

            Optional<Pkblobs> returnedRecord = mapper.selectByPrimaryKey(3);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.getId(), rr.getId());
                assertTrue(blobsAreEqual(record.getBlob1(), rr.getBlob1()));
                assertTrue(blobsAreEqual(newRecord.getBlob2(), rr.getBlob2()));
            });
        }
    }

    @Test
    public void testPKBlobsDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Pkblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());

            int rows = mapper.deleteByPrimaryKey(3);
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(0, answer.size());
        }
    }

    @Test
    public void testPKBlobsdelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            record = new Pkblobs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Pkblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(pkblobs.id, isLessThan(4)));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKBlobsSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            Pkblobs record1 = new Pkblobs();
            record1.setId(6);
            record1.setBlob1(generateRandomBlob());
            record1.setBlob2(generateRandomBlob());
            mapper.insert(record1);

            Optional<Pkblobs> newRecord = mapper.selectByPrimaryKey(6);
            
            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.getId(), nr.getId());
                assertTrue(blobsAreEqual(record1.getBlob1(), nr.getBlob1()));
                assertTrue(blobsAreEqual(record1.getBlob2(), nr.getBlob2()));
            });
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithBlobs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            record = new Pkblobs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Pkblobs> answer = mapper.select(DSL ->
                    DSL.where(pkblobs.id, isGreaterThan(4)));

            assertEquals(1, answer.size());

            Pkblobs newRecord = answer.get(0);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Collection<Pkblobs> records = new ArrayList<>();
            
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            records.add(record);

            record = new Pkblobs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            records.add(record);

            int recordsInserted = mapper.insertMultiple(records);
            assertEquals(2, recordsInserted);
            
            List<Pkblobs> answer = mapper.select(dsl ->
                    dsl.where(pkblobs.id, isGreaterThan(4)));

            assertEquals(1, answer.size());

            Pkblobs newRecord = answer.get(0);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        }
    }

    @Test
    public void testPKBlobscount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs();
            record.setId(3);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            record = new Pkblobs();
            record.setId(6);
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(pkblobs.id, isLessThan(4)));
            assertEquals(1, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testPKFieldsBlobsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());

            Pkfieldsblobs returnedRecord = answer.get(0);
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeyWithBLOBs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs();
            updateRecord.setId1(3);
            updateRecord.setId2(4);
            updateRecord.setFirstname("Scott");
            updateRecord.setLastname("Jones");
            updateRecord.setBlob1(generateRandomBlob());

            int rows = mapper.updateByPrimaryKey(updateRecord);
            assertEquals(1, rows);

            Optional<Pkfieldsblobs> newRecord = mapper.selectByPrimaryKey(3, 4);
            
            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(updateRecord.getFirstname(), nr.getFirstname());
                assertEquals(updateRecord.getLastname(), nr.getLastname());
                assertEquals(record.getId1(), nr.getId1());
                assertEquals(record.getId2(), nr.getId2());
                assertTrue(blobsAreEqual(updateRecord.getBlob1(), nr.getBlob1()));
            });
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs();
            updateRecord.setId1(3);
            updateRecord.setId2(4);
            updateRecord.setLastname("Jones");

            int rows = mapper.updateByPrimaryKeySelective(updateRecord);
            assertEquals(1, rows);

            Optional<Pkfieldsblobs> returnedRecord = mapper.selectByPrimaryKey(3, 4);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.getFirstname(), rr.getFirstname());
                assertEquals(updateRecord.getLastname(), rr.getLastname());
                assertEquals(record.getId1(), rr.getId1());
                assertEquals(record.getId2(), rr.getId2());
                assertTrue(blobsAreEqual(record.getBlob1(), rr.getBlob1()));
            });
        }
    }

    @Test
    public void testPKFieldsBlobsDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.deleteByPrimaryKey(5, 6);
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobsdelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(pkfieldsblobs.id1, isNotEqualTo(3)));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs record1 = new Pkfieldsblobs();
            record1.setId1(5);
            record1.setId2(6);
            record1.setFirstname("Scott");
            record1.setLastname("Jones");
            record1.setBlob1(generateRandomBlob());
            mapper.insert(record1);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            Optional<Pkfieldsblobs> newRecord = mapper.selectByPrimaryKey(5, 6);
            
            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.getId1(), nr.getId1());
                assertEquals(record1.getId2(), nr.getId2());
                assertEquals(record1.getFirstname(), nr.getFirstname());
                assertEquals(record1.getLastname(), nr.getLastname());
                assertTrue(blobsAreEqual(record1.getBlob1(), nr.getBlob1()));
            });
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(pkfieldsblobs.id2, isEqualTo(6)));
            assertEquals(1, answer.size());

            Pkfieldsblobs newRecord = answer.get(0);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Collection<Pkfieldsblobs> records = new ArrayList<>();
            
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            records.add(record);

            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            records.add(record);

            int rowsInserted = mapper.insertMultiple(records);
            assertEquals(2, rowsInserted);
            
            List<Pkfieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(pkfieldsblobs.id2, isEqualTo(6)));
            assertEquals(1, answer.size());

            Pkfieldsblobs newRecord = answer.get(0);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testFieldsBlobsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());

            Fieldsblobs returnedRecord = answer.get(0);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        }
    }

    @Test
    public void testFieldsBlobsdelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            record = new Fieldsblobs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(fieldsblobs.firstname, isLike("S%")));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            record = new Fieldsblobs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(fieldsblobs.firstname, isLike("S%")));
            assertEquals(1, answer.size());

            Fieldsblobs newRecord = answer.get(0);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Collection<Fieldsblobs> records = new ArrayList<>();
            
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            records.add(record);

            record = new Fieldsblobs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            records.add(record);

            int rowsInserted = mapper.insertMultiple(records);
            assertEquals(2, rowsInserted);
            
            List<Fieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(fieldsblobs.firstname, isLike("S%")));
            assertEquals(1, answer.size());

            Fieldsblobs newRecord = answer.get(0);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            record = new Fieldsblobs();
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            record.setBlob2(generateRandomBlob());
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobscount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(3);
            record.setId2(4);
            record.setFirstname("Jeff");
            record.setLastname("Smith");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs();
            record.setId1(5);
            record.setId2(6);
            record.setFirstname("Scott");
            record.setLastname("Jones");
            record.setBlob1(generateRandomBlob());
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(pkfieldsblobs.id1, isNotEqualTo(3)));
            assertEquals(1, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testAwfulTableInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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
            
            record.setActive(true);
            record.setActive1(Boolean.FALSE);
            record.setActive2(new byte[]{-128, 127});

            mapper.insert(record);
            Integer generatedCustomerId = record.getCustomerId();
            assertEquals(57, generatedCustomerId.intValue());

            Optional<AwfulTable> returnedRecord = mapper
                    .selectByPrimaryKey(generatedCustomerId);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(generatedCustomerId, rr.getCustomerId());
                assertEquals(record.geteMail(), rr.geteMail());
                assertEquals(record.getEmailaddress(), rr.getEmailaddress());
                assertEquals(record.getFirstFirstName(), rr.getFirstFirstName());
                assertEquals(record.getFrom(), rr.getFrom());
                assertEquals(record.getId1(), rr.getId1());
                assertEquals(record.getId2(), rr.getId2());
                assertEquals(record.getId5(), rr.getId5());
                assertEquals(record.getId6(), rr.getId6());
                assertEquals(record.getId7(), rr.getId7());
                assertEquals(record.getSecondFirstName(), rr.getSecondFirstName());
                assertEquals(record.getThirdFirstName(), rr.getThirdFirstName());
                assertTrue(rr.isActive());
                assertFalse(rr.getActive1().booleanValue());
                assertEquals(3, rr.getActive2().length);
                assertEquals(-128, rr.getActive2()[0]);
                assertEquals(127, rr.getActive2()[1]);
                assertEquals(0, rr.getActive2()[2]);
            });
        }
    }

    @Test
    public void testAwfulTableInsertSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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

            mapper.insertSelective(record);
            Integer generatedCustomerId = record.getCustomerId();
            assertEquals(57, generatedCustomerId.intValue());

            Optional<AwfulTable> returnedRecord = mapper
                    .selectByPrimaryKey(generatedCustomerId);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(generatedCustomerId, rr.getCustomerId());
                assertEquals(record.geteMail(), rr.geteMail());
                assertEquals(record.getEmailaddress(), rr.getEmailaddress());
                assertEquals("Mabel", rr.getFirstFirstName());
                assertEquals(record.getFrom(), rr.getFrom());
                assertEquals(record.getId1(), rr.getId1());
                assertEquals(record.getId2(), rr.getId2());
                assertEquals(record.getId5(), rr.getId5());
                assertEquals(record.getId6(), rr.getId6());
                assertEquals(record.getId7(), rr.getId7());
                assertEquals(record.getSecondFirstName(), rr.getSecondFirstName());
                assertEquals(record.getThirdFirstName(), rr.getThirdFirstName());
            });
        }
    }
    
    @Test
    public void testAwfulTableUpdateByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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

            mapper.insert(record);
            Integer generatedCustomerId = record.getCustomerId();

            record.setId1(11);
            record.setId2(22);

            int rows = mapper.updateByPrimaryKey(record);
            assertEquals(1, rows);

            Optional<AwfulTable> returnedRecord = mapper.selectByPrimaryKey(generatedCustomerId);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(generatedCustomerId, rr.getCustomerId());
                assertEquals(record.geteMail(), rr.geteMail());
                assertEquals(record.getEmailaddress(), rr.getEmailaddress());
                assertEquals(record.getFirstFirstName(), rr.getFirstFirstName());
                assertEquals(record.getFrom(), rr.getFrom());
                assertEquals(record.getId1(), rr.getId1());
                assertEquals(record.getId2(), rr.getId2());
                assertEquals(record.getId5(), rr.getId5());
                assertEquals(record.getId6(), rr.getId6());
                assertEquals(record.getId7(), rr.getId7());
                assertEquals(record.getSecondFirstName(), rr.getSecondFirstName());
                assertEquals(record.getThirdFirstName(), rr.getThirdFirstName());
            });
        }
    }

    @Test
    public void testAwfulTableUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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

            mapper.insert(record);
            Integer generatedCustomerId = record.getCustomerId();

            AwfulTable newRecord = new AwfulTable();
            newRecord.setCustomerId(generatedCustomerId);
            newRecord.setId1(11);
            newRecord.setId2(22);

            int rows = mapper.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);

            Optional<AwfulTable> returnedRecord = mapper.selectByPrimaryKey(generatedCustomerId);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(generatedCustomerId, rr.getCustomerId());
                assertEquals(record.geteMail(), rr.geteMail());
                assertEquals(record.getEmailaddress(), rr.getEmailaddress());
                assertEquals(record.getFirstFirstName(), rr.getFirstFirstName());
                assertEquals(record.getFrom(), rr.getFrom());
                assertEquals(newRecord.getId1(), rr.getId1());
                assertEquals(newRecord.getId2(), rr.getId2());
                assertEquals(record.getId5(), rr.getId5());
                assertEquals(record.getId6(), rr.getId6());
                assertEquals(record.getId7(), rr.getId7());
                assertEquals(record.getSecondFirstName(), rr.getSecondFirstName());
                assertEquals(record.getThirdFirstName(), rr.getThirdFirstName());
            });
        }
    }

    @Test
    public void testAwfulTableDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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

            mapper.insert(record);
            Integer generatedCustomerId = record.getCustomerId();

            int rows = mapper.deleteByPrimaryKey(generatedCustomerId);
            assertEquals(1, rows);

            List<AwfulTable> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(0, answer.size());
        }
    }

    @Test
    public void testAwfulTabledelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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

            mapper.insert(record);

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

            mapper.insert(record);

            List<AwfulTable> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(awfulTable.eMail, isLike("fred@%")));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testAwfulTableSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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

            mapper.insert(record);

            AwfulTable record1 = new AwfulTable();
            record1.seteMail("fred2@fred.com");
            record1.setEmailaddress("alsofred2@fred.com");
            record1.setFirstFirstName("fred11");
            record1.setFrom("from from field");
            record1.setId1(11);
            record1.setId2(22);
            record1.setId5(55);
            record1.setId6(66);
            record1.setId7(77);
            record1.setSecondFirstName("fred22");
            record1.setThirdFirstName("fred33");

            mapper.insert(record1);
            Integer generatedKey = record1.getCustomerId();

            Optional<AwfulTable> returnedRecord = mapper.selectByPrimaryKey(generatedKey);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record1.getCustomerId(), rr.getCustomerId());
                assertEquals(record1.geteMail(), rr.geteMail());
                assertEquals(record1.getEmailaddress(), rr.getEmailaddress());
                assertEquals(record1.getFirstFirstName(), rr.getFirstFirstName());
                assertEquals(record1.getFrom(), rr.getFrom());
                assertEquals(record1.getId1(), rr.getId1());
                assertEquals(record1.getId2(), rr.getId2());
                assertEquals(record1.getId5(), rr.getId5());
                assertEquals(record1.getId6(), rr.getId6());
                assertEquals(record1.getId7(), rr.getId7());
                assertEquals(record1.getSecondFirstName(), rr.getSecondFirstName());
                assertEquals(record1.getThirdFirstName(), rr.getThirdFirstName());
            });
        }
    }

    @Test
    public void testAwfulTableSelectByExampleLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(awfulTable.firstFirstName, isLike("b%"))
                    .orderBy(awfulTable.customerId));
            assertEquals(3, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(1111, returnedRecord.getId1().intValue());
            assertEquals(2222, returnedRecord.getId2().intValue());
            assertEquals(60, returnedRecord.getCustomerId());

            returnedRecord = answer.get(1);
            assertEquals(11111, returnedRecord.getId1().intValue());
            assertEquals(22222, returnedRecord.getId2().intValue());
            assertEquals(61, returnedRecord.getCustomerId());
            
            returnedRecord = answer.get(2);
            assertEquals(111111, returnedRecord.getId1().intValue());
            assertEquals(222222, returnedRecord.getId2().intValue());
            assertEquals(62, returnedRecord.getCustomerId());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleLikeWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
            List<AwfulTable> records = new ArrayList<>();
            
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
            records.add(record);

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
            records.add(record);

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
            records.add(record);

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
            records.add(record);

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
            records.add(record);

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
            records.add(record);

            int rowsInserted = mapper.insertMultiple(records);
            assertEquals(6, rowsInserted);
            
            // check generated keys
            assertEquals(57, records.get(0).getCustomerId());
            assertEquals(58, records.get(1).getCustomerId());
            assertEquals(59, records.get(2).getCustomerId());
            assertEquals(60, records.get(3).getCustomerId());
            assertEquals(61, records.get(4).getCustomerId());
            assertEquals(62, records.get(5).getCustomerId());
            
            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(awfulTable.firstFirstName, isLike("b%"))
                    .orderBy(awfulTable.customerId));
            assertEquals(3, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(1111, returnedRecord.getId1().intValue());
            assertEquals(2222, returnedRecord.getId2().intValue());
            assertEquals(60, returnedRecord.getCustomerId());

            returnedRecord = answer.get(1);
            assertEquals(11111, returnedRecord.getId1().intValue());
            assertEquals(22222, returnedRecord.getId2().intValue());
            assertEquals(61, returnedRecord.getCustomerId());
            
            returnedRecord = answer.get(2);
            assertEquals(111111, returnedRecord.getId1().intValue());
            assertEquals(222222, returnedRecord.getId2().intValue());
            assertEquals(62, returnedRecord.getCustomerId());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleNotLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(awfulTable.firstFirstName, isNotLike("b%"))
                    .orderBy(awfulTable.customerId));
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
        }
    }
    
    @Test
    public void testAwfulTableSelectByExampleComplexLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(awfulTable.firstFirstName, isLike("b%"), and(awfulTable.id2, isEqualTo(222222)))
                    .or(awfulTable.firstFirstName, isLike("wi%"))
                    .orderBy(awfulTable.customerId));
                            
            assertEquals(2, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(111111, returnedRecord.getId1().intValue());
            assertEquals(222222, returnedRecord.getId2().intValue());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleIn() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(awfulTable.id1, isIn(1, 11))
                    .orderBy(awfulTable.customerId));
            
            assertEquals(2, answer.size());
            AwfulTable returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.getId1().intValue());
            assertEquals(2, returnedRecord.getId2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(11, returnedRecord.getId1().intValue());
            assertEquals(22, returnedRecord.getId2().intValue());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleBetween() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(awfulTable.id1, isBetween(1).and(1000)));
            assertEquals(3, answer.size());
        }
    }

    @Test
    public void testAwfulTableSelectByExampleNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

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
            mapper.insert(record);

            List<AwfulTable> answer = mapper.select(
                    SelectDSLCompleter.allRowsOrderedBy(awfulTable.customerId.descending()));
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
        }
    }

    @Test
    public void testAwfulTablecount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
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

            mapper.insert(record);

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

            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(awfulTable.eMail, isLike("fred@%")));
            assertEquals(1, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(2, rows);
        }
    }
    
    @Test
    public void testTranslationTable() {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TranslationMapper mapper = sqlSession.getMapper(TranslationMapper.class);
            
            Translation t = new Translation();
            t.setId(1);
            t.setTranslation("Spanish");
            mapper.insert(t);
            
            Translation t1 = new Translation();
            t1.setId(2);
            t1.setTranslation("French");
            mapper.insert(t1);
            
            Optional<Translation> returnedRecord = mapper.selectByPrimaryKey(2);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(t1.getId(), rr.getId());
                assertEquals(t1.getTranslation(), rr.getTranslation());
            });
            
            t1.setTranslation("Italian");
            mapper.updateByPrimaryKey(t1);
            
            returnedRecord = mapper.selectByPrimaryKey(2);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(t1.getId(), rr.getId());
                assertEquals(t1.getTranslation(), rr.getTranslation());
            });
        }
    }
    
    @Test
    public void testIdTable() {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            IdMapper mapper = sqlSession.getMapper(IdMapper.class);
            
            Id id = new Id();
            id.setId(1);
            id.setDescription("Spanish");
            mapper.insert(id);
            
            Id id1 = new Id();
            id1.setId(2);
            id1.setDescription("French");
            mapper.insert(id1);
            
            Optional<Id> returnedRecord = mapper.selectByPrimaryKey(2);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(id1.getId(), rr.getId());
                assertEquals(id1.getDescription(), rr.getDescription());
            });
            
            id1.setDescription("Italian");
            mapper.updateByPrimaryKey(id1);
            
            returnedRecord = mapper.selectByPrimaryKey(2);
            
            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(id1.getId(), rr.getId());
                assertEquals(id1.getDescription(), rr.getDescription());
            });
        }
    }
    
    @Test
    public void testEquals1() {
        Pkfields pkfields1 = new Pkfields();
        assertFalse(pkfields1.equals(null));
    }
    
    @Test
    public void testEquals2() {
        Pkfields pkfields1 = new Pkfields();
        Pkfields pkfields2 = new Pkfields();
        assertTrue(pkfields1.equals(pkfields2));
    }
    
    @Test
    public void testEquals3() {
        Pkfields pkfields1 = new Pkfields();
        pkfields1.setId1(2);
        
        Pkfields pkfields2 = new Pkfields();
        pkfields2.setId1(2);
        
        assertTrue(pkfields1.equals(pkfields2));
    }
    
    @Test
    public void testEquals4() {
        Pkfields pkfields1 = new Pkfields();
        pkfields1.setId1(2);
        
        Pkfields pkfields2 = new Pkfields();
        pkfields2.setId1(3);
        
        assertFalse(pkfields1.equals(pkfields2));
    }

    @Test
    public void testEquals5() {
        AwfulTable awfulTable1 = new AwfulTable();
        awfulTable1.setActive(false);
        awfulTable1.setCustomerId(3);
        awfulTable1.seteMail("fred@fred.com");
        awfulTable1.setEmailaddress("fred@fred.com");
        awfulTable1.setFirstFirstName("Fred");
        awfulTable1.setFrom("from");
        awfulTable1.setId1(22);
        awfulTable1.setId2(33);
        awfulTable1.setId5(55);
        awfulTable1.setId6(66);
        awfulTable1.setId7(77);
        awfulTable1.setLastName("Rubble");
        awfulTable1.setSecondFirstName("Bamm Bamm");
        awfulTable1.setThirdFirstName("Pebbles");

        AwfulTable awfulTable2 = new AwfulTable();
        awfulTable2.setActive(false);
        awfulTable2.setCustomerId(3);
        awfulTable2.seteMail("fred@fred.com");
        awfulTable2.setEmailaddress("fred@fred.com");
        awfulTable2.setFirstFirstName("Fred");
        awfulTable2.setFrom("from");
        awfulTable2.setId1(22);
        awfulTable2.setId2(33);
        awfulTable2.setId5(55);
        awfulTable2.setId6(66);
        awfulTable2.setId7(77);
        awfulTable2.setLastName("Rubble");
        awfulTable2.setSecondFirstName("Bamm Bamm");
        awfulTable2.setThirdFirstName("Pebbles");
        
        assertTrue(awfulTable1.equals(awfulTable2));
        
        awfulTable2.setActive(true);
        assertFalse(awfulTable1.equals(awfulTable2));
    }

    @Test
    public void testHashCode1() {
        Pkfields pkfields1 = new Pkfields();
        Pkfields pkfields2 = new Pkfields();
        assertTrue(pkfields1.hashCode() == pkfields2.hashCode());
    }
    
    @Test
    public void testHashCode2() {
        Pkfields pkfields1 = new Pkfields();
        pkfields1.setId1(2);
        
        Pkfields pkfields2 = new Pkfields();
        pkfields2.setId1(2);
        
        assertTrue(pkfields1.hashCode() == pkfields2.hashCode());
    }

    @Test
    public void testHashCode3() {
        AwfulTable awfulTable1 = new AwfulTable();
        awfulTable1.setActive(false);
        awfulTable1.setCustomerId(3);
        awfulTable1.seteMail("fred@fred.com");
        awfulTable1.setEmailaddress("fred@fred.com");
        awfulTable1.setFirstFirstName("Fred");
        awfulTable1.setFrom("from");
        awfulTable1.setId1(22);
        awfulTable1.setId2(33);
        awfulTable1.setId5(55);
        awfulTable1.setId6(66);
        awfulTable1.setId7(77);
        awfulTable1.setLastName("Rubble");
        awfulTable1.setSecondFirstName("Bamm Bamm");
        awfulTable1.setThirdFirstName("Pebbles");

        AwfulTable awfulTable2 = new AwfulTable();
        awfulTable2.setActive(false);
        awfulTable2.setCustomerId(3);
        awfulTable2.seteMail("fred@fred.com");
        awfulTable2.setEmailaddress("fred@fred.com");
        awfulTable2.setFirstFirstName("Fred");
        awfulTable2.setFrom("from");
        awfulTable2.setId1(22);
        awfulTable2.setId2(33);
        awfulTable2.setId5(55);
        awfulTable2.setId6(66);
        awfulTable2.setId7(77);
        awfulTable2.setLastName("Rubble");
        awfulTable2.setSecondFirstName("Bamm Bamm");
        awfulTable2.setThirdFirstName("Pebbles");
        
        assertTrue(awfulTable1.hashCode() == awfulTable2.hashCode());
    }
}
