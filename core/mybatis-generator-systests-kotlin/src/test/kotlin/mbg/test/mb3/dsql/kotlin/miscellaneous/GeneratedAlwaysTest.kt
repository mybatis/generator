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

import mbg.test.common.util.TestUtilities.blobsAreEqual
import mbg.test.common.util.TestUtilities.generateRandomBlob
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.mapper.*
import org.junit.jupiter.api.Test

import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.model.Generatedalwaystest
import mbg.test.mb3.generated.dsql.kotlin.miscellaneous.mapper.GeneratedalwaystestDynamicSqlSupport.generatedalwaystest
import org.junit.jupiter.api.Assertions.*

class GeneratedAlwaysTest : AbstractAnnotatedMiscellaneousTest() {

    @Test
    fun testInsert() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55
            gaTest.idPlus2 = 66
            gaTest.blob1 = generateRandomBlob()
            val rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("fred", returnedRecord.name)
            assertTrue(blobsAreEqual(gaTest.blob1, returnedRecord.blob1))
        }
    }

    @Test
    fun testInsertSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55
            gaTest.idPlus2 = 66
            val rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("fred", returnedRecord.name)
            assertNull(returnedRecord.blob1)
        }
    }

    @Test
    fun testUpdateByExample() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55 // should be ignored
            gaTest.idPlus2 = 66 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            var rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            gaTest.name = "barney"
            gaTest.idPlus1 = 77 // should be ignored
            gaTest.idPlus2 = 88 // should be ignored
            gaTest.blob1 = generateRandomBlob()

            rows = mapper.update {
                updateAllColumns(gaTest)
                where { generatedalwaystest.idPlus1 isEqualTo 2 }
                and { generatedalwaystest.idPlus2 isEqualTo 3 }
            }
            assertEquals(1, rows)

            val returnedRecords = mapper.select {
                where { generatedalwaystest.idPlus1 isEqualTo 2 }
                and { generatedalwaystest.idPlus2 isEqualTo 3 }
            }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("barney", returnedRecord.name)
            // should not have update the BLOB in regular update by primary key
            assertTrue(blobsAreEqual(gaTest.blob1, returnedRecord.blob1))
        }
    }

    @Test
    fun testUpdateByExampleSelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55 // should be ignored
            gaTest.idPlus2 = 66 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            var rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            gaTest.name = null
            gaTest.idPlus1 = 77 // should be ignored
            gaTest.idPlus2 = 88 // should be ignored
            gaTest.blob1 = generateRandomBlob()

            rows = mapper.update {
                updateSelectiveColumns(gaTest)
                where { generatedalwaystest.idPlus1 isEqualTo 2 }
                and { generatedalwaystest.idPlus2  isEqualTo 3 }
            }
            assertEquals(1, rows)

            val returnedRecords = mapper.select {
                where { generatedalwaystest.idPlus1 isEqualTo 2 }
                and { generatedalwaystest.idPlus2  isEqualTo 3 }
            }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("fred", returnedRecord.name)
            assertTrue(blobsAreEqual(gaTest.blob1, returnedRecord.blob1))
        }
    }

    @Test
    fun testUpdateByExampleWithBlobs() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55 // should be ignored
            gaTest.idPlus2 = 66 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            var rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            gaTest.name = "barney"
            gaTest.idPlus1 = 77 // should be ignored
            gaTest.idPlus2 = 88 // should be ignored
            gaTest.blob1 = generateRandomBlob()

            rows = mapper.update {
                updateAllColumns(gaTest)
                where { generatedalwaystest.idPlus1 isEqualTo 2 }
                and { generatedalwaystest.idPlus2 isEqualTo 3 }
            }
            assertEquals(1, rows)

            val returnedRecords = mapper.select {
                where { generatedalwaystest.idPlus1 isEqualTo 2 }
                and { generatedalwaystest.idPlus2 isEqualTo 3 }
            }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("barney", returnedRecord.name)
            assertTrue(blobsAreEqual(gaTest.blob1, returnedRecord.blob1))
        }
    }

    @Test
    fun testUpdateByPrimaryKey() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55 // should be ignored
            gaTest.idPlus2 = 66 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            var rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            gaTest.name = "barney"
            gaTest.idPlus1 = 77 // should be ignored
            gaTest.idPlus2 = 88 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            rows = mapper.updateByPrimaryKey(gaTest)
            assertEquals(1, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("barney", returnedRecord.name)
            assertTrue(blobsAreEqual(gaTest.blob1, returnedRecord.blob1))
        }
    }

    @Test
    fun testUpdateByPrimaryKeySelective() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55 // should be ignored
            gaTest.idPlus2 = 66 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            var rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            gaTest.name = null
            gaTest.idPlus1 = 77 // should be ignored
            gaTest.idPlus2 = 88 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            rows = mapper.updateByPrimaryKeySelective(gaTest)
            assertEquals(1, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("fred", returnedRecord.name)
            assertTrue(blobsAreEqual(gaTest.blob1, returnedRecord.blob1))
        }
    }

    @Test
    fun testUpdateByPrimaryKeyWithBlobs() {
        openSession().use { sqlSession ->
            val mapper = sqlSession.getMapper(GeneratedalwaystestMapper::class.java)

            val gaTest = Generatedalwaystest()
            gaTest.id = 1
            gaTest.name = "fred"
            gaTest.idPlus1 = 55 // should be ignored
            gaTest.idPlus2 = 66 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            var rows = mapper.insert(gaTest)
            assertEquals(1, rows)

            gaTest.name = "barney"
            gaTest.idPlus1 = 77 // should be ignored
            gaTest.idPlus2 = 88 // should be ignored
            gaTest.blob1 = generateRandomBlob()
            rows = mapper.updateByPrimaryKey(gaTest)
            assertEquals(1, rows)

            val returnedRecords = mapper.select { allRows() }
            assertEquals(1, returnedRecords.size)

            val returnedRecord = returnedRecords[0]
            assertEquals(1, returnedRecord.id)
            assertEquals(2, returnedRecord.idPlus1)
            assertEquals(3, returnedRecord.idPlus2)
            assertEquals("barney", returnedRecord.name)
            assertTrue(blobsAreEqual(gaTest.blob1, returnedRecord.blob1))
        }
    }
}
