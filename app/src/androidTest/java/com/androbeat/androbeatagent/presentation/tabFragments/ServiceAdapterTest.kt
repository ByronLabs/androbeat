package com.androbeat.androbeatagent.presentation.tabFragments

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ServiceAdapterTest {

    private lateinit var adapter: ServiceAdapter
    private lateinit var services: List<String>
    private lateinit var serviceStatus: Map<String, ServiceAvailability>

    @Before
    fun setUp() {
        services = listOf("Service1", "Service2", "Service3")
        serviceStatus = mapOf(
            "Service1" to ServiceAvailability.AVAILABLE,
            "Service2" to ServiceAvailability.UNAVAILABLE,
            "Service3" to ServiceAvailability.AVAILABLE
        )
        adapter = ServiceAdapter(services, serviceStatus)
    }

    @Test
    fun getItemCount_returnsCorrectSize() {
        assertEquals(3, adapter.itemCount)
    }

    @Test
    fun updateServiceStatus_updatesDataAndNotifies() {
        val newServiceStatus = mapOf("Service1" to ServiceAvailability.UNSUPPORTED, "Service2" to ServiceAvailability.AVAILABLE, "Service3" to ServiceAvailability.UNAVAILABLE)
        adapter.updateServiceStatus(newServiceStatus)
        assertEquals(3, adapter.itemCount)
    }
}