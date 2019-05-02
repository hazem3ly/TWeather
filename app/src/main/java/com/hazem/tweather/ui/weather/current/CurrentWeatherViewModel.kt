package com.hazem.tweather.ui.weather.current

import androidx.lifecycle.ViewModel
import com.hazem.tweather.data.provider.UnitProvider
import com.hazem.tweather.data.repository.ForecastRepository
import com.hazem.tweather.internal.UnitSystem
import com.hazem.tweather.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()
    val isMetric
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
