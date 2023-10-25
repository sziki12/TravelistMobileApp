package hu.bme.aut.android.gyakorlas.location

import android.content.Intent
import android.util.Log
import hu.bme.aut.android.gyakorlas.PermissionHandler
import java.util.Timer
import java.util.TimerTask

class LocationThread(private var locationService: LocationService) : Thread(){

    init {
        isDaemon = true
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
            locationService.setUpListener()
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
            locationService.removeListener()
        }
    }
}