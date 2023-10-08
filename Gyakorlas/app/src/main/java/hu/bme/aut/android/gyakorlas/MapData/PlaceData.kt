package hu.bme.aut.android.gyakorlas.MapData

import android.graphics.Bitmap
import java.io.Serializable

data class PlaceData (var name:String):Serializable{

    var description:String = ""

    constructor(name:String,desc:String):this(name)
    {
        description=desc
    }
    var images : ArrayList<Bitmap> = ArrayList()
}