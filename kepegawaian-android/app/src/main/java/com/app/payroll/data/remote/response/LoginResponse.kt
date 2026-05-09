package com.app.payroll.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val user: UserResponse,
    val token: String
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    @SerializedName("gaji_pokok") val gajiPokok: Double? = null,
    @SerializedName("employee_id") val employeeId: Int? = null,
    val password: String? = null
)
