package com.hazem.tweather.data.network

import androidx.lifecycle.LiveData
import com.hazem.tweather.data.network.responce.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String = "en"
    )
}