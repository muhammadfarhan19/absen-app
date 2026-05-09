package com.app.payroll.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.payroll.data.remote.api.NetworkConfig
import com.app.payroll.data.repository.AttendanceRepository
import com.app.payroll.databinding.FragmentAttendanceBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.utils.UiState
import com.app.payroll.utils.ViewModelFactory

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTodayAttendance()

        binding.btnCheckIn.setOnClickListener { viewModel.checkIn() }
        binding.btnCheckOut.setOnClickListener { viewModel.checkOut() }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.attendanceState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = state.data
                    if (data != null) {
                        binding.tvStatus.text = "Status: ${data.status}"
                        binding.tvCheckInTime.text = "Check-in: ${data.checkIn ?: "-"}"
                        binding.tvCheckOutTime.text = "Check-out: ${data.checkOut ?: "-"}"
                        
                        binding.btnCheckIn.isEnabled = data.checkIn == null
                        binding.btnCheckOut.isEnabled = data.checkIn != null && data.checkOut == null
                    } else {
                        binding.tvStatus.text = "Status: Belum Absen"
                        binding.btnCheckIn.isEnabled = true
                        binding.btnCheckOut.isEnabled = false
                    }
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
                    viewModel.getTodayAttendance()
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
