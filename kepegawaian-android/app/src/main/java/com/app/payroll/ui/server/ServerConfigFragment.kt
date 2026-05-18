package com.app.payroll.ui.server

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.payroll.R
import com.app.payroll.databinding.FragmentServerConfigBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.ui.auth.LoginFragment
import com.app.payroll.ui.dashboard.DashboardFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Fragment untuk konfigurasi IP/URL server backend.
 *
 * [fromDashboard] = true  → ditampilkan dari tombol edit di Dashboard (ada tombol Batal).
 * [fromDashboard] = false → ditampilkan sebelum login (wajib isi dulu).
 */
class ServerConfigFragment : Fragment() {

    private var _binding: FragmentServerConfigBinding? = null
    private val binding get() = _binding!!
    private lateinit var authDataStore: AuthDataStore

    /** Jika true, berarti fragment ini dibuka dari Dashboard (bukan first-run). */
    var fromDashboard: Boolean = false

    companion object {
        fun newInstance(fromDashboard: Boolean = false): ServerConfigFragment {
            return ServerConfigFragment().apply {
                this.fromDashboard = fromDashboard
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServerConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authDataStore = AuthDataStore(requireContext())

        // Tampilkan URL yang tersimpan (jika ada)
        lifecycleScope.launch {
            val savedUrl = authDataStore.baseUrl.first()
            if (!savedUrl.isNullOrBlank()) {
                binding.etServerUrl.setText(savedUrl)
            }
        }

        // Tombol Batal hanya tampil saat dibuka dari Dashboard
        if (fromDashboard) {
            binding.tvCancel.visibility = View.VISIBLE
            binding.btnSaveServer.text = "Simpan"
            binding.tvSubtitle.text = "Perbarui IP atau URL backend server"
        }

        binding.tvCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSaveServer.setOnClickListener {
            val input = binding.etServerUrl.text.toString().trim()
            if (input.isEmpty()) {
                Toast.makeText(context, "URL server tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                authDataStore.saveBaseUrl(input)
                Toast.makeText(context, "URL server disimpan", Toast.LENGTH_SHORT).show()

                if (fromDashboard) {
                    // Kembali ke Dashboard
                    parentFragmentManager.popBackStack()
                } else {
                    // Lanjut ke Login
                    navigateTo(LoginFragment())
                }
            }
        }
    }

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
