package com.david.weatherapp.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class UnitsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url()
        val newUrl = url.newBuilder()
            .addQueryParameter("units", "metric")
            .build()

        val newRequest = request.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}