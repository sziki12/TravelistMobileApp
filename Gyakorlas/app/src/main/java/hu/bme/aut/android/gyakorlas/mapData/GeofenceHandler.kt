package hu.bme.aut.android.gyakorlas.mapData

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceHandler : BroadcastReceiver() {
    var activity: Activity? = null
    var geofencingClient:GeofencingClient? = null
    val geofenceRadius = 7500f

    fun calculateNearbyMarkers():ArrayList<MapMarker>
    {
        var nearbyMarkers:ArrayList<MapMarker> = ArrayList()
        for(geofence in activeGeofences)
        {
            for(marker in markers)
            {
                if(marker.lat==geofence.latitude&&marker.lng==geofence.longitude)
                {
                    nearbyMarkers.add(marker)
                    Log.i("GEOFENCE","Adding Marker: ${marker.name}")
                }
            }
        }
        return nearbyMarkers
    }


    fun setUpGeofencingClient(activity: Activity)
    {
        this.activity=activity
        geofencingClient = LocationServices.getGeofencingClient(activity)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent!!.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent!!.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent?.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Get the transition details as a String.
            /*val geofenceTransitionDetails = getGeofenceTransitionDetails(
                geofenceTransition,
                triggeringGeofences
            )*/
            onTransition(geofenceTransition,triggeringGeofences)

            // Send notification and log the transition details.
            //sendNotification(geofenceTransitionDetails)
            //Log.i(TAG, geofenceTransitionDetails)
        } else {
            // Log the error.
            /*Log.e(TAG, getString(
                R.string.geofence_transition_invalid_type,
                geofenceTransition))*/
        }
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(activity, GeofenceHandler::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_MUTABLE/*PendingIntent.FLAG_UPDATE_CURRENT*/)
    }



    @SuppressLint("MissingPermission")
    fun setUpGeofences(allMarkers:ArrayList<MapMarker>)
    {
        for(marker in allMarkers)
        {
            addGeofence(marker,geofenceRadius)
        }
        markers = allMarkers
        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences added
                // ...
                Log.i("GEOFENCE","Added Successfully")
            }
            addOnFailureListener {
                // Failed to add geofences
                // ...
                Log.i("GEOFENCE","Adding Failed")
                /*if(activity as? MenuActivity != null)
                {
                   //Enable/Disable Recommended For You button
                }*/
            }
        }
    }

    fun removeGeofences()
    {
        geofencingClient?.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences removed
                // ...
                Log.i("GEOFENCE","Removed Successfully")
            }
            addOnFailureListener {
                // Failed to remove geofences
                // ...
                Log.i("GEOFENCE","Removing Failed")
            }
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    private fun onTransition(geofenceTransition: Int, triggeringGeofences: List<Geofence>?) {

        when(geofenceTransition)
        {
            Geofence.GEOFENCE_TRANSITION_ENTER ->
            {
                if(triggeringGeofences!=null)
                {
                    for(geofence in triggeringGeofences)
                    {
                        activeGeofences.add(geofence)
                        Log.i("GEOFENCE","Added lat: ${geofence.latitude} lng:${geofence.longitude}")
                    }
                }
            }

            Geofence.GEOFENCE_TRANSITION_EXIT ->
            {
                if(triggeringGeofences!=null)
                {
                    for(geofence in triggeringGeofences)
                    {
                        activeGeofences.remove(geofence)
                        Log.i("GEOFENCE","Removed lat: ${geofence.latitude} lng:${geofence.longitude}")
                    }
                }
            }
        }
    }
    fun addGeofence(marker: MapMarker, radius:Float) {
        geofenceList.add(
            Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(marker.name)

                // Set the circular region of this geofence.
                .setCircularRegion(
                    marker.lat,
                    marker.lng,
                    radius
                )

                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(Geofence.NEVER_EXPIRE)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

                // Create the geofence.
                .build())
    }
    companion object
    {
        private var activeGeofences : ArrayList<Geofence> = ArrayList()
        private var geofenceList : ArrayList<Geofence> = ArrayList()
        private var markers:ArrayList<MapMarker> = ArrayList()
    }
}