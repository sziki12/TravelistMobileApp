package hu.bme.aut.android.gyakorlas.requestHelp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gyakorlas.databinding.HelpMessageBinding
import hu.bme.aut.android.gyakorlas.mapData.UserMarker

class UserMarkerAdapter(private var userMarkers:List<UserMarker>?) : RecyclerView.Adapter<UserMarkerAdapter.UserMarkerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserMarkerViewHolder (
        HelpMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: UserMarkerViewHolder, position: Int) {
        val selectedMessage = userMarkers?.get(position)
        if (selectedMessage != null) {
            holder.binding.tvMessage.text = selectedMessage.message
        }
        if (selectedMessage != null) {
            holder.binding.tvUsername.text = selectedMessage.username
        }
    }

    override fun getItemCount(): Int {
        return userMarkers?.size ?: 0
    }

    fun update(userMarkers: List<UserMarker>){
        this.userMarkers = userMarkers
        this.notifyDataSetChanged()
    }
    inner class UserMarkerViewHolder(val binding: HelpMessageBinding) : RecyclerView.ViewHolder(binding.root)
}