package com.skanderjabouzi.kotlincoroutinesplusflow.di.components

import com.skanderjabouzi.kotlincoroutinesplusflow.di.modules.AppModule
import com.skanderjabouzi.kotlincoroutinesplusflow.di.modules.DbModule
import com.skanderjabouzi.kotlincoroutinesplusflow.di.modules.OpenWeatherApiModule
import com.skanderjabouzi.kotlincoroutinesplusflow.di.modules.SubcomponentsModule
import com.skanderjabouzi.kotlincoroutinesplusflow.features.home.di.HomeComponent
import com.squareup.moshi.Moshi
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, OpenWeatherApiModule::class, DbModule::class, SubcomponentsModule::class])
@Singleton
interface ApplicationComponent {
    fun getMoshi(): Moshi
    fun homeComponent(): HomeComponent.Factory
}