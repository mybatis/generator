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
package mbg.test.mb3.record;

import static mbg.test.common.util.TestUtilities.blobsAreEqual;
import static mbg.test.common.util.TestUtilities.createDatabase;
import static mbg.test.common.util.TestUtilities.generateRandomBlob;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import mbg.test.mb3.generated.record.simpleannotated.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.record.simpleannotated.model.Pkfieldsblobs;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleAnnotatedRecordTest {
    private static final String JDBC_URL = "jdbc:hsqldb:mem:aname";
    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    void setup() throws Exception {
        createDatabase();

        UnpooledDataSource ds = new UnpooledDataSource(JDBC_DRIVER, JDBC_URL, "sa", "");
        Environment environment = new Environment("test", new JdbcTransactionFactory(), ds);
        Configuration config = new Configuration(environment);
        config.addMapper(PkfieldsblobsMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
    }

    @Test
    void testPKFieldsBlobsDeleteByPrimaryKey() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PkfieldsblobsMapper mapper = session.getMapper(PkfieldsblobsMapper.class);

            Pkfieldsblobs flintstone = new Pkfieldsblobs(1, 2, "Fred", "Flintstone", generateRandomBlob());
            int rows = mapper.insert(flintstone);
            assertThat(rows).isEqualTo(1);

            Pkfieldsblobs rubble = new Pkfieldsblobs(3, 4, "Barney", "Rubble", generateRandomBlob());
            rows = mapper.insert(rubble);
            assertThat(rows).isEqualTo(1);

            rows = mapper.deleteByPrimaryKey(1, 2);
            assertThat(rows).isEqualTo(1);

            long count = mapper.selectAll().size();
            assertThat(count).isEqualTo(1);

            Pkfieldsblobs updateRecord = new Pkfieldsblobs(3, 4, "Betty", rubble.lastname(), rubble.blob1());
            rows = mapper.updateByPrimaryKey(updateRecord);
            assertThat(rows).isEqualTo(1);

            Pkfieldsblobs expectedRecord = new Pkfieldsblobs(3, 4, "Betty", "Rubble", rubble.blob1());
            Pkfieldsblobs returnedRecord = mapper.selectByPrimaryKey(3, 4);
            assertThat(returnedRecord).isNotNull()
                    .extracting(Pkfieldsblobs::id1, Pkfieldsblobs::id2, Pkfieldsblobs::firstname, Pkfieldsblobs::lastname)
                    .containsExactly(expectedRecord.id1(), expectedRecord.id2(), expectedRecord.firstname(), expectedRecord.lastname());
            assertThat(blobsAreEqual(returnedRecord.blob1(), expectedRecord.blob1())).isTrue();

            List<Pkfieldsblobs> records = mapper.selectAll();
            assertThat(records).isNotNull()
                    .hasSize(1);
            assertThat(blobsAreEqual(records.get(0).blob1(), expectedRecord.blob1())).isTrue();
        }
    }
}
