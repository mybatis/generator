/*
 *    Copyright 2006-2025 the original author or authors.
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
package mbg.test.mb3.conditional.immutable;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.datesAreEqual;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static mbg.test.common.util.TestUtilities.timesAreEqual;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import mbg.test.mb3.generated.conditional.immutable.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.conditional.immutable.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.conditional.immutable.mapper.PkblobsMapper;
import mbg.test.mb3.generated.conditional.immutable.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.conditional.immutable.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.conditional.immutable.mapper.PkonlyMapper;
import mbg.test.mb3.generated.conditional.immutable.model.Fieldsblobs;
import mbg.test.mb3.generated.conditional.immutable.model.FieldsblobsExample;
import mbg.test.mb3.generated.conditional.immutable.model.FieldsblobsWithBLOBs;
import mbg.test.mb3.generated.conditional.immutable.model.Fieldsonly;
import mbg.test.mb3.generated.conditional.immutable.model.FieldsonlyExample;
import mbg.test.mb3.generated.conditional.immutable.model.Pkblobs;
import mbg.test.mb3.generated.conditional.immutable.model.PkblobsExample;
import mbg.test.mb3.generated.conditional.immutable.model.Pkfields;
import mbg.test.mb3.generated.conditional.immutable.model.PkfieldsExample;
import mbg.test.mb3.generated.conditional.immutable.model.PkfieldsKey;
import mbg.test.mb3.generated.conditional.immutable.model.Pkfieldsblobs;
import mbg.test.mb3.generated.conditional.immutable.model.PkfieldsblobsExample;
import mbg.test.mb3.generated.conditional.immutable.model.PkfieldsblobsKey;
import mbg.test.mb3.generated.conditional.immutable.model.PkonlyExample;
import mbg.test.mb3.generated.conditional.immutable.model.PkonlyKey;

/**
 * @author Jeff Butler
 */
public class ConditionalJava5Test extends AbstractConditionalImmutableTest {

    @Test
    public void testFieldsOnlyInsert() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldEqualTo(5);

            List<Fieldsonly> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());

            Fieldsonly returnedRecord = answer.get(0);
            assertEquals(record.getIntegerfield(), returnedRecord
                    .getIntegerfield());
            assertEquals(record.getDoublefield(), returnedRecord
                    .getDoublefield());
            assertEquals(record.getFloatfield(), returnedRecord.getFloatfield());
        }
    }

    @Test
    public void testFieldsOnlySelectByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);

            List<Fieldsonly> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());

            example = new FieldsonlyExample();
            answer = mapper.selectByExample(example);
            assertEquals(3, answer.size());
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

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria();

            List<Fieldsonly> answer = mapper.selectByExample(example);
            assertEquals(3, answer.size());
        }
    }

    @Test
    public void testFieldsOnlyDeleteByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);

            int rows = mapper.deleteByExample(example);
            assertEquals(2, rows);

            example = new FieldsonlyExample();
            List<Fieldsonly> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testFieldsOnlyCountByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);
            long rows = mapper.countByExample(example);
            assertEquals(2, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(3, rows);
        }
    }

    @Test
    public void testPKOnlyInsert() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            List<PkonlyKey> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());

            PkonlyKey returnedRecord = answer.get(0);
            assertEquals(key.getId(), returnedRecord.getId());
            assertEquals(key.getSeqNum(), returnedRecord.getSeqNum());
        }
    }

    @Test
    public void testPKOnlyDeleteByPrimaryKey() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            key = new PkonlyKey(5, 6);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            List<PkonlyKey> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());

            key = new PkonlyKey(5, 6);
            int rows = mapper.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKOnlyDeleteByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            key = new PkonlyKey(5, 6);
            mapper.insert(key);

            key = new PkonlyKey(7, 8);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            int rows = mapper.deleteByExample(example);
            assertEquals(2, rows);

            example = new PkonlyExample();
            List<PkonlyKey> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKOnlySelectByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            key = new PkonlyKey(5, 6);
            mapper.insert(key);

            key = new PkonlyKey(7, 8);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            List<PkonlyKey> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKOnlySelectByExampleNoCriteria() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            key = new PkonlyKey(5, 6);
            mapper.insert(key);

            key = new PkonlyKey(7, 8);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria();
            List<PkonlyKey> answer = mapper.selectByExample(example);
            assertEquals(3, answer.size());
        }
    }

    @Test
    public void testPKOnlyCountByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            key = new PkonlyKey(5, 6);
            mapper.insert(key);

            key = new PkonlyKey(7, 8);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            long rows = mapper.countByExample(example);
            assertEquals(2, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(3, rows);
        }
    }

    @Test
    public void testPKFieldsInsert() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
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

            mapper.insert(record);

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

            Pkfields returnedRecord = mapper.selectByPrimaryKey(key);
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

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

            Pkfields record2 = mapper.selectByPrimaryKey(key);

            assertEquals(record.getFirstname(), record2.getFirstname());
            assertEquals(record.getLastname(), record2.getLastname());
            assertEquals(record.getId1(), record2.getId1());
            assertEquals(record.getId2(), record2.getId2());
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

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

            Pkfields returnedRecord = mapper.selectByPrimaryKey(key);

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

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(1);
            key.setId2(2);

            int rows = mapper.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            PkfieldsExample example = new PkfieldsExample();
            List<Pkfields> answer = mapper.selectByExample(example);
            assertEquals(0, answer.size());
        }
    }

    @Test
    public void testPKFieldsDeleteByExample() {

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

            PkfieldsExample example = new PkfieldsExample();
            List<Pkfields> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());

            example = new PkfieldsExample();
            example.createCriteria().andLastnameLike("J%");
            int rows = mapper.deleteByExample(example);
            assertEquals(1, rows);

            example = new PkfieldsExample();
            answer = mapper.selectByExample(example);
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

            record = new Pkfields();
            record.setFirstname("Bob");
            record.setLastname("Jones");
            record.setId1(3);
            record.setId2(4);
            mapper.insert(record);

            PkfieldsKey key = new PkfieldsKey();
            key.setId1(3);
            key.setId2(4);
            Pkfields newRecord = mapper.selectByPrimaryKey(key);

            assertNotNull(newRecord);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
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

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameLike("B%");
            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
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

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameNotLike("B%");
            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
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

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andFirstnameLike("B%").andId2EqualTo(3);
            example.or(example.createCriteria().andFirstnameLike("Wi%"));

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
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

            List<Integer> ids = new ArrayList<>();
            ids.add(1);
            ids.add(3);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andId2In(ids);

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
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

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andId2Between(1, 3);

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
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

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria();

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
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

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andWierdFieldLessThan(40).andWierdFieldIn(
                    values);

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKFieldsCountByExample() {

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

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andLastnameLike("J%");
            long rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testPKBlobsInsert() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<Pkblobs> answer = mapper
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            Pkblobs returnedRecord = answer.get(0);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
            assertEquals(record.getCharacterlob(), returnedRecord.getCharacterlob());
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeyWithBLOBs() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            int rows = mapper.updateByPrimaryKeyWithBLOBs(record);
            assertEquals(1, rows);

            Pkblobs newRecord = mapper.selectByPrimaryKey(3);

            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
            assertEquals(record.getCharacterlob(), newRecord.getCharacterlob());
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeySelective() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            Pkblobs newRecord = new Pkblobs(3, null, generateRandomBlob(),
                    "Long String 2");
            mapper.updateByPrimaryKeySelective(newRecord);

            Pkblobs returnedRecord = mapper.selectByPrimaryKey(3);
            assertNotNull(returnedRecord);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(newRecord.getBlob2(), returnedRecord
                    .getBlob2()));
            assertEquals(newRecord.getCharacterlob(), returnedRecord.getCharacterlob());
        }
    }

    @Test
    public void testPKBlobsDeleteByPrimaryKey() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<Pkblobs> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());

            int rows = mapper.deleteByPrimaryKey(3);
            assertEquals(1, rows);

            example = new PkblobsExample();
            answer = mapper.selectByExample(example);
            assertEquals(0, answer.size());
        }
    }

    @Test
    public void testPKBlobsDeleteByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<Pkblobs> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());

            example = new PkblobsExample();
            example.createCriteria().andIdLessThan(4);
            int rows = mapper.deleteByExample(example);
            assertEquals(1, rows);

            example = new PkblobsExample();
            answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKBlobsSelectByPrimaryKey() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            Pkblobs newRecord = mapper.selectByPrimaryKey(6);
            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
            assertEquals(record.getCharacterlob(), newRecord.getCharacterlob());
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithoutBlobs() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            List<Pkblobs> answer = mapper.selectByExample(example);

            assertEquals(1, answer.size());

            Pkblobs key = answer.get(0);
            assertEquals(6, key.getId().intValue());
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithoutBlobsNoCriteria() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria();
            List<Pkblobs> answer = mapper.selectByExample(example);

            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithBlobs() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            List<Pkblobs> answer = mapper
                    .selectByExampleWithBLOBs(example);

            assertEquals(1, answer.size());

            Pkblobs newRecord = answer.get(0);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
            assertEquals(record.getCharacterlob(), newRecord.getCharacterlob());
        }
    }

    @Test
    public void testPKBlobsCountByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdLessThan(4);
            long rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testPKFieldsBlobsInsert() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(1, 2, ":Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = mapper
                    .selectByExampleWithBLOBs(example);
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
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs(3, 4, "Scott", "Jones", generateRandomBlob());

            int rows = mapper.updateByPrimaryKeyWithBLOBs(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey(3, 4);
            Pkfieldsblobs newRecord = mapper.selectByPrimaryKey(key);
            assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertTrue(blobsAreEqual(updateRecord.getBlob1(), newRecord
                    .getBlob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeyWithoutBLOBs() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs(3, 4, "Scott", "Jones");

            int rows = mapper.updateByPrimaryKey(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey(3, 4);
            Pkfieldsblobs newRecord = mapper.selectByPrimaryKey(key);
            assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
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

            PkfieldsblobsKey key = new PkfieldsblobsKey(3, 4);
            Pkfieldsblobs returnedRecord = mapper.selectByPrimaryKey(key);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), returnedRecord
                    .getLastname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
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

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = mapper
                    .selectByExample(example);
            assertEquals(2, answer.size());

            PkfieldsblobsKey key = new PkfieldsblobsKey(5, 6);
            int rows = mapper.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            example = new PkfieldsblobsExample();
            answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobsDeleteByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = mapper
                    .selectByExample(example);
            assertEquals(2, answer.size());

            example = new PkfieldsblobsExample();
            example.createCriteria().andId1NotEqualTo(3);
            int rows = mapper.deleteByExample(example);
            assertEquals(1, rows);

            example = new PkfieldsblobsExample();
            answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByPrimaryKey() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = mapper
                    .selectByExample(example);
            assertEquals(2, answer.size());

            PkfieldsblobsKey key = new PkfieldsblobsKey(5, 6);
            Pkfieldsblobs newRecord = mapper.selectByPrimaryKey(key);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithoutBlobs() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId2EqualTo(6);
            List<Pkfieldsblobs> answer = mapper
                    .selectByExample(example);
            assertEquals(1, answer.size());

            Pkfieldsblobs newRecord = answer.get(0);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
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

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId2EqualTo(6);
            List<Pkfieldsblobs> answer = mapper
                    .selectByExampleWithBLOBs(example);
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
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria();
            List<Pkfieldsblobs> answer = mapper.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testPKFieldsBlobsCountByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId1NotEqualTo(3);
            long rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        }
    }

    @Test
    public void testFieldsBlobsInsert() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            List<FieldsblobsWithBLOBs> answer = mapper
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            FieldsblobsWithBLOBs returnedRecord = answer.get(0);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
        }
    }

    @Test
    public void testFieldsBlobsDeleteByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new FieldsblobsWithBLOBs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            List<Fieldsblobs> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());

            example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = mapper.deleteByExample(example);
            assertEquals(1, rows);

            example = new FieldsblobsExample();
            answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithoutBlobs() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new FieldsblobsWithBLOBs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            List<Fieldsblobs> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());

            Fieldsblobs newRecord = answer.get(0);
            assertFalse(newRecord instanceof FieldsblobsWithBLOBs);
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobs() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new FieldsblobsWithBLOBs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            List<FieldsblobsWithBLOBs> answer = mapper
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            FieldsblobsWithBLOBs newRecord = answer.get(0);
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
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new FieldsblobsWithBLOBs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria();
            List<FieldsblobsWithBLOBs> answer = mapper.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        }
    }

    @Test
    public void testFieldsBlobsCountByExample() {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new FieldsblobsWithBLOBs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            long rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        }
    }
}
