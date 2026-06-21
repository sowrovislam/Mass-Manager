package com.example.massmanager.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.GroceryItem
import com.example.massmanager.Api_Otp.Data_Class.GroceryRequest
import com.example.massmanager.Api_Otp.Data_Class.RetrofitClient
import com.example.massmanager.Reprository.GroceryRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GroceryViewModel : ViewModel() {

    private val repo = GroceryRepository(RetrofitClient.api)

    var message = mutableStateOf("")
        private set

    var loading = mutableStateOf(false)
        private set

    fun saveAllItems(
        list: List<GroceryItem>,
        name: String,
        email: String,
        userid: String
    ) {
        if (list.isEmpty()) {
            message.value = "Please add at least one item first!"
            return
        }

        viewModelScope.launch {
            loading.value = true
            message.value = ""

            // ১. মোট প্রাইস হিসাব করা
            val totalAmount = list.sumOf {
                it.price.toDoubleOrNull() ?: 0.0
            }

            // ২. রিডেবল ডেট ফরম্যাট তৈরি করা
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())

            // ৩. রিকোয়েস্ট অবজেক্ট তৈরি
            val request = GroceryRequest(
                name = name,
                email = email,
                userid = userid,
                total = totalAmount.toString(),
                date = currentDate,
                items = list
            )

            try {
                // ৪. নেটওয়ার্ক কল
                val res = repo.saveBulk(request)
                message.value = res.message
            } catch (e: Exception) {
                message.value = "Connection Error: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }
}