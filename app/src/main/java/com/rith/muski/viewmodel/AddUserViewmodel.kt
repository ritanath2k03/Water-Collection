package com.rith.muski.viewmodel


import android.app.Application
import android.database.Cursor
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.rith.muski.Model.User
import com.rith.muski.repository.UserRepository

class AddUserViewModel(application: Application) : AndroidViewModel(application) {
    val name = ObservableField<String>("")
    val email = ObservableField<String>("")
    val phone = ObservableField<String>("")
    val address = ObservableField<String>("")

    private val repo = UserRepository(application)

    fun saveUser() {

        val user = User(
            name.get().orEmpty(),
            email.get().orEmpty(),
            phone.get().orEmpty(),
            address.get().orEmpty()
        )
        repo.insertUser(user)
    }

    fun getAllUser(): Cursor {
        return repo.getAllUser()
    }
}

