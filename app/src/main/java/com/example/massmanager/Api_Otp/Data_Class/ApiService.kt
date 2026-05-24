package com.example.massmanager.Api_Otp.Data_Class

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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