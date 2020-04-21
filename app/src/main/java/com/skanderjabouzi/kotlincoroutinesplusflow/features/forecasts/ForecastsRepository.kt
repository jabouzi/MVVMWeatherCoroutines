package com.skanderjabouzi.kotlincoroutinesplusflow.features.forecasts

import com.skanderjabouzi.kotlincoroutinesplusflow.base.Error
import com.skanderjabouzi.kotlincoroutinesplusflow.base.Success
import com.skanderjabouzi.kotlincoroutinesplusflow.custom.errors.ErrorHandler
import com.skanderjabouzi.kotlincoroutinesplusflow.custom.errors.NoDataException
import com.skanderjabouzi.kotlincoroutinesplusflow.custom.errors.NoResponseException
import com.skanderjabouzi.kotlincoroutinesplusflow.entitymappers.forecasts.ForecastMapper
import com.skanderjabouzi.kotlincoroutinesplusflow.extensions.applyCommonSideEffects
import com.skanderjabouzi.kotlincoroutinesplusflow.features.home.di.HomeScope
import com.skanderjabouzi.kotlincoroutinesplusflow.network.api.OpenWeatherApi
import com.skanderjabouzi.kotlincoroutinesplusflow.network.response.ErrorResponse
import com.skanderjabouzi.kotlincoroutinesplusflow.room.dao.forecasts.ForecastDao
import com.skanderjabouzi.kotlincoroutinesplusflow.room.dao.utils.StringKeyValueDao
import com.skanderjabouzi.kotlincoroutinesplusflow.room.models.forecasts.DbForecast
import com.skanderjabouzi.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HomeScope
class ForecastsRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val forecastDao: ForecastDao,
    private val stringKeyValueDao: StringKeyValueDao
) {

    private val forecastCacheThresholdMillis = 3 * 3600000L //3 hours//

    fun getForecasts(cityId: Int) = flow {
        stringKeyValueDao.get(Utils.LAST_FORECASTS_API_CALL_TIMESTAMP)
            ?.takeIf { !Utils.shouldCallApi(it.value, forecastCacheThresholdMillis) }
            ?.let { emit(getDataOrError(NoDataException())) }
            ?: emit((getForecastFromAPI(cityId)))
        }
        .applyCommonSideEffects()
        .catch {
            emit(getDataOrError(it))
        }

    private suspend fun getForecastFromAPI(cityId: Int) = openWeatherApi.getWeatherForecast(cityId)
        .run {
            if (isSuccessful && body() != null) {
                stringKeyValueDao.insert(
                    Utils.getCurrentTimeKeyValuePair(Utils.LAST_FORECASTS_API_CALL_TIMESTAMP)
                )
                forecastDao.deleteAllAndInsert(ForecastMapper(body()!!).map())
                getDataOrError(NoDataException())
            } else {
                Error(
                    NoResponseException(
                        ErrorHandler.parseError<ErrorResponse>(errorBody())?.message
                    )
                )
            }
        }

    private suspend fun getDataOrError(throwable: Throwable) =
        forecastDao.get()
            ?.let { dbValue -> Success(getForecastList(dbValue)) }
            ?: Error(throwable)

    private suspend fun getForecastList(dbForecast: DbForecast) = withContext(Dispatchers.Default) {
        dbForecast.list.map { it.forecast }
    }
}
