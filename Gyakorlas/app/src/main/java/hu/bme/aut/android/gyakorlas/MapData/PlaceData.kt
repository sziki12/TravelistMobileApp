package hu.bme.aut.android.gyakorlas.MapData

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class PlaceData(var name: String?):Parcelable{

    var description:String = ""

    constructor(name:String,desc:String):this(name)
    {
        description=desc
    }

    var images : ArrayList<Bitmap?> = ArrayList()

    constructor(parcel: Parcel) : this(parcel.readString()) {
        description = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.createTypedArrayList(Bitmap::class.java)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceData> {
        override fun createFromParcel(parcel: Parcel): PlaceData {
            return PlaceData(parcel)
        }

        override fun newArray(size: Int): Array<PlaceData?> {
            return arrayOfNulls(size)
        }
    }
}