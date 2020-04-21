package com.skanderjabouzi.kotlincoroutinesplusflow.custom.aliases

import com.skanderjabouzi.kotlincoroutinesplusflow.base.Result
import com.skanderjabouzi.kotlincoroutinesplusflow.room.models.forecasts.Forecast
import com.skanderjabouzi.kotlincoroutinesplusflow.room.models.weather.DbWeather

typealias WeatherResult = Result<DbWeather>

typealias ListOfForecasts = List<Forecast>

typealias ForecastResults = Result<ListOfForecasts>