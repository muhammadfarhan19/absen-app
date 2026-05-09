package com.app.payroll.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.payroll.data.remote.dto.LoginRequest
import com.app.payroll.data.remote.response.LoginResponse
import com.app.payroll.data.repository.AuthRepository
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.utils.UiState
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel(
    private val repository: AuthRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<LoginResponse>>()
    val loginState: LiveData<UiState<LoginResponse>> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true && body.data != null) {
                        authDataStore.saveToken(body.data.token)
                        authDataStore.saveRole(body.data.user.role)
                        authDataStore.saveName(body.data.user.name)
                        _loginState.value = UiState.Success(body.data)
                    } else {
                        _loginState.value = UiState.Error(body?.message ?: "Login failed")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "Error ${response.code()}"
                    }
                    _loginState.value = UiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authDataStore.clearAuth()
        }
    }
}
