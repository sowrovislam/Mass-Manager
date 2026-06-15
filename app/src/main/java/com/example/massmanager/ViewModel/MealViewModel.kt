package com.example.massmanager.ViewModel



import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Reprository.MealRepository

import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {

    private val repo = MealRepository.MealRepository()

    // 🔹 Loading State
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    // 🔹 Message State (Success / Error)
    private val _message = mutableStateOf("")
    val message: State<String> = _message

    // 🔹 Success Flag
    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> = _isSuccess


    // 🚀 MAIN FUNCTION
    fun addMeal(
        userid: String,
        name: String,
        date: String,
        counter: String,
        isDupur: String,
        isRat: String,
        onResult: (String) -> Unit
    ) {

        // ✅ Validation (important)
        if (userid.isEmpty()|| name.isEmpty() || date.isEmpty()
            || counter.isEmpty()
        ) {
            _message.value = "All fields are required"
            _isSuccess.value = false
            return
        }

        viewModelScope.launch {

            _loading.value = true
            _message.value = ""

            try {
                val response = repo.addMeal(
                    userid,
                    name,
                    date,
                    counter,
                    isDupur,
                    isRat
                )

                if (response.success) {
                    _message.value = response.message
                    _isSuccess.value = true
                    onResult(response.message)
                } else {
                    _message.value = response.message
                    _isSuccess.value = false
                    onResult(response.message)
                }

            } catch (e: Exception) {
                _message.value = e.localizedMessage ?: "Something went wrong"
                _isSuccess.value = false
            }

            _loading.value = false
        }
    }
}