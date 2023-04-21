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
package mbg.test.mb3.dsql.kotlin

import mbg.test.common.util.TestUtilities.blobsAreEqual
import mbg.test.common.util.TestUtilities.generateRandomBlob
import mbg.test.mb3.generated.dsql.kotlin.mapper.*
import mbg.test.mb3.generated.dsql.kotlin.mapper.AwfulTableDynamicSqlSupport.awfulTable
import mbg.test.mb3.generated.dsql.kotlin.mapper.FieldsblobsDynamicSqlSupport.fieldsblobs
import mbg.test.mb3.generated.dsql.kotlin.mapper.FieldsonlyDynamicSqlSupport.fieldsonly
import mbg.test.mb3.generated.dsql.kotlin.mapper.PkblobsDynamicSqlSupport.pkblobs
import mbg.test.mb3.generated.dsql.kotlin.mapper.PkfieldsDynamicSqlSupport.pkfieldstable
import mbg.test.mb3.generated.dsql.kotlin.mapper.PkfieldsblobsDynamicSqlSupport.pkfieldsblobs
import mbg.test.mb3.generated.dsql.kotlin.mapper.PkonlyDynamicSqlSupport.pkonly
import mbg.test.mb3.generated.dsql.kotlin.model.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 *
 * @author Jeff Butler
 */
class UpdateByExampleTest : AbstractTest() {

    @Test
    fun testFieldsOnlyUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val offset = Offset.offset(0.001)
            val mapper = sqlSession.getMapper(FieldsonlyMapper::class.java)
            mapper.insert(Fieldsonly(5, 11.22, 33.44))
            mapper.insert(Fieldsonly(8, 44.55, 66.77))
            mapper.insert(Fieldsonly(9, 88.88, 100.11))

            val rows = mapper.update {
                updateSelectiveColumns(Fieldsonly(doublefield = 99.0))
                where { fieldsonly.integerfield isGreaterThan 5 }
            }

            assertThat(rows).isEqualTo(2)

            var answer = mapper.select {
                where { fieldsonly.integerfield isEqualTo 5 }
            }

            assertThat(answer).hasSize(1)
            with(answer[0]) {
                assertThat(doublefield).isEqualTo(11.22, offset)
                assertThat(floatfield).isEqualTo(33.44, offset)
                assertThat(integerfield).isEqualTo(5)
            }

            answer = mapper.select {
                where { fieldsonly.integerfield isEqualTo 8 }
            }

            assertThat(answer).hasSize(1)
            with(answer[0]) {
                assertThat(doublefield).isEqualTo(99.0, offset)
                assertThat(floatfield).isEqualTo(66.77, offset)
                assertThat(integerfield).isEqualTo(8)

            }

            answer = mapper.select {
                where { fieldsonly.integerfield isEqualTo 9 }
            }
            assertThat(answer).hasSize(1)
            with(answer[0]) {
                assertThat(doublefield).isEqualTo(99.0, offset)
                assertThat(floatfield).isEqualTo(100.11, offset)
                assertThat(integerfield).isEqualTo(9)
            }
        }
    }

    @Test
    fun testFieldsOnlyUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(FieldsonlyMapper::class.java)
            var record = Fieldsonly()
            record.doublefield = 11.22
            record.floatfield = 33.44
            record.integerfield = 5
            mapper.insert(record)

            record = Fieldsonly()
            record.doublefield = 44.55
            record.floatfield = 66.77
            record.integerfield = 8
            mapper.insert(record)

            record = Fieldsonly()
            record.doublefield = 88.99
            record.floatfield = 100.111
            record.integerfield = 9
            mapper.insert(record)

            val updateRecord = Fieldsonly(integerfield = 22)

            val rows = mapper.update {
                updateAllColumns(updateRecord)
                where { fieldsonly.integerfield isEqualTo 5 }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { fieldsonly.integerfield isEqualTo 22 } }
            assertEquals(1, answer.size)
            record = answer[0]
            assertNull(record.doublefield)
            assertNull(record.floatfield)
            assertEquals(record.integerfield!!, 22)
        }
    }

    @Test
    fun testPKOnlyUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkonlyMapper::class.java)
            var key = Pkonly(1, 3)
            mapper.insert(key)

            key = Pkonly(5, 6)
            mapper.insert(key)

            key = Pkonly(7, 8)
            mapper.insert(key)

            val updateKey = Pkonly(seqNum = 3)

            val rows = mapper.update {
                updateSelectiveColumns(updateKey)
                where { pkonly.id isGreaterThan 4 }
            }
            assertEquals(2, rows)

            var returnedRows = mapper.count {
                where { pkonly.id isEqualTo 5 }
                and { pkonly.seqNum isEqualTo 3 }
            }
            assertEquals(1, returnedRows)

            returnedRows = mapper.count {
                where { pkonly.id isEqualTo 7 }
                and { pkonly.seqNum isEqualTo 3 }
            }
            assertEquals(1, returnedRows)
        }
    }

    @Test
    fun testPKOnlyUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkonlyMapper::class.java)
            var key = Pkonly(1, 3)
            mapper.insert(key)

            key = Pkonly(5, 6)
            mapper.insert(key)

            key = Pkonly(7, 8)
            mapper.insert(key)

            val updateKey = Pkonly(22, 3)

            val rows = mapper.update {
                updateAllColumns(updateKey)
                where { pkonly.id isEqualTo 7 }
            }
            assertEquals(1, rows)

            val returnedRows = mapper.count {
                where { pkonly.id isEqualTo 22 }
                and { pkonly.seqNum isEqualTo 3 }
            }
            assertEquals(1, returnedRows)
        }
    }

    @Test
    fun testPKFieldsUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkfieldsMapper::class.java)
            var record = Pkfields()
            record.firstname = "Jeff"
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = Pkfields()
            record.firstname = "Bob"
            record.lastname = "Jones"
            record.id1 = 3
            record.id2 = 4

            mapper.insert(record)

            val updateRecord = Pkfields(firstname = "Fred")

            val rows = mapper.update {
                updateSelectiveColumns(updateRecord)
                where { pkfieldstable.lastname isLike "J%" }
            }
            assertEquals(1, rows)

            val returnedRows = mapper.count {
                where { pkfieldstable.firstname isEqualTo "Fred" }
                and { pkfieldstable.lastname isEqualTo "Jones" }
                and { pkfieldstable.id1 isEqualTo 3 }
                and { pkfieldstable.id2 isEqualTo 4 }
            }
            assertEquals(1, returnedRows)
        }
    }

    @Test
    fun testPKFieldsUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkfieldsMapper::class.java)
            var record = Pkfields()
            record.firstname = "Jeff"
            record.lastname = "Smith"
            record.id1 = 1
            record.id2 = 2
            mapper.insert(record)

            record = Pkfields()
            record.firstname = "Bob"
            record.lastname = "Jones"
            record.id1 = 3
            record.id2 = 4

            mapper.insert(record)

            val updateRecord = Pkfields(id1 = 3, id2 = 4, firstname = "Fred")

            val rows = mapper.update {
                updateAllColumns(updateRecord)
                where { pkfieldstable.id1 isEqualTo 3 }
                and { pkfieldstable.id2 isEqualTo 4 }
            }
            assertEquals(1, rows)

            val returnedRows = mapper.count {
                where { pkfieldstable.firstname isEqualTo "Fred" }
                and { pkfieldstable.id1 isEqualTo 3 }
                and { pkfieldstable.id2 isEqualTo 4 }
            }
            assertEquals(1, returnedRows)
        }
    }

    @Test
    fun testPKBlobsUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkblobsMapper::class.java)
            var record = Pkblobs()
            record.id = 3
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            record = Pkblobs()
            record.id = 6
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            val newRecord = Pkblobs(blob1 = generateRandomBlob())

            val rows = mapper.update {
                updateSelectiveColumns(newRecord)
                where { pkblobs.id isGreaterThan 4 }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { pkblobs.id isGreaterThan 4 } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(6, returnedRecord.id)
            assertTrue(blobsAreEqual(newRecord.blob1, returnedRecord.blob1))
            assertTrue(blobsAreEqual(record.blob2, returnedRecord.blob2))
        }
    }

    @Test
    fun testPKBlobsUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkblobsMapper::class.java)
            var record = Pkblobs()
            record.id = 3
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            record = Pkblobs()
            record.id = 6
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            val newRecord = Pkblobs(id = 8)

            val rows = mapper.update {
                updateAllColumns(newRecord)
                where { pkblobs.id isGreaterThan 4 }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { pkblobs.id isGreaterThan 4 } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(8, returnedRecord.id)
            assertNull(returnedRecord.blob1)
            assertNull(returnedRecord.blob2)
        }
    }

    @Test
    fun testPKFieldsBlobsUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkfieldsblobsMapper::class.java)
            var record = Pkfieldsblobs()
            record.id1 = 3
            record.id2 = 4
            record.firstname = "Jeff"
            record.lastname = "Smith"
            record.blob1 = generateRandomBlob()
            mapper.insert(record)

            record = Pkfieldsblobs()
            record.id1 = 5
            record.id2 = 6
            record.firstname = "Scott"
            record.lastname = "Jones"
            record.blob1 = generateRandomBlob()
            mapper.insert(record)

            val newRecord = Pkfieldsblobs(firstname = "Fred")

            val rows = mapper.update {
                updateSelectiveColumns(newRecord)
                where { pkfieldsblobs.id1 isNotEqualTo 3 }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { pkfieldsblobs.id1 isNotEqualTo 3 } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(record.id1!!, returnedRecord.id1)
            assertEquals(record.id2!!, returnedRecord.id2)
            assertEquals(newRecord.firstname, returnedRecord.firstname)
            assertEquals(record.lastname, returnedRecord.lastname)
            assertTrue(blobsAreEqual(record.blob1, returnedRecord.blob1))
        }
    }

    @Test
    fun testPKFieldsBlobsUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(PkfieldsblobsMapper::class.java)
            var record = Pkfieldsblobs()
            record.id1 = 3
            record.id2 = 4
            record.firstname = "Jeff"
            record.lastname = "Smith"
            record.blob1 = generateRandomBlob()
            mapper.insert(record)

            record = Pkfieldsblobs()
            record.id1 = 5
            record.id2 = 6
            record.firstname = "Scott"
            record.lastname = "Jones"
            record.blob1 = generateRandomBlob()
            mapper.insert(record)

            val newRecord = Pkfieldsblobs()
            newRecord.id1 = 3
            newRecord.id2 = 8
            newRecord.firstname = "Fred"

            val rows = mapper.update {
                updateAllColumns(newRecord)
                where { pkfieldsblobs.id1 isEqualTo 3 }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { pkfieldsblobs.id1 isEqualTo 3 } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(newRecord.id1!!, returnedRecord.id1)
            assertEquals(newRecord.id2!!, returnedRecord.id2)
            assertEquals(newRecord.firstname, returnedRecord.firstname)
            assertNull(returnedRecord.lastname)
            assertNull(returnedRecord.blob1)
        }
    }

    @Test
    fun testFieldsBlobsUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(FieldsblobsMapper::class.java)
            var record = Fieldsblobs()
            record.firstname = "Jeff"
            record.lastname = "Smith"
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            record = Fieldsblobs()
            record.firstname = "Scott"
            record.lastname = "Jones"
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            val newRecord = Fieldsblobs(lastname = "Doe")

            val rows = mapper.update {
                updateSelectiveColumns(newRecord)
                where { fieldsblobs.firstname isLike "S%" }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { fieldsblobs.firstname isLike "S%" } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(record.firstname, returnedRecord.firstname)
            assertEquals(newRecord.lastname, returnedRecord.lastname)
            assertTrue(blobsAreEqual(record.blob1, returnedRecord.blob1))
            assertTrue(blobsAreEqual(record.blob2, returnedRecord.blob2))
        }
    }

    @Test
    fun testFieldsBlobsUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(FieldsblobsMapper::class.java)
            var record = Fieldsblobs()
            record.firstname = "Jeff"
            record.lastname = "Smith"
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            record = Fieldsblobs()
            record.firstname = "Scott"
            record.lastname = "Jones"
            record.blob1 = generateRandomBlob()
            record.blob2 = generateRandomBlob()
            mapper.insert(record)

            val newRecord = Fieldsblobs()
            newRecord.firstname = "Scott"
            newRecord.lastname = "Doe"

            val rows = mapper.update {
                updateAllColumns(newRecord)
                where { fieldsblobs.firstname isLike "S%" }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { fieldsblobs.firstname isLike "S%" } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(newRecord.firstname, returnedRecord.firstname)
            assertEquals(newRecord.lastname, returnedRecord.lastname)
            assertNull(returnedRecord.blob1)
            assertNull(returnedRecord.blob2)
        }
    }

    @Test
    fun testAwfulTableUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(AwfulTableMapper::class.java)
            var record = AwfulTable()
            record.eMail = "fred@fred.com"
            record.emailaddress = "alsofred@fred.com"
            record.firstFirstName = "fred1"
            record.from = "from field"
            record.id1 = 1
            record.id2 = 2
            record.id5 = 5
            record.id6 = 6
            record.id7 = 7
            record.secondFirstName = "fred2"
            record.thirdFirstName = "fred3"

            mapper.insert(record)

            record = AwfulTable()
            record.eMail = "fred2@fred.com"
            record.emailaddress = "alsofred2@fred.com"
            record.firstFirstName = "fred11"
            record.from = "from from field"
            record.id1 = 11
            record.id2 = 22
            record.id5 = 55
            record.id6 = 66
            record.id7 = 77
            record.secondFirstName = "fred22"
            record.thirdFirstName = "fred33"

            mapper.insert(record)

            val newRecord = AwfulTable(firstFirstName = "Alonzo")

            val rows = mapper.update {
                updateSelectiveColumns(newRecord)
                where { awfulTable.eMail isLike "fred2@%" }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { awfulTable.eMail isLike "fred2@%" } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(record.customerId, returnedRecord.customerId)
            assertEquals(record.eMail, returnedRecord.eMail)
            assertEquals(record.emailaddress, returnedRecord.emailaddress)
            assertEquals(newRecord.firstFirstName, returnedRecord.firstFirstName)
            assertEquals(record.from, returnedRecord.from)
            assertEquals(record.id1!!, returnedRecord.id1)
            assertEquals(record.id2!!, returnedRecord.id2)
            assertEquals(record.id5!!, returnedRecord.id5)
            assertEquals(record.id6!!, returnedRecord.id6)
            assertEquals(record.id7!!, returnedRecord.id7)
            assertEquals(record.secondFirstName, returnedRecord.secondFirstName)
            assertEquals(record.thirdFirstName, returnedRecord.thirdFirstName)
        }
    }

    @Test
    fun testAwfulTableUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(AwfulTableMapper::class.java)
            var record = AwfulTable()
            record.eMail = "fred@fred.com"
            record.emailaddress = "alsofred@fred.com"
            record.firstFirstName = "fred1"
            record.from = "from field"
            record.id1 = 1
            record.id2 = 2
            record.id5 = 5
            record.id6 = 6
            record.id7 = 7
            record.secondFirstName = "fred2"
            record.thirdFirstName = "fred3"

            mapper.insert(record)

            record = AwfulTable()
            record.eMail = "fred2@fred.com"
            record.emailaddress = "alsofred2@fred.com"
            record.firstFirstName = "fred11"
            record.from = "from from field"
            record.id1 = 11
            record.id2 = 22
            record.id5 = 55
            record.id6 = 66
            record.id7 = 77
            record.secondFirstName = "fred22"
            record.thirdFirstName = "fred33"

            mapper.insert(record)

            val newRecord = AwfulTable()
            newRecord.firstFirstName = "Alonzo"
            newRecord.customerId = 58
            newRecord.id1 = 111
            newRecord.id2 = 222
            newRecord.id5 = 555
            newRecord.id6 = 666
            newRecord.id7 = 777

            val rows = mapper.update {
                updateAllColumns(newRecord)
                where { awfulTable.eMail isLike "fred2@%" }
            }
            assertEquals(1, rows)

            val answer = mapper.select { where { awfulTable.customerId isEqualTo 58 } }
            assertEquals(1, answer.size)

            val returnedRecord = answer[0]

            assertEquals(newRecord.customerId!!, returnedRecord.customerId)
            assertNull(returnedRecord.eMail)
            assertNull(returnedRecord.emailaddress)
            assertEquals(newRecord.firstFirstName, returnedRecord.firstFirstName)
            assertNull(returnedRecord.from)
            assertEquals(newRecord.id1!!, returnedRecord.id1)
            assertEquals(newRecord.id2!!, returnedRecord.id2)
            assertEquals(newRecord.id5!!, returnedRecord.id5)
            assertEquals(newRecord.id6!!, returnedRecord.id6)
            assertEquals(newRecord.id7!!, returnedRecord.id7)
            assertNull(returnedRecord.secondFirstName)
            assertNull(returnedRecord.thirdFirstName)
        }
    }
}
