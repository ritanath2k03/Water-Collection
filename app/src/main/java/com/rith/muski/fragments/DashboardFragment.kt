package com.rith.muski.fragments

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rith.muski.Model.UserActivityModel
import com.rith.muski.R
import com.rith.muski.adapters.UserActivityAdapter
import com.rith.muski.adapters.WaterAdapter
import com.rith.muski.databinding.FragmentDashboardBinding
import com.rith.muski.viewmodel.AddUserViewModel
import com.rith.muski.viewmodel.OrderViewmodel
import com.rith.muski.viewmodel.WaterViewModel
import ir.mahozad.android.PieChart

class DashboardFragment : Fragment() {
    private lateinit var waterViewmodel: WaterViewModel
    private lateinit var dashboardBinding: FragmentDashboardBinding
    private  lateinit var orderViewmodel: OrderViewmodel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        waterViewmodel = ViewModelProvider(this)[WaterViewModel::class.java]
        orderViewmodel=ViewModelProvider(this)[OrderViewmodel::class.java]
        waterViewmodel.insertInitialWater()
        dashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        dashboardBinding.lifecycleOwner = viewLifecycleOwner
        dashboardBinding.header.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_userFragment2)
        }
        setGrid()
        setChart()
        return dashboardBinding.root
    }

    private fun setChart() {
        var five = 0f
        var oneL = 0f
        var twentyL = 0f
        var totalAmount = 0.0
        var dueAmount = 0.0
        orderViewmodel.loadOrderDataForPieChartInDashboard()
        orderViewmodel.pieorders.observe(viewLifecycleOwner) { json ->
            Log.d("OrderSummary", json.toString())
            // Extract amounts
            totalAmount = json.optDouble("paid_amount", 0.0)
            dueAmount = json.optDouble("due_amount", 0.0)

            // Extract bottle amounts
            val bottles = json.optJSONArray("bottle_amounts")
            if (bottles != null) {
                for (i in 0 until bottles.length()) {
                    val obj = bottles.getJSONObject(i)
                    when {
                        obj.has("fiveH") -> five = obj.getDouble("fiveH").toFloat()
                        obj.has("oneL") -> oneL = obj.getDouble("oneL").toFloat()
                        obj.has("twentyL") -> twentyL = obj.getDouble("twentyL").toFloat()
                    }
                }
            }
            val piechart=dashboardBinding.piechart
            piechart.slices= listOf(
                PieChart.Slice((five/(five+oneL+twentyL)),ContextCompat.getColor(context,R.color.five_color)),
                PieChart.Slice((oneL/(five+oneL+twentyL)), ContextCompat.getColor(context,R.color.oneL_color)),
                PieChart.Slice((twentyL/(five+oneL+twentyL)), ContextCompat.getColor(context,R.color.twentyL_color)),
            )
            piechart.labelsColor=ContextCompat.getColor(context,R.color.white)

            PieChart.BorderType.SOLID
            dashboardBinding.totalpaidamount.setText(totalAmount.toString())
            dashboardBinding.totaldueamount.setText(dueAmount.toString())
        }

    }

    private fun setGrid() {
        val activityList = mutableListOf(
            UserActivityModel("Order", R.drawable.order_image_png),
            UserActivityModel("Add merchant", R.drawable.merchant_image_png),
            UserActivityModel("Water", R.drawable.water_image_png),
            UserActivityModel("Profit", R.drawable.profit_image_png)
        )
        val adapter = UserActivityAdapter(activityList) { item ->
            when (item.title) {
                "Order" -> findNavController().navigate(R.id.action_dashboardFragment_to_orderFragment)
                "Add merchant" -> findNavController().navigate(R.id.action_dashboardFragment_to_userFragment2)
                "Water" -> findNavController().navigate(R.id.action_dashboardFragment_to_waterFragment)
                "Profit" -> findNavController().navigate(R.id.action_dashboardFragment_to_orderFragment)
            }
        }

        dashboardBinding.recyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        dashboardBinding.recyclerview.adapter = adapter


    }

}

