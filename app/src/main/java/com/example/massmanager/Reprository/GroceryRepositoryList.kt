package com.example.massmanager.Reprository

import com.example.massmanager.Api_Otp.Data_Class.ApiService
import com.example.massmanager.Api_Otp.Data_Class.GroceryResponse

class GroceryRepositoryList(private val api: ApiService) {

        suspend fun getGrocery(userId: String): GroceryResponse {
            return api.getGroceryData(userId)
        }
    }