package com.skanderjabouzi.kotlincoroutinesplusflow.features.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skanderjabouzi.kotlincoroutinesplusflow.R
import com.skanderjabouzi.kotlincoroutinesplusflow.WeatherApplication
import com.skanderjabouzi.kotlincoroutinesplusflow.features.home.di.HomeComponent


class HomeActivity : AppCompatActivity() {

    var homeComponent: HomeComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        homeComponent = (applicationContext as WeatherApplication)
            .appComponent.homeComponent().create()
        homeComponent?.inject(this)
    }
}
