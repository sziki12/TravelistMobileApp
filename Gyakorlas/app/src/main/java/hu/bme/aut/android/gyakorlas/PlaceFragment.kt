package hu.bme.aut.android.gyakorlas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.gyakorlas.databinding.FragmentPlaceBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.PlaceData

class PlaceFragment : Fragment() {
    var markerID: Int = 0
    var place: PlaceData? = null
    private lateinit var binding : FragmentPlaceBinding

    // get the arguments from the MapsFragment
    private val args : PlaceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)

        //receive the arguments in a variable
        markerID = args.markerID
        place = MapDataProvider.getMarkerByID(markerID).place

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
            var size = Math.min(3, place!!.images.size)
            Log.i("IMAGES",size.toString())

            for(i in 0 until size)
            {
                if(i==0)
                {
                    images[i].setImageDrawable(MapDataProvider.resizeDrawable(this,place!!.images[i],800,800))
                }else
                {
                    images[i].setImageDrawable(MapDataProvider.resizeDrawable(this,place!!.images[i],600,600))
                }


                //images[i].setImageDrawable(R.)
            }
        }
        for((index, image) in images.withIndex())
        {
            image.setOnClickListener()
            {
//                var intent = Intent(this, ImageViewActivity::class.java)
//                intent.putExtra("PLACE",markerID)
//                intent.putExtra("INDEX",index)
//                startActivity(intent)

                val action = PlaceFragmentDirections.actionPlaceFragmentToImageViewFragment(markerID, index)
                findNavController().navigate(action)
            }
        }

        //var options = BitmapFactory.Options()
        //options.inSampleSize = 2
        /* images[0].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft01)))
         images[1].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft02)))
         images[2].setImageBitmap(BitmapFactory.decodeStream(resources.openRawResource(R.raw.nyugati_barcraft03)))*/



        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}