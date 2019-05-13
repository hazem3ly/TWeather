package com.hazem.tweather.ui.weather.future.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hazem.tweather.R
import com.hazem.tweather.data.db.LocalDateConverter
import com.hazem.tweather.internal.DateNotFoundException
import com.hazem.tweather.internal.glide.GlideApp
import com.hazem.tweather.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.future_details_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureDetailsWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private lateinit var viewModel: FutureDetailsWeatherViewModel

    private val viewModelFactoryInstanceFactory
            : ((LocalDate) -> FutureDetailWeatherViewModelFactory) by factory()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_details_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { FutureDetailsWeatherFragmentArgs.fromBundle(it) }
        val date = LocalDateConverter.stringToDate(safeArgs?.dateString) ?: throw DateNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(date))
            .get(FutureDetailsWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@FutureDetailsWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })

        futureWeather.observe(this@FutureDetailsWeatherFragment, Observer { weatherEntry ->
            if (weatherEntry == null) return@Observer

            updateDate(weatherEntry.date)
            updateTemperatures(
                weatherEntry.avgTemperature,
                weatherEntry.minTemperature, weatherEntry.maxTemperature
            )
            updateCondition(weatherEntry.conditionText)
            updatePrecipitation(weatherEntry.totalPrecipitation)
            updateWindSpeed(weatherEntry.maxWindSpeed)
            updateVisibility(weatherEntry.avgVisibilityDistance)
            updateUv(weatherEntry.uv)

            GlideApp.with(this@FutureDetailsWeatherFragment)
                .load("https:" + weatherEntry.conditionIconUrl)
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(date: LocalDate) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        val unitAbbreviation =
            chooseLocalizedUnitAbbreviation(getString(R.string.celsius), getString(R.string.fahrenheit))
        textView_temperature.text = getString(R.string.temperature, temperature.toString(), unitAbbreviation)
        textView_min_max_temperature.text =
            getString(R.string.min_max_temperature, min.toString(), unitAbbreviation, max.toString(), unitAbbreviation)
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(getString(R.string.mm), getString(R.string.inch))
        textView_precipitation.text = getString(R.string.precipitation, precipitationVolume.toString(), unitAbbreviation)
    }

    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(getString(R.string.kph), getString(R.string.mph))
        textView_wind.text = getString(R.string.wind_speed, windSpeed.toString(), unitAbbreviation)
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation(getString(R.string.km), getString(R.string.mi))
        textView_visibility.text = getString(R.string.visibility, visibilityDistance.toString(), unitAbbreviation)
    }

    private fun updateUv(uv: Double) {
        textView_uv.text = getString(R.string.uv,uv.toString())
    }
}
