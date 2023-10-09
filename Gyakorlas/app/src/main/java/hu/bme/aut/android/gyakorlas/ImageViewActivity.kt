package hu.bme.aut.android.gyakorlas

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.annotation.RequiresApi
import hu.bme.aut.android.gyakorlas.MapData.PlaceData
import hu.bme.aut.android.gyakorlas.databinding.ActivityImageViewBinding

class ImageViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityImageViewBinding
    private var place: PlaceData? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        place = this.intent.getParcelableExtra("PLACE",PlaceData::class.java)
        var index = this.intent.getIntExtra("INDEX",0)



       if(place!=null)
        {
            //binding.imageView.setImageBitmap(place!!.images[index])
            //binding.imageView.setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
            for(bitMap in place!!.images)
            {
                //var image = binding.scrollImage01.
                //image.layoutParams.height = MATCH_PARENT
                //image.layoutParams.height = 300
                //image.setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft01)))
                //binding.linearLayout.addView(image)
            }
        }
       /* images[0].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft01)))
        images[1].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
        images[2].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft03)))*/
    }
}