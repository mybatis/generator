/**
 *    Copyright 2006-2018 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import mbg.test.mb3.generated.miscellaneous.mapper.IgnoremanycolumnsMapper;
import mbg.test.mb3.generated.miscellaneous.model.Ignoremanycolumns;

public class IgnoreManyColumnsTest extends AbstractMiscellaneousTest {

    @Test
    public void testField02Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col02");
        });
    }

    @Test
    public void testField03Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col03");
        });
    }

    @Test
    public void testField04Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col04");
        });
    }

    @Test
    public void testField05Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col05");
        });
    }

    @Test
    public void testField06Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col06");
        });
    }

    @Test
    public void testField07Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col07");
        });
    }

    @Test
    public void testField08Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col08");
        });
    }

    @Test
    public void testField09Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col09");
        });
    }

    @Test
    public void testField10Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col10");
        });
    }

    @Test
    public void testField11Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col11");
        });
    }

    @Test
    public void testField12Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col12");
        });
    }

    @Test
    public void testField14Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col14");
        });
    }

    @Test
    public void testField15Ignored() {
        assertThrows(NoSuchFieldException.class, () -> {
            Ignoremanycolumns.class.getDeclaredField("col15");
        });
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
