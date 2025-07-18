package com.androbeat.androbeatagent.models.extractors.software

import com.androbeat.androbeatagent.data.model.models.extractors.software.EnvironmentVariableModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EnvironmentVariableModelTest {

    @Test
    fun equalsWithIdenticalValuesReturnsTrue() {
        val envVar1 = EnvironmentVariableModel(name = "PATH", value = "/usr/bin:/bin")
        val envVar2 = EnvironmentVariableModel(name = "PATH", value = "/usr/bin:/bin")

        assertTrue(envVar1.equals(envVar2))
    }

    @Test
    fun equalsWithDifferentValuesReturnsFalse() {
        val envVar1 = EnvironmentVariableModel(name = "PATH", value = "/usr/bin:/bin")
        val envVar2 = EnvironmentVariableModel(name = "JAVA_HOME", value = "/usr/lib/jvm/java-8-openjdk")

        assertFalse(envVar1.equals(envVar2))
    }
}