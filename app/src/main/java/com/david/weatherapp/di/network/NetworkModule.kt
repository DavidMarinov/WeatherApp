package com.david.weatherapp.di.network

import com.david.weatherapp.data.WeatherService
import com.david.weatherapp.data.interceptors.ApiKeyInterceptor
import com.david.weatherapp.data.interceptors.UnitsInterceptor
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(UnitsInterceptor())
            .build()

    @Provides
    @Singleton
    fun provideWeatherService(
        okHttpClient: OkHttpClient
    ): WeatherService = provideService(
        okHttpClient = okHttpClient,
        entityClass = WeatherService::class.java
    )


    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        entityClass: Class<T>
    ): T = createRetrofit(okHttpClient = okHttpClient).create(entityClass)

    private fun createRetrofit(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .client(okHttpClient)
        .build()
}