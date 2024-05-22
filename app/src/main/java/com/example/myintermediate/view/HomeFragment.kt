package com.example.myintermediate.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.adapter.LoadingStateAdapter
import com.example.myintermediate.adapter.StoryAdapter
import com.example.myintermediate.databinding.FragmentHomeBinding
import com.example.myintermediate.viewModel.HomeFragmentViewModel


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentHomeBinding
    private val homeFragmentViewModel by viewModels<HomeFragmentViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var storyAdapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragmentViewModel.getSession().observe(requireActivity()) { session ->

            val username = session.username
            binding.tvUser.text = username
            Log.d("HomeFragment", "this name $username")
        }
        setupData()
        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireActivity(), UploadActivity::class.java)
            startActivity(intent)
        }

        binding.rvHome.layoutManager = LinearLayoutManager(requireActivity())

        binding.fabMap.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
            startActivity(intent)
        }
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.fabMap, View.TRANSLATION_Y, -180f).apply {
            duration = 500
        }.start()
    }

    private fun setupData() {
        storyAdapter = StoryAdapter()
        binding.rvHome.adapter = storyAdapter
        binding.rvHome.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        storyAdapter.addLoadStateListener { loadState ->
            isLoading(loadState.source.refresh is LoadState.Loading)

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(requireContext(), "Error: ${it.error}", Toast.LENGTH_SHORT).show()
            }
        }
        homeFragmentViewModel.story.observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
            isLoading(false)
        }
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        setupData()
    }

}
