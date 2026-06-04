package com.example.massmanager.Reprository

import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient
import com.example.massmanager.Api_Otp.Data_Class.ScheduleResponse

class ScheduleRepository {

    suspend fun getSchedule(id: Int, startDate: String? = null): ScheduleResponse {
        return RetrofitClient.api.getSchedule(id, startDate)
    }
}