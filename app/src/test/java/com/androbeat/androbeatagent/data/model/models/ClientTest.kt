package com.androbeat.androbeatagent.data.model.models

import org.junit.Assert.assertEquals
import org.junit.Test

class ClientTest {

    @Test
    fun equalsShouldReturnTrueForSameValues() {
        val client1 = Client(1, "Client Name")
        val client2 = Client(1, "Client Name")
        assertEquals(client1, client2)
    }

    @Test
    fun toStringShouldReturnCorrectString() {
        val client = Client(1, "Client Name")
        val expectedString = "Client(id=1, name=Client Name)"
        assertEquals(expectedString, client.toString())
    }
}