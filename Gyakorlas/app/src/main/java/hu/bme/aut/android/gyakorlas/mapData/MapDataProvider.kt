package hu.bme.aut.android.gyakorlas.mapData

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import hu.bme.aut.android.gyakorlas.comment.Comment
import hu.bme.aut.android.gyakorlas.retrofit.DataAccess


class MapDataProvider private constructor() {
    var markers: ArrayList<MapMarker> = ArrayList()
        private set
    companion object {
        val instance by lazy {
            MapDataProvider()
        }
            fun resizeBitmap(
                activity: Activity,
                bitmap: Bitmap?,
                dstWidth: Int,
                dstHeight: Int
            ): Drawable? {

                var drawableOut: Drawable? = null
                if (bitmap != null) {
                    drawableOut = BitmapDrawable(
                        activity.resources,
                        Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true)
                    )
                }

                return drawableOut
            }

        fun resizeDrawable(
            activity: Activity,
            drawable: Drawable?,
            dstWidth: Int,
            dstHeight: Int
        ): Drawable? {

            var drawableOut: Drawable? = null
            var bitmap:Bitmap? = null
            if (drawable != null)
                bitmap = drawable.toBitmap()

            drawableOut = resizeBitmap(activity,bitmap,dstWidth,dstHeight)
            return drawableOut
        }
    }

    interface MapDataChangedListener
    {
        fun onMapDataChanged(markers:List<MapMarker>)
    }
    private val listeners = ArrayList<MapDataChangedListener>()
    fun addListener(listener:MapDataChangedListener)
    {
        listeners.add(listener)
    }

    fun removeListener(listener:MapDataChangedListener)
    {
        listeners.remove(listener)
    }
    fun updateMarkers() {
        val newMarkers:ArrayList<MapMarker> = ArrayList()
        DataAccess.getMapMarkers()
        {
                outMakrers->
            if(outMakrers!=null)
            {
                newMarkers.clear()
                newMarkers.addAll(outMakrers)
            }
            for(listener in listeners)
            {
                listener.onMapDataChanged(markers)
            }
        }
        DataAccess.getComments() {
            outComments->
            if (outComments != null) {
                for(comment in outComments) {
                    TODO("Add Comment To Place")
                }
            }
            markers = newMarkers
        }
    }

    fun getLocations():ArrayList<String>
    {
        val locations = ArrayList<String>()
        locations.add("All")
        locations.add("None")
        for(marker in markers)
        {
            if(!locations.contains(marker.place?.location))
            {
                marker.place?.location?.let { locations.add(it) }
            }
        }
        return locations
    }
    /**
     * Get and load the Markers from a dedicated location
     */
    fun getSelectedMarkers(selectedLocation: String): ArrayList<MapMarker> {
        var selectedMarkers: ArrayList<MapMarker> = ArrayList()

        when (selectedLocation) {
            "All" -> {
                selectedMarkers = markers
            }
            "None" -> {
                return selectedMarkers
            }
            else -> {
                for (marker in markers) {
                    if (marker.place?.location == selectedLocation) {
                        selectedMarkers.add(marker)
                    }
                }
            }
        }
        return selectedMarkers
    }

    fun getMarkerByID(id: Int): MapMarker {
        return markers[id]
    }

    fun getIDByMarker(targetMarker: MapMarker): Int? {
        var id: Int? = null
        for (i in 0 until markers.size) {
            if (markers[i] == targetMarker) {
                id = i
            }
        }
        return id
    }
}