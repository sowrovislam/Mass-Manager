package com.example.massmanager.Reprository

import com.example.massmanager.Api_Otp.Data_Class.MealResponse
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient

class MealRepository {

    class MealRepository {

        suspend fun addMeal(
            userid: String,
            name: String,
            date: String,
            counter: String,
            isDupur: String,
            isRat: String
        ): MealResponse {

            return RetrofitClient.api.addMeal(
                userid, name, date, counter, isDupur,isRat
            )
        }
    }
}