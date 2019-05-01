package com.hazem.tweather

import android.app.Application
import com.hazem.tweather.data.db.ForcastDatabase
import com.hazem.tweather.data.network.*
import com.hazem.tweather.data.repository.ForcastRepository
import com.hazem.tweather.data.repository.ForcastRepositoryImpl
import com.hazem.tweather.ui.weather.current.CurrentWeatherViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForcastDatabase(instance()) }
        bind() from singleton { instance<ForcastDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForcastRepository>() with singleton { ForcastRepositoryImpl(instance(),instance()) }
        bind() from  provider { CurrentWeatherViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }


}