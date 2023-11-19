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

class RecommendedFragment : Fragment(),LocationService.LocationChangeListener, GeofenceRadiusListener {
    private var geofenceHandler = GeofenceHandler()
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

        //Somehow the markers get deleted from MapDataProvider
        this.activity?.let { MapDataProvider.initMarkers(it) }

        markers = geofenceHandler.calculateNearbyMarkers()

        val customAdapter = PlaceAdapter(this,markers)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = customAdapter
        Log.i("GEOFENCE", "Shown")
        LocationService.registerListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocationService.unregisterListener(this)
    }

    override fun notifyOnLocationChange() {

        activity?.runOnUiThread {
            val customAdapter = PlaceAdapter(this,markers)
            val recyclerView: RecyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(this.activity)
            recyclerView.adapter = customAdapter
        }

    }



    override fun onGeofenceRadiusChanged() {
        activity?.runOnUiThread {
            markers = geofenceHandler.calculateNearbyMarkers()

            for (m in markers){
                Log.i("MARKERS", m.name)
            }

            val customAdapter = PlaceAdapter(this, markers)
            val recyclerView: RecyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(this.activity)
            recyclerView.adapter = customAdapter
        }
    }
}