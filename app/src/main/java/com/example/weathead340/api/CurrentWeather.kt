package com.example.weathead340.api

import com.squareup.moshi.Json

data class Forecast(val temp: Float)
data class Coordinates(val lat: Float, val lon: Float)
data class CurrentForecastWeather(val main: String, val description: String, val icon: String)

data class CurrentWeather(
    val name: String,
    val coord: Coordinates,
    val weather: List<CurrentForecastWeather>,
    @field:Json(name = "main") val forecast: Forecast
)