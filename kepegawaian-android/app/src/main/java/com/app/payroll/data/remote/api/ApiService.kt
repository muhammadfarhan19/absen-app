package com.app.payroll.data.remote.api

import com.app.payroll.data.remote.dto.LoginRequest
import com.app.payroll.data.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse<LoginResponse>>

    // Attendance
    @POST("attendance/check-in")
    suspend fun checkIn(): Response<BaseResponse<Any>>

    @POST("attendance/check-out")
    suspend fun checkOut(): Response<BaseResponse<Any>>

    @GET("attendance/me")
    suspend fun getTodayAttendance(): Response<BaseResponse<AttendanceResponse>>

    // Salary
    @GET("salary/me")
    suspend fun getMySalary(): Response<BaseResponse<List<SalaryResponse>>>

    // Admin - Employee CRUD
    @GET("employees")
    suspend fun getAllEmployees(): Response<BaseResponse<List<UserResponse>>>

    @POST("employees")
    suspend fun createEmployee(@Body request: UserResponse): Response<BaseResponse<UserResponse>>

    @PUT("employees/{id}")
    suspend fun updateEmployee(@Path("id") id: Int, @Body request: UserResponse): Response<BaseResponse<UserResponse>>

    @DELETE("employees/{id}")
    suspend fun deleteEmployee(@Path("id") id: Int): Response<BaseResponse<Any>>

    // Admin - Reporting
    @GET("attendance")
    suspend fun getAllAttendance(): Response<BaseResponse<List<AttendanceResponse>>>

    @GET("salary")
    suspend fun getAllSalary(): Response<BaseResponse<List<SalaryResponse>>>

    @POST("salary/calculate")
    suspend fun calculateSalary(@Body request: Map<String, String>): Response<BaseResponse<Any>>
}
