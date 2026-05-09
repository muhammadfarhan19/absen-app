package com.app.payroll.data.repository

import com.app.payroll.data.remote.api.ApiService
import com.app.payroll.data.remote.dto.SalaryCalculationRequest
import com.app.payroll.data.remote.response.BaseResponse
import com.app.payroll.data.remote.response.SalaryResponse
import retrofit2.Response

class SalaryRepository(private val apiService: ApiService) {
    suspend fun getMySalary(): Response<BaseResponse<List<SalaryResponse>>> = apiService.getMySalary()
    suspend fun getAllSalary(): Response<BaseResponse<List<SalaryResponse>>> = apiService.getAllSalary()
    suspend fun calculateSalary(userId: Int, month: String): Response<BaseResponse<Any>> {
        return apiService.calculateSalary(SalaryCalculationRequest(userId, month))
    }
}
