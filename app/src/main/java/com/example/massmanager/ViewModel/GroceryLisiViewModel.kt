package com.example.massmanager.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.MonthlyData
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient
import com.example.massmanager.Reprository.GroceryRepositoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroceryLisiViewModel: ViewModel() {

    private val api = RetrofitClient.api
    private val repo = GroceryRepositoryList(api)

    private val _data = MutableStateFlow<List<MonthlyData>>(emptyList())
    val data: StateFlow<List<MonthlyData>> = _data

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    fun loadData(userId: String) {
        viewModelScope.launch {
            try {
                val response = repo.getGrocery(userId)
                if (response.success) {
                    _data.value = response.data
                    _total.value = response.grandTotalPrice
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}