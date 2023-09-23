package hu.bme.aut.android.gyakorlas

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hu.bme.aut.android.gyakorlas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.barImageButton.setOnClickListener()
        {
            var intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("MARKERS",MapsActivity.BAR_MARKERS)
            startActivity(intent)
        }

        binding.restaurantImageButton.setOnClickListener()
        {
            var intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("MARKERS",MapsActivity.RESTAURANT_MARKERS)
            startActivity(intent)
        }



    }
}
