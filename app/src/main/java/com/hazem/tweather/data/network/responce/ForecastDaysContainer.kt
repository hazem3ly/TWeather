package com.hazem.tweather.data.network.responce

import com.hazem.tweather.data.db.entity.FutureWeatherEntry

data class ForecastDaysContainer(
    val forecastday: List<FutureWeatherEntry>
)