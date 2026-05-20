package com.example.massmanager.Api_Otp.Data_Class

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class LoginViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user

    fun admin_login(email: String, password: String) {

        viewModelScope.launch {
            _loading.value = true
            _success.value = false   // reset every time

            try {
                val response = RetrofitClient.api.login(email, password)

                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!

                    _message.value = body.message

                    if (body.status == "success") {
                        _success.value = true
                        _user.value = body.data
                    } else {
                        _success.value = false
                    }

                } else {
                    _message.value = "Server Error"
                    _success.value = false
                }

            } catch (e: Exception) {
                _message.value = e.message ?: "Network Error"
                _success.value = false
            }

            _loading.value = false
        }
    }
}