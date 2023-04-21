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
package mbg.test.mb3.common

import mbg.test.common.FirstName
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.TypeHandler
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * @author Jeff Butler
 */
class FirstNameTypeHandler : TypeHandler<FirstName> {

    override fun getResult(cs: CallableStatement, columnIndex: Int): FirstName? =
        cs.getString(columnIndex)?.toFirstName()

    override fun getResult(rs: ResultSet, columnName: String): FirstName? =
        rs.getString(columnName)?.toFirstName()

    override fun getResult(rs: ResultSet, columnIndex: Int): FirstName? =
        rs.getString(columnIndex)?.toFirstName()

    private fun String.toFirstName(): FirstName =
        FirstName().also {
            it.value = this
        }

    override fun setParameter(ps: PreparedStatement, i: Int, parameter: FirstName?,
                              jdbcType: JdbcType) {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE)
        } else {
            ps.setString(i, parameter.value)
        }
    }
}
