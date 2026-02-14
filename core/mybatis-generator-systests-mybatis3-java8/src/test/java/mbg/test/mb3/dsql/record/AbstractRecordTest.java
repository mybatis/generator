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

import static mbg.test.common.util.TestUtilities.createDatabase;

import mbg.test.mb3.generated.dsql.record.mapper.AwfulTableMapper;
import mbg.test.mb3.generated.dsql.record.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.dsql.record.mapper.PkonlyMapper;
import mbg.test.mb3.generated.dsql.record.mapper.mbgtest.IdMapper;
import mbg.test.mb3.generated.dsql.record.mapper.mbgtest.sub.TranslationMapper;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Jeff Butler
 */
public abstract class AbstractRecordTest {

    private static final String JDBC_URL = "jdbc:hsqldb:mem:aname";
    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

    protected SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    public void setUp() throws Exception {
        createDatabase();

        UnpooledDataSource ds = new UnpooledDataSource(JDBC_DRIVER, JDBC_URL, "sa", "");
        Environment environment = new Environment("test", new JdbcTransactionFactory(), ds);
        Configuration config = new Configuration(environment);
        config.addMapper(AwfulTableMapper.class);
        config.addMapper(FieldsblobsMapper.class);
        config.addMapper(FieldsonlyMapper.class);
        config.addMapper(PkblobsMapper.class);
        config.addMapper(PkfieldsblobsMapper.class);
        config.addMapper(PkfieldsMapper.class);
        config.addMapper(PkonlyMapper.class);
        config.addMapper(TranslationMapper.class);
        config.addMapper(IdMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
    }
}
