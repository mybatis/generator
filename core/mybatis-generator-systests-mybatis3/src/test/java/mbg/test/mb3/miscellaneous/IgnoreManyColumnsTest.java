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

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import mbg.test.mb3.generated.miscellaneous.mapper.IgnoremanycolumnsMapper;
import mbg.test.mb3.generated.miscellaneous.model.Ignoremanycolumns;

public class IgnoreManyColumnsTest extends AbstractMiscellaneousTest {

    @Test(expected=NoSuchFieldException.class)
    public void testField02Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col02");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField03Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col03");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField04Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col04");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField05Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col05");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField06Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col06");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField07Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col07");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField08Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col08");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField09Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col09");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField10Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col10");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField11Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col11");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField12Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col12");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField14Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col14");
    }

    @Test(expected=NoSuchFieldException.class)
    public void testField15Ignored() throws NoSuchFieldException {
        Ignoremanycolumns.class.getDeclaredField("col15");
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            IgnoremanycolumnsMapper mapper = sqlSession.getMapper(IgnoremanycolumnsMapper.class);
            
            Ignoremanycolumns imc = new Ignoremanycolumns();
            imc.setCol01(22);
            imc.setCol13(33);
            int rows = mapper.insert(imc);
            assertEquals(1, rows);
            
            List<Ignoremanycolumns> returnedRecords = mapper.selectByExample(null);
            assertEquals(1, returnedRecords.size());
            
            Ignoremanycolumns returnedRecord = returnedRecords.get(0);
            assertEquals(22, returnedRecord.getCol01().intValue());
            assertEquals(33, returnedRecord.getCol13().intValue());
        } finally {
            sqlSession.close();
        }
    }
}
