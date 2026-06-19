package com.example.massmanager.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.*
import com.example.massmanager.Reprository.MealRepository
import com.example.massmanager.Reprository.ScheduleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    // ===================== SCHEDULE =====================

    private val scheduleRepo = ScheduleRepository()

    private val _response = MutableStateFlow<ScheduleResponse?>(null)
    val response: StateFlow<ScheduleResponse?> = _response

    private val _todayItem = MutableStateFlow<ScheduleItem?>(null)
    val todayItem: StateFlow<ScheduleItem?> = _todayItem

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun loadSchedule(id: Int, startDate: String? = null) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val res = scheduleRepo.getSchedule(id, startDate)
                _response.value = res

                val today = res.today

                _todayItem.value = res.schedule.find {
                    today >= it.start_date && today <= it.end_date
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }

    // ===================== MEALS =====================

    private val mealRepo = MealRepository()

    private val _meals = MutableStateFlow<List<MealItem>>(emptyList())
    val meals: StateFlow<List<MealItem>> = _meals

    private val _summary = MutableStateFlow<Summary?>(null)
    val summary: StateFlow<Summary?> = _summary

    private val _loadingMeals = MutableStateFlow(false)
    val loadingMeals: StateFlow<Boolean> = _loadingMeals

    private val _errorMeals = MutableStateFlow("")
    val errorMeals: StateFlow<String> = _errorMeals

    fun loadMeals(userid: String) {
        viewModelScope.launch {
            try {
                _loadingMeals.value = true

                val res = mealRepo.getMeals(userid)

                if (res.success) {
                    _meals.value = res.meals_list
                    _summary.value = res.summary
                } else {
                    _errorMeals.value = "Failed to load meals"
                }

            } catch (e: Exception) {
                _errorMeals.value = e.message ?: "Error"
            } finally {
                _loadingMeals.value = false
            }
        }
    }
}