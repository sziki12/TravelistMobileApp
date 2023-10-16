package hu.bme.aut.android.gyakorlas

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.gyakorlas.databinding.FragmentMapsBinding
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.MapMarker

class MapsFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    var markers: ArrayList<MapMarker> = ArrayList()
    private var isInitialized = false
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

        setUpMapData("All")

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.imgbtnMenu.setOnClickListener {
            findNavController(this as Fragment).navigate(R.id.action_mapsFragment_to_menuFragment)
        }
    }



    /**
     * Contains the initiations of the markers ArrayList, and gets the current location
     */

    private fun setUpMapData(selectedLocation: String) {
        markers.clear()
        markers = MapDataProvider.getSelectedMarkers(selectedLocation)
        Log.i("PLACE","setUpMapData")
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
        for (mapMarker in markers) {
            mMap.addMarker(mapMarker.marker)
        }
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
//                    var intent = Intent(this, PlaceActivity::class.java)
//                    //intent.putParcelableArrayListExtra("IMAGES", mapMarker.place.images)
//                    intent.putExtra("PLACE", MapDataProvider.getIDByMarker(mapMarker))
//                    Log.i("PLACE","Place ID:${MapDataProvider.getIDByMarker(mapMarker)}")
//                    startActivity(intent)

                    val markerID = MapDataProvider.getIDByMarker(mapMarker)
                    Log.i("PLACE","Place ID:$markerID")
                    // create an action and pass the required user object to it
                    val action =
                        markerID?.let { MapsFragmentDirections.actionMapsFragmentToPlaceFragment(it) }//

                    //this will navigate the MapsFragment to the PlaceFragment
                    if (action != null) {
                        findNavController(this as Fragment).navigate(action)
                    }
                }
            }
        }
        mMap.setOnMapLongClickListener { latLang ->

            var isMarkerClicked = false
            for (marker in markers) {
                if (marker.getLatLng() == (latLang)) {
                    isMarkerClicked = true
                }
            }

            if (!isMarkerClicked) {
                var newMarker = MarkerOptions()
                    .position(latLang)
                    .title("New Marker By Click At: $latLang")
                    .icon(BitmapDescriptorFactory.defaultMarker())
                mMap.addMarker(newMarker)
            }
        }

        binding.btnPlaceNewMarker.setOnClickListener { view ->
            val latLang = mMap.cameraPosition.target
            var newMarker = MarkerOptions()
                .position(latLang)
                .title("New Marker By Button At: $latLang")
                .icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(true)
            mMap.addMarker(newMarker)
        }
        /*override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionHandler.permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionHandler.permissionDenied = false
        }
    }*/
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
    }