package com.example.massmanager.Api_Otp.Data_Class

    data class User(
        val id: String,
        val name: String,
        val email: String,
        val number: String
    )

    data class UserResponse(
        val success: Boolean,
        val data: List<User>
    )

    data class DeleteResponse(
        val success: Boolean,
        val message: String
    )
