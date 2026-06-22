package com.example.massmanager.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient
import com.example.massmanager.Api_Otp.Data_Class.UserReport
import com.example.massmanager.Reprository.ReportRepository
import kotlinx.coroutines.launch

class ReportViewModel : ViewModel() {

    private val api = RetrofitClient.api
    private val repo = ReportRepository(api)

    var reportList = mutableStateOf<List<UserReport>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var successMessage = mutableStateOf<String?>(null)
        private set

    fun loadReport(userid: String) {
        viewModelScope.launch {

            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null

            try {
                val response = repo.getReport(userid)

                if (response.status == "success") {
                    reportList.value = response.data

                    // ✅ Success message
                    successMessage.value = "Data loaded successfully"
                } else {
                    errorMessage.value = "Server Error"
                    reportList.value = emptyList()
                }

            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Unknown Error"
                reportList.value = emptyList()
            } finally {
                isLoading.value = false
            }
        }
    }

    // optional
    fun clearMessages() {
        errorMessage.value = null
        successMessage.value = null
    }
}