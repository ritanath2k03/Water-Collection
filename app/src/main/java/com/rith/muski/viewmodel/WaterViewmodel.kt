package com.rith.muski.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rith.muski.Model.Water
import com.rith.muski.repository.WaterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WaterViewModel(application:Application): AndroidViewModel(application){
    private val waterRepo=WaterRepository(application)
    private val _waterList = MutableLiveData<List<Water>>()
    val waterList: LiveData<List<Water>> = _waterList
    fun insertInitialWater(){
        waterRepo.insertWater()
    }
    fun loadWaterItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = waterRepo.fetchWater()
            _waterList.postValue(result)
        }
    }
}