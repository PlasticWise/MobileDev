package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.FragmentSignupBinding
import com.capstone.plasticwise.viewModel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private val signupViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val displayName = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && displayName.isNotEmpty()) {
                signupViewModel.register(email, displayName, password)
                    .observe(viewLifecycleOwner, Observer { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    isLoading(true)
                                }

                                is Result.Success -> {
                                    isLoading(false)
                                    // Setelah berhasil register dengan API pribadi, langsung login dengan Firebase
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser
                                                val profileUpdates = UserProfileChangeRequest.Builder()
                                                    .setDisplayName(displayName)
                                                    .build()

                                                user?.updateProfile(profileUpdates)
                                                    ?.addOnCompleteListener { profileUpdateTask ->
                                                        if (profileUpdateTask.isSuccessful) {
                                                            showToast("Account created and logged in successfully")
                                                            val intent = Intent(requireActivity(), HomeActivity::class.java)
                                                            startActivity(intent)
                                                            requireActivity().finish()
                                                        } else {
                                                            Log.e("Update Profile", "Failed: ${profileUpdateTask.exception?.message}")
                                                            showToast("Failed to update profile")
                                                        }
                                                    }
                                            } else {
                                                Log.d("SignupFragment", "createUserWithEmail:failure", task.exception)
                                                showToast("Failed to create account in Firebase")
                                            }
                                        }
                                }

                                is Result.Error -> {
                                    isLoading(false)
                                    showToast("Failed to create account: ${result.error}")
                                }
                            }
                        }
                    })
            } else {
                showToast("Please fill all fields")
            }
        }

        binding.tvSignIn.setOnClickListener {
            navigateToLoginFragment()
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun navigateToLoginFragment() {
        val loginFragment = LoginFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                loginFragment,
                LoginFragment::class.java.simpleName
            )
            .addToBackStack(null)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
