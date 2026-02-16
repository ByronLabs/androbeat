package com.androbeat.androbeatagent.data

import android.content.Context
import com.androbeat.androbeatagent.data.remote.rest.logstash.LogstashApiInterface
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.data.repository.managers.DataManagerImp
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService

class DataManagerImpTest {

    private lateinit var dataManager: DataManagerImp
    private lateinit var context: Context
    @RelaxedMockK
    private lateinit var roomExecutor: ExecutorService
    @RelaxedMockK
    private lateinit var logstashApiInterface: LogstashApiInterface
    @RelaxedMockK
    private lateinit var appDatabase: AppDatabase
    private lateinit var dateFormatter: SimpleDateFormat

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        context = mockk<Context>()
        roomExecutor = mockk<ExecutorService>()
        logstashApiInterface = mockk<LogstashApiInterface>()
        appDatabase = mockk<AppDatabase>()
        dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        dataManager = DataManagerImp(
            context,
            roomExecutor,
            logstashApiInterface,
            appDatabase,
            dateFormatter
        )
    }


    @Test
    fun testCurrentDateInDesiredFormat() {
        val date = Date()
        val expectedFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = expectedFormat.format(date)
        assertEquals(formattedDate, dataManager.currentDateInDesiredFormat)
    }
}
