package com.rith.muski.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val orderAdapter = OrderAdapter()
        waterBinding.orderrv.adapter = orderAdapter

        orderViewModel.orders.observe(viewLifecycleOwner) {
            orderAdapter.submitList(it)
        }
    }
}