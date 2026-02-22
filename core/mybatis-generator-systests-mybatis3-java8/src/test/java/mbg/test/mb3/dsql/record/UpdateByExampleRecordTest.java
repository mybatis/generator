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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isLike;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;

import java.util.List;

import mbg.test.mb3.generated.dsql.record.mapper.AwfulTableMapper;
import mbg.test.mb3.generated.dsql.record.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkonlyMapper;
import mbg.test.mb3.generated.dsql.record.model.AwfulTable;
import mbg.test.mb3.generated.dsql.record.model.Fieldsblobs;
import mbg.test.mb3.generated.dsql.record.model.Fieldsonly;
import mbg.test.mb3.generated.dsql.record.model.Pkblobs;
import mbg.test.mb3.generated.dsql.record.model.Pkfields;
import mbg.test.mb3.generated.dsql.record.model.Pkfieldsblobs;
import mbg.test.mb3.generated.dsql.record.model.Pkonly;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

/**
 * @author Jeff Butler
 */
public class UpdateByExampleRecordTest extends AbstractRecordTest {

    @Test
    public void testFieldsOnlyUpdateByExampleSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            Fieldsonly updateRecord = new Fieldsonly(null, 99d, null);

            int rows = mapper.update(dsl ->
                FieldsonlyMapper.updateSelectiveColumns(updateRecord, dsl)
                .where(FIELDSONLY.INTEGERFIELD, isGreaterThan(5))
            );
            assertEquals(2, rows);

            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isEqualTo(5)));
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(11.22, record.doublefield(), 0.001);
            assertEquals(33.44, record.floatfield(), 0.001);
            assertEquals(5, record.integerfield().intValue());

            answer = mapper.select(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isEqualTo(8)));

            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(99d, record.doublefield(), 0.001);
            assertEquals(66.77, record.floatfield(), 0.001);
            assertEquals(8, record.integerfield().intValue());

            answer = mapper.select(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isEqualTo(9)));
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertEquals(99d, record.doublefield(), 0.001);
            assertEquals(100.111, record.floatfield(), 0.001);
            assertEquals(9, record.integerfield().intValue());
        }
    }

    @Test
    public void testFieldsOnlyUpdateByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsonlyMapper mapper = sqlSession.getMapper(FieldsonlyMapper.class);
            Fieldsonly record = new Fieldsonly(5, 11.22, 33.44);
            mapper.insert(record);

            record = new Fieldsonly(8, 44.55, 66.77);
            mapper.insert(record);

            record = new Fieldsonly(9, 88.99, 100.111);
            mapper.insert(record);

            Fieldsonly updateRecord = new Fieldsonly(22, null, null);

            int rows = mapper.update(dsl ->
                FieldsonlyMapper.updateAllColumns(updateRecord, dsl)
                .where(FIELDSONLY.INTEGERFIELD, isEqualTo(5)));
            assertEquals(1, rows);

            List<Fieldsonly> answer = mapper.select(dsl ->
                    dsl.where(FIELDSONLY.INTEGERFIELD, isEqualTo(22)));
            assertEquals(1, answer.size());
            record = answer.get(0);
            assertNull(record.doublefield());
            assertNull(record.floatfield());
            assertEquals(22, record.integerfield().intValue());
        }
    }

    @Test
    public void testPKOnlyUpdateByExampleSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            Pkonly updateKey = new Pkonly(null, 3);

            int rows = mapper.update(dsl ->
                PkonlyMapper.updateSelectiveColumns(updateKey, dsl)
                .where(PKONLY.ID, isGreaterThan(4)));
            assertEquals(2, rows);

            long returnedRows = mapper.count(dsl ->
                    dsl.where(PKONLY.ID, isEqualTo(5))
                    .and(PKONLY.SEQ_NUM, isEqualTo(3)));
            assertEquals(1, returnedRows);

            returnedRows = mapper.count(dsl ->
                    dsl.where(PKONLY.ID, isEqualTo(7))
                    .and(PKONLY.SEQ_NUM, isEqualTo(3)));
            assertEquals(1, returnedRows);
        }
    }

    @Test
    public void testPKOnlyUpdateByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkonlyMapper mapper = sqlSession.getMapper(PkonlyMapper.class);
            Pkonly key = new Pkonly(1, 3);
            mapper.insert(key);

            key = new Pkonly(5, 6);
            mapper.insert(key);

            key = new Pkonly(7, 8);
            mapper.insert(key);

            Pkonly updateKey = new Pkonly(22, 3);

            int rows = mapper.update(dsl -> PkonlyMapper.updateAllColumns(updateKey, dsl).where(PKONLY.ID, isEqualTo(7)));
            assertEquals(1, rows);

            long returnedRows = mapper.count(dsl ->
                    dsl.where(PKONLY.ID, isEqualTo(22))
                    .and(PKONLY.SEQ_NUM, isEqualTo(3)));
            assertEquals(1, returnedRows);
        }
    }

    @Test
    public void testPKFieldsUpdateByExampleSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(4, 3, "Bob", "Jones", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            Pkfields updateRecord = new Pkfields(null, null, "Fred", null, null, null, null, null, null, null, null, null, null, false);

            int rows = mapper.update(dsl ->
                PkfieldsMapper.updateSelectiveColumns(updateRecord, dsl)
                .where(PKFIELDSTABLE.LASTNAME, isLike("J%")));
            assertEquals(1, rows);

            long returnedRows = mapper.count(dsl ->
                    dsl.where(PKFIELDSTABLE.FIRSTNAME, isEqualTo("Fred"))
                    .and(PKFIELDSTABLE.LASTNAME, isEqualTo("Jones"))
                    .and(PKFIELDSTABLE.ID1, isEqualTo(3))
                    .and(PKFIELDSTABLE.ID2, isEqualTo(4)));
            assertEquals(1, returnedRows);
        }
    }

    @Test
    public void testPKFieldsUpdateByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsMapper mapper = sqlSession.getMapper(PkfieldsMapper.class);
            Pkfields record = new Pkfields(2, 1, "Jeff", "Smith", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            record = new Pkfields(4, 3, "Bob", "Jones", null, null, null, null, null, null, null, null, null, false);
            mapper.insert(record);

            Pkfields updateRecord = new Pkfields(4, 3, "Fred", null, null, null, null, null, null, null, null, null, null, false);

            int rows = mapper.update(dsl ->
                PkfieldsMapper.updateAllColumns(updateRecord, dsl)
                .where(PKFIELDSTABLE.ID1, isEqualTo(3))
                .and(PKFIELDSTABLE.ID2, isEqualTo(4)));
            assertEquals(1, rows);

            long returnedRows = mapper.count(dsl ->
                    dsl.where(PKFIELDSTABLE.FIRSTNAME, isEqualTo("Fred"))
                    .and(PKFIELDSTABLE.ID1, isEqualTo(3))
                    .and(PKFIELDSTABLE.ID2, isEqualTo(4)));
            assertEquals(1, returnedRows);
        }
    }

    @Test
    public void testPKBlobsUpdateByExampleSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            Pkblobs newRecord = new Pkblobs(null, generateRandomBlob(), null, null);

            int rows = mapper.update(dsl ->
                PkblobsMapper.updateSelectiveColumns(newRecord, dsl)
                .where(PKBLOBS.ID, isGreaterThan(4)));
            assertEquals(1, rows);

            List<Pkblobs> answer = mapper.select(dsl ->
                    dsl.where(PKBLOBS.ID, isGreaterThan(4)));
            assertEquals(1, answer.size());

            Pkblobs returnedRecord = answer.get(0);

            assertEquals(6, returnedRecord.id().intValue());
            assertTrue(blobsAreEqual(newRecord.blob1(), returnedRecord.blob1()));
            assertTrue(blobsAreEqual(record.blob2(), returnedRecord.blob2()));
        }
    }

    @Test
    public void testPKBlobsUpdateByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkblobsMapper mapper = sqlSession.getMapper(PkblobsMapper.class);
            Pkblobs record = new Pkblobs(3, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Pkblobs(6, generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            Pkblobs newRecord = new Pkblobs(8, null, null, null);

            int rows = mapper.update(dsl ->
                PkblobsMapper.updateAllColumns(newRecord, dsl)
                .where(PKBLOBS.ID, isGreaterThan(4)));
            assertEquals(1, rows);

            List<Pkblobs> answer = mapper.select(dsl ->
                    dsl.where(PKBLOBS.ID, isGreaterThan(4)));
            assertEquals(1, answer.size());

            Pkblobs returnedRecord = answer.get(0);

            assertEquals(8, returnedRecord.id().intValue());
            assertNull(returnedRecord.blob1());
            assertNull(returnedRecord.blob2());
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByExampleSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs newRecord = new Pkfieldsblobs(null, null, "Fred", null, null);

            int rows = mapper.update(dsl ->
                PkfieldsblobsMapper.updateSelectiveColumns(newRecord, dsl)
                .where(PKFIELDSBLOBS.ID1, isNotEqualTo(3)));
            assertEquals(1, rows);

            List<Pkfieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSBLOBS.ID1, isNotEqualTo(3)));
            assertEquals(1, answer.size());

            Pkfieldsblobs returnedRecord = answer.get(0);

            assertEquals(record.id1(), returnedRecord.id1());
            assertEquals(record.id2(), returnedRecord.id2());
            assertEquals(newRecord.firstname(), returnedRecord.firstname());
            assertEquals(record.lastname(), returnedRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), returnedRecord.blob1()));
        }
    }

    @Test
    public void testPKFieldsBlobsUpdateByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = sqlSession.getMapper(PkfieldsblobsMapper.class);
            Pkfieldsblobs record = new Pkfieldsblobs(3, 4, "Jeff", "Smith", generateRandomBlob());
            mapper.insert(record);

            record = new Pkfieldsblobs(5, 6, "Scott", "Jones", generateRandomBlob());
            mapper.insert(record);

            Pkfieldsblobs newRecord = new Pkfieldsblobs(3, 8, "Fred", null, null);

            int rows = mapper.update(dsl ->
                PkfieldsblobsMapper.updateAllColumns(newRecord, dsl)
                .where(PKFIELDSBLOBS.ID1, isEqualTo(3)));
            assertEquals(1, rows);

            List<Pkfieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(PKFIELDSBLOBS.ID1, isEqualTo(3)));
            assertEquals(1, answer.size());

            Pkfieldsblobs returnedRecord = answer.get(0);

            assertEquals(newRecord.id1(), returnedRecord.id1());
            assertEquals(newRecord.id2(), returnedRecord.id2());
            assertEquals(newRecord.firstname(), returnedRecord.firstname());
            assertNull(returnedRecord.lastname());
            assertNull(returnedRecord.blob1());
        }
    }

    @Test
    public void testFieldsBlobsUpdateByExampleSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Fieldsblobs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            Fieldsblobs newRecord = new Fieldsblobs(null, "Doe", null, null, null);

            int rows = mapper.update(dsl ->
                FieldsblobsMapper.updateSelectiveColumns(newRecord, dsl)
                .where(FIELDSBLOBS.FIRSTNAME, isLike("S%")));
            assertEquals(1, rows);

            List<Fieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(FIELDSBLOBS.FIRSTNAME, isLike("S%")));
            assertEquals(1, answer.size());

            Fieldsblobs returnedRecord = answer.get(0);

            assertEquals(record.firstname(), returnedRecord.firstname());
            assertEquals(newRecord.lastname(), returnedRecord.lastname());
            assertTrue(blobsAreEqual(record.blob1(), returnedRecord.blob1()));
            assertTrue(blobsAreEqual(record.blob2(), returnedRecord.blob2()));
        }
    }

    @Test
    public void testFieldsBlobsUpdateByExample() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            FieldsblobsMapper mapper = sqlSession.getMapper(FieldsblobsMapper.class);
            Fieldsblobs record = new Fieldsblobs("Jeff", "Smith", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            record = new Fieldsblobs("Scott", "Jones", generateRandomBlob(), generateRandomBlob(), null);
            mapper.insert(record);

            Fieldsblobs newRecord = new Fieldsblobs("Scott", "Doe", null, null, null);

            int rows = mapper.update(dsl ->
                FieldsblobsMapper.updateAllColumns(newRecord, dsl)
                .where(FIELDSBLOBS.FIRSTNAME, isLike("S%")));
            assertEquals(1, rows);

            List<Fieldsblobs> answer = mapper.select(dsl ->
                    dsl.where(FIELDSBLOBS.FIRSTNAME, isLike("S%")));
            assertEquals(1, answer.size());

            Fieldsblobs returnedRecord = answer.get(0);

            assertEquals(newRecord.firstname(), returnedRecord.firstname());
            assertEquals(newRecord.lastname(), returnedRecord.lastname());
            assertNull(returnedRecord.blob1());
            assertNull(returnedRecord.blob2());
        }
    }

    @Test
    public void testAwfulTableUpdateByExampleSelective() {
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

            AwfulTable newRecord = new AwfulTable();
            newRecord.setFirstFirstName("Alonzo");

            int rows = mapper.update(dsl ->
                AwfulTableMapper.updateSelectiveColumns(newRecord, dsl)
                .where(AWFUL_TABLE.E_MAIL, isLike("fred2@%")));
            assertEquals(1, rows);

            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(AWFUL_TABLE.E_MAIL, isLike("fred2@%")));
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
        }
    }

    @Test
    public void testAwfulTableUpdateByExample() {
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

            AwfulTable newRecord = new AwfulTable();
            newRecord.setFirstFirstName("Alonzo");
            newRecord.setCustomerId(58);
            newRecord.setId1(111);
            newRecord.setId2(222);
            newRecord.setId5(555);
            newRecord.setId6(666);
            newRecord.setId7(777);

            int rows = mapper.update(dsl ->
                AwfulTableMapper.updateAllColumns(newRecord, dsl)
                .where(AWFUL_TABLE.E_MAIL, isLike("fred2@%")));
            assertEquals(1, rows);

            List<AwfulTable> answer = mapper.select(dsl ->
                    dsl.where(AWFUL_TABLE.CUSTOMER_ID, isEqualTo(58)));
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
        }
    }
}
