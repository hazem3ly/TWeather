package com.hazem.tweather.data.network.responce

import com.google.gson.annotations.SerializedName
import com.hazem.tweather.data.db.entity.CurrentWeatherEntry
import com.hazem.tweather.data.db.entity.Location

data class CurrentWeatherResponce(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location
)