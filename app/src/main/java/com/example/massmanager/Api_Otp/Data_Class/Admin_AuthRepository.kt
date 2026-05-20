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
}