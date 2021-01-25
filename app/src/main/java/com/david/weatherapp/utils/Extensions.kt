package com.david.weatherapp.utils

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, lambda: (T) -> Unit) {
    observe(lifecycleOwner) {
        if (it == null) return@observe
        lambda.invoke(it)
    }
}


fun AppCompatActivity.executeWithPermissionCheck(
    call: () -> Unit,
    permission: String
) {
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) return@registerForActivityResult
            call.invoke()
        }

    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
        call.invoke()
    } else {
        requestPermissionLauncher.launch(permission)
    }
}

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun AppCompatActivity.singleShotUserLocation(onLocation: (Location) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    fusedLocationClient.lastLocation.addOnCompleteListener {
        val location = it.result
        if (location == null) {
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.create().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 2000
                    numUpdates = 1
                    fastestInterval = 1000
                },
                object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        if (p0 == null) return
                        onLocation.invoke(p0.lastLocation)
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                },
                null
            )
            return@addOnCompleteListener
        }
        onLocation.invoke(location)
    }
}