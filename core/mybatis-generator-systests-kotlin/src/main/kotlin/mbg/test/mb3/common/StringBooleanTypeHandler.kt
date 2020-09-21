/*
 *    Copyright 2006-2020 the original author or authors.
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
package mbg.test.mb3.common

import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.TypeHandler

class StringBooleanTypeHandler : TypeHandler<Boolean> {

    override fun getResult(cs: CallableStatement, columnIndex: Int): Boolean? {
        val s = cs.getString(columnIndex)
        return "Y" == s
    }

    override fun getResult(rs: ResultSet, columnName: String): Boolean? {
        val s = rs.getString(columnName)
        return "Y" == s
    }

    override fun setParameter(ps: PreparedStatement, columnIndex: Int, parameter: Boolean?,
                              jdbcType: JdbcType) {
        val s = if (parameter == null) "N" else if (parameter) "Y" else "N"
        ps.setString(columnIndex, s)
    }

    override fun getResult(rs: ResultSet, columnIndex: Int): Boolean? {
        val s = rs.getString(columnIndex)
        return "Y" == s
    }
}
