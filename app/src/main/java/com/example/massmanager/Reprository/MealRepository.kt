package com.example.massmanager.Reprository


import com.example.massmanager.Api_Otp.Data_Class.MealResponse
import com.example.massmanager.Api_Otp.Data_Class.MealResponseList
import com.example.massmanager.Api_Otp.Data_Class.MealsResponse
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient

class MealRepository {

        suspend fun addMeal(
            userid: String,
            name: String,
            date: String,
            counter: String,
            isDupur: String,
            isRat: String,
            email: String?
        ): MealResponse {

            return RetrofitClient.api.addMeal(
                userid, name, date, counter, isDupur,isRat,email
            )
        }




    suspend fun getMeals(userid: String): MealsResponse {
        return RetrofitClient.api.getMeals(userid)
    }



    suspend fun deleteMeal(email: String, date: String) =
        RetrofitClient.api.deleteMeal(email, date)


    suspend fun fetchMeals(userid: String): MealResponseList {
        return RetrofitClient.api.getMealsList(userid)
    }







}