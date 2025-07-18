package com.androbeat.androbeatagent.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.androbeat.androbeatagent.data.repository.AppDatabase
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class AppDatabaseTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext()
        // Use an in-memory database for testing.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // Allow queries on the main thread for testing purposes
            .build()
    }

    @Test
    fun testDatabaseSingletonInstance() {
        val instance1 = AppDatabase.getDatabase(context)
        val instance2 = AppDatabase.getDatabase(context)
        assertSame("The instances should be the same", instance1, instance2)
    }

    @After
    fun closeDb() {
        db.close()
    }
}