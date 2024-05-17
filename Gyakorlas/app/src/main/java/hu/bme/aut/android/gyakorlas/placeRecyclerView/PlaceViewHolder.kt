package hu.bme.aut.android.gyakorlas.placeRecyclerView

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import hu.bme.aut.android.gyakorlas.R

class PlaceViewHolder(itemView: View) : ViewHolder(itemView) {
    val titleView: TextView
    var imageButton1: ImageButton
    var imageButton2: ImageButton
    var imageButton3: ImageButton
    val detailesButton: Button
    val distanceText : TextView
    val ratingView: RatingBar

    init {
        // Define click listener for the ViewHolder's View
        titleView = itemView.findViewById(R.id.rowTitleView)
        imageButton1 = itemView.findViewById(R.id.rowImageButton1)
        imageButton2 = itemView.findViewById(R.id.rowImageButton2)
        imageButton3 = itemView.findViewById(R.id.rowImageButton3)
        detailesButton = itemView.findViewById(R.id.detailesButton)
        ratingView = itemView.findViewById(R.id.ratingBar)
        distanceText = itemView.findViewById(R.id.distanceText)
    }
}