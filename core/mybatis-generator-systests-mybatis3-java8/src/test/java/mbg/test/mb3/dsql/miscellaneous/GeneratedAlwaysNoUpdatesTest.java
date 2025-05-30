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
package mbg.test.mb3.dsql.miscellaneous;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;

import mbg.test.mb3.generated.dsql.miscellaneous.mapper.GeneratedalwaystestnoupdatesMapper;
import mbg.test.mb3.generated.dsql.miscellaneous.model.Generatedalwaystestnoupdates;

public class GeneratedAlwaysNoUpdatesTest extends AbstractAnnotatedMiscellaneousTest {

    @Test
    public void testInsert() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            GeneratedalwaystestnoupdatesMapper mapper = sqlSession.getMapper(GeneratedalwaystestnoupdatesMapper.class);

            Generatedalwaystestnoupdates gaTest = new Generatedalwaystestnoupdates();
            gaTest.setId(1);
            gaTest.setIdPlus1(55);
            gaTest.setIdPlus2(66);
            int rows = mapper.insert(gaTest);
            assertEquals(1, rows);

            List<Generatedalwaystestnoupdates> returnedRecords = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, returnedRecords.size());

            Generatedalwaystestnoupdates returnedRecord = returnedRecords.get(0);
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(2, returnedRecord.getIdPlus1().intValue());
            assertEquals(3, returnedRecord.getIdPlus2().intValue());
        }
    }

    @Test
    public void testInsertSelective() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            GeneratedalwaystestnoupdatesMapper mapper = sqlSession.getMapper(GeneratedalwaystestnoupdatesMapper.class);

            Generatedalwaystestnoupdates gaTest = new Generatedalwaystestnoupdates();
            gaTest.setId(1);
            int rows = mapper.insertSelective(gaTest);
            assertEquals(1, rows);

            List<Generatedalwaystestnoupdates> returnedRecords = mapper.select(SelectDSLCompleter.allRows());
            assertEquals(1, returnedRecords.size());

            Generatedalwaystestnoupdates returnedRecord = returnedRecords.get(0);
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(2, returnedRecord.getIdPlus1().intValue());
            assertEquals(3, returnedRecord.getIdPlus2().intValue());
        }
    }

    @Test
    public void testThatUpdatesByPrimaryKeyDidNotGetGenerated() {
        Method[] methods = GeneratedalwaystestnoupdatesMapper.class.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("updateByPrimaryKey")) {
                fail("Method " + method.getName() + " should not be generated");
            }
        }
    }
}
