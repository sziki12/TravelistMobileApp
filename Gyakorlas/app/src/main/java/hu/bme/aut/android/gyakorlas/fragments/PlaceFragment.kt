package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentPlaceBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.PlaceData

class PlaceFragment : Fragment() {
    var markerID: Int = 0
    var place: PlaceData? = null
    private val mapDataProvider = MapDataProvider.instance
    private lateinit var binding : FragmentPlaceBinding

    // get the arguments from the MapsFragment
    private val args : PlaceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)

        binding.imgbtnMenu.setOnClickListener {
            NavHostFragment.findNavController(this as Fragment).navigate(R.id.action_placeFragment_to_menuFragment)
        }
        //receive the arguments in a variable
        markerID = args.markerID
        place = mapDataProvider.getMarkerByID(markerID).place

        binding.placeCommentsButton.setOnClickListener()
        {
            val action =
                PlaceFragmentDirections.actionPlaceFragmentToCommentsFragment(
                    markerID
                )
            findNavController().navigate(action)
        }
        binding.tvName.text = place?.name ?: ""
        binding.tvDescription.text = place?.description ?: ""

        binding.placeRating.setIsIndicator(true)
        binding.placeRating.stepSize = 0.5f
        binding.placeRating.rating = place!!.rating
        Log.i("Place","Rating: ${place?.rating}")
        Log.i("Place","Rating: ${binding.placeRating.rating}")

        val images:ArrayList<ImageButton> = ArrayList()
        images.add(binding.pictureButton1)
        images.add(binding.pictureButton2)
        images.add(binding.pictureButton3)

        if(place!=null)
        {
            val size = Math.min(3, place!!.images.size)
            Log.i("IMAGES",size.toString())

            for(i in 0 until size)
            {
                if(i==0)
                {
                    images[i].setImageDrawable(this.activity?.let {
                        MapDataProvider.resizeBitmap(
                            it,place!!.images[i],800,800)
                    })
                }else
                {
                    images[i].setImageDrawable(this.activity?.let {
                        MapDataProvider.resizeBitmap(
                            it,place!!.images[i],600,600)
                    })
                }
            }
        }
        for((index, image) in images.withIndex())
        {
            image.setOnClickListener()
            {

                val action =
                    hu.bme.aut.android.gyakorlas.fragments.PlaceFragmentDirections.actionPlaceFragmentToImageViewFragment(
                        markerID,
                        index
                    )//
                findNavController().navigate(action)
            }
        }
        return binding.root;
    }
}