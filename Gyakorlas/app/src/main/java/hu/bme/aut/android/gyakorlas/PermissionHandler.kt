package hu.bme.aut.android.gyakorlas

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler

/**
 * Utility class for access to runtime permissions.
 */
class PermissionHandler(/*private var activity: Activity*/){

    //var permissionDenied = true
    //var mMap:GoogleMap? = null
    //private var geofenceHandler = GeofenceHandler()
    //var locationClient: FusedLocationProviderClient? = null



    companion object
    {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val BACKGROUND_LOCATION_REQUEST_CODE = 2
        var hasPermission:HashMap<Int,Boolean?> = HashMap()
        private var successCallbacks : HashMap<Int,ArrayList<(()->Unit)?>> = HashMap()
        private var falieurCallbacks : HashMap<Int,ArrayList<(()->Unit)?>> = HashMap()
        private var isOnePermissionEnaught:HashMap<Int,Boolean> = HashMap()

        /**
         * Set the isOnePermissionEnaught flag and add the required permissions to the ArrayList
         */
        fun getPermissions(permissionCode:Int): ArrayList<String>
        {
            var permissionString : ArrayList<String> = ArrayList()
            when(permissionCode)
            {
                LOCATION_PERMISSION_REQUEST_CODE -> {
                    isOnePermissionEnaught[LOCATION_PERMISSION_REQUEST_CODE]=true
                    permissionString.add( Manifest.permission.ACCESS_FINE_LOCATION)
                    permissionString.add( Manifest.permission.ACCESS_COARSE_LOCATION)
                }
                BACKGROUND_LOCATION_REQUEST_CODE ->
                {
                    isOnePermissionEnaught[BACKGROUND_LOCATION_REQUEST_CODE]=true
                    permissionString.add( Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
            return permissionString
        }

        fun checkPermissions(activity: Activity,permissionCode:Int):Boolean
        {
            var permissions = getPermissions(permissionCode)
            //getPermission() sets the isOnePermissionEnaught flag
            var granted = !isOnePermissionEnaught[permissionCode]!!

            for(permission in permissions)
            {
                if(ContextCompat.checkSelfPermission(
                        activity,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED)
                {
                    if(isOnePermissionEnaught[permissionCode]==true)
                        granted = true
                }
                else
                {
                    if(isOnePermissionEnaught[permissionCode]==false)
                    {
                        granted = false
                    }
                }
            }
            return granted
        }

        private fun showRationale(activity: Activity,permissionCode:Int) {
            for (permission in getPermissions(permissionCode)) {

                if (shouldShowRequestPermissionRationale(activity, permission)) {
                    when(permissionCode)
                    {
                        LOCATION_PERMISSION_REQUEST_CODE -> Toast.makeText(activity,"You should grant permission to access your location to fully use the app",Toast.LENGTH_SHORT).show()

                        BACKGROUND_LOCATION_REQUEST_CODE ->Toast.makeText(activity,"You should grant permission to access your background location to use the Recommended For You feature",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        fun requestPermission(activity: Activity,permissionCode:Int,successCallback:(()->Unit)? = null,falieurCallback:(()->Unit)? = null)
        {
            successCallbacks[permissionCode]?.add(successCallback)
            falieurCallbacks[permissionCode]?.add(falieurCallback)
            when {
                checkPermissions(activity,permissionCode) -> {
                    // You can use the API that requires the permission.
                    //performAction(...)
                    /*when(permissionCode)
                    {
                        LOCATION_PERMISSION_REQUEST_CODE -> {
                            hasPermission[LOCATION_PERMISSION_REQUEST_CODE]=true
                            //enableMyLocation()
                            Log.i("PERMISSION"," LOCATION_PERMISSION_REQUEST_CODE permissionGranted")
                        }

                        BACKGROUND_LOCATION_REQUEST_CODE ->
                        {

                            hasPermission[BACKGROUND_LOCATION_REQUEST_CODE]=true
                            //enableGeofencing()
                            Log.i("PERMISSION"," BACKGROUND_LOCATION_REQUEST_CODE permissionGranted")
                        }
                    }*/
                    Log.i("PERMISSION","Success")
                    hasPermission[permissionCode]=true
                    for(callback in successCallbacks[permissionCode]!!)
                    {
                        if (callback != null) {
                            callback()
                        }
                        Log.i("PERMISSION","Callback")
                        successCallbacks[permissionCode]?.remove(callback)
                    }
                }

                else -> {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected, and what
                    // features are disabled if it's declined. In this UI, include a
                    // "cancel" or "no thanks" button that lets the user continue
                    // using your app without granting the permission.
                    // showInContextUI(...)
                    showRationale(activity,permissionCode)

                    Log.i("PERMISSION","Request")
                    // You can directly ask for the permission.
                    requestPermissions(activity,
                        getPermissions(permissionCode).toTypedArray(),
                        permissionCode)
                }
            }
        }

        fun checkResult(permissionCode: Int,grantResults: IntArray):Boolean
        {
            var granted = !isOnePermissionEnaught[permissionCode]!!

            for(result in grantResults) {
                if (result==PackageManager.PERMISSION_GRANTED) {
                    if (isOnePermissionEnaught[permissionCode]==true)
                        granted = true
                }
                else
                {
                    if (isOnePermissionEnaught[permissionCode]==false)
                        granted = false
                }
            }
            return granted;
        }

        fun onRequestPermissionsResult(requestCode: Int,
                                       permissions: Array<String>, grantResults: IntArray) {
            if(checkResult(requestCode,grantResults))
            {
                Log.i("PERMISSION","Success")
                hasPermission[requestCode]=true
                for(callback in successCallbacks[requestCode]!!)
                {
                    if (callback != null) {
                        callback()
                    }
                    Log.i("PERMISSION","Callback")
                    successCallbacks[requestCode]?.remove(callback)
                }
            }
            else
            {
                Log.i("PERMISSION","Denied")
                hasPermission[requestCode]=false
                for(callback in falieurCallbacks[requestCode]!!)
                {
                    if (callback != null) {
                        callback()
                    }
                    Log.i("PERMISSION","Callback")
                    falieurCallbacks[requestCode]?.remove(callback)
                }
            }
    }
    init {
        hasPermission[LOCATION_PERMISSION_REQUEST_CODE] = null
        hasPermission[BACKGROUND_LOCATION_REQUEST_CODE] = null

        successCallbacks[LOCATION_PERMISSION_REQUEST_CODE] = ArrayList()
        successCallbacks[BACKGROUND_LOCATION_REQUEST_CODE] = ArrayList()

        falieurCallbacks[LOCATION_PERMISSION_REQUEST_CODE] = ArrayList()
        falieurCallbacks[BACKGROUND_LOCATION_REQUEST_CODE] = ArrayList()

        //geofenceHandler.setUpGeofencingClient(activity)
    }



        //Log.i("PERMISSION","IN")
       /* when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                Log.i("PERMISSION","Code Same")
                // If request is cancelled, the result arrays are empty.
                if (checkResult(grantResults)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Log.i("PERMISSION","LOCATION_PERMISSION_REQUEST_CODE permissionGranted")
                    hasPermission[LOCATION_PERMISSION_REQUEST_CODE]=true
                   // enableMyLocation()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.i("PERMISSION","LOCATION_PERMISSION_REQUEST_CODE permissionDenied")
                    hasPermission[LOCATION_PERMISSION_REQUEST_CODE]=false
                }
                return
            }
            BACKGROUND_LOCATION_REQUEST_CODE ->
            {
                if (checkResult(grantResults)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Log.i("PERMISSION","BACKGROUND_LOCATION_REQUEST_CODE permissionGranted")
                    hasPermission[BACKGROUND_LOCATION_REQUEST_CODE]=true
                    //enableGeofencing()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.i("PERMISSION","BACKGROUND_LOCATION_REQUEST_CODE permissionDenied")
                    hasPermission[BACKGROUND_LOCATION_REQUEST_CODE]=false
                }
                return
            }*/

            // Add other 'when' lines to check for other
            // permissions this app might request.
            //else -> {
                // Ignore all other requests.
            //}
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
       // @SuppressLint("MissingPermission")
    /*fun enableMyLocation() {
        if(hasPermission[LOCATION_PERMISSION_REQUEST_CODE]==true)
        {
            mMap?.isMyLocationEnabled = true
            locationClient?.lastLocation?.addOnSuccessListener { location : Location? ->
                    MapsActivity.currentLocation = location
                }
            requestPermission(BACKGROUND_LOCATION_REQUEST_CODE)

        }
        else
        {

        }
    }

    fun enableGeofencing()
    {
        if(hasPermission[LOCATION_PERMISSION_REQUEST_CODE]==true&&hasPermission[BACKGROUND_LOCATION_REQUEST_CODE]==true)
        {
            geofenceHandler.setUpGeofences(mapsActivity.markers)
            Log.i("PERMISSION","Geofence enabled")
        }
        else
        {
            Log.i("PERMISSION","Geofence disabled")
        }
    }*/
}