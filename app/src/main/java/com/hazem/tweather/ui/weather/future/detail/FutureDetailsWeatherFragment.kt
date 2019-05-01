package com.hazem.tweather.ui.weather.future.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hazem.tweather.R

class FutureDetailsWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = FutureDetailsWeatherFragment()
    }

    private lateinit var viewModel: FutureDetailsWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_details_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FutureDetailsWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
