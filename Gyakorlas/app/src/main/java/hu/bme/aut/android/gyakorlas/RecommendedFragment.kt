package hu.bme.aut.android.gyakorlas

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gyakorlas.databinding.FragmentRecommendedBinding
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.recyclerView.PlaceAdapter

class RecommendedFragment : Fragment() {
    private var markers: ArrayList<MapMarker> = ArrayList()
    private var geofenceHandler = GeofenceHandler()
    private lateinit var binding: FragmentRecommendedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendedBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Somehow the markers get deleted from MapDataProvider
        this.activity?.let { MapDataProvider.initMarkers(it) }

        markers = geofenceHandler.calculateNearbyMarkers()

        val customAdapter = PlaceAdapter(this,markers)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = customAdapter
        Log.i("GEOFENCE", "markers size: ${markers.size}")
        /*
            for (marker in markers) {
                var markerLocaton = Location("Provider")
                markerLocaton.latitude = marker.lat
                markerLocaton.longitude = marker.lng
                val results = FloatArray(1)
                Location.distanceBetween(
                    markerLocaton.latitude,
                    markerLocaton.longitude,
                    LocationService.currentLocation!!.latitude,
                    LocationService.currentLocation!!.longitude,
                    results
                )
                var textView = TextView(this.context)
                textView.text = "${marker.name} distance: ${Math.round(results[0])}m"
        }*/
        Log.i("GEOFENCE", "Shown")

    }

    override fun onResume() {
        super.onResume()

    }
}