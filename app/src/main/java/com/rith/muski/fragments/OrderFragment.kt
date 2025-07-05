package com.rith.muski.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rith.muski.R
import com.rith.muski.adapters.OrderAdapter
import com.rith.muski.databinding.FragmentOrderBinding
import com.rith.muski.viewmodel.OrderViewmodel
import org.json.JSONObject

class OrderFragment: Fragment() {
    private  lateinit var orderBinding:FragmentOrderBinding
    private lateinit var orderViewmodel: OrderViewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_order,container,false)
        orderBinding.addorderbutton.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_makeOrderFramgent)
        }
        orderViewmodel=ViewModelProvider(this)[OrderViewmodel::class.java]
        orderBinding.lifecycleOwner=viewLifecycleOwner
        setupOrderRecyclerView()
        setupSearchView()

        return orderBinding.root
    }

    private fun setupSearchView() {
        val fullOrderList = mutableListOf<JSONObject>()
        // Copy the original list once it's loaded

        orderViewmodel.loadOrders()

        val orderAdapter = OrderAdapter(){json->
            showDropdown(json)
            Log.d("JsonObject",json.toString())
        }
        orderViewmodel.orders.observe(viewLifecycleOwner) { list ->
            fullOrderList.clear()
            fullOrderList.addAll(list)
            orderAdapter.submitList(fullOrderList)
        }
        orderViewmodel.filteredOrders.observe(viewLifecycleOwner) { filteredList ->
            orderAdapter.submitList(filteredList)
        }
        orderBinding.orderrecyclerview.adapter=orderAdapter
        orderBinding.searchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()

                val filtered = fullOrderList.filter { json ->
                    val order = json.optJSONObject("order")
                    val user = json.optJSONObject("user")

                    val combinedText = buildString {
                        append(order?.optString("o_date", ""))
                        append(order?.optInt("o_five_ml", 0))
                        append(order?.optDouble("o_amount", 0.0))
                        append(order?.optDouble("o_paid", 0.0))
                        append(user?.optString("name", ""))
                        append(user?.optString("address", ""))
                        append(user?.optString("email", ""))
                        append(user?.optString("phone", ""))
                    }.lowercase()

                    query in combinedText
                }

                orderAdapter.submitList(filtered)
            }
        })
    }



    private fun setupOrderRecyclerView() {

    }
    private fun showDropdown(json: JSONObject) {
        val order = json.getJSONObject("order")
        val user = json.getJSONObject("user")

        val dialogView = LayoutInflater.from(context).inflate(R.layout.order_popup, null)

        dialogView.findViewById<TextView>(R.id.orderId).text = "Order ID: ${order.getLong("o_id")}"
        dialogView.findViewById<TextView>(R.id.orderAmount).text = "Amount: ₹${order.getDouble("o_amount")}"
        dialogView.findViewById<TextView>(R.id.orderPaid).text = "Paid: ₹${order.getDouble("o_paid")}"
        dialogView.findViewById<TextView>(R.id.orderDue).text = "Due: ₹${order.getDouble("o_due_payment")}"
        dialogView.findViewById<TextView>(R.id.orderDate).text = "Date: ${order.getString("o_date")}"
        dialogView.findViewById<TextView>(R.id.bottle500ml).text = "500ml: ${order.getInt("o_five_ml")}"
        dialogView.findViewById<TextView>(R.id.bottle1L).text = "1L: ${order.getInt("o_one_l")}"
        dialogView.findViewById<TextView>(R.id.bottle20L).text = "20L: ${order.getInt("o_twenty_l")}"
        dialogView.findViewById<TextView>(R.id.userName).text = "Name: ${user.getString("name")}"
        dialogView.findViewById<TextView>(R.id.userAddress).text = "Address: ${user.getString("address")}"
        dialogView.findViewById<TextView>(R.id.userPhone).text = "Phone: ${user.getString("phone")}"
        dialogView.findViewById<TextView>(R.id.userPhone).text = "Email: ${user.getString("email")}"

        AlertDialog.Builder(context)
            .setTitle("Order Details")
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .show()
    }
}