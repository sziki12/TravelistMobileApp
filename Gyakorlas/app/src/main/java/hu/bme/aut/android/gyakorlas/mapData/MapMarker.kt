package hu.bme.aut.android.gyakorlas.mapData

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

data class MapMarker(var name: String,var lat: Double, var lng: Double,var place:PlaceData? = null) {

    var marker = MarkerOptions().position(getLatLng()).title(name)
    fun getLatLng(): LatLng
    {
        return LatLng(lat,lng)
    }

    fun isOwnMarker(marker: Marker):Boolean{

        return marker.position == getLatLng()
    }

    constructor(place:PlaceData,lat:Double,lng: Double):this(place.name.toString(),lat, lng,place)

    override fun equals(other: Any?): Boolean {
        var marker = other as MapMarker?
        return marker!=null&&marker.lat==this.lat&&marker.lng==this.lng&&marker.name==this.name
    }



}