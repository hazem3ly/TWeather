package com.hazem.tweather.ui.weather.current

import com.hazem.tweather.data.provider.UnitProvider
import com.hazem.tweather.data.repository.ForecastRepository
import com.hazem.tweather.internal.lazyDeferred
import com.hazem.tweather.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(super.isMetricUnit)
    }
}
