package com.hazem.tweather.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hazem.tweather.R
import com.hazem.tweather.internal.glide.GlideApp
import com.hazem.tweather.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
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

    override fun onResume() {
        super.onResume()
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


        currentWeather.observe(this@CurrentWeatherFragment.viewLifecycleOwner, Observer {

            it?.let { currentWeatherEntry ->
                group_loading.visibility = View.GONE

                updateDateToToday()
                updateTemperatures(currentWeatherEntry.temperature, currentWeatherEntry.feelsLikeTemperature)
                updateCondition(currentWeatherEntry.conditionText)
                updatePrecipitation(currentWeatherEntry.precipitationVolume)
                updateWind(currentWeatherEntry.windDirection, currentWeatherEntry.windSpeed)
                updateVisibility(currentWeatherEntry.visibilityDistance)

                GlideApp.with(this@CurrentWeatherFragment)
                    .load("https:${currentWeatherEntry.conditionIconUrl}")
                    .into(imageView_condition_icon)

            }


        })

    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.today)
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation =
            chooseLocalizedUnitAbbreviation(getString(R.string.celsius), getString(R.string.fahrenheit))
        textView_temperature.text = getString(R.string.temperature, temperature.toString(), unitAbbreviation)
        textView_feels_like_temperature.text = getString(R.string.feels_like, feelsLike.toString(), unitAbbreviation)
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(getString(R.string.mm), getString(R.string.inch))
        textView_precipitation.text = getString(R.string.precipitation, precipitationVolume.toString(), unitAbbreviation)
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(getString(R.string.kph), getString(R.string.mph))
        textView_wind.text = getString(R.string.wind, windDirection, windSpeed.toString(), unitAbbreviation)
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(getString(R.string.km), getString(R.string.mi))
        textView_visibility.text = getString(R.string.visibility, visibilityDistance.toString(), unitAbbreviation)
    }
}
