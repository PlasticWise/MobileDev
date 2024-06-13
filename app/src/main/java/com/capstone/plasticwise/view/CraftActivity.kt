package com.capstone.plasticwise.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityCraftBinding
import com.capstone.plasticwise.viewModel.CraftViewModel

class CraftActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCraftBinding

    private val craftViewModel by viewModels<CraftViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCraftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        val btnCraft = findViewById<Button>(R.id.btnCraft)
        btnCraft.setOnClickListener {
            val detectFragment = DetectFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragDetect, detectFragment)
                .commit()
        }

        val id = intent.getStringExtra(EXTRA_ID)

        craftViewModel.getDetailCraft(id.toString()).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
//                        showLoading(true)
                    }

                    is Result.Success -> {
//                        showLoading(false)
                        val title = result.data.title
                        val tools = result.data.tools
                        val image = result.data.imageUrl
                        val equip = result.data.equip
                        val howto = result.data.howto
                        showDetail(title, tools, image, equip, howto)
                    }

                    is Result.Error -> {
//                        showLoading(false)
//                        showToast(result.error)
                        Log.d("Craft Activity", result.error)
                    }
                }
            }
        }


//        binding.btnCraft.setOnClickListener{
//            findNavController().navigate(R.id.action_nav_craft_to_nav_detect)
//        }
    }

    private fun showDetail(title: String, tools: List<String>, image: String, equip: List<String>, howto: List<String>) {
        binding.apply {
            tvTitleCraft.text = title

            tvTools.text = formatListWithNumbers(tools)
            tvHowTo.text = formatListWithNumbers(howto)

        }
            Glide.with(this)
                .load(image)
                .into(binding.ivCraft)

    }

    private fun formatListWithNumbers(list: List<String>): String {
        return list.mapIndexed { index, item -> "${index + 1}. $item"}.joinToString("\n")

    }


    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
