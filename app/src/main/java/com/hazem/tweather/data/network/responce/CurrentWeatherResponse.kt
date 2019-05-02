package com.hazem.tweather.data.network.responce

import com.google.gson.annotations.SerializedName
import com.hazem.tweather.data.db.entity.CurrentWeatherEntry
import com.hazem.tweather.data.db.entity.WeatherLocation

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)