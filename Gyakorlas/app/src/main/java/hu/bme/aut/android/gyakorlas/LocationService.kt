package hu.bme.aut.android.gyakorlas

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.concurrent.Volatile
import kotlin.concurrent.thread

class LocationService() : Service(){

    private lateinit var locationClient: FusedLocationProviderClient
    @Volatile
    private var isRunning:Boolean = true
    private var callbacks: ArrayList<()->Unit> = ArrayList()
    private var useHighAccuracy = false
    companion object {
        @Volatile
        var currentLocation: Location? = null
        lateinit var activity:Activity
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(activity!=null)
        {
            locationClient = LocationServices.getFusedLocationProviderClient(this)
            PermissionHandler.requestPermission(activity,PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE,{
                PermissionHandler.requestPermission(activity,PermissionHandler.BACKGROUND_LOCATION_REQUEST_CODE,{
                    thread {
                        Log.i("PERMISSION","LocationService Started")
                        while (isRunning) {
                            updateCurrentLocation(useHighAccuracy)
                            Thread.sleep(5000)
                        }
                    }
                })
                {
                    Log.i("PERMISSION","Location Access Denied")
                }

            })
            {
                Log.i("PERMISSION","Background Location Access Denied")
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
        TODO("Return the communication channel to the service.")
        return null
    }
}