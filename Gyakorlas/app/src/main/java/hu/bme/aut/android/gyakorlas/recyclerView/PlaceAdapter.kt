package hu.bme.aut.android.gyakorlas.recyclerView

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import hu.bme.aut.android.gyakorlas.R
import hu.bme.aut.android.gyakorlas.mapData.MapDataProvider
import hu.bme.aut.android.gyakorlas.mapData.MapMarker

class PlaceAdapter(private var activity: Activity, var markers: ArrayList<MapMarker>) : Adapter<PlaceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)

        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    override fun onBindViewHolder(viewHolder: PlaceViewHolder, position: Int) {
        viewHolder.titleView.text = markers[position].place?.name
        viewHolder.descriptionView.text = markers[position].place?.description
        if(markers[position].place?.images?.isNotEmpty() == true)
            viewHolder.imageButton.setImageDrawable(MapDataProvider.resizeBitmap(activity,markers[position].place?.images?.get(0),400,400))
    }
}