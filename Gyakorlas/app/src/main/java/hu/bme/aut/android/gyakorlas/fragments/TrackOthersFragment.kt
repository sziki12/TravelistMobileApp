package hu.bme.aut.android.gyakorlas.fragments

import android.annotation.SuppressLint
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.databinding.FragmentTrackOthersBinding
import hu.bme.aut.android.gyakorlas.location.LocationData
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.permission.PermissionHandler
import java.lang.StringBuilder

class TrackOthersFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentTrackOthersBinding
    private var isInitialized = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!isInitialized)
            binding = FragmentTrackOthersBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMapData("All")

        val mapFragment = childFragmentManager.findFragmentById(R.id.trackOthersMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.imgbtnMenu.setOnClickListener {
            findNavController(this as Fragment).navigate(R.id.action_trackOthersFragment_to_menuFragment)
        }
    }

    /**
     * Contains the initiations of the userLocations ArrayList
     */

    private fun setUpMapData(selectedLocation: String) {
        this.activity?.let { LocationData.initUsers() }
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

        val userLocations = LocationData.userLocations
        for (user in userLocations){
            val userLatLng = LatLng(user.latitude, user.longitude)

            //Distance between current location and userLatLng in meters:
            val distance = LocationService.calculateDistance(userLatLng)

            if (distance != null) {
                //if user is within 5 km, it is shown on the map
                if (distance <= 5000) {
                    val markerOptions = MarkerOptions().position(userLatLng).title("User")
                    mMap.addMarker(markerOptions)
                }
            }
        }
        //Adding own location marker:
        val selfLatLng = LocationService.currentLocation?.latitude?.let { LocationService.currentLocation?.longitude?.let { it1 ->
            LatLng(it,
                it1
            )
        } }
        val markerOptions =
            selfLatLng?.let { MarkerOptions().position(it).title("You are standing here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)) }
        if (markerOptions != null) {
            mMap.addMarker(markerOptions)
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