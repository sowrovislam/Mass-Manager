package com.example.massmanager.Api_Otp.Data_Class

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OtpViewModel : ViewModel() {


    private val repository = AuthRepository()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message










    private val api = RetrofitClient.api

    private val _otpState = MutableStateFlow("")
    val otpState: StateFlow<String> = _otpState

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    private val _status1 = MutableStateFlow("")
    val status1: StateFlow<String> = _status1

    fun sendOtp(email: String) {
        viewModelScope.launch {
            try {
                val res = api.sendOtp(email)
                if (res.isSuccessful) {
                    _status1.value = res.body()?.message ?: "Sucessfull"
                } else {
                    _status1.value ="Error"
                }
            } catch (e: Exception) {
                _status1.value = e.message ?: "Exception"
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            try {
                val res = api.verifyOtp(email, otp)

                if (res.isSuccessful && res.body()?.status == true) {
                    _status.value = "OTP Verified Success"
                } else {
                    _status.value = "Invalid OTP"
                }

            } catch (e: Exception) {
                _status.value = e.message ?: "Exception"
            }
        }
    }

    fun setOtp(value: String) {
        _otpState.value = value
    }



    fun register(name: String, email: String, number: String, password: String) {

        viewModelScope.launch {
            _loading.value = true

            try {
                val response = repository.register(name, email, number, password)
                _message.value = response.message

            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }

            _loading.value = false
        }
    }













}