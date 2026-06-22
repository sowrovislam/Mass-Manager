package com.example.massmanager.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.massmanager.Api_Otp.Data_Class.MealData
import com.example.massmanager.Reprository.Meal_Ret_Repository

class Meal_Ret_ViewModel: ViewModel() {


        var mealList by mutableStateOf<List<MealData>>(emptyList())
            private set

        var isLoading by mutableStateOf(false)
        var error by mutableStateOf("")

        private val repository = Meal_Ret_Repository()

        fun loadMealData(userId: String) {
            viewModelScope.launch {
                isLoading = true
                try {
                    val response = repository.getMealReport(userId)
                    if (response.status == "success") {
                        mealList = response.data
                    } else {
                        error = "Failed"
                    }
                } catch (e: Exception) {
                    error = e.message ?: "Unknown error"
                }
                isLoading = false
            }
        }









}