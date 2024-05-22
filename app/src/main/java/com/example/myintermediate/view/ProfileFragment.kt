package com.example.myintermediate.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.databinding.FragmentProfileBinding
import com.example.myintermediate.viewModel.ProfileFragmentViewModel


class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel by viewModels<ProfileFragmentViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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

        profileViewModel.getSession().observe(requireActivity()) { result ->
            val username = result.username
            binding.tvProfile.text = username
            val email = result.email
            binding.tvEmail.text = email
        }
    }
}