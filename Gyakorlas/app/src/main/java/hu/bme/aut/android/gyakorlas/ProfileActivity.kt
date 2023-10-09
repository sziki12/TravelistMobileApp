package hu.bme.aut.android.gyakorlas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.android.gyakorlas.databinding.ActivityMenuBinding
import hu.bme.aut.android.gyakorlas.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener()
        {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        binding.btnSave.setOnClickListener()
        {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        binding.imgbtnMenu.setOnClickListener()
        {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}