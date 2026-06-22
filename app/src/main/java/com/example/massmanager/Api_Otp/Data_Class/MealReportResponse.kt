package com.example.massmanager.Api_Otp.Data_Class

data class MealReportResponse(
    val status: String,
    val userid: String,
    val server_time: String,
    val data: List<MealData>
)

data class MealData(
    val month: String,
    val total_bajar: Double,
    val total_meals: Int,
    val meal_rate: String
)


//   get respons


data class ReportResponse(
    val status: String,
    val userid: String,
    val server_time: String,
    val data: List<UserReport>
)

data class UserReport(
    val month: String,
    val name: String,
    val email: String,
    val mess_total_bajar: Double,
    val mess_total_meals: Int,
    val meal_rate: String,
    val user_total_meals: Int,
    val user_meal_cost: String,
    val user_total_bajar: Double,
    val balance: String,
    val user_payment_amount: String,
    val payment_status: String
)