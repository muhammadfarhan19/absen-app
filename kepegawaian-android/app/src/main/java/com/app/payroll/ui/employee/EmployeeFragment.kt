package com.app.payroll.ui.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.payroll.data.remote.api.NetworkConfig
import com.app.payroll.data.repository.EmployeeRepository
import com.app.payroll.databinding.FragmentEmployeeBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.utils.UiState
import com.app.payroll.utils.ViewModelFactory

import androidx.appcompat.app.AlertDialog
import com.app.payroll.data.remote.response.UserResponse
import com.app.payroll.databinding.DialogEmployeeBinding

class EmployeeFragment : Fragment() {

    private var _binding: FragmentEmployeeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EmployeeAdapter

    private val viewModel: EmployeeViewModel by viewModels {
        val authDataStore = AuthDataStore(requireContext())
        val apiService = NetworkConfig.getApiService(authDataStore)
        val repository = EmployeeRepository(apiService)
        ViewModelFactory(authDataStore, employeeRepository = repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EmployeeAdapter(
            onEdit = { showAddEditDialog(it) },
            onDelete = { showDeleteConfirmation(it) }
        )
        binding.rvEmployees.layoutManager = LinearLayoutManager(context)
        binding.rvEmployees.adapter = adapter

        viewModel.getAllEmployees()

        binding.fabAdd.setOnClickListener { showAddEditDialog(null) }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.employeesState.observe(viewLifecycleOwner) { state ->
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

        viewModel.actionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showAddEditDialog(user: UserResponse?) {
        val dialogBinding = DialogEmployeeBinding.inflate(layoutInflater)
        val isEdit = user != null
        
        if (isEdit) {
            dialogBinding.etName.setText(user?.name)
            dialogBinding.etEmail.setText(user?.email)
            dialogBinding.etSalary.setText(user?.gajiPokok?.toLong()?.toString())
            dialogBinding.tilPassword.visibility = View.GONE
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (isEdit) "Edit Employee" else "Add New Employee")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val name = dialogBinding.etName.text.toString()
                val email = dialogBinding.etEmail.text.toString()
                val password = dialogBinding.etPassword.text.toString()
                val salary = dialogBinding.etSalary.text.toString().toDoubleOrNull() ?: 0.0

                val updatedUser = UserResponse(
                    id = user?.id ?: 0,
                    name = name,
                    email = email,
                    role = user?.role ?: "karyawan",
                    gajiPokok = salary,
                    password = if (isEdit) null else password
                )

                if (isEdit) {
                    viewModel.updateEmployee(user!!.id, updatedUser)
                } else {
                    viewModel.createEmployee(updatedUser)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmation(user: UserResponse) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete ${user.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteEmployee(user.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
