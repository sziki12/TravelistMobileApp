package hu.bme.a

import hu.bme.aut.android.gyakorlas.ImageViewActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.gyakorlas.MapData.PlaceData
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

        place = this.intent.getParcelableExtra("PLACE",PlaceData::class.java)
        place?.images = this.intent.getParcelableArrayListExtra("IMAGES",Bitmap::class.java)!!
        binding.tvName.text = place?.name ?: ""
        binding.tvDescription.text = place?.description ?: ""
        var images:ArrayList<ImageButton> = ArrayList()
        images.add(binding.pictureButton1)
        images.add(binding.pictureButton2)
        images.add(binding.pictureButton3)


        if(place!=null)
        {


            //place!!.images.add(Drawable.createFromStream(assets.open("images/nyugati_barcraft02.jpg"), null))
            //place!!.images.add(Drawable.createFromStream(assets.open("images/nyugati_barcraft03.jpg"), null))
            var size = min(3, place!!.images.size)
            Log.i("IMAGES",size.toString())
            for(i in 0 until size)
            {
                images[i].setImageDrawable(resizeDrawable(place!!.images[i],600,600))

                //images[i].setImageDrawable(R.)
            }
        }

        for(image in images)
        {
            var index = 0

            image.setOnClickListener()
            {
                var intent = Intent(this, ImageViewActivity::class.java)
                intent.putExtra("PLACE",place)
                intent.putExtra("INDEX",index)
                startActivity(intent)
            }
            index++
        }

        //var options = BitmapFactory.Options()
        //options.inSampleSize = 2
       /* images[0].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft01)))
        images[1].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
        images[2].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft03)))*/

    }
    private fun resizeDrawable(bitmap:Bitmap?,dstWidth:Int,dstHeight:Int):Drawable?{

        var drawableOut: Drawable? = null
        if(bitmap!=null)
        {
            //var bitmap = (drawable as BitmapDrawable).bitmap
            drawableOut= BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true))
        }

        return drawableOut
    }

}