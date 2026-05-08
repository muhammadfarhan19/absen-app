package com.app.payroll.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.payroll.data.repository.AttendanceRepository
import com.app.payroll.data.repository.AuthRepository
import com.app.payroll.data.repository.EmployeeRepository
import com.app.payroll.data.repository.SalaryRepository
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.ui.attendance.AttendanceViewModel
import com.app.payroll.ui.auth.AuthViewModel
import com.app.payroll.ui.employee.EmployeeViewModel
import com.app.payroll.ui.salary.SalaryViewModel

class ViewModelFactory(
    private val authDataStore: AuthDataStore,
    private val authRepository: AuthRepository? = null,
    private val attendanceRepository: AttendanceRepository? = null,
    private val salaryRepository: SalaryRepository? = null,
    private val employeeRepository: EmployeeRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository!!, authDataStore) as T
            }
            modelClass.isAssignableFrom(AttendanceViewModel::class.java) -> {
                AttendanceViewModel(attendanceRepository!!) as T
            }
            modelClass.isAssignableFrom(SalaryViewModel::class.java) -> {
                SalaryViewModel(salaryRepository!!) as T
            }
            modelClass.isAssignableFrom(EmployeeViewModel::class.java) -> {
                EmployeeViewModel(employeeRepository!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
