package com.app.payroll.data.remote.dto

data class SalaryCalculationRequest(
    val user_id: Int,
    val month: String
)
