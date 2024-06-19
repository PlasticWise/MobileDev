package com.capstone.plasticwise.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeFragmentViewModel by viewModels<HomeFragmentViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var pieChart: PieChart
    private lateinit var craftAdapter: CraftAdapter

    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragmentViewModel.getSession().observe(viewLifecycleOwner) { session ->
            val username = session.username
            binding.tvName.text = getString(R.string.user, username)
            Log.d("HomeFragment", "this name $username")
        }

        auth = FirebaseAuth.getInstance()

        binding.btnCraft.setOnClickListener{
            (activity as HomeActivity).findViewById<BottomNavigationView>(R.id.nav_view)
                .selectedItemId = R.id.nav_detect
        }
        Glide.with(this)
            .load(R.drawable.ic_profile_user)
            .circleCrop()
            .into(binding.ivUser)
        binding.rvHome.layoutManager = LinearLayoutManager(requireActivity())
        setUpRecyclerView()
        setupPieChart()
    }

    private fun setupPieChart() {
        val pieChart = binding.pieChart
        val list = arrayListOf(
            PieEntry(81.2f, "Waste"),
            PieEntry(18.8f, "Plastic Waste")
        )

        val pieDataSet = PieDataSet(list, null)
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataSet.valueTextSize = 10f
        pieDataSet.valueTextColor = Color.GREEN

        pieChart.data = PieData(pieDataSet)
        pieChart.description.text = ""
        pieChart.animateY(2000)
    }

    private fun setUpRecyclerView() {
        craftAdapter = CraftAdapter()
        binding.rvHome.adapter = craftAdapter

        homeFragmentViewModel.allCrafting.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showToast("Loading...")
                }
                is Result.Success -> {
                    val limitedStories = result.data
                    craftAdapter.submitList(limitedStories)
                }
                is Result.Error -> {
                    showToast("Please Login Again")
                    startActivity(Intent(requireActivity(), UserActivity::class.java))
                    requireActivity().finish()
                    auth.signOut()
                    Log.d("HomeFragment", result.error)
                }
            }
        }

        homeFragmentViewModel.getAllCrafting()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
