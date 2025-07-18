package com.androbeat.androbeatagent.data.repository

import android.content.Context

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androbeat.androbeatagent.data.converters.Converters
import com.androbeat.androbeatagent.data.daos.ClientIdDao
import com.androbeat.androbeatagent.data.daos.DeviceIdDao
import com.androbeat.androbeatagent.data.daos.ElasticDataDao
import com.androbeat.androbeatagent.data.daos.TokenDao
import com.androbeat.androbeatagent.data.model.models.Client
import com.androbeat.androbeatagent.data.model.models.DeviceId
import com.androbeat.androbeatagent.data.model.models.TokenModel
import com.androbeat.androbeatagent.data.model.models.elastic.ElasticDataModel

@Database(entities = [ElasticDataModel::class, Client::class,
    TokenModel::class,
    DeviceId::class], version = 17, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun elasticDataDao(): ElasticDataDao
    abstract fun clientIdDao(): ClientIdDao
    abstract fun tokenDao(): TokenDao
    abstract fun deviceIdDao(): DeviceIdDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "ANDROBEAT"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}