package com.example.massmanager.Reprository

import com.example.massmanager.Api_Otp.Data_Class.ApiService
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient

class MainRepository {

    suspend fun getDue(userid: String, email: String, month: String) =
        RetrofitClient.api.getDue(userid, email, month)

    suspend fun insertPayment(userid: String, email: String, amount: String, date: String) =
        RetrofitClient.api.insertPayment(userid, email, amount, date)
}
