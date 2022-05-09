package com.example.weathead340

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weathead340.api.CurrentWeather
import com.example.weathead340.api.WeeklyForecast
import com.example.weathead340.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository {

    private val weatherService = createOpenWeatherMapService()

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _weeklyForecast = MutableLiveData<WeeklyForecast>()
    val weeklyForecast: LiveData<WeeklyForecast> = _weeklyForecast
    fun loadWeeklyForecast(zipcode: String) {
        val call = weatherService.currentWeather(zipcode, BuildConfig.OPEN_WEATHER_MAP_API_KEY, "imperial")
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading location for weekly forecast", t)
            }

            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    val forecastCall = weatherService.sevenDayForecast(
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon,
                        exclude = "current,minutely,hourly",
                        units = "imperial",
                        apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                    )

                    forecastCall.enqueue(object : Callback<WeeklyForecast> {
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(ForecastRepository::class.java.simpleName, "error loading weekly forecast", t)
                        }

                        override fun onResponse(call: Call<WeeklyForecast>, response: Response<WeeklyForecast>) {
                            val weeklyForecastResponse = response.body()
                            if (weeklyForecastResponse != null) {
                                _weeklyForecast.value = weeklyForecastResponse!!
                            }
                        }

                    })
                }
            }
        })

    }



    fun loadCurrentForecast(zipCode: String){
        val call = createOpenWeatherMapService().currentWeather(zipCode, BuildConfig.OPEN_WEATHER_MAP_API_KEY, "imperial")
        call.enqueue(object: Callback<CurrentWeather>{
            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if(weatherResponse != null){
                    _currentWeather.value = weatherResponse!!
                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading current weather", t)
            }

        })
    }
}
