package hu.bme.aut.android.gyakorlas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import hu.bme.aut.android.gyakorlas.mapData.PlaceData
import hu.bme.aut.android.gyakorlas.databinding.ActivityImageViewBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider

class ImageViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityImageViewBinding
    private var place: PlaceData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        place = MapDataProvider.getMarkerByID(this.intent.getIntExtra("PLACE",0)).place
        var index = this.intent.getIntExtra("INDEX",0)

        Log.i("IMAGES","selected Index: $index")


       if(place!=null)
        {
           if(place!!.images.isNotEmpty())
           {
               binding.imageView.setImageDrawable(MapDataProvider.resizeDrawable(this,place!!.images[index],1000,1000))
               for(bitMap in place!!.images)
               {
                   var image = ImageButton(this)
                   image.setOnClickListener()
                   {
                       binding.imageView.setImageDrawable(MapDataProvider.resizeDrawable(this,bitMap,1000,1000))
                   }
                   image.setImageDrawable(MapDataProvider.resizeDrawable(this,bitMap,300,300))
                   binding.linearLayout.addView(image)
               }
           }
        }
        Log.i("IMAGES","Scroll View Width: ${binding.scrollView.width}")
        Log.i("IMAGES","Scroll View Height: ${binding.scrollView.height}")

        Log.i("IMAGES","Root Width: ${binding.root.width}")
        Log.i("IMAGES","Root Height: ${binding.root.height}")
    }
}