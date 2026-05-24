package com.example.massmanager.Api_Otp.Data_Class

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

        private val repository = AuthRepository()

        private val _message = MutableStateFlow("")
        val message: StateFlow<String> = _message

        private val _loading = MutableStateFlow(false)
        val loading: StateFlow<Boolean> = _loading

        fun signup(
            name: String,
            email: String,
            number: String,
            password: String,
            usersid: Int
        ) {

            viewModelScope.launch {

                _loading.value = true   // ✅ start loading FIRST

                try {

                    val response = repository.signupUser(
                        name, email, number, password, usersid
                    )

                    if (response.isSuccessful) {

                        // ✅ proper response handling
                        val body = response.body()

//                        _message.value = body?.message ?: "Signup Success"

                    } else {

                        _message.value = "Signup Failed: ${response.code()}"
                    }

                } catch (e: Exception) {

                    _message.value = "Error: ${e.message}"

                } finally {

                    _loading.value = false  // ✅ always stop loading
                }
            }
        }
}