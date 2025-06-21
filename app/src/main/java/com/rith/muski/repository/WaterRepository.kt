package com.rith.muski.repository

import android.content.Context
import com.rith.muski.Model.User
import com.rith.muski.Model.Water
import com.rith.muski.utils.DatabaseHelper
import com.rith.muski.utils.QueriesBuilder

class WaterRepository(private val context: Context) {
    private val db = DatabaseHelper.getInstance(context)

    fun insertWater() {
        if (!db.isWaterExists("Small Bottle", "500 mL")) {
            val smallBottle = QueriesBuilder.insertWater("Small Bottle", 10.00, "500 mL")
            db.writableDatabase.execSQL(smallBottle)
        }

        if (!db.isWaterExists("Medium Bottle", "1L")) {
            val mediumBottle = QueriesBuilder.insertWater("Medium Bottle", 20.00, "1L")
            db.writableDatabase.execSQL(mediumBottle)
        }

        if (!db.isWaterExists("Jar", "20L")) {
            val jarBottle = QueriesBuilder.insertWater("Jar", 30.00, "20L")
            db.writableDatabase.execSQL(jarBottle)
        }
    }

    fun fetchWater(): List<Water> {
        val list = mutableListOf<Water>()

        val cursor = db.readableDatabase.rawQuery("SELECT *  FROM water;", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("w_id"))
                val type = cursor.getString(cursor.getColumnIndexOrThrow("w_type"))
                val rate = cursor.getDouble(cursor.getColumnIndexOrThrow("w_rate"))
                val quantity = cursor.getString(cursor.getColumnIndexOrThrow("w_quantity"))
                list.add(Water(id, type, rate, quantity, null))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

}
