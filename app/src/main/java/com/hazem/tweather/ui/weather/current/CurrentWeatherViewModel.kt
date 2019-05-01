package com.hazem.tweather.ui.weather.current

import androidx.lifecycle.ViewModel
import com.hazem.tweather.data.repository.ForcastRepository
import com.hazem.tweather.internal.UnitSystem
import com.hazem.tweather.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForcastRepository
) : ViewModel() {

    private val unitSystem = UnitSystem.IMPERIAL
    val isMetric
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
