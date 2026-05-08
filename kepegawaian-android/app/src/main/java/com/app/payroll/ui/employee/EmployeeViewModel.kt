package com.app.payroll.ui.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.payroll.data.remote.response.UserResponse
import com.app.payroll.data.repository.EmployeeRepository
import com.app.payroll.utils.UiState
import kotlinx.coroutines.launch

class EmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {

    private val _employeesState = MutableLiveData<UiState<List<UserResponse>>>()
    val employeesState: LiveData<UiState<List<UserResponse>>> = _employeesState

    fun getAllEmployees() {
        _employeesState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getAllEmployees()
                if (response.isSuccessful) {
                    _employeesState.value = UiState.Success(response.body()?.data ?: emptyList())
                } else {
                    _employeesState.value = UiState.Error("Failed to fetch employees")
                }
            } catch (e: Exception) {
                _employeesState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }
}
