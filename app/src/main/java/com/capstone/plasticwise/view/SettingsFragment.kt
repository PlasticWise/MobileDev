// SettingsFragment.kt
package com.capstone.plasticwise.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.plasticwise.R
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.FragmentSettingsBinding
import com.capstone.plasticwise.viewModel.ProfileFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val profileViewModel by viewModels<ProfileFragmentViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        binding.switchAppearance.isChecked = isDarkMode

        profileViewModel.getSession().observe(viewLifecycleOwner) { result ->
            val username = result.username
            binding.tvProfile.text = username
            val email = result.email
            binding.tvEmail.text = email
        }

        binding.layoutLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.layoutAbout.setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_nav_about)
        }

        binding.switchAppearance.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun logout() {
        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().clear().apply()
        auth.signOut()

        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle("Confirmation Sign Out")
            .setMessage("Are you sure to Sign Out?")
            .setPositiveButton("Yes") { dialog, _ ->
                logout()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
