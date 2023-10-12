package hu.bme.aut.android.gyakorlas.mapData

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.google.android.gms.location.LocationServices


class MapDataProvider() {
    companion object {
        var markers: ArrayList<MapMarker> = ArrayList()
        fun initMarkers(activity: Activity) {
            if (markers.isEmpty()) {
                var barCraftNyugati = PlaceData("BarCraft Nyugati", "Budapest")
                barCraftNyugati.description =
                    "Service options: Dine-in · No takeaway · No delivery\n" +
                            "Address: Budapest, Bajcsy-Zsilinszky út 59, 1065\n" +
                            "\n" +
                            "Hours: \n" +
                            "\tSunday\t2 PM–12 AM\n" +
                            "\tMonday\t4 PM–12 AM\n" +
                            "\tTuesday\t4 PM–12 AM\n" +
                            "\tWednesday\t4 PM–12 AM\n" +
                            "\tThursday\t4 PM–12 AM\n" +
                            "\tFriday\t4 PM–2 AM\n" +
                            "\tSaturday\t2 PM–2 AM\n" +
                            "\n" +
                            "\nPhone: (06 1) 406 2201" +
                            "\nA város szívében, egy ugrásnyira a Nyugati pályaudvartól, autentikus fantasy pincében vár Rád a Barcraft 2!\n" +
                            "\n" +
                            "Description:\n" +
                            "Hamisítatlan fantasy taverna érzéssel, és hideg italokkal várja az arra tévedő kalandorokat! A konzolok szerelmesei otthonra lelnek kanapéinkon, miközben kontrollerrel a kezükben múlatják az időt, egy teremmel arréb pedig a társasjátékok rajongói élvezhetik a több mint 200 darabból álló társasjáték gyűjteményünk gyönyöreit. Az online bajnokokra pedig vár a Hősök Csarnoka, 10 high end gaming PCvel, NobleChair és Vertagear székekkel, így a győzelem garantált!"

                barCraftNyugati.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft01.jpg")))
                barCraftNyugati.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft02.jpg")))
                barCraftNyugati.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft03.jpg")))
                barCraftNyugati.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft03.jpg")))
                barCraftNyugati.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft03.jpg")))
                barCraftNyugati.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft03.jpg")))

                markers.add(MapMarker(barCraftNyugati, 47.50867271248256, 19.055384710139474))

                var barCraftBuda = PlaceData("BarCraft Buda", "Budapest")
                markers.add(MapMarker(barCraftBuda, 47.4816601353997, 19.0525543254818))

                var wesselenyi4es6os = PlaceData("4es6os Wesselényi", "Budapest")
                markers.add(MapMarker(wesselenyi4es6os, 47.500386932768905, 19.068751383155128))

                var wasabi = PlaceData("Wasabi Running Sushi & Wok Restaurant", "Budapest")
                markers.add(MapMarker(wasabi, 47.50828901836447, 19.057431346616017))

                var theMagic = PlaceData("The MAGIC Budapest", "Budapest")
                markers.add(MapMarker(theMagic, 47.5041399741706, 19.057411806181104))
            }
        }
            /**
             * Get and load the Markers from a dedicated location
             */
            fun getSelectedMarkers(selectedLocation: String): ArrayList<MapMarker> {
                var selectedMarkers: ArrayList<MapMarker> = ArrayList()

                if (selectedLocation == "All") {
                    selectedMarkers = markers
                } else {
                    for (marker in markers) {
                        if (marker.place?.location == selectedLocation) {
                            selectedMarkers.add(marker)
                        }
                    }
                }
                return selectedMarkers
            }

            fun getMarkerByID(ID: Int): MapMarker {
                return markers[ID]
            }

            fun getIDByMarker(targetMarker: MapMarker): Int? {
                var ID: Int? = null
                for (i in 0 until markers.size) {
                    if (markers[i] == targetMarker) {
                        ID = i
                    }
                }
                return ID
            }

            fun resizeDrawable(
                activity: Activity,
                bitmap: Bitmap?,
                dstWidth: Int,
                dstHeight: Int
            ): Drawable? {

                var drawableOut: Drawable? = null
                if (bitmap != null) {
                    //var bitmap = (drawable as BitmapDrawable).bitmap
                    drawableOut = BitmapDrawable(
                        activity.resources,
                        Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true)
                    )
                }

                return drawableOut
            }
    }
}