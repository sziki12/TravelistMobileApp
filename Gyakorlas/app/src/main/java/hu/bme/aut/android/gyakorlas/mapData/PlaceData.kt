package hu.bme.aut.android.gyakorlas.mapData

import android.graphics.Bitmap
import hu.bme.aut.android.gyakorlas.comment.Comment
import android.util.Log

data class PlaceData(var name: String?,var location:String) {

    private var reviewNumber:Int = 0
    private var reviewRating:Float = 0f
    var comments:ArrayList<Comment> = ArrayList()
        private set

    var description:String = ""

    val rating : Float
        get()
        {
            Log.i("Place","Rating: $reviewRating Num: $reviewNumber")
            return if(reviewNumber>0)
                    reviewRating/reviewNumber
                else
                    0f
        }


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

    fun userAlreadyCommented(email:String):Boolean
    {
        for(comment in comments)
        {
            if(comment.senderEmail==email)
            {
                return true
            }
        }
        return false
    }
    fun getUserComment(email:String):Comment?
    {
        for(comment in comments)
            if(comment.senderEmail==email)
                return comment
        return null
    }

    fun removeComment(email:String)
    {
        for(comment in comments)
        {
            if(comment.senderEmail==email)
            {
                reviewNumber--
                reviewRating-=comment.rating
                comments.remove(comment)
            }
        }
    }
}