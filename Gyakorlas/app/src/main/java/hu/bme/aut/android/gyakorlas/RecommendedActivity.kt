package hu.bme.aut.android.gyakorlas

import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.gyakorlas.mapData.GeofenceHandler
import hu.bme.aut.android.gyakorlas.databinding.ActivityRecommendedBinding
import hu.bme.aut.android.gyakorlas.mapData.MapMarker


class RecommendedActivity : AppCompatActivity() {

    private var markers:ArrayList<MapMarker> = ArrayList()
    private var geofenceHandler = GeofenceHandler()
    private lateinit var binding: ActivityRecommendedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        markers = geofenceHandler.calculateNearbyMarkers()

        if(MapsActivity.currentLocation!=null)
        {
            for(marker in markers)
            {
                var markerLocaton = Location("Provider")
                markerLocaton.latitude = marker.lat
                markerLocaton.longitude = marker.lng

                val results = FloatArray(1)
                Location.distanceBetween(
                    markerLocaton.latitude, markerLocaton.longitude,
                    MapsActivity.currentLocation!!.latitude, MapsActivity.currentLocation!!.longitude, results
                )
                var textView = TextView(this)
                textView.text="${marker.name} distance: ${Math.round(results[0])}m"
                binding.linearLayout.addView(textView)
            }
        }

    }


}