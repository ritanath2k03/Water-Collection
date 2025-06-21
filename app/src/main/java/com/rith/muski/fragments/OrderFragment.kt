package com.rith.muski.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rith.muski.R
import com.rith.muski.databinding.FragmentOrderBinding

class OrderFragment: Fragment() {
    private  lateinit var orderBinding:FragmentOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_order,container,false)
        return orderBinding.root
    }
}