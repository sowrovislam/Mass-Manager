package com.example.massmanager.Api_Otp.Data_Class

import com.example.massmanager.Data_Class.DueResponse
import com.example.massmanager.Data_Class.InsertResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("send_otp.php")
    suspend fun sendOtp(
        @Field("email") email: String
    ): Response<OtpResponse>

    @FormUrlEncoded
    @POST("verify_otp.php")
    suspend fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Response<VerifyResponse>


    @FormUrlEncoded
    @POST("admin_signup.php")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("number") number: String,
        @Field("password") password: String
    ): ApiResponse

    @FormUrlEncoded
    @POST("login_admin.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("users_signup.php") // your php file name
    suspend fun signupUser(

        @Field("name") name: String,
        @Field("email") email: String,
        @Field("number") number: String,
        @Field("password") password: String,
        @Field("usersid") usersid: Int

    ): Response<UserList>





    @FormUrlEncoded
    @POST("users_login.php")
    suspend fun users_login(
        @Field("email") email: String,
        @Field("password") password: String
    ): UsersLogin


    @FormUrlEncoded
    @POST("get_total_count.php")
    suspend fun getSchedule(
        @Field("id") id: Int,
        @Field("start_date") startDate: String? = null
    ): ScheduleResponse


    @FormUrlEncoded
    @POST("save_meals.php")
    suspend fun addMeal(
        @Field("userid") userid: String,
        @Field("name") name: String,
        @Field("date") date: String,
        @Field("counter") counter: String,
        @Field("isDupur") isDupur: String,
        @Field("isRat") isRat: String,
        @Field("email") email: String?
    ): MealResponse
//meal dekar jonne
    @FormUrlEncoded
    @POST("get_meals.php")
    suspend fun getMeals(
        @Field("userid") userid: String
    ): MealsResponse

// meal delete koarr jonne
    @FormUrlEncoded
    @POST("delete_meal.php")
    suspend fun deleteMeal(
        @Field("email") email: String,
        @Field("date") date: String
    ): DeleteMealResponse

    // user ar sokol data ger koar jonne
    @FormUrlEncoded
    @POST("get_user_all_data.php")
    suspend fun getUsers(
        @Field("usersid") usersid: String
    ): UserResponse

//    user ke delate koarr jonne
    @FormUrlEncoded
    @POST("delete_user.php")
    suspend fun deleteUser(
        @Field("email") email: String
    ): DeleteResponse

// bajar list save koar jonne
    @POST("save_grocery.php")
    suspend fun saveBulk(
        @Body request: GroceryRequest
    ): BasicResponse

    // total choloman meal
    @FormUrlEncoded
    @POST("get_meals_list.php")
    suspend fun getMealsList(
        @Field("userid") userid: String
    ): MealResponseList

// bajar ar list dekhar jonne
    @FormUrlEncoded
    @POST("grocery_list.php")
    suspend fun getGroceryData(
        @Field("userid") userId: String
    ): GroceryResponse

// total meal or due dekhar joonne
    @POST("total_meal_total_data.php")
    suspend fun getMealReport(
        @Body request: Map<String, String>
    ): MealReportResponse


//    meal_rate_total _summary.php
    @FormUrlEncoded
    @POST("save_payment_by_admin.php")
    suspend fun getReport(
        @Field("userid") userid: String
    ): ReportResponse


    //due chak korar jonne
    @FormUrlEncoded
    @POST("check_due.php")
    suspend fun getDue(
        @Field("userid") userid: String,
        @Field("email") email: String,
        @Field("month") month: String
    ): Response<DueResponse>

// prement insert korar jonne
    @FormUrlEncoded
    @POST("insert_payment.php")
    suspend fun insertPayment(
        @Field("userid") userid: String,
        @Field("email") email: String,
        @Field("amount") amount: String,
        @Field("date") date: String
    ): Response<InsertResponse>

    //Forgate Password



    @FormUrlEncoded
    @POST("forgot_password.php")
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("password") password: String
    ): ForgotResponse





}



object RetrofitClient {

    private const val BASE_URL = "http://nilferi.site/sowrovm/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}