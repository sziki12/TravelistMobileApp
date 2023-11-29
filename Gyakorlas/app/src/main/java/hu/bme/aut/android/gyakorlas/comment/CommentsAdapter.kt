package hu.bme.aut.android.gyakorlas.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gyakorlas.databinding.CommentRowBinding

class CommentsAdapter(var comments:ArrayList<Comment>): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

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
}