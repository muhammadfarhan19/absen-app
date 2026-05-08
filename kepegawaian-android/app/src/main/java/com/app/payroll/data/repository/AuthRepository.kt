package com.app.payroll.data.repository

import com.app.payroll.data.remote.api.ApiService
import com.app.payroll.data.remote.dto.LoginRequest
import com.app.payroll.data.remote.response.BaseResponse
import com.app.payroll.data.remote.response.LoginResponse
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {
    suspend fun login(request: LoginRequest): Response<BaseResponse<LoginResponse>> {
        return apiService.login(request)
    }
}
