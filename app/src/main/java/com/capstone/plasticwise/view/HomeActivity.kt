package com.capstone.plasticwise.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.plasticwise.R
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityHomeBinding
import com.capstone.plasticwise.viewModel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class HomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Load theme preference
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        checkSession()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> navView.menu.findItem(R.id.nav_home).isChecked = true
                R.id.nav_post -> navView.menu.findItem(R.id.nav_post).isChecked = true
                R.id.nav_detect -> navView.menu.findItem(R.id.nav_detect).isChecked = true
                R.id.nav_profile -> navView.menu.findItem(R.id.nav_profile).isChecked = true
                R.id.nav_about -> navView.menu.findItem(R.id.nav_about).isChecked = true
            }
        }

        handleIntent(intent, navController, navView)
    }

    private fun handleIntent(
        intent: Intent,
        navController: NavController,
        navView: BottomNavigationView
    ) {
        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo == "detectFragment") {
            navController.navigate(R.id.nav_detect)
            navView.selectedItemId = R.id.nav_detect

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(
            intent,
            findNavController(R.id.nav_host_fragment_activity_home),
            binding.navView
        )
    }

    private fun checkSession() {
        val user = auth.currentUser
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("HomeActivity", "checkSession: ${task.result?.token}")
                } else {
                    Log.e("HomeActivity", "Token expired or error occurred: ${task.exception}")
                    navigateToUserActivity()
                }
            }
        } else {
            navigateToUserActivity()
        }
    }

    private fun navigateToUserActivity() {
        startActivity(Intent(this, UserActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishAffinity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        when (navController.currentDestination?.id) {
            R.id.nav_detect -> {
                navController.navigate(R.id.nav_home)
                binding.navView.selectedItemId = R.id.nav_home
            }

            else -> super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        checkSession()
    }

    override fun onPause() {
        super.onPause()
        checkSession()
    }
}
