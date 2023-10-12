package hu.bme.aut.android.gyakorlas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hu.bme.aut.android.gyakorlas.databinding.ActivityMainBinding
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding : ActivityMainBinding
    private var geofenceHandler = GeofenceHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize PermissionHandler
        PermissionHandler.initialize()
        //Initialize Map Data, load MapMarkers
        MapDataProvider.initMarkers(this)
        geofenceHandler.setUpGeofencingClient(this)
        //Initialize Location Service
        LocationService.activity=this
        startService(Intent(this,LocationService::class.java))

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
