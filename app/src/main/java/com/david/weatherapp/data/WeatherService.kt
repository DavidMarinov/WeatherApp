package com.david.weatherapp.data

import com.david.weatherapp.data.model.CurrentWeatherResponse
import com.david.weatherapp.data.model.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("2.5/weather")
    suspend fun getCurrentWeather(@Query("q") cityName: String): NetworkResponse<CurrentWeatherResponse, ErrorResponse>

    @GET("2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): NetworkResponse<CurrentWeatherResponse, ErrorResponse>
}