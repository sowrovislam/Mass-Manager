package com.example.massmanager.Api_Otp.Data_Class

class AuthRepository {

    private val api = RetrofitClient.api

    suspend fun register(
        name: String,
        email: String,
        number: String,
        password: String
    ): ApiResponse {
        return api.registerUser(name, email, number, password)
    }


    suspend fun signupUser(
        name: String,
        email: String,
        number: String,
        password: String,
        usersid: Int
    ) = RetrofitClient.api.signupUser(
        name,
        email,
        number,
        password,
        usersid
    )





    suspend fun  user_login(email: String, password: String): UsersLogin {
        return RetrofitClient.api.users_login(email, password)
    }


    suspend fun resetPassword(email: String, password: String): ForgotResponse {
        return RetrofitClient.api.resetPassword(email, password)
    }





}