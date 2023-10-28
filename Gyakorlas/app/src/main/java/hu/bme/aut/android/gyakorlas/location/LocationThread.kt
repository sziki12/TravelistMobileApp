package hu.bme.aut.android.gyakorlas.location

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import hu.bme.aut.android.gyakorlas.PermissionHandler
import hu.bme.aut.android.gyakorlas.R
import java.util.Timer
import java.util.TimerTask

class LocationThread(private var locationService: LocationService) : Thread(), SharedPreferences.OnSharedPreferenceChangeListener{

    init {
        isDaemon = true
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, s: String?) {
        Log.i("PREFERENCES","SharedPreferences Changed")
        Log.i("PREFERENCES","SharedPreferences: $s")

        if(sharedPreferences==null||s==null)
            return

        if(s.contains("locationUpdateIntervalOnFailure"))
        {
            var t = locationService.locationUpdateIntervalMap[sharedPreferences.getString("locationUpdateIntervalOnFailure","")]
            if(t!=null)
            {
                locationService.waitOnFaileur = t
            }
            Log.i("PREFERENCES","locationUpdateIntervalOnFailure: ${locationService.waitOnFaileur}")
        }

        if(s.contains("locationUpdateInterval"))
        {
            var t = locationService.locationUpdateIntervalMap[sharedPreferences.getString("locationUpdateInterval","")]
            if(t!=null)
            {
                locationService.waitOnSuccess = t
            }
            Log.i("PREFERENCES","locationUpdateInterval: ${locationService.waitOnSuccess}")
        }

        if(s.contains("locationAccuracy"))
        {
            var t = sharedPreferences.getString("locationAccuracy","")
            var names = locationService.resources.getStringArray(R.array.location_accuray)
            var values = locationService.resources.getIntArray(R.array.location_accuray_values)
            for(i:Int in names.indices)
            {
                if(names[i].equals(t))
                {
                    locationService.useHighAccuracy = (values[i]==1)
                }
            }
            Log.i("PREFERENCES","useHighAccuracy: ${locationService.useHighAccuracy}")
        }
    }
    class UpdateTask(private var locationService: LocationService) : TimerTask()
    {
        override fun run() {
            if (PermissionHandler.hasPermission[PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE] == true)
            {
                if(PermissionHandler.hasPermission[PermissionHandler.BACKGROUND_LOCATION_REQUEST_CODE] == true)
                {
                    Log.i("PERMISSION", "LocationService RUNNING")
                    locationService.updateCurrentLocation(locationService.useHighAccuracy)
                    locationService.isSuccess = true
                } else
                {
                    Log.i("PERMISSION","Background Location Access Denied")
                    locationService.isSuccess = false
                }
            } else
            {
                Log.i("PERMISSION","Location Access Denied")
                locationService.isSuccess = false
            }
        }
    }
    override fun run() {
        super.run()
        var timer = Timer()

        while(locationService.isRunning)
        {
            locationService.setUpHashMaps()
            locationService.preferences.registerOnSharedPreferenceChangeListener(this)
            if(locationService.isSuccess)
            {
                if(locationService.waitOnSuccess==0)
                    locationService.stopService(Intent())
                else
                {
                    timer.schedule(UpdateTask(locationService),locationService.waitOnSuccess.toLong())
                    sleep(locationService.waitOnSuccess.toLong())
                }
            }
            else
            {
                if(locationService.waitOnFaileur==0)
                    locationService.stopService(Intent())
                else
                {
                    timer.schedule(UpdateTask(locationService),locationService.waitOnFaileur.toLong())
                    sleep(locationService.waitOnFaileur.toLong())
                }
            }
            locationService.preferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }
}