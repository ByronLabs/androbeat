package com.androbeat.androbeatagent.data.model.models

import com.androbeat.androbeatagent.data.model.models.TokenModel
import org.junit.Assert.assertEquals
import org.junit.Test

class TokenModelTest {

    @Test
    fun defaultValuesShouldBeCorrect() {
        val tokenModel = TokenModel(0,"")
        assertEquals(0, tokenModel.id)
        assertEquals("", tokenModel.token)
    }

    @Test
    fun customValuesShouldBeStoredCorrectly() {
        val tokenModel = TokenModel(id = 1, token = "sampleToken")
        assertEquals(1, tokenModel.id)
        assertEquals("sampleToken", tokenModel.token)
    }
}