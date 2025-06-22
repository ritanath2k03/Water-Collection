package com.rith.muski.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rith.muski.R
import com.rith.muski.adapters.UserListAdapter
import com.rith.muski.databinding.FragmentUserlistBinding
import com.rith.muski.viewmodel.AddUserViewModel

class UserListFragment:Fragment() {
    private lateinit var userlistBinding: FragmentUserlistBinding
    private lateinit var userViewModel : AddUserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userlistBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_userlist,container,false)
        userViewModel=ViewModelProvider(this)[AddUserViewModel::class.java]
        userlistBinding.lifecycleOwner = viewLifecycleOwner
        userlistBinding.merchantViewModel = userViewModel

        setRV()
        return  userlistBinding.root
    }

    private fun setRV() {
        val adapter = UserListAdapter() // you must implement UserAdapter
        userlistBinding.recyclerview.adapter = adapter

        userViewModel.userList.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }
        userViewModel.getAllUser()
    }
}