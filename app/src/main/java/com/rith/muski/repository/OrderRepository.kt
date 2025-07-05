package com.rith.muski.repository

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.ViewModelProvider
import com.rith.muski.Model.Order
import com.rith.muski.utils.DatabaseHelper
import com.rith.muski.utils.QueriesBuilder
import com.rith.muski.viewmodel.WaterViewModel

class OrderRepository(private val context: Context) {
    private val db = DatabaseHelper.getInstance(context)
    private lateinit var waterViewModel: WaterViewModel
    fun insertOrder(order: Order){
        val query=QueriesBuilder.insertOrder(order.u_id,order.o_amount,order.o_paid,order.o_due_payment,order.o_date,order.o_five_ml,order.o_one_l,order.o_twenty_l)
        db.executeQuery(query)
    }


    fun getOrdersByWaterId(wId: Int): Cursor {
        var query :String
        if(wId==1) {
             query = QueriesBuilder.getFiveHundredMlOrderByWaterId(wId)
            return db.readableDatabase.rawQuery(query,null)
        }
        if(wId==2){
            query =QueriesBuilder.getOneLOrderByWaterId(wId)
            return db.readableDatabase.rawQuery(query,null)
        }

            query = QueriesBuilder.getTwentyLOrderByWaterId(wId)
            return db.readableDatabase.rawQuery(query,null)




    }
    fun getOrderDataForPieChartInDashboard():Cursor{
        return  db.readableDatabase.rawQuery(QueriesBuilder.getOrderDataForPieChartInDashboard(),null)
    }

    fun getOrdersByWaterId(): Cursor {
        return db.readableDatabase.rawQuery(QueriesBuilder.getOrdersForOrderList(),null)
    }
}