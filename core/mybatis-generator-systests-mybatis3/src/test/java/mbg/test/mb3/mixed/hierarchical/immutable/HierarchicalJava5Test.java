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
package mbg.test.mb3.mixed.hierarchical.immutable;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.datesAreEqual;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static mbg.test.common.util.TestUtilities.timesAreEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Mapper.PkblobsMapper;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Mapper.PkfieldsMapper;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Mapper.PkonlyMapper;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.Fieldsblobs;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.FieldsblobsExample;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.FieldsblobsWithBLOBs;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.Fieldsonly;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.FieldsonlyExample;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkblobsExample;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkblobsKey;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkblobsWithBLOBs;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.Pkfields;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkfieldsExample;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkfieldsKey;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.Pkfieldsblobs;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkfieldsblobsExample;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkfieldsblobsKey;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkfieldsblobsWithBLOBs;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkonlyExample;
import mbg.test.mb3.generated.mixed.hierarchical.Immutable.Model.PkonlyKey;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author Jeff Butler
 * 
 */
public class HierarchicalJava5Test extends AbstractMixedHierarchicalImmutableTest {

    @Test
    public void testFieldsOnlyInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsOnlySelectByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsOnlySelectByExampleNoCriteria() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsOnlyDeleteByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsOnlyCountByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            FieldsonlyExample example = new FieldsonlyExample();
            example.createCriteria().andIntegerfieldGreaterThan(5);
            int rows = mapper.countByExample(example);
            assertEquals(2, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(3, rows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlyInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            List<PkonlyKey> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());

            PkonlyKey returnedRecord = answer.get(0);
            assertEquals(key.getId(), returnedRecord.getId());
            assertEquals(key.getSeqNum(), returnedRecord.getSeqNum());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlyDeleteByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlyDeleteByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlySelectByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlySelectByExampleNoCriteria() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlyCountByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            PkonlyKey key = new PkonlyKey(1, 3);
            mapper.insert(key);

            key = new PkonlyKey(5, 6);
            mapper.insert(key);

            key = new PkonlyKey(7, 8);
            mapper.insert(key);

            PkonlyExample example = new PkonlyExample();
            example.createCriteria().andIdGreaterThan(4);
            int rows = mapper.countByExample(example);
            assertEquals(2, rows);

            example.clear();
            rows  = mapper.countByExample(example);
            assertEquals(3, rows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKfieldsDeleteByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsDeleteByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByExampleLike() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNotLike() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByExampleComplexLike() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByExampleIn() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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

            List<Integer> ids = new ArrayList<Integer>();
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByExampleBetween() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByExampleNoCriteria() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsSelectByExampleEscapedFields() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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

            List<Integer> values = new ArrayList<Integer>();
            values.add(11);
            values.add(22);

            PkfieldsExample example = new PkfieldsExample();
            example.createCriteria().andWierdFieldLessThan(40).andWierdFieldIn(
                    values);

            example.setOrderByClause("ID1, ID2");
            List<Pkfields> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsCountByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
            int rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<PkblobsWithBLOBs> answer = mapper
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            PkblobsWithBLOBs returnedRecord = answer.get(0);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord
                    .getBlob2()));
            assertEquals(record.getCharacterlob(), returnedRecord.getCharacterlob());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeyWithBLOBs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            int rows = mapper.updateByPrimaryKeyWithBLOBs(record);
            assertEquals(1, rows);

            PkblobsKey key = new PkblobsKey(3);

            PkblobsWithBLOBs newRecord = mapper.selectByPrimaryKey(key);

            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
            assertEquals(record.getCharacterlob(), newRecord.getCharacterlob());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            PkblobsWithBLOBs newRecord = new PkblobsWithBLOBs(3, null, generateRandomBlob(),
                    "Long String 2");
            mapper.updateByPrimaryKeySelective(newRecord);

            PkblobsKey key = new PkblobsKey(3);

            PkblobsWithBLOBs returnedRecord = mapper.selectByPrimaryKey(key);
            assertNotNull(returnedRecord);
            assertEquals(record.getId(), returnedRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
            assertTrue(blobsAreEqual(newRecord.getBlob2(), returnedRecord
                    .getBlob2()));
            assertEquals(newRecord.getCharacterlob(), returnedRecord.getCharacterlob());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsDeleteByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<PkblobsKey> answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());

            PkblobsKey key = new PkblobsKey(3);
            int rows = mapper.deleteByPrimaryKey(key);
            assertEquals(1, rows);

            example = new PkblobsExample();
            answer = mapper.selectByExample(example);
            assertEquals(0, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsDeleteByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new PkblobsWithBLOBs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            List<PkblobsKey> answer = mapper.selectByExample(example);
            assertEquals(2, answer.size());

            example = new PkblobsExample();
            example.createCriteria().andIdLessThan(4);
            int rows = mapper.deleteByExample(example);
            assertEquals(1, rows);

            example = new PkblobsExample();
            answer = mapper.selectByExample(example);
            assertEquals(1, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsSelectByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new PkblobsWithBLOBs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsKey key = new PkblobsKey(6);
            PkblobsWithBLOBs newRecord = mapper.selectByPrimaryKey(key);
            assertNotNull(newRecord);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
            assertEquals(record.getCharacterlob(), newRecord.getCharacterlob());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithoutBlobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new PkblobsWithBLOBs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            List<PkblobsKey> answer = mapper.selectByExample(example);

            assertEquals(1, answer.size());

            PkblobsKey key = answer.get(0);
            assertFalse(key instanceof PkblobsWithBLOBs);
            assertEquals(6, key.getId().intValue());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithoutBlobsNoCriteria() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new PkblobsWithBLOBs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria();
            List<PkblobsKey> answer = mapper.selectByExample(example);

            assertEquals(2, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsSelectByExampleWithBlobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new PkblobsWithBLOBs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdGreaterThan(4);
            List<PkblobsWithBLOBs> answer = mapper
                    .selectByExampleWithBLOBs(example);

            assertEquals(1, answer.size());

            PkblobsWithBLOBs newRecord = answer.get(0);
            assertEquals(record.getId(), newRecord.getId());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), newRecord.getBlob2()));
            assertEquals(record.getCharacterlob(), newRecord.getCharacterlob());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsCountByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            PkblobsWithBLOBs record = new PkblobsWithBLOBs(3, generateRandomBlob(), generateRandomBlob(),
                    "Long String 1");
            mapper.insert(record);

            record = new PkblobsWithBLOBs(6, generateRandomBlob(), generateRandomBlob(),
                    "Long String 2");
            mapper.insert(record);

            PkblobsExample example = new PkblobsExample();
            example.createCriteria().andIdLessThan(4);
            int rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(1, 2, ":Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<PkfieldsblobsWithBLOBs> answer = mapper
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            PkfieldsblobsWithBLOBs returnedRecord = answer.get(0);
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeyWithBLOBs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsWithBLOBs updateRecord = new PkfieldsblobsWithBLOBs(3, 4, "Scott", "Jones", generateRandomBlob());

            int rows = mapper.updateByPrimaryKeyWithBLOBs(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey(3, 4);
            PkfieldsblobsWithBLOBs newRecord = mapper.selectByPrimaryKey(key);
            assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertTrue(blobsAreEqual(updateRecord.getBlob1(), newRecord
                    .getBlob1()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeyWithoutBLOBs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs(3, 4, "Scott", "Jones");

            int rows = mapper.updateByPrimaryKey(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey(3, 4);
            PkfieldsblobsWithBLOBs newRecord = mapper.selectByPrimaryKey(key);
            assertEquals(updateRecord.getFirstname(), newRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), newRecord.getLastname());
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsWithBLOBs updateRecord = new PkfieldsblobsWithBLOBs(3, 4, null, "Jones", null);

            int rows = mapper.updateByPrimaryKeySelective(updateRecord);
            assertEquals(1, rows);

            PkfieldsblobsKey key = new PkfieldsblobsKey(3, 4);
            PkfieldsblobsWithBLOBs returnedRecord = mapper.selectByPrimaryKey(key);
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(updateRecord.getLastname(), returnedRecord
                    .getLastname());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord
                    .getBlob1()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsDeleteByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new PkfieldsblobsWithBLOBs(5, 6, "Scott", "Jones", generateRandomBlob());
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsDeleteByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new PkfieldsblobsWithBLOBs(5, 6, "Scott", "Jones", generateRandomBlob());
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByPrimaryKey() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new PkfieldsblobsWithBLOBs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            List<Pkfieldsblobs> answer = mapper
                    .selectByExample(example);
            assertEquals(2, answer.size());

            PkfieldsblobsKey key = new PkfieldsblobsKey(5, 6);
            PkfieldsblobsWithBLOBs newRecord = mapper.selectByPrimaryKey(key);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithoutBlobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new PkfieldsblobsWithBLOBs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId2EqualTo(6);
            List<Pkfieldsblobs> answer = mapper
                    .selectByExample(example);
            assertEquals(1, answer.size());

            Pkfieldsblobs newRecord = answer.get(0);
            assertFalse(newRecord instanceof PkfieldsblobsWithBLOBs);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new PkfieldsblobsWithBLOBs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId2EqualTo(6);
            List<PkfieldsblobsWithBLOBs> answer = mapper
                    .selectByExampleWithBLOBs(example);
            assertEquals(1, answer.size());

            PkfieldsblobsWithBLOBs newRecord = answer.get(0);
            assertEquals(record.getId1(), newRecord.getId1());
            assertEquals(record.getId2(), newRecord.getId2());
            assertEquals(record.getFirstname(), newRecord.getFirstname());
            assertEquals(record.getLastname(), newRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), newRecord.getBlob1()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new PkfieldsblobsWithBLOBs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria();
            List<PkfieldsblobsWithBLOBs> answer = mapper.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsCountByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            PkfieldsblobsWithBLOBs record = new PkfieldsblobsWithBLOBs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new PkfieldsblobsWithBLOBs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            PkfieldsblobsExample example = new PkfieldsblobsExample();
            example.createCriteria().andId1NotEqualTo(3);
            int rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsDeleteByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithoutBlobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsSelectByExampleWithBlobsNoCriteria() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new FieldsblobsWithBLOBs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria();
            List<FieldsblobsWithBLOBs> answer = mapper.selectByExampleWithBLOBs(example);
            assertEquals(2, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsCountByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            FieldsblobsWithBLOBs record = new FieldsblobsWithBLOBs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new FieldsblobsWithBLOBs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            FieldsblobsExample example = new FieldsblobsExample();
            example.createCriteria().andFirstnameLike("S%");
            int rows = mapper.countByExample(example);
            assertEquals(1, rows);

            example.clear();
            rows = mapper.countByExample(example);
            assertEquals(2, rows);
        } finally {
            sqlSession.close();
        }
    }
}
