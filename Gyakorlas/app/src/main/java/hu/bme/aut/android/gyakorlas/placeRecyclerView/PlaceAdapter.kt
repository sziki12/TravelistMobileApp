package hu.bme.aut.android.gyakorlas.placeRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView.*
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.fragments.RecommendedFragmentDirections
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import java.lang.Integer.min

class PlaceAdapter(private var fragment: Fragment, var markers: ArrayList<MapMarker>) : Adapter<PlaceViewHolder>() {
    private var mapDataProvider = MapDataProvider.instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)

        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    override fun onBindViewHolder(viewHolder: PlaceViewHolder, position: Int) {
        var images:ArrayList<ImageButton> = ArrayList()
        images.add( viewHolder.imageButton1)
        images.add( viewHolder.imageButton2)
        images.add( viewHolder.imageButton3)

        var id = mapDataProvider.getIDByMarker(markers[position])

        viewHolder.titleView.text = markers[position].place?.name
        for(i in 0 until min(3,markers[position].place?.images?.size!!))
        {
          images[i].setImageDrawable(fragment.activity?.let { MapDataProvider.resizeBitmap(it,markers[position].place?.images?.get(i),400,400) })
            images[i].setOnClickListener()
            {
                // create an action and pass the required object to it
                val action = id?.let { RecommendedFragmentDirections.actionRecommendedFragmentToImageViewFragment(it,i)}//
                //this will navigate the MapsFragment to the PlaceFragment
                if (action != null) {
                    NavHostFragment.findNavController(fragment).navigate(action)
                }
            }
        }

        if(markers[position].place?.images?.isNotEmpty() == true)
            viewHolder.ratingView.rating = markers[position].place?.rating!!

        var distance:Float? = LocationService.calculateDistance(markers[position].getLatLng())
        if(distance!=null)
            viewHolder.distanceText.text = "Distance: ${Math.round(distance)} m"
        else
            viewHolder.distanceText.text = ""

        viewHolder.detailesButton.setOnClickListener()
        {
            // create an action and pass the required object to it
            val action =
                mapDataProvider.getIDByMarker(markers[position])?.let { RecommendedFragmentDirections.actionRecommendedFragmentToPlaceFragment(it) }//
            //this will navigate the MapsFragment to the PlaceFragment
            if (action != null) {
                NavHostFragment.findNavController(fragment).navigate(action)
            }
        }
    }

    fun update(markers: ArrayList<MapMarker>) {
        this.markers=markers
        notifyDataSetChanged()
    }
}