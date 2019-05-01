package com.hazem.tweather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hazem.tweather.data.network.responce.CurrentWeatherResponce
import com.hazem.tweather.internal.NoConnectivityException
import java.lang.Exception

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponce>()

    override val downloadCurrentWeather: LiveData<CurrentWeatherResponce>
        get() = _downloadCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = apixuWeatherApiService.getCurrentWeather(location, languageCode).await()
            _downloadCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("WeatherNetworkDataSourc", "fetchCurrentWeather (line 24): No Network Available", e)
        }
    }
}