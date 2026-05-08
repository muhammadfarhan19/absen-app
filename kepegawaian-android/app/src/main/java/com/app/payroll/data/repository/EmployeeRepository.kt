package com.app.payroll.data.repository

import com.app.payroll.data.remote.api.ApiService
import com.app.payroll.data.remote.response.BaseResponse
import com.app.payroll.data.remote.response.UserResponse
import retrofit2.Response

class EmployeeRepository(private val apiService: ApiService) {
    suspend fun getAllEmployees(): Response<BaseResponse<List<UserResponse>>> = apiService.getAllEmployees()
    suspend fun createEmployee(user: UserResponse) = apiService.createEmployee(user)
    suspend fun updateEmployee(id: Int, user: UserResponse) = apiService.updateEmployee(id, user)
    suspend fun deleteEmployee(id: Int) = apiService.deleteEmployee(id)
}
