package com.capstone.plasticwise.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.adapter.CraftAdapter
import com.capstone.plasticwise.databinding.FragmentHomeBinding
import com.capstone.plasticwise.viewModel.HomeFragmentViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentHomeBinding
    private val homeFragmentViewModel by viewModels<HomeFragmentViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var pieChart: PieChart

    private lateinit var craftAdapter: CraftAdapter
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
            binding.tvName.text = getString(R.string.user, username)
            Log.d("HomeFragment", "this name $username")
        }

//        Glide.with(requireActivity())
//            .load(R.raw.anim_upload)
//            .circleCrop()
//            .into(binding.lottieAnimationView)
//        setupData()

        binding.rvHome.layoutManager = LinearLayoutManager(requireActivity())

//        playAnimation()

        setUpRecyclerView()


        setupPieChart()
//        setupBarChart()

//        val trailingIcon = LayoutInflater.from(requireActivity()).inflate(R.layout.trailing_icon, binding.searchBar, false) as ImageView
//        val layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        trailingIcon.layoutParams = layoutParams
//        binding.searchBar.addView(trailingIcon)
    }

    private fun setupPieChart() {

        var pieChart = binding.pieChart
        pieChart = pieChart

        val list: ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(81.2f, "Waste"))
        list.add(PieEntry(18.8f, "Plastic Waste"))

        val pieDataSet = PieDataSet(list, null)

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataSet.valueTextSize = 10f


        pieDataSet.valueTextColor = Color.BLUE

//        val pieData = PieData(pieDataSet)

        pieChart.data = PieData(pieDataSet)

        pieChart.description.text = ""

        pieChart.animateY(2000)


    }


    private fun setUpRecyclerView() {
        craftAdapter = CraftAdapter()
        binding.rvHome.adapter = craftAdapter

        homeFragmentViewModel.getAllCrafting().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showToast("Loading...")
                    }

                    is Result.Success -> {
                        val limitedStories = result.data
                        craftAdapter.submitList(limitedStories)
                    }

                    is Result.Error -> {
                        showToast(result.error)
                        Log.d("HomeFragment", result.error)
                    }
                }
            }
        }


//    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.fabMap, View.TRANSLATION_Y, -180f).apply {
//            duration = 500
//        }.start()
//    }

//    private fun setupData() {
//        storyAdapter = StoryAdapter()
//        binding.rvHome.adapter = storyAdapter
//        binding.rvHome.adapter = storyAdapter.withLoadStateFooter(
//            footer = LoadingStateAdapter {
//                storyAdapter.retry()
//            }
//        )
//        storyAdapter.addLoadStateListener { loadState ->
//            isLoading(loadState.source.refresh is LoadState.Loading)
//
//            val errorState = loadState.source.append as? LoadState.Error
//                ?: loadState.source.prepend as? LoadState.Error
//                ?: loadState.source.refresh as? LoadState.Error
//                ?: loadState.append as? LoadState.Error
//                ?: loadState.prepend as? LoadState.Error
//
//            errorState?.let {
//                Toast.makeText(requireContext(), "Error: ${it.error}", Toast.LENGTH_SHORT).show()
//            }
//        }
//        homeFragmentViewModel.story.observe(viewLifecycleOwner) {
//            storyAdapter.submitData(lifecycle, it)
//            isLoading(false)
//        }
//    }

//    private fun isLoading(loading: Boolean) {
//        if (loading) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

//    override fun onResume() {
//        super.onResume()
////        setupData()
//    }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
