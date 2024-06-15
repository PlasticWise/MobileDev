package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.adapter.PostAdapter
import com.capstone.plasticwise.adapter.StoryAdapter
import com.capstone.plasticwise.databinding.FragmentDetectBinding
import com.capstone.plasticwise.databinding.FragmentPostBinding
import com.capstone.plasticwise.viewModel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding

    private val postViewModel by viewModels<PostViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabAddPost: FloatingActionButton = view.findViewById(R.id.fabAddPost)

        fabAddPost.setOnClickListener {
            val intent = Intent(requireActivity(), UploadActivity::class.java)
            startActivity(intent)
        }


        binding.rvPost.layoutManager = LinearLayoutManager(requireActivity())

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter()
        binding.rvPost.adapter = postAdapter

        postViewModel.getAllPost().observe(viewLifecycleOwner, Observer {result ->
            if (result!= null) {
                when(result) {
                    is Result.Loading -> {
                        showToast("Loading..")
                    }
                    is Result.Success -> {
                        showToast("Success...")
                        val responsePost = result.data.responsePostUser
                        postAdapter.submitList(responsePost)
                    }
                    is Result.Error -> {
                        showToast("Error...")
                    }
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()

    }
}
