package com.app.payroll.data.remote.response

data class LoginResponse(
    val user: UserResponse,
    val token: String
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)
