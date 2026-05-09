package com.app.payroll.ui.salary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.payroll.data.remote.api.NetworkConfig
import com.app.payroll.data.repository.SalaryRepository
import com.app.payroll.databinding.FragmentAdminSalaryBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.utils.UiState
import com.app.payroll.utils.ViewModelFactory

import android.app.DatePickerDialog
import com.app.payroll.R
import com.app.payroll.data.repository.EmployeeRepository
import com.app.payroll.ui.employee.EmployeeViewModel
import java.text.SimpleDateFormat
import java.util.*

class AdminSalaryFragment : Fragment() {

    private var _binding: FragmentAdminSalaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdminSalaryAdapter

    private val viewModel: SalaryViewModel by viewModels {
        val authDataStore = AuthDataStore(requireContext())
        val apiService = NetworkConfig.getApiService(authDataStore)
        val repository = SalaryRepository(apiService)
        ViewModelFactory(authDataStore, salaryRepository = repository)
    }

    private val employeeViewModel: EmployeeViewModel by viewModels {
        val authDataStore = AuthDataStore(requireContext())
        val apiService = NetworkConfig.getApiService(authDataStore)
        val repository = EmployeeRepository(apiService)
        ViewModelFactory(authDataStore, employeeRepository = repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminSalaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdminSalaryAdapter()
        binding.rvSalary.layoutManager = LinearLayoutManager(context)
        binding.rvSalary.adapter = adapter

        viewModel.getAllSalary()
        employeeViewModel.getAllEmployees()

        binding.btnCalculate.setOnClickListener {
            showMonthPicker()
        }

        viewModel.globalSalaryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(state.data)
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.calculateState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnCalculate.isEnabled = false
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnCalculate.isEnabled = true
                    Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnCalculate.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMonthPicker() {
        val calendar = Calendar.getInstance()
        val dialogView = layoutInflater.inflate(R.layout.dialog_month_picker, null)
        val monthPicker = dialogView.findViewById<android.widget.NumberPicker>(R.id.picker_month)
        val yearPicker = dialogView.findViewById<android.widget.NumberPicker>(R.id.picker_year)

        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        monthPicker.minValue = 0
        monthPicker.maxValue = 11
        monthPicker.displayedValues = months
        monthPicker.value = calendar.get(Calendar.MONTH)

        val year = calendar.get(Calendar.YEAR)
        yearPicker.minValue = year - 5
        yearPicker.maxValue = year + 5
        yearPicker.value = year

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Select Month and Year")
            .setView(dialogView)
            .setPositiveButton("Calculate") { _, _ ->
                val selectedMonth = String.format("%04d-%02d", yearPicker.value, monthPicker.value + 1)
                val employees = (employeeViewModel.employeesState.value as? UiState.Success)?.data
                if (employees != null) {
                    val userIds = employees.map { it.id }
                    viewModel.calculateAllSalaries(userIds, selectedMonth)
                } else {
                    Toast.makeText(context, "Employee list not loaded yet", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
