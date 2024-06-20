package com.capstone.plasticwise.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.adapter.ItemPostAdapter
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
        Glide.with(this)
            .load(R.drawable.ic_profile_user)
            .circleCrop()
            .into(binding.ivUser)

        binding.signOut.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.layoutAbout.setOnClickListener {
            startActivity(Intent(requireActivity(), AboutActivity::class.java))
        }

        setupRecyclerView()

        // Dummy list of items for RecyclerView


        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = gridLayoutManager



        binding.switchAppearance.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        auth = FirebaseAuth.getInstance()
    }

    private fun setupRecyclerView() {
        val adapter = ItemPostAdapter()
        binding.recyclerView.adapter = adapter
        val uid = auth.uid.toString()
        profileViewModel.getAllPostsByAuthor(uid).observe(requireActivity(), Observer { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showToast("Loading...")
                    }

                    is Result.Success -> {
                        showToast("Success")
                        val responsePost = result.data
                        if (responsePost.isNullOrEmpty()) {
                            binding.placeholderImage.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            binding.placeholderImage.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            adapter.submitList(responsePost)
                        }
                    }
                    is Result.Error -> {
                        showToast("Error")
                    }
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        auth.signOut()

        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(
            requireContext(),
            androidx.appcompat.R.style.AlertDialog_AppCompat
        )
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
