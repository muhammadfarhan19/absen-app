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
import com.app.payroll.ui.attendance.AttendanceViewModel
import com.app.payroll.ui.salary.AdminSalaryFragment
import com.app.payroll.data.remote.api.NetworkConfig
import com.app.payroll.data.repository.AttendanceRepository
import com.app.payroll.utils.UiState
import com.app.payroll.utils.ViewModelFactory
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.payroll.ui.attendance.AdminAttendanceAdapter
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var authDataStore: AuthDataStore
    private lateinit var attendanceAdapter: AdminAttendanceAdapter

    private val attendanceViewModel: AttendanceViewModel by viewModels {
        val authDataStore = AuthDataStore(requireContext())
        val apiService = NetworkConfig.getApiService(authDataStore)
        val repository = AttendanceRepository(apiService)
        ViewModelFactory(authDataStore, attendanceRepository = repository)
    }

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
        attendanceAdapter = AdminAttendanceAdapter()

        setupUserData()
        setupAttendanceList()
        checkRole()
        observeAttendance()
        
        attendanceViewModel.getTodayAttendance()

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

    private fun observeAttendance() {
        attendanceViewModel.attendanceState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    val data = state.data
                    if (data != null) {
                        attendanceAdapter.submitList(listOf(data))
                        if (data.checkIn != null && data.checkOut == null) {
                            binding.btnCheckIn.text = "Check Out"
                            binding.btnCheckIn.isEnabled = true
                            binding.btnCheckIn.setOnClickListener {
                                attendanceViewModel.checkOut()
                            }
                        } else if (data.checkIn != null && data.checkOut != null) {
                            binding.btnCheckIn.text = "Completed"
                            binding.btnCheckIn.isEnabled = false
                        } else {
                            setupCheckInButton()
                        }
                    } else {
                        attendanceAdapter.submitList(emptyList())
                        setupCheckInButton()
                    }
                }
                is UiState.Error -> {
                    setupCheckInButton()
                }
                else -> {}
            }
        }

        attendanceViewModel.actionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.btnCheckIn.isEnabled = false
                    binding.btnCheckIn.text = "Loading..."
                }
                is UiState.Success -> {
                    binding.btnCheckIn.isEnabled = true
                    Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                    attendanceViewModel.getTodayAttendance()
                }
                is UiState.Error -> {
                    binding.btnCheckIn.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupCheckInButton() {
        binding.btnCheckIn.text = "Check In"
        binding.btnCheckIn.isEnabled = true
        binding.btnCheckIn.setOnClickListener {
            attendanceViewModel.checkIn()
        }
    }

    private fun setupUserData() {
        lifecycleScope.launch {
            val name = authDataStore.name.first() ?: "User"
            val role = authDataStore.role.first() ?: "Employee"
            
            binding.tvName.text = name
            binding.tvRole.text = role.capitalize(Locale.ROOT)
            
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val greeting = when (hour) {
                in 0..11 -> "Good Morning,"
                in 12..15 -> "Good Afternoon,"
                in 16..20 -> "Good Evening,"
                else -> "Good Night,"
            }
            binding.tvGreeting.text = greeting
            
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
            binding.tvDate.text = sdf.format(Date())
        }
    }

    private fun setupAttendanceList() {
        binding.rvAttendance.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAttendance.adapter = attendanceAdapter

        binding.tvViewAll.setOnClickListener {
            lifecycleScope.launch {
                val role = authDataStore.role.first()
                if (role == "admin") {
                    navigateTo(AdminAttendanceFragment())
                } else {
                    navigateTo(AttendanceFragment())
                }
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
