package hu.bme.aut.android.gyakorlas

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

/**
 * Utility class for access to runtime permissions.
 */
class PermissionHandler(private var mapsActivity: MapsActivity){

    //var permissionDenied = true
    private lateinit var mMap:GoogleMap
    private var isOnePermissionEnaught:Boolean = true
    var hasPermission:HashMap<Int,Boolean?> = HashMap()

    companion object
    {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    init {
        hasPermission[LOCATION_PERMISSION_REQUEST_CODE] = null
    }


    fun setMap(googleMap: GoogleMap)
    {
        mMap = googleMap
    }

    /**
     * Set the isOnePermissionEnaught flag and add the required permissions to the ArrayList
     */
    fun getPermissions(permissionCode:Int): ArrayList<String>
    {
        var permissionString : ArrayList<String> = ArrayList()
        when(permissionCode)
        {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                isOnePermissionEnaught=true
                permissionString.add( Manifest.permission.ACCESS_FINE_LOCATION)
                permissionString.add( Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
        return permissionString
    }

    fun checkPermissions(permissionCode:Int):Boolean
    {
        var permissions = getPermissions(permissionCode)
        //getPermission() sets the isOnePermissionEnaught flag
        var granted = !isOnePermissionEnaught

        for(permission in permissions)
        {
            if(ContextCompat.checkSelfPermission(
                    mapsActivity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED)
            {
                if(isOnePermissionEnaught)
                    granted = true
            }
            else
            {
                if(!isOnePermissionEnaught)
                {
                    granted = false
                }
            }
        }
        return granted
    }

    private fun showRationale(permissionCode:Int) {
        for (permission in getPermissions(permissionCode)) {

            if (shouldShowRequestPermissionRationale(mapsActivity, permission)) {
                when(permissionCode)
                {
                    LOCATION_PERMISSION_REQUEST_CODE -> Toast.makeText(mapsActivity,"You should grant permission to access your location to fully use the app",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun requestPermission(permissionCode:Int)
    {
        when {
            checkPermissions(permissionCode) -> {
                // You can use the API that requires the permission.
                //performAction(...)
                when(permissionCode)
                {
                    LOCATION_PERMISSION_REQUEST_CODE -> {
                        hasPermission[LOCATION_PERMISSION_REQUEST_CODE]=true
                        enableMyLocation()
                    }
                }
                Log.i("PERMISSION","permissionGranted")
            }

             else -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                // showInContextUI(...)
                showRationale(permissionCode)

                 Log.i("PERMISSION","Request")
                // You can directly ask for the permission.
                requestPermissions(mapsActivity,
                    getPermissions(permissionCode).toTypedArray(),
                    permissionCode)
            }
        }
    }

    fun checkResult(grantResults: IntArray):Boolean
    {
        var granted = !isOnePermissionEnaught

        for(result in grantResults) {
            if (result==PackageManager.PERMISSION_GRANTED) {
                if (isOnePermissionEnaught)
                    granted = true
            }
            else
            {
                if (!isOnePermissionEnaught)
                    granted = false
            }
        }
        return granted;
    }

    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>, grantResults: IntArray) {
        Log.i("PERMISSION","IN")
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                Log.i("PERMISSION","Code Same")
                // If request is cancelled, the result arrays are empty.
                if (checkResult(grantResults)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Log.i("PERMISSION","permissionGranted")
                    hasPermission[LOCATION_PERMISSION_REQUEST_CODE]=true
                    enableMyLocation()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.i("PERMISSION","permissionDenied")
                    hasPermission[LOCATION_PERMISSION_REQUEST_CODE]=false
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        if(hasPermission[LOCATION_PERMISSION_REQUEST_CODE]==true)
        {
            mMap.isMyLocationEnabled = true
            Log.i("PERMISSION","Map Enabled")
        }
        else
        {
            Log.i("PERMISSION","Map Disabled")
        }
    }
}