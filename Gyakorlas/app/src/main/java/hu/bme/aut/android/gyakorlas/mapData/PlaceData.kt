package hu.bme.aut.android.gyakorlas.mapData

import android.graphics.Bitmap

data class PlaceData(var name: String?,var location:String) {

    var description:String = ""

    constructor(name:String,location:String,desc:String):this(name,location)
    {
        description=desc
    }

    var images : ArrayList<Bitmap?> = ArrayList()


}