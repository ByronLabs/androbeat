package com.androbeat.androbeatagent.data.model.models

import com.androbeat.androbeatagent.data.model.models.Result
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultTest {

    @Test
    fun successShouldStoreDataCorrectly() {
        val data: String = "Test Data"
        val result = Result.Success(data)
        assertEquals(data, result.data)
    }

    @Test
    fun errorShouldStoreExceptionCorrectly() {
        val exception: Exception = mockk()
        val result = Result.Error(exception)
        assertEquals(exception, result.exception)
    }

    @Test
    fun toStringReturnsSuccessWithData() {

        val mockData = mockk<Any>()
        every { mockData.toString() } returns "MockData"
        val successResult = Result.Success(mockData)
        val resultString = successResult.toString()
        assertEquals("Success(data=MockData)", resultString)
    }

    @Test
    fun toStringReturnsErrorWithException() {

        val mockException = mockk<Exception>()
        every { mockException.toString() } returns "MockException"
        val errorResult = Result.Error(mockException)
        val resultString = errorResult.toString()
        assertEquals("Error(exception=MockException)", resultString)
    }

}