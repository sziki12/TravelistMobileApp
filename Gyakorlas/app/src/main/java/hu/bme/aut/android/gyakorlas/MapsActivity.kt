package hu.bme.aut.android.gyakorlas



import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.gyakorlas.databinding.ActivityMapsBinding
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.CancellationTokenSource
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.PermissionHandler.Companion.LOCATION_PERMISSION_REQUEST_CODE
import hu.bme.aut.android.gyakorlas.mapData.MapMarker


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,ActivityCompat.OnRequestPermissionsResultCallback
    {
        private lateinit var  mMap: GoogleMap
        private var permissionHandler: PermissionHandler = PermissionHandler(this)
        private lateinit var binding: ActivityMapsBinding
        private var markers: ArrayList<MapMarker> = ArrayList()
        private lateinit var mapDataProvider:MapDataProvider
        private lateinit var locationClient: FusedLocationProviderClient
        var currentLocation:Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        //Layout
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Location
        locationClient = LocationServices.getFusedLocationProviderClient(this)

        //Map Data
        mapDataProvider = MapDataProvider(this)
        setUpMapData("All")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.imgbtnMenu.setOnClickListener()
        {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Contains the initiations of the markers ArrayList, and gets the current location
     */

    private fun setUpMapData(selectedLocation: String)
    {
        markers.clear()
        markers = mapDataProvider.getSelectedMarkers(selectedLocation)
    }

    /**
    * Updates the current location, needs location permission and if center camera is true than
    * mMap needs to be initialized
    */
    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation(highAccuracyRequired:Boolean,centerCamera:Boolean)
    {
        var priority = Priority.PRIORITY_HIGH_ACCURACY
        if(!highAccuracyRequired)
            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()
        if(permissionHandler.hasPermission[LOCATION_PERMISSION_REQUEST_CODE]==true)
        {
            locationClient!!.getCurrentLocation(priority, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                currentLocation=location

                if(centerCamera)
                {
                    centerCamera()
                }
                Log.i("LOCATION","GettingLocationSuccessful: $currentLocation")
        }
            .addOnFailureListener { exception ->
                Log.i("LOCATION","GettingLocationFailed: $exception")
            }
        }
    }




    private fun enableGestures()
    {
        if(mMap!=null)
        {
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        permissionHandler.mMap = mMap
        permissionHandler.requestPermission(LOCATION_PERMISSION_REQUEST_CODE)
        updateCurrentLocation(highAccuracyRequired = true, centerCamera = true)

        //Show Markers
        for(mapMarker in markers)
        {
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
        mMap.setOnInfoWindowClickListener {marker ->

            //Finds the appropriate MapMarker by its position
            for(mapMarker in markers)
            {
                if(mapMarker.isOwnMarker(marker)&&mapMarker.place!=null)
                {
                    var intent = Intent(this, PlaceActivity::class.java)
                    //intent.putParcelableArrayListExtra("IMAGES", mapMarker.place!!.images)
                    intent.putExtra("PLACE",mapDataProvider.getIDByMarker(mapMarker))
                    Log.i("PLACE","Place ID:${mapDataProvider.getIDByMarker(mapMarker)}")
                    startActivity(intent)
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

        binding.btnPlaceNewMarker.setOnClickListener {
            view ->
            val latLang = mMap.cameraPosition.target
                var newMarker = MarkerOptions()
                    .position(latLang)
                    .title("New Marker By Button At: $latLang")
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .draggable(true)
            mMap.addMarker(newMarker)
        }
    }


        /*override fun onResumeFragments() {
            super.onResumeFragments()
            if (permissionHandler.permissionDenied) {
                // Permission was not granted, display error dialog.
                showMissingPermissionError()
                permissionHandler.permissionDenied = false
            }
        }*/

        override fun onResume()
        {
            super.onResume()
            Log.i("PERMISSION","OnResume")
           if (permissionHandler.hasPermission[LOCATION_PERMISSION_REQUEST_CODE]==true) {
                // TODO Permission was not granted, display error dialog.
               //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()

           }
            else if(permissionHandler.hasPermission[LOCATION_PERMISSION_REQUEST_CODE]==false)
           {
               //Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_SHORT).show()
               //Log.i("PERMISSION","permissionDenied")
           }
        }

    /**
     * If we click on the GPS sign, which centers the camera around the device.
     */
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    /**
     * If we click on the device location marker.
     */
    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

        /**
         * Delegates the result to the PermissionHandler
         */
        override fun onRequestPermissionsResult(requestCode: Int,
                                                permissions: Array<String>, grantResults: IntArray) {
           super.onRequestPermissionsResult(requestCode,permissions,grantResults)
            permissionHandler.onRequestPermissionsResult(requestCode,permissions,grantResults)
        }

        /**
         * Centers the camera around user's location or if its null, around Budapest
         */
        private fun centerCamera()
        {
            var mapCenter = LatLng(47.5,19.05)
            if(currentLocation!=null)
            {
                mapCenter = LatLng(currentLocation!!.latitude,currentLocation!!.longitude)
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
