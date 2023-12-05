package hu.bme.aut.android.gyakorlas.fragments

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentMapsBinding
import hu.bme.aut.android.gyakorlas.databinding.FragmentSelectLocationMapBinding
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler

class SelectLocationMapFragment : Fragment(), GoogleMap.OnMarkerDragListener {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentSelectLocationMapBinding
    private var isInitialized = false
    private var selectedPosition:LatLng? = null
    private var marker:MarkerOptions? = null
    private val args : SelectLocationMapFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!isInitialized)
            binding = FragmentSelectLocationMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.selectLocationMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        selectedPosition = args.position
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        Log.i("PLACE","map ready callback")
        mMap = googleMap
        this.activity?.let {
            PermissionHandler.requestPermission(
                it,
                PermissionHandler.LOCATION_PERMISSION_REQUEST_CODE,
                {
                    mMap.isMyLocationEnabled = true
                    Log.i("PERMISSION", "Map Enabled")
                })
            {
                Log.i("PERMISSION", "Map Disabled")
            }
        }
        val pos = selectedPosition
        if(pos!=null)
        {
            marker = MarkerOptions()
                .position(pos)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(true)
            mMap.addMarker(marker!!)
            binding.btnPlaceMarker.isEnabled=false
        }

        //Set Map Center
        centerCamera()

        //Enable Gestures
        enableGestures()

        binding.btnPlaceMarker.setOnClickListener { view ->
            val latLang = mMap.cameraPosition.target
            val newMarker = MarkerOptions()
                .position(latLang)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(true)
            mMap.addMarker(newMarker)

            selectedPosition = newMarker.position
            marker = newMarker

            binding.btnPlaceMarker.isEnabled=false
            Log.i("position","Before: ${newMarker.position}")
        }

        binding.btnSaveMarker.setOnClickListener()
        {
            if(marker!=null)
            {
                Log.i("position","After: ${marker!!.position}")
                val action = SelectLocationMapFragmentDirections.actionSelectLocationMapFragmentToUploadNewPlaceFragment(selectedPosition)
                NavHostFragment.findNavController(this as Fragment).navigate(action)
            }
        }

        binding.btnDeleteMarker.setOnClickListener()
        {
            val action = SelectLocationMapFragmentDirections.actionSelectLocationMapFragmentToUploadNewPlaceFragment(null)
            NavHostFragment.findNavController(this as Fragment).navigate(action)
        }
        binding.imgbtnMenu.setOnClickListener(){
            findNavController().navigate(R.id.action_selectLocationMapFragment_to_menuFragment)
        }

        mMap.setOnMarkerDragListener(this)
    }

    private fun enableGestures() {
        if (mMap != null) {
            mMap.uiSettings.isScrollGesturesEnabled = true
            mMap.uiSettings.isRotateGesturesEnabled = true
            mMap.uiSettings.isZoomGesturesEnabled = true
            mMap.uiSettings.isTiltGesturesEnabled = true
        }
    }

    /**
     * Centers the camera around user's location or if its null, around Budapest
     */
    private fun centerCamera() {
        var mapCenter = LatLng(47.5, 19.05)
        if (LocationService.currentLocation != null) {
            mapCenter = LatLng(
                LocationService.currentLocation!!.latitude,
                LocationService.currentLocation!!.longitude
            )
        }
        val mapLocation: CameraPosition = CameraPosition.Builder()
            .target(mapCenter)
            .zoom(11.0f)
            .bearing(0f)
            .tilt(50f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mapLocation))
    }

    override fun onMarkerDrag(p0: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        selectedPosition = marker.position
        Log.i("position","AfterDrag: $selectedPosition")
        this.marker?.position(marker.position)
    }

    override fun onMarkerDragStart(p0: Marker) {
    }
}