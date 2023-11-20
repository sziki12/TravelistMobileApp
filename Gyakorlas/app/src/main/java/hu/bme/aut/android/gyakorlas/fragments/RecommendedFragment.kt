package hu.bme.aut.android.gyakorlas.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gyakorlas.GeofenceRadiusListener
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentRecommendedBinding
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.recyclerView.PlaceAdapter
import kotlin.concurrent.thread

class RecommendedFragment : Fragment(),LocationService.LocationChangeListener, GeofenceRadiusListener,GeofenceHandler.GeofenceChangeListener {
    private var geofenceHandler = GeofenceHandler.instance
    private lateinit var binding: FragmentRecommendedBinding
    companion object {
        var markers: ArrayList<MapMarker> = ArrayList()
    }

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

        binding.tvRadius.text = "Search Distance: ${GeofenceHandler.geofenceRadius}"
        binding.imgbtnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_recommendedFragment_to_menuFragment)
        }

        binding.btnModifyGeofenceRadius.setOnClickListener(){
            val dialogFragment = GeofenceRadiusDialogFragment()
            dialogFragment.listener = this
            dialogFragment.show(
                childFragmentManager,
                GeofenceRadiusDialogFragment.TAG
            )
        }

        markers = geofenceHandler.calculateNearbyMarkers()

        val customAdapter = PlaceAdapter(this,markers)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = customAdapter
        Log.i("GEOFENCE", "Shown")
        LocationService.registerListener(this)
        GeofenceHandler.registerListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocationService.unregisterListener(this)
        GeofenceHandler.unregisterListener(this)
    }

    override fun notifyOnLocationChange() {
        thread{
            markers = geofenceHandler.calculateNearbyMarkers()
            activity?.runOnUiThread {
                val recyclerView: RecyclerView = binding.recyclerView
                val adapter = recyclerView.adapter as? PlaceAdapter
                adapter?.update(markers)
            }
        }
    }



    override fun onGeofenceRadiusChanged() {
        val locationService = LocationService.instance
        //Gyorsabb kivéve ha a Geofence Handler lassabb mint a LocationService
        locationService?.updateCurrentLocation(locationService.useHighAccuracy)
        notifyOnLocationChange()
        binding.tvRadius.text = "Search Distance: ${GeofenceHandler.geofenceRadius}"
        for (m in markers){
            Log.i("MARKERS", m.name)
        }
    }

    override fun onGeofenceChange() {
        notifyOnLocationChange()
    }
}