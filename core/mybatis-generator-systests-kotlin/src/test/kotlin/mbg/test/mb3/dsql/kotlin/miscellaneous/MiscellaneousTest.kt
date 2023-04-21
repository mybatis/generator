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

import mbg.test.common.FirstName
import mbg.test.common.MyTime
import mbg.test.common.util.TestUtilities.datesAreEqual
import mbg.test.mb3.common.TestEnum
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.mapper.MyObjectMapper
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.mapper.*
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.mapper.MyObjectDynamicSqlSupport.myObject
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.model.Enumordinaltest
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.model.Enumtest
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.model.MyObject
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.model.Regexrename
import org.apache.ibatis.session.RowBounds
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import java.util.Date

/**
 * @author Jeff Butler
 */
class MiscellaneousTest : AbstractAnnotatedMiscellaneousTest() {

    @Test
    fun testMyObjectInsertMyObject() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            val record = MyObject()
            record.startDate = Date()
            record.decimal100field = 10L
            record.decimal155field = 15.12345
            record.decimal60field = 6
            val fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.id1 = 1
            record.id2 = 2
            record.lastname = "Butler"

            val myTime = MyTime()
            myTime.hours = 12
            myTime.minutes = 34
            myTime.seconds = 5
            record.timefield = myTime
            record.timestampfield = Date()

            mapper.insert(record)

            val returnedRecord = mapper.selectByPrimaryKey(2, 1)

            assertThat(returnedRecord).isNotNull
            if (returnedRecord != null) {
                assertTrue(datesAreEqual(record.startDate, returnedRecord.startDate))
                assertEquals(record.decimal100field!!, returnedRecord.decimal100field)
                assertEquals(record.decimal155field!!, returnedRecord.decimal155field)
                assertEquals(record.decimal60field!!, returnedRecord.decimal60field)
                assertEquals(record.firstname, returnedRecord.firstname)
                assertEquals(record.id1!!, returnedRecord.id1)
                assertEquals(record.id2!!, returnedRecord.id2)
                assertEquals(record.lastname, returnedRecord.lastname)
                assertEquals(record.timefield, returnedRecord.timefield)
                assertEquals(record.timestampfield, returnedRecord.timestampfield)
            }
        }
    }

    @Test
    fun testMyObjectUpdateByPrimaryKey() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            val record = MyObject()
            var fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2

            mapper.insert(record)

            fn = FirstName()
            fn.value = "Scott"
            record.firstname = fn
            record.lastname = "Jones"

            val rows = mapper.updateByPrimaryKey(record)
            assertThat(rows).isEqualTo(1)

            val record2 = mapper.selectByPrimaryKey(2, 1)

            assertThat(record2).isNotNull
            if (record2 != null) {
                assertEquals(record.firstname, record2.firstname)
                assertEquals(record.lastname, record2.lastname)
                assertEquals(record.id1!!, record2.id1)
                assertEquals(record.id2!!, record2.id2)
            }
        }
    }

    @Test
    fun testMyObjectUpdateByPrimaryKeySelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            val record = MyObject()
            var fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.lastname = "Smith"
            record.decimal60field = 5
            record.id1 = 1
            record.id2 = 2

            mapper.insert(record)

            val newRecord = MyObject()
            newRecord.id1 = 1
            newRecord.id2 = 2
            fn = FirstName()
            fn.value = "Scott"
            newRecord.firstname = fn
            record.startDate = Date()

            val rows = mapper.updateByPrimaryKeySelective(newRecord)
            assertEquals(1, rows)

            val returnedRecord = mapper.selectByPrimaryKey(2, 1)

            assertThat(returnedRecord).isNotNull
            if (returnedRecord != null) {
                assertTrue(datesAreEqual(newRecord.startDate, returnedRecord.startDate))
                assertEquals(record.decimal100field, returnedRecord.decimal100field)
                assertEquals(record.decimal155field, returnedRecord.decimal155field)
                assertEquals(record.decimal60field!!, returnedRecord.decimal60field)
                assertEquals(newRecord.firstname, returnedRecord.firstname)
                assertEquals(record.id1!!, returnedRecord.id1)
                assertEquals(record.id2!!, returnedRecord.id2)
                assertEquals(record.lastname, returnedRecord.lastname)
                assertEquals(record.timefield, returnedRecord.timefield)
                assertEquals(record.timestampfield, returnedRecord.timestampfield)
            }
        }
    }

    @Test
    fun testMyObjectDeleteByPrimaryKey() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            val record = MyObject()
            val fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2

            mapper.insert(record)

            val rows = mapper.deleteByPrimaryKey(2, 1)
            assertEquals(1, rows)

            val answer = mapper.select { allRows() }
            assertEquals(0, answer.size)
        }
    }

    @Test
    fun testMyObjectDeleteByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bob"
            record.firstname = fn
            record.lastname = "Jones"
            record.id1 = 3
            record.id2 = 4

            mapper.insert(record)

            var answer = mapper.select { allRows() }
            assertEquals(2, answer.size)

            val rows = mapper.delete { where { myObject.lastname isLike "J%" } }

            assertEquals(1, rows)

            answer = mapper.select { allRows() }
            assertEquals(1, answer.size)
        }
    }

    @Test
    fun testMyObjectSelectByPrimaryKey() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            val record = MyObject()
            var fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            val record1 = MyObject()
            fn = FirstName()
            fn.value = "Bob"
            record1.firstname = fn
            record1.lastname = "Jones"
            record1.id1 = 3
            record1.id2 = 4
            mapper.insert(record1)

            val newRecord = mapper.selectByPrimaryKey(4, 3)

            assertThat(newRecord).isNotNull
            if (newRecord != null) {
                assertEquals(record1.firstname, newRecord.firstname)
                assertEquals(record1.lastname, newRecord.lastname)
                assertEquals(record1.id1!!, newRecord.id1)
                assertEquals(record1.id2!!, newRecord.id2)
            }
        }
    }

    @Test
    fun testMyObjectSelectByExampleLike() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Fred"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Wilma"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Pebbles"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 3
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Barney"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Betty"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bamm Bamm"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 3
            mapper.insert(record)

            val fn1 = FirstName()
            fn1.value = "B%"

            val answer = mapper.select {
                where { myObject.firstname isLike fn1 }
                orderBy(myObject.id1, myObject.id2)
            }

            assertEquals(3, answer.size)
            var returnedRecord = answer[0]
            assertEquals(2, returnedRecord.id1)
            assertEquals(1, returnedRecord.id2)
            returnedRecord = answer[1]
            assertEquals(2, returnedRecord.id1)
            assertEquals(2, returnedRecord.id2)
            returnedRecord = answer[2]
            assertEquals(2, returnedRecord.id1)
            assertEquals(3, returnedRecord.id2)
        }
    }

    @Test
    fun testMyObjectSelectByExampleNotLike() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Fred"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Wilma"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Pebbles"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 3
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Barney"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Betty"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bamm Bamm"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 3
            mapper.insert(record)

            val fn1 = FirstName()
            fn1.value = "B%"

            val answer = mapper.select {
                where { myObject.firstname isNotLike fn1 }
                orderBy(myObject.id1, myObject.id2)
            }

            assertEquals(3, answer.size)
            var returnedRecord = answer[0]
            assertEquals(1, returnedRecord.id1)
            assertEquals(1, returnedRecord.id2)
            returnedRecord = answer[1]
            assertEquals(1, returnedRecord.id1)
            assertEquals(2, returnedRecord.id2)
            returnedRecord = answer[2]
            assertEquals(1, returnedRecord.id1)
            assertEquals(3, returnedRecord.id2)
        }
    }

    @Test
    fun testMyObjectSelectByExampleComplexLike() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)

            var fn = FirstName()
            fn.value = "Fred"
            mapper.insert(MyObject(id1 = 1, id2 = 1, firstname = fn, lastname = "Flintstone"))

            fn = FirstName()
            fn.value = "Wilma"
            mapper.insert(MyObject(id1 = 1, id2 = 2, firstname = fn, lastname = "Flintstone"))

            fn = FirstName()
            fn.value = "Pebbles"
            mapper.insert(MyObject(id1 = 1, id2 = 3, firstname = fn, lastname = "Flintstone"))

            fn = FirstName()
            fn.value = "Barney"
            mapper.insert(MyObject(id1 = 2, id2 = 1, firstname = fn, lastname = "Rubble"))

            fn = FirstName()
            fn.value = "Betty"
            mapper.insert(MyObject(id1 = 2, id2 = 2, firstname = fn, lastname = "Rubble"))

            fn = FirstName()
            fn.value = "Bamm Bamm"
            mapper.insert(MyObject(id1 = 2, id2 = 3, firstname = fn, lastname = "Rubble"))

            val fn1 = FirstName()
            fn1.value = "B%"
            val fn2 = FirstName()
            fn2.value = "W%"

            val answer = mapper.select {
                where {
                    myObject.firstname isLike fn1
                    and { myObject.id2 isEqualTo 3 }
                }
                or { myObject.firstname isLike fn2 }
                orderBy(myObject.id1, myObject.id2)
            }

            assertEquals(2, answer.size)
            var returnedRecord = answer[0]
            assertEquals(1, returnedRecord.id1)
            assertEquals(2, returnedRecord.id2)
            returnedRecord = answer[1]
            assertEquals(2, returnedRecord.id1)
            assertEquals(3, returnedRecord.id2)
        }
    }

    @Test
    fun testMyObjectSelectByExampleIn() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Fred"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Wilma"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Pebbles"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 3
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Barney"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Betty"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bamm Bamm"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 3
            mapper.insert(record)

            val ids = listOf(1, 3)

            val answer = mapper.select {
                where { myObject.id2 isIn ids }
                orderBy(myObject.id1, myObject.id2)
            }
            assertEquals(4, answer.size)
            var returnedRecord = answer[0]
            assertEquals(1, returnedRecord.id1)
            assertEquals(1, returnedRecord.id2)
            assertEquals("Flintstone", returnedRecord.lastname)

            returnedRecord = answer[1]
            assertEquals(1, returnedRecord.id1)
            assertEquals(3, returnedRecord.id2)
            assertEquals("Flintstone", returnedRecord.lastname)

            returnedRecord = answer[2]
            assertEquals(2, returnedRecord.id1)
            assertEquals(1, returnedRecord.id2)
            assertEquals("Rubble", returnedRecord.lastname)

            returnedRecord = answer[3]
            assertEquals(2, returnedRecord.id1)
            assertEquals(3, returnedRecord.id2)
            assertEquals("Rubble", returnedRecord.lastname)
        }
    }

    @Test
    fun testMyObjectSelectByExampleBetween() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Fred"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Wilma"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Pebbles"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 3
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Barney"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Betty"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bamm Bamm"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 3
            mapper.insert(record)

            val answer = mapper.select {
                where { myObject.id2 isBetween 1 and 3 }
                orderBy(myObject.id1, myObject.id2)
            }
            assertEquals(6, answer.size)
        }
    }

    @Test
    fun testMyObjectSelectByExampleTimeEquals() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            val record = MyObject()
            record.startDate = Date()
            record.decimal100field = 10L
            record.decimal155field = 15.12345
            record.decimal60field = 6
            val fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.id1 = 1
            record.id2 = 2
            record.lastname = "Butler"

            val myTime = MyTime()
            myTime.hours = 12
            myTime.minutes = 34
            myTime.seconds = 5
            record.timefield = myTime
            record.timestampfield = Date()

            mapper.insert(record)

            val results = mapper.select { where { myObject.timefield isEqualTo myTime } }
            assertEquals(1, results.size)

            val returnedRecord = results[0]
            assertTrue(datesAreEqual(record.startDate, returnedRecord.startDate))
            assertEquals(record.decimal100field!!, returnedRecord.decimal100field)
            assertEquals(record.decimal155field!!, returnedRecord.decimal155field)
            assertEquals(record.decimal60field!!, returnedRecord.decimal60field)
            assertEquals(record.firstname, returnedRecord.firstname)
            assertEquals(record.id1!!, returnedRecord.id1)
            assertEquals(record.id2!!, returnedRecord.id2)
            assertEquals(record.lastname, returnedRecord.lastname)
            assertEquals(record.timefield, returnedRecord.timefield)
            assertEquals(record.timestampfield, returnedRecord.timestampfield)
        }
    }

    @Test
    fun testFieldIgnored() {
        assertThrows(NoSuchFieldException::class.java) { myObject::class.java.getDeclaredField("decimal30field") }
    }

    @Test
    fun testMyObjectUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bob"
            record.firstname = fn
            record.lastname = "Jones"
            record.id1 = 3
            record.id2 = 4

            mapper.insert(record)

            val newRecord = MyObject()
            newRecord.lastname = "Barker"

            val fn1 = FirstName()
            fn1.value = "B%"

            val rows = mapper.update {
                updateSelectiveColumns(newRecord)
                where { myObject.firstname isLike fn1 }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { myObject.firstname isLike fn1 } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]
            assertEquals(newRecord.lastname, returnedRecord.lastname)
            assertEquals(record.firstname, returnedRecord.firstname)
            assertEquals(record.id1!!, returnedRecord.id1)
            assertEquals(record.id2!!, returnedRecord.id2)
        }
    }

    @Test
    fun testMyObjectUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Jeff"
            record.firstname = fn
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bob"
            record.firstname = fn
            record.lastname = "Jones"
            record.id1 = 3
            record.id2 = 4

            mapper.insert(record)

            val newRecord = MyObject()
            newRecord.lastname = "Barker"
            newRecord.id1 = 3
            newRecord.id2 = 4

            val rows = mapper.update {
                updateAllColumns(newRecord)
                where {
                    myObject.id1 isEqualTo 3
                    and { myObject.id2 isEqualTo 4 }
                }
            }
            assertEquals(1, rows)

            val answer = mapper.select {
                where {
                    myObject.id1 isEqualTo 3
                    and { myObject.id2 isEqualTo 4 }
                }
            }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(newRecord.lastname, returnedRecord.lastname)
            assertNull(returnedRecord.firstname)
            assertEquals(newRecord.id1!!, returnedRecord.id1)
            assertEquals(newRecord.id2!!, returnedRecord.id2)
        }
    }

    @Test
    fun testThatMultiRowInsertMethodsAreNotGenerated() {
        // regex rename has a generated key, but it is not JDBC. So it should be
        // ignored by the generator
        assertThrows(NoSuchMethodException::class.java) {
            RegexrenameMapper::class.java.getMethod("insertMultiple", Collection::class.java)
        }

        assertThrows(NoSuchMethodException::class.java) {
            RegexrenameMapper::class.java.getMethod("insertMultiple", MultiRowInsertStatementProvider::class.java)
        }

        assertThrows(NoSuchMethodException::class.java) {
            RegexrenameMapper::class.java.getMethod("insertMultiple", String::class.java, List::class.java)
        }
    }

    @Test
    fun testThatRowBoundsMethodsAreNotGenerated() {
        // regex rename has the rowbounds plugin, but that plugin is disabled for MyBatisDynamicSQLV2
        assertThrows(NoSuchMethodException::class.java) {
            RegexrenameMapper::class.java.getMethod("selectManyWithRowbounds", SelectStatementProvider::class.java, RowBounds::class.java)
        }

        assertThrows(NoSuchMethodException::class.java) {
            RegexrenameMapper::class.java.getMethod("selectManyWithRowbounds", RowBounds::class.java)
        }

        assertThrows(NoSuchMethodException::class.java) {
            RegexrenameMapper::class.java.getMethod("selectByExample", RowBounds::class.java)
        }

        assertThrows(NoSuchMethodException::class.java) {
            RegexrenameMapper::class.java.getMethod("selectDistinctByExample", RowBounds::class.java)
        }
    }

    @Test
    fun testRegexRenameInsert() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(RegexrenameMapper::class.java)
            val record = Regexrename()
            record.address = "123 Main Street"
            record.name = "Fred"
            record.zipCode = "99999"

            mapper.insert(record)
            // test generated id
            assertEquals(1, record.id)

            val returnedRecord = mapper.selectByPrimaryKey(1)

            assertThat(returnedRecord).isNotNull
            if(returnedRecord != null) {
                assertEquals(record.address, returnedRecord.address)
                assertEquals(1, returnedRecord.id)
                assertEquals(record.name, returnedRecord.name)
                assertEquals(record.zipCode, returnedRecord.zipCode)
            }
        }
    }

    @Test
    fun testRegexRenameInsertSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(RegexrenameMapper::class.java)
            val record = Regexrename()
            record.zipCode = "99999"

            mapper.insertSelective(record)
            assertEquals(1, record.id)

            val returnedRecord = mapper.selectByPrimaryKey(1)

            assertThat(returnedRecord).isNotNull
            if(returnedRecord != null) {
                assertNull(returnedRecord.address)
                assertEquals(record.id, returnedRecord.id)
                assertNull(returnedRecord.name)
                assertEquals(record.zipCode, returnedRecord.zipCode)
            }
        }
    }

    @Test
    fun testMyObjectSelectByExampleLikeInsensitive() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(MyObjectMapper::class.java)
            var record = MyObject()
            var fn = FirstName()
            fn.value = "Fred"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Wilma"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Pebbles"
            record.firstname = fn
            record.lastname = "Flintstone"
            record.id1 = 1
            record.id2 = 3
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Barney"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 1
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Betty"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 2
            mapper.insert(record)

            record = MyObject()
            fn = FirstName()
            fn.value = "Bamm Bamm"
            record.firstname = fn
            record.lastname = "Rubble"
            record.id1 = 2
            record.id2 = 3
            mapper.insert(record)

            var answer = mapper.select {
                where { myObject.lastname isLike "RU%" }
                orderBy(myObject.id1, myObject.id2)
            }
            assertEquals(0, answer.size)

            answer = mapper.select { where { myObject.lastname isLikeCaseInsensitive "RU%" } }
            assertEquals(3, answer.size)

            var returnedRecord = answer[0]
            assertEquals(2, returnedRecord.id1)
            assertEquals(1, returnedRecord.id2)
            returnedRecord = answer[1]
            assertEquals(2, returnedRecord.id1)
            assertEquals(2, returnedRecord.id2)
            returnedRecord = answer[2]
            assertEquals(2, returnedRecord.id1)
            assertEquals(3, returnedRecord.id2)
        }
    }

    @Test
    fun testEnum() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(EnumtestMapper::class.java)

            val enumTest = Enumtest()
            enumTest.id = 1
            enumTest.name = TestEnum.FRED
            val rows = mapper.insert(enumTest)
            assertEquals(1, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(TestEnum.FRED, returnedRecord.name)
        }
    }

    @Test
    fun testEnumInsertMultiple() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(EnumtestMapper::class.java)
            val records = listOf(
                Enumtest().apply {
                    id = 1
                    name = TestEnum.FRED
                },
                Enumtest().apply {
                    id = 2
                    name = TestEnum.BARNEY
                }
            )

            val rows = mapper.insertMultiple(records)
            assertEquals(2, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(2, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(TestEnum.FRED, returnedRecord.name)
        }
    }

    @Test
    fun testEnumOrdinal() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(EnumordinaltestMapper::class.java)


            val enumTest = Enumordinaltest()
            enumTest.id = 1
            enumTest.name = TestEnum.FRED

            val rows = mapper.insert(enumTest)
            assertEquals(1, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(TestEnum.FRED, returnedRecord.name)
        }
    }

    @Test
    fun testEnumOrdinalInsertMultiple() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(EnumordinaltestMapper::class.java)
            val records = listOf(
                Enumordinaltest().apply {
                    id = 1
                    name = TestEnum.FRED
                },
                Enumordinaltest().apply {
                    id = 2
                    name = TestEnum.BARNEY
                }
            )

            val rows = mapper.insertMultiple(records)
            assertEquals(2, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(2, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(TestEnum.FRED, returnedRecord.name)
        }
    }
}
