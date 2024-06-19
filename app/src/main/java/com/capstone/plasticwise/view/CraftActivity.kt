package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityCraftBinding
import com.capstone.plasticwise.viewModel.CraftViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

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
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("navigate_to", "detectFragment")
            startActivity(intent)
            finish()
        }

        val categories = intent.getStringExtra(EXTRA_CATEGORIES)

        val id = intent.getStringExtra(EXTRA_ID)

        if (categories != null) {
            getDetailCraftByCategories(categories.toString())
        } else {
            getDetailCraftById(id.toString())
        }


//        binding.btnCraft.setOnClickListener{
//            findNavController().navigate(R.id.action_nav_craft_to_nav_detect)
//        }
    }

    private fun getDetailCraftByCategories(categories: String) {
        craftViewModel.getDetailCraftByCategories(categories).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showToast("Loading..")
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showToast("Success")
                        showLoading(false)
                        val title = result.data.title
                        val tools = result.data.tools
                        val image = result.data.imageUrl
                        val equip = result.data.equip
                        val howto = result.data.howto
                        val type = result.data.type
                        showDetail(title, tools, image, equip, howto, type)
                    }

                    is Result.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getDetailCraftById(id: String) {
        craftViewModel.getDetailCraft(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val title = result.data.title
                        val tools = result.data.tools
                        val image = result.data.imageUrl
                        val equip = result.data.equip
                        val howto = result.data.howto
                        val type = result.data.type
                        showDetail(title, tools, image, equip, howto, type)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                        Log.d("Craft Activity", result.error)
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

    private fun showDetail(
        title: String,
        tools: List<String>,
        image: String,
        equip: List<String>,
        howto: List<String>,
        type: String
    ) {
        binding.apply {
            tvTitleCraft.text = title
            tvType.text = type
            tvEquip.text = formatListWithNumbers(equip)
            tvTools.text = formatListWithNumbers(tools)
            tvHowTo.text = formatListWithNumbers(howto)
            containerType.visibility = View.VISIBLE
            containerEquip.visibility = View.VISIBLE
            containerTools.visibility = View.VISIBLE
            containerHowTo.visibility = View.VISIBLE
        }
        Glide.with(this)
            .load(image)
            .into(binding.ivCraft)

    }

    private fun formatListWithNumbers(list: List<String>): String {
        return list.mapIndexed { index, item -> "${index + 1}. $item" }.joinToString("\n")

    }


    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_CATEGORIES = "extra_categories"
    }
}
