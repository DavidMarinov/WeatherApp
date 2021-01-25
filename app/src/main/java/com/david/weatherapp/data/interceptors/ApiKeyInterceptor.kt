package com.david.weatherapp.data.interceptors

import com.david.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url()
        val newUrl = url.newBuilder()
            .addQueryParameter("appid", BuildConfig.OPEN_WEATHER_MAP_APP_ID)
            .build()

        val newRequest = request.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}