package com.david.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
	@SerializedName("weather") val weather: List<WeatherItem>,
	@SerializedName("name") val name: String,
	@SerializedName("main") val main: Main,
	@SerializedName("id") val id: Int,
)


data class Main(
	@SerializedName("temp") val temp: Double,
	@SerializedName("humidity") val humidity: Int,
	@SerializedName("pressure") val pressure: Int,
	@SerializedName("temp_min") val tempMin: Double,
	@SerializedName("temp_max") val tempMax: Double
)

data class WeatherItem(
	@SerializedName("icon") val icon: String,
	@SerializedName("description") val description: String,
	@SerializedName("main") val main: String,
	@SerializedName("id") val id: Int
)

