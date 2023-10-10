package hu.bme.aut.android.gyakorlas

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import hu.bme.aut.android.gyakorlas.mapData.PlaceData
import hu.bme.aut.android.gyakorlas.databinding.ActivityImageViewBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider

class ImageViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityImageViewBinding
    private var place: PlaceData? = null
    private lateinit var mapDataProvider: MapDataProvider
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapDataProvider = MapDataProvider(this)
        place = mapDataProvider.getMarkerByID(this.intent.getIntExtra("PLACE",0)).place
        //place = this.intent.getParcelableExtra("PLACE",PlaceData::class.java)
        var index = this.intent.getIntExtra("INDEX",0)

        Log.i("IMAGES","selected Index: $index")


       if(place!=null)
        {
            binding.imageView.setImageDrawable(mapDataProvider.resizeDrawable(place!!.images[index],1000,1000))
            //binding.imageView.setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
            for(bitMap in place!!.images)
            {
                var image = ImageView(this)
                //image.layoutParams.height = MATCH_PARENT
                //image.layoutParams.height = 300
                image.setImageDrawable(mapDataProvider.resizeDrawable(bitMap,200,200))
                binding.linearLayout.addView(image)
            }
        }
       /* images[0].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft01)))
        images[1].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
        images[2].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft03)))*/
    }
}