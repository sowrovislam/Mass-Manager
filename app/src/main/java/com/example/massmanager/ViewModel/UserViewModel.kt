package com.example.massmanager.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient
import com.example.massmanager.Api_Otp.Data_Class.User
import com.example.massmanager.Reprository.UserRepository

import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val api = RetrofitClient.api
    private val repo = UserRepository(api)

    // 🔹 Users list
    var users = mutableStateOf<List<User>>(emptyList())
        private set

    // 🔹 Loading state
    var loading = mutableStateOf(false)
        private set

    // 🔹 Message (success / error)
    var message = mutableStateOf("")
        private set

    // 🔥 GET USERS
    fun loadUsers(usersid: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val res = repo.getUsers(usersid)

                if (res.success) {
                    users.value = res.data ?: emptyList()
                } else {
                    message.value = "No data found"
                }

            } catch (e: Exception) {
                message.value = e.message ?: "Error loading users"
            }
            loading.value = false
        }
    }

    // 🔥 DELETE USER
    fun deleteUser(email: String, usersid: String) {
        viewModelScope.launch {
            try {
                val res = repo.deleteUser(email)

                if (res.success) {
                    message.value = res.message
                    loadUsers(usersid) // refresh list
                } else {
                    message.value = "Delete failed"
                }

            } catch (e: Exception) {
                message.value = "Delete failed: ${'$'}{e.message}"
            }
        }
    }
}