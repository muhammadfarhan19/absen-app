package com.app.payroll.data.repository

import com.app.payroll.data.remote.api.ApiService
import com.app.payroll.data.remote.response.AttendanceResponse
import com.app.payroll.data.remote.response.BaseResponse
import retrofit2.Response

class AttendanceRepository(private val apiService: ApiService) {
    suspend fun checkIn(): Response<BaseResponse<Any>> = apiService.checkIn()
    suspend fun checkOut(): Response<BaseResponse<Any>> = apiService.checkOut()
    suspend fun getTodayAttendance(): Response<BaseResponse<AttendanceResponse>> = apiService.getTodayAttendance()
    suspend fun getAllAttendance(): Response<BaseResponse<List<AttendanceResponse>>> = apiService.getAllAttendance()
}
