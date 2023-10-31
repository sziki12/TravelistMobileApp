package hu.bme.aut.android.gyakorlas.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

/**
 * Utility class for access to runtime permissions.
 */
class PermissionHandler(/*private var activity: Activity*/){
    companion object {
        private const val requestCodeBegin=1

        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val FINE_LOCATION_REQUEST_CODE = 2
        const val COARSE_LOCATION_REQUEST_CODE = 3
        const val BACKGROUND_LOCATION_REQUEST_CODE = 4
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 5
        const val CAMERA_ACCESS_REQUEST_CODE = 6

        private const val requestCodeEnd=4
        var hasPermission: HashMap<Int, Boolean?> = HashMap()
            get()
            {
                synchronized(field)
                {
                    return field
                }
            }
        private var successCallbacks: HashMap<Int, ArrayList<(() -> Unit)?>> = HashMap()
        private var falieurCallbacks: HashMap<Int, ArrayList<(() -> Unit)?>> = HashMap()
        private var isOnePermissionEnaught: HashMap<Int, Boolean> = HashMap()

        /**
         * Set the isOnePermissionEnaught flag and add the required permissions to the ArrayList
         */
        fun getPermissions(permissionCode: Int): ArrayList<String> {
            var permissionString: ArrayList<String> = ArrayList()
            when (permissionCode) {
                LOCATION_PERMISSION_REQUEST_CODE -> {
                    isOnePermissionEnaught[LOCATION_PERMISSION_REQUEST_CODE] = true
                    permissionString.add(Manifest.permission.ACCESS_FINE_LOCATION)
                    permissionString.add(Manifest.permission.ACCESS_COARSE_LOCATION)
                }

                BACKGROUND_LOCATION_REQUEST_CODE -> {
                    isOnePermissionEnaught[BACKGROUND_LOCATION_REQUEST_CODE] = true
                    permissionString.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }

                READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                    isOnePermissionEnaught[READ_EXTERNAL_STORAGE_REQUEST_CODE] = true
                    permissionString.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            return permissionString
        }

        fun checkPermissions(activity: Activity, permissionCode: Int): Boolean {
            var permissions = getPermissions(permissionCode)
            //getPermission() sets the isOnePermissionEnaught flag
            var granted = !isOnePermissionEnaught[permissionCode]!!

            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (isOnePermissionEnaught[permissionCode] == true)
                        granted = true
                } else {
                    if (isOnePermissionEnaught[permissionCode] == false) {
                        granted = false
                    }
                }
            }
            return granted
        }

        private fun showRationale(activity: Activity, permissionCode: Int) {
            for (permission in getPermissions(permissionCode)) {

                if (shouldShowRequestPermissionRationale(activity, permission)) {
                    when (permissionCode) {
                        LOCATION_PERMISSION_REQUEST_CODE -> activity.runOnUiThread {
                            Toast.makeText(
                                activity,
                                "You should grant permission to access your location to fully use the app",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        BACKGROUND_LOCATION_REQUEST_CODE -> activity.runOnUiThread {
                            Toast.makeText(
                                activity,
                                "You should grant permission to access your background location to use the Recommended For You feature",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        fun requestPermission(
            activity: Activity,
            permissionCode: Int,
            successCallback: (() -> Unit)? = null,
            falieurCallback: (() -> Unit)? = null
        ) {
            synchronized(hasPermission)
            {
                //successCallbacks[permissionCode]?.add(successCallback)
                successCallbacks[permissionCode] = ArrayList()
                successCallbacks[permissionCode]?.add(successCallback)
                falieurCallbacks[permissionCode]?.add(falieurCallback)

                when {
                    checkPermissions(activity, permissionCode) -> {
                        Log.i("PERMISSION", "Success")
                        hasPermission[permissionCode] = true
                        for (callback in successCallbacks[permissionCode]!!) {
                            if (callback != null) {
                                callback()
                            }
                            Log.i("PERMISSION", "Callback")
                            //successCallbacks[permissionCode]?.remove(callback)
                        }
                        successCallbacks[permissionCode]?.clear()
                    }

                    else -> {
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected, and what
                        // features are disabled if it's declined. In this UI, include a
                        // "cancel" or "no thanks" button that lets the user continue
                        // using your app without granting the permission.
                        // showInContextUI(...)
                        showRationale(activity, permissionCode)

                        Log.i("PERMISSION", "Request")
                        // You can directly ask for the permission.
                        requestPermissions(
                            activity,
                            getPermissions(permissionCode).toTypedArray(),
                            permissionCode
                        )
                    }
                }
            }
        }

        fun checkResult(permissionCode: Int, grantResults: IntArray): Boolean {
            var granted = !isOnePermissionEnaught[permissionCode]!!

            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    if (isOnePermissionEnaught[permissionCode] == true)
                        granted = true
                } else {
                    if (isOnePermissionEnaught[permissionCode] == false)
                        granted = false
                }
            }
            return granted;
        }

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
        ) {
            synchronized(hasPermission)
            {
                if (checkResult(requestCode, grantResults)) {
                    Log.i("PERMISSION", "Success")
                    hasPermission[requestCode] = true
                    if(falieurCallbacks[requestCode]!=null)
                    {
                        for (callback in successCallbacks[requestCode]!!) {
                            if (callback != null) {
                                callback()
                            }
                            Log.i("PERMISSION", "Callback")
                            //successCallbacks[requestCode]?.remove(callback)
                        }
                        successCallbacks.clear()
                    }
                } else {
                    Log.i("PERMISSION", "Denied")
                    hasPermission[requestCode] = false
                    if(falieurCallbacks[requestCode]!=null)
                    {
                        for (callback in falieurCallbacks[requestCode]!!) {
                            if (callback != null) {
                                callback()
                            }
                            Log.i("PERMISSION", "Callback")
                            //falieurCallbacks[requestCode]?.remove(callback)
                        }
                        falieurCallbacks.clear()
                    }
                }
            }
        }
        fun initialize()
        {
            synchronized(hasPermission)
            {
                for(i:Int in requestCodeBegin .. requestCodeEnd)
                {
                    hasPermission[i] = null
                    successCallbacks[i] = ArrayList()
                    falieurCallbacks[i] = ArrayList()
                }
            }
        }
    }
}