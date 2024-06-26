package hu.bme.aut.android.gyakorlas
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.databinding.ActivityMainBinding
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding : ActivityMainBinding
    private var geofenceHandler = GeofenceHandler.instance
    private var mapDataProvider = MapDataProvider.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize PermissionHandler
        PermissionHandler.initialize()

        //Initialize Map Data, load MapMarkers
        mapDataProvider.updateMarkers()

        //Initialize Location Service
        PermissionHandler.requestPermission(this, PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE,
            {
                PermissionHandler.requestPermission(this, PermissionHandler.BACKGROUND_LOCATION_REQUEST_CODE,
                    {
                        Log.i("PERMISSION", "LocationService Starting")
                        startService(Intent(this, LocationService::class.java))
                    })
                { Log.i("PERMISSION", " FAIL LocationService Starting")}
            })
        { Log.i("PERMISSION", " FAIL LocationService Starting")}

        geofenceHandler.setUpGeofencingClient(this)
    }

    /**
     * Delegates the result to the PermissionHandler
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults)
        PermissionHandler.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }
}

/**
 * Returns the email and the username of the current user or empty strings
 */
fun Activity.getCurrentUser(): Pair<String,String>
{
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val emailUsernameSP = this.getSharedPreferences("user_data", android.content.Context.MODE_PRIVATE)
    val lastSavedEmail = sharedPreferences.getString("lastSavedEmail", "")?:""
    var  username = emailUsernameSP.getString(lastSavedEmail, "")?:""
    if(username == "")
    {
        username = lastSavedEmail
    }
    return Pair(lastSavedEmail,username)
}