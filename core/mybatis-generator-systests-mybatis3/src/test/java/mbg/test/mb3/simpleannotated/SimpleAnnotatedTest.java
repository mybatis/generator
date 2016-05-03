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
package mbg.test.mb3.simpleannotated;

import static mbg.test.common.util.TestUtilities.datesAreEqual;
import static mbg.test.common.util.TestUtilities.timesAreEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import mbg.test.common.util.TestUtilities;
import mbg.test.mb3.generated.simpleannotated.mapper.AwfulTableMapper;
import mbg.test.mb3.generated.simpleannotated.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.simpleannotated.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.simpleannotated.mapper.PkblobsMapper;
import mbg.test.mb3.generated.simpleannotated.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.simpleannotated.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.simpleannotated.mapper.PkonlyMapper;
import mbg.test.mb3.generated.simpleannotated.model.AwfulTable;
import mbg.test.mb3.generated.simpleannotated.model.Fieldsblobs;
import mbg.test.mb3.generated.simpleannotated.model.Fieldsonly;
import mbg.test.mb3.generated.simpleannotated.model.Pkblobs;
import mbg.test.mb3.generated.simpleannotated.model.Pkfields;
import mbg.test.mb3.generated.simpleannotated.model.Pkfieldsblobs;
import mbg.test.mb3.generated.simpleannotated.model.Pkonly;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class SimpleAnnotatedTest extends AbstractSimpleAnnotatedTest {

    @Test
    public void testAwfulTable() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            AwfulTableMapper mapper = sqlSession.getMapper(AwfulTableMapper.class);
            
            AwfulTable record = new AwfulTable();
            record.setFirstFirstName("Fred");
            int rows = mapper.insert(record);
            assertEquals(1, rows);
            assertNotNull(record.getCustomerId());
            
            record = new AwfulTable();
            record.setFirstFirstName("Barney");
            rows = mapper.insert(record);
            assertEquals(1, rows);
            assertNotNull(record.getCustomerId());
            
            List<AwfulTable> records = mapper.selectAll();
            assertEquals(2, records.size());
            
            AwfulTable returnedRecord = mapper.selectByPrimaryKey(record.getCustomerId());
            assertNotNull(returnedRecord);
            assertEquals(record.getFirstFirstName(), returnedRecord.getFirstFirstName());
            
            record.setFirstFirstName("Betty");
            rows = mapper.updateByPrimaryKey(record);
            assertEquals(1, rows);
            
            returnedRecord = mapper.selectByPrimaryKey(record.getCustomerId());
            assertNotNull(returnedRecord);
            assertEquals(record.getFirstFirstName(), returnedRecord.getFirstFirstName());
            
            rows = mapper.deleteByPrimaryKey(record.getCustomerId());
            assertEquals(1, rows);
            
            records = mapper.selectAll();
            assertEquals(1, records.size());
            
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

            Pkfields returnedRecord = mapper.selectByPrimaryKey(2, 1);
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

            Pkfields record2 = mapper.selectByPrimaryKey(2, 1);
            assertNotNull(record2);

            assertEquals(record.getFirstname(), record2.getFirstname());
            assertEquals(record.getLastname(), record2.getLastname());
            assertEquals(record.getId1(), record2.getId1());
            assertEquals(record.getId2(), record2.getId2());
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

            int rows = mapper.deleteByPrimaryKey(2, 1);
            assertEquals(1, rows);

            List<Pkfields> answer = mapper.selectAll();
            assertEquals(0, answer.size());
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

            Pkfields newRecord = mapper.selectByPrimaryKey(4, 3);

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
    public void testPKFieldsSelectAll() {
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

            List<Pkfields> answer = mapper.selectAll();
            assertEquals(6, answer.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsOnly() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            
            Fieldsonly record = new Fieldsonly();
            record.setDoublefield(1.23);
            record.setFloatfield(4.35);
            record.setIntegerfield(9);
            mapper.insert(record);
            
            record = new Fieldsonly();
            record.setDoublefield(11.2233);
            record.setFloatfield(44.3355);
            record.setIntegerfield(99);
            mapper.insert(record);
            
            List<Fieldsonly> records = mapper.selectAll();
            
            assertEquals(2, records.size());
            
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testFieldsblobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            
            Fieldsblobs record = new Fieldsblobs();
            record.setFirstname("Fred");
            record.setBlob1(TestUtilities.generateRandomBlob());
            mapper.insert(record);
            
            record = new Fieldsblobs();
            record.setFirstname("Barney");
            record.setBlob1(TestUtilities.generateRandomBlob());
            mapper.insert(record);
            
            List<Fieldsblobs> records = mapper.selectAll();
            
            assertEquals(2, records.size());
            
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPkblobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            
            Pkblobs record = new Pkblobs();
            record.setId(1);
            record.setBlob1(TestUtilities.generateRandomBlob());
            int rows = mapper.insert(record);
            assertEquals(1, rows);
            
            record = new Pkblobs();
            record.setId(2);
            record.setBlob1(TestUtilities.generateRandomBlob());
            rows = mapper.insert(record);
            assertEquals(1, rows);
            
            List<Pkblobs> records = mapper.selectAll();
            assertEquals(2, records.size());
            
            Pkblobs returnedRecord = mapper.selectByPrimaryKey(record.getId());
            assertNotNull(returnedRecord);
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            
            record.setBlob1(TestUtilities.generateRandomBlob());
            rows = mapper.updateByPrimaryKey(record);
            assertEquals(1, rows);
            
            returnedRecord = mapper.selectByPrimaryKey(record.getId());
            assertNotNull(returnedRecord);
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            
            rows = mapper.deleteByPrimaryKey(record.getId());
            assertEquals(1, rows);
            
            records = mapper.selectAll();
            assertEquals(1, records.size());
            
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testPkfieldsblobs() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            
            Pkfieldsblobs record = new Pkfieldsblobs();
            record.setId1(1);
            record.setId2(1);
            record.setBlob1(TestUtilities.generateRandomBlob());
            int rows = mapper.insert(record);
            assertEquals(1, rows);
            
            record = new Pkfieldsblobs();
            record.setId1(2);
            record.setId2(2);
            record.setBlob1(TestUtilities.generateRandomBlob());
            rows = mapper.insert(record);
            assertEquals(1, rows);
            
            List<Pkfieldsblobs> records = mapper.selectAll();
            assertEquals(2, records.size());
            
            Pkfieldsblobs returnedRecord = mapper.selectByPrimaryKey(record.getId1(), record.getId2());
            assertNotNull(returnedRecord);
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            
            record.setBlob1(TestUtilities.generateRandomBlob());
            rows = mapper.updateByPrimaryKey(record);
            assertEquals(1, rows);
            
            returnedRecord = mapper.selectByPrimaryKey(record.getId1(), record.getId2());
            assertNotNull(returnedRecord);
            assertTrue(TestUtilities.blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            
            rows = mapper.deleteByPrimaryKey(record.getId1(), record.getId2());
            assertEquals(1, rows);
            
            records = mapper.selectAll();
            assertEquals(1, records.size());
            
        } finally {
            sqlSession.close();
        }
    }
    
    @Test
    public void testPkonly() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);

            Pkonly record = new Pkonly();
            record.setId(1);
            record.setSeqNum(1);
            mapper.insert(record);
            
            record = new Pkonly();
            record.setId(1);
            record.setSeqNum(2);
            mapper.insert(record);
            
            record = new Pkonly();
            record.setId(2);
            record.setSeqNum(1);
            mapper.insert(record);
            
            record = new Pkonly();
            record.setId(2);
            record.setSeqNum(2);
            mapper.insert(record);
            
            List<Pkonly> records = mapper.selectAll();
            assertEquals(4, records.size());
            
            mapper.deleteByPrimaryKey(record.getId(), record.getSeqNum());
            
            records = mapper.selectAll();
            assertEquals(3, records.size());
        } finally {
            sqlSession.close();
        }
    }
}
