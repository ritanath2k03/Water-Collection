package com.rith.muski.repository

import android.content.Context
import android.database.Cursor
import com.rith.muski.Model.User
import com.rith.muski.utils.DatabaseHelper
import com.rith.muski.utils.QueriesBuilder

class UserRepository(private  val  context: Context) {
    private val db = DatabaseHelper.getInstance(context)
    fun insertUser(user: User) {
        val query = QueriesBuilder.insertUser(user.name, user.email, user.phone, user.address)
        db.executeQuery(query)
    }

    fun getAllUser(): Cursor {
    val query=QueriesBuilder.getAllUsers()
        return db.fetchQuery(query)
    }
}