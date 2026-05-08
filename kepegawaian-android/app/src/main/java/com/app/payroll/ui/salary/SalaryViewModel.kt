package com.app.payroll.ui.salary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.payroll.data.remote.response.SalaryResponse
import com.app.payroll.data.repository.SalaryRepository
import com.app.payroll.utils.UiState
import kotlinx.coroutines.launch

class SalaryViewModel(private val repository: SalaryRepository) : ViewModel() {

    private val _salaryState = MutableLiveData<UiState<List<SalaryResponse>>>()
    val salaryState: LiveData<UiState<List<SalaryResponse>>> = _salaryState

    private val _globalSalaryState = MutableLiveData<UiState<List<SalaryResponse>>>()
    val globalSalaryState: LiveData<UiState<List<SalaryResponse>>> = _globalSalaryState

    private val _calculateState = MutableLiveData<UiState<String>>()
    val calculateState: LiveData<UiState<String>> = _calculateState

    fun getMySalary() {
        _salaryState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getMySalary()
                if (response.isSuccessful) {
                    _salaryState.value = UiState.Success(response.body()?.data ?: emptyList())
                } else {
                    _salaryState.value = UiState.Error("Failed to fetch salary data")
                }
            } catch (e: Exception) {
                _salaryState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }

    fun getAllSalary() {
        _globalSalaryState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getAllSalary()
                if (response.isSuccessful) {
                    _globalSalaryState.value = UiState.Success(response.body()?.data ?: emptyList())
                } else {
                    _globalSalaryState.value = UiState.Error("Failed to fetch all salary")
                }
            } catch (e: Exception) {
                _globalSalaryState.value = UiState.Error(e.message ?: "Error")
            }
        }
    }
}
