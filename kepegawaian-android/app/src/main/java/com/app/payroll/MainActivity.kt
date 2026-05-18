package com.app.payroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.app.payroll.databinding.ActivityMainBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.ui.auth.LoginFragment
import com.app.payroll.ui.dashboard.DashboardFragment
import com.app.payroll.ui.server.ServerConfigFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authDataStore: AuthDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        authDataStore = AuthDataStore(this)

        if (savedInstanceState == null) {
            checkInitialScreen()
        }
    }

    private fun checkInitialScreen() {
        lifecycleScope.launch {
            val baseUrl = authDataStore.baseUrl.first()
            val token = authDataStore.token.first()

            val fragment = when {
                baseUrl.isNullOrBlank() -> ServerConfigFragment.newInstance(fromDashboard = false)
                token != null -> DashboardFragment()
                else -> LoginFragment()
            }
            
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
