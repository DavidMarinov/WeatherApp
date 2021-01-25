package com.david.weatherapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.david.weatherapp.R
import com.david.weatherapp.databinding.ActivityMainBinding
import com.david.weatherapp.utils.LocationLiveData
import com.david.weatherapp.utils.executeWithPermissionCheck
import com.david.weatherapp.utils.observeNonNull
import com.david.weatherapp.utils.singleShotUserLocation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var locationLiveData: LocationLiveData
    private lateinit var binding: ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = homeViewModel

        binding.searchView.apply {
            queryHint = context.getString(R.string.hint_city_name)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        homeViewModel.fetchCurrentWeather(it)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    return false
                }
            })
        }

        homeViewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        executeWithPermissionCheck(
            call = ::singleShotUserLocation,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun singleShotUserLocation() {
        singleShotUserLocation(homeViewModel::fetchCurrentWeather)
    }


    private fun attachLocationLiveDataObserver() {
        locationLiveData.observeNonNull(this, homeViewModel::fetchCurrentWeather)
    }
}