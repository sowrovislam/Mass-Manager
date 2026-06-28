package com.example.massmanager.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.massmanager.Data_Class.DueResponse
import com.example.massmanager.Reprository.MainRepository

class MainViewModel : ViewModel() {

    private val repo = MainRepository()

    var dueData by mutableStateOf<DueResponse?>(null)
    var message by mutableStateOf("")
    var loading by mutableStateOf(false)

    fun fetchDue(userid: String, email: String, month: String) {
        viewModelScope.launch {
            loading = true
            try {
                val res = repo.getDue(userid, email, month)
                if (res.isSuccessful) {
                    dueData = res.body()
                }
            } catch (e: Exception) {
                message = e.message.toString()
            }
            loading = false
        }
    }

    fun insertPayment(userid: String, email: String, amount: String, date: String) {
        viewModelScope.launch {
            loading = true
            try {
                val res = repo.insertPayment(userid, email, amount, date)
                if (res.isSuccessful) {
                    message = res.body()?.message ?: "Success"
                }
            } catch (e: Exception) {
                message = e.message.toString()
            }
            loading = false
        }
    }
}