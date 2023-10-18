package hu.bme.aut.android.gyakorlas.recyclerView

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import hu.bme.aut.android.gyakorlas.R

class PlaceViewHolder(itemView: View) : ViewHolder(itemView) {
    val titleView: TextView
    var imageButton: ImageButton
    val descriptionView: TextView

    init {
        // Define click listener for the ViewHolder's View
        titleView = itemView.findViewById(R.id.rowTitleView)
        imageButton = itemView.findViewById(R.id.rowImageButton)
        descriptionView = itemView.findViewById(R.id.rowDescriptionView)
    }
}