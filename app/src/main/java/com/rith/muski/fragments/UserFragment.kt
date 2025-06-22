package com.rith.muski.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.rith.muski.Model.User
import com.rith.muski.R
import com.rith.muski.databinding.FragmentUserBinding
import com.rith.muski.viewmodel.AddUserViewModel

class UserFragment:Fragment() {
       private lateinit var userViewModel : AddUserViewModel
    private lateinit var binding: FragmentUserBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userViewModel = ViewModelProvider(this)[AddUserViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = userViewModel

        binding.buttonSave.setOnClickListener {
            userViewModel.saveUser()
            binding.inputName.setText("")
            binding.inputEmail.setText("")
            binding.inputPhone.setText("")
            binding.inputAddress.setText("")

        }

    return  binding.root
    }
}