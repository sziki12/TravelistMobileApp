package hu.bme.aut.android.gyakorlas.comment

data class Comment(val title:String,val description:String,val rating:Float,val senderEmail:String)
{
    override fun equals(other: Any?): Boolean {
        val comment = other as? Comment ?: return false
        return comment.senderEmail==senderEmail&&comment.rating==rating&&comment.description==description
    }
}
