package com.rith.muski.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rith.muski.Model.Order

import com.rith.muski.repository.OrderRepository
import org.json.JSONObject


class OrderViewmodel(application: Application): AndroidViewModel(application) {
    val orderRepository=OrderRepository(application)
    private val _orders = MutableLiveData<List<org.json.JSONObject>>()
    val orders: LiveData<List<org.json.JSONObject>> = _orders
    fun insertSampleOrder(order:Order){
    orderRepository.insertOrder(order)
    }
    fun loadOrdersByWaterId(wId: Int) {
        val cursor = OrderRepository(getApplication()).getOrdersByWaterId(wId)
        val list = mutableListOf<JSONObject>()

        while (cursor.moveToNext()) {
            val orderObj = JSONObject()
            val userObj = JSONObject()

            // extract order fields
            orderObj.put("o_id", cursor.getLong(cursor.getColumnIndexOrThrow("o_id")))
            orderObj.put("o_amount", cursor.getDouble(cursor.getColumnIndexOrThrow("o_amount")))
            orderObj.put("o_date", cursor.getString(cursor.getColumnIndexOrThrow("o_date")))
            orderObj.put("o_paid", cursor.getDouble(cursor.getColumnIndexOrThrow("o_paid")))
            orderObj.put("o_due_payment", cursor.getDouble(cursor.getColumnIndexOrThrow("o_due_payment")))
            orderObj.put("o_five_ml", cursor.getDouble(cursor.getColumnIndexOrThrow("o_five_ml")))
            orderObj.put("o_one_l", cursor.getDouble(cursor.getColumnIndexOrThrow("o_one_l")))
            orderObj.put("o_twenty_l", cursor.getDouble(cursor.getColumnIndexOrThrow("o_twenty_l")))


            // extract user fields
            userObj.put("u_id", cursor.getInt(cursor.getColumnIndexOrThrow("u_id")))
            userObj.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")))
            userObj.put("address", cursor.getString(cursor.getColumnIndexOrThrow("address")))

            // combine
            val mainObj = JSONObject()
            mainObj.put("order", orderObj)
            mainObj.put("user", userObj)

            list.add(mainObj)
        }

        cursor.close()
        _orders.postValue(list)
    }


}