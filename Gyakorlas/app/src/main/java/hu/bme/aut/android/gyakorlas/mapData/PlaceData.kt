package hu.bme.aut.android.gyakorlas.mapData

import android.graphics.Bitmap
import hu.bme.aut.android.gyakorlas.comment.Comment

data class PlaceData(var name: String?,var location:String) {

    var comments:ArrayList<Comment> = ArrayList()
        private set

    var description:String = ""

    val rating : Float
        get()
        {
            return reviewRating/reviewNumber
        }
    private var reviewNumber:Int = 0
    var reviewRating:Float = 0f

    constructor(name:String,location:String,desc:String):this(name,location)
    {
        description=desc
    }

    constructor(name:String,location:String,desc:String,rat:Float,revNum:Int):this(name,location,desc)
    {
        reviewRating = rat
        reviewNumber = revNum
    }

    var images : ArrayList<Bitmap?> = ArrayList()

    fun addComment(comment: Comment)
    {
        reviewNumber++
        reviewRating+=comment.rating
        comments.add(comment)
    }


}