package hu.bme.aut.android.gyakorlas

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.preference.PreferenceManager
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

    private var callbacks: ArrayList<()->Unit> = ArrayList()
    private var useHighAccuracy = false
    private lateinit var preferences:SharedPreferences
    private var locationUpdateIntervalMap:HashMap<String,Int> = HashMap()
    private var locationUpdateIntervalOnFaleurMap:HashMap<String,Int> = HashMap()
    private var updateThread :Thread? = null
    companion object
    {
        @Volatile
        var currentLocation: Location? = null
        @Volatile
        var waitOnFaileur:Int = 2000
        @Volatile
        var waitOnSuccess:Int = 5000
        @Volatile
        var isRunning:Boolean = true

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


        loadPreferences()
        updateThread = thread(start=true, isDaemon = true)
        {
            while (isRunning)
            {
                if (PermissionHandler.hasPermission[PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE] == true)
                {
                    if(PermissionHandler.hasPermission[PermissionHandler.BACKGROUND_LOCATION_REQUEST_CODE] == true)
                    {
                        Log.i("PERMISSION", "LocationService RUNNING")
                            updateCurrentLocation(useHighAccuracy)
                        if(waitOnSuccess==0)
                            stopService(Intent())
                        else
                            Thread.sleep(waitOnSuccess.toLong())

                    } else
                    {
                        Log.i("PERMISSION","Background Location Access Denied")
                        if(waitOnFaileur==0)
                            stopService(Intent())
                        else
                            Thread.sleep(waitOnFaileur.toLong())
                    }
                } else
                {
                    Log.i("PERMISSION","Location Access Denied")
                    if(waitOnFaileur==0)
                        stopService(Intent())
                    else
                        Thread.sleep(waitOnFaileur.toLong())
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
        Log.i("LOCATION","Location Service Stopped")
        return super.stopService(name)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun loadPreferences()
    {
        setUpHashMaps()
        Log.i("LOCATION","SharedPreferences Callback Setup")

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


        preferences.registerOnSharedPreferenceChangeListener()
        { sharedPreferences, s ->
            Log.i("PREFERENCES","SharedPreferences Changed")
            Log.i("PREFERENCES","SharedPreferences: $s")

            if(s.contains("locationUpdateIntervalOnFailure"))
            {
                var t = locationUpdateIntervalMap[sharedPreferences.getString("locationUpdateIntervalOnFailure","")]
                if(t!=null)
                {
                    waitOnFaileur = t
                }
                Log.i("PREFERENCES","locationUpdateIntervalOnFailure: $waitOnFaileur")
            }

            if(s.contains("locationUpdateInterval"))
            {
                var t = locationUpdateIntervalMap[sharedPreferences.getString("locationUpdateInterval","")]
                if(t!=null)
                {
                    waitOnSuccess = t
                }
                Log.i("PREFERENCES","locationUpdateInterval: $waitOnSuccess")
            }
            /*if(updateThread!=null&&updateThread!!.state.equals(Thread.State.WAITING))
            {
                updateThread!!.interrupt()
            }*/
        }
    }

    private fun setUpHashMaps()
    {
        var names = resources.getStringArray(R.array.update_location_interval)
        var values = resources.getIntArray(R.array.update_location_interval_values)

        for(i:Int in names.indices)
        {
            locationUpdateIntervalMap[names[i]]=values[i]
            locationUpdateIntervalOnFaleurMap[names[i]]=values[i]
        }
    }
}