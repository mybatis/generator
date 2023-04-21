/*
 *    Copyright 2006-2023 the original author or authors.
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
package mbg.test.mb3.dsql.kotlin.miscellaneous

import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.mapper.*
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory

import mbg.test.common.util.TestUtilities.createDatabase
import org.apache.ibatis.session.SqlSession

abstract class AbstractAnnotatedMiscellaneousTest {

    protected fun openSession(): SqlSession {
        createDatabase()

        val ds = UnpooledDataSource(JDBC_DRIVER, JDBC_URL, "sa", "")
        val environment = Environment("test", JdbcTransactionFactory(), ds)
        val config = Configuration(environment)
        config.addMapper(EnumtestMapper::class.java)
        config.addMapper(EnumordinaltestMapper::class.java)
        config.addMapper(GeneratedalwaystestMapper::class.java)
        config.addMapper(GeneratedalwaystestnoupdatesMapper::class.java)
        config.addMapper(MyObjectMapper::class.java)
        config.addMapper(RegexrenameMapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession()
    }

    companion object {
        private const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        private const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
