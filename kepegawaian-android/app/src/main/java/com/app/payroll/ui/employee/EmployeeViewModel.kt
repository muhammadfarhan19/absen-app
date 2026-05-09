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

    private val _actionState = MutableLiveData<UiState<String>>()
    val actionState: LiveData<UiState<String>> = _actionState

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

    fun createEmployee(user: UserResponse) {
        _actionState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.createEmployee(user)
                if (response.isSuccessful) {
                    _actionState.value = UiState.Success(response.body()?.message ?: "Employee created")
                    getAllEmployees()
                } else {
                    _actionState.value = UiState.Error(response.body()?.message ?: "Failed to create")
                }
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }

    fun updateEmployee(id: Int, user: UserResponse) {
        _actionState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.updateEmployee(id, user)
                if (response.isSuccessful) {
                    _actionState.value = UiState.Success(response.body()?.message ?: "Employee updated")
                    getAllEmployees()
                } else {
                    _actionState.value = UiState.Error(response.body()?.message ?: "Failed to update")
                }
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }

    fun deleteEmployee(id: Int) {
        _actionState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.deleteEmployee(id)
                if (response.isSuccessful) {
                    _actionState.value = UiState.Success(response.body()?.message ?: "Employee deleted")
                    getAllEmployees()
                } else {
                    _actionState.value = UiState.Error(response.body()?.message ?: "Failed to delete")
                }
            } catch (e: Exception) {
                _actionState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }
}
