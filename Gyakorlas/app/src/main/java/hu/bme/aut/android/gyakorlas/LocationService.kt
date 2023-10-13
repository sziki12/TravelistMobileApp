package hu.bme.aut.android.gyakorlas

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
    companion object
    {
        @Volatile
        var currentLocation: Location? = null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        //Thread.sleep(1000)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        thread(start=true)
        {
            while (isRunning)
            {
            if (PermissionHandler.hasPermission[PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE] == true)
            {
                if(PermissionHandler.hasPermission[PermissionHandler.BACKGROUND_LOCATION_REQUEST_CODE] == true)
                {
                    Log.i("PERMISSION", "LocationService RUNNING")

                        updateCurrentLocation(useHighAccuracy)
                        Thread.sleep(5000)

                } else
                {
                    Log.i("PERMISSION","Background Location Access Denied")
                }
            } else
            {
                Log.i("PERMISSION","Location Access Denied")
            }
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