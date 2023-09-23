package hu.bme.aut.android.gyakorlas

import com.google.android.gms.maps.model.LatLng

class MapMarker(var name: String,var lat: Double, var lng: Double) {

    fun getLatLng(): LatLng
    {
        return LatLng(lat,lng)
    }

}