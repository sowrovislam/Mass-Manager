package com.example.massmanager.Reprository

import com.example.massmanager.Api_Otp.Data_Class.ApiService
import com.example.massmanager.Api_Otp.Data_Class.MealReportResponse
import com.example.massmanager.Api_Otp.Data_Class.ReportResponse
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient

class Meal_Ret_Repository {


    suspend fun getMealReport(userId: String): MealReportResponse {
        return RetrofitClient.api.getMealReport(
            mapOf("userid" to userId)
        )
    }
}


//   all list of meail and rat

class ReportRepository(private val api: ApiService) {

    suspend fun getReport(userid: String): ReportResponse {
        return api.getReport(userid)
    }
}

