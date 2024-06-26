package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.adapter.PostAdapter
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

        val menuOverflow = view.findViewById<ImageView>(R.id.menu_overflow)
        menuOverflow.setOnClickListener { showPopupMenu(menuOverflow) }


        binding.rvPost.layoutManager = LinearLayoutManager(requireActivity())

        setupRecyclerView()

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.post_overflow_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_list_view -> {
                    changeRecyclerViewLayout(false)
                    true
                }
                R.id.menu_grid_view -> {
                    changeRecyclerViewLayout(true)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun changeRecyclerViewLayout(isGridView: Boolean) {
        val layoutManager = if (isGridView) {
            GridLayoutManager(requireContext(), 2)
        } else {
            LinearLayoutManager(requireContext())
        }
        binding.rvPost.layoutManager = layoutManager
    }
    private fun setupRecyclerView() {
        postAdapter = PostAdapter()
        binding.rvPost.adapter = postAdapter

        postViewModel.getAllPost().observe(viewLifecycleOwner, Observer {result ->
            if (result!= null) {
                when(result) {
                    is Result.Loading -> {
                        showLoading(true)
                        showToast("Loading..")
                    }
                    is Result.Success -> {
                        showToast("Success...")
                        showLoading(false)
                        val responsePost = result.data
                        postAdapter.submitList(responsePost)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast("Error...")
                    }
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()

    }
}
