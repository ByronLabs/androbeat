package com.androbeat.androbeatagent.data.model.models

import com.androbeat.androbeatagent.data.model.models.LoggedInUser
import org.junit.Assert.assertEquals
import org.junit.Test

class LoggedInUserTest {

    @Test
    fun equalsShouldReturnTrueForSameValues() {
        val user1 = LoggedInUser("id1", "John Doe")
        val user2 = LoggedInUser("id1", "John Doe")
        assertEquals(user1, user2)
    }

    @Test
    fun hashCodeShouldReturnSameValueForSameValues() {
        val user1 = LoggedInUser("id1", "John Doe")
        val user2 = LoggedInUser("id1", "John Doe")
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun toStringShouldReturnCorrectString() {
        val user = LoggedInUser("id1", "John Doe")
        val expectedString = "LoggedInUser(userId=id1, displayName=John Doe)"
        assertEquals(expectedString, user.toString())
    }
}