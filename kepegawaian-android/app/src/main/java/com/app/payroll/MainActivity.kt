package com.app.payroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.payroll.databinding.ActivityMainBinding
import com.app.payroll.storage.AuthDataStore
import com.app.payroll.ui.auth.LoginFragment
import com.app.payroll.ui.dashboard.DashboardFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authDataStore: AuthDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        authDataStore = AuthDataStore(this)

        if (savedInstanceState == null) {
            checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            val token = authDataStore.token.first()
            val fragment = if (token != null) {
                DashboardFragment()
            } else {
                LoginFragment()
            }
            
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
