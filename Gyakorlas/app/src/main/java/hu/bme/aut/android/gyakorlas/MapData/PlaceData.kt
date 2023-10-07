package hu.bme.aut.android.gyakorlas.MapData

import android.graphics.Bitmap
import java.io.Serializable

data class PlaceData (var name:String):Serializable{

    var description:String = ""
    //var images : ArrayList<Bitmap> = ArrayList()
}