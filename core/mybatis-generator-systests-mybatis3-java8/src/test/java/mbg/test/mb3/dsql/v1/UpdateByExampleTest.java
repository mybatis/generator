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
package mbg.test.mb3.dsql.v1;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static mbg.test.mb3.generated.dsql.v1.mapper.AwfulTableDynamicSqlSupport.awfulTable;
import static mbg.test.mb3.generated.dsql.v1.mapper.FieldsblobsDynamicSqlSupport.fieldsblobs;
import static mbg.test.mb3.generated.dsql.v1.mapper.FieldsonlyDynamicSqlSupport.fieldsonly;
import static mbg.test.mb3.generated.dsql.v1.mapper.PkblobsDynamicSqlSupport.pkblobs;
import static mbg.test.mb3.generated.dsql.v1.mapper.PkfieldsDynamicSqlSupport.pkfields;
import static mbg.test.mb3.generated.dsql.v1.mapper.PkfieldsblobsDynamicSqlSupport.pkfieldsblobs;
import static mbg.test.mb3.generated.dsql.v1.mapper.PkonlyDynamicSqlSupport.pkonly;
import static org.junit.jupiter.api.Assertions.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import mbg.test.mb3.generated.dsql.v1.mapper.AwfulTableMapper;
import mbg.test.mb3.generated.dsql.v1.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.dsql.v1.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.dsql.v1.mapper.PkblobsMapper;
import mbg.test.mb3.generated.dsql.v1.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.dsql.v1.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.dsql.v1.mapper.PkonlyMapper;
import mbg.test.mb3.generated.dsql.v1.model.AwfulTable;
import mbg.test.mb3.generated.dsql.v1.model.Fieldsblobs;
import mbg.test.mb3.generated.dsql.v1.model.Fieldsonly;
import mbg.test.mb3.generated.dsql.v1.model.Pkblobs;
import mbg.test.mb3.generated.dsql.v1.model.Pkfields;
import mbg.test.mb3.generated.dsql.v1.model.Pkfieldsblobs;
import mbg.test.mb3.generated.dsql.v1.model.Pkonly;

/**
 * 
 * @author Jeff Butler
 *
 */
public class UpdateByExampleTest extends AbstractTest {

    @Test
    public void testFieldsOnlyUpdateByExampleSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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

            record = new Fieldsonly();
            record.setDoublefield(99d);
            
            int rows = mapper.updateByExampleSelective(record)
                    .where(fieldsonly.integerfield, isGreaterThan(5))
                    .build()
                    .execute();
            assertEquals(2, rows);

            List<Fieldsonly> answer = mapper.selectByExample()
                    .where(fieldsonly.integerfield, isEqualTo(5))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(record.getDoublefield(), 11.22, 0.001);
            assertEquals(record.getFloatfield(), 33.44, 0.001);
            assertEquals(record.getIntegerfield().intValue(), 5);
            
            answer = mapper.selectByExample()
                    .where(fieldsonly.integerfield, isEqualTo(8))
                    .build()
                    .execute();
                    
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(record.getDoublefield(), 99d, 0.001);
            assertEquals(record.getFloatfield(), 66.77, 0.001);
            assertEquals(record.getIntegerfield().intValue(), 8);
            
            answer = mapper.selectByExample()
                    .where(fieldsonly.integerfield, isEqualTo(9))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(record.getDoublefield(), 99d, 0.001);
            assertEquals(record.getFloatfield(), 100.111, 0.001);
            assertEquals(record.getIntegerfield().intValue(), 9);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsOnlyUpdateByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
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

            record = new Fieldsonly();
            record.setIntegerfield(22);
            
            int rows = mapper.updateByExample(record)
                    .where(fieldsonly.integerfield, isEqualTo(5))
                    .build()
                    .execute();
            assertEquals(1, rows);

            List<Fieldsonly> answer = mapper.selectByExample()
                    .where(fieldsonly.integerfield, isEqualTo(22))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertNull(record.getDoublefield());
            assertNull(record.getFloatfield());
            assertEquals(record.getIntegerfield().intValue(), 22);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlyUpdateByExampleSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            key = new Pkonly(null, 3);
            int rows = mapper.updateByExampleSelective(key)
                    .where(pkonly.id, isGreaterThan(4))
                    .build()
                    .execute();
            assertEquals(2, rows);

            long returnedRows = mapper.countByExample()
                    .where(pkonly.id, isEqualTo(5))
                    .and(pkonly.seqNum, isEqualTo(3))
                    .build()
                    .execute();
            assertEquals(1, returnedRows);
            
            returnedRows = mapper.countByExample()
                    .where(pkonly.id, isEqualTo(7))
                    .and(pkonly.seqNum, isEqualTo(3))
                    .build()
                    .execute();
            assertEquals(1, returnedRows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKOnlyUpdateByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            key = new Pkonly(22, 3);
            int rows = mapper.updateByExample(key)
                    .where(pkonly.id, isEqualTo(7))
                    .build()
                    .execute();
            assertEquals(1, rows);

            long returnedRows = mapper.countByExample()
                    .where(pkonly.id, isEqualTo(22))
                    .and(pkonly.seqNum, isEqualTo(3))
                    .build()
                    .execute();
            assertEquals(1, returnedRows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsUpdateByExampleSelective() {
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

            record = new Pkfields();
            record.setFirstname("Fred");
            int rows = mapper.updateByExampleSelective(record)
                    .where(pkfields.lastname, isLike("J%"))
                    .build()
                    .execute();
            assertEquals(1, rows);
            
            long returnedRows = mapper.countByExample()
                    .where(pkfields.firstname, isEqualTo("Fred"))
                    .and(pkfields.lastname, isEqualTo("Jones"))
                    .and(pkfields.id1, isEqualTo(3))
                    .and(pkfields.id2, isEqualTo(4))
                    .build()
                    .execute();
            assertEquals(1, returnedRows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsUpdateByExample() {
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

            record = new Pkfields();
            record.setFirstname("Fred");
            record.setId1(3);
            record.setId2(4);
            
            int rows = mapper.updateByExample(record)
                    .where(pkfields.id1, isEqualTo(3))
                    .and(pkfields.id2, isEqualTo(4))
                    .build()
                    .execute();
            assertEquals(1, rows);
            
            long returnedRows = mapper.countByExample()
                    .where(pkfields.firstname, isEqualTo("Fred"))
                    .and(pkfields.id1, isEqualTo(3))
                    .and(pkfields.id2, isEqualTo(4))
                    .build()
                    .execute();
            assertEquals(1, returnedRows);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsUpdateByExampleSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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
    
            Pkblobs newRecord = new Pkblobs();
            newRecord.setBlob1(generateRandomBlob());
            
            int rows = mapper.updateByExampleSelective(newRecord)
                    .where(pkblobs.id, isGreaterThan(4))
                    .build()
                    .execute();
            assertEquals(1, rows);
            
            List<Pkblobs> answer = mapper.selectByExample()
                    .where(pkblobs.id, isGreaterThan(4))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            Pkblobs returnedRecord = answer.get(0);
            
            assertEquals(6, returnedRecord.getId().intValue());
            assertTrue(blobsAreEqual(newRecord.getBlob1(), returnedRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKBlobsUpdateByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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
    
            Pkblobs newRecord = new Pkblobs();
            newRecord.setId(8);
            
            int rows = mapper.updateByExample(newRecord)
                    .where(pkblobs.id, isGreaterThan(4))
                    .build()
                    .execute();
            assertEquals(1, rows);
            
            List<Pkblobs> answer = mapper.selectByExample()
                    .where(pkblobs.id, isGreaterThan(4))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            Pkblobs returnedRecord = answer.get(0);
            
            assertEquals(8, returnedRecord.getId().intValue());
            assertNull(returnedRecord.getBlob1());
            assertNull(returnedRecord.getBlob2());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByExampleSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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

            Pkfieldsblobs newRecord = new Pkfieldsblobs();
            newRecord.setFirstname("Fred");
            int rows = mapper.updateByExampleSelective(newRecord)
                    .where(pkfieldsblobs.id1, isNotEqualTo(3))
                    .build()
                    .execute();
            assertEquals(1, rows);
    
            List<Pkfieldsblobs> answer = mapper.selectByExample()
                    .where(pkfieldsblobs.id1, isNotEqualTo(3))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            Pkfieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertEquals(record.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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

            Pkfieldsblobs newRecord = new Pkfieldsblobs();
            newRecord.setId1(3);
            newRecord.setId2(8);
            newRecord.setFirstname("Fred");
            int rows = mapper.updateByExample(newRecord)
                    .where(pkfieldsblobs.id1, isEqualTo(3))
                    .build()
                    .execute();
            assertEquals(1, rows);
    
            List<Pkfieldsblobs> answer = mapper.selectByExample()
                    .where(pkfieldsblobs.id1, isEqualTo(3))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            Pkfieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertNull(returnedRecord.getLastname());
            assertNull(returnedRecord.getBlob1());
            
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsUpdateByExampleSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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

            Fieldsblobs newRecord = new Fieldsblobs();
            newRecord.setLastname("Doe");
            int rows = mapper.updateByExampleSelective(newRecord)
                    .where(fieldsblobs.firstname, isLike("S%"))
                    .build()
                    .execute();
            assertEquals(1, rows);
            
            List<Fieldsblobs> answer = mapper.selectByExample()
                    .where(fieldsblobs.firstname, isLike("S%"))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            Fieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(record.getFirstname(), returnedRecord.getFirstname());
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertTrue(blobsAreEqual(record.getBlob1(), returnedRecord.getBlob1()));
            assertTrue(blobsAreEqual(record.getBlob2(), returnedRecord.getBlob2()));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testFieldsBlobsUpdateByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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

            Fieldsblobs newRecord = new Fieldsblobs();
            newRecord.setFirstname("Scott");
            newRecord.setLastname("Doe");
            int rows = mapper.updateByExample(newRecord)
                    .where(fieldsblobs.firstname, isLike("S%"))
                    .build()
                    .execute();
                            
            assertEquals(1, rows);
            
            List<Fieldsblobs> answer = mapper.selectByExample()
                    .where(fieldsblobs.firstname, isLike("S%"))
                    .build()
                    .execute();
            assertEquals(1, answer.size());
            
            Fieldsblobs returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getFirstname(), returnedRecord.getFirstname());
            assertEquals(newRecord.getLastname(), returnedRecord.getLastname());
            assertNull(returnedRecord.getBlob1());
            assertNull(returnedRecord.getBlob2());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAwfulTableUpdateByExampleSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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
    
            AwfulTable newRecord = new AwfulTable();
            newRecord.setFirstFirstName("Alonzo");
            int rows = mapper.updateByExampleSelective(newRecord)
                    .where(awfulTable.eMail, isLike("fred2@%"))
                    .build()
                    .execute();
            assertEquals(1, rows);
    
            List<AwfulTable> answer = mapper.selectByExample()
                    .where(awfulTable.eMail, isLike("fred2@%"))
                    .build()
                    .execute();
            assertEquals(1, answer.size());

            AwfulTable returnedRecord = answer.get(0);
            
            assertEquals(record.getCustomerId(), returnedRecord.getCustomerId());
            assertEquals(record.geteMail(), returnedRecord.geteMail());
            assertEquals(record.getEmailaddress(), returnedRecord.getEmailaddress());
            assertEquals(newRecord.getFirstFirstName(), returnedRecord.getFirstFirstName());
            assertEquals(record.getFrom(), returnedRecord.getFrom());
            assertEquals(record.getId1(), returnedRecord.getId1());
            assertEquals(record.getId2(), returnedRecord.getId2());
            assertEquals(record.getId5(), returnedRecord.getId5());
            assertEquals(record.getId6(), returnedRecord.getId6());
            assertEquals(record.getId7(), returnedRecord.getId7());
            assertEquals(record.getSecondFirstName(), returnedRecord.getSecondFirstName());
            assertEquals(record.getThirdFirstName(), returnedRecord.getThirdFirstName());
            
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAwfulTableUpdateByExample() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
    
        try {
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
    
            AwfulTable newRecord = new AwfulTable();
            newRecord.setFirstFirstName("Alonzo");
            newRecord.setCustomerId(58);
            newRecord.setId1(111);
            newRecord.setId2(222);
            newRecord.setId5(555);
            newRecord.setId6(666);
            newRecord.setId7(777);
            int rows = mapper.updateByExample(newRecord)
                    .where(awfulTable.eMail, isLike("fred2@%"))
                    .build()
                    .execute();
            assertEquals(1, rows);

            List<AwfulTable> answer = mapper.selectByExample()
                    .where(awfulTable.customerId, isEqualTo(58))
                    .build()
                    .execute();
            assertEquals(1, answer.size());

            AwfulTable returnedRecord = answer.get(0);
            
            assertEquals(newRecord.getCustomerId(), returnedRecord.getCustomerId());
            assertNull(returnedRecord.geteMail());
            assertNull(returnedRecord.getEmailaddress());
            assertEquals(newRecord.getFirstFirstName(), returnedRecord.getFirstFirstName());
            assertNull(returnedRecord.getFrom());
            assertEquals(newRecord.getId1(), returnedRecord.getId1());
            assertEquals(newRecord.getId2(), returnedRecord.getId2());
            assertEquals(newRecord.getId5(), returnedRecord.getId5());
            assertEquals(newRecord.getId6(), returnedRecord.getId6());
            assertEquals(newRecord.getId7(), returnedRecord.getId7());
            assertNull(returnedRecord.getSecondFirstName());
            assertNull(returnedRecord.getThirdFirstName());
        } finally {
            sqlSession.close();
        }
    }
}
