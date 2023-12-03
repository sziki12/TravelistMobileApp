package hu.bme.aut.android.gyakorlas.comment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gyakorlas.databinding.CommentRowBinding
import hu.bme.aut.android.gyakorlas.mapData.MapMarker

class CommentsAdapter(inComments:ArrayList<Comment>): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    private var comments:ArrayList<Comment> = ArrayList()
    init {
        comments.addAll(inComments)
    }
    inner class CommentsViewHolder(val binding: CommentRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            CommentRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val binding = holder.binding
        val selectedComment = comments[position]
        binding.commentRating.rating = selectedComment.rating
        binding.commentText.text = selectedComment.description
        binding.commentTitle.text = selectedComment.title
    }

    fun update(newComments:ArrayList<Comment>)
    {
        //Remove and Notify
        val removedItems = ArrayList<Comment>()
        for(comment in comments)
        {
            if(!newComments.contains(comment))
            {
                removedItems.add(comment)
            }
        }
        for(comment in removedItems)
        {
            val removeIndex = comments.indexOf(comment)
            comments.remove(comment)
            notifyItemRemoved(removeIndex)
        }

        //Insert and Notify
        val addedItems = ArrayList<Int>()
        for((index, comment) in newComments.withIndex())
        {
            if(!comments.contains(comment))
            {
                addedItems.add(index)
            }
        }
        for(index in addedItems)
        {
            comments.add(newComments[index])
            notifyItemInserted(comments.size-1)

        }
    }
}