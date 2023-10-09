package hu.bme.aut.android.gyakorlas.MapData

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import hu.bme.aut.android.gyakorlas.MapsActivity


class MapDataProvider(var activity: Activity){
    /**
     * Get and load the Markers from a dedicated location
     */
    fun getMarkers(markerType : Int):ArrayList<MapMarker>
    {
        var markers: ArrayList<MapMarker> = ArrayList()
        when (markerType) {
            MapsActivity.BAR_MARKERS -> {
                var barCraftNyugati = PlaceData("BarCraft Nyugati")
                barCraftNyugati.description = "Service options: Dine-in · No takeaway · No delivery\n" +
                        "Address: Budapest, Bajcsy-Zsilinszky út 59, 1065\n" +
                        "\n"+
                        "Hours: \n" +
                        "\tSunday\t2 PM–12 AM\n" +
                        "\tMonday\t4 PM–12 AM\n" +
                        "\tTuesday\t4 PM–12 AM\n" +
                        "\tWednesday\t4 PM–12 AM\n" +
                        "\tThursday\t4 PM–12 AM\n" +
                        "\tFriday\t4 PM–2 AM\n" +
                        "\tSaturday\t2 PM–2 AM\n" +
                        "\n"+
                        "Phone: (06 1) 406 2201\n"+
                        "A város szívében, egy ugrásnyira a Nyugati pályaudvartól, autentikus fantasy pincében vár Rád a Barcraft 2!\n" +
                        "\n" +
                        "Description:\n" +
                        "Hamisítatlan fantasy taverna érzéssel, és hideg italokkal várja az arra tévedő kalandorokat! A konzolok szerelmesei otthonra lelnek kanapéinkon, miközben kontrollerrel a kezükben múlatják az időt, egy teremmel arréb pedig a társasjátékok rajongói élvezhetik a több mint 200 darabból álló társasjáték gyűjteményünk gyönyöreit. Az online bajnokokra pedig vár a Hősök Csarnoka, 10 high end gaming PCvel, NobleChair és Vertagear székekkel, így a győzelem garantált!"


                barCraftNyugati!!.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft01.jpg")))
                barCraftNyugati!!.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft02.jpg")))
                barCraftNyugati!!.images.add(BitmapFactory.decodeStream(activity.assets.open("images/nyugati_barcraft03.jpg")))
                /*dr = Drawable.createFromStream(activity.assets.open("images/nyugati_barcraft03.jpg"),null)
                barCraftNyugati!!.images.add(resizeDrawable(dr,600,600))*/


                /*var options = BitmapFactory.Options()
                options.inSampleSize = 2*/
               /* barCraftNyugati.images.add(Drawable.createFromStream(activity.assets.open("images/nyugati_barcraft01.jpg"), null))
                barCraftNyugati.images.add(Drawable.createFromStream(activity.assets.open("images/nyugati_barcraft02.jpg"), null))
                barCraftNyugati.images.add(Drawable.createFromStream(activity.assets.open("images/nyugati_barcraft03.jpg"), null))*/

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