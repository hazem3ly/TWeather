package com.hazem.tweather.ui.weather.future.detail

import com.hazem.tweather.data.provider.UnitProvider
import com.hazem.tweather.data.repository.ForecastRepository
import com.hazem.tweather.internal.lazyDeferred
import com.hazem.tweather.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailsWeatherViewModel (
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getFutureWeatherByDate(detailDate, super.isMetricUnit)
    }
}