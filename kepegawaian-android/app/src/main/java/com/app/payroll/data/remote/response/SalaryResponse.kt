package com.app.payroll.data.remote.response

import com.google.gson.annotations.SerializedName

data class SalaryResponse(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val name: String?,
    val month: String,
    @SerializedName("total_gaji") val totalGaji: Double,
    @SerializedName("total_potongan") val totalPotongan: Double
)
