package com.david.weatherapp.utils

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("android:weatherIcon")
fun ImageView.weatherIcon(iconId: String?) {
    if (iconId == null) return
    val url = "http://openweathermap.org/img/wn/$iconId@2x.png"
    load(url)
}

@BindingAdapter("android:cityName")
fun SearchView.cityName(cityName: String?) {
    if (cityName == null) return
    setQuery(cityName, false)
}