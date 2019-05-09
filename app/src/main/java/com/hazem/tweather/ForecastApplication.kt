package com.hazem.tweather

import android.content.Context
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.hazem.tweather.data.db.ForecastDatabase
import com.hazem.tweather.data.network.*
import com.hazem.tweather.data.provider.LocationProvider
import com.hazem.tweather.data.provider.LocationProviderImpl
import com.hazem.tweather.data.provider.UnitProvider
import com.hazem.tweather.data.provider.UnitProviderImpl
import com.hazem.tweather.data.repository.ForecastRepository
import com.hazem.tweather.data.repository.ForecastRepositoryImpl
import com.hazem.tweather.ui.weather.current.CurrentWeatherViewModelFactory
import com.hazem.tweather.ui.weather.future.detail.FutureDetailWeatherViewModelFactory
import com.hazem.tweather.ui.weather.future.list.FutureListWeatherViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class ForecastApplication : MultiDexApplication(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind() from singleton { ApixuWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(),instance(),instance(),instance(),instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from  provider { CurrentWeatherViewModelFactory(instance(),instance()) }
        bind() from  provider { FutureListWeatherViewModelFactory(instance(),instance()) }
        bind() from factory { detailDate: LocalDate -> FutureDetailWeatherViewModelFactory(detailDate, instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this,R.xml.preference,false)
    }


}