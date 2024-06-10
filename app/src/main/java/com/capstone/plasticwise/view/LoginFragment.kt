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
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.data.pref.UserModel
import com.capstone.plasticwise.data.remote.ApiConfig
import com.capstone.plasticwise.databinding.FragmentLoginBinding
import com.capstone.plasticwise.di.Injection
import com.capstone.plasticwise.viewModel.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val idToken = tokenTask.result?.token
                                Log.d("LOGINFRAGMENT", "User ID: $idToken")
                            }
                        }
                        loginActivity(email, password)
                    } else {
                        showToast("Login failed: ${task.exception?.message}")
                    }
                }.addOnFailureListener {
                    showToast("Login failed: ${it.message}")
                    binding.passwordEditText.text?.clear()
                    binding.emailEditText.text?.clear()
                }
            } else {
                showToast("Please fill all fields")
            }
        }

        binding.tvSignUp.setOnClickListener {
            val signupFragment = SignupFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, signupFragment, SignupFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        }

        setupListener()
    }

    private fun loginActivity(email : String, password : String) {
        loginViewModel.login(email, password).observe(requireActivity()) { result ->
            if (result!= null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val token = result.data.loginResult.token
                        val username = result.data.loginResult.name
                        loginViewModel.saveSession(
                            UserModel(
                                email,
                                token,
                                username,
                                true
                            )
                        )
                        val userRepository = Injection.provideRepository(requireActivity())
                        userRepository.update(ApiConfig.getApiService(token))
                        MaterialAlertDialogBuilder(requireActivity())
                            .setTitle("Login Success")
                            .setMessage("Welcome ${result.data.loginResult.name}")
                            .setPositiveButton("Next") { _, _ ->
                                val intent =
                                    Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                            .show()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun setupListener() {
        binding.tvForgotPassword.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Password reset email sent")
                    } else {
                        showToast(task.exception?.message.toString())
                    }
                }
            } else {
                showToast("Please enter your email")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}
