// adapters/HealthReportAdapter.kt
package com.example.dummy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.R
import com.example.dummy.models.HealthReport

class HealthReportAdapter(private val list: List<HealthReport>) :
    RecyclerView.Adapter<HealthReportAdapter.HealthViewHolder>() {

    class HealthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameText: TextView = view.findViewById(R.id.userNameText)
        val healthStatusText: TextView = view.findViewById(R.id.healthStatusText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_health_report, parent, false)
        return HealthViewHolder(view)
    }

    override fun onBindViewHolder(holder: HealthViewHolder, position: Int) {
        val item = list[position]
        holder.userNameText.text = item.userName
        holder.healthStatusText.text = item.healthStatus
    }

    override fun getItemCount(): Int = list.size
}
