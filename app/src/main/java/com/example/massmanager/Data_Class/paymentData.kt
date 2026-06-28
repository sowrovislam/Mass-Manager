package com.example.massmanager.Data_Class


data class DueResponse(
    val status: String,
    val userid: String,
    val email: String,
    val name: String,
    val month: String,
    val due_amount: String
)

data class InsertResponse(
    val status: String,
    val message: String
)


//profile update korar jonne respons

data class ApiResponse(
    val status: String,
    val message: String
)