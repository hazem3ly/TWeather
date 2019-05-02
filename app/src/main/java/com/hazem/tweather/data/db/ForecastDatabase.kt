package com.hazem.tweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hazem.tweather.data.db.entity.CurrentWeatherEntry
import com.hazem.tweather.data.db.entity.WeatherLocation

@Database(
    entities = [CurrentWeatherEntry::class, WeatherLocation::class],
    version = 2
)

abstract class ForecastDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao

    companion object {
        @Volatile
        private var inistance: ForecastDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = inistance ?: synchronized(LOCK) {
            inistance ?: buildDatabase(context).also { inistance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ForecastDatabase::class.java,
            "forecast.db"
        ).build()
    }

}