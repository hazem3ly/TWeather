package com.hazem.tweather

import android.app.Application
import androidx.preference.PreferenceManager
import com.hazem.tweather.data.db.ForecastDatabase
import com.hazem.tweather.data.network.*
import com.hazem.tweather.data.provider.LocationProvider
import com.hazem.tweather.data.provider.LocationProviderImpl
import com.hazem.tweather.data.provider.UnitProvider
import com.hazem.tweather.data.provider.UnitProviderImpl
import com.hazem.tweather.data.repository.ForecastRepository
import com.hazem.tweather.data.repository.ForecastRepositoryImpl
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

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl() }
        bind() from singleton { ApixuWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(),instance(),instance(),instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from  provider { CurrentWeatherViewModelFactory(instance(),instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this,R.xml.preference,false)
    }


}