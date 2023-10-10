package hu.bme.aut.android.gyakorlas

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import hu.bme.aut.android.gyakorlas.mapData.PlaceData
import hu.bme.aut.android.gyakorlas.databinding.ActivityImageViewBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider

class ImageViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityImageViewBinding
    private var place: PlaceData? = null
    private lateinit var mapDataProvider: MapDataProvider

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
           if(place!!.images.isNotEmpty())
           {
               binding.imageView.setImageDrawable(mapDataProvider.resizeDrawable(place!!.images[index],1000,1000))
               //binding.imageView.setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
               for(bitMap in place!!.images)
               {
                   var image = ImageButton(this)
                   image.setOnClickListener()
                   {
                       binding.imageView.setImageDrawable(mapDataProvider.resizeDrawable(bitMap,1000,1000))
                   }
                   //image.layoutParams.height = MATCH_PARENT
                   //image.layoutParams.height = 300
                   image.setImageDrawable(mapDataProvider.resizeDrawable(bitMap,300,300))
                   binding.linearLayout.addView(image)
               }
           }
            else{
                //TODO Ez valamiért nem jó
               //binding.imageView.setImageDrawable(mapDataProvider.resizeDrawable(BitmapFactory.decodeResource(resources,R.drawable.travelistlogo),1000,1000))
           }

        }
        //binding.scrollView.requestLayout()
        //binding.linearLayout.requestLayout()
        //binding.scrollView.requestLayout()
        Log.i("IMAGES","Scroll View Width: ${binding.scrollView.width}")
        Log.i("IMAGES","Scroll View Height: ${binding.scrollView.height}")

        Log.i("IMAGES","Root Width: ${binding.root.width}")
        Log.i("IMAGES","Root Height: ${binding.root.height}")
       /* images[0].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft01)))
        images[1].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
        images[2].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft03)))*/
    }
}