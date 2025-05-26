package com.example.dummy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.R
import com.example.dummy.model.Announcement

class AnnouncementAdapter(private val announcements: List<Announcement>) :
    RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val description: TextView = itemView.findViewById(R.id.textViewDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.announcement_item, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val item = announcements[position]
        holder.title.text = item.title
        holder.description.text = item.description
    }

    override fun getItemCount() = announcements.size
}
