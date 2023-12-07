package hu.bme.aut.android.gyakorlas.mapData

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
}