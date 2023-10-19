package hu.bme.aut.android.gyakorlas.mapData

import android.graphics.Bitmap

data class PlaceData(var name: String?,var location:String) {

    var description:String = ""

    var rating : Float = 0f
    var reviewNumber:Int = 0;

    constructor(name:String,location:String,desc:String):this(name,location)
    {
        description=desc
    }

    constructor(name:String,location:String,desc:String,rat:Float,revNum:Int):this(name,location,desc)
    {
        rating = rat
        reviewNumber = revNum
    }

    var images : ArrayList<Bitmap?> = ArrayList()


}