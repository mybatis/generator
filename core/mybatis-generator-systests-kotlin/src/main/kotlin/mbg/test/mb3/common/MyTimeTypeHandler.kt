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

import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
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

    override fun getResult(cs: CallableStatement, columnIndex: Int): MyTime? =
        cs.getTime(columnIndex)?.toMyTime()

    override fun getResult(rs: ResultSet, columnName: String): MyTime? =
        rs.getTime(columnName)?.toMyTime()

    override fun getResult(rs: ResultSet, columnIndex: Int): MyTime? =
        rs.getTime(columnIndex)?.toMyTime()

    private fun Time.toMyTime(): MyTime =
        MyTime().also {
            val c = Calendar.getInstance()
            c.time = this

            it.hours = c.get(Calendar.HOUR_OF_DAY)
            it.minutes = c.get(Calendar.MINUTE)
            it.seconds = c.get(Calendar.SECOND)
        }

    override fun setParameter(ps: PreparedStatement, i: Int, parameter: MyTime?,
                              jdbcType: JdbcType) {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE)
        } else {
            with(Calendar.getInstance()) {
                set(Calendar.HOUR_OF_DAY, parameter.hours)
                set(Calendar.MINUTE, parameter.minutes)
                set(Calendar.SECOND, parameter.seconds)
                ps.setTime(i, Time(timeInMillis))
            }
        }
    }
}
