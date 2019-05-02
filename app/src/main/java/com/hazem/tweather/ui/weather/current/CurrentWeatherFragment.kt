package com.hazem.tweather.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.hazem.tweather.R
import com.hazem.tweather.data.network.ApixuWeatherApiService
import com.hazem.tweather.data.network.ConnectivityInterceptorImpl
import com.hazem.tweather.data.network.WeatherNetworkDataSourceImpl
import com.hazem.tweather.internal.glide.GlideApp
import com.hazem.tweather.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUi()

    }

    private fun bindUi() = launch {
        val currentWeather = viewModel.weather.await()
        val currentWeatherLocation = viewModel.weatherLocation.await()


        currentWeatherLocation.observe(this@CurrentWeatherFragment.viewLifecycleOwner, Observer { location ->
            location?.let {
                updateLocation(it.name)
            }

        })


        currentWeather.observe(this@CurrentWeatherFragment.viewLifecycleOwner, Observer { currentWeather ->

            currentWeather?.let {
                group_loading.visibility = View.GONE

                updateDateToToday()
                updateTemperatures(it.temperature, it.feelsLikeTemperature)
                updateCondition(it.conditionText)
                updatePrecipitation(it.precipitationVolume)
                updateWind(it.windDirection, it.windSpeed)
                updateVisibility(it.visibilityDistance)

                GlideApp.with(this@CurrentWeatherFragment)
                    .load("https:${it.conditionIconUrl}")
                    .into(imageView_condition_icon)

            }


        })

    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbreviation"
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }
}
