package com.example.massmanager.Api_Otp.Data_Class


data class DataMail(
    val id: String,
    val userid: String,
    val name: String,
    val date: String,
    val counter: String,
    val isDupur: String,
    val isRat: String,
    val email: String
)

data class MealMonthGroup(
    val month: String,
    val totalMeal: Int,
    val items: List<DataMail>
)

data class MealResponseList(
    val success: Boolean,
    val userid: String,
    val totalMeal: Int,
    val data: List<MealMonthGroup>
)




data class GroceryResponse(
    val success: Boolean,
    val userid: String,
    val grandTotalPrice: Double,
    val data: List<MonthlyData>
)

data class MonthlyData(
    val month: String,
    val totalPrice: Double,
    val items: List<GroceryList>
)

data class GroceryList(
    val id: String,
    val userid: String,
    val name: String,
    val email: String,
    val productname: String,
    val weight: String,
    val price: String,
    val total: String,
    val date: String
)