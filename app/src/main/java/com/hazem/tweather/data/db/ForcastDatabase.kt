package com.hazem.tweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hazem.tweather.data.db.entity.CurrentWeatherEntry

@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1
)

abstract class ForcastDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object {
        @Volatile
        private var inistance: ForcastDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = inistance ?: synchronized(LOCK) {
            inistance ?: buildDatabase(context).also { inistance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ForcastDatabase::class.java,
            "forcast.db"
        ).build()
    }

}