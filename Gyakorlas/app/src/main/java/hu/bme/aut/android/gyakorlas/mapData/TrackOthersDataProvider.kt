package hu.bme.aut.android.gyakorlas.mapData

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import hu.bme.aut.android.gyakorlas.MainActivity
import hu.bme.aut.android.gyakorlas.fragments.TrackOthersFragment
import hu.bme.aut.android.gyakorlas.location.LocationService
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess

class TrackOthersDataProvider {
    var markers: ArrayList<UserMarker> = ArrayList()
        private set

    companion object {
        val instance by lazy {
            TrackOthersDataProvider()
        }
    }
    interface TrackOthersDataChangedListener
    {
        fun onTrackOthersDataChanged(markers:List<UserMarker>)
    }

    private val listeners = ArrayList<TrackOthersDataProvider.TrackOthersDataChangedListener>()
    fun addListener(listener: TrackOthersDataProvider.TrackOthersDataChangedListener)
    {
        listeners.add(listener)
    }

    fun removeListener(listener: TrackOthersDataProvider.TrackOthersDataChangedListener)
    {
        listeners.remove(listener)
    }

    fun updateUserMarkers() {
        DataAccess.getUserMarkers()
        {
                outMarkers->
            if(outMarkers!=null)
            {
                markers.clear()
                markers.addAll(outMarkers)
            }
            for(listener in listeners)
            {
                listener.onTrackOthersDataChanged(markers)
            }
        }
    }

    fun postUserMarker(){
        var lat = LocationService.currentLocation?.latitude
        var lng = LocationService.currentLocation?.longitude
        Log.i("POSTUSERMARKER", Token.token)

        if (lat != null && lng != null){
            var user = DataAccess.UserMarkerServerData(Token.token, lat, lng, "")
            DataAccess.startHelpMessageListener(user, ::onSuccess, ::onFailure)
        }
    }

    private fun onFailure(message:String)
    {
        Log.i("Retrofit","OnFailure")
    }
    private fun onSuccess()
    {
        Log.i("Retrofit","OnSuccess")
    }
}