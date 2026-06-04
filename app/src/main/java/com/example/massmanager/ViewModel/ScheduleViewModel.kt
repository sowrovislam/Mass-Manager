package com.example.massmanager.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.massmanager.Api_Otp.Data_Class.ScheduleItem
import com.example.massmanager.Api_Otp.Data_Class.ScheduleResponse
import com.example.massmanager.Reprository.ScheduleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {

    private val repo = ScheduleRepository()

    private val _response = MutableStateFlow<ScheduleResponse?>(null)
    val response: StateFlow<ScheduleResponse?> = _response

    private val _todayItem = MutableStateFlow<ScheduleItem?>(null)
    val todayItem: StateFlow<ScheduleItem?> = _todayItem

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun load(id: Int, startDate: String? = null) {

        viewModelScope.launch {
            try {
                _loading.value = true

                val res = repo.getSchedule(id, startDate)

                _response.value = res

                val today = res.today

                // 🔥 TODAY ACTIVE FIND
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
}