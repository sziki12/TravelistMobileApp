package hu.bme.aut.android.gyakorlas.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler
import hu.bme.aut.android.gyakorlas.R
import kotlin.concurrent.Volatile
import kotlin.concurrent.thread

class LocationService() : Service()
{

    private lateinit var locationClient: FusedLocationProviderClient

    lateinit var preferences:SharedPreferences
    var locationUpdateIntervalMap:HashMap<String,Int> = HashMap()
    var locationUpdateIntervalOnFaleurMap:HashMap<String,Int> = HashMap()
    private var updateThread :LocationThread? = null
    //private var changeListener: LocationService.changeListener
    @Volatile
    var waitOnFaileur:Int = 2000
    @Volatile
    var waitOnSuccess:Int = 5000
    @Volatile
    var useHighAccuracy = false
    @Volatile
    var isSuccess = false

    interface LocationChangeListener
    {
        fun notifyOnLocationChange()
    }

    companion object
    {
        @Volatile
        var isRunning:Boolean = true
        @Volatile
        var currentLocation: Location? = null
        @Volatile
        private var listeners : ArrayList<LocationChangeListener> = ArrayList()

        var instance:LocationService? = null


        fun registerListener(listener:LocationChangeListener)
        {
            listeners.add(listener)
        }

        fun unregisterListener(listener:LocationChangeListener)
        {
            listeners.remove(listener)
        }

        private fun updateLocationListeners()
        {
            for(listener in listeners)
            {
                listener.notifyOnLocationChange()
            }
        }

        fun calculateDistance(dest: LatLng):Float?

        {   var distance:Float? = null
            if(currentLocation !=null)
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        instance = this
        isRunning = true
        locationClient = LocationServices.getFusedLocationProviderClient(this)

        updateThread = LocationThread(this)
        updateThread!!.start()

        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    fun updateCurrentLocation(highAccuracyRequired:Boolean)
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
                    thread {
                        updateLocationListeners()
                    }


                }
                .addOnFailureListener { exception ->
                    Log.i("LOCATION","GettingLocationFailed: $exception")
                }
        }
    }

    fun stopLocationService()
    {
        isRunning = false
        Log.i("LOCATION","Location Service Stopped")
        stopSelf()
    }

    override fun stopService(name: Intent?): Boolean {
        isRunning = false
        Log.i("LOCATION","Location Service Stopped")
        return super.stopService(name)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    fun setUpHashMaps()
    {
        var names = resources.getStringArray(R.array.update_location_interval)
        var values = resources.getIntArray(R.array.update_location_interval_values)

        for(i:Int in names.indices)
        {
            locationUpdateIntervalMap[names[i]]=values[i]
            locationUpdateIntervalOnFaleurMap[names[i]]=values[i]
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this)/*.all
        preferences.forEach {
            Log.i("Preferences", "${it.key} -> ${it.value}")
        }*/

        var temp = locationUpdateIntervalMap[preferences.getString("locationUpdateInterval","")]
        if(temp!=null)
        {
            waitOnSuccess = temp
            Log.i("PREFERENCES","locationUpdateInterval: $waitOnSuccess")
        }

        temp = locationUpdateIntervalMap[preferences.getString("locationUpdateIntervalOnFailure","")]
        if(temp!=null)
        {
            waitOnFaileur = temp
            Log.i("PREFERENCES","locationUpdateIntervalOnFailure: $waitOnFaileur")
        }
    }
}