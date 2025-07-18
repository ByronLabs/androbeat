package com.androbeat.androbeatagent.data.model.models

import org.junit.Assert.assertEquals
import org.junit.Test

class LoginResultTest {

    @Test
    fun successShouldStoreDataCorrectly() {
        val data: Boolean = true
        val result = LoginResult.Success(data)
        assertEquals(data, result.data)
    }

    @Test
    fun errorShouldBeSingleton() {
        val result1 = LoginResult.Error
        val result2 = LoginResult.Error
        assertEquals(result1, result2)
    }

    @Test
    fun toStringShouldReturnCorrectStringForSuccess() {
        val data: Boolean = true
        val result = LoginResult.Success(data)
        assertEquals("Success(data=true)", result.toString())
    }

    @Test
    fun toStringShouldReturnCorrectStringForError() {
        val result = LoginResult.Error
        assertEquals("Error", result.toString())
    }
}