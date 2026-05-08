package com.app.payroll.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.payroll.R
import com.app.payroll.databinding.FragmentDashboardBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.ui.attendance.AttendanceFragment
import com.app.payroll.ui.auth.LoginFragment
import com.app.payroll.ui.employee.EmployeeFragment
import com.app.payroll.ui.salary.SalaryFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import com.app.payroll.ui.attendance.AdminAttendanceFragment
import com.app.payroll.ui.salary.AdminSalaryFragment

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var authDataStore: AuthDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authDataStore = AuthDataStore(requireContext())

        checkRole()

        binding.btnAttendance.setOnClickListener { navigateTo(AttendanceFragment()) }
        binding.btnSalary.setOnClickListener { navigateTo(SalaryFragment()) }
        
        binding.btnEmployeeList.setOnClickListener { navigateTo(EmployeeFragment()) }
        binding.btnAdminAttendance.setOnClickListener { navigateTo(AdminAttendanceFragment()) }
        binding.btnAdminSalary.setOnClickListener { navigateTo(AdminSalaryFragment()) }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                authDataStore.clearAuth()
                navigateTo(LoginFragment())
            }
        }
    }

    private fun checkRole() {
        lifecycleScope.launch {
            val role = authDataStore.role.first()
            if (role == "admin") {
                binding.btnEmployeeList.visibility = View.VISIBLE
                binding.btnAdminAttendance.visibility = View.VISIBLE
                binding.btnAdminSalary.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
