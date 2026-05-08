package com.app.payroll.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.payroll.R
import com.app.payroll.data.remote.api.NetworkConfig
import com.app.payroll.data.repository.AuthRepository
import com.app.payroll.databinding.FragmentLoginBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.ui.dashboard.DashboardFragment
import com.app.payroll.utils.UiState
import com.app.payroll.utils.ViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        val authDataStore = AuthDataStore(requireContext())
        val apiService = NetworkConfig.getApiService(authDataStore)
        val repository = AuthRepository(apiService)
        ViewModelFactory(authDataStore, authRepository = repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    navigateToDashboard()
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToDashboard() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DashboardFragment())
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
