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


data class LoginResponse(
    val status: String,
    val message: String,
    val data: UserData? = null
)




data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val number: String
)
data class UserList(
    val status: String,
    val message: String,
    val name: String,
    val email: String,
    val number: String,
    val password: String,
    val usersid: String
)



data class UsersLogin(
    val status: String,
    val message: String,
    val user_id: Int,
    val name: String,
    val email: String
)