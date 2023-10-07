package hu.bme.aut.android.gyakorlas.MapData

import com.google.android.gms.maps.model.LatLng

class MapMarker(var name: String,var lat: Double, var lng: Double,var place:PlaceData? = null) {

    fun getLatLng(): LatLng
    {
        return LatLng(lat,lng)
    }

}