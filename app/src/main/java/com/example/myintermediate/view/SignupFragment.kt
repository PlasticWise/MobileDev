package com.example.myintermediate.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myintermediate.Result
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.databinding.FragmentSignupBinding
import com.example.myintermediate.viewModel.RegisterViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentSignupBinding
    private val signupViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uploadData()
    }

    private fun uploadData() {
        val nama = binding.nameEditText.text
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text
        binding.signupButton.setOnClickListener {
            signupViewModel.register(
                nama.toString(), email.toString(),
                password.toString()
            ).observe(requireActivity()) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showToast("Lagi Loading ya!!")
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            MaterialAlertDialogBuilder(requireActivity())
                                .setTitle("Account Created")
                                .setMessage("Account has been created, please login")
                                .setPositiveButton("Login") { _, _ ->
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
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }
}