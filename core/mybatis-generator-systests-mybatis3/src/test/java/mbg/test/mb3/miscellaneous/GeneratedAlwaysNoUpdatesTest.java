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
package mbg.test.mb3.miscellaneous;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import mbg.test.mb3.generated.miscellaneous.mapper.GeneratedalwaystestnoupdatesMapper;
import mbg.test.mb3.generated.miscellaneous.model.Generatedalwaystestnoupdates;

public class GeneratedAlwaysNoUpdatesTest extends AbstractMiscellaneousTest {

    @Test
    public void testInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            GeneratedalwaystestnoupdatesMapper mapper = sqlSession.getMapper(GeneratedalwaystestnoupdatesMapper.class);
            
            Generatedalwaystestnoupdates gaTest = new Generatedalwaystestnoupdates();
            gaTest.setId(1);
            gaTest.setIdPlus1(55);
            gaTest.setIdPlus2(66);
            int rows = mapper.insert(gaTest);
            assertEquals(1, rows);
            
            List<Generatedalwaystestnoupdates> returnedRecords = mapper.selectByExample(null);
            assertEquals(1, returnedRecords.size());
            
            Generatedalwaystestnoupdates returnedRecord = returnedRecords.get(0);
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(2, returnedRecord.getIdPlus1().intValue());
            assertEquals(3, returnedRecord.getIdPlus2().intValue());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsertSelective() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            GeneratedalwaystestnoupdatesMapper mapper = sqlSession.getMapper(GeneratedalwaystestnoupdatesMapper.class);
            
            Generatedalwaystestnoupdates gaTest = new Generatedalwaystestnoupdates();
            gaTest.setId(1);
            int rows = mapper.insertSelective(gaTest);
            assertEquals(1, rows);
            
            List<Generatedalwaystestnoupdates> returnedRecords = mapper.selectByExample(null);
            assertEquals(1, returnedRecords.size());
            
            Generatedalwaystestnoupdates returnedRecord = returnedRecords.get(0);
            assertEquals(1, returnedRecord.getId().intValue());
            assertEquals(2, returnedRecord.getIdPlus1().intValue());
            assertEquals(3, returnedRecord.getIdPlus2().intValue());
        } finally {
            sqlSession.close();
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
