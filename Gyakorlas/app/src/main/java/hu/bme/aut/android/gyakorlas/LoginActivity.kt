package hu.bme.aut.android.gyakorlas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hu.bme.aut.android.gyakorlas.databinding.ActivityLoginBinding
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider

class LoginActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding : ActivityLoginBinding
    private var geofenceHandler = GeofenceHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize PermissionHandler
        PermissionHandler.initialize()

        //Initialize Map Data, load MapMarkers
        MapDataProvider.initMarkers(this)

        //Initialize Location Service
        PermissionHandler.requestPermission(this,PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE,
            {
                PermissionHandler.requestPermission(this, PermissionHandler.BACKGROUND_LOCATION_REQUEST_CODE,
                    {
                        Log.i("PERMISSION", "LocationService Starting")
                        startService(Intent(this, LocationService::class.java))
                    })
                {Log.i("PERMISSION", " FAIL LocationService Starting")}
            })
        { Log.i("PERMISSION", " FAIL LocationService Starting")}

        geofenceHandler.setUpGeofencingClient(this)

        binding.btnLogin.setOnClickListener {
            if (binding.etUsername.text.toString().isEmpty()){
                binding.etUsername.requestFocus()
                binding.etUsername.error = "Please enter your username"
            }
            else if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.requestFocus()
                binding.etPassword.error = "Please enter your password"
            }
            else {
                startActivity(Intent(this, MenuActivity::class.java))
            }
        }

        binding.btnSignUp.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
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
