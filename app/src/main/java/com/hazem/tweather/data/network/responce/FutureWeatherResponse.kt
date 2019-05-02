package com.hazem.tweather.data.network.responce

import com.google.gson.annotations.SerializedName
import com.hazem.tweather.data.db.entity.WeatherLocation

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val futureWeatherEntries: ForecastDaysContainer,
    val location: WeatherLocation
)