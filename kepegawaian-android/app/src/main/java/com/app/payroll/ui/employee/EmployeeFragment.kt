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

        adapter = EmployeeAdapter()
        binding.rvEmployees.layoutManager = LinearLayoutManager(context)
        binding.rvEmployees.adapter = adapter

        viewModel.getAllEmployees()

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
