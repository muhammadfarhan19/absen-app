package com.app.payroll.data.remote.response

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val name: String?,
    val date: String,
    @SerializedName("check_in") val checkIn: String?,
    @SerializedName("check_out") val checkOut: String?,
    val status: String
)
