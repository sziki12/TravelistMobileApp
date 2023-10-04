package hu.bme.aut.android.gyakorlas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.android.gyakorlas.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.barImageButton.setOnClickListener()
        {
            var intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("MARKERS", MapsActivity.BAR_MARKERS)
            startActivity(intent)
        }

        binding.restaurantImageButton.setOnClickListener()
        {
            var intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("MARKERS", MapsActivity.RESTAURANT_MARKERS)
            startActivity(intent)
        }
    }
}