package com.rith.muski.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.rith.muski.Model.UserActivityModel
import com.rith.muski.R
import com.rith.muski.adapters.UserActivityAdapter
import com.rith.muski.adapters.WaterAdapter
import com.rith.muski.databinding.FragmentDashboardBinding
import com.rith.muski.viewmodel.AddUserViewModel
import com.rith.muski.viewmodel.WaterViewModel

class DashboardFragment : Fragment() {
    private lateinit var waterViewmodel: WaterViewModel
    private lateinit var dashboardBinding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        waterViewmodel = ViewModelProvider(this)[WaterViewModel::class.java]
        waterViewmodel.insertInitialWater()
        dashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        dashboardBinding.lifecycleOwner = viewLifecycleOwner
        dashboardBinding.header.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_userFragment2)
        }
        setGrid()

        return dashboardBinding.root
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

        dashboardBinding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        dashboardBinding.recyclerview.adapter = adapter


    }

}

