/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mbg.test.mb3.dsql.record;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static mbg.test.mb3.generated.dsql.record.mapper.AwfulTableDynamicSqlSupport.AWFUL_TABLE;
import static mbg.test.mb3.generated.dsql.record.mapper.FieldsblobsDynamicSqlSupport.FIELDSBLOBS;
import static mbg.test.mb3.generated.dsql.record.mapper.FieldsonlyDynamicSqlSupport.FIELDSONLY;
import static mbg.test.mb3.generated.dsql.record.mapper.PkblobsDynamicSqlSupport.PKBLOBS;
import static mbg.test.mb3.generated.dsql.record.mapper.PkfieldsDynamicSqlSupport.PKFIELDSTABLE;
import static mbg.test.mb3.generated.dsql.record.mapper.PkfieldsblobsDynamicSqlSupport.PKFIELDSBLOBS;
import static mbg.test.mb3.generated.dsql.record.mapper.PkonlyDynamicSqlSupport.PKONLY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import mbg.test.mb3.generated.dsql.record.mapper.AwfulTableMapper;
import mbg.test.mb3.generated.dsql.record.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkonlyMapper;
import mbg.test.mb3.generated.dsql.record.mapper.mbgtest.IdMapper;
import mbg.test.mb3.generated.dsql.record.mapper.mbgtest.sub.TranslationMapper;
import mbg.test.mb3.generated.dsql.record.model.AwfulTable;
import mbg.test.mb3.generated.dsql.record.model.Fieldsblobs;
import mbg.test.mb3.generated.dsql.record.model.Fieldsonly;
import mbg.test.mb3.generated.dsql.record.model.Pkblobs;
import mbg.test.mb3.generated.dsql.record.model.Pkfields;
import mbg.test.mb3.generated.dsql.record.model.Pkfieldsblobs;
import mbg.test.mb3.generated.dsql.record.model.Pkonly;
import mbg.test.mb3.generated.dsql.record.model.mbgtest.Id;
import mbg.test.mb3.generated.dsql.record.model.mbgtest.sub.Translation;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;

/**
 * @author Jeff Butler
 */
public class DynamicSqlRecordTest extends AbstractRecordTest {

    @Test
    public void testFieldsOnlyInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isEqualTo(5)));
            assertThat(answer.size()).isEqualTo(1);

            Fieldsonly returnedRecord = answer.get(0);
            assertThat(returnedRecord.integerfield()).isEqualTo(record.integerfield());
            assertThat(returnedRecord.doublefield()).isEqualTo(record.doublefield());
            assertThat(returnedRecord.floatfield()).isEqualTo(record.floatfield());
        }
    }

    @Test
    public void testFieldsOnlySelect() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isGreaterThan(5)));
            assertThat(answer.size()).isEqualTo(2);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(3);
        }
    }

    @Test
    public void testFieldsOnlySelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);

            Fieldsonly record1 = new Fieldsonly(5, 11.22, 33.44);
            Fieldsonly record2 = new Fieldsonly(8, 44.55, 66.77);
            Fieldsonly record3 = new Fieldsonly(9, 88.99, 100.111);

            mapper.insertMultiple(Arrays.asList(record1, record2, record3));

            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isGreaterThan(5)));
            assertThat(answer.size()).isEqualTo(2);

            answer = mapper.select(SelectDSLCompleter.allRowsOrderedBy(FIELDSONLY.INTEGERFIELD));
            assertThat(answer.size()).isEqualTo(3);
            assertThat(answer.get(0).integerfield()).isEqualTo(5);
            assertThat(answer.get(1).integerfield()).isEqualTo(8);
            assertThat(answer.get(2).integerfield()).isEqualTo(9);
        }
    }

    @Test
    public void testFieldsOnlySelectByExampleDistinct() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);
            mapper.insert(record);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.selectDistinct(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isEqualTo(5)));
            assertThat(answer.size()).isEqualTo(1);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(5);
        }
    }

    @Test
    public void testFieldsOnlySelectByExampleNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            List<Fieldsonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(3);
        }
    }

    @Test
    public void testFieldsOnlyDelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            int rows = mapper.delete(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isGreaterThan(5)));
            assertThat(rows).isEqualTo(2);

            List<Fieldsonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(1);
        }
    }

    @Test
    public void testFieldsOnlyCount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isGreaterThan(5)));
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
            assertThat(returnedRecord.id()).isEqualTo(key.id());
            assertThat(returnedRecord.seqNum()).isEqualTo(key.seqNum());
        }
    }

    @Test
    public void testPKOnlyDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            int rows = mapper.insert(key);
            assertThat(rows).isEqualTo(1);

            key = new Pkonly(5, 6);
            rows = mapper.insert(key);
            assertThat(rows).isEqualTo(1);

            List<Pkonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(2);

            rows = mapper.deleteByPrimaryKey(5, 6);
            assertThat(rows).isEqualTo(1);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(1);
        }
    }

    @Test
    public void testPKOnlyDelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            int rows = mapper.delete(dsl ->
                    dsl.where(PKONLY.ID, isGreaterThan(4)));
            assertThat(rows).isEqualTo(2);

            List<Pkonly> answer = mapper.select(SelectDSLCompleter.allRows());
            assertThat(answer.size()).isEqualTo(1);
        }
    }

    @Test
    public void testPKOnlySelect() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            List<Pkonly> answer = mapper.select(dsl ->
                    dsl.where(PKONLY.ID, isGreaterThan(4))
                    .orderBy(PKONLY.ID));
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).id().intValue()).isEqualTo(5);
            assertThat(answer.get(0).seqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).id().intValue()).isEqualTo(7);
            assertThat(answer.get(1).seqNum().intValue()).isEqualTo(8);
        }
    }

    @Test
    public void testPKOnlySelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);

            mapper.insertMultiple(Arrays.asList(new Pkonly(1,3), new Pkonly(5, 6), new Pkonly(7, 8)));

            List<Pkonly> answer = mapper.select(dsl ->
                    dsl.where(PKONLY.ID, isGreaterThan(4))
                    .orderBy(PKONLY.ID));
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).id().intValue()).isEqualTo(5);
            assertThat(answer.get(0).seqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).id().intValue()).isEqualTo(7);
            assertThat(answer.get(1).seqNum().intValue()).isEqualTo(8);
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
                c.where(PKONLY.ID, isGreaterThan(4))
                .orderBy(PKONLY.ID)
            );
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).id().intValue()).isEqualTo(5);
            assertThat(answer.get(0).seqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).id().intValue()).isEqualTo(7);
            assertThat(answer.get(1).seqNum().intValue()).isEqualTo(8);
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
                c.where(PKONLY.ID, isGreaterThan(4))
                .orderBy(PKONLY.ID)
            );
            assertThat(answer.size()).isEqualTo(2);
            assertThat(answer.get(0).id().intValue()).isEqualTo(5);
            assertThat(answer.get(0).seqNum().intValue()).isEqualTo(6);
            assertThat(answer.get(1).id().intValue()).isEqualTo(7);
            assertThat(answer.get(1).seqNum().intValue()).isEqualTo(8);
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
    public void testPKOnlyCount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            long rows = mapper.count(dsl ->
                    dsl.where(PKONLY.ID, isGreaterThan(4)));
            assertEquals(2, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(3, rows);
        }
    }

    @Test
    public void testPKFieldsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Butler", LocalDate.now(), LocalTime.of(13, 2, 4),
                    LocalDateTime.now(), (short) 3, 6, 10L, new BigDecimal("15.12345"), null, null, true);
            mapper.insert(record);

            Optional<Pkfields> returnedRecord = mapper.selectByPrimaryKey(2, 1);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.datefield(), rr.datefield());
                assertEquals(record.decimal100field(), rr.decimal100field());
                assertEquals(record.decimal155field(), rr.decimal155field());
                assertEquals(record.decimal30field(), rr.decimal30field());
                assertEquals(record.decimal60field(), rr.decimal60field());
                assertEquals(record.firstname(), rr.firstname());
                assertEquals(record.id1(), rr.id1());
                assertEquals(record.id2(), rr.id2());
                assertEquals(record.lastname(), rr.lastname());
                assertEquals(record.timefield(), rr.timefield());
                assertThat(record.timestampfield()).isCloseTo(rr.timestampfield(), within(1, ChronoUnit.MILLIS));
                assertEquals(record.stringboolean(), rr.stringboolean());
            });
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            Pkfields updateRecord = new Pkfields(2, 1, "Scott", "Junes", null, null, null, null, null, null, null, null, null, false);
            int rows = mapper.updateByPrimaryKey(updateRecord);
            assertEquals(1, rows);

            Optional<Pkfields> record2 = mapper.selectByPrimaryKey(2, 1);

            assertThat(record2).hasValueSatisfying(r2 -> {
                assertEquals(updateRecord.firstname(), r2.firstname());
                assertEquals(updateRecord.lastname(), r2.lastname());
                assertEquals(updateRecord.id1(), r2.id1());
                assertEquals(updateRecord.id2(), r2.id2());
            });
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, 5, null, null, null, null, false);
            mapper.insert(record);

            Pkfields newRecord = new Pkfields(2, 1, "Scott", null, null, null, null, null, 4, null, null, null, null, false);
            int rows = mapper.updateByPrimaryKeySelective(newRecord);
            assertEquals(1, rows);

            Optional<Pkfields> returnedRecord = mapper.selectByPrimaryKey(2, 1);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.datefield(), rr.datefield());
                assertEquals(record.decimal100field(), rr.decimal100field());
                assertEquals(record.decimal155field(), rr.decimal155field());
                assertEquals(record.decimal30field(), rr.decimal30field());
                assertEquals(newRecord.decimal60field(), rr.decimal60field());
                assertEquals(newRecord.firstname(), rr.firstname());
                assertEquals(record.id1(), rr.id1());
                assertEquals(record.id2(), rr.id2());
                assertEquals(record.lastname(), rr.lastname());
                assertEquals(record.timefield(), rr.timefield());
                assertEquals(record.timestampfield(), rr.timestampfield());
            });
        }
    }

    @Test
    public void testPKfieldsDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            int rows = mapper.deleteByPrimaryKey(2, 1);
            assertEquals(1, rows);

            List<Pkfields> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(0, answer.size());
        }
    }

    @Test
    public void testPKFieldsDelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(4, 3, "Bob", "Jones", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(PKFIELDSTABLE.LASTNAME, isLike("J%")));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            Pkfields record1 = new Pkfields(4, 3, "Bob", "Jones", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record1);

            Optional<Pkfields> newRecord = mapper.selectByPrimaryKey(4, 3);

            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.firstname(), nr.firstname());
                assertEquals(record1.lastname(), nr.lastname());
                assertEquals(record1.id1(), nr.id1());
                assertEquals(record1.id2(), nr.id2());
            });
        }
    }

    @Test
    public void testPKFieldsSelectByExampleLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSTABLE.FIRSTNAME, isLike("B%"))
                    .orderBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));
            assertEquals(3, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(2, returnedRecord.id1().intValue());
            assertEquals(1, returnedRecord.id2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.id1().intValue());
            assertEquals(2, returnedRecord.id2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(2, returnedRecord.id1().intValue());
            assertEquals(3, returnedRecord.id2().intValue());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNotLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSTABLE.FIRSTNAME, isNotLike("B%"))
                    .orderBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));
            assertEquals(3, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.id1().intValue());
            assertEquals(1, returnedRecord.id2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(1, returnedRecord.id1().intValue());
            assertEquals(2, returnedRecord.id2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(1, returnedRecord.id1().intValue());
            assertEquals(3, returnedRecord.id2().intValue());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleComplexLike() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSTABLE.FIRSTNAME, isLike("B%"), and(PKFIELDSTABLE.ID2, isEqualTo(3)))
                    .or(PKFIELDSTABLE.FIRSTNAME, isLike("Wi%"))
                    .orderBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));

            assertEquals(2, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.id1().intValue());
            assertEquals(2, returnedRecord.id2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(2, returnedRecord.id1().intValue());
            assertEquals(3, returnedRecord.id2().intValue());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleIn() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSTABLE.ID2, isIn(1, 3))
                    .orderBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));
            assertEquals(4, answer.size());
            Pkfields returnedRecord = answer.get(0);
            assertEquals(1, returnedRecord.id1().intValue());
            assertEquals(1, returnedRecord.id2().intValue());
            returnedRecord = answer.get(1);
            assertEquals(1, returnedRecord.id1().intValue());
            assertEquals(3, returnedRecord.id2().intValue());
            returnedRecord = answer.get(2);
            assertEquals(2, returnedRecord.id1().intValue());
            assertEquals(1, returnedRecord.id2().intValue());
            returnedRecord = answer.get(3);
            assertEquals(2, returnedRecord.id1().intValue());
            assertEquals(3, returnedRecord.id2().intValue());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleBetween() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSTABLE.ID2, isBetween(1).and(3))
                    .orderBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));
            assertEquals(6, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(
                    SelectDSLCompleter.allRowsOrderedBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));

            assertEquals(6, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNoCriteriaWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);

            Collection<Pkfields> records = new ArrayList<>();

            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            records.add(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            records.add(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, null, null, false);
            records.add(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, null, null, false);
            records.add(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, null, null, false);
            records.add(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, null, null, false);
            records.add(record);

            int rowsInserted = mapper.insertMultiple(records);

            assertEquals(6, rowsInserted);

            List<Pkfields> answer = mapper.select(
                    SelectDSLCompleter.allRowsOrderedBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));

            assertEquals(6, answer.size());
        }
    }

    @Test
    public void testPKFieldsSelectByExampleEscapedFields() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(1, 1, "Fred", "Flintstone", null, null, null, null, null, null, null, 11, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 1, "Wilma", "Flintstone", null, null, null, null, null, null, null, 22, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 1, "Pebbles", "Flintstone", null, null, null, null, null, null, null, 33, null, false);
            mapper.insert(record);

            record = new Pkfields(1, 2, "Barney", "Rubble", null, null, null, null, null, null, null, 44, null, false);
            mapper.insert(record);

            record = new Pkfields(2, 2, "Betty", "Rubble", null, null, null, null, null, null, null, 55, null, false);
            mapper.insert(record);

            record = new Pkfields(3, 2, "Bamm Bamm", "Rubble", null, null, null, null, null, null, null, 66, null, false);
            mapper.insert(record);

            List<Pkfields> answer = mapper.select(DSL ->
                    DSL.where(PKFIELDSTABLE.WIERD_FIELD, isLessThan(40))
                    .and(PKFIELDSTABLE.WIERD_FIELD, isIn(11, 22))
                    .orderBy(PKFIELDSTABLE.ID1, PKFIELDSTABLE.ID2));

            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKFieldsCount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(4, 3, "Bob", "Jones", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(PKFIELDSTABLE.LASTNAME, isLike("J%")));

            assertEquals(1, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testPKBlobsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            List<Pkblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());

            Pkblobs returnedRecord = answer.get(0);
            assertEquals(record.id(), returnedRecord.id());
            assertTrue(blobsAreEqual(record.blob1(), returnedRecord
                    .blob1()));
            assertTrue(blobsAreEqual(record.blob2(), returnedRecord
                    .blob2()));
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeyWithBLOBs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            Pkblobs record1 = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            int rows = mapper.updateByPrimaryKey(record1);
            assertEquals(1, rows);

            Optional<Pkblobs> newRecord = mapper.selectByPrimaryKey(3);

            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.id(), nr.id());
                assertTrue(blobsAreEqual(record1.blob1(), nr.blob1()));
                assertTrue(blobsAreEqual(record1.blob2(), nr.blob2()));
            });
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            Pkblobs newRecord = new Pkblobs(3, null, generateRandomBlob(), null);
            mapper.updateByPrimaryKeySelective(newRecord);

            Optional<Pkblobs> returnedRecord = mapper.selectByPrimaryKey(3);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.id(), rr.id());
                assertTrue(blobsAreEqual(record.blob1(), rr.blob1()));
                assertTrue(blobsAreEqual(newRecord.blob2(), rr.blob2()));
            });
        }
    }

    @Test
    public void testPKBlobsDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
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
    public void testPKBlobsDelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            List<Pkblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(PKBLOBS.ID, isLessThan(4)));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKBlobsSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            Pkblobs record1 = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record1);

            Optional<Pkblobs> newRecord = mapper.selectByPrimaryKey(6);

            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.id(), nr.id());
                assertTrue(blobsAreEqual(record1.blob1(), nr.blob1()));
                assertTrue(blobsAreEqual(record1.blob2(), nr.blob2()));
            });
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithBlobs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            List<Pkblobs> answer = mapper.select(DSL ->
                    DSL.where(PKBLOBS.ID, isGreaterThan(4)));

            assertEquals(1, answer.size());

            Pkblobs newRecord = answer.get(0);
            assertEquals(record.id(), newRecord.id());
            assertTrue(blobsAreEqual(record.blob1(), newRecord.blob1()));
            assertTrue(blobsAreEqual(record.blob2(), newRecord.blob2()));
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Collection<Pkblobs> records = new ArrayList<>();

            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            records.add(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(), null);
            records.add(record);

            int recordsInserted = mapper.insertMultiple(records);
            assertEquals(2, recordsInserted);

            List<Pkblobs> answer = mapper.select(dsl ->
                    dsl.where(PKBLOBS.ID, isGreaterThan(4)));

            assertEquals(1, answer.size());

            Pkblobs newRecord = answer.get(0);
            assertEquals(record.id(), newRecord.id());
            assertTrue(blobsAreEqual(record.blob1(), newRecord.blob1()));
            assertTrue(blobsAreEqual(record.blob2(), newRecord.blob2()));
        }
    }

    @Test
    public void testPKBlobsCount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(PKBLOBS.ID, isLessThan(4)));
            assertEquals(1, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testPKFieldsBlobsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());

            Pkfieldsblobs returnedRecord = answer.get(0);
            assertEquals(record.id1(), returnedRecord.id1());
            assertEquals(record.id2(), returnedRecord.id2());
            assertEquals(record.firstname(), returnedRecord.firstname());
            assertEquals(record.lastname(), returnedRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), returnedRecord
                    .blob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeyWithBLOBs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs(3, 4, "Scott", "Jones", generateRandomBlob());

            int rows = mapper.updateByPrimaryKey(updateRecord);
            assertEquals(1, rows);

            Optional<Pkfieldsblobs> newRecord = mapper.selectByPrimaryKey(3, 4);

            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(updateRecord.firstname(), nr.firstname());
                assertEquals(updateRecord.lastname(), nr.lastname());
                assertEquals(record.id1(), nr.id1());
                assertEquals(record.id2(), nr.id2());
                assertTrue(blobsAreEqual(updateRecord.blob1(), nr.blob1()));
            });
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs(3, 4, null, "Jones", null);

            int rows = mapper.updateByPrimaryKeySelective(updateRecord);
            assertEquals(1, rows);

            Optional<Pkfieldsblobs> returnedRecord = mapper.selectByPrimaryKey(3, 4);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(record.firstname(), rr.firstname());
                assertEquals(updateRecord.lastname(), rr.lastname());
                assertEquals(record.id1(), rr.id1());
                assertEquals(record.id2(), rr.id2());
                assertTrue(blobsAreEqual(record.blob1(), rr.blob1()));
            });
        }
    }

    @Test
    public void testPKFieldsBlobsDeleteByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
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
    public void testPKFieldsBlobsDelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(PKFIELDSBLOBS.ID1, isNotEqualTo(3)));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByPrimaryKey() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs record1 = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record1);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            Optional<Pkfieldsblobs> newRecord = mapper.selectByPrimaryKey(5, 6);

            assertThat(newRecord).hasValueSatisfying(nr -> {
                assertEquals(record1.id1(), nr.id1());
                assertEquals(record1.id2(), nr.id2());
                assertEquals(record1.firstname(), nr.firstname());
                assertEquals(record1.lastname(), nr.lastname());
                assertTrue(blobsAreEqual(record1.blob1(), nr.blob1()));
            });
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSBLOBS.ID2, isEqualTo(6)));
            assertEquals(1, answer.size());

            Pkfieldsblobs newRecord = answer.get(0);
            assertEquals(record.id1(), newRecord.id1());
            assertEquals(record.id2(), newRecord.id2());
            assertEquals(record.firstname(), newRecord.firstname());
            assertEquals(record.lastname(), newRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), newRecord.blob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Collection<Pkfieldsblobs> records = new ArrayList<>();

            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            records.add(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            records.add(record);

            int rowsInserted = mapper.insertMultiple(records);
            assertEquals(2, rowsInserted);

            List<Pkfieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSBLOBS.ID2, isEqualTo(6)));
            assertEquals(1, answer.size());

            Pkfieldsblobs newRecord = answer.get(0);
            assertEquals(record.id1(), newRecord.id1());
            assertEquals(record.id2(), newRecord.id2());
            assertEquals(record.firstname(), newRecord.firstname());
            assertEquals(record.lastname(), newRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), newRecord.blob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            List<Pkfieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testFieldsBlobsInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());

            Fieldsblobs returnedRecord = answer.get(0);
            assertEquals(record.firstname(), returnedRecord.firstname());
            assertEquals(record.lastname(), returnedRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), returnedRecord
                    .blob1()));
            assertTrue(blobsAreEqual(record.blob2(), returnedRecord
                    .blob2()));
        }
    }

    @Test
    public void testFieldsBlobsDelete() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Fieldsblobs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());

            int rows = mapper.delete(dsl ->
                    dsl.where(FIELDSBLOBS.FIRSTNAME, isLike("S%")));
            assertEquals(1, rows);

            answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Fieldsblobs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(FIELDSBLOBS.FIRSTNAME, isLike("S%")));
            assertEquals(1, answer.size());

            Fieldsblobs newRecord = answer.get(0);
            assertEquals(record.firstname(), newRecord.firstname());
            assertEquals(record.lastname(), newRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), newRecord.blob1()));
            assertTrue(blobsAreEqual(record.blob2(), newRecord.blob2()));
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithMultiInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Collection<Fieldsblobs> records = new ArrayList<>();

            Fieldsblobs record = new Fieldsblobs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            records.add(record);

            record = new Fieldsblobs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            records.add(record);

            int rowsInserted = mapper.insertMultiple(records);
            assertEquals(2, rowsInserted);

            List<Fieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(FIELDSBLOBS.FIRSTNAME, isLike("S%")));
            assertEquals(1, answer.size());

            Fieldsblobs newRecord = answer.get(0);
            assertEquals(record.firstname(), newRecord.firstname());
            assertEquals(record.lastname(), newRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), newRecord.blob1()));
            assertTrue(blobsAreEqual(record.blob2(), newRecord.blob2()));
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Fieldsblobs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            List<Fieldsblobs> answer = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobsCount() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            long rows = mapper.count(dsl ->
                    dsl.where(PKFIELDSBLOBS.ID1, isNotEqualTo(3)));
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
                assertFalse(rr.getActive1());
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
    public void testAwfulTableDelete() {
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
                    dsl.where(AWFUL_TABLE.E_MAIL, isLike("fred@%")));
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
                    dsl.where(AWFUL_TABLE.FIRST_FIRST_NAME, isLike("b%"))
                    .orderBy(AWFUL_TABLE.CUSTOMER_ID));
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
                    dsl.where(AWFUL_TABLE.FIRST_FIRST_NAME, isLike("b%"))
                    .orderBy(AWFUL_TABLE.CUSTOMER_ID));
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
                    dsl.where(AWFUL_TABLE.FIRST_FIRST_NAME, isNotLike("b%"))
                    .orderBy(AWFUL_TABLE.CUSTOMER_ID));
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
                    dsl.where(AWFUL_TABLE.FIRST_FIRST_NAME, isLike("b%"), and(AWFUL_TABLE.ID2, isEqualTo(222222)))
                    .or(AWFUL_TABLE.FIRST_FIRST_NAME, isLike("wi%"))
                    .orderBy(AWFUL_TABLE.CUSTOMER_ID));

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
                    dsl.where(AWFUL_TABLE.ID1, isIn(1, 11))
                    .orderBy(AWFUL_TABLE.CUSTOMER_ID));

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
                    dsl.where(AWFUL_TABLE.ID1, isBetween(1).and(1000)));
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
                    SelectDSLCompleter.allRowsOrderedBy(AWFUL_TABLE.CUSTOMER_ID.descending()));
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
                    dsl.where(AWFUL_TABLE.E_MAIL, isLike("fred@%")));
            assertEquals(1, rows);

            rows = mapper.count(dsl -> dsl);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testTranslationTable() {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TranslationMapper mapper = sqlSession.getMapper(TranslationMapper.class);

            Translation t1 = new Translation(1, "Spanish");
            mapper.insert(t1);

            Translation t2 = new Translation(2, "French");
            mapper.insert(t2);

            Optional<Translation> returnedRecord = mapper.selectByPrimaryKey(2);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(t2.id(), rr.id());
                assertEquals(t2.translation(), rr.translation());
            });

            Translation t3 = new Translation(2, "Italian");
            mapper.updateByPrimaryKey(t3);

            returnedRecord = mapper.selectByPrimaryKey(2);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(t3.id(), rr.id());
                assertEquals(t3.translation(), rr.translation());
            });
        }
    }

    @Test
    public void testIdTable() {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            IdMapper mapper = sqlSession.getMapper(IdMapper.class);

            Id idSpanish = new Id();
            idSpanish.setId(1);
            idSpanish.setDescription("Spanish");
            mapper.insert(idSpanish);

            Id idFrench = new Id();
            idFrench.setId(2);
            idFrench.setDescription("French");
            mapper.insert(idFrench);

            Optional<Id> returnedRecord = mapper.selectByPrimaryKey(2);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(idFrench.getId(), rr.getId());
                assertEquals(idFrench.getDescription(), rr.getDescription());
            });

            Id idItalian = new Id();
            idItalian.setId(2);
            idItalian.setDescription("Italian");
            mapper.updateByPrimaryKey(idItalian);

            returnedRecord = mapper.selectByPrimaryKey(2);

            assertThat(returnedRecord).hasValueSatisfying(rr -> {
                assertEquals(idItalian.getId(), rr.getId());
                assertEquals(idItalian.getDescription(), rr.getDescription());
            });

            List<Id> allIds = mapper.select(SelectDSLCompleter.allRows());
            assertThat(allIds).containsExactlyInAnyOrder(idSpanish, idItalian);
        }
    }
}
