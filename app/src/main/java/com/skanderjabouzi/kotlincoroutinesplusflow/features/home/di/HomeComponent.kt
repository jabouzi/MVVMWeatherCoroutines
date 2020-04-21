package com.skanderjabouzi.kotlincoroutinesplusflow.features.home.di

import com.skanderjabouzi.kotlincoroutinesplusflow.features.forecasts.ForecastsFragment
import com.skanderjabouzi.kotlincoroutinesplusflow.features.home.HomeActivity
import com.skanderjabouzi.kotlincoroutinesplusflow.features.weather.WeatherFragment
import dagger.Subcomponent

@HomeScope
@Subcomponent(modules = [HomeViewModelsModule::class])
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(homeActivity: HomeActivity)
    fun inject(weatherFragment: WeatherFragment)
    fun inject(forecastsFragment: ForecastsFragment)
}