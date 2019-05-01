package com.hazem.tweather.data.repository

import androidx.lifecycle.LiveData
import com.hazem.tweather.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForcastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
}