package hu.bme.aut.android.gyakorlas.requestHelp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gyakorlas.databinding.HelpMessageBinding
import hu.bme.aut.android.gyakorlas.mapData.UserMarker

class UserMarkerAdapter(private var userMarkers:ArrayList<UserMarker>) : RecyclerView.Adapter<UserMarkerAdapter.UserMarkerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserMarkerViewHolder (
        HelpMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: UserMarkerViewHolder, position: Int) {
        val selectedMessage = userMarkers[position]
        holder.binding.tvMessage.text = selectedMessage.message
        holder.binding.tvUsername.text = selectedMessage.username
    }

    override fun getItemCount(): Int {
        return userMarkers.size
    }

    fun update(userMarkers: ArrayList<UserMarker>){
        this.userMarkers = userMarkers
        this.notifyDataSetChanged()
    }
    inner class UserMarkerViewHolder(val binding: HelpMessageBinding) : RecyclerView.ViewHolder(binding.root)
}