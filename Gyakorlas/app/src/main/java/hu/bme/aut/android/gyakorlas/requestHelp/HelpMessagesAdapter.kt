package hu.bme.aut.android.gyakorlas.requestHelp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.gyakorlas.databinding.HelpMessageBinding

class HelpMessagesAdapter(private var helpMessages:ArrayList<HelpMessage>) : RecyclerView.Adapter<HelpMessagesAdapter.HelpMessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HelpMessageViewHolder (
        HelpMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: HelpMessageViewHolder, position: Int) {
        val selectedMessage = helpMessages[position]
        holder.binding.tvMessage.text = selectedMessage.message
        holder.binding.tvUsername.text = selectedMessage.username
    }

    override fun getItemCount(): Int {
        return helpMessages.size
    }

    fun update(helpMessages: ArrayList<HelpMessage>){
        this.helpMessages = helpMessages
        this.notifyDataSetChanged()
    }
    inner class HelpMessageViewHolder(val binding: HelpMessageBinding) : RecyclerView.ViewHolder(binding.root)
}