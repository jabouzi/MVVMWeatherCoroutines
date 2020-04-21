package com.skanderjabouzi.kotlincoroutinesplusflow.features.forecasts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skanderjabouzi.kotlincoroutinesplusflow.custom.aliases.ForecastResults
import com.skanderjabouzi.kotlincoroutinesplusflow.extensions.cancelIfActive
import com.skanderjabouzi.kotlincoroutinesplusflow.features.home.di.HomeScope
import com.skanderjabouzi.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HomeScope
class ForecastsViewModel @Inject constructor(private val forecastsRepository: ForecastsRepository) :
    ViewModel() {

    private val mutableForecastLiveData = MutableLiveData<ForecastResults>()
    private var getForecastsJob: Job? = null

    val forecastLiveData = mutableForecastLiveData

    /**
     * Cancel existing job if running and then launch forecastsRepository.getForecasts using
     * viewModelScope
     */
    fun getForecasts() {
        getForecastsJob.cancelIfActive()
        getForecastsJob = viewModelScope.launch {
            forecastsRepository.getForecasts(Utils.LONDON_CITY_ID).collect {
                mutableForecastLiveData.value = it
            }
        }
    }
}