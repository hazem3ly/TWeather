package com.hazem.tweather.data.repository

import androidx.lifecycle.LiveData
import com.hazem.tweather.data.db.entity.WeatherLocation
import com.hazem.tweather.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}