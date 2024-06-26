package hu.bme.aut.android.gyakorlas.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
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
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler

class MapsFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback,
    AdapterView.OnItemSelectedListener, MapDataProvider.MapDataChangedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    var markers: ArrayList<MapMarker> = ArrayList()
    private val mapDataProvider = MapDataProvider.instance
    private var isInitialized = false
    var locations = mapDataProvider.getLocations()
    var selectedLocation = locations[0]
    var shownMarkers = ArrayList<Marker?>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!isInitialized)
            binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapDataProvider.addListener(this)

        setUpMapData("All")

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.imgbtnMenu.setOnClickListener {
            findNavController(this as Fragment).navigate(R.id.action_mapsFragment_to_menuFragment)
        }

        binding.categorySpinner.onItemSelectedListener = this
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            locations
        )
        binding.categorySpinner.adapter = spinnerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapDataProvider.removeListener(this)
    }


    /**
     * Contains the initiations of the markers ArrayList, and gets the current location
     */

    private fun setUpMapData(selectedLocation: String) {
        markers.clear()

        markers.addAll(mapDataProvider.getSelectedMarkers(selectedLocation))
        Log.i("PLACE","setUpMapData markers.size: ${markers.size}")
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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

        //Show Markers
        showMarkers()
        //Set Map Center
        centerCamera()

        //Enable Gestures
        enableGestures()
        //Set MapCLickListeners
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        mMap.setOnMarkerClickListener { marker ->

            //marker.isDraggable=true
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            true
        }
        mMap.setOnInfoWindowClickListener { marker ->

            //Finds the appropriate MapMarker by its position
            for (mapMarker in markers) {
                if (mapMarker.isOwnMarker(marker) && mapMarker.place != null) {

                    val markerID = mapDataProvider.getIDByMarker(mapMarker)
                    Log.i("PLACE","Place ID:$markerID")
                    // create an action and pass the required object to it
                    val action =
                        markerID?.let {
                            hu.bme.aut.android.gyakorlas.fragments.MapsFragmentDirections.actionMapsFragmentToPlaceFragment(
                                it
                            )
                        }//

                    //this will navigate the MapsFragment to the PlaceFragment
                    if (action != null) {
                        findNavController(this as Fragment).navigate(action)
                    }
                }
            }
        }
    }

    private fun showMarkers()
    {
        for(marker in shownMarkers) {
            marker?.remove()
        }
        shownMarkers.clear()

        for(mapMarker in markers) {
            shownMarkers.add(mMap.addMarker(mapMarker.marker))
        }
    }
        /**
         * If we click on the GPS sign, which centers the camera around the device.
         */
        override fun onMyLocationButtonClick(): Boolean {
            Toast.makeText(this.context, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show()
            // Return false so that we don't consume the event and the default behavior still occurs
            // (the camera animates to the user's current position).
            return false
        }

        /**
         * If we click on the device location marker.
         */
        override fun onMyLocationClick(location: Location) {
            Toast.makeText(this.context, "Current location:\n$location", Toast.LENGTH_LONG)
                .show()
        }

        /**
         * Delegates the result to the PermissionHandler
         */
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            PermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
            var mapLocation: CameraPosition = CameraPosition.Builder()
                .target(mapCenter)
                .zoom(11.0f)
                .bearing(0f)
                .tilt(50f)
                .build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mapLocation))
        }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        selectedLocation = locations[position]
        setUpMapData(selectedLocation)
        showMarkers()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        selectedLocation = locations[0]
    }

    override fun onMapDataChanged(markers: List<MapMarker>) {
        this.markers.clear()
        this.markers.addAll(markers)
        showMarkers()
    }
}