package com.capstone.plasticwise.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.FragmentProfileBinding
import com.capstone.plasticwise.viewModel.ProfileFragmentViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel by viewModels<ProfileFragmentViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

//        val user = auth.currentUser
//        if (user != null) {
//            binding.tvProfile.text = user.displayName
//            binding.tvEmail.text = user.email
//        }
        profileViewModel.getSession().observe(requireActivity()) { result ->
            val username = result.username
            binding.tvProfile.text = username
            val email = result.email
            binding.tvEmail.text = email
        }
    }
}