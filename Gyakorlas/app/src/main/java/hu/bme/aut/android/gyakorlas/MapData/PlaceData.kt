package hu.bme.aut.android.gyakorlas.MapData

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class PlaceData(var name: String?): Serializable {

    var description:String = ""

    constructor(name:String,desc:String):this(name)
    {
        description=desc
    }

    @Transient
    var images : ArrayList<Bitmap?> = ArrayList()


}