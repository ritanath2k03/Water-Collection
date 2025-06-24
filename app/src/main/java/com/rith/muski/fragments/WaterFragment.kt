package com.rith.muski.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rith.muski.Model.Order
import com.rith.muski.R
import com.rith.muski.adapters.OrderAdapter
import com.rith.muski.adapters.WaterAdapter
import com.rith.muski.databinding.FragmentWaterBinding
import com.rith.muski.viewmodel.OrderViewmodel
import com.rith.muski.viewmodel.WaterViewModel
import org.json.JSONObject

class WaterFragment : Fragment() {
    private lateinit var waterBinding:FragmentWaterBinding
    private lateinit var  waterViewModel: WaterViewModel
    private lateinit var  orderViewModel: OrderViewmodel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        waterBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_water,container,false)
        waterViewModel=ViewModelProvider(this)[WaterViewModel::class.java]
        orderViewModel=ViewModelProvider(this)[OrderViewmodel::class.java]
        waterBinding.lifecycleOwner=viewLifecycleOwner
        waterBinding.viewModel=waterViewModel
        setWaterRv()
        setListners()
        insertSampleWaterOrder()
        return waterBinding.root
    }

    private fun insertSampleWaterOrder() {

    }

    private fun setListners() {

    }

    private fun setWaterRv() {
        waterViewModel.insertInitialWater()
        val waterAdapter = WaterAdapter { wId ->
            Log.d("wId",wId.toString())
            if(wId==1)waterBinding.sRv.text="500 mL Orders"
            if(wId==2)waterBinding.sRv.text="1L Orders"
            if(wId==3)waterBinding.sRv.text="20L Orders"
            orderViewModel.loadOrdersByWaterId(wId)
        }

        waterBinding.waterrv.adapter=waterAdapter
        waterViewModel.waterList.observe(viewLifecycleOwner){it->

            waterAdapter.submitList(it)
        }
        waterViewModel.loadWaterItems()

        val orderAdapter = OrderAdapter(){json->
            showDropdown(json)
            Log.d("JsonObject",json.toString())
        }
        waterBinding.orderrv.adapter = orderAdapter

        orderViewModel.orders.observe(viewLifecycleOwner) {
            orderAdapter.submitList(it)
        }
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