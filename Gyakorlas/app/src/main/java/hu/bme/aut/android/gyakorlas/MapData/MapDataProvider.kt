package hu.bme.aut.android.gyakorlas.MapData

import android.util.Log
import hu.bme.aut.android.gyakorlas.MapsActivity

class MapDataProvider {
    /**
     * Get and load the Markers from a dedicated location
     */
    fun getMarkers(markerType : Int):ArrayList<MapMarker>
    {
        var markers: ArrayList<MapMarker> = ArrayList()
        when (markerType) {
            MapsActivity.BAR_MARKERS -> {
                var barCraftNyugati = PlaceData("BarCraft Nyugati")
                markers.add(MapMarker(barCraftNyugati,47.50867271248256, 19.055384710139474))

                var barCraftBuda = PlaceData("BarCraft Buda")
                markers.add(MapMarker(barCraftBuda,47.4816601353997, 19.0525543254818))

                var wesselenyi4es6os = PlaceData("4es6os Wesselényi")
                markers.add(MapMarker(wesselenyi4es6os,47.500386932768905, 19.068751383155128))
            }
            MapsActivity.RESTAURANT_MARKERS -> {
                var wasabi = PlaceData("Wasabi Running Sushi & Wok Restaurant")
                markers.add(MapMarker(wasabi,47.50828901836447, 19.057431346616017))

                var theMagic = PlaceData("The MAGIC Budapest")
                markers.add(MapMarker(theMagic,47.5041399741706, 19.057411806181104))
            }
            else -> {
                Log.w("MARKERS","INVALID MARKERS INPUT")
            }
        }
        return markers
    }
}