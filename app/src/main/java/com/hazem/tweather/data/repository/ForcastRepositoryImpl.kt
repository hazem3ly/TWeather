package com.hazem.tweather.data.repository

import androidx.lifecycle.LiveData
import com.hazem.tweather.data.db.CurrentWeatherDao
import com.hazem.tweather.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.hazem.tweather.data.network.WeatherNetworkDataSource
import com.hazem.tweather.data.network.responce.CurrentWeatherResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForcastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForcastRepository {

    init {
        weatherNetworkDataSource.downloadCurrentWeather.observeForever { newCurrentWeather ->
            // persist
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()

        }
    }

    private fun persistFetchedCurrentWeather(currentWeatherResponce: CurrentWeatherResponce) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(currentWeatherResponce.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData() {

        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()

    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather("EGYPT")
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinitsAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinitsAgo)
    }

}