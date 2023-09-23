package hu.bme.aut.android.gyakorlas


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.gyakorlas.databinding.ActivityMapsBinding


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var markers: ArrayList<MapMarker> = ArrayList<MapMarker>()
   //private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var currentLocation:LatLng;

    companion object
    {
        val BAR_MARKERS:Int = 1
        val RESTAURANT_MARKERS:Int = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpMapData(this.intent.getIntExtra("MARKERS",-1))

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Contains the initiations of the markers ArrayList, and gets the current location
     */
    private fun setUpMapData(markerType : Int)
    {
        markers.clear()
        //locationClient = LocationServices.getFusedLocationProviderClient(this)
        getMarkers(markerType)
        var result: LatLng? =  getCurrentLocation()
        if (result != null) {
            currentLocation = result

        } else {
            if(markers.isNotEmpty())
                currentLocation = markers[0].getLatLng()
        }
    }

    private fun getCurrentLocation():LatLng?
    {
        return LatLng(47.5,19.0)
    }

    /**
     * Get and load the Markers from a dedicated location
     */
    private fun getMarkers(markerType : Int)
    {
        if(markerType == BAR_MARKERS)
        {
            markers.add(MapMarker("BarCraft Nyugati",47.50867271248256, 19.055384710139474))
            markers.add(MapMarker("BarCraft Buda",47.4816601353997, 19.0525543254818))
            markers.add(MapMarker("4es6os Wesselényi",47.500386932768905, 19.068751383155128))
        }
        else if(markerType == RESTAURANT_MARKERS)
        {
            markers.add(MapMarker("Wasabi Running Sushi & Wok Restaurant",47.50828901836447, 19.057431346616017))
            markers.add(MapMarker("The MAGIC Budapest",47.5041399741706, 19.057411806181104))
        }
        else
        {
            println("INVALID MARKERS INPUT")
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        for(marker in markers)
        {
            mMap.addMarker(MarkerOptions().position(marker.getLatLng()).title(marker.name))
        }

        var mapLocation: CameraPosition = CameraPosition.Builder()
            .target(currentLocation?:LatLng(10.0,10.0))
            .zoom(11.0f)
            .bearing(0f)
            .tilt(50f)
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mapLocation))
    }
}