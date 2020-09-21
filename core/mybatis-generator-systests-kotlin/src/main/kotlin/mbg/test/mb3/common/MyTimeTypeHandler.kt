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
import java.sql.Time
import java.util.Calendar

import mbg.test.common.MyTime

import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.TypeHandler

/**
 * @author Jeff Butler
 */
/**
 *
 */
class MyTimeTypeHandler : TypeHandler<MyTime> {

    override fun getResult(cs: CallableStatement, columnIndex: Int): MyTime? {
        val time = cs.getTime(columnIndex)
        return time?.let { makeTime(it) }
    }

    override fun getResult(rs: ResultSet, columnName: String): MyTime? {
        val time = rs.getTime(columnName)
        return time?.let { makeTime(it) }
    }

    override fun getResult(rs: ResultSet, columnIndex: Int): MyTime? {
        val time = rs.getTime(columnIndex)
        return time?.let { makeTime(it) }
    }

    private fun makeTime(time: Time): MyTime {
        val answer = MyTime()

        val c = Calendar.getInstance()
        c.time = time

        answer.hours = c.get(Calendar.HOUR_OF_DAY)
        answer.minutes = c.get(Calendar.MINUTE)
        answer.seconds = c.get(Calendar.SECOND)
        return answer
    }

    override fun setParameter(ps: PreparedStatement, i: Int, parameter: MyTime?,
                              jdbcType: JdbcType) {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE)
        } else {
            val c = Calendar.getInstance()
            c.set(Calendar.HOUR_OF_DAY, parameter.hours)
            c.set(Calendar.MINUTE, parameter.minutes)
            c.set(Calendar.SECOND, parameter.seconds)

            val time = Time(c.time.time)

            ps.setTime(i, time)
        }
    }
}
