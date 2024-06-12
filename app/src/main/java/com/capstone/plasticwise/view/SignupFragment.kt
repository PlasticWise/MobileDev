package com.capstone.plasticwise.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.FragmentSignupBinding
import com.capstone.plasticwise.viewModel.RegisterViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val nama = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && nama.isNotEmpty()) {
                auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result.signInMethods ?: emptyList<String>()
                        if (signInMethods.isNotEmpty()) {
                            showToast("Account has been created")
                        } else {
                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { createTask ->
                                if (createTask.isSuccessful) {
                                    navigateToLoginFragment()
                                    showToast("Account Created")
                                    signupViewModel.register(nama, email, password).observe(requireActivity()){result ->
                                        if (result != null){
                                            when (result) {
                                                is Result.Loading -> {
                                                    isLoading(true)
                                                }
                                                is Result.Success -> {
                                                    isLoading(false)
                                                    MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                                                        .setTitle("Success")
                                                        .setMessage("Account Created")
                                                        .setPositiveButton("Next") { dialog, _ ->
                                                            dialog.dismiss()
                                                            navigateToLoginFragment()
                                                        }
                                                }
                                                is Result.Error -> {
                                                    isLoading(false)
                                                    showToast(result.error)
                                                }
                                            }
                                        }
                                    }
//                                    val user = auth.currentUser
//                                    val profileUpdates = UserProfileChangeRequest.Builder()
//                                        .setDisplayName(nama)
//                                        .build()
//                                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileUpdateTask ->
//                                        if (profileUpdateTask.isSuccessful) {
//                                            showToast("Profile Updated")
//                                            showToast("Account Created")
//                                            auth.signOut()
//                                            navigateToLoginFragment()
//                                        }
//                                    }
                                } else {
                                    showToast(createTask.exception?.message.toString())
                                }
                            }
                        }
                    } else {
                        showToast(task.exception?.message.toString())
                    }
                }
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
            .replace(R.id.fragment_container, loginFragment, LoginFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
