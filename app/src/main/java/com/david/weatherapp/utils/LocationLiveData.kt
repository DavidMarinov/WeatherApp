package com.david.weatherapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class LocationLiveData @Inject constructor(
    @ActivityContext
    private val context: Context
) : LiveData<Location>() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationUpdateCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 == null) return
            value = p0.lastLocation
        }
    }


    override fun onActive() {
        super.onActive()

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            error("Cannot Attach LocationLiveData WithOut Location Permission")
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            value = it
        }


        if (!hasActiveObservers()) return
        fusedLocationClient.requestLocationUpdates(
            LocationRequest(),
            locationUpdateCallback,
            null
        )
    }


    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationUpdateCallback)
    }

}