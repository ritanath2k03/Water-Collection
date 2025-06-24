package com.rith.muski.viewmodel


import android.app.Application
import android.database.Cursor
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rith.muski.Model.User
import com.rith.muski.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddUserViewModel(application: Application) : AndroidViewModel(application) {
    val name = ObservableField<String>("")
    val email = ObservableField<String>("")
    val phone = ObservableField<String>("")
    val address = ObservableField<String>("")
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList
    private val repo = UserRepository(application)

    fun saveUser() {
    viewModelScope.launch(Dispatchers.IO){
        val user = User(
            name.get().orEmpty(),
            email.get().orEmpty(),
            phone.get().orEmpty(),
            address.get().orEmpty()
        )
        repo.insertUser(user)
    }

    }

    fun getAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val cursor = repo.getAllUser()
            val users = mutableListOf<User>()

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow("u_id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                    // Adjust fields based on your DB schema
                    users.add(
                        User(
                            name,
                            email,
                            phone,
                            address,
                            id
                        )
                    ) // fill in address or other fields
                } while (cursor.moveToNext())
            }

            cursor?.close()
            _userList.postValue(users)

        }
    }
}

