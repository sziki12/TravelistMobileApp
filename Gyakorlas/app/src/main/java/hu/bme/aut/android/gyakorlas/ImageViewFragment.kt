package hu.bme.aut.android.gyakorlas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.gyakorlas.databinding.FragmentImageViewBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.PlaceData

class ImageViewFragment : Fragment() {
    private lateinit var binding : FragmentImageViewBinding
    private var place: PlaceData? = null

    private val args : ImageViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageViewBinding.inflate(inflater, container, false)

        place = MapDataProvider.getMarkerByID(args.markerID).place
        var index = args.index

        Log.i("IMAGES","selected Index: $index")

        if(place!=null)
        {
            if(place!!.images.isNotEmpty())
            {
                binding.imageView.setImageDrawable(MapDataProvider.resizeDrawable(this,place!!.images[index],1000,1000))
                for(bitMap in place!!.images)
                {
                    var image = ImageButton(this.context)
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

        return binding.root;
    }
}