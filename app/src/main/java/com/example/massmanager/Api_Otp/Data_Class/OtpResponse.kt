package com.example.massmanager.Api_Otp.Data_Class

data class OtpResponse(
    val status: Boolean,
    val message: String
)

data class VerifyResponse(
    val status: Boolean,
    val message: String,
    val sms: String? = null
)

data class ApiResponse(
    val status: String,
    val message: String
)