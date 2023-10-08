package hu.bme.aut.android.gyakorlas

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import hu.bme.aut.android.gyakorlas.MapData.PlaceData
import hu.bme.aut.android.gyakorlas.databinding.ActivityMenuBinding
import hu.bme.aut.android.gyakorlas.databinding.ActivityPlaceBinding
import java.lang.Math.min

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
        var images:ArrayList<ImageButton> = ArrayList()
        images.add(binding.pictureButton1)
        images.add(binding.pictureButton2)
        images.add(binding.pictureButton3)
        if(place!=null)
        {
            var size = min(3, place!!.images.size)
            for(i in 0 until size)
            {
                images[i].setImageBitmap(place!!.images[i])
            }
        }



    }
}