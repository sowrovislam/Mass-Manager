package com.example.massmanager.Reprository



import com.example.massmanager.Api_Otp.Data_Class.ApiService
import com.example.massmanager.Api_Otp.Data_Class.BasicResponse
import com.example.massmanager.Api_Otp.Data_Class.GroceryRequest

class GroceryRepository(private val apiService: ApiService) {
    suspend fun saveBulk(request: GroceryRequest): BasicResponse {
        return apiService.saveBulk(request)
    }
}