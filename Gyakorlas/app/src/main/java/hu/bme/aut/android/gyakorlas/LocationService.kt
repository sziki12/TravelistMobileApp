package hu.bme.aut.android.gyakorlas

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.concurrent.Volatile
import kotlin.concurrent.thread

class LocationService() : Service()
{

    private lateinit var locationClient: FusedLocationProviderClient
    @Volatile
    private var isRunning:Boolean = true
    private var callbacks: ArrayList<()->Unit> = ArrayList()
    private var useHighAccuracy = false
    private var waitOfFaileur:Long = 2000
    private var waitOnSuccess:Long = 5000
    private var stringIndex = 0
    companion object
    {
        @Volatile
        var currentLocation: Location? = null

        fun calculateDistance(dest: LatLng):Float?

        {   var distance:Float? = null
            if(currentLocation!=null)
            {
                var markerLocaton = Location("Provider")
                markerLocaton.latitude = dest.latitude
                markerLocaton.longitude = dest.longitude
                val results = FloatArray(1)
                Location.distanceBetween(
                    markerLocaton.latitude,
                    markerLocaton.longitude,
                    currentLocation!!.latitude,
                    currentLocation!!.longitude,
                    results
                )
                distance =results[0]
            }
            return distance
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        //Thread.sleep(1000)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        thread(start=true, isDaemon = true)
        {
            while (isRunning)
            {
                //waitOfFaileur = 0
                //waitOnSuccess = 1
                if (PermissionHandler.hasPermission[PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE] == true)
                {
                    if(PermissionHandler.hasPermission[PermissionHandler.BACKGROUND_LOCATION_REQUEST_CODE] == true)
                    {
                        Log.i("PERMISSION", "LocationService RUNNING")

                            updateCurrentLocation(useHighAccuracy)
                            Thread.sleep(waitOnSuccess)

                    } else
                    {
                        Log.i("PERMISSION","Background Location Access Denied")
                        Thread.sleep(waitOfFaileur)
                    }
                } else
                {
                    Log.i("PERMISSION","Location Access Denied")
                    Thread.sleep(waitOfFaileur)
                }
            }
        }

        //TODO Nem működik, SharedPreferences
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, s ->
            Log.i("LOCATION","SharedPreferences: $s")
            if(s.contains("locationUpdateIntervalOnFailure"))
            {
                waitOnSuccess = sharedPreferences.getLong("locationUpdateIntervalOnFailure",waitOnSuccess)
                Log.i("LOCATION","locationUpdateIntervalOnFailure: $waitOnSuccess")
            }

            if(s.contains("locationUpdateInterval"))
            {
                waitOfFaileur = sharedPreferences.getLong("locationUpdateIntervalOnFailure",waitOfFaileur)
                Log.i("LOCATION","locationUpdateIntervalOnFailure: $waitOfFaileur")
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }



    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation(highAccuracyRequired:Boolean)
    {
        var priority = Priority.PRIORITY_HIGH_ACCURACY
        if(!highAccuracyRequired)
            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()
        if(PermissionHandler.hasPermission[PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE]==true)
        {
            locationClient!!.getCurrentLocation(priority, cancellationTokenSource.token)
                .addOnSuccessListener { location ->
                   currentLocation = location
                    Log.i("LOCATION","GettingLocationSuccessful: $currentLocation")
                }
                .addOnFailureListener { exception ->
                    Log.i("LOCATION","GettingLocationFailed: $exception")
                }
        }
    }

    override fun stopService(name: Intent?): Boolean {
        isRunning = false
        return super.stopService(name)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}