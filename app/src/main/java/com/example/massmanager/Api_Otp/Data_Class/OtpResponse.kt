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


data class ScheduleResponse(
    val status: String,
    val searched_id: String,
    val admin_match: Int,
    val user_match: Int,
    val total_found: Int,
    val start_date_used: String,
    val today: String,
    val schedule: List<ScheduleItem>
)

data class ScheduleItem(
    val name: String,
    val email: String,
    val number: String,
    val start_date: String,
    val end_date: String
)

data class MealResponse(
    val success: Boolean,
    val message: String
)



data class MealsResponse(
    val success: Boolean,
    val today: String,
    val meals_list: List<MealItem>,
    val summary: Summary
)

data class MealItem(
    val id: Int,
    val userid: String,
    val name: String,
    val email: String,
    val date: String,
    val counter: Int,
    val isDupur: Int,
    val isRat: Int
)

data class Summary(
    val total_counter: Int,
    val total_dupur: Int,
    val total_rat: Int
)