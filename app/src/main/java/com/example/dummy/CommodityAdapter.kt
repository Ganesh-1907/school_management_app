package com.example.dummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommodityAdapter(private val commodities: List<Commodity>) :
    RecyclerView.Adapter<CommodityAdapter.CommodityViewHolder>() {

    class CommodityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.commodityTitle)
        val month: TextView = itemView.findViewById(R.id.commodityMonth)
        val phase1: TextView = itemView.findViewById(R.id.commodityPhase1)
        val phase2: TextView = itemView.findViewById(R.id.commodityPhase2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommodityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_commodity, parent, false)
        return CommodityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommodityViewHolder, position: Int) {
        val commodity = commodities[position]
        holder.title.text = "Title: ${commodity.title}"
        holder.month.text = "Month: ${commodity.month}"
        holder.phase1.text = "Phase 1: ${commodity.phase1}"
        holder.phase2.text = "Phase 2: ${commodity.phase2}"
    }

    override fun getItemCount(): Int = commodities.size
}
