package hu.bme.aut.android.gyakorlas

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import hu.bme.aut.android.gyakorlas.MapData.PlaceData
import hu.bme.aut.android.gyakorlas.databinding.ActivityMenuBinding
import hu.bme.aut.android.gyakorlas.databinding.ActivityPlaceBinding

class PlaceActivity : AppCompatActivity() {
    var place: PlaceData? = null
    lateinit var binding: ActivityPlaceBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        place = this.intent.getSerializableExtra("PLACE",PlaceData::class.java)
        binding.tvName.text = place?.name ?: ""
        binding.tvDescription.text = place?.description ?: ""


    }
}