package com.rith.muski.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rith.muski.R
import org.json.JSONObject


class OrderAdapter : ListAdapter<JSONObject, OrderAdapter.OrderViewHolder>(DiffCallback()) {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(json: JSONObject) {
            val order = json.getJSONObject("order")
            val user = json.getJSONObject("user")

            itemView.findViewById<TextView>(R.id.orderprice).text = "₹${order.getDouble("o_amount")}"
            itemView.findViewById<TextView>(R.id.orderdate).text = order.getString("o_date")
            itemView.findViewById<TextView>(R.id.name).text = user.getString("name")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<JSONObject>() {
        override fun areItemsTheSame(oldItem: JSONObject, newItem: JSONObject): Boolean {
            return oldItem.getJSONObject("order").getLong("o_id") ==
                    newItem.getJSONObject("order").getLong("o_id")
        }

        override fun areContentsTheSame(oldItem: JSONObject, newItem: JSONObject): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
}
