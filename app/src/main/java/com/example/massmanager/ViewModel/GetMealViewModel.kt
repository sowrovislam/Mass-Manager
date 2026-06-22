package com.example.massmanager.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.MealMonthGroup
import com.example.massmanager.Reprository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetMealViewModel : ViewModel() {

    private val repo = MealRepository()

    private val _meals = MutableStateFlow<List<MealMonthGroup>>(emptyList())
    val meals = _meals

    private val _totalMeal = MutableStateFlow(0)
    val totalMeal = _totalMeal

    private val _loading = MutableStateFlow(false)
    val loading = _loading

    fun loadMeals(userid: String) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val response = repo.fetchMeals(userid)

                if (response.success) {
                    _meals.value = response.data
                    _totalMeal.value = response.totalMeal
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}