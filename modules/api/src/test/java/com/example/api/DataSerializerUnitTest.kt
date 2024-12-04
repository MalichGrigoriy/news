package com.example.api

import com.example.api.util.DateSerializer
import kotlinx.serialization.json.Json
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DataSerializerUnitTest {

    private val testDateString = "2024-11-24T16:30:00Z"
    private val expectedDate: Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").apply {
        timeZone = TimeZone.getDefault()
    }.parse(testDateString)

    @Test
    fun `test deserialization of ISO date string to Date object`() {
        val result = Json.decodeFromString(DateSerializer, "\"$testDateString\"")
        assertEquals(expectedDate.time, result.time)
    }

    @Test
    fun `test serialization of Date object to ISO date string`() {
        val result = Json.encodeToString(DateSerializer, expectedDate)
        assertEquals("\"$testDateString\"", result)
    }
}
