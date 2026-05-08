package com.app.payroll.ui.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.payroll.data.remote.response.AttendanceResponse
import com.app.payroll.data.repository.AttendanceRepository
import com.app.payroll.utils.UiState
import kotlinx.coroutines.launch
import org.json.JSONObject

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    private val _attendanceState = MutableLiveData<UiState<AttendanceResponse?>>()
    val attendanceState: LiveData<UiState<AttendanceResponse?>> = _attendanceState

    private val _attendanceListState = MutableLiveData<UiState<List<AttendanceResponse>>>()
    val attendanceListState: LiveData<UiState<List<AttendanceResponse>>> = _attendanceListState

    private val _actionState = MutableLiveData<UiState<String>>()
    val actionState: LiveData<UiState<String>> = _actionState

    fun getTodayAttendance() {
        _attendanceState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getTodayAttendance()
                if (response.isSuccessful) {
                    _attendanceState.value = UiState.Success(response.body()?.data)
                } else {
                    _attendanceState.value = UiState.Error("Failed to fetch status")
                }
            } catch (e: Exception) {
                _attendanceState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }

    fun getAllAttendance() {
        _attendanceListState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getAllAttendance()
                if (response.isSuccessful) {
                    _attendanceListState.value = UiState.Success(response.body()?.data ?: emptyList())
                } else {
                    _attendanceListState.value = UiState.Error("Failed to fetch all attendance")
                }
            } catch (e: Exception) {
                _attendanceListState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }

    fun checkIn() {
        performAction { repository.checkIn() }
    }

    fun checkOut() {
        performAction { repository.checkOut() }
    }

    private fun performAction(call: suspend () -> retrofit2.Response<com.app.payroll.data.remote.response.BaseResponse<Any>>) {
        _actionState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = call()
                if (response.isSuccessful) {
                    _actionState.value = UiState.Success(response.body()?.message ?: "Success")
                    getTodayAttendance() // Refresh status
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "Error ${response.code()}"
                    }
                    _actionState.value = UiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }
}
