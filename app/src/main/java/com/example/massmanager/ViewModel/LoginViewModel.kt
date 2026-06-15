package com.example.massmanager.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.AuthRepository
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Api_Otp.Data_Class.UserData
import com.example.massmanager.Api_Otp.Data_Class.UsersLogin
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

    fun admin_login(email: String, password: String,context: Context) {

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

                        val sessionManager = SessionManager(context)
                        sessionManager.saveLogin(body.data?.id, body.data!!.email)


                        sessionManager.save(body.data.id,body.data.name)


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





    private val repository = AuthRepository()

    var loginState = mutableStateOf<UsersLogin?>(null)
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun user_login(email: String, password: String,context: Context) {
        viewModelScope.launch {
            try {
                isLoading.value = true

                val response = repository.user_login(email, password)

                if (response.status == "success") {
                    loginState.value = response

                    val sessionManager = SessionManager(context)
                    sessionManager.saveLogin(response.user_id, response.name)


                } else {
                    errorMessage.value = response.message



                }

            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Error"
            } finally {
                isLoading.value = false
            }
        }
    }

















}