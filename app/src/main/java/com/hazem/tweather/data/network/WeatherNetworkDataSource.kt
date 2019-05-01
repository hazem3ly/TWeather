package com.hazem.tweather.data.network

import androidx.lifecycle.LiveData
import com.hazem.tweather.data.network.responce.CurrentWeatherResponce

interface WeatherNetworkDataSource {
    val downloadCurrentWeather: LiveData<CurrentWeatherResponce>
    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String = "en"
    )
}