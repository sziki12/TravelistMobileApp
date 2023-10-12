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
        setSupportActionBar(binding.toolbar)

        binding.btnMenuProfile.setOnClickListener()
        {
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnMenuSearch.setOnClickListener()
        {
            var intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.btnTrackOthers.setOnClickListener()
        {
            var intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.imgbtnMap.setOnClickListener(){

        }
        binding.btnMenuRecommended.setOnClickListener()
        {
            var intent = Intent(this, RecommendedActivity::class.java)
            startActivity(intent)
        }

    }
}