package com.androbeat.androbeatagent.data.model

import com.androbeat.androbeatagent.data.model.models.LoggedInUser
import org.junit.Assert.assertEquals
import org.junit.Test

class LoggedInUserTest {

    @Test
    fun testequals() {
        val user1 = LoggedInUser("id1", "John Doe")
        val user2 = LoggedInUser("id1", "John Doe")

        assertEquals(user1, user2)
    }

    @Test
    fun testhashCode() {
        val user = LoggedInUser("id1", "John Doe")
        val expectedHashCode = user.hashCode()
        val actualHashCode = LoggedInUser("id1", "John Doe").hashCode()

        assertEquals(expectedHashCode, actualHashCode)
    }

    @Test
    fun testtoString() {
        val user = LoggedInUser("id1", "John Doe")
        val expectedString = "LoggedInUser(userId=id1, displayName=John Doe)"
        val actualString = user.toString()

        assertEquals(expectedString, actualString)
    }
}