package com.app.payroll.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.payroll.data.remote.api.NetworkConfig
import com.app.payroll.data.repository.AttendanceRepository
import com.app.payroll.databinding.FragmentAdminAttendanceBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.utils.UiState
import com.app.payroll.utils.ViewModelFactory

class AdminAttendanceFragment : Fragment() {

    private var _binding: FragmentAdminAttendanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdminAttendanceAdapter

    private val viewModel: AttendanceViewModel by viewModels {
        val authDataStore = AuthDataStore(requireContext())
        val apiService = NetworkConfig.getApiService(authDataStore)
        val repository = AttendanceRepository(apiService)
        ViewModelFactory(authDataStore, attendanceRepository = repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdminAttendanceAdapter()
        binding.rvAttendance.layoutManager = LinearLayoutManager(context)
        binding.rvAttendance.adapter = adapter

        viewModel.getAllAttendance()

        viewModel.attendanceListState.observe(viewLifecycleOwner) { state ->
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
