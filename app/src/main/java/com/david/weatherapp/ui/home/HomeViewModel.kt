package com.david.weatherapp.ui.home

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.weatherapp.data.WeatherService
import com.david.weatherapp.data.model.CurrentWeatherResponse
import com.david.weatherapp.data.model.ErrorResponse
import com.david.weatherapp.utils.SingleLiveEvent
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel @ViewModelInject constructor(
    private val weatherService: WeatherService
) : ViewModel() {

    val currentWeatherLiveData = MutableLiveData<CurrentWeatherPresentation>()
    val errorLiveData = SingleLiveEvent<String>()

    fun fetchCurrentWeather(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = weatherService.getCurrentWeather(
                lat = location.latitude,
                lon = location.longitude
            )
            handleCurrentWeatherResponse(data = data)
        }
    }

    fun fetchCurrentWeather(cityName: String) {
        if (cityName.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            val data = weatherService.getCurrentWeather(cityName = cityName)
            handleCurrentWeatherResponse(data = data)
        }
    }

    private suspend fun handleCurrentWeatherResponse(
        data: NetworkResponse<CurrentWeatherResponse, ErrorResponse>
    ) = withContext(Dispatchers.Default) {
        when (data) {
            is NetworkResponse.Success -> {
                val presentationData = CurrentWeatherPresentation(
                    weatherDescription = data.body.weather.firstOrNull()?.description ?: "",
                    weatherIcon = data.body.weather.firstOrNull()?.icon ?: "",
                    minTemp = data.body.main.tempMin,
                    maxTemp = data.body.main.tempMax,
                    temp = data.body.main.temp,
                    cityName = data.body.name
                )
                currentWeatherLiveData.postValue(presentationData)
            }
            is NetworkResponse.NetworkError -> {
                errorLiveData.postValue("Error! Please try again later")
            }
            is NetworkResponse.ServerError -> {
                errorLiveData.postValue("${data.body?.message}")
            }
            is NetworkResponse.UnknownError -> {
                errorLiveData.postValue("UnknownError Please Try Again Later")
            }
        }
    }

}

data class CurrentWeatherPresentation(
    val weatherDescription: String,
    val weatherIcon: String,
    val minTemp: Double,
    val maxTemp: Double,
    val temp: Double,
    val cityName: String
)