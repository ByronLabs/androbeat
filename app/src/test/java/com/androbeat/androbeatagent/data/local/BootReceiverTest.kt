package com.androbeat.androbeatagent.data.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.androbeat.androbeatagent.data.daos.TokenDao
import com.androbeat.androbeatagent.data.model.models.TokenModel
import com.androbeat.androbeatagent.data.repository.AppDatabase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class BootReceiverTest {
    private lateinit var bootReceiver: BootReceiver
    private lateinit var context: Context
    private lateinit var intent: Intent
    private lateinit var appDatabase: AppDatabase
    private lateinit var tokenDao: TokenDao
    private lateinit var pendingResult: BroadcastReceiver.PendingResult

    @Before
    fun setUp() {
        bootReceiver = spyk(BootReceiver())
        context = mockk(relaxed = true)
        intent = mockk(relaxed = true)
        appDatabase = mockk(relaxed = true)
        tokenDao = mockk(relaxed = true)
        pendingResult = mockk(relaxed = true)

        mockkObject(AppDatabase.Companion)
        every { AppDatabase.getDatabase(context) } returns appDatabase
        every { appDatabase.tokenDao() } returns tokenDao
        coEvery { tokenDao.getToken() } returns TokenModel(token = "token")
        every { bootReceiver.goAsync() } returns pendingResult
    }

    @Test
    fun testOnReceiveStartsServiceWhenTokenExists() {
        every { intent.action } returns Intent.ACTION_BOOT_COMPLETED

        bootReceiver.onReceive(context, intent)

        verify(timeout = 1000) { context.startService(any()) }
        verify(timeout = 1000) { pendingResult.finish() }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
