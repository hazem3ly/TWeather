package com.hazem.tweather.data.network

import androidx.lifecycle.LiveData
import com.hazem.tweather.data.network.responce.CurrentWeatherResponse
import com.hazem.tweather.data.network.responce.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String = "en"
    )

    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String = "en"
    )
}