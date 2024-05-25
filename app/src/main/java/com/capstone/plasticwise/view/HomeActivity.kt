package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.plasticwise.R
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityHomeBinding
import com.capstone.plasticwise.viewModel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class
HomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityHomeBinding

    private var isButton : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getSession().observe(this) { session ->
            if (!session.isLogin) {
                finish()
                startActivity(Intent(this@HomeActivity, UserActivity::class.java))
            }
        }



        val homeFragment = HomeFragment()
        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                setTitle("Logout")
                setMessage("Are you sure want to logout?")
                setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                    finish()
                    supportFragmentManager.beginTransaction().remove(homeFragment).commit()
                    startActivity(Intent(this@HomeActivity, UserActivity::class.java))
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navContrroller = findNavController(R.id.nav_host_fragment_activity_home)

        navView.setupWithNavController(navContrroller)

        setupNavigation(navContrroller)
        setupButton(isButton)
    }

    private fun setupButton(showButton: Boolean) {
        isButton = showButton
        if (showButton) {
            binding.btnLogout.visibility = android.view.View.VISIBLE
        } else {
            binding.btnLogout.visibility = android.view.View.GONE
        }
    }

    private fun setupNavigation(navContrroller: NavController) {
        navContrroller.addOnDestinationChangedListener{_, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> {
                    setupButton(true)
                } R.id.nav_profile -> {
                    setupButton(false)
                }
            }
        }
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
        super.onBackPressed()
        finishAffinity()
    }

}