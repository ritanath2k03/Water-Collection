package com.rith.muski.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rith.muski.Model.Water
import com.rith.muski.R

class WaterAdapter : ListAdapter<Water, WaterAdapter.WaterViewHolder>(DiffCallback()) {

    class WaterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Water) {
            itemView.findViewById<TextView>(R.id.wquantity).text = item.wQuantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_water_container_item, parent, false)
        return WaterViewHolder(view)
    }

    override fun onBindViewHolder(holder: WaterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Water>() {
        override fun areItemsTheSame(oldItem: Water, newItem: Water) = oldItem.wId == newItem.wId
        override fun areContentsTheSame(oldItem: Water, newItem: Water) = oldItem == newItem
    }
}
